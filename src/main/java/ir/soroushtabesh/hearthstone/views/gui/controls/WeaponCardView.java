package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.WeaponObject;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.util.gui.AnimationUtil;

public class WeaponCardView extends CardView {

    public WeaponCardView(Card card) {
        super(card);
    }

    public WeaponCardView(CardObject cardObject, GameController gameController) {
        super(cardObject, gameController);
    }

    @Override
    protected void bindView() {
        super.bindView();
        WeaponObject weaponObject = (WeaponObject) getCardObject();
        weaponObject.durabilityProperty()
                .addListener(AnimationUtil.numberChangeListener(getAttackLabel()));
        weaponObject.durabilityProperty()
                .addListener(AnimationUtil.numberChangeListener(getHpLabel()));
    }

    @Override
    protected String getMaskName() {
        return "weaponMask";
    }
}
