package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.models.Card;

import javax.persistence.Entity;

@Entity
public class Spell extends Card {
    @Override
    public String toString() {
        return super.toString() + "\nType: Spell";
    }
}
