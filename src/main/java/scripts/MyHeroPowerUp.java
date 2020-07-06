package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;

public class MyHeroPowerUp extends SpellBehavior {
    private final int hp;
    private final int shield;

    public MyHeroPowerUp(int hp, int shield) {
        this.hp = hp;
        this.shield = shield;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getId());
        HeroObject hero = playerData.getHero();
        hero.setHp(hero.getHp() + hp);
        hero.setShield(hero.getShield() + shield);
        return true;
    }
}
