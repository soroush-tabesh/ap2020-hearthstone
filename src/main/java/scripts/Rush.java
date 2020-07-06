package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class Rush extends MinionBehavior {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ((MinionObject) getOwnerObject()).setCanAttackHero(false);
        ((MinionObject) getOwnerObject()).setSleep(false);
    }

    @Override
    public void onCardPlay() {
        super.onScriptAdded();
        ((MinionObject) getOwnerObject()).setCanAttackHero(false);
        ((MinionObject) getOwnerObject()).setSleep(false);
    }

    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        ((MinionObject) getOwnerObject()).setCanAttackHero(true);
    }
}
