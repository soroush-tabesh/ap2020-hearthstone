package ir.soroushtabesh.hearthstone.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class ScriptModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    //will further contain a reference to a controller script
    //this class is just a model to load the specified controller

    @Override
    public String toString() {
        return "Script{" +
                "script_id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScriptModel that = (ScriptModel) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
