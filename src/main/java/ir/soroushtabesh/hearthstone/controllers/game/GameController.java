package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;

public abstract class GameController {

    private final ModelPool modelPool;

    public GameController(ModelPool modelPool) {
        this.modelPool = modelPool;
    }

    public final ModelPool getViewModels() {
        return modelPool;
    }

    public abstract PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive);

    protected abstract boolean endTurn(int playerId, long token);

    protected abstract void startGame(int playerId, long token);

    protected abstract Message drawCard(CardObject cardObject, GameObject target, int playerId, long token);

    protected abstract Message playMinion(MinionObject source, MinionObject target, int playerId, long token);

    public enum Message {
        IMPOSSIBLE, SUCCESS, ERROR
    }

}
