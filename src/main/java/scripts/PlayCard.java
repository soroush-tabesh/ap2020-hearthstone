package scripts;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.models.Card;

public class PlayCard extends SpellBehavior {
    private final String cardName;

    public PlayCard(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        Card locust = CardManager.getInstance().getCardByName(cardName);
        CardObject cardObject = CardObject.build(getPlayerController().getId(), getGameController(), locust);
        getPlayerController().playCard(cardObject, 0, null);
        return true;
    }
}
