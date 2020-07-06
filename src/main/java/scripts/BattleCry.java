package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class BattleCry extends MinionBehavior {
    private SpellBehavior effect;

    public BattleCry(SpellBehavior effect) {
        this.effect = effect;
    }
}
