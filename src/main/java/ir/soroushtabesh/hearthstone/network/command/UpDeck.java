package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.DeckManager;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class UpDeck implements Command {
    private final Deck deck;
    private final long token;

    public UpDeck(Deck deck, long token) {
        this.deck = deck;
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        DeckManager.getInstance().updateDeckProperties(deck, token);
        return new Packet(Message.SUCCESS);
    }
}
