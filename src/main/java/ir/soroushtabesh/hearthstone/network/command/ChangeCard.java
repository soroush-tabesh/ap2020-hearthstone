package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class ChangeCard implements Command {
    private final int cardNum;
    private final long token;

    public ChangeCard(int cardNum, long token) {
        this.cardNum = cardNum;
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        GameController controller = gameServer.getGameControllerByToken(token);
        return new Packet(controller.changeCard(cardNum, token));
    }
}
