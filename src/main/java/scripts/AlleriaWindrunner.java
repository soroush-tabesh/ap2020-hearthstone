package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GameEventListener;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;

public class AlleriaWindrunner extends HeroBehavior implements GameEventListener {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        getGameController().getScriptEngine().registerEventFilter(this);
    }

    @Override
    public void onEvent(GameAction gameAction) {
        if (gameAction instanceof GameAction.CardPlay) {
            GameAction.CardPlay cardPlay = (GameAction.CardPlay) gameAction;
            CardObject source = cardPlay.getSource();
            if (source.getPlayerId() == getOwnerObject().getPlayerId()) {
                source.addMiscScript(new Rush());
            }
        }
    }
}
