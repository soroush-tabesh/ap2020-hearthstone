package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class RequestGame implements Command {
    private final long token;
    private final Deck deck;
    private final Hero hero;
    private final InfoPassive passive;

    public RequestGame(long token, Hero hero, Deck deck, InfoPassive passive) {
        this.token = token;
        this.deck = new Deck();
        this.hero = new Hero();
        this.passive = new InfoPassive();
        this.deck.setId(deck.getId());
        this.hero.setId(hero.getId());
        this.passive.setId(passive.getId());
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        return gameServer.requestGame(token, hero, deck, passive);
    }
}
