package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

public class DeckManager {
    private static DeckManager instance = new DeckManager();

    private DeckManager() {
    }

    public static DeckManager getInstance() {
        return instance;
    }

    public boolean removeDeck(Deck deck) {
        return DBUtil.doInJPA(session -> {
            boolean res = deck.getPlayer().getDecks().remove(deck);
            if (res) {
                Logger.log("DeckManager", "remove deck: " + deck.getName());
                deck.setPlayer(null);
                session.remove(deck);
            }
            return res;
        });
    }

    public void updateDeckProperties(Deck deck) {
        Logger.log("DeckManager", "update deck: " + deck.getName());
        DBUtil.doInJPA(session -> {
            session.saveOrUpdate(deck);
            return null;
        });
    }

    public boolean removeCardFromDeck(Card card, Deck deck) {
        Boolean res = DBUtil.doInJPA(session -> deck.removeCard(card));
        if (res)
            Logger.log("DeckManager", String.format("remove card '%s' from deck '%s'"
                    , card.getCard_name(), deck.getName()));
        return res;
    }

    public Deck.Message addCardToDeck(Card card, Deck deck) {
        Deck.Message res = DBUtil.doInJPA(session -> deck.addCard(card));
        if (res == Deck.Message.SUCCESS)
            Logger.log("DeckManager", String.format("add card '%s' to deck '%s'"
                    , card.getCard_name(), deck.getName()));
        return res;
    }

    public void saveNewDeck(Deck deck, Player player) {
        DBUtil.doInJPA(session -> {
            session.saveOrUpdate(deck);
            player.addDeck(deck);
            session.saveOrUpdate(player);
            return null;
        });
        Logger.log("DeckManager", "new deck: " + deck.getName());
    }

}
