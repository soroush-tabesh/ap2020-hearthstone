package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.views.gui.AbstractScene;
import ir.soroushtabesh.hearthstone.views.gui.MenuScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractSceneController implements Initializable {

    private AbstractScene currentScene;
    @FXML
    private Pane pane;
    @FXML
    private Button backButton;

    @FXML
    protected void backPressed(ActionEvent event) {
        SceneManager.getInstance().showScene(MenuScene.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDisable(true);
    }

    public AbstractScene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(AbstractScene scene) {
        this.currentScene = scene;
    }

    public Pane getPane() {
        return pane;
    }

    public void setDisable(boolean disabled) {
        pane.setVisible(!disabled);
    }

    public void onStart(Object message) {

    }

    public void onStop() {

    }
}
