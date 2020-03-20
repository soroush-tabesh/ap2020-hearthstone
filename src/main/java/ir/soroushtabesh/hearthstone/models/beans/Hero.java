package ir.soroushtabesh.hearthstone.models.beans;

import ir.soroushtabesh.hearthstone.models.beans.cards.HeroPower;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Hero {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hero_id")
    private int hero_id;
    @Enumerated(EnumType.STRING)
    private HeroClass heroClass;
    private String name;
    private Integer hp;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "heropower_id")
    private HeroPower heroPower;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "hero_speccard", joinColumns = @JoinColumn(name = "hero_id"), inverseJoinColumns = @JoinColumn(name = "speccard_id"))
    private Set<Card> specialCards;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "specpower_script_id")
    private Script specialPower;

    public Hero() {
        specialCards = new HashSet<>();
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

    public Set<Card> getSpecialCards() {
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
        return "Hero{" +
                "hero_id=" + hero_id +
                ", heroClass=" + heroClass +
                ", name='" + name + '\'' +
                ", hp=" + hp +
                ", heroPower=" + heroPower +
                ", specialPower=" + specialPower +
                '}';
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
