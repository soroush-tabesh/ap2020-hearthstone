package ir.soroushtabesh.hearthstone.controllers.game.scripts.custom;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class OnDamageTaken extends MinionBehavior {
    private SpellBehavior effect;

    public OnDamageTaken(SpellBehavior effect) {
        this.effect = effect;
    }
}
