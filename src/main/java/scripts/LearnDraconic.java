package scripts;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GameEventListener;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.SpellObject;
import ir.soroushtabesh.hearthstone.models.Card;

public class LearnDraconic extends QuestWatch implements GameEventListener {

    private transient int spent;

    @Override
    public void onCardPlay() {//start watch
        super.onCardPlay();
        getGameController().getScriptEngine().registerEventFilter(this);
    }

    @Override
    public void applyReward() {
        getGameController().getScriptEngine().unregisterEventFilter(this);
        Card dragon = CardManager.getInstance().getCardByName("Draconic Emissary");
        CardObject cardObject = CardObject.build(getPlayerController().getId(), getGameController(), dragon);
        getGameController().summonMinion((MinionObject) cardObject
                , getPlayerController().getId(), getPlayerController().getToken());
        super.applyReward();
    }

    @Override
    public void onEvent(GameAction gameAction) {
        if (gameAction instanceof GameAction.CardPlay) {
            GameAction.CardPlay cardPlay = (GameAction.CardPlay) gameAction;
            if (cardPlay.getSource().getPlayerId() == getOwnerObject().getPlayerId()
                    && cardPlay.getSource() instanceof SpellObject) {
                spent += cardPlay.getSource().getManaCost();
                if (spent >= 8) {
                    getCustomReward().applyReward();
                }
            }
        }
    }
}
