package ir.soroushtabesh.hearthstone.models.cards;

import ir.soroushtabesh.hearthstone.models.ScriptModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quest extends Spell {
    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE, CascadeType.REFRESH})
    private ScriptModel rewardScript;

    public ScriptModel getRewardScript() {
        return rewardScript;
    }

    public void setRewardScript(ScriptModel reward) {
        this.rewardScript = reward;
    }

}
