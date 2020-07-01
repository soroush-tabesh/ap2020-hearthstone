package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class WeaponObject extends CardObject {
    private final IntegerProperty attackPower = new SimpleIntegerProperty();
    private final IntegerProperty durability = new SimpleIntegerProperty();

    public WeaponObject(int playerId, GameController gameController, Weapon cardModel) {
        super(playerId, gameController, cardModel);
        attackPower.set(cardModel.getAttackPower());
        durability.set(cardModel.getDurability());
    }

    public int getAttackPower() {
        return attackPower.get();
    }

    public void setAttackPower(int attackPower) {
        this.attackPower.set(attackPower);
    }

    public IntegerProperty attackPowerProperty() {
        return attackPower;
    }

    public int getDurability() {
        return durability.get();
    }

    public void setDurability(int durability) {
        this.durability.set(durability);
    }

    public IntegerProperty durabilityProperty() {
        return durability;
    }
}
