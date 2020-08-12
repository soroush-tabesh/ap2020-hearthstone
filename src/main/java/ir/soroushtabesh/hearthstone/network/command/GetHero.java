package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import ir.soroushtabesh.hearthstone.util.db.DBUtil;

public class GetHero implements Command {
    private final Hero.HeroClass hc;

    public GetHero(Hero.HeroClass hc) {
        this.hc = hc;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        Packet packet = new Packet(Message.SUCCESS);
        Hero hero = CardManager.getInstance().getHeroByClass(hc);
        DBUtil.hydrate(hero);
        packet.setParcel(hero);
        return packet;
    }
}
