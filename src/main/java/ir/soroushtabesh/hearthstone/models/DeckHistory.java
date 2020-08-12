package ir.soroushtabesh.hearthstone.models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public class DeckHistory implements Serializable {
    private static final long serialVersionUID = 7187395941249722709L;
    private Integer totalGames = 0;
    private Integer wonGames = 0;
    private Integer cupCount = 0;
    @ElementCollection
    @Column(name = "deck_card_count_use")
    @Cascade({CascadeType.MERGE, CascadeType.REFRESH, CascadeType.SAVE_UPDATE})
    private final Map<Card, Integer> cardsInDeckUsage = new HashMap<>();

    public DeckHistory() {
    }

    public Integer getCupCount() {
        return cupCount;
    }

    public void setCupCount(Integer cup) {
        this.cupCount = cup;
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
