package ir.soroushtabesh.hearthstone.models.beans.scripts;

import ir.soroushtabesh.hearthstone.models.beans.Script;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("HeroPower")
public class HeroPower extends Script {
}
