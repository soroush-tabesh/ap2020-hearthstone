package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.ScriptModel;

public class OnTurnEnd extends MinionBehavior {
    private final ScriptModel effectModel;
    private transient SpellBehavior effect;

    private transient boolean enable = false;

    public OnTurnEnd(SpellBehavior effect) {
        effectModel = new ScriptModel(effect);
        this.effect = effect;
    }

    @Override
    public boolean battleCry(GameObject gameObject) {
        enable = true;
        return super.battleCry(gameObject);
    }

    @Override
    public void deathRattle() {
        enable = false;
        super.deathRattle();
    }

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        effect = (SpellBehavior) effectModel.getScript(getGameController());
        effect.setGameController(getGameController());
        effect.setOwnerObject(getOwnerObject());
        effect.setPlayerController(getPlayerController());
    }

    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        if (((MinionObject) getOwnerObject()).isDead() || !enable)
            return;
        if (getGameController().getTurn() == getOwnerObject().getPlayerId()) {
            effect.onSpellEffect(null);
        }
    }
}
