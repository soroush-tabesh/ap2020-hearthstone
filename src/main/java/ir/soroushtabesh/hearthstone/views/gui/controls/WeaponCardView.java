package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.WeaponObject;
import ir.soroushtabesh.hearthstone.models.Card;

public class WeaponCardView extends CardView {

    public WeaponCardView(Card card) {
        super(card);
    }

    public WeaponCardView(CardObject cardObject) {
        super(cardObject);
    }

    @Override
    protected void bindView() {
        super.bindView();
        ((WeaponObject) getCardObject()).attackPowerProperty()
                .addListener((observable, oldValue, newValue) -> {
                    getAttackLabel().setText(newValue + "");
                });
        ((WeaponObject) getCardObject()).durabilityProperty()
                .addListener((observable, oldValue, newValue) -> {
                    getHpLabel().setText(newValue + "");
                });
    }

    @Override
    protected String getMaskName() {
        return "weaponMask";
    }
}
