package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class RestoreHealthTarget extends SpellBehavior {
    private int amount;

    public RestoreHealthTarget(int amount) {
        this.amount = amount;
    }
}
