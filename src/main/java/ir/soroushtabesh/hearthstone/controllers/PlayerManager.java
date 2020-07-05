package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.HashUtil;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;
import ir.soroushtabesh.hearthstone.util.db.Seeding;

public class PlayerManager {
    private static PlayerManager instance;
    private Player player;

    private PlayerManager() {
    }

    public static PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();
        return instance;
    }

    public boolean logout() {
        boolean res = player != null;
        player = null;
        return res;
    }

    public Message authenticate(String username, String password) {
        password = HashUtil.hash(password);
        String finalPassword = password;
        return DBUtil.doInJPA((session) -> {
            try {
                Player player1 = (Player) session
                        .createQuery("from Player where username=:username and deleted=false ")
                        .setParameter("username", username).uniqueResult();
                if (player1 == null || !player1.getPassword().equals(finalPassword)) {
                    return Message.WRONG;
                }
                this.player = player1;
            } catch (Exception e) {
                e.printStackTrace();
                return Message.ERROR;
            }
            Logger.log("PlayerManager", "login " + player.getUsername());
            return Message.SUCCESS;
        });
    }

    public Message deleteAccount(String password) {
        password = HashUtil.hash(password);
        if (!password.equals(player.getPassword()))
            return Message.WRONG;
        player.setDeleted(true);
        try {
            DBUtil.pushSingleObject(player);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.ERROR;
        }
        Logger.log("PlayerManager", "delete-user" + player.getUsername());
        player = null;
        return Message.SUCCESS;
    }

    public Message makeAccount(String username, String password) {
        password = HashUtil.hash(password);
        Player player = new Player(username, password);
        Message message = DBUtil.doInJPA(session -> {
            try {
                boolean exist = session.createQuery("from Player where username=:username and deleted=false ")
                        .setParameter("username", username).uniqueResult() != null;
                if (exist)
                    return Message.EXISTS;
                session.save(player);
            } catch (Exception e) {
                e.printStackTrace();
                return Message.ERROR;
            }
            Logger.log("PlayerManager", "sign-up: " + player.getUsername());
            return Message.SUCCESS;
        });
        if (message == Message.SUCCESS) {
            Seeding.seedPlayer(player);
        }
        return message;
    }

    public Player getPlayer() {
        return player;
    }

    public void refreshPlayer() {
        try {
            player = DBUtil.doInJPA(session -> session.get(player.getClass(), player.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer(Player player) {
        Logger.log("PlayerManager", "update player " + player.getUsername());
        DBUtil.doInJPA(session -> {
            session.saveOrUpdate(player);
            return null;
        });
    }

    public enum Message {
        EXISTS, SUCCESS, ERROR, WRONG
    }
}
