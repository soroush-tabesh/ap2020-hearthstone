package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class GetMinions implements Command {
    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        List<Minion> cards = new ArrayList<>(CardManager.getInstance().getAllMinions());
        Packet packet = new Packet(Message.SUCCESS);
        DBUtil.hydrate(cards);
        packet.setParcel(cards);
        return packet;
    }
}
