package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Player;
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
                deck.setPlayer(null);
                session.remove(deck);
            }
            return res;
        });
    }

    public void updateDeckProperties(Deck deck) {
        DBUtil.doInJPA(session -> {
            session.saveOrUpdate(deck);
            return null;
        });
    }

    public boolean removeCardFromDeck(Card card, Deck deck) {
        return DBUtil.doInJPA(session -> deck.removeCard(card));
    }

    public Deck.Message addCardToDeck(Card card, Deck deck) {
        return DBUtil.doInJPA(session -> deck.addCard(card));
    }

    public void saveNewDeck(Deck deck, Player player) {
        DBUtil.doInJPA(session -> {
            session.saveOrUpdate(deck);
            player.addDeck(deck);
            session.saveOrUpdate(player);
            return null;
        });
    }

}
