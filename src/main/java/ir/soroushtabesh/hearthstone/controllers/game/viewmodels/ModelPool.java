package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelPool {
    private final GameController gameController;
    private final SceneData sceneData;
    private final List<PlayerData> playerDataList = new ArrayList<>(2);
    private int idCounter = 2;
    private final List<GameObject> gameObjects = new ArrayList<>();

    public ModelPool(GameController gameController) {
        this.gameController = gameController;
        this.sceneData = new SceneData(gameController);
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
        private final GameController gameController;
        private final ObservableList<GameAction> log = FXCollections.observableArrayList();

        public SceneData(GameController gameController) {
            this.gameController = gameController;
        }

        public ObservableList<GameAction> getLog() {
            return log;
        }
    }

    public static class PlayerData {
        private final int playerId;
        private final GameController gameController;
        //cards
        private final ObservableList<CardObject> deckCard = FXCollections.observableArrayList();
        private final ObservableList<CardObject> handCard = FXCollections.observableArrayList();
        private final ObservableList<MinionObject> groundCard = FXCollections.observableArrayList();
        private final ObservableList<MinionObject> deadCard = FXCollections.observableArrayList();
        private final ObservableList<CardObject> burnedCard = FXCollections.observableArrayList();
        private final ObservableList<Boolean> changeCardFlag = FXCollections.observableArrayList();
        //flags and counters
        private final IntegerProperty mana = new SimpleIntegerProperty();
        private final IntegerProperty manaMax = new SimpleIntegerProperty();
        private final BooleanProperty myTurn = new SimpleBooleanProperty();
        private final BooleanProperty ready = new SimpleBooleanProperty();
        private Hero heroModel;
        private Deck deckModel;
        private InfoPassive infoPassiveModel;
        //objects
        private HeroObject hero;
        private GenericScript infoPassive;

        public PlayerData(int playerId, GameController gameController
                , Hero heroModel, Deck deckModel, InfoPassive infoPassiveModel) {
            this(playerId, gameController, heroModel, deckModel, infoPassiveModel, true);
        }

        public PlayerData(int playerId, GameController gameController
                , Hero heroModel, Deck deckModel, InfoPassive infoPassiveModel, boolean shuffleDeck) {
            this.playerId = playerId;
            this.gameController = gameController;
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
}
