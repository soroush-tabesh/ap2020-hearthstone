package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.WeaponObject;

public class PowerUpWeapon extends GenericScript {
    private final int hp;
    private final int attack;

    public PowerUpWeapon(int hp, int attack) {
        this.hp = hp;
        this.attack = attack;
    }

    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        WeaponObject weaponObject = (WeaponObject) getOwnerObject();
        weaponObject.setDurability(weaponObject.getDurability() + hp);
        weaponObject.setAttackPower(weaponObject.getAttackPower() + attack);
    }
}
