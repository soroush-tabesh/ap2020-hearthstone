package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.ScriptModel;

import java.util.ArrayList;
import java.util.List;

public class TargetAdd extends SpellBehavior {
    private final List<ScriptModel> scriptModels = new ArrayList<>();

    public TargetAdd add(MinionBehavior minionBehavior) {
        scriptModels.add(new ScriptModel(minionBehavior));
        return this;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        if (!(gameObject instanceof MinionObject))
            return false;
        MinionObject minionObject = (MinionObject) gameObject;
        scriptModels.forEach(scriptModel -> minionObject.addMiscScript(scriptModel.getScript(getGameController())));
        return true;
    }
}
