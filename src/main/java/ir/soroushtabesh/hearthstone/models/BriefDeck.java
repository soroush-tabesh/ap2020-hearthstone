package ir.soroushtabesh.hearthstone.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BriefDeck {

    private String name;
    private int winCount;
    private int gameCount;
    private float winRatio;
    private float avgMana;
    private String heroName;
    private String heroClass;
    private String favCard = "N/A";
    private int favCardUsage;

    public static BriefDeck buildBriefDeck(Deck deck) {
        DeckHistory deckHistory = deck.getDeckHistory();
        BriefDeck briefDeck = new BriefDeck();
        briefDeck.name = deck.getName();
        briefDeck.winCount = deckHistory.getWonGames();
        briefDeck.gameCount = deckHistory.getTotalGames();
        briefDeck.winRatio = 1f * briefDeck.winCount / Math.max(briefDeck.gameCount, 1);

        List<Card> cards = deck.getCardsInDeck();
        if (!cards.isEmpty()) {
            for (Card card : cards) {
                briefDeck.avgMana += card.getMana();
            }
            briefDeck.avgMana /= cards.size();
        }

        briefDeck.heroName = deck.getHero().getName();
        briefDeck.heroClass = deck.getHero().getHeroClass().toString();

        for (Map.Entry<Card, Integer> entry : deckHistory.getCardsInDeckUsage().entrySet()) {
            if (entry.getValue() > briefDeck.favCardUsage) {
                briefDeck.favCardUsage = entry.getValue();
                briefDeck.favCard = entry.getKey().getCard_name();
            }
        }

        return briefDeck;
    }

    public static ObservableList<BriefDeck> buildList(Collection<Deck> decks) {
        ObservableList<BriefDeck> result = FXCollections.observableArrayList();
        decks.forEach(deck -> result.add(buildBriefDeck(deck)));
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

    public String getHeroName() {
        return heroName;
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
}
