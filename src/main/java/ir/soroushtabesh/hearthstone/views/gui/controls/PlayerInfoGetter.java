package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;

public interface PlayerInfoGetter {
    Deck getSelectedDeck();

    Hero getSelectedHero();

    InfoPassive getSelectedInfoPassive();
}