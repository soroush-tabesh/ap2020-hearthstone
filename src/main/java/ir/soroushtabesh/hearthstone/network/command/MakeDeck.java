package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.DeckManager;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class MakeDeck implements Command {
    private final Deck deck;
    private final long token;

    public MakeDeck(Deck deck, long token) {
        this.deck = new Deck(deck.getId(), deck.getName(), deck.getHeroClass());
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        return new Packet(DeckManager.getInstance().saveNewDeck(deck, token));
    }
}
