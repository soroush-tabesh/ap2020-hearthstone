package ir.soroushtabesh.hearthstone.models.beans;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "RawScript")
public abstract class Script {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "script_id")
    private int script_id;

    public int getScript_id() {
        return script_id;
    }
}
