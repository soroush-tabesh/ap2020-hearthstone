package ir.soroushtabesh.hearthstone.models.beans.cards;

import ir.soroushtabesh.hearthstone.models.beans.Card;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Quest")
public class Quest extends Card {
    @Override
    public String toString() {
        return super.toString() + "\nType: Quest";
    }
}
