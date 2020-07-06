package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class MyHeroPowerUp extends SpellBehavior {
    private int hp, shield;

    public MyHeroPowerUp(int hp, int shield) {
        this.hp = hp;
        this.shield = shield;
    }
}
