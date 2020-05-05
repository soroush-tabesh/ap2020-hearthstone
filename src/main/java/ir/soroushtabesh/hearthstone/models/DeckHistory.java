package ir.soroushtabesh.hearthstone.models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public class DeckHistory {
    private Integer totalGames = 0;
    private Integer wonGames = 0;
    @ElementCollection
    @Column(name = "deck_card_count_use")
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private Map<Card, Integer> cardsInDeckUsage = new HashMap<>();

    public DeckHistory() {
    }

    public Map<Card, Integer> getCardsInDeckUsage() {
        return cardsInDeckUsage;
    }

    public Integer getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(Integer total_games) {
        this.totalGames = total_games;
    }

    public Integer getWonGames() {
        return wonGames;
    }

    public void setWonGames(Integer won_games) {
        this.wonGames = won_games;
    }
}
