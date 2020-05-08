package ir.soroushtabesh.hearthstone.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BriefDeck {

    private final Deck deck;
    private String name;
    private int winCount;
    private int gameCount;
    private float winRatio;
    private float avgMana;
    private String heroClass;
    private String favCard = "N/A";
    private int favCardUsage;

    private BriefDeck(Deck deck) {
        this.deck = deck;
    }

    public static BriefDeck build(Deck deck) {
        BriefDeck briefDeck = new BriefDeck(deck);
        briefDeck.refresh();
        return briefDeck;
    }

    public static ObservableList<BriefDeck> buildAll(Collection<Deck> decks) {
        ObservableList<BriefDeck> result = FXCollections.observableArrayList();
        decks.forEach(deck -> result.add(build(deck)));
        return result;
    }

    public String getName() {
        return name;
    }

    public int getWinCount() {
        return winCount;
    }

    public int getGameCount() {
        return gameCount;
    }

    public float getWinRatio() {
        return winRatio;
    }

    public float getAvgMana() {
        return avgMana;
    }

    public String getHeroClass() {
        return heroClass;
    }

    public String getFavCard() {
        return favCard;
    }

    public int getFavCardUsage() {
        return favCardUsage;
    }

    public Deck getDeck() {
        return deck;
    }

    public void refresh() {
        DeckHistory deckHistory = deck.getDeckHistory();
        name = deck.getName();
        winCount = deckHistory.getWonGames();
        gameCount = deckHistory.getTotalGames();
        winRatio = 1f * winCount / Math.max(gameCount, 1);

        List<Card> cards = deck.getCardsInDeck();
        if (!cards.isEmpty()) {
            for (Card card : cards) {
                avgMana += card.getMana();
            }
            avgMana /= cards.size();
        }

        heroClass = deck.getHeroClass().toString();

        for (Map.Entry<Card, Integer> entry : deckHistory.getCardsInDeckUsage().entrySet()) {
            if (entry.getValue() > favCardUsage) {
                favCardUsage = entry.getValue();
                favCard = entry.getKey().getCard_name();
            }
        }
    }
}
