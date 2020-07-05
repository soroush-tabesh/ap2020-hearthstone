package ir.soroushtabesh.hearthstone.controllers.game.scripts.custom;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class ManaPowerUp extends SpellBehavior {
    private int mana, manaMax;

    public ManaPowerUp(int mana, int manaMax) {
        this.mana = mana;
        this.manaMax = manaMax;
    }
}
