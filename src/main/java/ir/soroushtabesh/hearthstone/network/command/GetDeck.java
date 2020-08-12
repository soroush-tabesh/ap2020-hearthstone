package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.DeckManager;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

public class GetDeck implements Command {
    private int id;

    public GetDeck(int id) {
        this.id = id;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        Deck deck;
        deck = DeckManager.getInstance().getDeckByID(id);
        Packet packet;
        if (deck != null)
            packet = new Packet(Message.SUCCESS);
        else
            packet = new Packet(Message.ERROR);
        DBUtil.hydrate(deck);
        packet.setParcel(deck);
        return packet;
    }
}
