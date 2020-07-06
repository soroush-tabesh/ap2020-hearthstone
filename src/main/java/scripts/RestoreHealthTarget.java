package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.cards.Minion;

public class RestoreHealthTarget extends SpellBehavior {
    private final int amount;

    public RestoreHealthTarget(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        if (gameObject instanceof MinionObject) {
            MinionObject minionObject = (MinionObject) gameObject;
            if (minionObject.getHp() < ((Minion) minionObject.getCardModel()).getHp()) {
                minionObject.setHp(minionObject.getHp() + amount);
            }
        } else {
            HeroObject heroObject = (HeroObject) gameObject;
            if (heroObject.getHp() < heroObject.getHeroModel().getHp()) {
                heroObject.setHp(heroObject.getHp() + amount);
            }
        }
        return true;
    }
}
