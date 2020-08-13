package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.models.Card;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class HeroPowerObject extends CardObject {
    private SimpleBooleanProperty used = new SimpleBooleanProperty();
    private SimpleBooleanProperty playable = new SimpleBooleanProperty();

    public HeroPowerObject(int playerId, GameController gameController, Card cardModel) {
        super(playerId, gameController, cardModel);
    }

    public boolean isUsed() {
        return used.get();
    }

    public void setUsed(boolean used) {
        this.used.set(used);
    }

    public BooleanProperty usedProperty() {
        return used;
    }

    public boolean isPlayable() {
        return playable.get();
    }

    public void setPlayable(boolean playable) {
        this.playable.set(playable);
    }

    public BooleanProperty playableProperty() {
        return playable;
    }
}
