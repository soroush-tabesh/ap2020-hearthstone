package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class Charge extends MinionBehavior {
    @Override
    public void onCardPlay() {
        super.onCardPlay();
        ((MinionObject) getOwnerObject()).setSleep(false);
    }

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ((MinionObject) getOwnerObject()).setCanAttackHero(false);
        ((MinionObject) getOwnerObject()).setSleep(false);
    }
}
