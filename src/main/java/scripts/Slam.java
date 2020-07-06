package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class Slam extends SpellBehavior {
    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        if (!(gameObject instanceof MinionObject))
            return false;
        MinionObject minionObject = (MinionObject) gameObject;
        ((LocalGameController) getGameController()).performDamageOnMinion(minionObject, 2
                , getPlayerController().getId(), getPlayerController().getToken());
        if (!minionObject.isDead()) {
            ((LocalGameController) getGameController())
                    .drawToHand(getGameController().getModelPool().getPlayerDataById(getPlayerController().getId()));
        }
        return true;
    }
}
