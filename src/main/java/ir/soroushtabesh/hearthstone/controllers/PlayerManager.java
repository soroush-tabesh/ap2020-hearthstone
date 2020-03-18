package ir.soroushtabesh.hearthstone.controllers;

public class PlayerManager {
    private static PlayerManager instance;

    private PlayerManager() {
    }

    public static PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();
        return instance;
    }

    public boolean logout() {
        //todo
        return false;
    }

    public Message authenticate(String username, String password) {
        //todo
        return Message.SUCCESS;
    }

    public Message makeAccount(String username, String password) {
        //todo
        return Message.SUCCESS;
    }

    public enum Message {
        EXISTS, SUCCESS, ERROR, WRONG
    }
}
