package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.models.BriefCard;
import ir.soroushtabesh.hearthstone.models.BriefDeck;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class CardTextView extends HBox implements Initializable {
    @FXML
    private Label amountLabel;
    @FXML
    private Label cardNameLabel;

    private BriefCard briefCard;
    private BriefDeck briefDeck;

    public CardTextView(Card card, Deck deck) {
        this.briefCard = BriefCard.build(card);
        this.briefDeck = BriefDeck.build(deck);
        FXUtil.loadFXMLasRoot(this);
    }

    public static CardTextView build(Card card, Deck deck) {
        return new CardTextView(card, deck);
    }

    public static ObservableList<CardTextView> buildAll(Collection<Card> cards, Deck deck) {
        ObservableList<CardTextView> result = FXCollections.observableArrayList();
        cards.forEach(card -> result.add(build(card, deck)));
        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindLabels();
    }

    private void bindLabels() {
        cardNameLabel.setText(String.format("%s (%d Mana)", briefCard.getName(), briefCard.getMana()));
        amountLabel.setText(briefDeck.getDeck().getCountInDeck(briefCard.getCard()) + "");
    }

    public BriefCard getBriefCard() {
        return briefCard;
    }

    public BriefDeck getBriefDeck() {
        return briefDeck;
    }

    public void refresh() {
        briefCard.refresh();
        bindLabels();
    }
}
