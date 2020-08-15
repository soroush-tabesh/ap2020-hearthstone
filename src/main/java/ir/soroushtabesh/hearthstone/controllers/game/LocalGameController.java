package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.util.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LocalGameController extends GameController {

    private final Long[] tokens = new Long[2];
    private final PlayerController[] playerControllers = new PlayerController[2];
    private int playerIdCounter = 0;

    @Override
    public int token2playerId(long token) {
        if (tokens[0] == token)
            return 0;
        else if (tokens[1] == token)
            return 1;
        else
            return -1;
    }

    @Override
    public PlayerController registerPlayer(Player player, Hero hero, Deck deck, InfoPassive infoPassive, boolean shuffle) {
        PlayerController playerController = getNewPlayerController(player);
        if (playerController == null)
            return null;
        seedPlayerData(playerController, player, hero, deck, infoPassive, shuffle);
        checkForGameStart();
        return playerController;
    }

    @Override
    public PlayerController registerPlayer(Player player, Hero hero, Deck deck, InfoPassive infoPassive, List<Card> cardOrder) {
        PlayerController playerController = getNewPlayerController(player);
        if (playerController == null)
            return null;
        seedPlayerData(playerController, player, hero, deck, infoPassive, false);
        getModelPool().getPlayerDataById(playerController.getPlayerId()).setCardByOrder(cardOrder);
        checkForGameStart();
        return playerController;
    }

    private PlayerController getNewPlayerController(Player player) {
        if (playerIdCounter >= 2)
            return null;
        int playerId = playerIdCounter++;
        System.out.println("tst " + player.getUsername() + " " + (tokens[playerId] = PlayerManager.getInstance().getTokenOf(player.getUsername())));
        playerControllers[playerId] = new PlayerController(this
                , tokens[playerId]);
        return playerControllers[playerId];
    }

    private void checkForGameStart() {
        if (playerIdCounter >= 2) {
            setGameReady(true);
            handInitiation(0);
            handInitiation(1);
        }
    }

    private void handInitiation(int playerId) {
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        playerData.getHandCard().clear();
        for (int i = 0; i < 3; i++) {
            drawToHand(playerData);
            playerData.getChangeCardFlag().add(true);
        }
    }

    @Override
    public Message changeCard(int cardNumberInList, long token) {
        if (!checkPlayerValidity(token) || !isGameReady() || isStarted())
            return Message.ERROR;
        int playerId = token2playerId(token);
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        if (playerData.isReady())
            return Message.ERROR;
        if (playerData.getChangeCardFlag().size() <= cardNumberInList || cardNumberInList < 0)
            return Message.ERROR;
        if (playerData.getChangeCardFlag().get(cardNumberInList)) {
            CardObject card = playerData.getHandCard().remove(cardNumberInList);
            playerData.getChangeCardFlag().set(cardNumberInList, false);
            drawToHand(playerData, cardNumberInList);
            playerData.getDeckCard().add(new Random().nextInt(playerData.getDeadCard().size() + 1), card);
        } else {
            return Message.IMPOSSIBLE;
        }
        return Message.SUCCESS;
    }

    public Message drawToHand(ModelPool.PlayerData playerData) {
        return drawToHand(playerData, 0);
    }

    public Message drawToHand(ModelPool.PlayerData playerData, int index) {
        if (playerData.getDeckCard().isEmpty())
            return Message.INSUFFICIENT;
        CardObject cardObject = playerData.getDeckCard().remove(0);
        if (playerData.getHandCard().size() >= 12) {
            playerData.getBurnedCard().add(cardObject);
            return Message.FULL;
        } else {
            playerData.getHandCard().add(index, cardObject);
            return Message.SUCCESS;
        }
    }

    public Message drawToHand(ModelPool.PlayerData playerData, int index, Class<? extends CardObject> targetClass) {
        if (playerData.getDeckCard().isEmpty())
            return Message.INSUFFICIENT;
        CardObject cardObject = null;
        for (CardObject object : playerData.getDeckCard()) {
            if (targetClass.isAssignableFrom(object.getClass()))
                cardObject = object;
        }
        if (cardObject == null)
            return Message.IMPOSSIBLE;
        if (playerData.getHandCard().size() >= 12) {
            playerData.getBurnedCard().add(cardObject);
            return Message.FULL;
        } else {
            playerData.getHandCard().add(index, cardObject);
            return Message.SUCCESS;
        }
    }

    @Override
    public PlayerController[] getAllPlayerControllers() {
        return playerControllers;
    }

    private void seedPlayerData(PlayerController playerController, Player player, Hero hero, Deck deck, InfoPassive infoPassive, boolean shuffle) {
        ModelPool.PlayerData playerData = new ModelPool.PlayerData(playerController.getPlayerId(),
                this, player, hero, deck, infoPassive, shuffle);
        getModelPool().addPlayerData(playerData);
    }

    private boolean checkPlayerValidityAndTurn(long token) {
        return getTurn() == token2playerId(token);
    }

    private boolean checkPlayerValidity(long token) {
        return token2playerId(token) != -1;
    }


    @Override
    public boolean endTurn(long token) {
        if (!checkPlayerValidityAndTurn(token) || !isStarted())
            return false;
        int playerId = token2playerId(token);
        List<ModelPool.PlayerData> playerDataList = getModelPool().getPlayerDataList();
        if (!playerDataList.get(playerId).isReady())
            return false;
        logEvent(new GameAction.EndTurn(playerId));
        getScriptEngine().broadcastEventOnPlayer(GenericScript.TURN_END, playerId);
        turnStartRoutine(1 - playerId);
        return false;
    }

    @Override
    public Message startGame(long token) {
        if (!checkPlayerValidity(token))
            return Message.ERROR;
        int playerId = token2playerId(token);
        getModelPool().getPlayerDataById(playerId).setReady(true);
        List<ModelPool.PlayerData> playerDataList = getModelPool().getPlayerDataList();
        if (playerDataList.size() == 2 && playerDataList.get(0).isReady() && playerDataList.get(1).isReady()) {
            setStarted(true);
            turnStartRoutine(0);
            logEvent(new GameAction.GameStart());
        }
        return Message.SUCCESS;
    }

    private void turnStartRoutine(int playerId) {
        List<ModelPool.PlayerData> playerDataList = getModelPool().getPlayerDataList();
        setTurn(playerId);
        ModelPool.PlayerData playerData = playerDataList.get(playerId);
        playerData.setMyTurn(true);
        playerDataList.get(1 - playerId).setMyTurn(false);
        resetMana(playerData);
        drawToHand(playerData);
        getScriptEngine().broadcastEventOnPlayer(GenericScript.TURN_START, playerId);
    }

    private void resetMana(ModelPool.PlayerData playerData) {
        playerData.setManaMax(playerData.getManaMax() + (playerData.getManaMax() < 10 ? 1 : 0));
        playerData.setMana(playerData.getManaMax());
        System.out.println("mana max " + playerData.getManaMax());
        System.out.println("mana " + playerData.getMana());
    }

    @Override
    public Message playCard(CardObject cardObject, int groundIndex, GameObject optionalTarget, long token) {
        if (!checkPlayerValidityAndTurn(token) || !isStarted())
            return Message.ERROR;
        int playerId = token2playerId(token);
        if (cardObject instanceof HeroPowerObject)
            return useHeroPower(getModelPool().getPlayerDataById(cardObject.getPlayerId()).getHero()
                    , optionalTarget, token);
        //check validity of parameters
        if (cardObject.getPlayerId() != playerId)
            return Message.ERROR;
        if (cardObject.getCardModel().getActionType() == Card.ActionType.TARGETED && optionalTarget == null)
            return Message.ERROR;
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        if (groundIndex > playerData.getGroundCard().size() || groundIndex < 0)
            return Message.ERROR;
        if (!playerData.getHandCard().contains(cardObject))
            return Message.ERROR;
        if (!withdrawMana(playerData, cardObject.getManaCost()))
            return Message.INSUFFICIENT;

        boolean res = false;
        if (cardObject instanceof MinionObject) {
            if (res = playCardMinion((MinionObject) cardObject, groundIndex, optionalTarget, playerId, token)) {
                playerData.getHandCard().remove(cardObject);
                playerData.getGroundCard().add(groundIndex, (MinionObject) cardObject);
            }
        } else if (cardObject instanceof SpellObject) {
            if (res = playCardSpell((SpellObject) cardObject, groundIndex, optionalTarget, playerId, token)) {
                playerData.getHandCard().remove(cardObject);
            }
        } else if (cardObject instanceof WeaponObject) {
            if (res = playCardWeapon(((WeaponObject) cardObject), groundIndex, optionalTarget, playerId, token)) {
                playerData.getHandCard().remove(cardObject);
            }
        }

        if (res) {
            logEvent(new GameAction.CardPlay(cardObject));
            getScriptEngine().broadcastEventOnObject(cardObject, GenericScript.CARD_PLAY);
            return Message.SUCCESS;
        } else {
            //return mana back
            depositMana(playerData, cardObject.getCardModel().getMana());
            return Message.ERROR;
        }
    }

    private boolean playCardMinion(MinionObject minionObject, int groundIndex, GameObject optionalTarget, int playerId, long token) {
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        if (playerData.getGroundCard().size() >= 7)
            return false;
        minionObject.setSleep(true);
        Optional<Boolean> optional;
        if (minionObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
            optional = getScriptEngine().broadcastEventOnObject(minionObject, MinionBehavior.BATTLE_CRY);
        } else {
            optional = getScriptEngine().broadcastEventOnObject(minionObject, MinionBehavior.BATTLE_CRY, optionalTarget);
        }
        return optional.isEmpty() || optional.get();
    }

    private boolean playCardSpell(SpellObject spellObject, int groundIndex, GameObject optionalTarget, int playerId, long token) {
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        Optional<Boolean> optional;
        if (spellObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
            optional = getScriptEngine().broadcastEventOnObject(spellObject, SpellBehavior.SPELL_EFFECT);
            logEvent(new GameAction.SpellGlobal(spellObject));
        } else {
            optional = getScriptEngine().broadcastEventOnObject(spellObject, SpellBehavior.SPELL_EFFECT, optionalTarget);
            logEvent(new GameAction.TargetedAttack(spellObject, optionalTarget));
        }
        getScriptEngine().broadcastEventOnObject(spellObject, SpellBehavior.SPELL_DONE, optionalTarget);
        return optional.isEmpty() || optional.get();
    }

    private boolean playCardWeapon(WeaponObject weaponObject, int groundIndex, GameObject optionalTarget, int playerId, long token) {
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        playerData.getHero().setCurrentWeapon(weaponObject);
        return true;
    }

    @Override
    public Message summonMinion(MinionObject source, long token) {
        if (!checkPlayerValidity(token) || !isStarted())
            return Message.ERROR;
        int playerId = token2playerId(token);
        if (source.getPlayerId() != playerId)
            return Message.ERROR;
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        playerData.getGroundCard().add(source);
        getScriptEngine().broadcastEventOnObject(source, GenericScript.CARD_PLAY);
        source.setSleep(true);
        return Message.SUCCESS;
    }

    /**
     * bidirectional operation
     */
    @Override
    public Message playMinion(MinionObject source, GameObject target, long token) {
        if (!checkPlayerValidityAndTurn(token) || !isStarted())
            return Message.ERROR;
        int playerId = token2playerId(token);
        if (source.getPlayerId() != playerId)
            return Message.ERROR;
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        if (!playerData.getGroundCard().contains(source) || source.isSleep())
            return Message.IMPOSSIBLE;
        if (target.getPlayerId() == playerId)
            return Message.IMPOSSIBLE;
        if (hasTaunt(1 - playerId) &&
                ((target instanceof MinionObject
                        && !((MinionObject) target).hasTaunt())
                        || target instanceof HeroObject))
            return Message.IMPOSSIBLE;
        if (target instanceof MinionObject) {
            if (((MinionObject) target).hasStealth() && target.getPlayerId() != playerId)
                return Message.IMPOSSIBLE;
            singleDirectionMinionAttack(source, target, token);
            singleDirectionMinionAttack((MinionObject) target, source, token);
            logEvent(new GameAction.MinionAttack(source, (MinionObject) target));
        } else if (target instanceof HeroObject) {
            if (!source.getCanAttackHero())
                return Message.IMPOSSIBLE;
//            forceUseWeapon(((HeroObject) target), source, playerId, token);
            singleDirectionMinionAttack(source, target, token);
            logEvent(new GameAction.MinionAttack(source, (HeroObject) target));
        } else {
            return Message.ERROR;
        }
        source.setSleep(true);
        return Message.SUCCESS;
    }

    /**
     * mono-directional operation
     */
    public Message performDamageOnMinion(MinionObject target, int amount, long token) {
        if (!checkPlayerValidity(token) || !isStarted())
            return Message.ERROR;
        if (amount <= 0)
            return Message.ERROR;
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(target.getPlayerId());
        if (!playerData.getGroundCard().contains(target))
            return Message.IMPOSSIBLE;
        target.setHp(target.getHp() - amount);
        getScriptEngine().broadcastEventOnObject(target, MinionBehavior.DAMAGE_TAKEN);
        if (target.getHp() <= 0) {
            target.setDead(true);
            playerData.getGroundCard().remove(target);
            playerData.getDeadCard().add(target);
            getScriptEngine().broadcastEventOnObject(target, MinionBehavior.DEATH_RATTLE);
        }
        return Message.SUCCESS;
    }

    /**
     * mono-directional operation
     */
    public Message performDamageOnHero(HeroObject target, int amount, long token) {
        if (!checkPlayerValidity(token) || !isStarted())
            return Message.ERROR;
        int playerId = token2playerId(token);
        if (amount <= 0)
            return Message.ERROR;
        if (target.getImmune() > 0)
            return Message.IMPOSSIBLE;
        target.setHp(target.getHp() - Math.max(amount - target.getShield(), 0));
        target.setShield(Math.max(target.getShield() - amount, 0));
        System.err.println(target.getHp());
        getScriptEngine().broadcastEventOnObject(target, HeroBehavior.DAMAGE_TAKEN);
        if (target.getHp() <= 0) {
            setWinner(1 - playerId);
            setStarted(false);
            ModelPool.PlayerData pd1 = getModelPool().getPlayerDataById(playerId);
            PlayerStats ps1 = pd1.getPlayer().getPlayerStats();
            ps1.setCupCount(Math.max(0, ps1.getCupCount() - (playerId == 0 ? 1 : -1)));
            ModelPool.PlayerData pd2 = getModelPool().getPlayerDataById(playerId);
            PlayerStats ps2 = pd2.getPlayer().getPlayerStats();
            ps1.setCupCount(Math.max(0, ps2.getCupCount() + (playerId == 0 ? 1 : -1)));
        }
        return Message.SUCCESS;
    }

    private boolean hasTaunt(int playerId) {
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        boolean flag = false;
        for (MinionObject minionObject : playerData.getGroundCard()) {
            flag |= minionObject.hasTaunt() && !minionObject.hasStealth();
        }
        return flag;
    }

    /**
     * bidirectional operation
     */
    @Override
    public Message useWeapon(HeroObject source, GameObject target, long token) {
        if (!checkPlayerValidityAndTurn(token) || !isStarted())
            return Message.ERROR;
        return forceUseWeapon(source, target, token);
    }

    @Override
    public Message useHeroPower(HeroObject source, GameObject target, long token) {
        if (!checkPlayerValidityAndTurn(token) || !isStarted())
            return Message.ERROR;
        int playerId = token2playerId(token);
        HeroPowerObject cardObject = source.getHeroPower();
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);

        if (cardObject.isUsed())
            return Message.IMPOSSIBLE;

        if (!withdrawMana(playerData, cardObject.getManaCost()))
            return Message.INSUFFICIENT;

        Optional<Boolean> optional = Optional.empty();
        if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
            optional = getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_EFFECT);
            logEvent(new GameAction.HeroPower(cardObject));
        } else {
            optional = getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_EFFECT, target);
            logEvent(new GameAction.TargetedAttack(cardObject, target));
        }
        if (optional.isEmpty() || optional.get()) {
            cardObject.setUsed(true);
            getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_DONE, target);
            return Message.SUCCESS;
        } else {
            depositMana(playerData, cardObject.getCardModel().getMana());
            return Message.ERROR;
        }
    }

    private boolean withdrawMana(ModelPool.PlayerData playerData, int amount) {
        if (playerData.getMana() < amount)
            return false;
        playerData.setMana(playerData.getMana() - amount);
        return true;
    }

    private void depositMana(ModelPool.PlayerData playerData, int amount) {
        playerData.setMana(playerData.getMana() + amount);
    }

    private Message forceUseWeapon(HeroObject source, GameObject target, long token) {
        if (!checkPlayerValidity(token) || !isStarted())
            return Message.ERROR;
        int playerId = token2playerId(token);
        if (source == target)
            return Message.IMPOSSIBLE;
        WeaponObject weapon = source.getCurrentWeapon();
        if (weapon == null)
            return Message.ERROR;
        if (target instanceof MinionObject) {
            if (((MinionObject) target).hasStealth() && target.getPlayerId() != playerId)
                return Message.IMPOSSIBLE;
            singleDirectionWeapon(source, target, token);
            singleDirectionMinionAttack((MinionObject) target, source, token);
        } else if (target instanceof HeroObject) {
            singleDirectionWeapon(source, target, token);
            singleDirectionWeapon((HeroObject) target, source, token);
        } else {
            return Message.IMPOSSIBLE;
        }
        weapon.setDurability(weapon.getDurability() - 1);
        if (weapon.getDurability() <= 0) {
            source.setCurrentWeapon(null);
        }
        logEvent(new GameAction.TargetedAttack(weapon, target));
        return Message.SUCCESS;
    }

    private void singleDirectionWeapon(HeroObject source, GameObject target, long token) {
        if (source.getCurrentWeapon() == null)
            return;
        if (target instanceof MinionObject) {
            getScriptEngine().broadcastEventOnObject(source, HeroBehavior.ATTACK_EFFECT, target);
            performDamageOnMinion((MinionObject) target, source.getCurrentWeapon().getAttackPower(), token);
        } else if (target instanceof HeroObject) {
            getScriptEngine().broadcastEventOnObject(source, HeroBehavior.ATTACK_EFFECT, target);
            performDamageOnHero((HeroObject) target, source.getCurrentWeapon().getAttackPower(), token);
        }
    }

    private void singleDirectionMinionAttack(MinionObject source, GameObject target, long token) {
        if (target instanceof MinionObject) {
            getScriptEngine().broadcastEventOnObject(source, MinionBehavior.ATTACK_EFFECT, target);
            performDamageOnMinion((MinionObject) target, source.getAttackPower(), token);
        } else if (target instanceof HeroObject) {
            getScriptEngine().broadcastEventOnObject(source, MinionBehavior.ATTACK_EFFECT, target);
            performDamageOnHero((HeroObject) target, source.getAttackPower(), token);
        }
    }

    @Override
    public void logEvent(GameAction gameAction) {
        Logger.log("game board", gameAction.getMessage());
        getModelPool().getSceneData().getLog().add(gameAction);
    }
}
