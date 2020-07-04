package ir.soroushtabesh.hearthstone.views.gui.controls;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.Card;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MinionCardView extends CardView {

    @FXML
    private ImageView tauntEffect;
    @FXML
    private ImageView immuneEffect;
    @FXML
    private ImageView stealthEffect;
    @FXML
    private ImageView sleepEffect;
    @FXML
    private ImageView buffedEffect;

    public MinionCardView(Card card) {
        super(card);
    }

    public MinionCardView(CardObject cardObject) {
        super(cardObject);
    }

    @Override
    protected void loadImages() {
        super.loadImages();
        tauntEffect.setImage(new Image(getClass().getResourceAsStream("../image/taunt.png")));
        immuneEffect.setImage(new Image(getClass().getResourceAsStream("../image/inplay_minion_immune.png")));
        stealthEffect.setImage(new Image(getClass().getResourceAsStream("../image/inplay_minion_stealth.png")));
        sleepEffect.setImage(new Image(getClass().getResourceAsStream("../image/effect_sleep.png")));
        buffedEffect.setImage(new Image(getClass().getResourceAsStream("../image/inplay_minion_buffed.png")));
    }

    @Override
    protected void bindView() {
        super.bindView();
        //todo animations
        AnimationFX animationFX1 = new FadeOut(immuneEffect).setCycleCount(-1);
        animationFX1.getTimeline().setAutoReverse(true);
        animationFX1.play();
        AnimationFX animationFX2 = new FadeOut(stealthEffect).setCycleCount(-1);
        animationFX2.getTimeline().setAutoReverse(true);
        animationFX2.play();
        AnimationFX animationFX3 = new FadeOut(buffedEffect).setCycleCount(-1);
        animationFX3.getTimeline().setAutoReverse(true);
        animationFX3.play();
        // bind other properties
        ((MinionObject) getCardObject()).hpProperty()
                .addListener((observable, oldValue, newValue) -> {
                    getHpLabel().setText(newValue + "");
                });
        ((MinionObject) getCardObject()).attackPowerProperty()
                .addListener((observable, oldValue, newValue) -> {
                    getAttackLabel().setText(newValue + "");
                });
        ((MinionObject) getCardObject()).sleepProperty()
                .addListener((observable, oldValue, newValue) -> {
                    sleepEffect.setVisible(newValue);
                });
        ((MinionObject) getCardObject()).superAttackPowerProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        getAttackLabel().setStyle("-fx-text-fill: greenyellow");
                    } else {
                        getAttackLabel().setStyle("-fx-text-fill: gainsboro");
                    }
                });
        ((MinionObject) getCardObject()).superHPProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        getHpLabel().setStyle("-fx-text-fill: greenyellow");
                    } else {
                        getHpLabel().setStyle("-fx-text-fill: gainsboro");
                    }
                });
        ((MinionObject) getCardObject()).tauntProperty()
                .addListener((observable, oldValue, newValue) -> {
                    tauntEffect.setVisible(newValue);
                });
        ((MinionObject) getCardObject()).halo1Property()
                .addListener((observable, oldValue, newValue) -> {
                    immuneEffect.setVisible(newValue);
                });
        ((MinionObject) getCardObject()).halo2Property()
                .addListener((observable, oldValue, newValue) -> {
                    stealthEffect.setVisible(newValue);
                });
        ((MinionObject) getCardObject()).halo3Property()
                .addListener((observable, oldValue, newValue) -> {
                    buffedEffect.setVisible(newValue);
                });
        ((MinionObject) getCardObject()).halo4Property()
                .addListener((observable, oldValue, newValue) -> {
                });
    }

    @Override
    protected String getMaskName() {
        return "minionMask";
    }

}
