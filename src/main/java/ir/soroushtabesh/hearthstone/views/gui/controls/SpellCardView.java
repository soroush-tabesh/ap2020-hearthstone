package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.models.Card;

import java.net.URL;
import java.util.ResourceBundle;

public class SpellCardView extends CardView {

    public SpellCardView(Card card) {
        super(card);
    }

    public SpellCardView(CardObject cardObject, GameController gameController) {
        super(cardObject, gameController);
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
