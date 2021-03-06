package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.network.command.*;
import ir.soroushtabesh.hearthstone.util.Constants;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

import static ir.soroushtabesh.hearthstone.network.RemoteGameServer.sendPOST;

public class DeckManager {
    private static DeckManager instanceMain;
    private static DeckManager instanceProxy;

    private DeckManager() {
    }

    public static DeckManager getInstance() {
        if (Constants.isServerMode()) {
            if (instanceMain == null)
                instanceMain = new DeckManager();
            return instanceMain;
        } else {
            if (instanceProxy == null)
                instanceProxy = new DeckManagerProxy();
            return instanceProxy;
        }
    }

    public boolean removeDeck(int deckID, long token) {
        Deck deck = getDeckByID(deckID);
        if (deck.getPlayer() != PlayerManager.getInstance().getPlayerByToken(token))
            return false;
        Boolean rs = DBUtil.doInJPA(session -> {
            boolean res = deck.getPlayer().getDecks().remove(deck);
            if (res) {
                deck.setPlayer(null);
                session.remove(deck);
            }
            return res;
        });
        if (rs)
            Logger.log("DeckManager", "remove deck: " + deck.getId());
        return rs;
    }

    public void updateDeckProperties(Deck tDeck, long token) {
        if (tDeck == null)
            return;
        Deck deck = getDeckByID(tDeck.getId());
        if (deck.getPlayer() != PlayerManager.getInstance().getPlayerByToken(token))
            return;
        Logger.log("DeckManager", "update deck: " + deck.getName());
        DBUtil.doInJPA(session -> {
            deck.setName(tDeck.getName());
            deck.setHeroClass(tDeck.getHeroClass());
            return null;
        });
    }

    public boolean removeCardFromDeck(int cardID, int deckID, long token) {
        Deck deck = getDeckByID(deckID);
        Player player = PlayerManager.getInstance().getPlayerByToken(token);
        if (deck.getPlayer() != player)
            return false;
        Card card = CardManager.getInstance().getCardByID(cardID);
        Boolean res = DBUtil.doInJPA(session -> deck.removeCardOnce(card));
        if (res)
            Logger.log("DeckManager", String.format("remove card '%s' from deck '%s'"
                    , card.getName(), deck.getName()));
        return res;
    }

    public Message addCardToDeck(int cardID, int deckID, long token) {
        Deck deck = getDeckByID(deckID);
        Player player = PlayerManager.getInstance().getPlayerByToken(token);
        if (deck.getPlayer() != player)
            return Message.ERROR;
        Card card = CardManager.getInstance().getCardByID(cardID);
        Message res = DBUtil.doInJPA(session -> deck.addCard(card));
        if (res == Message.SUCCESS)
            Logger.log("DeckManager", String.format("add card '%s' to deck '%s'"
                    , card.getName(), deck.getName()));
        return res;
    }

    public Message saveNewDeck(Deck tDeck, long token) {
        Player player = PlayerManager.getInstance().getPlayerByToken(token);
        if (player == null)
            return Message.ERROR;
        DBUtil.doInJPA(session -> {
            Deck deck = new Deck();
            deck.setName(tDeck.getName());
            deck.setHeroClass(tDeck.getHeroClass());
            session.saveOrUpdate(deck);
            player.addDeck(deck);
            return null;
        });
        Logger.log("DeckManager", "new deck: " + tDeck.getName());
        return Message.SUCCESS;
    }

    public Deck getDeckByID(int id) {
        return DBUtil.doInJPA(session -> session.get(Deck.class, id));
    }

    private static class DeckManagerProxy extends DeckManager {
        @Override
        public boolean removeDeck(int deckID, long token) {
            return sendPOST(new RemoveDeck(deckID, token)).getMessage() == Message.SUCCESS;
        }

        @Override
        public void updateDeckProperties(Deck deck, long token) {
            sendPOST(new UpdateDeck(deck, token));
        }

        @Override
        public boolean removeCardFromDeck(int cardID, int deckID, long token) {
            return sendPOST(new RemoveCardInDeck(cardID, deckID, token)).getMessage() == Message.SUCCESS;
        }

        @Override
        public Message addCardToDeck(int cardID, int deckID, long token) {
            return sendPOST(new AddCardToDeck(cardID, deckID, token)).getMessage();
        }

        @Override
        public Message saveNewDeck(Deck deck, long token) {
            return sendPOST(new MakeDeck(deck, token)).getMessage();
        }

        @Override
        public Deck getDeckByID(int id) {
            return (Deck) sendPOST(new GetDeck(id)).getParcel();
        }
    }

}
