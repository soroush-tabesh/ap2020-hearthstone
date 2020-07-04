package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.GameEventListener;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import javafx.collections.ListChangeListener;

import java.lang.reflect.Method;
import java.util.*;

public class ScriptEngine {
    private final GameController gameController;
    private final Map<GameObject, List<GenericScript>> scripts = new HashMap<>();
    private final Set<GameEventListener> eventListeners = new HashSet<>();

    public ScriptEngine(GameController gameController) {
        this.gameController = gameController;
        // scripts will be added on game start
        gameController.startedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                broadcastEventGlobal(GenericScript.GAME_START);
        });
        // broadcasting game actions
        gameController.getModelPool().getSceneData().getLog().addListener((ListChangeListener<GameAction>) c -> {
            while (c.next())
                c.getAddedSubList().forEach(gameAction ->
                        eventListeners.forEach(gameEventListener ->
                                gameEventListener.onEvent(gameAction)));
        });
    }

    public GameController getGameController() {
        return gameController;
    }

    public void registerScript(GenericScript script) {
        put(script, script.getOwnerObject());
        script.setPlayerController(gameController.getAllPlayerControllers()[script.getOwnerObject().getPlayerId()]);
        if (gameController.isStarted())
            script.onScriptAdded();
    }

    public void unregisterScript(GenericScript script) {
        if (gameController.isStarted())
            script.onScriptRemoved();
        script.setPlayerController(null);
        remove(script, script.getOwnerObject());
    }

    public void registerEventFilter(GameEventListener listener) {
        if (listener != null)
            eventListeners.add(listener);
    }

    private void put(GenericScript script, GameObject gameObject) {
        scripts.put(gameObject, scripts.getOrDefault(gameObject, new ArrayList<>()));
        scripts.get(gameObject).add(script);
    }

    private void remove(GenericScript script, GameObject gameObject) {
        scripts.put(gameObject, scripts.getOrDefault(gameObject, new ArrayList<>()));
        scripts.get(gameObject).remove(script);
    }

    public void broadcastEventGlobal(String event, Object... params) {
        scripts.values().forEach((e) -> e.forEach((e1) -> broadcastEventOnScript(e1, event, params)));
    }

    public void broadcastEventOnPlayer(String event, int playerId, Object... params) {
        scripts.values().forEach((e) -> e.forEach((e1) -> {
            if (e1.getOwnerObject().getPlayerId() == playerId)
                broadcastEventOnScript(e1, event, params);
        }));
    }

    public void broadcastEventOnObject(GameObject gameObject, String event, Object... params) {
        scripts.getOrDefault(gameObject, Collections.emptyList())
                .forEach((e1) -> broadcastEventOnScript(e1, event, params));
    }

    public void broadcastEventOnScript(GenericScript e1, String event, Object... params) {
        try {
            Method declaredMethod = e1.getClass().getMethod(event, getClassList(params));
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(e1, params);
        } catch (NoSuchMethodException ignored) {
            System.err.println("No such method:");
            System.err.println("    e1 = " + e1 + ", event = " + event + ", params = " + Arrays.deepToString(params));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Class[] getClassList(Object... params) {
        Class[] cls = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            cls[i] = params[i].getClass();
        }
        return cls;
    }

}
