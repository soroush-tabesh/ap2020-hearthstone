package ir.soroushtabesh.hearthstone.models.beans.cards;

import ir.soroushtabesh.hearthstone.models.beans.Card;
import ir.soroushtabesh.hearthstone.models.beans.Script;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("Quest")
public class Quest extends Card {
    @ManyToOne//(cascade = javax.persistence.CascadeType.ALL)
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "reward_script_id")
    private Script reward;

    @Override
    public String toString() {
        return super.toString() + "\nType: Quest";
    }
}
