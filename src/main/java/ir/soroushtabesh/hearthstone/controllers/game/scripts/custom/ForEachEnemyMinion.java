package ir.soroushtabesh.hearthstone.controllers.game.scripts.custom;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class ForEachEnemyMinion extends SpellBehavior {
    private SpellBehavior effect;

    public ForEachEnemyMinion(SpellBehavior effect) {
        this.effect = effect;
    }
}
