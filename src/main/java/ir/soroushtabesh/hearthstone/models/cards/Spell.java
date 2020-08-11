package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.ScriptModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Spell extends Card {

    private static final long serialVersionUID = 8245206235590360156L;

    public Spell() {
        setScriptModel(new ScriptModel(new SpellBehavior()));
    }

    public Spell(String card_name, String description, Integer mana
            , Hero.HeroClass heroClass, Integer price, Rarity rarity) {
        super(card_name, description, mana, heroClass, price, rarity);
        setScriptModel(new ScriptModel(new SpellBehavior()));
    }

    @Override
    public String toString() {
        return super.toString() + "\nType: Spell";
    }
}
