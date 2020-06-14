package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

import java.util.List;

public class CardManager {
    private static CardManager instance = new CardManager();

    private CardManager() {
    }

    public static CardManager getInstance() {
        return instance;
    }

    public List<Card> getAllCards() {
        return DBUtil.doInJPA(session -> session.createQuery("from Card ", Card.class).list());
    }

    public List<InfoPassive> getAllPassives() {
        return DBUtil.doInJPA(session -> session.createQuery("from InfoPassive ", InfoPassive.class).list());
    }

    public Message buyCard(Card card) {
        return DBUtil.doInJPA(session -> {
            Player player = PlayerManager.getInstance().getPlayer();
            if (player.getCoin() < card.getPrice())
                return Message.INSUFFICIENT;
            if (!player.addOwnedCard(card))
                return Message.FULL;
            player.setCoin(player.getCoin() - card.getPrice());
            Logger.log("CardManager", "buy " + card.getCard_name());
            return Message.SUCCESS;
        });
    }

    public Message sellCard(Card card) {
        return DBUtil.doInJPA(session -> {
            Player player = PlayerManager.getInstance().getPlayer();
            if (player.getOwnedAmount(card) <= 0)
                return Message.EMPTY;
            player.removeOwnedCard(card);
            player.setCoin(player.getCoin() + card.getPrice());
            Logger.log("CardManager", "sell " + card.getCard_name());
            return Message.SUCCESS;
        });
    }

    public boolean isInAnyDeck(Card card) {
        Player player = PlayerManager.getInstance().getPlayer();
        return player.getOwnedCardsList().contains(card);
    }

    public enum Message {
        INSUFFICIENT, FULL, EMPTY, SUCCESS
    }

}
