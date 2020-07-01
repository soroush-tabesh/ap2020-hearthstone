package ir.soroushtabesh.hearthstone.controllers.game.scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;

@FunctionalInterface
public interface GameEventListener {
    void onEvent(GameAction gameAction);
}
