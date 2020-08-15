package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class DealDamage extends SpellBehavior {
    private final int amount;

    public DealDamage(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        if (gameObject instanceof MinionObject) {
            ((LocalGameController) getGameController()).performDamageOnMinion((MinionObject) gameObject, amount
                    , getPlayerController().getToken());
        } else {
            ((LocalGameController) getGameController()).performDamageOnHero((HeroObject) gameObject, amount
                    , getPlayerController().getToken());
        }
        return true;
    }
}
