package ir.soroushtabesh.hearthstone.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String card_name = "";
    private String description = "";

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.SAVE_UPDATE, CascadeType.REFRESH})
    private ScriptModel rewardScript;

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ScriptModel getRewardScript() {
        return rewardScript;
    }

    public void setRewardScript(ScriptModel reward) {
        this.rewardScript = reward;
    }

    @Override
    public String toString() {
        return super.toString() + "\nType: Quest";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quest quest = (Quest) o;
        return getId().equals(quest.getId()) &&
                getCard_name().equals(quest.getCard_name());
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
