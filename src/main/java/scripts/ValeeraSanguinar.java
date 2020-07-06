package scripts;

import ir.soroushtabesh.hearthstone.controllers.game.scripts.HeroBehavior;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.models.Hero;

import java.util.ArrayList;
import java.util.List;

public class ValeeraSanguinar extends HeroBehavior {
    @Override
    public void onScriptAdded() {
        super.onScriptAdded();
        ModelPool.PlayerData playerData = getGameController().getModelPool()
                .getPlayerDataById(getPlayerController().getId());
        List<CardObject> cardObjects = new ArrayList<>();
        cardObjects.addAll(playerData.getHandCard());
        cardObjects.addAll(playerData.getDeckCard());
        cardObjects.forEach(cardObject -> {
            Hero.HeroClass heroClass = cardObject.getCardModel().getHeroClass();
            if (heroClass != Hero.HeroClass.ALL && heroClass != Hero.HeroClass.ROUGE)
                cardObject.setManaCost(Math.max(0, cardObject.getManaCost() - 2));
        });
    }
}
