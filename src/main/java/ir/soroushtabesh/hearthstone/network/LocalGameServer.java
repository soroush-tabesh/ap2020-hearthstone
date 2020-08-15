package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.DeckManager;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.ModelPool;
import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.network.models.Packet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalGameServer implements IGameServer {

    private final Map<Long, Long> token2gid = new HashMap<>();
    private final Map<Long, GameController> gid2game = new HashMap<>();
    private final Map<Long, List<Long>> gid2bantoken = new HashMap<>();
    private Long waiterToken;
    private Hero waiterHero;
    private Deck waiterDeck;
    private InfoPassive waiterPassive;

    @Override
    public synchronized Packet requestGame(long token, Hero hero, Deck deck, InfoPassive passive) {
        PlayerManager playerManager = PlayerManager.getInstance();

        if (!playerManager.checkToken(token))
            return new Packet(Message.WRONG);

        if (((Long) token).equals(waiterToken))
            return new Packet(Message.WAIT);

        Long gid = token2gid.get(token);

        if (gid == null) {
            if (waiterToken == null
                    || !PlayerManager.getInstance().checkToken(waiterToken)
                    || token2gid.get(waiterToken) != null) {

                Player player = PlayerManager.getInstance().getPlayerByToken(token);
                deck = DeckManager.getInstance().getDeckByID(deck.getId());
                hero = CardManager.getInstance().getHeroByID(hero.getId());
                passive = CardManager.getInstance().getPassiveByID(passive.getId());
                if (deck == null || deck.getPlayer() != player || !player.getOpenHeroes().contains(hero)
                        || (deck.getHeroClass() != Hero.HeroClass.ALL && deck.getHeroClass() != hero.getHeroClass()))
                    return new Packet(Message.ERROR);

                waiterToken = token;
                waiterHero = hero;
                waiterDeck = deck;
                waiterPassive = passive;

                return new Packet(Message.WAIT);
            } else {
                if (deck == null || hero == null || passive == null)
                    return new Packet(Message.ERROR);

                Player player = PlayerManager.getInstance().getPlayerByToken(token);
                deck = DeckManager.getInstance().getDeckByID(deck.getId());
                hero = CardManager.getInstance().getHeroByID(hero.getId());
                passive = CardManager.getInstance().getPassiveByID(passive.getId());
                if (deck == null || deck.getPlayer() != player || !player.getOpenHeroes().contains(hero)
                        || (deck.getHeroClass() != Hero.HeroClass.ALL && deck.getHeroClass() != hero.getHeroClass()))
                    return new Packet(Message.ERROR);

                GameController gameController = new LocalGameController();
                gameController.registerPlayer(PlayerManager.getInstance().getPlayerByToken(waiterToken)
                        , waiterHero, waiterDeck, waiterPassive, true);
                gameController.registerPlayer(player, hero, deck, passive, true);

                token2gid.put(waiterToken, gameController.getModelPool().getGid());
                token2gid.put(token, gameController.getModelPool().getGid());
                gid2game.put(gameController.getModelPool().getGid(), gameController);

                waiterToken = null;

                Packet packet = new Packet(Message.SUCCESS);
                packet.setJSONParcel(convert(gameController.getModelPool(), token));
                return packet;
            }
        } else {
            GameController gameController = gid2game.get(gid);
            if (gameController == null)
                return new Packet(Message.ERROR);

            Packet packet = new Packet(Message.SUCCESS);
            packet.setJSONParcel(convert(gameController.getModelPool(), token));
            return packet;
        }
    }

    @Override
    public Packet getGames() {
        Packet packet = new Packet(Message.SUCCESS);
        packet.setParcel(gid2game.keySet());
        return packet;
    }

    @Override
    public Packet requestAudience(long token, long gid) {
        PlayerManager playerManager = PlayerManager.getInstance();
        if (!playerManager.checkToken(token))
            return new Packet(Message.WRONG);
        GameController gameController = gid2game.get(gid);
        if (gameController == null)
            return new Packet(Message.ERROR);
        Packet packet = new Packet(Message.SUCCESS);
        packet.setJSONParcel(convert(gameController.getModelPool(), 0));
        return packet;
    }

    @Override
    public GameController getGameControllerByToken(long token) {
        Long key = token2gid.get(token);
        if (key == null)
            return null;
        return gid2game.get(key);
    }

    public ModelPool.ModelPoolShadow convert(ModelPool modelPool, long token) {
        return new ModelPool.ModelPoolShadow(modelPool, token);
    }
}
