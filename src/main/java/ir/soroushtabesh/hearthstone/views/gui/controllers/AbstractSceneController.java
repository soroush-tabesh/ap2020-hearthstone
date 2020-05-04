package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.views.gui.AbstractScene;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractSceneController implements Initializable {
    @FXML
    private Pane pane;

    private AbstractScene currentScene;

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
        System.out.println(getClass().getSimpleName() + " : disabled=" + disabled);
    }
}
