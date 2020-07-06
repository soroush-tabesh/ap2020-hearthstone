package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.models.ScriptModel;

public class ForEachEnemyMinion extends SpellBehavior {
    private final ScriptModel effectModel;
    private transient SpellBehavior effect;

    public ForEachEnemyMinion(SpellBehavior effect) {
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
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        getGameController().getModelPool().getPlayerDataById(1 - getOwnerObject().getPlayerId())
                .getGroundCard().forEach(effect::onSpellEffect);
        return true;
    }
}
