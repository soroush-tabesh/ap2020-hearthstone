package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.SpellObject;
import ir.soroushtabesh.hearthstone.models.Message;

public class RandomDrawer extends SpellBehavior {
    private boolean discardSpell;

    public RandomDrawer() {
    }

    public RandomDrawer(boolean discardSpell) {
        this.discardSpell = discardSpell;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getId());
        if (((LocalGameController) getGameController())
                .drawToHand(playerData, 0) == Message.SUCCESS) {
            CardObject cardObject = playerData.getHandCard().get(0);
            if (discardSpell && cardObject instanceof SpellObject) {
                playerData.getHandCard().remove(0);
                playerData.getBurnedCard().add(cardObject);
            }
        }
        return true;
    }
}
