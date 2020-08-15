package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class EndTurn implements Command {
    private final long token;

    public EndTurn(long token) {
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        GameController controller = gameServer.getGameControllerByToken(token);
        return new Packet(controller.endTurn(token) ? Message.SUCCESS : Message.WRONG);
    }
}
