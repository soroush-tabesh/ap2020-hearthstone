package ir.soroushtabesh.hearthstone.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayerStats implements Serializable {
    private static final long serialVersionUID = 9186777021941124210L;
    private int ssid;
    private String un;
    private int gameCount;
    private int winCount;
    private int cupCount;
    private transient int rnk;

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public int getSsid() {
        return ssid;
    }

    public void setSsid(int ssid) {
        this.ssid = ssid;
    }

    public int getRnk() {
        return rnk;
    }

    public void setRnk(int rnk) {
        this.rnk = rnk;
    }

    public int getCupCount() {
        return cupCount;
    }

    public void setCupCount(int cup) {
        this.cupCount = cup;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStats that = (PlayerStats) o;
        return getSsid() == that.getSsid();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSsid());
    }
}
