package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;

public class ManaPowerUp extends SpellBehavior {
    private final int mana;
    private final int manaMax;

    public ManaPowerUp(int mana, int manaMax) {
        this.mana = mana;
        this.manaMax = manaMax;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getId());
        playerData.setManaMax(Math.max(0, Math.min(10, playerData.getManaMax() + manaMax)));
        playerData.setMana(Math.max(0, Math.min(10, playerData.getMana() + mana)));
        return true;
    }
}
