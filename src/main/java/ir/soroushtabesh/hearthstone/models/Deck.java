package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.util.Exclude;
import ir.soroushtabesh.hearthstone.util.Logger;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deck implements Serializable {

    private static final long serialVersionUID = -64440247449920895L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name = "untitled deck";

    private Hero.HeroClass heroClass = Hero.HeroClass.ALL;

    private int playerID;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    @Exclude
    private Player player;

    @ElementCollection
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private Map<Card, Integer> cardsInDeck = new HashMap<>();

    private final DeckHistory deckHistory = new DeckHistory();

    public Deck() {
    }

    public Deck(int id, String name, Hero.HeroClass heroClass) {
        this.id = id;
        this.name = name;
        this.heroClass = heroClass;
    }

    public Deck(Hero.HeroClass heroClass, Player player) {
        this.heroClass = heroClass;
        this.playerID = player.getId();
        this.player = player;
    }

    public Deck(Hero.HeroClass heroClass, Player player, Map<Card, Integer> cardsInDeck) {
        this.heroClass = heroClass;
        this.playerID = player.getId();
        this.player = player;
        this.cardsInDeck = cardsInDeck;
    }

    public void setCardsInDeck(Map<Card, Integer> cardsInDeck) {
        this.cardsInDeck = cardsInDeck;
    }


    public Integer getId() {
        return id;
    }

    public Hero.HeroClass getHeroClass() {
        return heroClass;
    }

    public void setHeroClass(Hero.HeroClass heroClass) {
        this.heroClass = heroClass;
    }

    public DeckHistory getDeckHistory() {
        return deckHistory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        if (player != null)
            return player;
        return PlayerManager.getInstance().getPlayerByID(playerID);
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (player != null)
            this.playerID = player.getId();
        else
            this.playerID = 0;
    }

    public List<Card> getCardsInDeck() {
        return new ArrayList<>(cardsInDeck.keySet());
    }

    public Message addCard(Card card) {
        return addCard(card, 1);
    }

    public Message addCard(Card card, int count) {
        if (!card.getHeroClass().equals(Hero.HeroClass.ALL) && !card.getHeroClass().equals(getHeroClass()))
            return Message.INCOMPATIBLE;
        int current = getCountInDeck(card);
        int possess = getPlayer().getOwnedAmount(card);
        if (current + count > Math.min(possess, 2))
            return Message.INSUFFICIENT;
        if (getTotalCountOfCards() >= 30)
            return Message.FULL;
        cardsInDeck.put(card, current + count);
        Logger.log("deck add", card.getName() + " to " + getHeroClass() + "'s deck");
        return Message.SUCCESS;
    }

    public boolean removeCardOnce(Card card) {
        boolean res = true;
        if (getCountInDeck(card) <= 1) {
            res = cardsInDeck.remove(card) != null;
        } else {
            cardsInDeck.put(card, cardsInDeck.getOrDefault(card, 0) - 1);
        }
        if (res)
            Logger.log("deck remove" + res, card.getName() + " from " + heroClass + "'s deck");
        return res;
    }

    public int getCountInDeck(Card card) {
        return cardsInDeck.getOrDefault(card, 0);
    }

    public int getTotalCountOfCards() {
        int res = 0;
        for (Integer value : cardsInDeck.values()) {
            res += value;
        }
        return res;
    }

    public List<Card> getFullDeck() {
        List<Card> res = new ArrayList<>();
        cardsInDeck.forEach((key, value) -> {
            for (int i = 0; i < value; i++) {
                res.add(key);
            }
        });
        return res;
    }

    public boolean isPure() {
        for (Card card : cardsInDeck.keySet()) {
            if (card.getHeroClass() != Hero.HeroClass.ALL)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, heroClass);
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
