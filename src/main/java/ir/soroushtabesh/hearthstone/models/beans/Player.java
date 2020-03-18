package ir.soroushtabesh.hearthstone.models.beans;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Player {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int user_id;

    public int getUser_id() {
        return user_id;
    }
}
