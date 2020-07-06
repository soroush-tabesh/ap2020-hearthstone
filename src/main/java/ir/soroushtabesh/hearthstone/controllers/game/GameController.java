package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import javafx.beans.property.*;

import java.util.List;

public abstract class GameController {

    private final ModelPool modelPool;
    private final ScriptEngine scriptEngine;
    private final BooleanProperty started = new SimpleBooleanProperty(false);
    private final BooleanProperty gameReady = new SimpleBooleanProperty(false);
    private final IntegerProperty turn = new SimpleIntegerProperty(-1);
    private final IntegerProperty winner = new SimpleIntegerProperty(-1);

    public GameController() {
        scriptEngine = new ScriptEngine(this);
        modelPool = new ModelPool(this);
        scriptEngine.init();
    }

    public boolean isGameReady() {
        return gameReady.get();
    }

    protected void setGameReady(boolean gameReady) {
        this.gameReady.set(gameReady);
    }

    public ReadOnlyBooleanProperty gameReadyProperty() {
        return gameReady;
    }

    public final ModelPool getModelPool() {
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

    protected void setStarted(boolean started) {
        this.started.set(started);
    }

    public int getTurn() {
        return turn.get();
    }

    public void setTurn(int turn) {
        this.turn.set(turn);
    }

    public IntegerProperty turnProperty() {
        return turn;
    }

    public int getWinner() {
        return winner.get();
    }

    protected void setWinner(int winner) {
        this.winner.set(winner);
    }

    public ReadOnlyIntegerProperty winnerProperty() {
        return winner;
    }

    public abstract PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive, boolean shuffle);

    public abstract PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive, List<Card> cardOrder);

    protected abstract PlayerController[] getAllPlayerControllers();

    protected abstract boolean endTurn(int playerId, int token);

    protected abstract Message startGame(int playerId, int token);

    protected abstract Message playCard(CardObject cardObject, int groundIndex, GameObject optionalTarget, int playerId, int token);

    protected abstract Message summonMinion(MinionObject source, int playerId, int token);

    protected abstract Message playMinion(MinionObject source, GameObject target, int playerId, int token);

    protected abstract Message useWeapon(HeroObject source, GameObject target, int playerId, int token);

    protected abstract Message useHeroPower(HeroObject source, GameObject target, int playerId, int token);

    protected abstract void logEvent(GameAction gameAction);

    protected abstract Message changeCard(int cardNumberInList, int playerId, int token);

    public enum Message {
        IMPOSSIBLE, SUCCESS, ERROR, FULL, INSUFFICIENT
    }

}
