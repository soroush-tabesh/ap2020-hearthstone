package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.views.gui.LoginScene;
import ir.soroushtabesh.hearthstone.views.gui.MenuScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginSceneController extends AbstractSceneController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void signUp(ActionEvent event) {
        if (!checkInputValid()) {
            ((LoginScene) getCurrentScene()).showCheckInput();
            return;
        }
        PlayerManager.Message message = PlayerManager.getInstance().makeAccount(getInputUsername(), getInputPassword());
        switch (message) {
            case ERROR:
                ((LoginScene) getCurrentScene()).showError();
                break;
            case EXISTS:
                ((LoginScene) getCurrentScene()).showExists();
                break;
            case SUCCESS:
                ((LoginScene) getCurrentScene()).showSignUpSuccess();
        }
    }

    @FXML
    private void login(ActionEvent event) {
        if (!checkInputValid()) {
            ((LoginScene) getCurrentScene()).showCheckInput();
            return;
        }
        PlayerManager.Message message = PlayerManager.getInstance().authenticate(getInputUsername(), getInputPassword());
        switch (message) {
            case ERROR:
                ((LoginScene) getCurrentScene()).showError();
                break;
            case WRONG:
                ((LoginScene) getCurrentScene()).showWrong();
                break;
            case SUCCESS:
                SceneManager.getInstance().showScene(MenuScene.class);
        }
    }

    private boolean checkInputValid() {
        return getInputPassword().length() >= 3 && !getInputUsername().isEmpty();
    }

    private String getInputUsername() {
        return usernameField.getText().trim();
    }

    private String getInputPassword() {
        return passwordField.getText();
    }
}
