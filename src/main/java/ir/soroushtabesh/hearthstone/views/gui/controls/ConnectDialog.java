package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.network.RemoteGameServer;
import ir.soroushtabesh.hearthstone.network.models.Config;
import ir.soroushtabesh.hearthstone.util.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectDialog extends Dialog<ButtonType> implements Initializable {

    @FXML
    private Label mal;
    @FXML
    private TextField portLabel;
    @FXML
    private TextField ipLabel;

    public ConnectDialog(Stage owner) {
        Logger.log("Dialog", getClass().getSimpleName());
        initOwner(owner.getOwner());
        setTitle("Network");
        setHeaderText("Connect");
        setContentText("Enter server info:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(theClass -> this);
        fxmlLoader.setLocation(getClass().getResource(getClass().getSimpleName() + ".fxml"));
        try {
            getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        setupButtons();
    }

    private void setupButtons() {
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, this::okPressed);
        setOnCloseRequest(this::okPressed);
    }

    private void okPressed(Event event) {
        if (!portLabel.getText().matches("[0-9]{1,5}")) {
            mal.setVisible(true);
            mal.setText("Malformed input.");
            event.consume();
            return;
        }

        RemoteGameServer server = RemoteGameServer.getInstance();
        server.setConfig(new Config(ipLabel.getText(), Integer.parseInt(portLabel.getText())));
        mal.setVisible(true);
        mal.setText("Can't connect.");
        if (!server.connect()) {
            event.consume();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mal.setVisible(false);
    }
}
