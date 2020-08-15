package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.models.Card;

public class HeroPowerCardView extends CardView {
    public HeroPowerCardView(Card card) {
        super(card);
    }

    public HeroPowerCardView(CardObject cardObject, GameController gameController) {
        super(cardObject, gameController);
    }

    @Override
    protected String getMaskName() {
        return "heroPowerMask";
    }
}
