package ir.soroushtabesh.hearthstone.controllers.game.scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;

public abstract class GenericScript {
    private transient int id;
    private transient GameObject ownerObject;
    private transient GameController gameController;

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

}
