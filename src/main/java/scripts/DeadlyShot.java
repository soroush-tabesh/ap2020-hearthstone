package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;

import java.util.Random;

public class DeadlyShot extends SpellBehavior {
    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        ModelPool.PlayerData playerData = getGameController().getModelPool()
                .getPlayerDataById(1 - getOwnerObject().getPlayerId());
        MinionObject minionObject = playerData.getGroundCard()
                .get(new Random(System.currentTimeMillis()).nextInt(playerData.getGroundCard().size()));
        ((LocalGameController) getGameController()).performDamageOnMinion(minionObject, 1000
                , getPlayerController().getId(), getPlayerController().getToken());
        return true;
    }
}
