package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class Deathrattle extends MinionBehavior {
    private SpellBehavior effect;

    public Deathrattle(SpellBehavior effect) {
        this.effect = effect;
    }
}
