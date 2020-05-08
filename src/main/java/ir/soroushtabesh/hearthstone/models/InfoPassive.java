package ir.soroushtabesh.hearthstone.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class InfoPassive {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String description;

    public InfoPassive() {
    }

    public InfoPassive(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
