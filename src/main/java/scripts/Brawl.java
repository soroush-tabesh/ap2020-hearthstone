package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

import java.util.Collections;
import java.util.List;

public class Brawl extends SpellBehavior {
    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        List<MinionObject> allCards = getGameController().getModelPool().getAllMinions();
        Collections.shuffle(allCards);
        allCards.remove(0);
        allCards.forEach(minionObject -> ((LocalGameController) getGameController())
                .performDamageOnMinion(minionObject, 100
                        , getPlayerController().getId(), getPlayerController().getToken()));
        return true;
    }
}
