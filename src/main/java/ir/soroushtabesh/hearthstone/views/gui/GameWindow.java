package ir.soroushtabesh.hearthstone.views.gui;

import ir.soroushtabesh.hearthstone.controllers.AudioManager;
import ir.soroushtabesh.hearthstone.util.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.controllers.GameWindowController;
import ir.soroushtabesh.hearthstone.views.gui.controllers.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameWindow extends Application {
    private Stage stage;
    private GameWindowController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        setStage(stage);
        if (!setUpStage(stage)) return;
        gameInit();
        FXUtil.runLater(() -> {
            SceneManager.getInstance().showScene(MenuScene.class);
            AudioManager.getInstance().startBackgroundMusic();
//            FXUtil.runLater(() -> SceneManager.getInstance().showScene(CollectionScene.class), 2000);
        }, 2000);
    }

    private boolean setUpStage(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/GameWindow.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        setController(fxmlLoader.getController());
        stage.setScene(new Scene(root));
        stage.setTitle("Hearthstone - Soroush Tabesh");
        stage.setResizable(false);
        stage.show();
        return true;
    }

    private void gameInit() {
        initializeSceneManager();
    }

    private void initializeSceneManager() {
        SceneManager sceneManager = SceneManager.init(this);
        sceneManager.addScene(new BoardScene());
        sceneManager.addScene(new CollectionScene());
        sceneManager.addScene(new MenuScene());
        sceneManager.addScene(new ShopScene());
    }

    public Stage getStage() {
        return stage;
    }

    private void setStage(Stage stage) {
        this.stage = stage;
    }

    public GameWindowController getController() {
        return controller;
    }

    private void setController(GameWindowController controller) {
        this.controller = controller;
    }
}
