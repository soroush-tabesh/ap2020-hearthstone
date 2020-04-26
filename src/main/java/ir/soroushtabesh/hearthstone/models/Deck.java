package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.util.Logger;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private Hero hero;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private Player player;

    @ElementCollection
    @Column(name = "count")
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private Map<Card, Integer> cardsInDeck = new HashMap<>();

    public Deck() {
    }

    public Deck(Hero hero, Player player) {
        this.hero = hero;
        this.player = player;
    }

    public Deck(Hero hero, Player player, Map<Card, Integer> cardsInDeck) {
        this.hero = hero;
        this.player = player;
        this.cardsInDeck = cardsInDeck;
    }

    public Integer getId() {
        return id;
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

    public List<Card> getCardsInDeck() {
        return new ArrayList<>(cardsInDeck.keySet());
    }

    public void addCard(Card card) {
        cardsInDeck.put(card, cardsInDeck.getOrDefault(card, 0) + 1);
        Logger.log("deck add", card.getCard_name() + " to " + hero.getName() + "'s deck");
    }

    public boolean removeCard(Card card) {
        boolean res = false;
        if (cardsInDeck.getOrDefault(card, 0) < 2) {
            res = cardsInDeck.remove(card) != null;
        } else {
            cardsInDeck.put(card, cardsInDeck.getOrDefault(card, 0) - 1);
        }
        Logger.log("deck remove" + res, card.getCard_name() + " from " + hero.getName() + "'s deck");
        return res;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck_id=" + id +
                ", hero=" + hero +
                ", player=" + player +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return getId().equals(deck.getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }

}
