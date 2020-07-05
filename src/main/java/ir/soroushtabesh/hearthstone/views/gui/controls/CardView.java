package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.BriefCard;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import javafx.beans.binding.Bindings;
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
    private BriefCard briefCard;
    private CardObject cardObject;
    @FXML
    private Label attackLabel;
    @FXML
    private Label hpLabel;
    @FXML
    private Label manaLabel;
    @FXML
    private Label countLabel;
    @FXML
    private Label classLabel;
    @FXML
    private ImageView maskImage;
    @FXML
    private ImageView bgImage;

    public CardView(Card card) {
        this.briefCard = BriefCard.build(card);
        FXUtil.loadFXMLasRoot(this);
    }

    public CardView(CardObject cardObject) {
        this.cardObject = cardObject;
        briefCard = BriefCard.build(cardObject.getCardModel());
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

    public static CardView build(CardObject cardObject) {
        CardView cardView = null;
        if (cardObject instanceof MinionObject) {
            cardView = new MinionCardView(cardObject);
        } else if (cardObject instanceof WeaponObject) {
            cardView = new WeaponCardView(cardObject);
        } else if (cardObject instanceof SpellObject) {
            cardView = new SpellCardView(cardObject);
        } else if (cardObject instanceof HeroPowerObject) {
            cardView = new HeroPowerCardView(cardObject);
        }
        return cardView;
    }

    public static ObservableList<CardView> buildAll(Collection<Card> cards) {
        ObservableList<CardView> result = FXCollections.observableArrayList();
        cards.forEach(card -> result.add(build(card)));
        return result;
    }

    public final CardObject getCardObject() {
        return cardObject;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImages();
        bindDimensions();
        initViews();
        if (getCardObject() != null)
            bindView();
    }

    protected void bindView() {
        manaLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> getCardObject().getManaCost() + "", getCardObject().manaCostProperty()));
    }

    private void initViews() {
        manaLabel.setText(briefCard.getMana() + "");
        attackLabel.setText(briefCard.getAttack() + "");
        hpLabel.setText(briefCard.getHP() + "");
        countLabel.setText(PlayerManager.getInstance().getPlayer().getOwnedAmount(briefCard.getCard()) + "");
        classLabel.setText(briefCard.getCard().getHeroClass().toString());
    }

    protected void loadImages() {
        try {
            bgImage.setImage(new Image(getClass().getResourceAsStream(String.format("../image/card/%s.png"
                    , briefCard.getCard().getName()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        maskImage.setImage(new Image(getClass()
                .getResourceAsStream(String.format("../image/card/mask/%s.png", getMaskName()))));
    }

    private void bindDimensions() {
        double manaX = manaLabel.getTranslateX();
        double attackX = attackLabel.getTranslateX();
        double hpx = hpLabel.getTranslateX();
        double manaY = manaLabel.getTranslateY();
        double attackY = attackLabel.getTranslateY();
        double hpy = hpLabel.getTranslateY();
        double w = getPrefWidth();
        double h = getPrefHeight();
        heightProperty().addListener((observable, oldValue, newValue) -> {
            manaLabel.setTranslateY(maskImage.getFitHeight() * manaY / h);
            attackLabel.setTranslateY(maskImage.getFitHeight() * attackY / h);
            hpLabel.setTranslateY(maskImage.getFitHeight() * hpy / h);
        });
        widthProperty().addListener((observable, oldValue, newValue) -> {
            manaLabel.setTranslateX(maskImage.getFitWidth() * manaX / w);
            attackLabel.setTranslateX(maskImage.getFitWidth() * attackX / w);
            hpLabel.setTranslateX(maskImage.getFitWidth() * hpx / w);
        });
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

    protected Label getCountLabel() {
        return countLabel;
    }

    protected Label getClassLabel() {
        return classLabel;
    }

    public void forCollection(boolean is) {
        getCountLabel().setVisible(is);
        getClassLabel().setVisible(is);
    }

    public void disable(boolean b) {
        countLabel.setDisable(b);
        classLabel.setDisable(b);
        ColorAdjust desaturate = new ColorAdjust();
        desaturate.setSaturation(b ? -1 : 1);
        bgImage.setEffect(desaturate);
        maskImage.setEffect(desaturate);
    }

    public void update() {
        briefCard.refresh();
        initViews();
    }

}
