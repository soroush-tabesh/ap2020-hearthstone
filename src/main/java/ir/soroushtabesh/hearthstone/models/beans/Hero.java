package ir.soroushtabesh.hearthstone.models.beans;

import ir.soroushtabesh.hearthstone.models.beans.cards.HeroPower;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "heropower_id")
    private HeroPower heroPower;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "hero_speccard", joinColumns = @JoinColumn(name = "hero_id"), inverseJoinColumns = @JoinColumn(name = "speccard_id"))
    private List<Card> specialCards;
    @ManyToOne(cascade = CascadeType.ALL)
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

    public enum HeroClass {
        MAGE, WARLOCK, ROUGE, DEMON_HUNTER, DRUID, HUNTER, PALADIN, PRIEST, SHAMAN, WARRIOR
    }

}
