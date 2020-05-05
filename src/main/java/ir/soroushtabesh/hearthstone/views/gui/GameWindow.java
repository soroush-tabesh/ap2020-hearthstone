package ir.soroushtabesh.hearthstone.views.gui;

import ir.soroushtabesh.hearthstone.util.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.controllers.GameWindowController;
import ir.soroushtabesh.hearthstone.views.gui.controllers.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GameWindow extends Application {
    private Stage stage;
    private GameWindowController controller;
    private Parent root = null;
    private Scene scene = null;
    private Image image;
    private Image image2;
    private ImageCursor cursorNormal;
    private ImageCursor cursorDown;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        setStage(stage);
        if (!setUpStage(stage)) throw new RuntimeException("Could not load fxml");
        gameInit();
        FXUtil.runLater(() -> {
            SceneManager.getInstance().showScene(LoginScene.class);
//            FXUtil.runLater(()->SceneManager.getInstance().showScene(MenuScene.class),2000);
//            AudioManager.getInstance().startBackgroundMusic();
        }, 500);
    }

    private boolean setUpStage(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/GameWindow.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        setController(fxmlLoader.getController());
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Hearthstone - Soroush Tabesh");
        stage.setResizable(false);
        stage.show();
        return true;
    }

    private void gameInit() {
        initSceneManager();
        initCursor();
    }

    private void initCursor() {
        image = new Image(getClass().getResourceAsStream("image/hearthstone-hand.png"));
        image2 = new Image(getClass().getResourceAsStream("image/hearthstone-hand-down.png"));
        cursorNormal = new ImageCursor(image);
        cursorDown = new ImageCursor(image2);
        scene.setCursor(cursorNormal);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> scene.setCursor(cursorDown));
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> scene.setCursor(cursorNormal));
    }

    private void initSceneManager() {
        SceneManager sceneManager = SceneManager.init(this);
        sceneManager.addScene(new BoardScene());
        sceneManager.addScene(new CollectionScene());
        sceneManager.addScene(new MenuScene());
        sceneManager.addScene(new ShopScene());
        sceneManager.addScene(new StatusScene());
        sceneManager.addScene(new SettingScene());
        sceneManager.addScene(new LoginScene());
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
