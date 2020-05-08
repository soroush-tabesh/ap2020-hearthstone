package ir.soroushtabesh.hearthstone.views.gui.controllers;

import animatefx.animation.Hinge;
import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomInRight;
import ir.soroushtabesh.hearthstone.controllers.GameController;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.controls.CardView;
import ir.soroushtabesh.hearthstone.views.gui.controls.HeroView;
import ir.soroushtabesh.hearthstone.views.gui.controls.SelectHeroDeckDialog;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class BoardSceneController extends AbstractSceneController {
    @FXML
    private StackPane burnedCardStand;
    @FXML
    private StackPane weaponStand;
    @FXML
    private ImageView heroPowerImage;
    @FXML
    private HBox groundBox;
    @FXML
    private StackPane heroStand;
    @FXML
    private HBox handCardBox;
    @FXML
    private Label manaLabel;
    @FXML
    private VBox logBox;
    @FXML
    private Label deckRemLabel;

    private GameController gameController;
    private Map<Card, CardView> cardViewMap = new HashMap<>();
    private ContextMenu cardContextMenu;
    private CardView selectedCard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initContextMenu();
    }

    @Override
    public void onStart(Object message) {
        super.onStart(message);
        initUI();
        if (!validateHeroAndDeck()) {
            super.backPressed(null);
            return;
        }
        initController();
        bindUI();
        gameController.startGame();
    }

    private void initContextMenu() {
        cardContextMenu = new ContextMenu();
        MenuItem playCard = new MenuItem("Play Card");
        cardContextMenu.getItems().add(playCard);
        playCard.setOnAction(event -> {
            GameController.Message message = gameController.playCard(selectedCard.getBriefCard().getCard());
            switch (message) {
                case NO_MANA:
                    FXUtil.showAlertInfo("Board", "Play card", "Not enough mana.");
                    break;
                case GROUND_FULL:
                    FXUtil.showAlertInfo("Board", "Play card", "Ground is full");
                    break;
                case ERROR:
                    FXUtil.showAlertInfo("Board", "Play card", "Error");
            }
        });
    }

    private void bindUI() {
        Player player = PlayerManager.getInstance().getPlayer();
        heroStand.getChildren().add(HeroView.build(player.getCurrentHero()));
        //fixme: debug
        heroPowerImage.setImage(new Image(getClass().getResourceAsStream(String.format("../image/card/Anduin_Wrynn.png"
                , player.getCurrentHero().getHeroPower().getName()))));
        gameController.getDeck().addListener((ListChangeListener<Card>) c -> {
            c.next();
            deckRemLabel.setText(gameController.getDeck().size() + "");
        });
        gameController.getGround().addListener((ListChangeListener<Card>) c
                -> {
            c.next();
            c.getAddedSubList().forEach(card -> {
                CardView cardView = getCardView(card);
                groundBox.getChildren().add(cardView);
                new ZoomInRight(cardView).play();
            });
        });
        gameController.getHand().addListener((ListChangeListener<Card>) c -> {
            c.next();
            c.getRemoved().forEach(card -> handCardBox.getChildren()
                    .removeIf(node -> ((CardView) node).getBriefCard().getCard().equals(card)));
            c.getAddedSubList().forEach(card -> handCardBox.getChildren().add(getCardView(card)));
        });
        gameController.manaProperty().addListener((observable, oldValue, newValue) ->
                manaLabel.setText(String.format("%d/%d", newValue.intValue(), gameController.getManaMax())));
        gameController.getLog().addListener((ListChangeListener<String>) c -> {
            c.next();
            c.getAddedSubList().forEach(s -> {
                Label element = new Label(s);
                element.getStyleClass().add("label_log");
                logBox.getChildren().add(0, element);

            });
        });
        gameController.currentWeaponProperty().addListener((observable, oldValue, newValue) -> {
            weaponStand.getChildren().clear();
            CardView cardView = getCardView(newValue);
            weaponStand.getChildren().add(cardView);
            new ZoomIn(cardView).play();
        });
        gameController.burnedCardProperty().addListener((observable, oldValue, newValue) -> {
            burnedCardStand.getChildren().clear();
            CardView cardView = getCardView(newValue);
            burnedCardStand.getChildren().add(cardView);
            Hinge hinge = new Hinge(cardView);
            hinge.setOnFinished(event -> burnedCardStand.getChildren().clear());
            hinge.play();
        });
    }

    private CardView getCardView(Card card) {
        CardView cardView = cardViewMap.get(card);
        if (cardView == null) {
            cardView = CardView.build(card);
            CardView finalCardView = cardView;
            CardView finalCardView1 = cardView;
            cardView.setOnMouseClicked(event -> {
                if (finalCardView.getParent() == handCardBox) {
                    selectedCard = finalCardView1;
                    cardContextMenu
                            .show(finalCardView, event.getScreenX(), event.getScreenY());
                }
            });
            cardViewMap.put(card, cardView);
        }
        return cardView;
    }

    private void initController() {
        Player player = PlayerManager.getInstance().getPlayer();
        gameController = new GameController(player.getCurrentHero(), player.getCurrentDeck());
    }

    private boolean validateHeroAndDeck() {
        SelectHeroDeckDialog dialog = new SelectHeroDeckDialog(heroStand);
        Optional<ButtonType> res = dialog.showAndWait();
        return res.isPresent() && !res.get().equals(ButtonType.CANCEL);
    }

    private void initUI() {
        heroStand.getChildren().clear();
        handCardBox.getChildren().clear();
        manaLabel.setText("1/1");
        logBox.getChildren().clear();
        weaponStand.getChildren().clear();
    }

    @Override
    protected void backPressed(ActionEvent event) {
        Optional<ButtonType> res = FXUtil.showAlert("Game", "Exit"
                , "Are you sure you want to leave the board?", Alert.AlertType.CONFIRMATION);
        if (res.isPresent() && res.get().equals(ButtonType.OK))
            super.backPressed(event);
    }

    public void endTurn(ActionEvent event) {
        gameController.endTurn();
    }
}
