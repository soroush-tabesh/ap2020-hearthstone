package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class Bomb extends SpellBehavior {
    private int damage;
    private boolean friendlyFire = true;

    public Bomb(int damage) {
        this.damage = damage;
    }

    public Bomb(int damage, boolean friendlyFire) {
        this.damage = damage;
        this.friendlyFire = friendlyFire;
    }
}
