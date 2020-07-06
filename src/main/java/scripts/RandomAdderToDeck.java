package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.models.ScriptModel;

import java.util.ArrayList;
import java.util.List;

public class RandomAdderToDeck extends SpellBehavior {

    private final List<ScriptModel> scriptsToAdd = new ArrayList<>();
    private final String targetClass;

    public RandomAdderToDeck(Class<? extends CardObject> targetClass, GenericScript... behaviors) {
        for (GenericScript behavior : behaviors) {
            scriptsToAdd.add(new ScriptModel(behavior));
        }
        this.targetClass = targetClass.getName();
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        try {
            ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getId());
            if (((LocalGameController) getGameController())
                    .drawToHand(playerData, 0
                            , (Class<? extends CardObject>) getClass().getClassLoader().loadClass(targetClass))
                    == GameController.Message.SUCCESS) {
                CardObject cardObject = playerData.getHandCard().get(0);
                scriptsToAdd.forEach(scriptModel -> cardObject.addMiscScript(scriptModel.getScript(getGameController())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
