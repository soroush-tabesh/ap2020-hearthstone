package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class Stealth extends MinionBehavior {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ((MinionObject) getOwnerObject()).setStealth(true);
    }

    @Override
    public void onAttackEffect(GameObject gameObject) {
        super.onAttackEffect(gameObject);
        ((MinionObject) getOwnerObject()).setStealth(false);
    }
}
