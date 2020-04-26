package ir.soroushtabesh.hearthstone.models.scripts;

import ir.soroushtabesh.hearthstone.models.ScriptModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HeroPower extends ScriptModel {
    public HeroPower() {
    }

    @Override
    public String toString() {
        return "HeroPower{}";
    }
}
