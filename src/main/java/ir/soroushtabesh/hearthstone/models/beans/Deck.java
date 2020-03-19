package ir.soroushtabesh.hearthstone.models.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Deck {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "deck_id")
    private int deck_id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hero_id")
    private Hero hero;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "deck_card", joinColumns = @JoinColumn(name = "deck_id"), inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<Card> cardsList;

    public Deck() {
        cardsList = new ArrayList<>();
    }

    public Deck(Hero hero, Player player) {
        this.hero = hero;
        this.player = player;
        this.cardsList = new ArrayList<>();
    }

    public int getDeck_id() {
        return deck_id;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Card> getCardsList() {
        return cardsList;
    }

    public void addCard(Card card) {
        cardsList.add(card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return getDeck_id() == deck.getDeck_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeck_id());
    }
}
