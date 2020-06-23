package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class PlayerController {

    private final GameController gameController;
    private final long token;
    private final int id;

    public PlayerController(GameController gameController, long token, int id) {
        this.gameController = gameController;
        this.token = token;
        this.id = id;
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

    public void startGame() {
        gameController.startGame(id, token);
    }

    public GameController.Message drawCard(CardObject cardObject, GameObject target) {
        return gameController.drawCard(cardObject, target, id, token);
    }

    public GameController.Message playMinion(MinionObject source, MinionObject target) {
        return gameController.playMinion(source, target, id, token);
    }
}
