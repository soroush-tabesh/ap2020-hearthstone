package ir.soroushtabesh.hearthstone.controllers.game.scripts;

import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;

public class HeroBehavior extends GenericScript {

    public static final String DAMAGE_TAKEN = "onDamageTaken";
    public static final String ATTACK_DONE = "onAttackDone";
    public static final String ATTACK_EFFECT = "onAttackEffect";

    public void onDamageTaken() {

    }

    public void onAttackDone() {

    }

    public void onAttackEffect(GameObject gameObject) {

    }

    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        getGameController().getModelPool().getPlayerDataById(getPlayerController().getId()).getHero()
                .getHeroPower().setUsed(false);
    }
}
