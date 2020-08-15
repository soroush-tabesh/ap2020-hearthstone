package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import ir.soroushtabesh.hearthstone.network.models.Packet;

public interface IGameServer {
    Packet requestGame(long token, Hero hero, Deck deck, InfoPassive passive);

    Packet getGames();

    Packet requestAudience(long token, long gid);

    GameController getGameControllerByToken(long token);
}
