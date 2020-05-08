package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.util.FXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditDeckDialog extends Dialog<ButtonType> implements Initializable {
    private final Deck deck;
    @FXML
    private TextField deckNameField;
    @FXML
    private ComboBox<Hero.HeroClass> heroCombo;

    public EditDeckDialog(Node owner, String title, String header, Deck deck) {
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
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        getDialogPane()
                .lookupButton(ButtonType.OK)
                .addEventFilter(ActionEvent.ACTION, event -> {
                    if (deckNameField.getText().isEmpty()) {
                        event.consume();
                        FXUtil.showAlertInfo("Collection", "Deck", "Deck name can't be empty.");
                    } else if (heroCombo.getValue() == null) {
                        event.consume();
                        FXUtil.showAlertInfo("Collection", "Deck", "Hero class can't be empty.");
                    }
                });
    }

    public void updateDeck() {
        deck.setName(deckNameField.getText());
        deck.setHeroClass(heroCombo.getValue());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deckNameField.setText(deck.getName());
        heroCombo.getItems().addAll(Hero.HeroClass.values());
        heroCombo.setDisable(!deck.isPure());
        if (deck.getHeroClass() != null)
            heroCombo.setValue(deck.getHeroClass());
    }
}
