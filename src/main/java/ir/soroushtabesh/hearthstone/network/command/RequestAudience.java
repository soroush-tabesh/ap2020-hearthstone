package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class RequestAudience implements Command {
    private final long token;
    private final long gid;

    public RequestAudience(long token, long gid) {
        this.token = token;
        this.gid = gid;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        return gameServer.requestAudience(token, gid);
    }
}
