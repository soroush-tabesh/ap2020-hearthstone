package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.MinionBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class PowerUpMinion extends MinionBehavior {
    private final int hp;
    private final int attack;

    public PowerUpMinion(int hp, int attack) {
        this.hp = hp;
        this.attack = attack;
    }

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        MinionObject minionObject = (MinionObject) getOwnerObject();
        minionObject.setHp(minionObject.getHp() + hp);
        minionObject.setAttackPower(minionObject.getAttackPower() + attack);
    }
}
