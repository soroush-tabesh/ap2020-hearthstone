package ir.soroushtabesh.hearthstone.views.gui;

import ir.soroushtabesh.hearthstone.network.RemoteGameServer;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.controllers.GameWindowController;
import ir.soroushtabesh.hearthstone.views.gui.controllers.SceneManager;
import ir.soroushtabesh.hearthstone.views.gui.controls.ConnectDialog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GameWindow extends Application {
    private Stage stage;
    private GameWindowController controller;
    private Parent root = null;
    private Scene scene = null;
    private ImageCursor cursorNormal;
    private ImageCursor cursorDown;
    private static int busy = 0;
    private static Alert fetchAlert;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        fetchAlert = new Alert(Alert.AlertType.CONFIRMATION);
        fetchAlert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        fetchAlert.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
        fetchAlert.setTitle("Hearthstone");
        fetchAlert.setHeaderText("Network");
        fetchAlert.setContentText("Fetching Data...");

        Dialog<ButtonType> connectDialog = new ConnectDialog(stage);

//        RemoteGameServer.getInstance().setOnFetchStartListener(GameWindow::addBusyWaiter);
//
//        RemoteGameServer.getInstance().setOnFetchEndListener(GameWindow::releaseBusyWaiter);

        Runnable connectionRunnable = () -> Platform.runLater(connectDialog::showAndWait);
        RemoteGameServer.getInstance().setOnErrorListener(connectionRunnable);
        FXUtil.runLater(connectionRunnable, 100);

        setStage(stage);
        if (!setUpStage(stage)) throw new RuntimeException("Could not load fxml");
        gameInit();
        FXUtil.runLater(() -> {
            SceneManager.getInstance().showScene(LoginScene.class);
            // FIXME: 8/11/20
//            AudioManager.getInstance().startBackgroundMusic();
        }, 500);

    }

    public static void addBusyWaiter() {
        FXUtil.runLater(() -> {
            ++busy;
            if (busy > 0 && !fetchAlert.isShowing())
                fetchAlert.showAndWait();
        }, 200);
    }

    public static void releaseBusyWaiter() {
        FXUtil.runLater(() -> {
            --busy;
            fetchAlert.close();
        }, 0);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        RemoteGameServer.getInstance().disconnect();
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
        Image image = new Image(getClass().getResourceAsStream("image/hearthstone-hand.png"));
        Image image2 = new Image(getClass().getResourceAsStream("image/hearthstone-hand-down.png"));
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
