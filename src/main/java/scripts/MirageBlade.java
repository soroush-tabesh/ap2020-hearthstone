package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.WeaponObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class MirageBlade extends HeroBehavior implements ChangeListener<WeaponObject> {
    @Override
    public void onCardPlay() {
        super.onCardPlay();
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getId());
        HeroObject hero = playerData.getHero();
        hero.setImmune(hero.getImmune() + 1);
        hero.currentWeaponProperty().addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends WeaponObject> observable, WeaponObject oldValue, WeaponObject newValue) {
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getId());
        HeroObject hero = playerData.getHero();
        hero.setImmune(hero.getImmune() - 1);
        hero.currentWeaponProperty().removeListener(this);
    }
}
