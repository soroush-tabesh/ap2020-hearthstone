package ir.soroushtabesh.hearthstone.models.beans;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "Card")
public abstract class Card {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "card_id")
    private int card_id;
    private String card_name;
    private String description;
    private Integer mana;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private Rarity rarity;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hero_id")
    private Hero heroClass;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "script_id")
    private Script script;

    public Card() {
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

    public Hero getHeroClass() {
        return heroClass;
    }

    public void setHeroClass(Hero heroClass) {
        this.heroClass = heroClass;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public enum Rarity {
        COMMON, RARE, EPIC, LEGENDARY
    }

}
