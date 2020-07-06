package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class Taunt extends MinionBehavior {
    @Override
    public void onCardPlay() {
        if (!(getOwnerObject() instanceof MinionObject))
            return;
        ((MinionObject) getOwnerObject()).setTaunt(true);
        return;
    }
}
