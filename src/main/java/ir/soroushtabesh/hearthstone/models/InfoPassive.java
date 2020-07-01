package ir.soroushtabesh.hearthstone.models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class InfoPassive {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private ScriptModel scriptModel;

    public InfoPassive() {
    }

    public InfoPassive(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ScriptModel getScriptModel() {
        return scriptModel;
    }

    public void setScriptModel(ScriptModel scriptModel) {
        this.scriptModel = scriptModel;
    }

    @Override
    public String toString() {
        return name;
    }
}
