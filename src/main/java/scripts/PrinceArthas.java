package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import javafx.collections.ObservableList;

import java.util.Random;

public class PrinceArthas extends HeroBehavior {
    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        if (getGameController().getTurn() == getOwnerObject().getPlayerId()) {
            ObservableList<MinionObject> groundCard = getGameController().getModelPool().getPlayerDataById(1 - getOwnerObject().getPlayerId())
                    .getGroundCard();
            MinionObject minionObject = groundCard.get(new Random(System.currentTimeMillis()).nextInt(groundCard.size()));
            minionObject.addMiscScript(new PowerUpMinion(1, 1));
        }
    }
}
