package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public abstract class GameAction {

    private final String message;

    public GameAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    static class MinionAttack extends GameAction {
        public MinionAttack(MinionObject source, MinionObject target) {
            super(String.format("%s(Pl.%d) attacked %s(Pl.%d)"
                    , source.getCardModel().getName()
                    , source.getPlayerId()
                    , target.getCardModel().getName()
                    , source.getPlayerId()));
        }

    }

}
