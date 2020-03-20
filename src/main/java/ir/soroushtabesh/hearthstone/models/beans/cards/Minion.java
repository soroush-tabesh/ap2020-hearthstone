package ir.soroushtabesh.hearthstone.models.beans.cards;

import ir.soroushtabesh.hearthstone.models.beans.Card;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("Minion")
public class Minion extends Card {
    private Integer hp;
    private Integer attackPower;
    @Enumerated(EnumType.STRING)
    private MinionClass minionClass;

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(Integer attackPower) {
        this.attackPower = attackPower;
    }

    public MinionClass getMinionClass() {
        return minionClass;
    }

    public void setMinionClass(MinionClass minionClass) {
        this.minionClass = minionClass;
    }

    @Override
    public String toString() {//todo
        return super.toString() + "\nType: Minion [" +
                "HP: " + hp +
                ", AttackPower: " + attackPower +
                ", MinionClass: " + minionClass +
                ']';
    }

    public enum MinionClass {
        BEAST, DEMON, DRAGON, ELEMENTAL, MECH, MURLOC, PIRATE, TOTEM, ALL, GENERAL
    }
}
