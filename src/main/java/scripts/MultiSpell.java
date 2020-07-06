package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.models.ScriptModel;

import java.util.ArrayList;
import java.util.List;

public class MultiSpell extends SpellBehavior {
    private transient final List<SpellBehavior> spellBehaviors = new ArrayList<>();
    private final List<ScriptModel> scriptModels = new ArrayList<>();

    public MultiSpell add(SpellBehavior minionBehavior) {
        spellBehaviors.add(minionBehavior);
        scriptModels.add(new ScriptModel(minionBehavior));
        return this;
    }

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        scriptModels.forEach(scriptModel -> spellBehaviors.add((SpellBehavior) scriptModel.getScript(getGameController())));
        spellBehaviors.forEach(spellBehavior -> {
            spellBehavior.setOwnerObject(getOwnerObject());
            spellBehavior.setGameController(getGameController());
            spellBehavior.setPlayerController(getPlayerController());
        });
        spellBehaviors.forEach(SpellBehavior::onScriptAdded);
    }

    @Override
    public void onSpellDone() {
        super.onSpellDone();
        spellBehaviors.forEach(SpellBehavior::onSpellDone);
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        spellBehaviors.forEach(spellBehavior -> spellBehavior.onSpellEffect(gameObject));
        return true;
    }
}
