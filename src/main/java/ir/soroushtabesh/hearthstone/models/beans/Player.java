package ir.soroushtabesh.hearthstone.models.beans;

import ir.soroushtabesh.hearthstone.util.DBUtil;
import ir.soroushtabesh.hearthstone.util.Logger;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Player {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "player_id")
    private int player_id;
    private String username;
    private String password;
    private Integer coin = 50;
    private Boolean deleted = false;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "players_cards", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "card_id"))
    private Set<Card> ownedCards;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "players_heroes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "hero_id"))
    private Set<Hero> openHeroes;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "players_decks", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "deck_id"))
    private Set<Deck> decks;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "current_hero_id")
    private Hero currentHero;

    public Player(String username, String password) {
        this();
        this.username = username;
        this.password = password;
        //todo: card,hero,deck init
    }

    public Player() {
        ownedCards = new HashSet<>();
        openHeroes = new HashSet<>();
        decks = new HashSet<>();
    }

    public Hero getCurrentHero() {
        return currentHero;
    }

    public void setCurrentHero(Hero currentHero) {
        this.currentHero = currentHero;
        Logger.log("select hero", currentHero.getName());
    }

    public int getPlayer_id() {
        return player_id;
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

    public Set<Card> getOwnedCards() {
        return ownedCards;
    }

    public void addOwnedCard(Card card) {
        ownedCards.add(card);
    }

    public boolean removeOwnedCard(Card card) {
        return ownedCards.remove(card);
    }

    public Set<Hero> getOpenHeroes() {
        return openHeroes;
    }

    public void addOpenHero(Hero hero) {
        openHeroes.add(hero);
    }

    public boolean removeOpenHero(Hero hero) {
        return openHeroes.remove(hero);
    }

    public Set<Deck> getDecks() {
        return decks;
    }

    public Deck getDeckOfHero(Hero hero, Session session) {
        if (hero == null)
            return null;
        for (Deck deck : decks) {
            if (deck.getHero().getHero_id() == hero.getHero_id())
                return deck;
        }
        Deck deck = new Deck(hero, this);
        decks.add(deck);
        DBUtil.pushSingleObject(this, session);
        return deck;
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return getPlayer_id() == player.getPlayer_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer_id());
    }
}
