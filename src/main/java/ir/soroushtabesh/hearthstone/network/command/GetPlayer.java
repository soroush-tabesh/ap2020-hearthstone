package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class GetPlayer implements Command {
    private Integer id;
    private Long token;
    private String username;

    public GetPlayer(String username) {
        this.username = username;
    }

    public GetPlayer(Long token) {
        this.token = token;
    }

    public GetPlayer(Integer id) {
        this.id = id;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        Player player;
        if (id != null) {
            player = PlayerManager.getInstance().getPlayerByID(id);
        } else if (token != null) {
            player = PlayerManager.getInstance().getPlayerByToken(token);
        } else {
            player = PlayerManager.getInstance().getPlayerByUsername(username);
        }
        if (player != null) {
            Packet p = new Packet(Message.SUCCESS);
            p.setParcel(player);
            return p;
        } else {
            return new Packet(Message.ERROR);
        }
    }
}
