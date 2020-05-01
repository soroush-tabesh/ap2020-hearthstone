package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.views.gui.AbstractScene;
import ir.soroushtabesh.hearthstone.views.gui.GameWindow;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {
    private static SceneManager instance;
    private GameWindow gameWindow;
    private List<AbstractScene> scenes = new ArrayList<>();
    private AbstractScene currentScene;

    private SceneManager(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    public static SceneManager getInstance() {
        return instance;
    }

    public static SceneManager init(GameWindow gameWindow) {
        return instance = new SceneManager(gameWindow);
    }

    public void addScene(AbstractScene scene) {
        scenes.add(scene);
        StackPane windowPane = gameWindow.getController().getWindowPane();
        windowPane.getChildren().add(scene.getPane());
    }

    public <T extends AbstractScene> void showScene(Class<T> tClass) {
        showScene(tClass, null);
    }

    public <T extends AbstractScene> void showScene(Class<T> tClass, Object message) {
        System.out.println("SceneManager.showScene");
        if (currentScene != null && currentScene.getClass().equals(tClass)) {
            return;
        }
        for (AbstractScene scene : scenes) {
            if (scene.getClass().equals(tClass)) {
                changeScene(scene, message);
                return;
            }
        }
        throw new NoSuchSceneException();
    }

    private void changeScene(AbstractScene target, Object message) {
        applyTransit(target);
        target.onStart(message);
        if (currentScene != null)
            currentScene.onStop();
        currentScene = target;
    }

    private void applyTransit(AbstractScene target) {
        target.getPane().toFront();
        target.fadeIn();
        if (currentScene != null) {
            currentScene.getPane().toFront();
            currentScene.fadeOut();
        }
    }
}
