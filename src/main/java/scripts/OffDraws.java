package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;

import java.util.ArrayList;
import java.util.List;

public class OffDraws extends HeroBehavior {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ModelPool.PlayerData playerData = getGameController().getModelPool()
                .getPlayerDataById(getPlayerController().getId());
        List<CardObject> cardObjects = new ArrayList<>();
        cardObjects.addAll(playerData.getHandCard());
        cardObjects.addAll(playerData.getDeckCard());
        cardObjects.forEach(cardObject -> cardObject.setManaCost(Math.max(0, cardObject.getManaCost() - 1)));
    }
}
