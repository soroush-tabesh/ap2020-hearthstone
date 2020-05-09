package ir.soroushtabesh.hearthstone.models;

import org.hibernate.Session;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String card_name = "";

    private String description = "";

    private Integer mana = 0;

    @Enumerated(EnumType.STRING)
    private Hero.HeroClass heroClass = Hero.HeroClass.ALL;

    private Integer price = 0;

    @Enumerated(EnumType.STRING)
    private Rarity rarity = Rarity.COMMON;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private ScriptModel scriptModel;

    public Card() {
    }

    public Card(String card_name, String description, Integer mana, Hero.HeroClass heroClass, Integer price, Rarity rarity) {
        this.card_name = card_name;
        this.description = description;
        this.mana = mana;
        this.heroClass = heroClass;
        this.price = price;
        this.rarity = rarity;
    }

    public static Card getCardByName(String cardname, Session session) {
        return session.createQuery("from Card where card_name=:cardname", Card.class)
                .setParameter("cardname", cardname).uniqueResult();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Hero.HeroClass getHeroClass() {
        return heroClass;
    }

    public void setHeroClass(Hero.HeroClass heroClass) {
        this.heroClass = heroClass;
    }

    public ScriptModel getScriptModel() {
        return scriptModel;
    }

    public void setScriptModel(ScriptModel scriptModel) {
        this.scriptModel = scriptModel;
    }

    @Override
    public String toString() {
        return "Card\n" +
                "Name: " + card_name + '\n' +
                "Description: " + description + '\n' +
                "Mana: " + mana + '\n' +
                "Price: " + price + '\n' +
                "Rarity: " + rarity + '\n' +
                "HeroClass: " + heroClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getId().equals(card.getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public enum Rarity {
        FREE, COMMON, RARE, EPIC, LEGENDARY
    }
}
