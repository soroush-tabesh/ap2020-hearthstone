package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.HeroPowerObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FreePower extends HeroBehavior implements ChangeListener<Boolean> {
    private transient HeroPowerObject heroPower;
    private transient int rev = 1;

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        heroPower = getGameController().getModelPool()
                .getPlayerDataById(getPlayerController().getPlayerId()).getHero().getHeroPower();
        heroPower.setManaCost(Math.max(0, heroPower.getManaCost() - 1));
        heroPower.usedProperty().addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue && rev > 0) {
            --rev;
            heroPower.setUsed(false);
        }
    }

    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        rev = 1;
    }
}
