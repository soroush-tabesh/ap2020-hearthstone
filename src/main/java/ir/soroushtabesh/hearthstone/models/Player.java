package ir.soroushtabesh.hearthstone.models;

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
public class Player implements Serializable {

    private static final long serialVersionUID = -3493860018461994382L;
    @ManyToMany
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private final List<Hero> openHeroes = new ArrayList<>();
    @OneToMany(mappedBy = "player")
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private final List<Deck> decks = new ArrayList<>();
    @ElementCollection
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private final Map<Card, Integer> ownedCards = new HashMap<>();
    private final PlayerStats playerStats = new PlayerStats();
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String username;
    @Exclude
    private String password;
    private Integer coin = 50;
    private Integer cup = 0;
    private Boolean deleted = false;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player() {
    }

    public Integer getCup() {
        return cup;
    }

    public void setCup(Integer cup) {
        this.cup = cup;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public Integer getId() {
        return id;
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

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<Card> getOwnedCardsList() {
        return new ArrayList<>(ownedCards.keySet());
    }

    public boolean addOwnedCard(Card card) {
        if (ownedCards.getOrDefault(card, 0) >= 2)
            return false;
        ownedCards.put(card, ownedCards.getOrDefault(card, 0) + 1);
        return true;
    }

    public boolean removeOwnedCard(Card card) {
        boolean res = false;
        if (ownedCards.getOrDefault(card, 0) <= 1) {
            res = ownedCards.remove(card) != null;
        } else {
            ownedCards.put(card, ownedCards.getOrDefault(card, 0) - 1);
        }
        for (Deck deck : decks) {
            deck.removeCardOnce(card);
            deck.removeCardOnce(card);
        }
        Logger.log("ownedCards remove" + res,
                card.getName() + " from " + getUsername() + "'s collection");
        return res;
    }

    public int getOwnedAmount(Card card) {
        return ownedCards.getOrDefault(card, 0);
    }

    public List<Hero> getOpenHeroes() {
        return openHeroes;
    }

    public void addOpenHero(Hero hero) {
        openHeroes.add(hero);
    }

    public boolean removeOpenHero(Hero hero) {
        return openHeroes.remove(hero);
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void addDeck(Deck deck) {
        deck.setPlayer(this);
        decks.add(deck);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return getId().equals(player.getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
