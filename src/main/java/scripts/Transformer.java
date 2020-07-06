package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class Transformer extends SpellBehavior {//todo
    private final String transformTo;

    public Transformer(String transformTo) {
        this.transformTo = transformTo;
    }
}
