package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.models.Card;

public class HeroPowerCardView extends CardView {
    public HeroPowerCardView(Card card) {
        super(card);
    }

    public HeroPowerCardView(CardObject cardObject) {
        super(cardObject);
    }

    @Override
    protected String getMaskName() {
        return "heroPowerMask";
    }
}
