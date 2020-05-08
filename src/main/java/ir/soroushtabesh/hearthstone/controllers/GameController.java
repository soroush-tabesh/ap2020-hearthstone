package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.BriefHero;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;

public class GameController {
    private Deck deckModel;
    private Hero heroModel;
    private ObservableList<Card> hand = FXCollections.observableArrayList();
    private ObservableList<Card> deck = FXCollections.observableArrayList();
    private ObservableList<Card> ground = FXCollections.observableArrayList();
    private ObservableList<String> log = FXCollections.observableArrayList();
    private BriefHero briefHero;
    private IntegerProperty mana = new SimpleIntegerProperty();
    private IntegerProperty manaMax = new SimpleIntegerProperty();

    public GameController(Hero heroModel, Deck deckModel) {
        this.deckModel = deckModel;
        this.heroModel = heroModel;
        briefHero = BriefHero.build(heroModel);
    }

    public Deck getDeckModel() {
        return deckModel;
    }

    public Hero getHeroModel() {
        return heroModel;
    }

    public ObservableList<Card> getHand() {
        return hand;
    }

    public ObservableList<Card> getDeck() {
        return deck;
    }

    public ObservableList<Card> getGround() {
        return ground;
    }

    public ObservableList<String> getLog() {
        return log;
    }

    public BriefHero getBriefHero() {
        return briefHero;
    }

    public int getMana() {
        return mana.get();
    }

    public IntegerProperty manaProperty() {
        return mana;
    }

    public int getManaMax() {
        return manaMax.get();
    }

    public IntegerProperty manaMaxProperty() {
        return manaMax;
    }

    private void fillDeck() {
        List<Card> fullDeck = deckModel.getFullDeck();
        Collections.shuffle(fullDeck);
        deck.addAll(fullDeck);
    }

    private void fillHand() {
        for (int i = 0; i < 3; i++) {
            hand.add(deck.remove(0));
        }
    }

    public void startGame() {
        fillDeck();
        fillHand();
        resetMana();
        log("Game Start");
    }

    public boolean playCard(Card card) {
        if (card.getMana() > mana.intValue())
            return false;
        if (ground.size() >= 7)
            return false;
        int index = hand.indexOf(card);
        if (index < 0)
            return false;
        Card removed = hand.remove(index);
        mana.setValue(mana.intValue() - card.getMana());
        if (card instanceof Minion)
            ground.add(removed);
        log("Play: " + card.getCard_name() + " (" + card.getClass().getSimpleName() + ")");
        return true;
    }

    private boolean drawToHand() {
        if (hand.size() >= 12 || deck.isEmpty())
            return false;
        hand.add(deck.remove(0));
        return true;
    }

    private void resetMana() {
        if (manaMax.intValue() < 10) {
            manaMax.setValue(manaMax.intValue() + 1);
        }
        mana.setValue(manaMax.getValue());
    }

    public void endTurn() {
        resetMana();
        drawToHand();
        log("End turn");
    }

    public void log(String log) {
        this.log.add(log);
    }
}
