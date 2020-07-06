package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class OnTurnEnd extends MinionBehavior {
    private SpellBehavior effect;

    public OnTurnEnd(SpellBehavior effect) {
        this.effect = effect;
    }
}
