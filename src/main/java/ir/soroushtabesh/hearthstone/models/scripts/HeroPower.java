package ir.soroushtabesh.hearthstone.models.scripts;

import ir.soroushtabesh.hearthstone.models.ScriptModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HeroPower extends ScriptModel {
    private String name;
    private String description;

    public HeroPower() {
    }

    public HeroPower(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "HeroPower{}";
    }
}
