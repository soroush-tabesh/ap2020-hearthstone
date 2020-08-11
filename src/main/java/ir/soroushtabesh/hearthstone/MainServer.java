package ir.soroushtabesh.hearthstone;

import ir.soroushtabesh.hearthstone.network.ComServer;
import ir.soroushtabesh.hearthstone.network.LocalGameServer;
import ir.soroushtabesh.hearthstone.util.Constants;
import ir.soroushtabesh.hearthstone.util.db.Seeding;

public class MainServer {
    private static ComServer comServer;
    public static void main(String[] args) {
        Constants.setServerMode(true);
        Seeding.initiate();
        comServer = new ComServer(new LocalGameServer());
        comServer.startServer();
    }
}
