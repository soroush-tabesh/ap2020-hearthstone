//package ir.soroushtabesh.hearthstone.views.cli.activities;
//
//import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
//import ir.soroushtabesh.hearthstone.models.*;
//import ir.soroushtabesh.hearthstone.util.Logger;
//import ir.soroushtabesh.hearthstone.util.cli.CommandProcessor;
//import ir.soroushtabesh.hearthstone.util.cli.PrintUtil;
//import ir.soroushtabesh.hearthstone.util.db.DBUtil;
//import ir.soroushtabesh.hearthstone.views.cli.CLIActivity;
//import org.hibernate.Session;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Collections extends CLIActivity {
//
//    private CommandProcessor processor = new CommandProcessor();
//
//    @Override
//    public void onStart(String[] args) {
//        System.out.println("::Collections");
//        processor.add("ls", event -> {
//            if (event.args.length != 2) {
//                System.out.println("Warning:: Wrong use of switches...");
//                return;
//            }
//            switch (event.args[1]) {
//                case "-heroes":
//                    showHeroes(event.args[0]);
//                    break;
//                case "-cards":
//                    showCards(event.args[0]);
//                    break;
//                default:
//                    System.out.println("Warning:: Unknown switch...");
//            }
//        });
//        processor.add("select", event -> {
//            if (event.args.length != 1) {
//                System.out.println("Warning:: Wrong use of switches...");
//                return;
//            }
//            selectHero(event.args[0]);
//        });
//        processor.add("add", event -> {
//            if (event.args.length != 1) {
//                System.out.println("Warning:: Wrong use of switches...");
//                return;
//            }
//            addCardToDeck(event.args[0]);
//        });
//        processor.add("remove", event -> {
//            if (event.args.length != 1) {
//                System.out.println("Warning:: Wrong use of switches...");
//                return;
//            }
//            removeCardFromDeck(event.args[0]);
//        });
//    }
//
//    private void showHeroes(String option) {
//        Player player = PlayerManager.getInstance().getPlayer();
//        DBUtil.doInJPA(session -> {
//            try {
//                session.refresh(player);
//                switch (option) {
//                    case "-a":
//                        List<Hero> lst = session.createQuery("from Hero ", Hero.class).list();
//                        System.out.println("All Heroes:\n");
//                        PrintUtil.printList(lst);
//                        System.out.println("Heroes of yours:\n");
//                        PrintUtil.printList(player.getOpenHeroes());
//                        Logger.log("collections", "ls -a -heroes");
//                        break;
//                    case "-m":
//                        Hero hero = player.getCurrentHero();
//                        if (hero == null) {
//                            System.out.println("You haven't selected any hero.");
//                            return null;
//                        }
//                        System.out.println("Current Hero:\n");
//                        PrintUtil.printFormat(hero);
//                        Logger.log("collections", "ls -m -heroes");
//                        break;
//                    default:
//                        System.out.println("Warning:: Unknown switch...");
//                        Logger.log("collections", "ls -heroes: unknown switch", Log.Severity.WARNING);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("collections", "ls -heroes: db error", Log.Severity.FATAL);
//            }
//            return null;
//        });
//    }
//
//    private void showCards(String option) {
//        Player player = PlayerManager.getInstance().getPlayer();
//        DBUtil.doInJPA(session -> {
//            try {
//                session.refresh(player);
//                Deck deck = player.getDeckOfHero(player.getCurrentHero(), session);
//                switch (option) {
//                    case "-a":
//                        Logger.log("collections", "ls -a -cards");
//                        System.out.println("All cards of yours:\n");
//                        PrintUtil.printList(player.getOwnedCardsList());
//                        break;
//                    case "-m":
//                        Logger.log("collections", "ls -m -cards");
//                        if (deck == null) {
//                            System.out.println("Well, first you need to select a hero...");
//                            Logger.log("collections", "ls -m -cards: hero not selected", Log.Severity.WARNING);
//                            return null;
//                        }
//                        System.out.println("Cards in your deck:\n");
//                        PrintUtil.printList(deck.getCardsInDeck());
//                        break;
//                    case "-n":
//                        Logger.log("collections", "ls -n -cards");
//                        if (deck == null) {
//                            System.out.println("Well, first you need to select a hero...");
//                            Logger.log("collections", "ls -n -cards: hero not selected", Log.Severity.WARNING);
//                            return null;
//                        }
//                        System.out.println("Owned cards which you can add to the deck of hero:\n");
//                        PrintUtil.printList(getAddableCards(session));
//                        break;
//                    default:
//                        System.out.println("Warning:: Unknown switch...");
//                        Logger.log("collections", "ls -cards: unknown switch", Log.Severity.WARNING);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("collections", "ls -cards: db error", Log.Severity.FATAL);
//            }
//            return null;
//        });
//    }
//
//    private List<Card> getAddableCards(Session session) {
//        List<Card> list = new ArrayList<>();
//        Player player = PlayerManager.getInstance().getPlayer();
//        Deck deck = player.getDeckOfHero(player.getCurrentHero(), session);
//        for (Card card : player.getOwnedCardsList()) {
//            int cnt = 0;
//            for (Card inDeck : deck.getCardsInDeck()) {
//                cnt += card.equals(inDeck) ? 1 : 0;
//            }
//            if (cnt >= 2)
//                continue;
//            list.add(card);
//        }
//        return list;
//    }
//
//    private void selectHero(String heroName) {
//        Player player = PlayerManager.getInstance().getPlayer();
//        DBUtil.doInJPA(session -> {
//            try {
//                session.refresh(player);
//                Hero hero = session.createQuery("from Hero where name=:heroName", Hero.class)
//                        .setParameter("heroName", heroName).uniqueResult();
//                if (hero == null) {
//                    System.out.println("Warning:: Wrong hero name...");
//                    Logger.log("collections", "select hero: wrong name", Log.Severity.WARNING);
//                    return null;
//                }
//                if (!player.getOpenHeroes().contains(hero)) {
//                    System.out.println("You don't own this hero.");
//                    Logger.log("collections", "select hero: locked", Log.Severity.WARNING);
//                    return null;
//                }
//                player.setCurrentHero(hero);
//                DBUtil.pushSingleObject(player, session);
//                System.out.println("Successfully selected " + hero.getName());
//                Logger.log("collections", "select hero: " + hero.getName());
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("collections", "select hero: db error", Log.Severity.FATAL);
//            }
//
//            return null;
//        });
//    }
//
//    @Transactional
//    private void addCardToDeck(String cardname) {
//        Player player = PlayerManager.getInstance().getPlayer();
//        DBUtil.doInJPA(session -> {
//            try {
//                session.refresh(player);
//                Hero currentHero = player.getCurrentHero();
//                Deck deck = player.getDeckOfHero(currentHero, session);
//                Card card = Card.getCardByName(cardname, session);
//                if (card == null) {
//                    System.out.println("No such card Exists...");
//                    Logger.log("collections", "add: no such card", Log.Severity.WARNING);
//                    return null;
//                }
//                if (deck == null) {
//                    System.out.println("Well, first you need to select a hero...");
//                    Logger.log("collections", "add: hero not selected", Log.Severity.WARNING);
//                    return null;
//                }
//                if (!player.getOwnedCardsList().contains(card)) {
//                    System.out.println("You don't own this card.");
//                    Logger.log("collections", "add: locked", Log.Severity.WARNING);
//                    return null;
//                }
//                if (!getAddableCards(session).contains(card)
//                        || deck.getCardsInDeck().size() >= 15
//                        || (card.getHeroClass() != Hero.HeroClass.ALL && card.getHeroClass() != currentHero.getHeroClass())) {
//                    System.out.println("You can't add this card to your deck.");
//                    Logger.log("collections", "add: unable", Log.Severity.WARNING);
//                    return null;
//                }
//                session.refresh(player);
//                deck.addCard(card);
//                DBUtil.pushSingleObject(player, session);
//                System.out.println("Successfully added to your deck.");
//                Logger.log("collections", "add: " + card.getName());
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("collections", "add: db error", Log.Severity.FATAL);
//            }
//
//            return null;
//        });
//    }
//
//    private void removeCardFromDeck(String cardname) {
//        Player player = PlayerManager.getInstance().getPlayer();
//        DBUtil.doInJPA(session -> {
//            try {
//                session.refresh(player);
//                Deck deck = player.getDeckOfHero(player.getCurrentHero(), session);
//                Card card = Card.getCardByName(cardname, session);
//                if (card == null) {
//                    System.out.println("No such card Exists...");
//                    Logger.log("collections", "remove: no such card", Log.Severity.WARNING);
//                    return null;
//                }
//                if (deck == null) {
//                    System.out.println("Well, first you need to select a hero...");
//                    Logger.log("collections", "remove: hero not selected", Log.Severity.WARNING);
//                    return null;
//                }
//                if (deck.removeCardOnce(card)) {
//                    DBUtil.pushSingleObject(deck, session);
//                    System.out.println("Successfully removed from your deck.");
//                    if (deck.getCardsInDeck().size() < 10)
//                        System.out.println("Alert! You have less than 10 cards in your deck!");
//                } else {
//                    System.out.println("This card is not in your deck!");
//                    Logger.log("collections", "remove: not in deck", Log.Severity.WARNING);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Logger.log("collections", "remove: db error", Log.Severity.FATAL);
//            }
//
//            return null;
//        });
//    }
//
//    @Override
//    public String getActivityCommand() {
//        return "collections";
//    }
//
//    @Override
//    public CommandProcessor getProcessor() {
//        return processor;
//    }
//}
