package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class InfoPassive implements Serializable {
    private static final long serialVersionUID = -2586946767926388312L;
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private ScriptModel scriptModel = new ScriptModel(new HeroBehavior());

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
