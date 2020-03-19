package ir.soroushtabesh.hearthstone.models.beans;

import ir.soroushtabesh.hearthstone.util.DBUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "players_cards", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<Card> ownedCards;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "players_heroes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "hero_id"))
    private List<Hero> openHeroes;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "players_decks", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "deck_id"))
    private List<Deck> decks;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_hero_id")
    private Hero currentHero;

    public Player(String username, String password) {
        this();
        this.username = username;
        this.password = password;
        //todo: card,hero,deck init
    }

    public Hero getCurrentHero() {
        return currentHero;
    }

    public Player() {
        ownedCards = new ArrayList<>();
        openHeroes = new ArrayList<>();
        decks = new ArrayList<>();
    }

    public void setCurrentHero(Hero currentHero) {
        this.currentHero = currentHero;
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

    public List<Card> getOwnedCards() {
        return ownedCards;
    }

    public void addOwnedCard(Card card) {
        ownedCards.add(card);
    }

    public boolean removeOwnedCard(Card card) {
        return ownedCards.remove(card);
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

    public Deck getDeckOfHero(Hero hero) {
        if (hero == null)
            return null;
        for (Deck deck : decks) {
            if (deck.getHero().getHero_id() == hero.getHero_id())
                return deck;
        }
        Deck deck = new Deck(hero, this);
        decks.add(deck);
        DBUtil.syncSingleObject(this);
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
