package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class RandomDrawer extends SpellBehavior {
    private boolean discardSpell;

    public RandomDrawer() {
    }

    public RandomDrawer(boolean discardSpell) {
        this.discardSpell = discardSpell;
    }
}
