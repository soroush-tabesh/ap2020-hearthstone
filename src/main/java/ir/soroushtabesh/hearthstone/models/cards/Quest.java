package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Script;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
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
