package ir.soroushtabesh.hearthstone.controllers.game.scripts;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;

public class MinionBehavior extends GenericScript {

    public static final String BATTLE_CRY = "battleCry";
    public static final String DEATH_RATTLE = "deathRattle";
    public static final String DAMAGE_TAKEN = "onDamageTaken";
    public static final String ATTACK_DONE = "onAttackDone";
    public static final String ATTACK_EFFECT = "onAttackEffect";

    public boolean battleCry(GameObject gameObject) {
        return true;
    }

    public void deathRattle() {

    }

    public void onDamageTaken() {

    }

    public void onAttackDone() {

    }

    public void onAttackEffect(GameObject gameObject) {

    }

    @Override
    public void onTurnEnd() {
        if (getGameController().getTurn() == getOwnerObject().getPlayerId())
            ((MinionObject) getOwnerObject()).setSleep(false);
    }
}
