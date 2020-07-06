package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GameEventListener;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.Card;

public class DesertSpear extends SpellBehavior implements GameEventListener {
    @Override
    public void onCardPlay() {
        super.onScriptAdded();
        getGameController().getScriptEngine().registerEventFilter(this);
    }

    @Override
    public void onEvent(GameAction gameAction) {
        if (gameAction instanceof GameAction.TargetedAttack) {
            GameAction.TargetedAttack targetedAttack = (GameAction.TargetedAttack) gameAction;
            if (targetedAttack.getSource() == getOwnerObject()
                    && targetedAttack.getSource().getPlayerId() == getOwnerObject().getPlayerId()) {
                Card locust = Card.getCardByName("Locust");
                CardObject cardObject = CardObject.build(getPlayerController().getId(), getGameController(), locust);
                getGameController().summonMinion((MinionObject) cardObject
                        , getPlayerController().getId(), getPlayerController().getToken());
            }
        }
    }
}
