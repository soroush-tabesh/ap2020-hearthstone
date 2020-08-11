package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.ScriptModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quest extends Spell {

    private static final long serialVersionUID = -102855245924033686L;

    public Quest() {
        setScriptModel(new ScriptModel(new SpellBehavior()));
    }

    public Quest(String card_name, String description, Integer mana, Hero.HeroClass heroClass, Integer price, Rarity rarity) {
        super(card_name, description, mana, heroClass, price, rarity);
        setScriptModel(new ScriptModel(new SpellBehavior()));
    }

}
