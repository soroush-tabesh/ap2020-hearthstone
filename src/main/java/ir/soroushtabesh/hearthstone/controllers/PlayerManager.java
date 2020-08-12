package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.models.PlayerStats;
import ir.soroushtabesh.hearthstone.network.command.*;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import ir.soroushtabesh.hearthstone.util.Constants;
import ir.soroushtabesh.hearthstone.util.HashUtil;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;
import ir.soroushtabesh.hearthstone.util.db.Seeding;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ir.soroushtabesh.hearthstone.network.RemoteGameServer.sendPOST;

public class PlayerManager {
    private static PlayerManager instanceMain;
    private static PlayerManager instanceProxy;
    private final Map<String, Long> username2token = Collections.synchronizedMap(new HashMap<>());
    private final Map<Long, String> token2username = Collections.synchronizedMap(new HashMap<>());
    private final SecureRandom random = new SecureRandom();

    private PlayerManager() {
    }

    public static PlayerManager getInstance() {
        if (Constants.isServerMode()) {
            if (instanceMain == null)
                instanceMain = new PlayerManager();
            return instanceMain;
        } else {
            if (instanceProxy == null)
                instanceProxy = new PlayerManagerProxy();
            return instanceProxy;
        }
    }

    public Message logout(long token) {
        String s = token2username.remove(token);
        if (s != null)
            username2token.remove(s);
        return s != null ? Message.SUCCESS : Message.ERROR;
    }

    public Message logout() {
        return null;
    }

    public Packet authenticate(String username, String password) {
        password = HashUtil.hash(password);
        String finalPassword = password;
        return DBUtil.doInJPA((session) -> {
            Player player;
            try {
                player = (Player) session
                        .createQuery("from Player where username=:username and deleted=false ")
                        .setParameter("username", username).uniqueResult();
                if (player == null || !player.getPassword().equals(finalPassword))
                    return new Packet(Message.WRONG);
            } catch (Exception e) {
                e.printStackTrace();
                return new Packet(Message.ERROR);
            }
            Logger.log("PlayerManager", "login " + player.getUsername());
            Packet packet = new Packet(Message.SUCCESS);
            long token = random.nextLong();
            token2username.put(token, username);
            username2token.put(username, token);
            packet.setParcel(token);
            return packet;
        });
    }

    public Message deleteAccount(String username, String password) {
        password = HashUtil.hash(password);
        Player player = getPlayerByUsername(username);
        if (player == null || !password.equals(player.getPassword()))
            return Message.WRONG;
        DBUtil.doInJPA(session -> {
            player.setDeleted(true);
            return null;
        });
        Logger.log("PlayerManager", "delete-user" + player.getUsername());
        Long aLong = username2token.remove(username);
        if (aLong != null)
            token2username.remove(aLong);
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
        return null;
    }

    public void refreshPlayer() {

    }

    public Long getToken() {
        return null;
    }

    public Player getPlayerByID(int id) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Player where id=:id", Player.class)
                .setParameter("id", id)
                .uniqueResult());
    }

    public Player getPlayerByUsername(String username) {
        return DBUtil.doInJPA(session -> session
                .createQuery("from Player where username=:username and deleted=false", Player.class)
                .setParameter("username", username)
                .uniqueResult());
    }

    public Player getPlayerByToken(long token) {
        return getPlayerByUsername(token2username.get(token));
    }

    public List<PlayerStats> getPlayers() {
        return DBUtil.doInJPA(session ->
                session.createQuery(
                        "select p.playerStats from Player p where p.deleted=false " +
                                "order by p.playerStats.cupCount desc , p.username asc", PlayerStats.class)
                        .list());
    }

    private static class PlayerManagerProxy extends PlayerManager {

        private boolean loggedIn = false;
        private long token;
        private Player player;

        @Override
        public Message logout() {
            if (!loggedIn)
                return Message.ERROR;
            Message message = sendPOST(new Logout(token)).getMessage();
            if (message == Message.SUCCESS) {
                loggedIn = false;
                player = null;
            }
            return message;
        }

        @Override
        public Message logout(long token) {
            return null;
        }

        @Override
        public Packet authenticate(String username, String password) {
            Packet packet = sendPOST(new Authentication(username, password));
            Object tk = packet.getParcel();
            if (tk != null) {
                token = (long) tk;
                loggedIn = true;
            }
            return packet;
        }

        @Override
        public Message deleteAccount(String username, String password) {
            Message message = sendPOST(new DeleteAccount(username, password)).getMessage();
            if (player != null && loggedIn && player.getUsername().equals(username) && message == Message.SUCCESS) {
                loggedIn = false;
                player = null;
            }
            return message;
        }

        @Override
        public Message makeAccount(String username, String password) {
            return sendPOST(new MakeAccount(username, password)).getMessage();
        }

        @Override
        public Player getPlayer() {
            if (!loggedIn)
                return null;
            if (player != null)
                return player;
            Player player = getPlayerByToken(token);
            this.player = player;
            return player;
        }

        @Override
        public void refreshPlayer() {
            player = null;
            getPlayer();
        }

        @Override
        public Long getToken() {
            return token;
        }

        @Override
        public Player getPlayerByID(int id) {
            return (Player) sendPOST(new GetPlayer(id)).getParcel();
        }

        @Override
        public Player getPlayerByUsername(String username) {
            return (Player) sendPOST(new GetPlayer(username)).getParcel();
        }

        @Override
        public Player getPlayerByToken(long token) {
            return (Player) sendPOST(new GetPlayer(token)).getParcel();
        }

        @Override
        public List<PlayerStats> getPlayers() {
            return (List<PlayerStats>) sendPOST(new GetPlayers()).getParcel();
        }
    }

}
