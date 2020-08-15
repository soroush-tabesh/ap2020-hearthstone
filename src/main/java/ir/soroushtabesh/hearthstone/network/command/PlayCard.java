package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class PlayCard implements Command {
    private final int cardId;
    private final int groundIndex;
    private int targetId = 0;
    private final long token;

    public PlayCard(CardObject card, int groundIndex, GameObject target, long token) {
        this.cardId = card.getId();
        this.groundIndex = groundIndex;
        if (target != null)
            this.targetId = target.getId();
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        GameController controller = gameServer.getGameControllerByToken(token);
        ModelPool pool = controller.getModelPool();
        return new Packet(controller.playCard((CardObject) pool.getGameObjectById(cardId)
                , groundIndex, pool.getGameObjectById(targetId), token));
    }
}
