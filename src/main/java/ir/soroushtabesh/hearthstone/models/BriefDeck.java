package ir.soroushtabesh.hearthstone.models;

import ir.soroushtabesh.hearthstone.controllers.DeckManager;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BriefDeck implements Serializable {

    private static final long serialVersionUID = 8474100873165587456L;
    private Deck deck;
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
        briefDeck.refresh2();
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
        deck = DeckManager.getInstance().getDeckByID(deck.getId());
        refresh2();
    }

    private void refresh2() {
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

        List<Map.Entry<Card, Integer>> prior = new ArrayList<>(deckHistory.getCardsInDeckUsage().entrySet());
        prior.sort((o1, o2) -> new CompareToBuilder()
                .append(o2.getValue(), o1.getValue())
                .append(o2.getKey().getRarity().ordinal(), o1.getKey().getRarity().ordinal())
                .append(o2.getKey().getMana(), o1.getKey().getMana())
                .append(o2.getKey() instanceof Minion ? 1 : 0, o1.getKey() instanceof Minion ? 1 : 0)
                .toComparison());
        if (prior.isEmpty()) {
            favCardUsage = 0;
            favCard = "N/A";
        } else {
            favCardUsage = prior.get(0).getValue();
            favCard = prior.get(0).getKey().getName();
        }
    }
}
