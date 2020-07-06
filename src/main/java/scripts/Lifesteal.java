package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class Lifesteal extends MinionBehavior {
    @Override
    public void onAttackEffect(GameObject gameObject) {
        super.onAttackEffect(gameObject);
        HeroObject hero = getGameController().getModelPool().getPlayerDataById(getPlayerController().getId()).getHero();
        hero.setHp(Math.min(hero.getHp() + ((MinionObject) getOwnerObject()).getAttackPower(), hero.getHeroModel().getHp()));
    }
}
