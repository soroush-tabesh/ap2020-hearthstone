package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

public class GetCard implements Command {
    private String name;
    private Integer id;

    public GetCard(String name) {
        this.name = name;
    }

    public GetCard(Integer id) {
        this.id = id;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        Card card;
        if (name != null)
            card = CardManager.getInstance().getCardByName(name);
        else
            card = CardManager.getInstance().getCardByID(id);
        Packet packet;
        if (card != null)
            packet = new Packet(Message.SUCCESS);
        else
            packet = new Packet(Message.ERROR);
        DBUtil.hydrate(card);
        packet.setParcel(card);
        return packet;
    }
}
