package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.models.cards.Minion;

public class Nurse extends HeroBehavior {
    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        if (getGameController().getTurn() == getOwnerObject().getPlayerId()) {
            getGameController().getModelPool().getPlayerDataById(getOwnerObject().getPlayerId())
                    .getGroundCard().forEach(minionObject -> {
                if (minionObject.getHp() < ((Minion) minionObject.getCardModel()).getHp()) {
                    minionObject.setHp(minionObject.getHp() + 1);
                }
            });
        }
    }
}
