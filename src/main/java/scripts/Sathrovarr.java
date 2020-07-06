package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;

public class Sathrovarr extends MinionBehavior {
    @Override
    public boolean battleCry(GameObject gameObject) {
        super.battleCry(gameObject);
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getId());
        if (gameObject instanceof MinionObject) {
            MinionObject minionObject = (MinionObject) gameObject;
            if (minionObject.getPlayerId() != getPlayerController().getId())
                return false;
            CardObject build1 = CardObject.build(gameObject.getPlayerId(), getGameController(), minionObject.getCardModel());
            CardObject build2 = CardObject.build(gameObject.getPlayerId(), getGameController(), minionObject.getCardModel());
            CardObject build3 = CardObject.build(gameObject.getPlayerId(), getGameController(), minionObject.getCardModel());
            if (playerData.getHandCard().size() < 12)
                playerData.getHandCard().add(build1);
            if (playerData.getHandCard().size() < 12)
                playerData.getHandCard().add(build2);
            playerData.getDeckCard().add(build3);
            if (getPlayerController().playCard(build2, 0, null) != GameController.Message.SUCCESS)
                playerData.getHandCard().remove(build2);
        }
        return true;
    }
}
