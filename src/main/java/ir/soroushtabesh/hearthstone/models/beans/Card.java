package ir.soroushtabesh.hearthstone.models.beans;

import ir.soroushtabesh.hearthstone.util.DBUtil;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "Card")
public class Card {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "card_id")
    private int card_id;
    private String card_name;
    private String description;
    private Integer mana;

    @Enumerated(EnumType.STRING)
    private Hero.HeroClass heroClass;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private Rarity rarity;
    @ManyToOne//(cascade = CascadeType.ALL)
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "script_id")
    private Script script;

    public Card() {
    }

    public static Card getCardByName(String cardname) {
        Card card = null;
        try (Session session = DBUtil.openSession()) {
            card = session.createQuery("from Card where card_name=:cardname", Card.class)
                    .setParameter("cardname", cardname).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return card;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public int getCard_id() {
        return card_id;
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

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    @Override
    public String toString() {//todo
        return "Card{" +
                "card_id=" + card_id +
                ", card_name='" + card_name + '\'' +
                ", description='" + description + '\'' +
                ", mana=" + mana +
                ", price=" + price +
                ", rarity=" + rarity +
                ", heroClass=" + heroClass +
                ", script=" + script +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getCard_id() == card.getCard_id() &&
                Objects.equals(getCard_name(), card.getCard_name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCard_id());
    }

    public enum Rarity {
        COMMON, RARE, EPIC, LEGENDARY
    }
}
