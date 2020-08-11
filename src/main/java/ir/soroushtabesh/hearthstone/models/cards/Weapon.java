package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Hero;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Weapon extends Card {
    private static final long serialVersionUID = -3828816520320939517L;
    private Integer durability = 1;
    private Integer attackPower = 1;

    public Weapon() {
    }

    public Weapon(String card_name, String description, Integer mana, Hero.HeroClass heroClass, Integer price, Rarity rarity, Integer durability, Integer attackPower) {
        super(card_name, description, mana, heroClass, price, rarity);
        this.durability = durability;
        this.attackPower = attackPower;
    }

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
