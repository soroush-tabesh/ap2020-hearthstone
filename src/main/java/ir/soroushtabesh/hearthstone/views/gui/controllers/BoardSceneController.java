package ir.soroushtabesh.hearthstone.views.gui.controllers;

import animatefx.animation.FadeOut;
import animatefx.animation.Hinge;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.PlayerController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Message;
import ir.soroushtabesh.hearthstone.network.RemoteGameController;
import ir.soroushtabesh.hearthstone.util.TimerUnit;
import ir.soroushtabesh.hearthstone.util.gui.AnimationPool;
import ir.soroushtabesh.hearthstone.util.gui.AnimationUtil;
import ir.soroushtabesh.hearthstone.util.gui.DnDHelper;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public class BoardSceneController extends AbstractSceneController {
    @FXML
    private Label timerLabel;
    @FXML
    private Region globalDragReceiver;
    @FXML
    private AnchorPane dimPane;
    @FXML
    private Button readyButton0;
    @FXML
    private Button readyButton1;
    @FXML
    private AnchorPane changeStand;
    @FXML
    private StackPane hoverCardStand;
    @FXML
    private Label manaLabel0;
    @FXML
    private Label deckRemLabel0;
    @FXML
    private StackPane heroStand0;
    @FXML
    private HBox handCardBox0;
    @FXML
    private HBox groundBox0;
    @FXML
    private StackPane weaponStand0;
    @FXML
    private StackPane heroPowerStand0;
    @FXML
    private Label manaLabel1;
    @FXML
    private Label deckRemLabel1;
    @FXML
    private StackPane heroStand1;
    @FXML
    private HBox handCardBox1;
    @FXML
    private HBox groundBox1;
    @FXML
    private StackPane weaponStand1;
    @FXML
    private StackPane heroPowerStand1;
    @FXML
    private VBox logBox;

    private final DnDHelper dnDHelper;
    private final AnimationPool animationPool;
    private final Map<HeroObject, HeroView> heroCache = new HashMap<>();
    private final Map<CardObject, CardView> cardCache = new HashMap<>();
    private final TimerUnit timerUnit = new TimerUnit();
    private GameController gameController;
    private PlayerController pc0, pc1;
    private ModelPool.PlayerData playerData0, playerData1;
    private boolean askTargetMode = false;
    private Function<GameObject, ?> targetFunction;

    @FXML
    private ImageView boardBgImage;

    {
        animationPool = new AnimationPool();
        dnDHelper = new DnDHelper(animationPool);
    }

    private PlayMode playMode = PlayMode.NORMAL;

    @Override
    public void onStart(Object message) {
        super.onStart(message);
        if (message instanceof PlayMode)
            playMode = ((PlayMode) message);
        //todo show mode chooser dialog
        initUI();
        switch (playMode) {
            case AI:
                if (!initGameControllerAI()) {
                    super.backPressed(null);
                    return;
                }
                break;
            case DECK_READER:
                if (!initGameControllerDeckReader()) {
                    super.backPressed(null);
                    return;
                }
                break;
            case NORMAL:
                if (!initGameControllerDuel()) {
                    super.backPressed(null);
                    return;
                }
        }
        bindUI();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }

    private boolean initGameControllerAI() {
        //todo AI
        return false;
    }

    private boolean initGameControllerDeckReader() {
//        FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showOpenDialog(getPane().getScene().getWindow());
//        if (file == null)
//            return false;
//        DeckReaderModel deckReaderModel = DeckReader.read(file);
//        if (deckReaderModel == null)
//            return false;
//        gameController = new LocalGameController();
//        pc0 = gameController.registerPlayer(
//                CardManager.getInstance().getHeroByClass(Hero.HeroClass.MAGE),
//                deckReaderModel.getFriendlyDeck(),
//                new InfoPassive(), deckReaderModel.getFriendlyDeckList());
//        pc1 = gameController.registerPlayer(
//                CardManager.getInstance().getHeroByClass(Hero.HeroClass.MAGE),
//                deckReaderModel.getEnemyDeck(),
//                new InfoPassive(), deckReaderModel.getEnemyDeckList());
//        playerData0 = gameController.getModelPool().getPlayerDataById(pc0.getId());
//        playerData1 = gameController.getModelPool().getPlayerDataById(pc1.getId());
        return false;
    }

    private boolean initGameControllerDuel() {
//        gameController = new LocalGameController();
//        PlayerInfoGetter p0 = getPlayerInfo(0);
//        if (p0 == null) {
//            super.backPressed(null);
//            return false;
//        }
//        PlayerInfoGetter p1 = getPlayerInfo(1);
//        if (p1 == null) {
//            super.backPressed(null);
//            return false;
//        }
//        pc0 = gameController.registerPlayer(
//                p0.getSelectedHero(),
//                p0.getSelectedDeck(),
//                p0.getSelectedInfoPassive(), true);
//        pc1 = gameController.registerPlayer(
//                p1.getSelectedHero(),
//                p1.getSelectedDeck(),
//                p1.getSelectedInfoPassive(), true);
//        playerData0 = gameController.getModelPool().getPlayerDataById(pc0.getId());
//        playerData1 = gameController.getModelPool().getPlayerDataById(pc1.getId());
        gameController = new RemoteGameController();

        PlayerInfoGetter info = getPlayerInfo();
        if (info == null)
            return false;

        PlayerController pcTemp = gameController.registerPlayer(
                PlayerManager.getInstance().getPlayer(),
                info.getSelectedHero(),
                info.getSelectedDeck(), info.getSelectedInfoPassive(), true);

        BooleanProperty gameReady = gameController.getModelPool().getSceneData().gameReadyProperty();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game");
        alert.setHeaderText("Waiting for a match...");
        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        Runnable afterReady = () -> {
            if (pcTemp.getPlayerId() == 0) {
                pc0 = pcTemp;
                pc1 = new PlayerController.DummyPlayerController(1);
            } else {
                pc0 = new PlayerController.DummyPlayerController(0);
                pc1 = pcTemp;
            }
            playerData0 = gameController.getModelPool().getPlayerDataById(pc0.getPlayerId());
            System.out.println("pd0: " + playerData0 + " " + pc0.getPlayerId());
            playerData1 = gameController.getModelPool().getPlayerDataById(pc1.getPlayerId());
            System.out.println("pd1: " + playerData1 + " " + pc1.getPlayerId());
            Platform.runLater(() -> {
                alert.setResult(ButtonType.OK);
                alert.close();
            });
        };

        if (gameReady.get()) {
            afterReady.run();
            return true;
        } else {
            gameReady.addListener(observable -> afterReady.run());
            Optional<ButtonType> msg = alert.showAndWait();
            return msg.isPresent() && msg.get() != ButtonType.CANCEL;
        }
    }

    private PlayerInfoGetter getPlayerInfo() {
        SelectHeroDeckDialog dialog = new SelectHeroDeckDialog(getPane());
        Optional<ButtonType> msg = dialog.showAndWait();
        if (msg.isEmpty() || msg.get() == ButtonType.CANCEL)
            return null;
        else if (msg.get() == ButtonType.APPLY)
            return null;
        return dialog;
    }

    private PlayerInfoGetter getPlayerInfo(int playerId) {
        SelectHeroDeckDialog dialog = new SelectHeroDeckDialog(getPane(), playerId);
        Optional<ButtonType> msg = dialog.showAndWait();
        if (msg.isEmpty() || msg.get() == ButtonType.CANCEL)
            return null;
        else if (msg.get() == ButtonType.APPLY)
            return null;
        return dialog;
    }

    public enum PlayMode {
        NORMAL, DECK_READER, AI
    }

    private void initUI() {
        dimPane.setVisible(true);
        dimPane.setOpacity(1);
        readyButton0.setDisable(false);
        readyButton1.setDisable(false);
        hoverCardStand.getChildren().clear();
        manaLabel0.setText("0/0");
        manaLabel1.setText("0/0");
        deckRemLabel0.setText("0");
        deckRemLabel1.setText("0");
        heroStand0.getChildren().clear();
        heroStand1.getChildren().clear();
        handCardBox0.getChildren().clear();
        handCardBox1.getChildren().clear();
        groundBox0.getChildren().clear();
        groundBox1.getChildren().clear();
        weaponStand0.getChildren().clear();
        weaponStand1.getChildren().clear();
        heroPowerStand0.getChildren().clear();
        heroPowerStand1.getChildren().clear();
        logBox.getChildren().clear();
    }

    private void bindUI() {
        bindHeroStands();
        bindHands();
        bindDeckRemLabels();
        bindWeapons();
        bindHeroPowers();
        bindGroundCards();
        bindLogs();
        bindMana();
        bindBurnedCard();
        bindDimPane();
        bindTimer();
        bindCommonAnimation();
        bindWinner();
        initBoardDragDetection();
    }

    private void bindWinner() {
        gameController.winnerProperty().addListener((observable, oldValue, newValue) ->
                FXUtil.showAlertInfo("GameBoard", "Game Over"
                        , "Player" + newValue + " won the game"));
    }

    private void unbindUI() {
        animationPool.clearAll();
        timerUnit.stop();
        cardCache.clear();
        heroCache.clear();
        gameController = null;
        pc0 = null;
        pc1 = null;
        playerData0 = null;
        playerData1 = null;
        askTargetMode = false;
        targetFunction = null;
        deckRemLabel0.textProperty().unbind();
        deckRemLabel1.textProperty().unbind();
        readyButton0.disableProperty().unbind();
        readyButton1.disableProperty().unbind();
        timerLabel.textProperty().unbind();
    }

    private void showSpell(CardObject cardObject) {
        CardView cardView = getCardView(cardObject);
        hoverCardStand.getChildren().clear();
        hoverCardStand.getChildren().add(cardView);
        cardView.setOpacity(0);
        AnimationUtil.getSpellAppearance(cardView).play();
    }

    private void bindCommonAnimation() {
        gameController.getModelPool().getSceneData().getLog().addListener((ListChangeListener<GameAction>) c -> {
            c.next();
            c.getAddedSubList().forEach(gameAction -> {
                if (gameAction instanceof GameAction.MinionAttack) {
                    GameAction.MinionAttack minionAttack = (GameAction.MinionAttack) gameAction;
                    AnimationUtil.getAttackAnimation(getView(minionAttack.getSource())).play();
                    AnimationUtil.getAttackAnimation(getView(minionAttack.getTarget())).play();
                } else if (gameAction instanceof GameAction.TargetedAttack) {
                    GameAction.TargetedAttack targetedAttack = (GameAction.TargetedAttack) gameAction;
                    AnimationUtil.getAttackAnimation(getView(targetedAttack.getTarget())).play();
                    if (targetedAttack.getSource() instanceof WeaponObject) {
                        AnimationUtil.getAttackAnimation(getView(targetedAttack.getSource())).play();
                    }
                } else if (gameAction instanceof GameAction.SpellGlobal) {
                    showSpell(((GameAction.SpellGlobal) gameAction).getSource());
                }
            });
        });
    }

    private void bindTimer() {
        gameController.turnProperty().addListener((observable, oldValue, newValue) -> {
            timerUnit.startTimer(60, 15);
            timerLabel.setTextFill(Color.GAINSBORO);
        });
        timerLabel.textProperty().bind(Bindings
                .createStringBinding(() ->
                                String.format("%d\"", timerUnit.getRemTime())
                        , timerUnit.remTimeProperty()));
        timerUnit.setOnTimerEnd(() -> {
            timerLabel.setTextFill(Color.GAINSBORO);
            endTurnButton(null);
        });
        timerUnit.setOnTimerPreEnd(() -> {
            AudioManager.getInstance().playAlarm();
            timerLabel.setTextFill(Color.ORANGERED);
        });
    }

    private void bindDimPane() {
        gameController.startedProperty().addListener((observable, oldValue, newValue) -> {
            FadeOut fadeOut = new FadeOut(dimPane);
            fadeOut.setOnFinished((event) -> dimPane.setVisible(!newValue));
            fadeOut.play();
        });
        readyButton0.disableProperty().bind(playerData0.readyProperty());
        readyButton1.disableProperty().bind(playerData1.readyProperty());
        readyButton0.setVisible(!(pc0 instanceof PlayerController.DummyPlayerController));
        readyButton1.setVisible(!(pc1 instanceof PlayerController.DummyPlayerController));
    }

    private void bindBurnedCard() {
        ListChangeListener<? super CardObject> changeListener = c -> {
            c.next();
            c.getAddedSubList().forEach(cardObject -> {
                hoverCardStand.getChildren().clear();
                CardView cardView = getCardView(cardObject);
                hoverCardStand.getChildren().add(cardView);
                new Hinge(cardView).play();
            });
        };
        playerData0.getBurnedCard().addListener(changeListener);
        playerData1.getBurnedCard().addListener(changeListener);
    }

    private void bindMana() {
        playerData0.manaMaxProperty().addListener((observable) -> {
            manaLabel0.setText(String.format("%d/%d", playerData0.getMana(), playerData0.getManaMax()));
            AnimationUtil.getPulse(manaLabel0).play();
            AnimationUtil.getTada(manaLabel0).play();
        });
        playerData1.manaMaxProperty().addListener((observable) -> {
            manaLabel1.setText(String.format("%d/%d", playerData1.getMana(), playerData1.getManaMax()));
            AnimationUtil.getPulse(manaLabel1).play();
            AnimationUtil.getTada(manaLabel1).play();
        });
    }

    private void bindLogs() {
        FXUtil.BindingUtil.mapContent(logBox.getChildren()
                , gameController.getModelPool().getSceneData().getLog(), this::getLogLabel);
    }

    private Label getLogLabel(GameAction gameAction) {
        Label label = new Label(gameAction.getMessage());
        label.getStyleClass().add("label_log");
        return label;
    }

    private void bindGroundCards() {
        FXUtil.BindingUtil.mapContent(groundBox0.getChildren(), playerData0.getGroundCard(), this::getCardViewAnimated, this::getCardView);
        FXUtil.BindingUtil.mapContent(groundBox1.getChildren(), playerData1.getGroundCard(), this::getCardViewAnimated, this::getCardView);
    }

    private CardView getCardViewAnimated(CardObject cardObject) {
        CardView cardView = getCardView(cardObject);
        AnimationUtil.getMinionAppearance(cardView).play();
        return cardView;
    }

    private void bindHeroPowers() {
        if (playerData0.getHero().getHeroPower() != null)
            heroPowerStand0.getChildren().add(getCardView(playerData0.getHero().getHeroPower()));
        if (playerData1.getHero().getHeroPower() != null)
            heroPowerStand1.getChildren().add(getCardView(playerData1.getHero().getHeroPower()));
    }

    private void bindWeapons() {
        ChangeListener<WeaponObject> changeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                CardView cardView = getCardView(newValue);
                (newValue.getPlayerId() == 0 ? weaponStand0 : weaponStand1).getChildren().clear();
                (newValue.getPlayerId() == 0 ? weaponStand0 : weaponStand1).getChildren().add(cardView);
                AnimationUtil.getWeaponAppearance(cardView).play();
            } else {
                (oldValue.getPlayerId() == 0 ? weaponStand0 : weaponStand1).getChildren().clear();
            }
        };
        playerData0.getHero().currentWeaponProperty().addListener(changeListener);
        playerData1.getHero().currentWeaponProperty().addListener(changeListener);
    }

    private void bindDeckRemLabels() {
        deckRemLabel0.textProperty().bind(
                Bindings.createStringBinding(() -> playerData0.getDeckCard().size() + "", playerData0.getDeckCard()));
        deckRemLabel1.textProperty().bind(
                Bindings.createStringBinding(() -> playerData1.getDeckCard().size() + "", playerData1.getDeckCard()));
    }

    private void bindHands() {
        FXUtil.BindingUtil.mapContent(handCardBox0.getChildren(), playerData0.getHandCard(), this::getCardView);
        FXUtil.BindingUtil.mapContent(handCardBox1.getChildren(), playerData1.getHandCard(), this::getCardView);
    }

    private void bindHeroStands() {
        HeroView heroView0 = getHeroView(playerData0.getHero());
        heroStand0.getChildren().add(heroView0);
        HeroView heroView1 = getHeroView(playerData1.getHero());
        heroStand1.getChildren().add(heroView1);

        heroView0.setOnMouseClicked(event -> {
            if (!askTargetMode)
                return;
            endAskForTarget(heroView0.getHeroObject());
        });
        dnDHelper.addDropDetector(heroView0, getHeroViewDnDHandler(heroView0), TransferMode.LINK);

        heroView1.setOnMouseClicked(event -> {
            if (!askTargetMode)
                return;
            endAskForTarget(heroView1.getHeroObject());
        });
        dnDHelper.addDropDetector(heroView1, getHeroViewDnDHandler(heroView1), TransferMode.LINK);
    }

    private Function<DragEvent, Boolean> getHeroViewDnDHandler(HeroView heroView) {
        return event -> {
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            if (cardObject instanceof MinionObject) {
                if (cardObject.getPlayerId() == 0)
                    System.out.println(pc0.playMinion((MinionObject) cardObject, heroView.getHeroObject()));
                else
                    System.out.println(pc1.playMinion((MinionObject) cardObject, heroView.getHeroObject()));
                return true;
            } else if (cardObject instanceof WeaponObject) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).useWeapon(heroView.getHeroObject()));
                return true;
            }
            return false;
        };
    }

    private Node getView(GameObject gameObject) {
        if (gameObject instanceof CardObject)
            return getCardView((CardObject) gameObject);
        else if (gameObject instanceof HeroObject)
            return getHeroView(((HeroObject) gameObject));
        return null;
    }

    private HeroView getHeroView(HeroObject heroObject) {
        if (heroObject == null)
            return null;
        HeroView heroView = heroCache.get(heroObject);
        if (heroView == null) {
            heroView = HeroView.build(heroObject, gameController);
            heroCache.put(heroObject, heroView);
        }
        return heroView;
    }

    private CardView getCardView(CardObject cardObject) {
        if (cardObject == null)
            return null;
        CardView cardView = cardCache.get(cardObject);
        if (cardView == null) {
            cardView = CardView.build(cardObject, gameController);
            addCardDragDetection(cardView);
            cardCache.put(cardObject, cardView);
        }
        return cardView;
    }

    private void addCardDragDetection(CardView cardView) {
        if (cardView instanceof MinionCardView) {
            addDragDetectionMinion((MinionCardView) cardView);
        } else if (cardView instanceof SpellCardView) {
            addDragDetectionSpell((SpellCardView) cardView);
        } else if (cardView instanceof HeroPowerCardView) {
            addDragDetectionHeroPower((HeroPowerCardView) cardView);
        } else if (cardView instanceof WeaponCardView) {
            addDragDetectionWeapon((WeaponCardView) cardView);
        }
    }

    private void addDragDetectionMinion(MinionCardView cardView) {
        cardView.setOnMouseClicked(event -> {
            if (!askTargetMode || cardView.getCardObject().getCardState() != CardObject.CardState.GROUND)
                return;
            endAskForTarget(cardView.getCardObject());
        });

        dnDHelper.addCardDragDetector(cardView, cardView.getCardObject().getId(), () -> {
            if (askTargetMode)
                return null;
            CardObject.CardState cardState = cardView.getCardObject().getCardState();
            if (cardState == CardObject.CardState.HAND)
                return TransferMode.COPY;
            else if (cardState == CardObject.CardState.GROUND)
                return TransferMode.LINK;
            return null;
        });

        dnDHelper.addDropDetector(cardView, event -> {
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            if (cardObject instanceof MinionObject) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).playMinion((MinionObject) cardObject, cardView.getCardObject()));
                event.setDropCompleted(true);
                event.consume();
            } else if (cardObject instanceof WeaponObject) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).useWeapon(cardView.getCardObject()));
                event.setDropCompleted(true);
                event.consume();
            }
            return true;
        }, TransferMode.LINK);

    }

    private void addDragDetectionSpell(SpellCardView cardView) {
        dnDHelper.addCardDragDetector(cardView, cardView.getCardObject().getId(), () -> {
            if (askTargetMode)
                return null;
            if (cardView.getCardObject().getCardState() == CardObject.CardState.HAND)
                return TransferMode.MOVE;
            else
                return null;
        });
    }

    private void addDragDetectionHeroPower(HeroPowerCardView cardView) {
        dnDHelper.addCardDragDetector(cardView, cardView.getCardObject().getId(), () -> {
            if (askTargetMode)
                return null;
            return TransferMode.MOVE;
        });
    }

    private void addDragDetectionWeapon(WeaponCardView cardView) {
        dnDHelper.addCardDragDetector(cardView, cardView.getCardObject().getId(), () -> {
            System.err.println("BoardSceneController.addDragDetectionWeapon");
            if (askTargetMode)
                return null;
            CardObject.CardState cardState = cardView.getCardObject().getCardState();
            if (cardState == CardObject.CardState.HAND)
                return TransferMode.MOVE;
            else
                return TransferMode.LINK;
        });
    }

    private void initBoardDragDetection() {
        dimPane.setOnDragOver(Event::consume);
        handCardBox0.setOnDragOver(Event::consume);
        handCardBox1.setOnDragOver(Event::consume);

        dnDHelper.addDropDetector(groundBox0, getGroundBoxDnDHandler(groundBox0), TransferMode.COPY_OR_MOVE);
        dnDHelper.addDropDetector(groundBox1, getGroundBoxDnDHandler(groundBox1), TransferMode.COPY_OR_MOVE);

        dnDHelper.addDropDetector(globalDragReceiver, event -> {
            System.err.println("BoardSceneController.initBoardDragDetection1");
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, 0, null));
            } else {
                startAskForTarget((target) ->
                        (cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, 0, target)
                );
            }
            return true;
        }, TransferMode.MOVE);

        dnDHelper.addDropDetector(changeStand, (event) -> {
            System.err.println("BoardSceneController.initBoardDragDetection2");
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            Message message = (cardObject.getPlayerId() == 0 ? pc0 : pc1)
                    .changeCard((cardObject.getPlayerId() == 0 ? playerData0 : playerData1)
                            .getHandCard().indexOf(cardObject));
            System.out.println(message);
            return true;
        }, TransferMode.COPY_OR_MOVE);
    }

    private Function<DragEvent, Boolean> getGroundBoxDnDHandler(HBox groundBox) {
        return event -> {
            System.err.println("BoardSceneController.getGroundBoxDnDHandler");
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            int index = FXUtil.getNearestGap(groundBox, new Point2D(event.getSceneX(), event.getSceneY()));
            if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, index, null));
            } else {
                startAskForTarget((target) ->
                        (cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, index, target));
            }
            return true;
        };
    }

    private void startAskForTarget(Function<GameObject, ?> function) {
        askTargetMode = true;
        targetFunction = function;
        ColorAdjust desaturate = new ColorAdjust();
        desaturate.setSaturation(-0.8);
        boardBgImage.setEffect(desaturate);
    }

    private void interruptAskForTarget() {
        askTargetMode = false;
        boardBgImage.setEffect(null);
    }

    private void endAskForTarget(GameObject gameObject) {
        askTargetMode = false;
        boardBgImage.setEffect(null);
        System.out.println(targetFunction.apply(gameObject));
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindUI();
        initUI();
        cardCache.clear();
        gameController = null;
        pc0 = null;
        pc1 = null;
    }

    @Override
    protected void backPressed(ActionEvent event) {
        Optional<ButtonType> res = FXUtil.showAlert("Game", "Exit"
                , "Are you sure you want to leave the board?", Alert.AlertType.CONFIRMATION);
        if (res.isPresent() && res.get().equals(ButtonType.OK))
            super.backPressed(event);
    }

    public void endTurnButton(ActionEvent event) {
        AudioManager.getInstance().stopAlarm();
        interruptAskForTarget();
        if (gameController.getTurn() == 0)
            pc0.endTurn();
        else
            pc1.endTurn();
    }

    public void readyButton0(ActionEvent event) {
        pc0.startGame();
    }

    public void readyButton1(ActionEvent event) {
        pc1.startGame();
    }
}
