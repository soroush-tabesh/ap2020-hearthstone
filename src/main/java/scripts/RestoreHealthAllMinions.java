package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class RestoreHealthAllMinions extends SpellBehavior {
    private int amount;

    public RestoreHealthAllMinions(int amount) {
        this.amount = amount;
    }
}
