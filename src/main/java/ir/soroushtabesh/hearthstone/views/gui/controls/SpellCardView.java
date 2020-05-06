package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.models.Card;

import java.net.URL;
import java.util.ResourceBundle;

public class SpellCardView extends CardView {

    public SpellCardView(Card card) {
        super(card);
    }

    @Override
    protected String getMaskName() {
        return "spellMask";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        getHpLabel().setVisible(false);
        getAttackLabel().setVisible(false);
    }
}
