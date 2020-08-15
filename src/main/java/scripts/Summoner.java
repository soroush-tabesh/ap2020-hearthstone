package scripts;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.SpellBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.models.Card;

public class Summoner extends SpellBehavior {
    private final String[] summonList;

    public Summoner(String... summonList) {
        this.summonList = summonList;
    }

    @Override
    public boolean onSpellEffect(GameObject gameObject) {
        super.onSpellEffect(gameObject);
        for (String s : summonList) {
            Card locust = CardManager.getInstance().getCardByName(s);
            CardObject cardObject = CardObject.build(getPlayerController().getPlayerId(), getGameController(), locust);
            getGameController().summonMinion((MinionObject) cardObject
                    , getPlayerController().getToken());
        }
        return true;
    }
}
