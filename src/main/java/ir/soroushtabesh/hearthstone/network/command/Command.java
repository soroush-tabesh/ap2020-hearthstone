package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;
import ir.soroushtabesh.hearthstone.network.models.Message;

public interface Command {
    Message visit(SocketWorker worker, IGameServer gameServer, long pid);
}
