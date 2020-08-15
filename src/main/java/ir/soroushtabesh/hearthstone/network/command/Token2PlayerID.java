package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class Token2PlayerID implements Command {
    private final long token;

    public Token2PlayerID(long token) {
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        GameController controller = gameServer.getGameControllerByToken(token);
        if (controller == null)
            return new Packet(Message.WRONG);
        int playerId = controller.token2playerId(token);
        Packet packet = new Packet(Message.SUCCESS);
        packet.setJSONParcel(playerId);
        return packet;
    }
}
