package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptEngine {
    public static final String BATTLE_CRY = "battleCry";
    public static final String DEATH_RATTLE = "deathRattle";
    public static final String GAME_START = "onScriptAdded";

    private final GameController gameController;
    private final Map<GameObject, List<GenericScript>> scripts = new HashMap<>();

    public ScriptEngine(GameController gameController) {
        this.gameController = gameController;
        gameController.startedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                broadcastEvent(GAME_START);
        });
    }

    public GameController getGameController() {
        return gameController;
    }

    public void registerScript(GenericScript script, GameObject gameObject) {
        put(script, gameObject);
        if (gameController.isStarted())
            script.onScriptAdded();
    }

    public void unregisterScript(GenericScript script, GameObject gameObject) {
        if (gameController.isStarted())
            script.onScriptRemoved();
        remove(script, gameObject);
    }

    private void put(GenericScript script, GameObject gameObject) {
        scripts.put(gameObject, scripts.getOrDefault(gameObject, new ArrayList<>()));
        scripts.get(gameObject).add(script);
    }

    private void remove(GenericScript script, GameObject gameObject) {
        scripts.put(gameObject, scripts.getOrDefault(gameObject, new ArrayList<>()));
        scripts.get(gameObject).remove(script);
    }

    public void broadcastEvent(String event) {
        scripts.values().forEach((e) -> e.forEach((e1) -> {
            try {
                e1.getClass().getDeclaredMethod(event).invoke(e1);
            } catch (NoSuchMethodException ignored) {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }));
    }

}
