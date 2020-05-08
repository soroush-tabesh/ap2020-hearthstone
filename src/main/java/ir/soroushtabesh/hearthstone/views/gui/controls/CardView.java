package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.BriefCard;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import ir.soroushtabesh.hearthstone.util.FXUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public abstract class CardView extends StackPane implements Initializable {
    @FXML
    private Label attackLabel;
    @FXML
    private Label hpLabel;
    @FXML
    private Label manaLabel;
    @FXML
    private Label countLabel;
    @FXML
    private ImageView maskImage;
    @FXML
    private ImageView bgImage;

    private String filename;
    private final BriefCard briefCard;

    public CardView(Card card) {
        this.briefCard = BriefCard.build(card);
        FXUtil.loadFXMLasRoot(this);
    }

    public static CardView build(Card card) {
        CardView cardView = null;
        if (card instanceof Minion) {
            cardView = new MinionCardView(card);
        } else if (card instanceof Weapon) {
            cardView = new WeaponCardView(card);
        } else if (card instanceof Spell) {
            cardView = new SpellCardView(card);
        }
        return cardView;
    }

    public static ObservableList<CardView> buildAll(Collection<Card> cards) {
        ObservableList<CardView> result = FXCollections.observableArrayList();
        cards.forEach(card -> result.add(build(card)));
        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //fixme: debug
        Image image = new Image(getClass().getResourceAsStream(String.format("../image/card/CS1_069.png"
                , briefCard.getCard().getCard_name())));
        bgImage.setImage(image);
        maskImage.setImage(new Image(getClass()
                .getResourceAsStream(String.format("../image/card/mask/%s.png", getMaskName()))));
        double manax = manaLabel.getTranslateX();
        double attackx = attackLabel.getTranslateX();
        double hpx = hpLabel.getTranslateX();
        double manay = manaLabel.getTranslateY();
        double attacky = attackLabel.getTranslateY();
        double hpy = hpLabel.getTranslateY();
        double w = getPrefWidth();
        double h = getPrefHeight();
        heightProperty().addListener((observable, oldValue, newValue) -> {
            manaLabel.setTranslateY(maskImage.getFitHeight() * manay / h);
            attackLabel.setTranslateY(maskImage.getFitHeight() * attacky / h);
            hpLabel.setTranslateY(maskImage.getFitHeight() * hpy / h);
        });
        widthProperty().addListener((observable, oldValue, newValue) -> {
            manaLabel.setTranslateX(maskImage.getFitWidth() * manax / w);
            attackLabel.setTranslateX(maskImage.getFitWidth() * attackx / w);
            hpLabel.setTranslateX(maskImage.getFitWidth() * hpx / w);
        });
        manaLabel.setText(briefCard.getMana() + "");
        attackLabel.setText(briefCard.getAttack() + "");
        hpLabel.setText(briefCard.getHP() + "");
        countLabel.setText(PlayerManager.getInstance().getPlayer().getOwnedAmount(briefCard.getCard()) + "");
//        setStyle("-fx-background-color: rgba(225,106,107,0.3)");
    }

    protected abstract String getMaskName();

    public BriefCard getBriefCard() {
        return briefCard;
    }

    public Label getAttackLabel() {
        return attackLabel;
    }

    public Label getHpLabel() {
        return hpLabel;
    }

    public Label getManaLabel() {
        return manaLabel;
    }

    public Label getCountLabel() {
        return countLabel;
    }

    public void disable(boolean b) {
//        setDisable(b);

//        attackLabel.setDisable(b);
//        hpLabel.setDisable(b);
//        manaLabel.setDisable(b);
        countLabel.setDisable(b);
        ColorAdjust desaturate = new ColorAdjust();
        desaturate.setSaturation(b ? -1 : 1);
        bgImage.setEffect(desaturate);
        maskImage.setEffect(desaturate);
    }

}
