package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.Message;

public class PlayerController {

    private final GameController gameController;
    private final long token;

    public PlayerController(GameController gameController, long token) {
        this.gameController = gameController;
        this.token = token;
    }

    public int getPlayerId() {
        return gameController.token2playerId(token);
    }

    public long getToken() {
        return token;
    }

    public GameController getGameController() {
        return gameController;
    }


    public boolean endTurn() {
        return gameController.endTurn(token);
    }

    public Message startGame() {
        return gameController.startGame(token);
    }

    public Message playCard(CardObject cardObject, int groundIndex, GameObject target) {
        return gameController.playCard(cardObject, groundIndex, target, token);
    }

    public Message playMinion(MinionObject source, GameObject target) {
        return gameController.playMinion(source, target, token);
    }

    public Message useWeapon(GameObject target) {
        return gameController.useWeapon(
                getGameController().getModelPool().getPlayerDataById(getPlayerId()).getHero(), target, token);
    }

    public Message useHeroPower(GameObject optionalTarget) {
        return gameController.useHeroPower(
                getGameController().getModelPool().getPlayerDataById(getPlayerId()).getHero(), optionalTarget, token);
    }

    public Message changeCard(int cardNumberInList) {
        return gameController.changeCard(cardNumberInList, token);
    }

    public static class DummyPlayerController extends PlayerController {
        private final int playerId;

        public DummyPlayerController(int playerId) {
            super(null, 0);
            this.playerId = playerId;
        }

        @Override
        public int getPlayerId() {
            return playerId;
        }

        @Override
        public long getToken() {
            return 0;
        }

        @Override
        public GameController getGameController() {
            return null;
        }

        @Override
        public boolean endTurn() {
            return false;
        }

        @Override
        public Message startGame() {
            return Message.ERROR;
        }

        @Override
        public Message playCard(CardObject cardObject, int groundIndex, GameObject target) {
            return Message.ERROR;
        }

        @Override
        public Message playMinion(MinionObject source, GameObject target) {
            return Message.ERROR;
        }

        @Override
        public Message useWeapon(GameObject target) {
            return Message.ERROR;
        }

        @Override
        public Message useHeroPower(GameObject optionalTarget) {
            return Message.ERROR;
        }

        @Override
        public Message changeCard(int cardNumberInList) {
            return Message.ERROR;
        }
    }
}
