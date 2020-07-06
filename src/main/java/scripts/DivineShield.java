package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class DivineShield extends MinionBehavior implements ChangeListener<Number> {
    private transient boolean enabled = false;

    @Override
    public void onCardPlay() {
        super.onScriptAdded();
        enabled = true;
        MinionObject ownerObject = (MinionObject) getOwnerObject();
        ownerObject.hpProperty().addListener(this);
        ownerObject.setImmune(true);
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (!enabled)
            return;
        enabled = false;
        MinionObject ownerObject = (MinionObject) getOwnerObject();
        ownerObject.setImmune(false);
        ownerObject.hpProperty().removeListener(this);
        ownerObject.setHp(oldValue.intValue());
    }
}
