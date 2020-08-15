package ir.soroushtabesh.hearthstone.network;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.PlayerController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.network.command.*;
import ir.soroushtabesh.hearthstone.network.models.Packet;
import javafx.application.Platform;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static ir.soroushtabesh.hearthstone.network.RemoteGameServer.sendPOST;

public class RemoteGameController extends GameController {

    private final RemoteGameServer server = RemoteGameServer.getInstance();
    private Timer timer;
    private boolean beacon = false;

    public RemoteGameController() {
    }

    private synchronized void startBeacon() {
        if (beacon)
            return;
        timer = new Timer();
        beacon = true;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateModelPool((ModelPool.ModelPoolShadow)
                        server.requestGame(PlayerManager.getInstance().getToken()
                                , new Hero(), new Deck(), new InfoPassive())
                                .getJSONParcel());
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private synchronized void stopBeacon() {
        if (!beacon)
            return;
        timer.cancel();
        timer.purge();
        timer = null;
        beacon = false;
    }

    private void updateModelPool(ModelPool.ModelPoolShadow modelPoolShadow) {
        if (modelPoolShadow == null)
            return;
        ModelPool shadow = modelPoolShadow.toModelPool(this);
//        System.out.println("bla: "+shadow.getPlayerDataList().get(0).getHandCard());
//        System.out.println("bla: "+shadow.getPlayerDataList().get(1).getHandCard());
        Platform.runLater(() -> getModelPool().update(shadow));
    }

    @Override
    public PlayerController registerPlayer(Player player, Hero hero, Deck deck, InfoPassive infoPassive, boolean shuffle) {
        if (getModelPool().getSceneData().isGameReady())
            return null;
        Packet packet = server.requestGame(PlayerManager.getInstance().getToken(), hero, deck, infoPassive);
        if (packet.getMessage() == Message.SUCCESS) {
            startBeacon();
            return new PlayerController(this, PlayerManager.getInstance().getToken());
        } else if (packet.getMessage() == Message.WAIT) {
            startBeacon();
            return new PlayerController(this, PlayerManager.getInstance().getToken());
        }
        return null;
    }

    @Override
    public PlayerController registerPlayer(Player player, Hero hero, Deck deck, InfoPassive infoPassive, List<Card> cardOrder) {
        // TODO: 8/14/20 Deck reader
        return null;
    }

    @Override
    public boolean endTurn(long token) {
        return sendPOST(new EndTurn(token)).getMessage() == Message.SUCCESS;
    }

    @Override
    public Message startGame(long token) {
        return sendPOST(new StartGame(token)).getMessage();
    }

    @Override
    public Message playCard(CardObject cardObject, int groundIndex, GameObject optionalTarget, long token) {
        return sendPOST(new PlayCard(cardObject, groundIndex, optionalTarget, token)).getMessage();
    }

    @Override
    public Message playMinion(MinionObject source, GameObject target, long token) {
        return sendPOST(new PlayMinion(source, target, token)).getMessage();
    }

    @Override
    public Message useWeapon(HeroObject source, GameObject target, long token) {
        return sendPOST(new UseWeapon(source, target, token)).getMessage();
    }

    @Override
    public Message useHeroPower(HeroObject source, GameObject target, long token) {
        return sendPOST(new UseHeroPower(source, target, token)).getMessage();
    }

    @Override
    public Message changeCard(int cardNumberInList, long token) {
        return sendPOST(new ChangeCard(cardNumberInList, token)).getMessage();
    }

    @Override
    public int token2playerId(long token) {
        Packet packet = sendPOST(new Token2PlayerID(token));
        if (packet.getMessage() != Message.SUCCESS)
            return -1;
        return (int) packet.getJSONParcel();
    }

    @Override
    public PlayerController[] getAllPlayerControllers() {
        return new PlayerController[0];
    }

    @Override
    public Message summonMinion(MinionObject source, long token) {
        return null;
    }

    @Override
    public void logEvent(GameAction gameAction) {

    }

}
