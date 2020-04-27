package ir.soroushtabesh.hearthstone.views.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractSceneController implements Initializable {
    @FXML
    private Pane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDisable(true);
    }

    public Pane getPane() {
        return pane;
    }

    public void setDisable(boolean disabled) {
        pane.setVisible(!disabled);
        pane.setDisable(disabled);
        System.out.println(getClass().getSimpleName() + " : disabled=" + disabled);
    }
}
