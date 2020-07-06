package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GameEventListener;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class AttackOnPlay extends MinionBehavior implements GameEventListener {

    @Override
    public void onCardPlay() {
        super.onCardPlay();
        getGameController().getScriptEngine().registerEventFilter(this);
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
            CardObject source = cardPlay.getSource();
            if (source.getPlayerId() != getOwnerObject().getPlayerId()) {
                getPlayerController().playMinion((MinionObject) getOwnerObject(), source);
            }
        }
    }
}
