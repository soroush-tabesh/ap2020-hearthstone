package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class PlayCard extends SpellBehavior {
    private String cardName;

    public PlayCard(String cardName) {
        this.cardName = cardName;
    }
}
