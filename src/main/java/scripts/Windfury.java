package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Windfury extends MinionBehavior implements ChangeListener<Boolean> {
    private transient int rev = 1;

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ((MinionObject) getOwnerObject()).sleepProperty().addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue && rev > 0) {
            --rev;
            ((MinionObject) getOwnerObject()).setSleep(false);
        }
    }

    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        rev = 1;
    }
}
