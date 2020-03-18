package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.beans.Player;

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

    public Player getPlayer() {
        //todo
        return null;
    }

    public enum Message {
        EXISTS, SUCCESS, ERROR, WRONG
    }
}
