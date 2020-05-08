package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.models.scripts.HeroPower;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private HeroClass heroClass;

    private Integer hp;

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private HeroPower heroPower;

    @OneToMany
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "hero_id")
    private List<Card> specialCards = new ArrayList<>();

    @ManyToOne
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private ScriptModel specialPower;

    public Hero(String name, HeroClass heroClass, int hp, HeroPower heroPower, List<Card> specialCards, ScriptModel specialPower) {
        this.name = name;
        this.heroClass = heroClass;
        this.hp = hp;
        this.heroPower = heroPower;
        this.specialCards = specialCards;
        this.specialPower = specialPower;
    }

    public Hero() {
    }


    public void addSpecialCard(Card card) {
        specialCards.add(card);
    }

    public Integer getId() {
        return id;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public void setHeroClass(HeroClass heroClass) {
        this.heroClass = heroClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public HeroPower getHeroPower() {
        return heroPower;
    }

    public void setHeroPower(HeroPower heroPower) {
        this.heroPower = heroPower;
    }

    public List<Card> getSpecialCards() {
        return specialCards;
    }

    public ScriptModel getSpecialPower() {
        return specialPower;
    }

    public void setSpecialPower(ScriptModel specialPower) {
        this.specialPower = specialPower;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, heroClass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return getId().equals(hero.getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public enum HeroClass {
        MAGE, WARLOCK, ROUGE, DEMON_HUNTER, DRUID, HUNTER, PALADIN, PRIEST, SHAMAN, WARRIOR, ALL
    }
}
