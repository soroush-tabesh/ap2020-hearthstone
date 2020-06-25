package ir.soroushtabesh.hearthstone.controllers.game.viewmodels;

import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ModelPool {
    private final GameController gameController;
    private final SceneData sceneData;
    private final PlayerData playerDataA, playerDataB;

    public ModelPool(GameController gameController) {
        this.gameController = gameController;
        this.sceneData = new SceneData(gameController);
        this.playerDataA = new PlayerData(0, gameController);
        this.playerDataB = new PlayerData(1, gameController);
    }

    public SceneData getSceneData() {
        return sceneData;
    }

    public PlayerData getPlayerData(int id) {
        if (playerDataA.getPlayerId() == id)
            return playerDataA;
        else if (playerDataB.getPlayerId() == id)
            return playerDataB;
        else
            return null;
    }

    public PlayerData[] getAllPlayerData() {
        return new PlayerData[]{playerDataA, playerDataB};
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
        private final ObservableList<CardObject> deckCard = FXCollections.observableArrayList();
        private final ObservableList<CardObject> handCard = FXCollections.observableArrayList();
        private final ObservableList<CardObject> groundCard = FXCollections.observableArrayList();
        private final ObservableList<CardObject> deadCard = FXCollections.observableArrayList();
        private final ObservableList<CardObject> burnedCard = FXCollections.observableArrayList();
        private final ObservableList<CardObject> changeCard = FXCollections.observableArrayList();
        private final IntegerProperty mana = new SimpleIntegerProperty();
        private final IntegerProperty manaMax = new SimpleIntegerProperty();
        private final BooleanProperty myTurn = new SimpleBooleanProperty();
        private final BooleanProperty ready = new SimpleBooleanProperty();
        private Hero heroModel;
        private Deck deckModel;
        private InfoPassive infoPassiveModel;
        private HeroObject hero;
        private GenericScript infoPassive;

        public PlayerData(int playerId, GameController gameController) {
            this.playerId = playerId;
            this.gameController = gameController;
        }

        public int getPlayerId() {
            return playerId;
        }

        public HeroObject getHero() {
            return hero;
        }

        public void setHero(HeroObject hero) {
            this.hero = hero;
        }

        public Deck getDeckModel() {
            return deckModel;
        }

        public Hero getHeroModel() {
            return heroModel;
        }

        public InfoPassive getInfoPassiveModel() {
            return infoPassiveModel;
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

        public ObservableList<CardObject> getGroundCard() {
            return groundCard;
        }

        public ObservableList<CardObject> getDeadCard() {
            return deadCard;
        }

        public ObservableList<CardObject> getBurnedCard() {
            return burnedCard;
        }

        public ObservableList<CardObject> getChangeCard() {
            return changeCard;
        }

        public int getMana() {
            return mana.get();
        }

        public IntegerProperty manaProperty() {
            return mana;
        }

        public int getManaMax() {
            return manaMax.get();
        }

        public IntegerProperty manaMaxProperty() {
            return manaMax;
        }

        public boolean isMyTurn() {
            return myTurn.get();
        }

        public BooleanProperty myTurnProperty() {
            return myTurn;
        }

        public boolean isReady() {
            return ready.get();
        }

        public BooleanProperty readyProperty() {
            return ready;
        }
    }
}
