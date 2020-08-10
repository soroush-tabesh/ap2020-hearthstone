package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.network.models.Message;

public class LocalGameServer implements IGameServer {
    @Override
    public Message signUp(String username, String password) {
        return null;
    }

    @Override
    public UserHandle login(String username, String password) {
        return null;
    }

    @Override
    public Message logout(long token) {
        return null;
    }
}
