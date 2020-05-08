package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.views.gui.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuSceneController extends AbstractSceneController {

    @FXML
    private Label coins;
    @FXML
    private Button playButton;
    @FXML
    private Button shopButton;
    @FXML
    private Button statusButton;
    @FXML
    private Button collectionButton;
    @FXML
    private Button settingButton;

    @FXML
    private void playButtonAction(ActionEvent event) {
        SceneManager.getInstance().showScene(BoardScene.class);
    }

    @FXML
    private void shopButtonAction(ActionEvent event) {
        SceneManager.getInstance().showScene(ShopScene.class);
    }

    @FXML
    private void statusButtonAction(ActionEvent event) {
        SceneManager.getInstance().showScene(StatusScene.class);
    }

    @FXML
    private void collectionButtonAction(ActionEvent event) {
        SceneManager.getInstance().showScene(CollectionScene.class);
    }

    @FXML
    private void settingButtonAction(ActionEvent event) {
        SceneManager.getInstance().showScene(SettingScene.class);
    }

    @Override
    public void onStart(Object message) {
        super.onStart(message);
        Player player = PlayerManager.getInstance().getPlayer();
        coins.setText(player.getCoin() + "");
    }
}