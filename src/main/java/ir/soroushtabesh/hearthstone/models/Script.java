package ir.soroushtabesh.hearthstone.models;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "RawScript")
public class Script {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "script_id")
    private int script_id;

    public Script() {
    }

    public int getScript_id() {
        return script_id;
    }

    @Override
    public String toString() {
        return "Script{" +
                "script_id=" + script_id +
                '}';
    }

}
