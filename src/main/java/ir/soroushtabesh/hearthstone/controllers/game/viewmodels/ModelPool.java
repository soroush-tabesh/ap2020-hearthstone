package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import com.google.gson.reflect.TypeToken;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.models.cards.HeroPower;
import ir.soroushtabesh.hearthstone.models.cards.Minion;
import ir.soroushtabesh.hearthstone.models.cards.Spell;
import ir.soroushtabesh.hearthstone.models.cards.Weapon;
import ir.soroushtabesh.hearthstone.util.JSONUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModelPool implements Serializable {
    private static final long serialVersionUID = 6513610423154357758L;

    private transient GameController gameController;
    private SceneData sceneData;
    private long gid;
    private final List<PlayerData> playerDataList = new ArrayList<>();
    private List<GameObject> gameObjects = new ArrayList<>();

    public ModelPool(GameController gameController) {
        this.gameController = gameController;
        this.sceneData = new SceneData(gameController);
        this.gid = new SecureRandom().nextLong();
    }

    private ModelPool(long gid) {
        this.gid = gid;
    }

    public SceneData getSceneData() {
        return sceneData;
    }

    public long getGid() {
        return gid;
    }

    public List<MinionObject> getAllMinions() {
        ModelPool.PlayerData pd0 = getPlayerDataById(0);
        ModelPool.PlayerData pd1 = getPlayerDataById(1);
        List<MinionObject> allCards = new ArrayList<>();
        allCards.addAll(pd0.getGroundCard());
        allCards.addAll(pd1.getGroundCard());
        return allCards;
    }

    public PlayerData getPlayerDataById(int id) {
        if (id >= 0 && id < playerDataList.size())
            return playerDataList.get(id);
        else
            return null;
    }

    public List<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    public void addPlayerData(PlayerData playerData) {
        playerDataList.add(playerData.getPlayerId(), playerData);
        System.out.println("ModelPool.addPlayerData" + playerDataList.size());
    }

    public int generateID(GameObject gameObject) {
        gameObjects.add(gameObject);
        return gameObjects.size() - 1;
    }

    public GameObject getGameObjectById(int id) {
        if (id < 0 || id >= gameObjects.size())
            return null;
        return gameObjects.get(id);
    }

    public void update(ModelPool shadow) {
        gid = shadow.gid;
        for (int i = 0; i < shadow.playerDataList.size(); i++) {
            if (playerDataList.size() < i + 1)
                addPlayerData(shadow.getPlayerDataById(i));
            else
                playerDataList.get(i).update(shadow.playerDataList.get(i), gameController);
        }
        for (int i = 0; i < shadow.gameObjects.size(); i++) {
            if (gameObjects.size() < i + 1)
                gameObjects.add(shadow.gameObjects.get(i));
            else
                gameObjects.get(i).update(shadow.gameObjects.get(i), gameController);
        }
        if (sceneData == null)
            sceneData = shadow.sceneData;
        else
            sceneData.update(shadow.getSceneData(), gameController);
    }

    public static class SceneData {
        private transient GameController gameController;
        private SimpleBooleanProperty started = new SimpleBooleanProperty(false);
        private SimpleBooleanProperty gameReady = new SimpleBooleanProperty(false);
        private SimpleIntegerProperty turn = new SimpleIntegerProperty(-1);
        private SimpleIntegerProperty winner = new SimpleIntegerProperty(-1);
        private final ObservableList<GameAction> log = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

        public SceneData(GameController gameController) {
            this.gameController = gameController;
        }

        public void update(SceneData shadow, GameController gameController) {
            started.set(shadow.started.get());
            gameReady.set(shadow.gameReady.get());
            turn.set(shadow.turn.get());
            winner.set(shadow.winner.get());
            this.gameController = gameController;

            for (int i = 0; i < shadow.log.size(); i++) {
                if (log.size() < i + 1)
                    log.add(shadow.log.get(i));
            }
        }

        public boolean isStarted() {
            return started.get();
        }

        public BooleanProperty startedProperty() {
            return started;
        }

        public void setStarted(boolean started) {
            this.started.set(started);
        }

        public boolean isGameReady() {
            return gameReady.get();
        }

        public BooleanProperty gameReadyProperty() {
            return gameReady;
        }

        public void setGameReady(boolean gameReady) {
            this.gameReady.set(gameReady);
        }

        public int getTurn() {
            return turn.get();
        }

        public IntegerProperty turnProperty() {
            return turn;
        }

        public void setTurn(int turn) {
            this.turn.set(turn);
        }

        public int getWinner() {
            return winner.get();
        }

        public IntegerProperty winnerProperty() {
            return winner;
        }

        public void setWinner(int winner) {
            this.winner.set(winner);
        }

        public ObservableList<GameAction> getLog() {
            return log;
        }
    }

    public static class PlayerData {
        private int playerId;
        private transient GameController gameController;
        //cards
        private ObservableList<CardObject> deckCard = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
        private ObservableList<CardObject> handCard = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
        private ObservableList<MinionObject> groundCard = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
        private ObservableList<MinionObject> deadCard = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
        private ObservableList<CardObject> burnedCard = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
        private ObservableList<Boolean> changeCardFlag = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
        //flags and counters
        private SimpleIntegerProperty mana = new SimpleIntegerProperty();
        private SimpleIntegerProperty manaMax = new SimpleIntegerProperty();
        private SimpleBooleanProperty myTurn = new SimpleBooleanProperty();
        private SimpleBooleanProperty ready = new SimpleBooleanProperty();
        private Player player;
        private Hero heroModel;
        private Deck deckModel;
        private InfoPassive infoPassiveModel;
        //objects
        private HeroObject hero;
        private GenericScript infoPassive;

        private PlayerData() {
        }

        public PlayerData(int playerId, GameController gameController
                , Player player, Hero heroModel, Deck deckModel, InfoPassive infoPassiveModel) {
            this(playerId, gameController, player, heroModel, deckModel, infoPassiveModel, true);
        }

        public PlayerData(int playerId, GameController gameController
                , Player player, Hero heroModel, Deck deckModel, InfoPassive infoPassiveModel, boolean shuffleDeck) {
            this.playerId = playerId;
            this.gameController = gameController;
            this.player = player;
            setHeroModel(heroModel);
            setDeckModel(deckModel, shuffleDeck);
            setInfoPassiveModel(infoPassiveModel);
        }

        public void update(PlayerData shadow, GameController gameController) {
            this.gameController = gameController;

            playerId = shadow.playerId;

            matchLists(shadow.deckCard, deckCard);
            matchLists(shadow.handCard, handCard);
            matchLists(shadow.groundCard, groundCard);
            matchLists(shadow.deadCard, deadCard);
            matchLists(shadow.burnedCard, burnedCard);
            changeCardFlag.clear();
            changeCardFlag.addAll(shadow.changeCardFlag);

            mana.set(shadow.mana.get());
            manaMax.set(shadow.manaMax.get());
            myTurn.set(shadow.myTurn.get());
            ready.set(shadow.ready.get());

            player = shadow.player;
            heroModel = shadow.heroModel;
            deckModel = shadow.deckModel;
            infoPassiveModel = shadow.infoPassiveModel;

            if (hero == null)
                hero = shadow.hero;
            else
                hero.update(shadow.hero, gameController);
        }

        private <T extends CardObject> void matchLists(List<T> refList, List<T> tarList) {
            refList.forEach(cardObject -> {
                int index = tarList.indexOf(cardObject);
                if (index == -1)
                    tarList.add(cardObject);
                else
                    tarList.get(index).update(cardObject, gameController);
            });
            tarList.removeIf(t -> !refList.contains(t));
            for (int i = 0; i < refList.size(); i++) {
                if (refList.get(i) == tarList.get(i))
                    continue;
                swap(i, tarList.indexOf(refList.get(i)), tarList);
            }
        }

        private <T> void swap(int i, int j, List<T> list) {
            if (i == j || i >= list.size() || j >= list.size() || i < 0 || j < 0)
                return;
            int ti = i;
            int tj = j;
            i = Math.min(ti, tj);
            j = Math.max(ti, tj);
            T b = list.remove(j);
            T a = list.remove(i);
            list.add(i, b);
            list.add(j, a);
        }

        public Player getPlayer() {
            return player;
        }

        public void setGameController(GameController gameController) {
            this.gameController = gameController;
        }

        public int getPlayerId() {
            return playerId;
        }

        public HeroObject getHero() {
            return hero;
        }

        public Deck getDeckModel() {
            return deckModel;
        }

        private void setDeckModel(Deck deckModel, boolean shuffleDeck) {
            this.deckModel = deckModel;
            deckModel.getFullDeck().forEach(card -> deckCard.add(CardObject.build(playerId, gameController, card)));
            if (shuffleDeck)
                Collections.shuffle(deckCard);
        }

        public void setCardByOrder(List<Card> cardOrder) {
            deckCard.clear();
            cardOrder.forEach(card -> deckCard.add(CardObject.build(playerId, gameController, card)));
        }

        public Hero getHeroModel() {
            return heroModel;
        }

        private void setHeroModel(Hero heroModel) {
            this.heroModel = heroModel;
            hero = new HeroObject(playerId, gameController, heroModel);
        }

        public InfoPassive getInfoPassiveModel() {
            return infoPassiveModel;
        }

        private void setInfoPassiveModel(InfoPassive infoPassiveModel) {
            this.infoPassiveModel = infoPassiveModel;
            setInfoPassive(infoPassiveModel.getScriptModel().getScript(gameController));
            infoPassive.setOwnerObject(hero);
            gameController.getScriptEngine().registerScript(infoPassive);
        }

        public GenericScript getInfoPassive() {
            return infoPassive;
        }

        public void setInfoPassive(GenericScript infoPassive) {
            this.infoPassive = infoPassive;
            hero.addMiscScript(infoPassive);
        }

        public ObservableList<CardObject> getDeckCard() {
            return deckCard;
        }

        public ObservableList<CardObject> getHandCard() {
            return handCard;
        }

        public ObservableList<MinionObject> getGroundCard() {
            return groundCard;
        }

        public ObservableList<MinionObject> getDeadCard() {
            return deadCard;
        }

        public ObservableList<CardObject> getBurnedCard() {
            return burnedCard;
        }

        public ObservableList<Boolean> getChangeCardFlag() {
            return changeCardFlag;
        }

        public int getMana() {
            return mana.get();
        }

        public void setMana(int mana) {
            this.mana.set(mana);
        }

        public IntegerProperty manaProperty() {
            return mana;
        }

        public int getManaMax() {
            return manaMax.get();
        }

        public void setManaMax(int manaMax) {
            this.manaMax.set(manaMax);
        }

        public IntegerProperty manaMaxProperty() {
            return manaMax;
        }

        public boolean isMyTurn() {
            return myTurn.get();
        }

        public void setMyTurn(boolean myTurn) {
            this.myTurn.set(myTurn);
        }

        public BooleanProperty myTurnProperty() {
            return myTurn;
        }

        public boolean isReady() {
            return ready.get();
        }

        public void setReady(boolean ready) {
            this.ready.set(ready);
        }

        public BooleanProperty readyProperty() {
            return ready;
        }
    }

    public static class ModelPoolShadow implements Serializable {

        private static final long serialVersionUID = -1157560470636529636L;

        private final long gid;
        private SceneDataShadow sceneData;
        private List<PlayerDataShadow> playerDataList = new ArrayList<>();
        private String goData;

        public ModelPoolShadow(ModelPool modelPool) {
            sceneData = new SceneDataShadow(modelPool.getSceneData());
            playerDataList.add(new PlayerDataShadow(modelPool.playerDataList.get(0), false));
            playerDataList.add(new PlayerDataShadow(modelPool.playerDataList.get(1), false));
            goData = JSONUtil.getGson().toJson(modelPool.gameObjects);
            gid = modelPool.gid;
        }

        public ModelPoolShadow(ModelPool modelPool, long token) {
            Player playerByToken = PlayerManager.getInstance().getPlayerByToken(token);
            sceneData = new SceneDataShadow(modelPool.getSceneData());
            playerDataList = modelPool.playerDataList.stream().map(playerData ->
                    new PlayerDataShadow(playerData, playerData.player != playerByToken))
                    .collect(Collectors.toList());
            goData = JSONUtil.getGson().toJson(modelPool.gameObjects);
            gid = modelPool.gid;
        }

        public ModelPool toModelPool(GameController gameController) {
            ModelPool modelPool = new ModelPool(gid);
            modelPool.sceneData = sceneData.toSceneData(gameController);
            modelPool.gameObjects = JSONUtil.getGson().fromJson(goData, new TypeToken<List<GameObject>>() {
            }.getType());
            modelPool.gameObjects.forEach(gameObject -> gameObject.setGameController(gameController));
            modelPool.playerDataList.add(playerDataList.get(0).toPlayerData(gameController));
            modelPool.playerDataList.add(playerDataList.get(1).toPlayerData(gameController));
            return modelPool;
        }
    }

    public static class SceneDataShadow implements Serializable {

        private static final long serialVersionUID = 1698549900402484133L;
        private final List<GameAction> log;
        private final SimpleBooleanProperty started;
        private final SimpleBooleanProperty gameReady;
        private final SimpleIntegerProperty turn;
        private final SimpleIntegerProperty winner;

        public SceneDataShadow(SceneData sceneData) {
            log = new ArrayList<>(sceneData.log);
            started = sceneData.started;
            gameReady = sceneData.gameReady;
            turn = sceneData.turn;
            winner = sceneData.winner;
        }

        public SceneData toSceneData(GameController gameController) {
            SceneData sceneData = new SceneData(null);
            sceneData.gameController = gameController;
            sceneData.log.addAll(log);
            sceneData.started = started;
            sceneData.gameReady = gameReady;
            sceneData.turn = turn;
            sceneData.winner = winner;
            return sceneData;
        }
    }

    public static class PlayerDataShadow implements Serializable {
        private static final long serialVersionUID = 4223373459911117358L;

        private int playerId;
        //cards
        private List<CardObject> deckCard;
        private List<CardObject> handCard;
        private List<MinionObject> groundCard;
        private List<MinionObject> deadCard;
        private List<CardObject> burnedCard;
        private List<Boolean> changeCardFlag;
        //flags and counters
        private SimpleIntegerProperty mana;
        private SimpleIntegerProperty manaMax;
        private SimpleBooleanProperty myTurn;
        private SimpleBooleanProperty ready;
        private Player player;
        private Hero heroModel;
        private Deck deckModel;
        private InfoPassive infoPassiveModel;
        //objects
        private HeroObject hero;

        public PlayerDataShadow(PlayerData playerData, boolean hideSensitive) {
            playerId = playerData.playerId;

            if (hideSensitive)
                deckCard = new ArrayList<>(getDummyCards(playerData.deckCard));
            else
                deckCard = new ArrayList<>(playerData.deckCard);
            if (hideSensitive)
                handCard = new ArrayList<>(getDummyCards(playerData.handCard));
            else
                handCard = new ArrayList<>(playerData.handCard);
            groundCard = new ArrayList<>(playerData.groundCard);
            deadCard = new ArrayList<>(playerData.deadCard);
            burnedCard = new ArrayList<>(playerData.burnedCard);
            if (hideSensitive)
                changeCardFlag = new ArrayList<>();
            else
                changeCardFlag = new ArrayList<>(playerData.changeCardFlag);

            mana = playerData.mana;
            manaMax = playerData.manaMax;
            myTurn = playerData.myTurn;
            ready = playerData.ready;
            player = playerData.player;
            heroModel = playerData.heroModel;
            deckModel = playerData.deckModel;
            infoPassiveModel = playerData.infoPassiveModel;

            hero = playerData.hero;
        }

        private List<CardObject> getDummyCards(List<CardObject> orig) {
            return orig.stream().map(cardObject -> {
                Card cardModel = cardObject.getCardModel();
                Card res = null;
                if (cardModel instanceof Minion) {
                    res = new Minion();
                    res.setId(0);
                } else if (cardModel instanceof Spell) {
                    res = new Spell();
                    res.setId(0);
                } else if (cardModel instanceof Weapon) {
                    res = new Weapon();
                    res.setId(0);
                } else if (cardModel instanceof HeroPower) {
                    res = cardModel;
                }
                return new CardObject(cardObject.getId(), cardObject.getPlayerId()
                        , res);
            }).collect(Collectors.toList());
        }

        public PlayerData toPlayerData(GameController gameController) {
            PlayerData playerData = new PlayerData();
            playerData.setGameController(gameController);

            playerData.playerId = playerId;

            playerData.deckCard = FXCollections.observableList(deckCard);
            playerData.deckCard.forEach(cardObject -> cardObject.setGameController(gameController));
            playerData.handCard = FXCollections.observableList(handCard);
            playerData.handCard.forEach(cardObject -> cardObject.setGameController(gameController));
            playerData.groundCard = FXCollections.observableList(groundCard);
            playerData.groundCard.forEach(cardObject -> cardObject.setGameController(gameController));
            playerData.deadCard = FXCollections.observableList(deadCard);
            playerData.deadCard.forEach(cardObject -> cardObject.setGameController(gameController));
            playerData.burnedCard = FXCollections.observableList(burnedCard);
            playerData.burnedCard.forEach(cardObject -> cardObject.setGameController(gameController));
            playerData.changeCardFlag = FXCollections.observableList(changeCardFlag);

            playerData.mana = mana;
            playerData.manaMax = manaMax;
            playerData.myTurn = myTurn;
            playerData.ready = ready;
            playerData.player = player;
            playerData.heroModel = heroModel;
            playerData.deckModel = deckModel;
            playerData.infoPassiveModel = infoPassiveModel;

            playerData.hero = hero;

            return playerData;
        }
    }
}
