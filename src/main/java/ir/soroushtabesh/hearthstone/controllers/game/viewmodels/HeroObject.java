package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.models.Hero;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class HeroObject extends GameObject {

    private Hero heroModel;
    private final HeroPowerObject heroPower;
    private final SimpleIntegerProperty hp = new SimpleIntegerProperty();
    private final SimpleIntegerProperty shield = new SimpleIntegerProperty();
    private final SimpleIntegerProperty immune = new SimpleIntegerProperty();
    private final SimpleObjectProperty<WeaponObject> currentWeapon = new SimpleObjectProperty<>();
    private final transient GenericScript specialPowerScript;

    public HeroObject(int playerId, GameController gameController, Hero heroModel) {
        super(playerId, gameController);
        this.heroModel = heroModel;
        heroPower = new HeroPowerObject(playerId, gameController, heroModel.getHeroPower());
        hp.set(heroModel.getHp());
        specialPowerScript = heroModel.getSpecialPower().getScript(gameController);
        addMiscScript(specialPowerScript);
    }

    @Override
    public void update(GameObject gameObject, GameController gameController) {
        super.update(gameObject, gameController);
        HeroObject heroObject = (HeroObject) gameObject;
        heroModel = heroObject.heroModel;
        heroPower.update(heroObject.heroPower, gameController);
        hp.set(heroObject.getHp());
        shield.set(heroObject.getShield());
        immune.set(heroObject.getImmune());
        currentWeapon.set(heroObject.getCurrentWeapon());
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

    public int getShield() {
        return shield.get();
    }

    public void setShield(int shield) {
        this.shield.set(shield);
    }

    public int getImmune() {
        return immune.get();
    }

    public void setImmune(int immune) {
        this.immune.set(immune);
    }

    public IntegerProperty immuneProperty() {
        return immune;
    }

    public IntegerProperty shieldProperty() {
        return shield;
    }

    public WeaponObject getCurrentWeapon() {
        return currentWeapon.get();
    }

    public ObjectProperty<WeaponObject> currentWeaponProperty() {
        return currentWeapon;
    }

    public void setCurrentWeapon(WeaponObject currentWeapon) {
        this.currentWeapon.set(currentWeapon);
    }

    public GenericScript getSpecialPowerScript() {
        return specialPowerScript;
    }
}
