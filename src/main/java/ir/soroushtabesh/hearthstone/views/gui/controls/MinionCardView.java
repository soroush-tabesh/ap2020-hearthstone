package ir.soroushtabesh.hearthstone.views.gui.controls;

import animatefx.animation.AnimationFX;
import animatefx.animation.FadeOut;
import animatefx.animation.Pulse;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.util.gui.AnimationUtil;
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
        AnimationFX fadeOutAnim = new FadeOut(immuneEffect).setCycleCount(-1);
        fadeOutAnim.getTimeline().setAutoReverse(true);
        fadeOutAnim.play();

        AnimationFX StealthAnim = new FadeOut(stealthEffect).setCycleCount(-1);
        StealthAnim.getTimeline().setAutoReverse(true);
        StealthAnim.play();

        AnimationFX buffedAnim = new FadeOut(buffedEffect).setCycleCount(-1);
        buffedAnim.getTimeline().setAutoReverse(true);
        buffedAnim.play();
        AnimationUtil.getPassiveBounce(sleepEffect).play();

        AnimationFX superAttackAnim = new Pulse(getAttackLabel()).setCycleCount(-1);
        superAttackAnim.setResetOnFinished(true);
        superAttackAnim.getTimeline().setAutoReverse(true);
        superAttackAnim.setCycleCount(-1);

        AnimationFX superHpAnim = new Pulse(getHpLabel()).setCycleCount(-1);
        superAttackAnim.setResetOnFinished(true);
        superAttackAnim.getTimeline().setAutoReverse(true);
        superAttackAnim.setCycleCount(-1);

        // bind other properties
        MinionObject minionObject = (MinionObject) getCardObject();

        minionObject.hpProperty().addListener(AnimationUtil.numberChangeListener(getHpLabel()));
        minionObject.attackPowerProperty().addListener(AnimationUtil.numberChangeListener(getAttackLabel()));

        minionObject.sleepProperty()
                .addListener((observable, oldValue, newValue) -> {
                    sleepEffect.setVisible(newValue);
                });
        minionObject.superAttackPowerProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        superAttackAnim.play();
                        getAttackLabel().setStyle("-fx-text-fill: greenyellow");
                    } else {
                        superAttackAnim.stop();
                        getAttackLabel().setStyle("-fx-text-fill: gainsboro");
                    }
                });
        minionObject.superHPProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        superHpAnim.play();
                        getHpLabel().setStyle("-fx-text-fill: greenyellow");
                    } else {
                        superHpAnim.stop();
                        getHpLabel().setStyle("-fx-text-fill: gainsboro");
                    }
                });
        minionObject.tauntProperty()
                .addListener((observable, oldValue, newValue) -> {
                    tauntEffect.setVisible(newValue);
                });
        minionObject.immuneProperty()
                .addListener((observable, oldValue, newValue) -> {
                    immuneEffect.setVisible(newValue);
                });
        minionObject.stealthProperty()
                .addListener((observable, oldValue, newValue) -> {
                    stealthEffect.setVisible(newValue);
                });
        minionObject.buffedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    buffedEffect.setVisible(newValue);
                });
        minionObject.halo4Property()
                .addListener((observable, oldValue, newValue) -> {
                });
    }

    @Override
    protected String getMaskName() {
        return "minionMask";
    }

}
