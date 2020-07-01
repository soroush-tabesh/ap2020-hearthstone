package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import javafx.beans.property.*;

public abstract class GameController {

    private final ModelPool modelPool;
    private final ScriptEngine scriptEngine;
    private final BooleanProperty started = new SimpleBooleanProperty(false);
    private final BooleanProperty gameReady = new SimpleBooleanProperty(false);
    private final IntegerProperty turn = new SimpleIntegerProperty();
    private final IntegerProperty winner = new SimpleIntegerProperty(-1);

    public GameController() {
        modelPool = new ModelPool(this);
        scriptEngine = new ScriptEngine(this);
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

    public abstract PlayerController registerPlayer(Hero hero, Deck deck, InfoPassive infoPassive);

    protected abstract PlayerController[] getAllPlayerControllers();

    protected abstract boolean endTurn(int playerId, int token);

    protected abstract void startGame(int playerId, int token);

    protected abstract Message playCard(CardObject cardObject, int groundIndex, GameObject optionalTarget, int playerId, int token);

    protected abstract Message summonMinion(MinionObject source, int playerId, int token);

    protected abstract Message playMinion(MinionObject source, GameObject target, int playerId, int token);

    protected abstract Message useWeapon(HeroObject source, GameObject target, int playerId, int token);

    protected abstract void logEvent(GameAction gameAction);

    protected abstract Message changeCard(int cardNumberInList, int playerId, int token);

    public enum Message {
        IMPOSSIBLE, SUCCESS, ERROR, FULL, INSUFFICIENT
    }

}
