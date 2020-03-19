package ir.soroushtabesh.hearthstone.cli.activities;

import ir.soroushtabesh.hearthstone.cli.CLIActivity;
import ir.soroushtabesh.hearthstone.cli.CommandProcessor;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.beans.*;
import ir.soroushtabesh.hearthstone.util.DBUtil;
import ir.soroushtabesh.hearthstone.util.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class Collections extends CLIActivity {

    private CommandProcessor processor = new CommandProcessor();

    @Override
    public void onStart(String[] args) {
        System.out.println("::Collections");
        processor.add("ls", event -> {
            if (event.args.length != 2) {
                System.out.println("Warning:: Wrong use of switches...");
                return;
            }
            switch (event.args[1]) {
                case "-heroes":
                    showHeroes(event.args[0]);
                    break;
                case "-cards":
                    showCards(event.args[0]);
                    break;
                default:
                    System.out.println("Warning:: Unknown switch...");
            }
        });
        processor.add("select", event -> {
            if (event.args.length != 1) {
                System.out.println("Warning:: Wrong use of switches...");
                return;
            }
            selectHero(event.args[0]);
        });
        processor.add("add", event -> {
            if (event.args.length != 1) {
                System.out.println("Warning:: Wrong use of switches...");
                return;
            }
            addCardToDeck(event.args[0]);
        });
        processor.add("remove", event -> {
            if (event.args.length != 1) {
                System.out.println("Warning:: Wrong use of switches...");
                return;
            }
            removeCardFromDeck(event.args[0]);
        });
    }

    private void showHeroes(String option) {
        PlayerManager.getInstance().refreshPlayer();
        Player player = PlayerManager.getInstance().getPlayer();
        switch (option) {
            case "-a":
                List<Hero> lst = new ArrayList<>();
                try (Session session = DBUtil.openSession()) {
                    lst = session.createQuery("from Hero ", Hero.class).list();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("All Heroes:\n");
                for (Hero hero : lst) {
                    System.out.println(">>");
                    System.out.println(hero);
                    System.out.println();
                }
                Logger.log("collections", "ls -a -heroes");
                break;
            case "-m":
                System.out.println("Owned Heroes:\n");
                for (Hero hero : player.getOpenHeroes()) {
                    System.out.println(">>");
                    System.out.println(hero);
                    System.out.println();
                }
                Logger.log("collections", "ls -m -heroes");
                break;
            default:
                System.out.println("Warning:: Unknown switch...");
                Logger.log("collections", "ls -heroes: unknown switch", Log.Severity.WARNING);
        }

    }

    private void showCards(String option) {
        PlayerManager.getInstance().refreshPlayer();
        Player player = PlayerManager.getInstance().getPlayer();
        Deck deck = player.getDeckOfHero(player.getCurrentHero());
        switch (option) {
            case "-a":
                System.out.println("All cards of yours:\n");
                for (Card card : player.getOwnedCards()) {
                    System.out.println(">>");
                    System.out.println(card);
                    System.out.println();
                }
                Logger.log("collections", "ls -a -cards");
                break;
            case "-m":
                if (deck == null) {
                    System.out.println("Well, first you need to select a hero...");
                    Logger.log("collections", "ls -m -cards: hero not selected", Log.Severity.WARNING);
                    return;
                }
                System.out.println("Cards in your deck:\n");
                for (Card card : deck.getCardsList()) {
                    System.out.println(">>");
                    System.out.println(card);
                    System.out.println();
                }
                Logger.log("collections", "ls -m -cards");

                break;
            case "-n":
                if (deck == null) {
                    System.out.println("Well, first you need to select a hero...");
                    Logger.log("collections", "ls -n -cards: hero not selected", Log.Severity.WARNING);
                    return;
                }
                System.out.println("Owned cards which you can add to the deck of hero:\n");
                for (Card card : player.getOwnedCards()) {
                    int cnt = 0;
                    for (Card carddeck : deck.getCardsList()) {
                        cnt += card.equals(carddeck) ? 1 : 0;
                    }
                    if (cnt >= 2)
                        continue;
                    System.out.println(">>");
                    System.out.println(card);
                    System.out.println();
                }
                Logger.log("collections", "ls -n -cards");
                break;
            default:
                System.out.println("Warning:: Unknown switch...");
                Logger.log("collections", "ls -cards: unknown switch", Log.Severity.WARNING);
        }

    }

    private void selectHero(String heroname) {
        try (Session session = DBUtil.openSession()) {
            Player player = PlayerManager.getInstance().getPlayer();
            Hero hero = session.createQuery("from Hero where name=:heroname", Hero.class)
                    .setParameter("heroname", heroname).uniqueResult();
            if (hero == null) {
                System.out.println("Warning:: Wrong hero name...");
                Logger.log("collections", "select hero: wrong name", Log.Severity.WARNING);
                return;
            }
            if (!player.getOpenHeroes().contains(hero)) {
                System.out.println("You don't own this hero.");
                Logger.log("collections", "select hero: locked", Log.Severity.WARNING);
                return;
            }
            player.setCurrentHero(hero);
            session.beginTransaction();
            session.saveOrUpdate(player);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log("collections", "select hero: db error", Log.Severity.FATAL);
        }
    }

    private void addCardToDeck(String cardname) {
        PlayerManager.getInstance().refreshPlayer();
        Player player = PlayerManager.getInstance().getPlayer();
        Deck deck = player.getDeckOfHero(player.getCurrentHero());
        Card card = Card.getCardByName(cardname);
        if (card == null) {
            System.out.println("No such card Exists...");
            Logger.log("collections", "add: no such card", Log.Severity.WARNING);
            return;
        }
        if (deck == null) {
            System.out.println("Well, first you need to select a hero...");
            Logger.log("collections", "add: hero not selected", Log.Severity.WARNING);
            return;
        }
        if (!player.getOwnedCards().contains(card)) {
            System.out.println("You don't own this hero.");
            Logger.log("collections", "add: locked", Log.Severity.WARNING);
            return;
        }
        deck.addCard(card);
        DBUtil.syncSingleObject(deck);
        System.out.println("Successfully added to your deck.");
    }

    private void removeCardFromDeck(String cardname) {
        PlayerManager.getInstance().refreshPlayer();
        Player player = PlayerManager.getInstance().getPlayer();
        Deck deck = player.getDeckOfHero(player.getCurrentHero());
        Card card = Card.getCardByName(cardname);
        if (card == null) {
            System.out.println("No such card Exists...");
            Logger.log("collections", "remove: no such card", Log.Severity.WARNING);
            return;
        }
        if (deck == null) {
            System.out.println("Well, first you need to select a hero...");
            Logger.log("collections", "remove: hero not selected", Log.Severity.WARNING);
            return;
        }
        if (deck.removeCard(card)) {
            DBUtil.syncSingleObject(deck);
            System.out.println("Successfully removed from your deck.");
        } else {
            System.out.println("This card is not in your deck!");
            Logger.log("collections", "remove: not in deck", Log.Severity.WARNING);
        }
    }

    @Override
    public String getActivityCommand() {
        return "collections";
    }

    @Override
    public CommandProcessor getProcessor() {
        return processor;
    }
}
