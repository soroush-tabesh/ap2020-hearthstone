package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class Poisonous extends MinionBehavior {
    @Override
    public void onAttackEffect(GameObject gameObject) {
        super.onAttackEffect(gameObject);
        if (gameObject instanceof MinionObject) {
            MinionObject minionObject = (MinionObject) gameObject;
            if (!minionObject.hasImmunity()) {
                ((LocalGameController) getGameController()).performDamageOnMinion(minionObject, 100
                        , getPlayerController().getToken());
            }
        }
    }
}
