package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.models.Card;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Spell extends Card {
    @Override
    public String toString() {
        return super.toString() + "\nType: Spell";
    }
}
