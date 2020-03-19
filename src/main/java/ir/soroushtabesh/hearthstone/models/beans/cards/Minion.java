package ir.soroushtabesh.hearthstone.models.beans.cards;

import ir.soroushtabesh.hearthstone.models.beans.Card;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("Minion")
public class Minion extends Card {
    private Integer hp;
    private Integer attackPower;
    @Enumerated(EnumType.STRING)
    private MinionClass minionClass;

    public enum MinionClass {
        BEAST, DEMON, DRAGON, ELEMENTAL, MECH, MURLOC, PIRATE, TOTEM, ALL, GENERAL
    }
}
