package ir.soroushtabesh.hearthstone.models.beans;

import javax.persistence.*;

@Entity
@Table(name = "players")
public class Player {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int user_id;
    private String username;
    private String password;
    private int coin = 50;
    private boolean deleted = false;
    //todo: cards and heroes and current hero
//    @ManyToMany(cascade = CascadeType.MERGE)
//    @JoinTable(name = "players_cards",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn("card_id"))
//    List<Card> openCards;
//    @ManyToMany(cascade = CascadeType.MERGE)
//    @JoinTable(name = "players_heroes",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn("hero_id"))
//    List<Hero> openHeroes;


    public Player() {
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoin() {
        return coin;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

}
