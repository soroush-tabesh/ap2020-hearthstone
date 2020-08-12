package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

import java.util.ArrayList;

public class GetPlayers implements Command {
    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        Packet packet = new Packet(Message.SUCCESS);
        packet.setParcel(new ArrayList<>(PlayerManager.getInstance().getPlayers()));
        return packet;
    }
}
