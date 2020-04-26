package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.util.DBUtil;
import ir.soroushtabesh.hearthstone.util.Logger;
import org.hibernate.Session;
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
public class Player {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String username;
    private String password;
    private Integer coin = 50;
    private Boolean deleted = false;

    @ManyToMany
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private List<Hero> openHeroes = new ArrayList<>();

    @OneToMany(mappedBy = "player")
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private List<Deck> decks = new ArrayList<>();

    @ElementCollection
    @Column(name = "count")
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private Map<Card, Integer> ownedCards = new HashMap<>();

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private Hero currentHero;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player() {
    }

    public Hero getCurrentHero() {
        return currentHero;
    }

    public void setCurrentHero(Hero currentHero) {
        this.currentHero = currentHero;
        Logger.log("select hero", currentHero.getName());
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
        return new ArrayList<Card>(ownedCards.keySet());
    }

    public void addOwnedCard(Card card) {
        ownedCards.put(card, ownedCards.getOrDefault(card, 0) + 1);
    }

    public boolean removeOwnedCard(Card card) {
        boolean res = false;
        if (ownedCards.getOrDefault(card, 0) < 2) {
            res = ownedCards.remove(card) != null;
        } else {
            ownedCards.put(card, ownedCards.getOrDefault(card, 0) - 1);
        }
        Logger.log("ownedCards remove" + res,
                card.getCard_name() + " from " + getUsername() + "'s collection");
        return res;
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

    public Deck getDeckOfHero(Hero hero, Session session) {
        if (hero == null)
            return null;
        for (Deck deck : decks) {
            if (deck.getHero().getId().equals(hero.getId()))
                return deck;
        }
        Deck deck = new Deck(hero, this);
        decks.add(deck);
        DBUtil.pushSingleObject(this, session);
        return deck;
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
