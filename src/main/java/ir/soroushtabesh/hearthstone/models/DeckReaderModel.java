package ir.soroushtabesh.hearthstone.models;

import java.util.ArrayList;
import java.util.List;

public class DeckReaderModel {
    private final List<String> friendly = new ArrayList<>();
    private final List<String> enemy = new ArrayList<>();
    private transient Deck friendlyDeck, enemyDeck;

    public List<String> getFriendlyCardNames() {
        return friendly;
    }

    public List<String> getEnemyCardNames() {
        return enemy;
    }

    public Deck getFriendlyDeck() {
        return friendlyDeck;
    }

    public void setFriendlyDeck(Deck friendlyDeck) {
        this.friendlyDeck = friendlyDeck;
        friendlyDeck.getFullDeck().forEach(card -> friendly.add(card.getName()));
    }

    public Deck getEnemyDeck() {
        return enemyDeck;
    }

    public void setEnemyDeck(Deck enemyDeck) {
        this.enemyDeck = enemyDeck;
        enemyDeck.getFullDeck().forEach(card -> enemy.add(card.getName()));
    }
}
