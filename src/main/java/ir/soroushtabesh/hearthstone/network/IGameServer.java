package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.network.models.Message;

public interface IGameServer {
    Message signUp(String username, String password);

    UserHandle login(String username, String password);

    Message logout(long token);


}
