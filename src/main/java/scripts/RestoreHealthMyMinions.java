package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.models.cards.Minion;

public class RestoreHealthMyMinions extends SpellBehavior {
    private final int amount;

    public RestoreHealthMyMinions(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        getGameController().getModelPool().getPlayerDataById(getOwnerObject().getPlayerId())
                .getGroundCard().forEach(minionObject -> {
            if (minionObject.getHp() < ((Minion) minionObject.getCardModel()).getHp()) {
                minionObject.setHp(Math.min(minionObject.getHp() + amount,
                        ((Minion) minionObject.getCardModel()).getHp()));
            }
        });
        return true;
    }
}
