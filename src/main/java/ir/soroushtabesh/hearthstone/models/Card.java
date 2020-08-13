package ir.soroushtabesh.hearthstone.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Card implements Serializable {

    private static final long serialVersionUID = -1046084412697634862L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name = "";

    private String description = "";

    private Integer mana = 0;

    @Enumerated(EnumType.STRING)
    private Hero.HeroClass heroClass = Hero.HeroClass.ALL;

    @Enumerated(EnumType.STRING)
    private ActionType actionType = ActionType.GLOBAL;

    private Integer price = 0;

    private Boolean tradable = true;
    private Boolean deckAssociative = true;

    @Enumerated(EnumType.STRING)
    private Rarity rarity = Rarity.COMMON;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private ScriptModel scriptModel = new ScriptModel();

    public Card() {
    }

    public Card(String card_name, String description, Integer mana
            , Hero.HeroClass heroClass, Integer price, Rarity rarity) {
        this.name = card_name;
        this.description = description;
        this.mana = mana;
        this.heroClass = heroClass;
        this.price = price;
        this.rarity = rarity;
    }

    public Boolean getTradable() {
        return tradable;
    }

    public void setTradable(Boolean tradable) {
        this.tradable = tradable;
    }

    public Boolean getDeckAssociative() {
        return deckAssociative;
    }

    public void setDeckAssociative(Boolean deckAssociable) {
        this.deckAssociative = deckAssociable;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                "Name: " + name + '\n' +
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

    public enum ActionType {
        GLOBAL, TARGETED
    }
}
