package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.CollectionScene;
import ir.soroushtabesh.hearthstone.views.gui.controls.CardView;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import java.util.Optional;

public class ShopSceneController extends AbstractSceneController {
    @FXML
    private Label priceLabel;
    @FXML
    private Label ownedLabel;
    @FXML
    private StackPane cardPreview;
    @FXML
    private TilePane tilePane;
    @FXML
    private Label coins;

    private ObservableList<CardView> cards;
    private CardView selectedCardView = null;
    private Card messageCard;

    @Override
    public void onStart(Object message) {
        messageCard = null;
        if (message instanceof Card)
            messageCard = (Card) message;
        new Thread(() -> {
            cards = CardView.buildAll(CardManager.getInstance().getAllCards());
            selectedCardView = null;
            Player player = PlayerManager.getInstance().getPlayer();
            FXUtil.runLater(() -> {
                cards.forEach(cardView -> {
                    cardView.setOnMouseClicked(event -> selectCard(cardView));
                    cardView.forCollection(true);
                });
                Bindings.bindContent(tilePane.getChildren(), cards);
                cardPreview.getChildren().clear();
                coins.setText(player.getCoin() + "");
                cards.forEach(cardView -> {
                    if (cardView.getBriefCard().getCard().equals(messageCard))
                        selectCard(cardView);
                });
            }, 0);
        }).start();
    }

    @Override
    protected void backPressed(ActionEvent event) {
        if (messageCard == null)
            super.backPressed(event);
        else
            SceneManager.getInstance().showScene(CollectionScene.class, messageCard);
    }

    private void selectCard(CardView cardView) {
        if (selectedCardView != null)
            selectedCardView.setStyle("-fx-background-color: transparent");
        Player player = PlayerManager.getInstance().getPlayer();
        selectedCardView = cardView;
        selectedCardView.setStyle("-fx-background-color: rgba(64,186,213,0.29)");
        cardPreview.getChildren().clear();
        cardPreview.getChildren().add(CardView.build(cardView.getBriefCard().getCard()));
        priceLabel.setText(cardView.getBriefCard().getPrice() + "");
        ownedLabel.setText(player.getOwnedAmount(cardView.getBriefCard().getCard()) + "");
        coins.setText(player.getCoin() + "");
    }

    @Override
    public void onStop() {
        super.onStop();
        Bindings.unbindContent(tilePane.getChildren(), cards);
        cardPreview.getChildren().clear();
        selectedCardView = null;
        cards = null;
    }

    @FXML
    private void buyButton(ActionEvent event) {
        if (selectedCardView == null) {
            FXUtil.showAlert("Shop", "Buy", "Error: Please select a card first."
                    , Alert.AlertType.ERROR);
            return;
        }
        Message message = CardManager.getInstance().buyCard(selectedCardView.getBriefCard().getCard()
                , PlayerManager.getInstance().getToken());
        switch (message) {
            case FULL:
                FXUtil.showAlert("Shop", "Buy: " + selectedCardView.getBriefCard().getName()
                        , "Error: You can't buy more."
                        , Alert.AlertType.ERROR);
                break;
            case INSUFFICIENT:
                FXUtil.showAlert("Shop", "Buy: " + selectedCardView.getBriefCard().getName()
                        , "Error: You don't have enough coin."
                        , Alert.AlertType.ERROR);
                break;
            case SUCCESS:
                Optional<ButtonType> result = FXUtil.showAlert("Shop"
                        , "Buy: " + selectedCardView.getBriefCard().getName()
                        , "Are you sure you want to buy?", Alert.AlertType.CONFIRMATION);
                if (result.isPresent() && result.get() == ButtonType.OK)
                    FXUtil.showAlertInfo("Shop", "Sell: " + selectedCardView.getBriefCard().getName()
                            , "Successfully bought!");
        }
        selectCard(selectedCardView);
        selectedCardView.update();
    }

    @FXML
    private void sellButton(ActionEvent event) {
        if (selectedCardView == null) {
            FXUtil.showAlert("Shop", "Sell", "Error: Please select a card first."
                    , Alert.AlertType.ERROR);
            return;
        }
        Card card = selectedCardView.getBriefCard().getCard();
        CardManager cardManager = CardManager.getInstance();
        Optional<ButtonType> result = FXUtil.showAlert("Shop"
                , "Sell: " + selectedCardView.getBriefCard().getName()
                , "Are you sure you want to sell?"
                        + (cardManager.isInAnyDeck(card) ? " You have this card in some of your decks" : "")
                , cardManager.isInAnyDeck(card) ? Alert.AlertType.WARNING : Alert.AlertType.CONFIRMATION);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Message message = cardManager.sellCard(card, PlayerManager.getInstance().getToken());
            switch (message) {
                case EMPTY:
                    FXUtil.showAlert("Shop", "Sell: " + selectedCardView.getBriefCard().getName()
                            , "Error: You don't have this card."
                            , Alert.AlertType.ERROR);
                    break;
                case SUCCESS:
                    FXUtil.showAlertInfo("Shop", "Sell: " + selectedCardView.getBriefCard().getName()
                            , "Successfully sold!");
            }
        }
        selectCard(selectedCardView);
        selectedCardView.update();
    }

}