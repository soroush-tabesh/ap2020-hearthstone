package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

import java.util.ArrayList;
import java.util.List;

public class MultiSpell extends SpellBehavior {
    private final List<SpellBehavior> minionBehaviors = new ArrayList<>();

    public MultiSpell add(SpellBehavior minionBehavior) {
        minionBehaviors.add(minionBehavior);
        return this;
    }
}
