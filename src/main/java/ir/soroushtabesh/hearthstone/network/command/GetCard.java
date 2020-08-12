package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

public class GetCard implements Command {
    private final String name;

    public GetCard(String name) {
        this.name = name;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        Packet packet = new Packet(Message.SUCCESS);
        Card card = CardManager.getInstance().getCardByName(name);
        DBUtil.hydrate(card);
        packet.setParcel(card);
        return packet;
    }
}
