package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class DealDamage extends SpellBehavior {
    private int amount;

    public DealDamage(int amount) {
        this.amount = amount;
    }
}
