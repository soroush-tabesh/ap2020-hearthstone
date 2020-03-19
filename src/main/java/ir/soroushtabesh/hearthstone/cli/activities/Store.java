package ir.soroushtabesh.hearthstone.cli.activities;

import ir.soroushtabesh.hearthstone.cli.CLIActivity;
import ir.soroushtabesh.hearthstone.cli.CommandProcessor;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.beans.Card;
import ir.soroushtabesh.hearthstone.models.beans.Deck;
import ir.soroushtabesh.hearthstone.models.beans.Player;
import ir.soroushtabesh.hearthstone.util.DBUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class Store extends CLIActivity {

    private CommandProcessor processor = new CommandProcessor();

    @Override
    public void onStart(String[] args) {
        System.out.println("::Store");
        processor.add("ls", event -> {
            if (event.args.length != 1) {
                System.out.println("Warning:: Wrong use of switches...");
                return;
            }
            switch (event.args[0]) {
                case "-s":
                    showSalable();
                    break;
                case "-b":
                    showPurchasable();
                    break;
                default:
                    System.out.println("Warning:: Unknown switch...");
            }
        });
        processor.add("buy", event -> {
            if (event.args.length != 1) {
                System.out.println("Warning:: Wrong use of switches...");
                return;
            }
            buyCard(event.args[0]);
        });
        processor.add("sell", event -> {
            if (event.args.length != 1) {
                System.out.println("Warning:: Wrong use of switches...");
                return;
            }
            sellCard(event.args[0]);
        });
        processor.add("wallet", event -> {
            System.out.println("You have "
                    + PlayerManager.getInstance().getPlayer().getCoin()
                    + "coins in your wallet.");
        });
    }

    private void showSalable() {
        List<Card> allCards = getSalableCards();
        System.out.println("Salable cards:");
        printCardList(allCards);
    }

    private void showPurchasable() {
        List<Card> allGameCards = getPurchasableCards();
        System.out.println("Purchasable cards:");
        printCardList(allGameCards);
    }

    private void printCardList(List<Card> cardList) {
        for (Card card : cardList) {
            System.out.println(">>");
            System.out.println(card);
            System.out.println();
        }
    }

    private List<Card> getPurchasableCards() {
        List<Card> allGameCards = new ArrayList<>();
        try (Session session = DBUtil.openSession()) {
            allGameCards.addAll(session.createQuery("from Card ", Card.class).list());
        } catch (Exception e) {
            e.printStackTrace();
        }
        PlayerManager.getInstance().refreshPlayer();
        Player player = PlayerManager.getInstance().getPlayer();
        List<Card> ownedCards = player.getOwnedCards();
        allGameCards.removeAll(ownedCards);
        return allGameCards;
    }

    private List<Card> getSalableCards() {
        PlayerManager.getInstance().refreshPlayer();
        Player player = PlayerManager.getInstance().getPlayer();
        List<Card> allCards = new ArrayList<>(player.getOwnedCards());
        for (Deck deck : player.getDecks()) {
            allCards.removeAll(deck.getCardsList());
        }
        return allCards;
    }

    private void buyCard(String cardname) {
        List<Card> purchasableCards = getPurchasableCards();
        Card card = Card.getCardByName(cardname);
        if (card == null) {
            System.out.println("No such card Exists...");
            return;
        }
        if (!purchasableCards.contains(card)) {
            System.out.println("Sorry you can't purchase this card. You already own this card.");
            return;
        }
        Player player = PlayerManager.getInstance().getPlayer();
        if (card.getPrice() > player.getCoin()) {
            System.out.println("You don't have enough coin to buy this card");
            return;
        }
        player.getOwnedCards().add(card);
        player.setCoin(player.getCoin() - card.getPrice());
        DBUtil.syncSingleObject(player);
        System.out.println("Successfully Purchased!");
    }

    private void sellCard(String cardname) {
        List<Card> salableCards = getSalableCards();
        Card card = Card.getCardByName(cardname);
        if (card == null) {
            System.out.println("No such card Exists...");
            return;
        }
        if (!salableCards.contains(card)) {
            System.out.println("Sorry you can't sell this card. Either you don't own this card or it's still in your decks.");
            return;
        }
        Player player = PlayerManager.getInstance().getPlayer();
        player.getOwnedCards().remove(card);
        player.setCoin(player.getCoin() + card.getPrice());
        DBUtil.syncSingleObject(player);
        System.out.println("Successfully Sold!");
    }


    @Override
    public String getActivityCommand() {
        return "store";
    }

    @Override
    public CommandProcessor getProcessor() {
        return processor;
    }
}
