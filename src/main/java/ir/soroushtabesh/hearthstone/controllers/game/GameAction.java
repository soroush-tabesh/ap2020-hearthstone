package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;

public abstract class GameAction {

    private final String message;

    public GameAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static class MinionAttack extends GameAction {
        private final MinionObject source;
        private final GameObject target;

        MinionAttack(MinionObject source, MinionObject target) {
            super(String.format("'%s(Pl.%d)' attacked '%s(Pl.%d)'"
                    , source.getCardModel().getName()
                    , source.getPlayerId()
                    , target.getCardModel().getName()
                    , target.getPlayerId()));
            this.source = source;
            this.target = target;
        }

        MinionAttack(MinionObject source, HeroObject target) {
            super(String.format("'%s(Pl.%d)' attacked '%s(Pl.%d)'"
                    , source.getCardModel().getName()
                    , source.getPlayerId()
                    , target.getHeroModel().getName()
                    , target.getPlayerId()));
            this.source = source;
            this.target = target;
        }

        public MinionObject getSource() {
            return source;
        }

        public GameObject getTarget() {
            return target;
        }
    }

    public static class CardPlay extends GameAction {
        private final CardObject source;

        CardPlay(CardObject source) {
            super(String.format("Card '%s(Pl.%d)' played"
                    , source.getCardModel().getName()
                    , source.getPlayerId()));
            this.source = source;
        }

        public CardObject getSource() {
            return source;
        }
    }

    public static class SpellGlobal extends GameAction {
        private final SpellObject source;

        SpellGlobal(SpellObject source) {
            super(String.format("Global Spell '%s'(Pl.%d)"
                    , source.getCardModel().getName(), source.getPlayerId()));
            this.source = source;
        }

        public SpellObject getSource() {
            return source;
        }
    }

    public static class TargetedAttack extends GameAction {
        private final CardObject source;
        private final GameObject target;

        TargetedAttack(CardObject source, GameObject target) {
            super(String.format("%s '%s(Pl.%d)' on '%s(Pl.%d)'"
                    , source.getCardModel().getClass().getSimpleName()
                    , source.getCardModel().getName()
                    , source.getPlayerId()
                    , target instanceof MinionObject ? ((MinionObject) target).getCardModel().getName()
                            : ((HeroObject) target).getHeroModel().getName()
                    , target.getPlayerId()));
            this.source = source;
            this.target = target;
        }

        public CardObject getSource() {
            return source;
        }

        public GameObject getTarget() {
            return target;
        }
    }

    public static class GameStart extends GameAction {
        GameStart() {
            super("Game started.");
        }
    }

    public static class EndTurn extends GameAction {
        EndTurn(int playerId) {
            super(String.format("End of Turn for Pl.%d.", playerId));
        }
    }

    public static class HeroPower extends GameAction {
        HeroPower(HeroPowerObject cardObject) {
            super(String.format("Hero Power '%s(Pl.%d)' used."
                    , cardObject.getCardModel().getName()
                    , cardObject.getPlayerId()));
        }
    }
}
