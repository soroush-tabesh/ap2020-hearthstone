package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.LoginScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SettingSceneController extends AbstractSceneController {
    @FXML
    private PasswordField passField;
    @FXML
    private Button deleteButton;
    @FXML
    private Slider volumeSlider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        volumeSlider.setMax(1);
        volumeSlider.valueProperty().bindBidirectional(AudioManager.getInstance().bgMusicVolumeProperty());
    }

    @FXML
    private void deleteAccount(ActionEvent event) {
        Optional<ButtonType> buttonType =
                FXUtil.showAlert("Setting"
                        , "Delete Account"
                        , "Are you sure you want to delete your account?"
                        , Alert.AlertType.CONFIRMATION);
        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {
            PlayerManager playerManager = PlayerManager.getInstance();
            Message message = playerManager.deleteAccount(passField.getText());
            if (message == Message.ERROR)
                FXUtil.showAlert("Setting", "Account Delete", "Error", Alert.AlertType.ERROR);
            else if (message == Message.WRONG)
                FXUtil.showAlert("Setting", "Account Delete", "Wrong Password", Alert.AlertType.ERROR);
            else {
                SceneManager.getInstance().showScene(LoginScene.class);
                FXUtil.showAlertInfo("Setting", "Account Delete", "Success");
            }
        }
    }
}
