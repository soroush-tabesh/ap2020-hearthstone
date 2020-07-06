package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class OnEnemyDraw extends HeroBehavior {
    private SpellBehavior effect;

    public OnEnemyDraw(SpellBehavior effect) {
        this.effect = effect;
    }
}
