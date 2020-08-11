package ir.soroushtabesh.hearthstone.models;

import java.io.Serializable;

public class BriefHero implements Serializable {

    private static final long serialVersionUID = -3625789241555460828L;
    private final Hero hero;
    private String name;
    private int hp;

    private BriefHero(Hero hero) {
        this.hero = hero;
    }

    public static BriefHero build(Hero hero) {
        BriefHero briefHero = new BriefHero(hero);
        briefHero.refresh();
        return briefHero;
    }

    public Hero getHero() {
        return hero;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    private void refresh() {
        hp = hero.getHp();
        name = hero.getName();
    }
}
