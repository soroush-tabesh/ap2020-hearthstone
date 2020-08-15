package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GameEventListener;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.SpellObject;
import javafx.collections.ObservableList;

public class StrengthInNumbers extends QuestWatch implements GameEventListener {

    private transient int spent;

    @Override
    public void onCardPlay() {//start watch
        super.onCardPlay();
        getGameController().getScriptEngine().registerEventFilter(this);
        new Thread(this::onCardPlay);
    }

    @Override
    public void applyReward() {
        super.applyReward();
        getGameController().getScriptEngine().unregisterEventFilter(this);
        ObservableList<CardObject> deckCard = getGameController().getModelPool()
                .getPlayerDataById(getPlayerController().getPlayerId()).getDeckCard();
        MinionObject minionObject = null;
        for (CardObject cardObject : deckCard)
            if (cardObject instanceof MinionObject)
                minionObject = (MinionObject) cardObject;
        if (minionObject != null) {
            getGameController().summonMinion(minionObject
                    , getPlayerController().getToken());
        }
    }

    @Override
    public void onEvent(GameAction gameAction) {
        if (gameAction instanceof GameAction.CardPlay) {
            GameAction.CardPlay cardPlay = (GameAction.CardPlay) gameAction;
            if (cardPlay.getSource().getPlayerId() == getOwnerObject().getPlayerId()
                    && cardPlay.getSource() instanceof SpellObject) {
                spent += cardPlay.getSource().getManaCost();
                if (spent >= 10) {
                    getCustomReward().applyReward();
                }
            }
        }
    }

}
