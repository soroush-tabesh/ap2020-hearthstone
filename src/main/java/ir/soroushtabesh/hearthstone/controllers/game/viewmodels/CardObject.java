package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.models.Card;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class CardObject extends GameObject {
    private final Card cardModel;
    private final IntegerProperty manaCost = new SimpleIntegerProperty();
    private GenericScript script;

    protected CardObject(int playerId, GameController gameController, Card cardModel) {
        super(playerId, gameController);
        this.cardModel = cardModel;
        manaCost.set(cardModel.getMana());
        script = cardModel.getScriptModel().getScript(gameController);
        addMiscScript(script);
    }

    public int getManaCost() {
        return manaCost.get();
    }

    public void setManaCost(int manaCost) {
        this.manaCost.set(manaCost);
    }

    public IntegerProperty manaCostProperty() {
        return manaCost;
    }

    public GenericScript getScript() {
        return script;
    }

    public void setScript(GenericScript script) {
        this.script = script;
    }

    public Card getCardModel() {
        return cardModel;
    }
}
