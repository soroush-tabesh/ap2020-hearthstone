package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;

public class LocalGameController extends GameController {

    @Override
    public PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive) {
        return null;
    }

    @Override
    protected boolean endTurn(int playerId, long token) {
        return false;
    }

    @Override
    protected void startGame(int playerId, long token) {
        setStarted();
    }

    @Override
    protected Message drawCard(CardObject cardObject, GameObject optionalTarget, int playerId, long token) {
        return null;
    }

    @Override
    protected Message playMinion(MinionObject source, MinionObject target, int playerId, long token) {
        return null;
    }
}
