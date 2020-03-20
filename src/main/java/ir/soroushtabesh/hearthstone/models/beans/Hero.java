package ir.soroushtabesh.hearthstone.models.beans;

import ir.soroushtabesh.hearthstone.models.beans.scripts.HeroPower;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Hero {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hero_id")
    private int hero_id;
    private String name;
    @Enumerated(EnumType.STRING)
    private HeroClass heroClass;
    private Integer hp;
    @ManyToOne//(cascade = javax.persistence.CascadeType.ALL)
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "heropower_id")
    private HeroPower heroPower;
    @OneToMany//(cascade = javax.persistence.CascadeType.ALL)
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "hero_speccard", joinColumns = @JoinColumn(name = "hero_id"), inverseJoinColumns = @JoinColumn(name = "speccard_id"))
    private List<Card> specialCards;

    public Hero(String name, HeroClass heroClass, int hp, HeroPower heroPower, List<Card> specialCards, Script specialPower) {
        this.name = name;
        this.heroClass = heroClass;
        this.hp = hp;
        this.heroPower = heroPower;
        this.specialCards = specialCards;
        this.specialPower = specialPower;
    }

    @ManyToOne//(cascade = javax.persistence.CascadeType.ALL)
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "specpower_script_id")
    private Script specialPower;

    public Hero() {
        specialCards = new ArrayList<>();
    }


    public void addSpecialCard(Card card) {
        specialCards.add(card);
    }

    public int getHero_id() {
        return hero_id;
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

    public Script getSpecialPower() {
        return specialPower;
    }

    public void setSpecialPower(Script specialPower) {
        this.specialPower = specialPower;
    }

    @Override
    public String toString() {
        return "Hero" + "\n" +
                "Name: " + name + "\n" +
                "HP: " + hp + "\n" +
                "Class: " + heroClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return getHero_id() == hero.getHero_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHero_id());
    }

    public enum HeroClass {
        MAGE, WARLOCK, ROUGE, DEMON_HUNTER, DRUID, HUNTER, PALADIN, PRIEST, SHAMAN, WARRIOR, ALL
    }
}
