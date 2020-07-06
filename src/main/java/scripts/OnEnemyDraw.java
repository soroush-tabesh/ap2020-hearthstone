package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GameEventListener;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.models.ScriptModel;

public class OnEnemyDraw extends HeroBehavior implements GameEventListener {
    private final ScriptModel effectModel;
    private transient SpellBehavior effect;

    public OnEnemyDraw(SpellBehavior effect) {
        effectModel = new ScriptModel(effect);
        this.effect = effect;
    }

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        effect = (SpellBehavior) effectModel.getScript(getGameController());
        effect.setGameController(getGameController());
        effect.setOwnerObject(getOwnerObject());
        effect.setOwnerObject(getOwnerObject());
        getGameController().getScriptEngine().registerEventFilter(this);
    }

    @Override
    public void onEvent(GameAction gameAction) {
        if (gameAction instanceof GameAction.CardPlay) {
            GameAction.CardPlay cardPlay = (GameAction.CardPlay) gameAction;
            if (cardPlay.getSource().getPlayerId() != getOwnerObject().getPlayerId()) {
                effect.onSpellEffect(cardPlay.getSource());
            }
        }
    }
}
