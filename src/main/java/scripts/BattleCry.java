package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.models.ScriptModel;

public class BattleCry extends MinionBehavior {
    private final ScriptModel effectModel;
    private transient SpellBehavior effect;

    public BattleCry(SpellBehavior effect) {
        effectModel = new ScriptModel(effect);
        this.effect = effect;
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
    public boolean battleCry(GameObject gameObject) {
        super.battleCry(gameObject);
        return effect.onSpellEffect(gameObject);
    }
}
