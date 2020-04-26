package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.models.Card;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Minion extends Card {
    private Integer hp = 1;
    private Integer attackPower = 1;
    @Enumerated(EnumType.STRING)
    private MinionClass minionClass = MinionClass.GENERAL;

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
    public String toString() {
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
