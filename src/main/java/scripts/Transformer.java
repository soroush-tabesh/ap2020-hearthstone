package scripts;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.models.Card;

public class Transformer extends SpellBehavior {
    private final String transformTo;

    public Transformer(String transformTo) {
        this.transformTo = transformTo;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        if (!(gameObject instanceof MinionObject))
            return false;
        MinionObject minionObject = (MinionObject) gameObject;
        getGameController().getScriptEngine().unregisterScriptAll(minionObject);
        ModelPool.PlayerData playerData = getGameController().getModelPool().getPlayerDataById(getPlayerController().getPlayerId());
        playerData.getGroundCard().remove(minionObject);

        Card card = CardManager.getInstance().getCardByName(transformTo);
        CardObject cardObject = CardObject.build(getPlayerController().getPlayerId(), getGameController(), card);
        getGameController().summonMinion((MinionObject) cardObject
                , getPlayerController().getToken());
        return true;
    }
}
