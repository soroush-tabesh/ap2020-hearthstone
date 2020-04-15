package ir.soroushtabesh.hearthstone.models.beans.cards;

import ir.soroushtabesh.hearthstone.models.beans.Card;

import javax.persistence.Entity;

@Entity
public class Spell extends Card {
    @Override
    public String toString() {
        return super.toString() + "\nType: Spell";
    }
}
