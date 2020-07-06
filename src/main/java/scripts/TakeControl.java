package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;

public class TakeControl extends SpellBehavior {
    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        if (!(gameObject instanceof MinionObject))
            return false;
        MinionObject minionObject = (MinionObject) gameObject;
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(minionObject.getPlayerId());
        playerData.getGroundCard().remove(minionObject);

        CardObject cardObject = CardObject.build(getPlayerController().getId(), getGameController(), minionObject.getCardModel());
        getGameController().summonMinion((MinionObject) cardObject
                , getPlayerController().getId(), getPlayerController().getToken());
        return true;
    }
}
