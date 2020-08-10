package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.util.Logger;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LocalGameController extends GameController {

    private final int[] tokens = new int[2];
    private final SecureRandom random = new SecureRandom();
    private final PlayerController[] playerControllers = new PlayerController[2];
    private int playerIdCounter = 0;

    @Override
    public PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive, boolean shuffle) {
        PlayerController playerController = getNewPlayerController();
        if (playerController == null)
            return null;
        seedPlayerData(playerController, hero, deck, infoPassive, shuffle);
        checkForGameStart();
        return playerController;
    }

    @Override
    public PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive, List<Card> cardOrder) {
        PlayerController playerController = getNewPlayerController();
        if (playerController == null)
            return null;
        seedPlayerData(playerController, hero, deck, infoPassive, false);
        getModelPool().getPlayerDataById(playerController.getId()).setCardByOrder(cardOrder);
        checkForGameStart();
        return playerController;
    }

    private PlayerController getNewPlayerController() {
        if (playerIdCounter >= 2)
            return null;
        int playerId = playerIdCounter++;
        tokens[playerId] = random.nextInt();
        playerControllers[playerId] = new PlayerController(this
                , tokens[playerId], playerId);
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
    protected Message changeCard(int cardNumberInList, int playerId, int token) {
        if (!checkPlayerValidity(playerId, token) || !isGameReady() || isStarted())
            return Message.ERROR;
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
    protected PlayerController[] getAllPlayerControllers() {
        return playerControllers;
    }

    private void seedPlayerData(PlayerController playerController, Hero hero, Deck deck, InfoPassive infoPassive, boolean shuffle) {
        ModelPool.PlayerData playerData = new ModelPool.PlayerData(playerController.getId(),
                this, hero, deck, infoPassive, shuffle);
        getModelPool().addPlayerData(playerData);
    }

    private boolean checkPlayerValidityAndTurn(int playerId, int token) {
        return tokens[playerId] == token && getTurn() == playerId;
    }

    private boolean checkPlayerValidity(int playerId, int token) {
        return tokens[playerId] == token;
    }


    @Override
    protected boolean endTurn(int playerId, int token) {
        if (!checkPlayerValidityAndTurn(playerId, token) || !isStarted())
            return false;
        List<ModelPool.PlayerData> playerDataList = getModelPool().getPlayerDataList();
        if (!playerDataList.get(playerId).isReady())
            return false;
        logEvent(new GameAction.EndTurn(playerId));
        getScriptEngine().broadcastEventOnPlayer(GenericScript.TURN_END, playerId);
        turnStartRoutine(1 - playerId);
        return false;
    }

    @Override
    protected Message startGame(int playerId, int token) {
        if (!checkPlayerValidity(playerId, token))
            return Message.ERROR;
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
    }

    @Override
    protected Message playCard(CardObject cardObject, int groundIndex, GameObject optionalTarget, int playerId, int token) {
        if (!checkPlayerValidityAndTurn(playerId, token) || !isStarted())
            return Message.ERROR;
        if (cardObject instanceof HeroPowerObject)
            return useHeroPower(getModelPool().getPlayerDataById(cardObject.getPlayerId()).getHero()
                    , optionalTarget, playerId, token);
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

    private boolean playCardMinion(MinionObject minionObject, int groundIndex, GameObject optionalTarget, int playerId, int token) {
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

    private boolean playCardSpell(SpellObject spellObject, int groundIndex, GameObject optionalTarget, int playerId, int token) {
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

    private boolean playCardWeapon(WeaponObject weaponObject, int groundIndex, GameObject optionalTarget, int playerId, int token) {
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        playerData.getHero().setCurrentWeapon(weaponObject);
        return true;
    }

    @Override
    public Message summonMinion(MinionObject source, int playerId, int token) {
        if (!checkPlayerValidity(playerId, token) || !isStarted())
            return Message.ERROR;
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
    protected Message playMinion(MinionObject source, GameObject target, int playerId, int token) {
        if (!checkPlayerValidityAndTurn(playerId, token) || !isStarted())
            return Message.ERROR;
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
            singleDirectionMinionAttack(source, target, playerId, token);
            singleDirectionMinionAttack((MinionObject) target, source, playerId, token);
            logEvent(new GameAction.MinionAttack(source, (MinionObject) target));
        } else if (target instanceof HeroObject) {
            if (!source.getCanAttackHero())
                return Message.IMPOSSIBLE;
//            forceUseWeapon(((HeroObject) target), source, playerId, token);
            singleDirectionMinionAttack(source, target, playerId, token);
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
    public Message performDamageOnMinion(MinionObject target, int amount, int playerId, int token) {
        if (!checkPlayerValidity(playerId, token) || !isStarted())
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
    public Message performDamageOnHero(HeroObject target, int amount, int playerId, int token) {
        if (!checkPlayerValidity(playerId, token) || !isStarted())
            return Message.ERROR;
        if (amount <= 0)
            return Message.ERROR;
        if (target.getImmune() > 0)
            return Message.IMPOSSIBLE;
        target.setHp(target.getHp() - Math.max(amount - target.getShield(), 0));
        target.setShield(Math.max(target.getShield() - amount, 0));
        getScriptEngine().broadcastEventOnObject(target, HeroBehavior.DAMAGE_TAKEN);
        if (target.getHp() <= 0) {
            setWinner(1 - playerId);
            setStarted(false);
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
    protected Message useWeapon(HeroObject source, GameObject target, int playerId, int token) {
        if (!checkPlayerValidityAndTurn(playerId, token) || !isStarted())
            return Message.ERROR;
        return forceUseWeapon(source, target, playerId, token);
    }

    @Override
    protected Message useHeroPower(HeroObject source, GameObject target, int playerId, int token) {
        if (!checkPlayerValidityAndTurn(playerId, token) || !isStarted())
            return Message.ERROR;
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

    private Message forceUseWeapon(HeroObject source, GameObject target, int playerId, int token) {
        if (!checkPlayerValidity(playerId, token) || !isStarted())
            return Message.ERROR;
        if (source == target)
            return Message.IMPOSSIBLE;
        WeaponObject weapon = source.getCurrentWeapon();
        if (weapon == null)
            return Message.ERROR;
        if (target instanceof MinionObject) {
            if (((MinionObject) target).hasStealth() && target.getPlayerId() != playerId)
                return Message.IMPOSSIBLE;
            singleDirectionWeapon(source, target, playerId, token);
            singleDirectionMinionAttack((MinionObject) target, source, playerId, token);
        } else if (target instanceof HeroObject) {
            singleDirectionWeapon(source, target, playerId, token);
            singleDirectionWeapon((HeroObject) target, source, playerId, token);
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

    private void singleDirectionWeapon(HeroObject source, GameObject target, int playerId, int token) {
        if (source.getCurrentWeapon() == null)
            return;
        if (target instanceof MinionObject) {
            getScriptEngine().broadcastEventOnObject(source, HeroBehavior.ATTACK_EFFECT, target);
            performDamageOnMinion((MinionObject) target, source.getCurrentWeapon().getAttackPower(), playerId, token);
        } else if (target instanceof HeroObject) {
            getScriptEngine().broadcastEventOnObject(source, HeroBehavior.ATTACK_EFFECT, target);
            performDamageOnHero((HeroObject) target, source.getCurrentWeapon().getAttackPower(), playerId, token);
        }
    }

    private void singleDirectionMinionAttack(MinionObject source, GameObject target, int playerId, int token) {
        if (target instanceof MinionObject) {
            getScriptEngine().broadcastEventOnObject(source, MinionBehavior.ATTACK_EFFECT, target);
            performDamageOnMinion((MinionObject) target, source.getAttackPower(), playerId, token);
        } else if (target instanceof HeroObject) {
            getScriptEngine().broadcastEventOnObject(source, MinionBehavior.ATTACK_EFFECT, target);
            performDamageOnHero((HeroObject) target, source.getAttackPower(), playerId, token);
        }
    }

    @Override
    protected void logEvent(GameAction gameAction) {
        Logger.log("game board", gameAction.getMessage());
        getModelPool().getSceneData().getLog().add(gameAction);
    }
}
