package ir.soroushtabesh.hearthstone.network.models;

public class Config {
    private int port = 8000;

    public Config() {
    }

    public Config(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
