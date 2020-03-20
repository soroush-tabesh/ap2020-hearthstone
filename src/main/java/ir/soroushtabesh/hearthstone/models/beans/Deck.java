package ir.soroushtabesh.hearthstone.models.beans;

import ir.soroushtabesh.hearthstone.util.Logger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    @ManyToOne//(cascade = javax.persistence.CascadeType.ALL)
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "hero_id")
    private Hero hero;
    @ManyToOne//(cascade = javax.persistence.CascadeType.ALL)
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "player_id")
    private Player player;
    @ManyToMany//(cascade = javax.persistence.CascadeType.ALL)
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
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

    public Deck(Hero hero, Player player, List<Card> cardList) {
        this.hero = hero;
        this.player = player;
        this.cardsList = cardList;
    }

    public void setCardsList(List<Card> cardsList) {
        this.cardsList = cardsList;
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

    public boolean addCard(Card card) {
        boolean res = cardsList.add(card);
        Logger.log("deck add", card.getCard_name() + " to " + hero.getName() + "'s deck");
        return res;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deck_id=" + deck_id +
                ", hero=" + hero +
                ", player=" + player +
                '}';
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

    public boolean removeCard(Card card) {
        boolean res = cardsList.remove(card);
        if (res)
            Logger.log("deck remove", card.getCard_name() + " from " + hero.getName() + "'s deck");
        return res;
    }
}
