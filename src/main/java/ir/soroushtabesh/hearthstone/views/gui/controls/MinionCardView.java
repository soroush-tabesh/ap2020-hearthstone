package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.models.Card;

public class MinionCardView extends CardView {

    public MinionCardView(Card card) {
        super(card);
    }

    @Override
    protected String getMaskName() {
        return "minionMask";
    }

}
