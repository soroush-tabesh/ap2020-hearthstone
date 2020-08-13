package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import com.google.gson.reflect.TypeToken;
import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.util.JSONUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelPool implements Serializable {
    private static final long serialVersionUID = 6513610423154357758L;

    private transient GameController gameController;
    private SceneData sceneData;
    private final List<PlayerData> playerDataList = new ArrayList<>();
    private List<GameObject> gameObjects = new ArrayList<>();

    public ModelPool(GameController gameController) {
        this.gameController = gameController;
        this.sceneData = new SceneData(gameController);
    }

    private ModelPool() {
    }

    public SceneData getSceneData() {
        return sceneData;
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

    public static class SceneData {
        private final transient GameController gameController;
        private final ObservableList<GameAction> log = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

        public SceneData(GameController gameController) {
            this.gameController = gameController;
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

        private SceneDataShadow sceneData;
        private List<PlayerDataShadow> playerDataList = new ArrayList<>();
        private String goData;

        public ModelPoolShadow(ModelPool modelPool) {
            sceneData = new SceneDataShadow(modelPool.getSceneData());
            System.out.println("1");
            playerDataList.add(new PlayerDataShadow(modelPool.playerDataList.get(0)));
            System.out.println("2");
            playerDataList.add(new PlayerDataShadow(modelPool.playerDataList.get(1)));
            System.out.println("3");
            goData = JSONUtil.getGson().toJson(modelPool.gameObjects);
            System.out.println("4");
        }

        public ModelPool toModelPool() {
            ModelPool modelPool = new ModelPool();
            modelPool.sceneData = sceneData.toSceneData();
            modelPool.gameObjects = JSONUtil.getGson().fromJson(goData, new TypeToken<List<GameObject>>() {
            }.getType());
            modelPool.playerDataList.add(playerDataList.get(0).toPlayerData());
            modelPool.playerDataList.add(playerDataList.get(1).toPlayerData());
            return modelPool;
        }
    }

    public static class SceneDataShadow implements Serializable {

        private static final long serialVersionUID = 1698549900402484133L;
        private final List<GameAction> log;

        public SceneDataShadow(SceneData sceneData) {
            log = new ArrayList<>(sceneData.log);
        }

        public SceneData toSceneData() {
            SceneData sceneData = new SceneData(null);
            sceneData.log.addAll(log);
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

        public PlayerDataShadow(PlayerData playerData) {
            playerId = playerData.playerId;

            deckCard = new ArrayList<>(playerData.deckCard);
            handCard = new ArrayList<>(playerData.handCard);
            groundCard = new ArrayList<>(playerData.groundCard);
            deadCard = new ArrayList<>(playerData.deadCard);
            burnedCard = new ArrayList<>(playerData.burnedCard);
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

        public PlayerData toPlayerData() {
            PlayerData playerData = new PlayerData();
            playerData.playerId = playerId;

            playerData.deckCard = FXCollections.observableList(deckCard);
            playerData.handCard = FXCollections.observableList(handCard);
            playerData.groundCard = FXCollections.observableList(groundCard);
            playerData.deadCard = FXCollections.observableList(deadCard);
            playerData.burnedCard = FXCollections.observableList(burnedCard);
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
