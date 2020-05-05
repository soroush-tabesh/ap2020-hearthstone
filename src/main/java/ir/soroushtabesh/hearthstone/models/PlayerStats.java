package ir.soroushtabesh.hearthstone.models;

import javax.persistence.Embeddable;

@Embeddable
public class PlayerStats {
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
