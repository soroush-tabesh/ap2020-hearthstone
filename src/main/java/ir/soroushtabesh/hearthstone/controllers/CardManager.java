package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import ir.soroushtabesh.hearthstone.network.command.BuyCard;
import ir.soroushtabesh.hearthstone.network.command.SellCard;
import ir.soroushtabesh.hearthstone.util.Constants;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

import java.util.List;

import static ir.soroushtabesh.hearthstone.network.RemoteGameServer.sendPOST;

public class CardManager {
    private static CardManager instanceMain;
    private static CardManager instanceProxy;

    private CardManager() {
    }

    public static CardManager getInstance() {
        if (Constants.isServerMode()) {
            if (instanceMain == null)
                instanceMain = new CardManager();
            return instanceMain;
        } else {
            if (instanceProxy == null)
                instanceProxy = new CardManagerProxy();
            return instanceProxy;
        }
    }

    public List<Card> getAllCards() {
        List<Card> cards = DBUtil.doInJPA(session ->
                session.createQuery("from Card where tradable=true", Card.class).list());
        System.out.println("CardManager.getAllCards " + cards.size());
        return cards;
    }

    public List<InfoPassive> getAllPassives() {
        return DBUtil.doInJPA(session -> session.createQuery("from InfoPassive ", InfoPassive.class).list());
    }

    public Message buyCard(int cardID, long token) {
        Player player = PlayerManager.getInstance().getPlayerByToken(token);
        Card card = getCardByID(cardID);
        if (card == null || player == null)
            return Message.ERROR;
        return DBUtil.doInJPA(session -> {
            if (player.getCoin() < card.getPrice())
                return Message.INSUFFICIENT;
            if (!player.addOwnedCard(card))
                return Message.FULL;
            player.setCoin(player.getCoin() - card.getPrice());
            Logger.log("CardManager", "buy " + card.getName());
            return Message.SUCCESS;
        });
    }

    public Message sellCard(int cardID, long token) {
        Player player = PlayerManager.getInstance().getPlayerByToken(token);
        Card card = getCardByID(cardID);
        if (card == null || player == null)
            return Message.ERROR;
        return DBUtil.doInJPA(session -> {
            if (player.getOwnedAmount(card) <= 0)
                return Message.EMPTY;
            player.removeOwnedCard(card);
            player.setCoin(player.getCoin() + card.getPrice());
            Logger.log("CardManager", "sell " + card.getName());
            return Message.SUCCESS;
        });
    }

    public boolean isInAnyDeck(Card card) {
        return false;
    }

    public Card getCardByName(String name) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Card where lower(name)=:name ", Card.class)
                .setParameter("name", name.toLowerCase())
                .uniqueResult());
    }

    public Card getCardByID(int id) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Card where id=:id ", Card.class)
                .setParameter("id", id)
                .uniqueResult());
    }

    public List<Minion> getAllMinions() {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Minion ", Minion.class)
                .list());
    }

    public List<Spell> getAllSpells() {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Spell ", Spell.class)
                .list());
    }

    public List<Weapon> getAllWeapons() {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Weapon ", Weapon.class)
                .list());
    }

    public Hero getHeroByClass(Hero.HeroClass heroClass) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Hero where heroClass=:cls ", Hero.class)
                .setParameter("cls", heroClass)
                .uniqueResult());
    }

    public Hero getHeroByID(int id) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Hero where id=:id ", Hero.class)
                .setParameter("id", id)
                .uniqueResult());
    }

    public InfoPassive getPassiveByID(int id) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from InfoPassive where id=:id ", InfoPassive.class)
                .setParameter("id", id)
                .uniqueResult());
    }

    private static class CardManagerProxy extends CardManager {
//        @Override
//        public List<Card> getAllCards() {
//            Packet packet = sendPOST(new GetCards());
//            return (List<Card>) packet.getParcel();
//        }
//
//        @Override
//        public List<InfoPassive> getAllPassives() {
//            Packet packet = sendPOST(new GetPassives());
//            return (List<InfoPassive>) packet.getParcel();
//        }

        @Override
        public Message buyCard(int cardID, long token) {
            return sendPOST(new BuyCard(token, cardID)).getMessage();
        }

        @Override
        public Message sellCard(int cardID, long token) {
            return sendPOST(new SellCard(token, cardID)).getMessage();
        }

        @Override
        public boolean isInAnyDeck(Card card) {
            Player player = PlayerManager.getInstance().getPlayer();
            return player.getOwnedCardsList().contains(card);
        }

//        @Override
//        public Card getCardByName(String name) {
//            Packet packet = sendPOST(new GetCard(name));
//            return (Card) packet.getParcel();
//        }
//
//        @Override
//        public List<Minion> getAllMinions() {
//            Packet packet = sendPOST(new GetMinions());
//            return (List<Minion>) packet.getParcel();
//        }
//
//        @Override
//        public List<Spell> getAllSpells() {
//            Packet packet = sendPOST(new GetSpells());
//            return (List<Spell>) packet.getParcel();
//        }
//
//        @Override
//        public List<Weapon> getAllWeapons() {
//            Packet packet = sendPOST(new GetWeapons());
//            return (List<Weapon>) packet.getParcel();
//        }
//
//        @Override
//        public Hero getHeroByClass(Hero.HeroClass heroClass) {
//            Packet packet = sendPOST(new GetHero(heroClass));
//            return (Hero) packet.getParcel();
//        }
//
//        @Override
//        public Card getCardByID(int id) {
//            Packet packet = sendPOST(new GetCard(id));
//            return (Card) packet.getParcel();
//        }
    }

}
