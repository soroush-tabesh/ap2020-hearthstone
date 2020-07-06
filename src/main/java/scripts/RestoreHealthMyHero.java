package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class RestoreHealthMyHero extends SpellBehavior {
    private int amount;

    public RestoreHealthMyHero(int amount) {
        this.amount = amount;
    }
}
