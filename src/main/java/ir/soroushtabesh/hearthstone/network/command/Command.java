package ir.soroushtabesh.hearthstone.network.command;

import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.IGameServer;
import ir.soroushtabesh.hearthstone.network.SocketWorker;

public interface Command {
    Message visit(SocketWorker worker, IGameServer gameServer, long pid);
}
