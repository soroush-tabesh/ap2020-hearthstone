package ir.soroushtabesh.hearthstone.controllers.game.scripts.custom;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;

public class PowerUpMinion extends MinionBehavior {
    private int hp, attack;

    public PowerUpMinion(int hp, int attack) {
        this.hp = hp;
        this.attack = attack;
    }
}
