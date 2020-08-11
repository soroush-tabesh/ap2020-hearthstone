package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class Logout implements Command {
    private final long token;

    public Logout(long token) {
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        return new Packet(PlayerManager.getInstance().logout(token));
    }
}
