package ir.soroushtabesh.hearthstone.models.beans;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Script script = (Script) o;
        return getScript_id() == script.getScript_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getScript_id());
    }
}
