package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.ScriptModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HeroPower extends Card {

    public HeroPower() {
        setScriptModel(new ScriptModel(new SpellBehavior()));
    }

    public HeroPower(String card_name, String description, Integer mana
            , Hero.HeroClass heroClass, Integer price, Rarity rarity) {
        super(card_name, description, mana, heroClass, price, rarity);
        setScriptModel(new ScriptModel(new SpellBehavior()));
        setTradable(false);
        setDeckAssociative(false);
    }

    @Override
    public String toString() {
        return "HeroPower{}";
    }
}
