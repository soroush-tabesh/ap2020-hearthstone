package ir.soroushtabesh.hearthstone.controllers;

import ir.soroushtabesh.hearthstone.db.DBUtil;
import ir.soroushtabesh.hearthstone.db.HashUtil;
import ir.soroushtabesh.hearthstone.models.beans.Player;
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
        try (Session session = DBUtil.openSession()) {
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
        return Message.SUCCESS;
    }

    public Message deleteAccount(String password) {
        password = HashUtil.hash(password);
        if (!password.equals(player.getPassword()))
            return Message.WRONG;
        player.setDeleted(true);
        try (Session session = DBUtil.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(player);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.ERROR;
        }
        return Message.SUCCESS;
    }

    public Message makeAccount(String username, String password) {
        password = HashUtil.hash(password);
        Player player = new Player();
        player.setPassword(password);
        player.setUsername(username);
        Transaction transaction = null;
        try (Session session = DBUtil.openSession()) {
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
        return Message.SUCCESS;
    }

    public Player getPlayer() {
        return player;
    }

    public enum Message {
        EXISTS, SUCCESS, ERROR, WRONG
    }
}
