package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class BuyCard implements Command {
    private final long token;
    private final Card card;

    public BuyCard(long token, Card card) {
        this.token = token;
        this.card = card;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        return new Packet(CardManager.getInstance().buyCard(card, token));
    }
}
