package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.models.Card;

public class SpellObject extends CardObject {
    public SpellObject(int playerId, GameController gameController, Card cardModel) {
        super(playerId, gameController, cardModel);
    }
}
