package ir.soroushtabesh.hearthstone.controllers.game.scripts.custom;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

import java.util.ArrayList;
import java.util.List;

public class TargetAdd extends SpellBehavior {
    private final List<MinionBehavior> minionBehaviors = new ArrayList<>();

    public TargetAdd add(MinionBehavior minionBehavior) {
        minionBehaviors.add(minionBehavior);
        return this;
    }
}
