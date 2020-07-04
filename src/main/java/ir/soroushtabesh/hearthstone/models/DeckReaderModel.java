package ir.soroushtabesh.hearthstone.models;

import java.util.ArrayList;
import java.util.List;

public class DeckReaderModel {
    private final List<String> friendlyCardNames = new ArrayList<>();
    private final List<String> enemyCardNames = new ArrayList<>();
    private transient Deck friendly, enemy;

    public List<String> getFriendlyCardNames() {
        return friendlyCardNames;
    }

    public List<String> getEnemyCardNames() {
        return enemyCardNames;
    }

    public Deck getFriendly() {
        return friendly;
    }

    public void setFriendly(Deck friendly) {
        this.friendly = friendly;
        friendly.getFullDeck().forEach(card -> friendlyCardNames.add(card.getName()));
    }

    public Deck getEnemy() {
        return enemy;
    }

    public void setEnemy(Deck enemy) {
        this.enemy = enemy;
        enemy.getFullDeck().forEach(card -> enemyCardNames.add(card.getName()));
    }
}
