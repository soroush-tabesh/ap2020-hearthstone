package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.ScriptEngine;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.security.SecureRandom;

public abstract class GameController {

    private final ModelPool modelPool;
    private final ScriptEngine scriptEngine;
    private final BooleanProperty started = new SimpleBooleanProperty(false);
    private final SecureRandom random = new SecureRandom();

    public GameController() {
        modelPool = new ModelPool(this);
        scriptEngine = new ScriptEngine(this);
    }

    public final ModelPool getViewModels() {
        return modelPool;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public boolean isStarted() {
        return started.get();
    }

    public ReadOnlyBooleanProperty startedProperty() {
        return started;
    }

    protected void setStarted() {
        this.started.set(true);
    }

    public int generateID(GameObject gameObject) {
        return random.nextInt();
    }

    public abstract PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive);

    protected abstract boolean endTurn(int playerId, long token);

    protected abstract void startGame(int playerId, long token);

    protected abstract Message drawCard(CardObject cardObject, GameObject optionalTarget, int playerId, long token);

    protected abstract Message playMinion(MinionObject source, MinionObject target, int playerId, long token);

    public enum Message {
        IMPOSSIBLE, SUCCESS, ERROR, FULL, INSUFFICIENT
    }

}
