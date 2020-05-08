package ir.soroushtabesh.hearthstone.models;

public class BriefHero {

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
