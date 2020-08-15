package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;

public class ManaJump extends HeroBehavior {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getPlayerId());
        playerData.setManaMax(Math.max(0, Math.min(11, playerData.getManaMax() + 1)));
    }
}
