package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class MakeAccount implements Command {
    private final String username;
    private final String password;

    public MakeAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        return new Packet(PlayerManager.getInstance().makeAccount(username, password));
    }
}
