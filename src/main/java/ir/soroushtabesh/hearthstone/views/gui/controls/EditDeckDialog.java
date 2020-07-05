package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditDeckDialog extends Dialog<ButtonType> implements Initializable {
    private final Deck deck;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField deckNameField;
    @FXML
    private ComboBox<Hero.HeroClass> heroCombo;

    public EditDeckDialog(Node owner, String title, String header, Deck deck) {
        Logger.log("Dialog", getClass().getSimpleName());
        this.deck = deck;
        initOwner(owner.getScene().getWindow());
        setTitle(title);
        setHeaderText(header);
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
        getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, this::okPressed);
    }

    private void okPressed(ActionEvent event) {
        if (deckNameField.getText().isEmpty()) {
            event.consume();
            FXUtil.showAlertInfo("Collection", "Deck", "Deck name can't be empty.");
        } else if (heroCombo.getValue() == null) {
            event.consume();
            FXUtil.showAlertInfo("Collection", "Deck", "Hero class can't be empty.");
        }
    }

    public void updateDeck() {
        Logger.log("DeckManager", String.format("update deck: %s", deck.getName()));
        deck.setName(deckNameField.getText());
        deck.setHeroClass(heroCombo.getValue());
        Logger.log("DeckManager", String.format("updated deck: %s (%s)", deck.getName(), deck.getHeroClass()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deckNameField.setText(deck.getName());
        heroCombo.getItems().addAll(Hero.HeroClass.values());
        boolean pure = deck.isPure();
        heroCombo.setDisable(!pure);
        errorLabel.setVisible(!pure);
        if (deck.getHeroClass() != null)
            heroCombo.setValue(deck.getHeroClass());
    }
}
