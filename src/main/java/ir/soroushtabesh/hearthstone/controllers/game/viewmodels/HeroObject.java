package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.models.Hero;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HeroObject extends GameObject {

    private final Hero heroModel;
    private final HeroPowerObject heroPower;
    private final IntegerProperty hp = new SimpleIntegerProperty();
    private final ObjectProperty<WeaponObject> currentWeapon = new SimpleObjectProperty<>();
    private final GenericScript specialPowerScript;

    public HeroObject(int playerId, GameController gameController, Hero heroModel) {
        super(playerId, gameController);
        this.heroModel = heroModel;
        heroPower = new HeroPowerObject(playerId, gameController, heroModel.getHeroPower());
        hp.set(heroModel.getHp());
        specialPowerScript = heroModel.getSpecialPower().getScript(gameController);
        addMiscScript(specialPowerScript);
    }

    public Hero getHeroModel() {
        return heroModel;
    }

    public HeroPowerObject getHeroPower() {
        return heroPower;
    }

    public int getHp() {
        return hp.get();
    }

    public void setHp(int hp) {
        this.hp.set(hp);
    }

    public IntegerProperty hpProperty() {
        return hp;
    }

    public WeaponObject getCurrentWeapon() {
        return currentWeapon.get();
    }

    public ObjectProperty<WeaponObject> currentWeaponProperty() {
        return currentWeapon;
    }

    public GenericScript getSpecialPowerScript() {
        return specialPowerScript;
    }
}
