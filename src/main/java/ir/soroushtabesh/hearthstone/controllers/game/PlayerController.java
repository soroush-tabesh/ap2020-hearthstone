package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.Message;

public class PlayerController {

    private final GameController gameController;
    private final int token;
    private final int id;

    public PlayerController(GameController gameController, int token, int id) {
        this.gameController = gameController;
        this.token = token;
        this.id = id;
    }

    public int getToken() {
        return token;
    }

    public GameController getGameController() {
        return gameController;
    }

    public int getId() {
        return id;
    }

    public boolean endTurn() {
        return gameController.endTurn(id, token);
    }

    public Message startGame() {
        return gameController.startGame(id, token);
    }

    public Message playCard(CardObject cardObject, int groundIndex, GameObject target) {
        return gameController.playCard(cardObject, groundIndex, target, id, token);
    }

    public Message playMinion(MinionObject source, GameObject target) {
        return gameController.playMinion(source, target, id, token);
    }

    public Message useWeapon(GameObject target) {
        return gameController.useWeapon(
                getGameController().getModelPool().getPlayerDataById(id).getHero(), target, id, token);
    }

    public Message useHeroPower(GameObject optionalTarget) {
        return gameController.useHeroPower(
                getGameController().getModelPool().getPlayerDataById(id).getHero(), optionalTarget, id, token);
    }

    public Message changeCard(int cardNumberInList) {
        return gameController.changeCard(cardNumberInList, id, token);
    }
}
