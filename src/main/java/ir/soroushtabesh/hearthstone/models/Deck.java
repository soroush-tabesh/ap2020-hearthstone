package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.util.Logger;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    private Hero hero;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    private Player player;

    @ManyToMany
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    private List<Card> cardsList;

    public Deck() {
        cardsList = new ArrayList<>();
    }

    public Deck(Hero hero, Player player) {
        this.hero = hero;
        this.player = player;
        this.cardsList = new ArrayList<>();
    }

    public Deck(Hero hero, Player player, List<Card> cardList) {
        this.hero = hero;
        this.player = player;
        this.cardsList = cardList;
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

    public List<Card> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<Card> cardsList) {
        this.cardsList = cardsList;
    }

    public void addCard(Card card) {
        boolean res = cardsList.add(card);
        Logger.log("deck add", card.getCard_name() + " to " + hero.getName() + "'s deck");
    }

    public boolean removeCard(Card card) {
        boolean res = cardsList.remove(card);
        if (res)
            Logger.log("deck remove", card.getCard_name() + " from " + hero.getName() + "'s deck");
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
