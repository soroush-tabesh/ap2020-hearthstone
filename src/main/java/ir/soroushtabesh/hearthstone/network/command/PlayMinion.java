package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.CardObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.MinionObject;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public class PlayMinion implements Command {
    private final int sourceId;
    private int targetId = 0;
    private final long token;

    public PlayMinion(CardObject source, GameObject target, long token) {
        this.sourceId = source.getId();
        if (target != null)
            this.targetId = target.getId();
        this.token = token;
    }

    @Override
    public Packet visit(SocketWorker worker, IGameServer gameServer, long pid) {
        GameController controller = gameServer.getGameControllerByToken(token);
        ModelPool pool = controller.getModelPool();
        return new Packet(controller
                .playMinion((MinionObject) pool.getGameObjectById(sourceId)
                        , pool.getGameObjectById(targetId), token));
    }
}
