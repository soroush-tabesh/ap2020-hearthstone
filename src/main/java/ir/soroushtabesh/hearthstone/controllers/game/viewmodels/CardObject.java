package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CardObject extends GameObject {
    private Card cardModel;
    private final SimpleIntegerProperty manaCost = new SimpleIntegerProperty();
    private transient GenericScript script;

    protected CardObject(int playerId, GameController gameController, Card cardModel) {
        super(playerId, gameController);
        this.cardModel = cardModel;
        manaCost.set(cardModel.getMana());
        script = cardModel.getScriptModel().getScript(gameController);
        addMiscScript(script);
    }

    public CardObject(int id, int playerID, Card cardModel) {
        super(id, playerID);
        this.cardModel = cardModel;
    }

    @Override
    public void update(GameObject gameObject, GameController gameController) {
        super.update(gameObject, gameController);
        CardObject cardObject = (CardObject) gameObject;
        cardModel = cardObject.cardModel;
        manaCost.set(cardObject.getManaCost());
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

    public static CardObject build(int playerId, GameController gameController, Card card) {
        CardObject cardObject = null;
        if (card instanceof Minion) {
            cardObject = new MinionObject(playerId, gameController, (Minion) card);
        } else if (card instanceof Spell) {
            cardObject = new SpellObject(playerId, gameController, (Spell) card);
        } else if (card instanceof Weapon) {
            cardObject = new WeaponObject(playerId, gameController, (Weapon) card);
        }
        return cardObject;
    }

    public CardState getCardState() {
        ModelPool.PlayerData playerData = getGameController()
                .getModelPool()
                .getPlayerDataById(getPlayerId());
        if (playerData.getDeckCard().contains(this))
            return CardState.DECK;
        else if (playerData.getHandCard().contains(this))
            return CardState.HAND;
        else if (playerData.getGroundCard().contains(this))
            return CardState.GROUND;
        else
            return CardState.DEAD;
    }

    public enum CardState {
        DECK, HAND, GROUND, DEAD
    }

}
