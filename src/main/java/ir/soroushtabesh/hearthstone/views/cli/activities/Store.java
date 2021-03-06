//package ir.soroushtabesh.hearthstone.views.cli.activities;
//
//import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
//import ir.soroushtabesh.hearthstone.models.Card;
//import ir.soroushtabesh.hearthstone.models.Deck;
//import ir.soroushtabesh.hearthstone.models.Log;
//import ir.soroushtabesh.hearthstone.models.Player;
//import ir.soroushtabesh.hearthstone.util.Logger;
//import ir.soroushtabesh.hearthstone.util.cli.CommandProcessor;
//import ir.soroushtabesh.hearthstone.util.cli.PrintUtil;
//import ir.soroushtabesh.hearthstone.util.db.DBUtil;
//import ir.soroushtabesh.hearthstone.views.cli.CLIActivity;
//import org.hibernate.Session;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class Store extends CLIActivity {
//
//    private CommandProcessor processor = new CommandProcessor();
//
//    @Override
//    public void onStart(String[] args) {
//        System.out.println("::Store");
//        processor.add("ls", event -> {
//            if (event.args.length != 1) {
//                System.out.println("Warning:: Wrong use of switches...");
//                return;
//            }
//            switch (event.args[0]) {
//                case "-s":
//                    showSalable();
//                    break;
//                case "-b":
//                    showPurchasable();
//                    break;
//                default:
//                    System.out.println("Warning:: Unknown switch...");
//            }
//        });
//        processor.add("buy", event -> {
//            if (event.args.length != 1) {
//                System.out.println("Warning:: Wrong use of switches...");
//                return;
//            }
//            buyCard(event.args[0]);
//        });
//        processor.add("sell", event -> {
//            if (event.args.length != 1) {
//                System.out.println("Warning:: Wrong use of switches...");
//                return;
//            }
//            sellCard(event.args[0]);
//        });
//        processor.add("wallet", event -> {
//            System.out.println("You have "
//                    + PlayerManager.getInstance().getPlayer().getCoin()
//                    + " coins in your wallet.");
//            Logger.log("store", "wallet");
//        });
//    }
//
//    private void showSalable() {
//        DBUtil.doInJPA(session -> {
//            try {
//                session.refresh(PlayerManager.getInstance().getPlayer());
//                Collection<Card> allCards = getSalableCards();
//                System.out.println("Salable cards:");
//                PrintUtil.printList(allCards);
//                Logger.log("store", "ls -s");
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("store", "ls -s: db error", Log.Severity.FATAL);
//            }
//            return null;
//        });
//    }
//
//    private void showPurchasable() {
//        DBUtil.doInJPA(session -> {
//            try {
//                session.refresh(PlayerManager.getInstance().getPlayer());
//                System.out.println("Purchasable cards:");
//                PrintUtil.printList(getPurchasableCards(session));
//                Logger.log("store", "ls -b");
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("store", "ls -s: db error", Log.Severity.FATAL);
//            }
//            return null;
//        });
//    }
//
//    private Collection<Card> getPurchasableCards(Session session) {
//        Collection<Card> allGameCards = new ArrayList<>(session.createQuery("from Card ", Card.class).list());
//        Player player = PlayerManager.getInstance().getPlayer();
//        List<Card> ownedCards = player.getOwnedCardsList();
//        allGameCards.removeAll(ownedCards);
//        return allGameCards;
//    }
//
//    private Collection<Card> getSalableCards() {
//        Player player = PlayerManager.getInstance().getPlayer();
//        Collection<Card> allCards = new ArrayList<>(player.getOwnedCardsList());
//        for (Deck deck : player.getDecks()) {
//            allCards.removeAll(deck.getCardsInDeck());
//        }
//        return allCards;
//    }
//
//    private void buyCard(String cardname) {
//        DBUtil.doInJPA(session -> {
//            try {
//                Player player = PlayerManager.getInstance().getPlayer();
//                session.refresh(player);
//                Collection<Card> purchasableCards = getPurchasableCards(session);
//                Card card = Card.getCardByName(cardname, session);
//                if (card == null) {
//                    System.out.println("No such card Exists...");
//                    return null;
//                }
//                if (!purchasableCards.contains(card)) {
//                    System.out.println("Sorry you can't purchase this card. You already own this card.");
//                    return null;
//                }
//                if (card.getPrice() > player.getCoin()) {
//                    System.out.println("You don't have enough coin to buy this card");
//                    return null;
//                }
//                player.getOwnedCardsList().add(card);
//                player.setCoin(player.getCoin() - card.getPrice());
//                DBUtil.pushSingleObject(player, session);
//                System.out.println("Successfully Purchased!");
//                Logger.log("store", "buy: " + card.getName());
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("store", "buy: db error", Log.Severity.FATAL);
//            }
//            return null;
//        });
//    }
//
//    private void sellCard(String cardname) {
//        DBUtil.doInJPA(session -> {
//            try {
//                Player player = PlayerManager.getInstance().getPlayer();
//                session.refresh(player);
//                Collection<Card> salableCards = getSalableCards();
//                Card card = Card.getCardByName(cardname, session);
//                if (card == null) {
//                    System.out.println("No such card Exists...");
//                    return null;
//                }
//                if (!salableCards.contains(card)) {
//                    System.out.println("Sorry you can't sell this card. Either you don't own this card or it's still in your decks.");
//                    return null;
//                }
//                player.getOwnedCardsList().remove(card);
//                player.setCoin(player.getCoin() + card.getPrice());
//                DBUtil.pushSingleObject(player, session);
//                System.out.println("Successfully Sold!");
//                Logger.log("store", "sell: " + card.getName());
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("store", "sell: db error", Log.Severity.FATAL);
//            }
//
//            return null;
//        });
//    }
//
//
//    @Override
//    public String getActivityCommand() {
//        return "store";
//    }
//
//    @Override
//    public CommandProcessor getProcessor() {
//        return processor;
//    }
//}
