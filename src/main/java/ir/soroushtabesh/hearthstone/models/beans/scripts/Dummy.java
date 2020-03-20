package ir.soroushtabesh.hearthstone.models.beans.scripts;

import ir.soroushtabesh.hearthstone.models.beans.Script;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Dummy")
public class Dummy extends Script {
    public Dummy() {
    }

    @Override
    public String toString() {
        return super.toString() + "Dummy{}";
    }
}
