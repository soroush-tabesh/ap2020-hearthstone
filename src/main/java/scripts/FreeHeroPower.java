package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroPowerObject;

public class FreeHeroPower extends SpellBehavior {
    private transient boolean enabled = true;
    private transient HeroPowerObject heroPower;
    private transient int mana;

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        heroPower = getGameController().getModelPool()
                .getPlayerDataById(getPlayerController().getPlayerId()).getHero().getHeroPower();
        mana = heroPower.getManaCost();
        heroPower.setManaCost(0);
        return true;
    }

    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        if (!enabled)
            return;
        enabled = false;
        heroPower.setManaCost(mana);
    }
}
