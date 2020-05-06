package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.FXUtil;
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
    private CardView selectedCard = null;

    @Override
    public void onStart(Object message) {
        cards = CardView.buildAll(CardManager.getInstance().getAllCards());
        cards.forEach(cardView -> {
            cardView.setOnMouseClicked(event -> selectCard(cardView));
            cardView.getCountLabel().setVisible(true);
        });
        Bindings.bindContent(tilePane.getChildren(), cards);
        selectedCard = null;
        cardPreview.getChildren().clear();
    }

    private void selectCard(CardView cardView) {
        if (selectedCard != null)
            selectedCard.setStyle("-fx-background-color: transparent");
        Player player = PlayerManager.getInstance().getPlayer();
        selectedCard = cardView;
        selectedCard.setStyle("-fx-background-color: rgba(64,186,213,0.29)");
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
        selectedCard = null;
        cards = null;
    }

    @FXML
    private void buyButton(ActionEvent event) {
        if (selectedCard == null) {
            FXUtil.showAlert("Shop", "Buy", "Error: Please select a card first."
                    , Alert.AlertType.ERROR);
            return;
        }
        CardManager.Message message = CardManager.getInstance().buyCard(selectedCard.getBriefCard().getCard());
        switch (message) {
            case FULL:
                FXUtil.showAlert("Shop", "Buy: " + selectedCard.getBriefCard().getName()
                        , "Error: You can't buy more."
                        , Alert.AlertType.ERROR);
                break;
            case INSUFFICIENT:
                FXUtil.showAlert("Shop", "Buy: " + selectedCard.getBriefCard().getName()
                        , "Error: You don't have enough coin."
                        , Alert.AlertType.ERROR);
                break;
            case SUCCESS:
                Optional<ButtonType> result = FXUtil.showAlert("Shop"
                        , "Buy: " + selectedCard.getBriefCard().getName()
                        , "Are you sure you want to buy?", Alert.AlertType.CONFIRMATION);
                if (result.isPresent() && result.get() == ButtonType.OK)
                    FXUtil.showAlertInfo("Shop", "Sell: " + selectedCard.getBriefCard().getName()
                            , "Successfully bought!");
        }
        selectCard(selectedCard);
    }

    @FXML
    private void sellButton(ActionEvent event) {
        if (selectedCard == null) {
            FXUtil.showAlert("Shop", "Sell", "Error: Please select a card first."
                    , Alert.AlertType.ERROR);
            return;
        }
        CardManager.Message message = CardManager.getInstance().sellCard(selectedCard.getBriefCard().getCard());
        switch (message) {
            case EMPTY:
                FXUtil.showAlert("Shop", "Sell: " + selectedCard.getBriefCard().getName()
                        , "Error: You don't have this card."
                        , Alert.AlertType.ERROR);
                break;
            case SUCCESS:
                Optional<ButtonType> result = FXUtil.showAlert("Shop"
                        , "Sell: " + selectedCard.getBriefCard().getName()
                        , "Are you sure you want to sell?", Alert.AlertType.CONFIRMATION);
                if (result.isPresent() && result.get() == ButtonType.OK)
                    FXUtil.showAlertInfo("Shop", "Sell: " + selectedCard.getBriefCard().getName()
                            , "Successfully sold!");
        }
        selectCard(selectedCard);
    }

}