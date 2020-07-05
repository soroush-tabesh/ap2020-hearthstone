package ir.soroushtabesh.hearthstone.controllers.game.scripts.custom;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class Summoner extends SpellBehavior {//todo
    private final String[] summonList;

    public Summoner(String... summonList) {
        this.summonList = summonList;
    }
}
