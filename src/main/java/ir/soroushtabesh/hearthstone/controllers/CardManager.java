package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import ir.soroushtabesh.hearthstone.util.Constants;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

import java.util.List;

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
        return DBUtil.doInJPA(session ->
                session.createQuery("from Card where tradable=true", Card.class).list());
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
            Logger.log("CardManager", "buy " + card.getName());
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
            Logger.log("CardManager", "sell " + card.getName());
            return Message.SUCCESS;
        });
    }

    public boolean isInAnyDeck(Card card) {
        Player player = PlayerManager.getInstance().getPlayer();
        return player.getOwnedCardsList().contains(card);
    }

    public Card getCardByName(String name) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Card where lower(name)=:name ", Card.class)
                .setParameter("name", name.toLowerCase())
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

    private static class CardManagerProxy extends CardManager {

    }

}
