package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.models.Card;

public class WeaponCardView extends CardView {

    public WeaponCardView(Card card) {
        super(card);
    }

    @Override
    protected String getMaskName() {
        return "weaponMask";
    }


}
