package ir.soroushtabesh.hearthstone.models.beans.cards;

import ir.soroushtabesh.hearthstone.models.beans.Card;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Weapon")
public class Weapon extends Card {
    private Integer durability;
    private Integer attackPower;

    @Override
    public String toString() {
        return super.toString() + "\nType: Weapon [" +
                "Durability: " + durability +
                ", AttackPower: " + attackPower +
                ']';
    }

    public Integer getDurability() {
        return durability;
    }

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

    public Integer getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(Integer attackPower) {
        this.attackPower = attackPower;
    }
}
