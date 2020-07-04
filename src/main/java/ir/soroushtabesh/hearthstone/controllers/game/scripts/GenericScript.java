package ir.soroushtabesh.hearthstone.controllers.game.scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.PlayerController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;

public class GenericScript {
    public static final String GAME_START = "onScriptAdded";
    public static final String REMOVE_SCRIPT = "onScriptRemoved";
    public static final String TURN_START = "onTurnStart";
    public static final String TURN_END = "onTurnEnd";
    private transient int id;
    private transient GameObject ownerObject;
    private transient GameController gameController;
    private transient PlayerController playerController;

    protected PlayerController getPlayerController() {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameObject getOwnerObject() {
        return ownerObject;
    }

    public void setOwnerObject(GameObject ownerObject) {
        this.ownerObject = ownerObject;
    }

    public void onScriptAdded() {
    }

    public void onScriptRemoved() {
    }

    public void onTurnStart() {
    }

    public void onTurnEnd() {
    }
}
