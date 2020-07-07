package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.QuestReward;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;

public class QuestWatch extends SpellBehavior implements QuestReward {
    private transient QuestReward customReward = this;

    public QuestReward getCustomReward() {
        return customReward;
    }

    public void setCustomReward(QuestReward customReward) {
        this.customReward = customReward;
    }

    @Override
    public void applyReward() {

    }
}
