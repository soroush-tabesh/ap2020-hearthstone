package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import ir.soroushtabesh.hearthstone.util.Logger;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class LocalGameController extends GameController {

    private final int[] tokens = new int[2];
    private final SecureRandom random = new SecureRandom();
    private final PlayerController[] playerControllers = new PlayerController[2];
    private int playerIdCounter = 0;

    @Override
    public PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive) {
        if (playerIdCounter >= 2)
            return null;
        int playerId = playerIdCounter++;
        tokens[playerId] = random.nextInt();
        playerControllers[playerId] = new PlayerController(this
                , tokens[playerId], playerId);
        PlayerController playerController = playerControllers[playerId];
        seedPlayerData(playerController, hero, deck, infoPassive);
        if (playerIdCounter >= 2) {
            setGameReady(true);
            handInitiation(0);
            handInitiation(1);
        }
        return playerController;
    }

    private void handInitiation(int playerId) {
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
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

    private Message drawToHand(ModelPool.PlayerData playerData) {
        return drawToHand(playerData, playerData.getHandCard().size());
    }

    private Message drawToHand(ModelPool.PlayerData playerData, int index) {
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

    @Override
    protected PlayerController[] getAllPlayerControllers() {
        return playerControllers;
    }

    private void seedPlayerData(PlayerController playerController, Hero hero, Deck deck, InfoPassive infoPassive) {
        ModelPool.PlayerData playerData = new ModelPool.PlayerData(playerController.getId(),
                this, hero, deck, infoPassive);
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
        if (playerData.getMana() < cardObject.getCardModel().getMana())
            return Message.INSUFFICIENT;

        playerData.setMana(playerData.getMana() - cardObject.getCardModel().getMana());
        playerData.getHandCard().remove(cardObject);

        if (cardObject instanceof MinionObject) {
            if (playerData.getGroundCard().size() >= 7)
                return Message.FULL;
            playerData.getGroundCard().add(groundIndex, (MinionObject) cardObject);
            ((MinionObject) cardObject).setSleep(true);
            if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
                getScriptEngine().broadcastEventOnObject(cardObject, MinionBehavior.BATTLE_CRY);
            } else {
                getScriptEngine().broadcastEventOnObject(cardObject, MinionBehavior.BATTLE_CRY, optionalTarget);
            }
            logEvent(new GameAction.MinionPlay((MinionObject) cardObject));
        } else if (cardObject instanceof SpellObject) {
            if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
                getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_EFFECT);
                logEvent(new GameAction.SpellGlobal((SpellObject) cardObject));
            } else {
                getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_EFFECT, optionalTarget);
                logEvent(new GameAction.TargetedAttack(cardObject, optionalTarget));
            }
            getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_DONE, optionalTarget);
        } else if (cardObject instanceof WeaponObject) {
            playerData.getHero().setCurrentWeapon((WeaponObject) cardObject);
        }
        return Message.SUCCESS;
    }

    @Override
    protected Message summonMinion(MinionObject source, int playerId, int token) {
        if (!checkPlayerValidity(playerId, token) || !isStarted())
            return Message.ERROR;
        if (source.getPlayerId() != playerId)
            return Message.ERROR;
        ModelPool.PlayerData playerData = getModelPool().getPlayerDataById(playerId);
        playerData.getGroundCard().add(source);
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
                        && !((MinionObject) target).isTaunt())
                        || target instanceof HeroObject))
            return Message.IMPOSSIBLE;
        if (target instanceof MinionObject) {
            singleDirectionMinionAttack(source, target, playerId, token);
            singleDirectionMinionAttack((MinionObject) target, source, playerId, token);
            logEvent(new GameAction.MinionAttack(source, (MinionObject) target));
        } else if (target instanceof HeroObject) {
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
            flag |= minionObject.isTaunt();
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
        playerData.setMana(playerData.getMana() - cardObject.getCardModel().getMana());
        if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
            getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_EFFECT);
            logEvent(new GameAction.HeroPower(cardObject));
        } else {
            getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_EFFECT, target);
            logEvent(new GameAction.TargetedAttack(cardObject, target));
        }
        getScriptEngine().broadcastEventOnObject(cardObject, SpellBehavior.SPELL_DONE, target);
        return Message.SUCCESS;
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
            performDamageOnMinion((MinionObject) target, source.getCurrentWeapon().getAttackPower(), playerId, token);
            getScriptEngine().broadcastEventOnObject(source, HeroBehavior.ATTACK_EFFECT, target);
        } else if (target instanceof HeroObject) {
            performDamageOnHero((HeroObject) target, source.getCurrentWeapon().getAttackPower(), playerId, token);
            getScriptEngine().broadcastEventOnObject(source, HeroBehavior.ATTACK_EFFECT, target);
        }
    }

    private void singleDirectionMinionAttack(MinionObject source, GameObject target, int playerId, int token) {
        if (target instanceof MinionObject) {
            performDamageOnMinion((MinionObject) target, source.getAttackPower(), playerId, token);
            getScriptEngine().broadcastEventOnObject(source, MinionBehavior.ATTACK_EFFECT, target);
        } else if (target instanceof HeroObject) {
            performDamageOnHero((HeroObject) target, source.getAttackPower(), playerId, token);
            getScriptEngine().broadcastEventOnObject(source, MinionBehavior.ATTACK_EFFECT, target);
        }
    }

    @Override
    protected void logEvent(GameAction gameAction) {
        Logger.log("game board", gameAction.getMessage());
        getModelPool().getSceneData().getLog().add(gameAction);
    }
}
