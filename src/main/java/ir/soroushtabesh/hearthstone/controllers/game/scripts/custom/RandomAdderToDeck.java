package ir.soroushtabesh.hearthstone.controllers.game.scripts.custom;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomAdderToDeck extends SpellBehavior {//todo

    private final List<GenericScript> scriptsToAdd = new ArrayList<>();

    public RandomAdderToDeck() {
    }

    public RandomAdderToDeck(GenericScript... minionBehaviors) {
        scriptsToAdd.addAll(Arrays.asList(minionBehaviors));
    }

}
