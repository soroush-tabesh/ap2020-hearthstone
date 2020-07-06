package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroObject;

public class Guldan extends HeroBehavior {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ((HeroObject) getOwnerObject()).setHp(35);
    }
}
