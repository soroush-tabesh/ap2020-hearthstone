package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.AudioManager;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingSceneController extends AbstractSceneController {
    @FXML
    private Slider volumeSlider;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        volumeSlider.setMax(1);
        volumeSlider.valueProperty().bindBidirectional(AudioManager.getInstance().bgMusicVolumeProperty());
    }
}
