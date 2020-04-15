package ir.soroushtabesh.hearthstone.models.scripts;

import ir.soroushtabesh.hearthstone.models.Script;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("HeroPower")
public class HeroPower extends Script {
}
