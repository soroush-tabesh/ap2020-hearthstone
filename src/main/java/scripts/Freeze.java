package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class Freeze extends MinionBehavior {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ((MinionObject) getOwnerObject()).setSleep(true);
    }
}
