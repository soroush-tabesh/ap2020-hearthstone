package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class GameObject {
    private final int id;
    private final int playerId;
    private final GameController gameController;
    private final List<GenericScript> miscScripts = new ArrayList<>();

    public GameObject(int playerId, GameController gameController) {
        this.id = gameController.generateID(this);
        this.playerId = playerId;
        this.gameController = gameController;
    }

    public int getId() {
        return id;
    }

    public GameController getGameController() {
        return gameController;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void addMiscScript(GenericScript genericScript) {
        miscScripts.add(genericScript);
        genericScript.setOwnerObject(this);
        gameController.getScriptEngine().registerScript(genericScript, this);
    }

    public void removeMiscScript(GenericScript genericScript) {
        gameController.getScriptEngine().unregisterScript(genericScript, this);
        genericScript.setOwnerObject(null);
        miscScripts.remove(genericScript);
    }

    public List<GenericScript> getMiscScripts() {
        return Collections.unmodifiableList(miscScripts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObject that = (GameObject) o;
        return getId() == that.getId() &&
                getPlayerId() == that.getPlayerId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPlayerId());
    }
}
