package ir.soroushtabesh.hearthstone.models.beans.cards;

import ir.soroushtabesh.hearthstone.models.beans.Card;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Weapon")
public class Weapon extends Card {
    private Integer durability;
    private Integer attackPower;
}
