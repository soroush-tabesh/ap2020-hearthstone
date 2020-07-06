package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GameEventListener;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class HighPriestAmet extends MinionBehavior implements GameEventListener {

    @Override
    public boolean battleCry(GameObject gameObject) {
        super.battleCry(gameObject);
        getGameController().getScriptEngine().registerEventFilter(this);
        return true;
    }

    @Override
    public void deathRattle() {
        super.deathRattle();
        getGameController().getScriptEngine().unregisterEventFilter(this);
    }

    @Override
    public void onEvent(GameAction gameAction) {
        if (gameAction instanceof GameAction.CardPlay) {
            GameAction.CardPlay cardPlay = (GameAction.CardPlay) gameAction;
            if (cardPlay.getSource().getPlayerId() == getOwnerObject().getPlayerId()) {
                MinionObject minionObject = (MinionObject) getOwnerObject();
                minionObject.setHp(((MinionObject) getOwnerObject()).getHp());
            }
        }
    }
}