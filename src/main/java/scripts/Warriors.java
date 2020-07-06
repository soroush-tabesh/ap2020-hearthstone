package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Warriors extends HeroBehavior {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ModelPool.PlayerData playerData = getGameController().getModelPool()
                .getPlayerDataById(getOwnerObject().getPlayerId());
        ObservableList<MinionObject> groundCard = playerData.getGroundCard();
        groundCard.addListener((ListChangeListener<? super MinionObject>) c -> {
            c.next();
            c.getRemoved().forEach(minionObject -> playerData.getHero().setShield(playerData.getHero().getShield() + 1));
        });
    }
}
