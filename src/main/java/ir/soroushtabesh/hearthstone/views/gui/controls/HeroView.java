package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroObject;
import ir.soroushtabesh.hearthstone.models.BriefHero;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.util.AnimationUtil;
import ir.soroushtabesh.hearthstone.util.FXUtil;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class HeroView extends StackPane implements Initializable {
    @FXML
    private ImageView bgImage;
    @FXML
    private ImageView maskImage;
    @FXML
    private Label hpLabel;
    @FXML
    private ImageView heroShield;
    @FXML
    private Label shieldLabel;

    private BriefHero briefHero;
    private HeroObject heroObject;

    public HeroView(Hero hero) {
        briefHero = BriefHero.build(hero);
        FXUtil.loadFXMLasRoot(this);
    }

    public HeroView(HeroObject heroObject) {
        this.heroObject = heroObject;
        heroObject.getGameController().turnProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue.intValue() == heroObject.getPlayerId())
                        setEffect(AnimationUtil.getGlowAnimated(Color.LIMEGREEN, 20, 70));
                    else
                        setEffect(null);
                });
        briefHero = BriefHero.build(heroObject.getHeroModel());
        FXUtil.loadFXMLasRoot(this);
    }

    public static HeroView build(Hero hero) {
        return new HeroView(hero);
    }

    public static HeroView build(HeroObject heroObject) {
        return new HeroView(heroObject);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImages();
        bindDimensions();
        initViews();
        if (heroObject != null)
            bindViews();
    }

    public BriefHero getBriefHero() {
        return briefHero;
    }

    public HeroObject getHeroObject() {
        return heroObject;
    }

    private void bindViews() {
        hpLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> heroObject.getHp() + "", heroObject.hpProperty()));
        shieldLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> heroObject.getShield() + "", heroObject.shieldProperty()));
        shieldLabel.visibleProperty().bind(heroObject.shieldProperty().greaterThan(0));
        heroShield.visibleProperty().bind(heroObject.shieldProperty().greaterThan(0));
    }

    private void initViews() {
        hpLabel.setText(briefHero.getHp() + "");
    }

    private void bindDimensions() {
        double hpx = hpLabel.getTranslateX();
        double hpy = hpLabel.getTranslateY();
        double w = getPrefWidth();
        double h = getPrefHeight();
        heightProperty().addListener((observable, oldValue, newValue) -> {
            hpLabel.setTranslateY(maskImage.getFitHeight() * hpy / h);
        });
        widthProperty().addListener((observable, oldValue, newValue) -> {
            hpLabel.setTranslateX(maskImage.getFitWidth() * hpx / w);
        });
    }

    private void loadImages() {
        maskImage.setImage(new Image(getClass()
                .getResourceAsStream("../image/hero/mask/heroMask.png")));
        heroShield.setImage(new Image(getClass()
                .getResourceAsStream("../image/hero_armor.png")));
        try {
            bgImage.setImage(new Image(getClass().getResourceAsStream(String.format("../image/hero/%s.png"
                    , briefHero.getName()))));
        } catch (Exception e) {
            e.printStackTrace();
            bgImage.setImage(new Image(getClass().getResourceAsStream("../image/hero/card_hero_neutral.png")));
        }
    }
}
