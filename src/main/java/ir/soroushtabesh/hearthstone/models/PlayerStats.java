package ir.soroushtabesh.hearthstone.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PlayerStats implements Serializable {
    private static final long serialVersionUID = 9186777021941124210L;
    private int gameCount;
    private int winCount;

    public int getGameCount() {
        return gameCount;
    }

    public void setGameCount(int gameCount) {
        this.gameCount = gameCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }
}
