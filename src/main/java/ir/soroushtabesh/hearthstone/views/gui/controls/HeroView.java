package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.models.BriefHero;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.util.FXUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HeroView extends StackPane implements Initializable {
    @FXML
    private ImageView bgImage;
    @FXML
    private ImageView maskImage;
    @FXML
    private Label hpLabel;

    private BriefHero briefHero;

    public HeroView(Hero hero) {
        briefHero = BriefHero.build(hero);
        FXUtil.loadFXMLasRoot(this);
    }

    public static HeroView build(Hero hero) {
        return new HeroView(hero);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImages();
        bindDimensions();
        initViews();
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
        //fixme: debug
        bgImage.setImage(new Image(getClass().getResourceAsStream(String.format("../image/hero/Anduin_Wrynn.png"
                , briefHero.getName()))));
        maskImage.setImage(new Image(getClass()
                .getResourceAsStream("../image/hero/mask/heroMask.png")));
    }
}
