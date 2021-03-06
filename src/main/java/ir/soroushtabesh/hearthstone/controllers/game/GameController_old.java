//package ir.soroushtabesh.hearthstone.controllers.game;
//
//import ir.soroushtabesh.hearthstone.models.BriefHero;
//import ir.soroushtabesh.hearthstone.models.Card;
//import ir.soroushtabesh.hearthstone.models.Deck;
//import ir.soroushtabesh.hearthstone.models.Hero;
//import ir.soroushtabesh.hearthstone.models.cards.Minion;
//import ir.soroushtabesh.hearthstone.models.cards.Quest;
//import ir.soroushtabesh.hearthstone.models.cards.Weapon;
//import ir.soroushtabesh.hearthstone.util.Logger;
//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.ObjectProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.util.Collections;
//import java.util.List;
//
//public class GameController_old {
//    private Deck deckModel;
//    private Hero heroModel;
//    private ObservableList<Card> hand = FXCollections.observableArrayList();
//    private ObservableList<Card> deck = FXCollections.observableArrayList();
//    private ObservableList<Card> ground = FXCollections.observableArrayList();
//    private ObservableList<String> log = FXCollections.observableArrayList();
//    private BriefHero briefHero;
//    private IntegerProperty mana = new SimpleIntegerProperty();
//    private IntegerProperty manaMax = new SimpleIntegerProperty();
//    private ObjectProperty<Weapon> currentWeapon = new SimpleObjectProperty<>();
//    private ObjectProperty<Card> burnedCard = new SimpleObjectProperty<>();
//
//    public GameController_old(Hero heroModel, Deck deckModel) {
//        this.deckModel = deckModel;
//        this.heroModel = heroModel;
//        briefHero = BriefHero.build(heroModel);
//    }
//
//    public Deck getDeckModel() {
//        return deckModel;
//    }
//
//    public Hero getHeroModel() {
//        return heroModel;
//    }
//
//    public ObservableList<Card> getHand() {
//        return hand;
//    }
//
//    public ObservableList<Card> getDeck() {
//        return deck;
//    }
//
//    public ObservableList<Card> getGround() {
//        return ground;
//    }
//
//    public ObservableList<String> getLog() {
//        return log;
//    }
//
//    public BriefHero getBriefHero() {
//        return briefHero;
//    }
//
//    public int getMana() {
//        return mana.get();
//    }
//
//    public IntegerProperty manaProperty() {
//        return mana;
//    }
//
//    public int getManaMax() {
//        return manaMax.get();
//    }
//
//    public IntegerProperty manaMaxProperty() {
//        return manaMax;
//    }
//
//    public Weapon getCurrentWeapon() {
//        return currentWeapon.get();
//    }
//
//    public ObjectProperty<Weapon> currentWeaponProperty() {
//        return currentWeapon;
//    }
//
//    public Card getBurnedCard() {
//        return burnedCard.get();
//    }
//
//    public ObjectProperty<Card> burnedCardProperty() {
//        return burnedCard;
//    }
//
//    public void startGame() {
//        fillDeck();
//        fillHand();
//        resetMana();
//        log("Game Start");
//    }
//
//    public Message playCard(Card card) {
//        if (card.getMana() > mana.intValue())
//            return Message.INSUFFICIENT;
//        if (ground.size() >= 7)
//            return Message.GROUND_FULL;
//        int index = hand.indexOf(card);
//        if (index < 0)
//            return Message.ERROR;
//        Card removed = hand.remove(index);
//        mana.setValue(mana.intValue() - card.getMana());
//        if (card instanceof Minion)
//            ground.add(removed);
//        if (card instanceof Weapon)
//            currentWeapon.setValue((Weapon) card);
//        log("Play: " + card.getName() + " (" + card.getClass().getSimpleName() + ")");
//        return Message.SUCCESS;
//    }
//
//    public void endTurn() {
//        resetMana();
//        drawToHand();
//        log("End turn");
//    }
//
//    public void log(String log) {
//        Logger.log("GameController", log);
//        this.log.add(log);
//    }
//
//    private boolean drawToHand() {
//        if (deck.isEmpty()) {
//            return false;
//        } else if (hand.size() >= 12) {
//            burnedCard.set(deck.remove(0));
//            return false;
//        }
//        hand.add(deck.remove(0));
//        return true;
//    }
//
//    private void resetMana() {
//        if (manaMax.intValue() < 10) {
//            manaMax.setValue(manaMax.intValue() + 1);
//        }
//        mana.setValue(manaMax.getValue());
//    }
//
//    private void fillDeck() {
//        List<Card> fullDeck = deckModel.getFullDeck();
//        Collections.shuffle(fullDeck);
//        for (int i = 0; i < fullDeck.size(); i++) {
//            if (fullDeck.get(i) instanceof Quest) {
//                Collections.swap(fullDeck, 0, i);
//            }
//        }
//        deck.addAll(fullDeck);
//    }
//
//    private void fillHand() {
//        for (int i = 0; i < 3; i++) {
//            hand.add(deck.remove(0));
//        }
//    }
//
//    public enum Message {
//        INSUFFICIENT, GROUND_FULL, ERROR, SUCCESS;
//    }
//}
