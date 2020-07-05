package ir.soroushtabesh.hearthstone.models;

import java.util.ArrayList;
import java.util.List;

public class DeckReaderModel {
    private final List<String> friend = new ArrayList<>();
    private final List<String> enemy = new ArrayList<>();
    private transient Deck friendlyDeck, enemyDeck;

    public List<String> getFriendlyCardNames() {
        return friend;
    }

    public List<String> getEnemyCardNames() {
        return enemy;
    }

    public Deck getFriendlyDeck() {
        return friendlyDeck;
    }

    public void setFriendlyDeck(Deck friendlyDeck) {
        this.friendlyDeck = friendlyDeck;
        friend.clear();
        friendlyDeck.getFullDeck().forEach(card -> friend.add(card.getName()));
    }

    public Deck getEnemyDeck() {
        return enemyDeck;
    }

    public void setEnemyDeck(Deck enemyDeck) {
        this.enemyDeck = enemyDeck;
        enemy.clear();
        enemyDeck.getFullDeck().forEach(card -> enemy.add(card.getName()));
    }
}
