package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.DBUtil;
import ir.soroushtabesh.hearthstone.util.HashUtil;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.Seeding;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        try {
            Session session = DBUtil.getOpenSession();
            Player player = (Player) session.createQuery("from Player where username=:username and deleted=false ")
                    .setParameter("username", username).uniqueResult();
            if (player == null || !player.getPassword().equals(password)) {
                return Message.WRONG;
            }
            this.player = player;
        } catch (Exception e) {
            e.printStackTrace();
            return Message.ERROR;
        }
        Logger.log("login", player.getUsername());
        return Message.SUCCESS;
    }

    public Message deleteAccount(String password) {
        password = HashUtil.hash(password);
        if (!password.equals(player.getPassword()))
            return Message.WRONG;
        player.setDeleted(true);
        try {
            Session session = DBUtil.getOpenSession();
            Transaction transaction = session.beginTransaction();
            session.merge(player);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.ERROR;
        }
        Logger.log("delete-user", "deleted: " + player.getUsername());
        return Message.SUCCESS;
    }

    public Message makeAccount(String username, String password) {
        password = HashUtil.hash(password);
        Player player = new Player(username, password);
        Transaction transaction = null;
        try {
            Session session = DBUtil.getOpenSession();
            transaction = session.beginTransaction();
            boolean exist = session.createQuery("from Player where username=:username and deleted=false ")
                    .setParameter("username", username).uniqueResult() != null;
            if (exist)
                return Message.EXISTS;
            session.save(player);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.ERROR;
        }
        Seeding.seedPlayer(player);
        Logger.log("sign-up", player.getUsername());
        return Message.SUCCESS;
    }

    public Player getPlayer() {
        return player;
    }

    public void refreshPlayer() {
        try {
            Session session = DBUtil.getOpenSession();
            session.refresh(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Message {
        EXISTS, SUCCESS, ERROR, WRONG
    }
}
