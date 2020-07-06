package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class QuestWatch extends SpellBehavior {
    private transient SpellBehavior customReward = this;

    public SpellBehavior getCustomReward() {
        return customReward;
    }

    public void setCustomReward(SpellBehavior customReward) {
        this.customReward = customReward;
        customReward.setGameController(getGameController());
        customReward.setOwnerObject(getOwnerObject());
        customReward.setPlayerController(getPlayerController());
    }

}
