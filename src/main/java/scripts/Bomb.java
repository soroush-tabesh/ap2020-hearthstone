package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

import java.util.List;

public class Bomb extends SpellBehavior {
    private final int damage;
    private boolean friendlyFire = true;

    public Bomb(int damage) {
        this.damage = damage;
    }

    public Bomb(int damage, boolean friendlyFire) {
        this.damage = damage;
        this.friendlyFire = friendlyFire;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onTurnEnd();
        List<MinionObject> allCards = getGameController().getModelPool().getAllMinions();
        allCards.forEach(minionObject -> {
            if (minionObject == getOwnerObject())
                return;
            if (!friendlyFire && minionObject.getPlayerId() == getOwnerObject().getPlayerId())
                return;
            ((LocalGameController) getGameController())
                    .performDamageOnMinion(minionObject, damage
                            , getPlayerController().getId(), getPlayerController().getToken());
        });
        return true;
    }
}
