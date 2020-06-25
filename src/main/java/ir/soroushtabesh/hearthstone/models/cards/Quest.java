package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.models.Hero;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quest extends Spell {

    public Quest() {
    }

    public Quest(String card_name, String description, Integer mana, Hero.HeroClass heroClass, Integer price, Rarity rarity) {
        super(card_name, description, mana, heroClass, price, rarity);
    }

}
