package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.DeckManager;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class RemoveCardInDeck implements Command {
    private final int card;
    private final int deck;
    private final long token;

    public RemoveCardInDeck(int card, int deck, long token) {
        this.card = card;
        this.deck = deck;
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        return new Packet(DeckManager.getInstance().removeCardFromDeck(card, deck, token) ? Message.SUCCESS : Message.ERROR);
    }
}
