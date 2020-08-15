package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;

public class RestoreHealthMyHero extends SpellBehavior {
    private final int amount;

    public RestoreHealthMyHero(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getPlayerId());
        HeroObject hero = playerData.getHero();
        if (hero.getHp() < hero.getHeroModel().getHp()) {
            hero.setHp(Math.min(hero.getHp() + amount, hero.getHeroModel().getHp()));
        }
        return true;
    }
}
