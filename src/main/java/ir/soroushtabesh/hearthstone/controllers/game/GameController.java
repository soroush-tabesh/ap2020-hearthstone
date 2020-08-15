package ir.soroushtabesh.hearthstone.controllers.game;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;

import java.util.List;

public abstract class GameController {

    private final transient ModelPool modelPool;
    private final transient ScriptEngine scriptEngine;

    public GameController() {
        scriptEngine = new ScriptEngine(this);
        modelPool = new ModelPool(this);
        scriptEngine.init();
    }

    public boolean isGameReady() {
        return modelPool.getSceneData().gameReadyProperty().get();
    }

    public void setGameReady(boolean gameReady) {
        this.modelPool.getSceneData().gameReadyProperty().set(gameReady);
    }

    public ReadOnlyBooleanProperty gameReadyProperty() {
        return modelPool.getSceneData().gameReadyProperty();
    }

    public final ModelPool getModelPool() {
        return modelPool;
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public boolean isStarted() {
        return modelPool.getSceneData().startedProperty().get();
    }

    public ReadOnlyBooleanProperty startedProperty() {
        return modelPool.getSceneData().startedProperty();
    }

    public void setStarted(boolean started) {
        this.modelPool.getSceneData().startedProperty().set(started);
    }

    public int getTurn() {
        return modelPool.getSceneData().turnProperty().get();
    }

    public void setTurn(int turn) {
        this.modelPool.getSceneData().turnProperty().set(turn);
    }

    public IntegerProperty turnProperty() {
        return modelPool.getSceneData().turnProperty();
    }

    public int getWinner() {
        return modelPool.getSceneData().winnerProperty().get();
    }

    public void setWinner(int winner) {
        this.modelPool.getSceneData().winnerProperty().set(winner);
    }

    public ReadOnlyIntegerProperty winnerProperty() {
        return modelPool.getSceneData().winnerProperty();
    }

    public abstract PlayerController registerPlayer(Player player, Hero hero, Deck deck, InfoPassive infoPassive, boolean shuffle);

    public abstract PlayerController registerPlayer(Player player, Hero hero, Deck deck, InfoPassive infoPassive, List<Card> cardOrder);

    public abstract PlayerController[] getAllPlayerControllers();

    public abstract boolean endTurn(long token);

    public abstract Message startGame(long token);

    public abstract Message playCard(CardObject cardObject, int groundIndex, GameObject optionalTarget, long token);

    public abstract Message summonMinion(MinionObject source, long token);

    public abstract Message playMinion(MinionObject source, GameObject target, long token);

    public abstract Message useWeapon(HeroObject source, GameObject target, long token);

    public abstract Message useHeroPower(HeroObject source, GameObject target, long token);

    public abstract void logEvent(GameAction gameAction);

    public abstract Message changeCard(int cardNumberInList, long token);

    public abstract int token2playerId(long token);

}
