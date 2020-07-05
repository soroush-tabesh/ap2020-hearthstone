package ir.soroushtabesh.hearthstone.views.gui.controllers;

import animatefx.animation.FadeOut;
import animatefx.animation.Hinge;
import ir.soroushtabesh.hearthstone.controllers.AudioManager;
import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.LocalGameController;
import ir.soroushtabesh.hearthstone.controllers.game.PlayerController;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.*;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.DeckReaderModel;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import ir.soroushtabesh.hearthstone.util.DeckReader;
import ir.soroushtabesh.hearthstone.util.TimerUnit;
import ir.soroushtabesh.hearthstone.util.gui.AnimationPool;
import ir.soroushtabesh.hearthstone.util.gui.AnimationUtil;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.*;
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
    private final Map<HeroObject, HeroView> heroCache = new HashMap<>();

    private GameController gameController;
    private PlayerController pc0, pc1;
    private ModelPool.PlayerData playerData0, playerData1;
    private boolean askTargetMode = false;
    private Function<GameObject, ?> targetFunction;
    private final Map<CardObject, CardView> cardCache = new HashMap<>();
    private final AnimationPool animationPool = new AnimationPool();
    private PlayMode playMode = PlayMode.NORMAL;
    private final TimerUnit timerUnit = new TimerUnit();
    @FXML
    private ImageView boardBgImage;

    @Override
    public void onStart(Object message) {
        super.onStart(message);
        if (message instanceof PlayMode)
            playMode = ((PlayMode) message);
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
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(getPane().getScene().getWindow());
        if (file == null)
            return false;
        DeckReaderModel deckReaderModel = DeckReader.read(file);
        if (deckReaderModel == null)
            return false;
        pc0 = gameController.registerPlayer(
                DeckReader.getHeroByClass(Hero.HeroClass.MAGE.toString()),
                deckReaderModel.getFriendlyDeck(),
                new InfoPassive());
        pc1 = gameController.registerPlayer(
                DeckReader.getHeroByClass(Hero.HeroClass.MAGE.toString()),
                deckReaderModel.getEnemyDeck(),
                new InfoPassive());
        playerData0 = gameController.getModelPool().getPlayerDataById(pc0.getId());
        playerData1 = gameController.getModelPool().getPlayerDataById(pc1.getId());
        return true;
    }

    private boolean initGameControllerDuel() {
        gameController = new LocalGameController();
        PlayerInfoGetter p0 = getPlayerInfo(0);
        PlayerInfoGetter p1 = getPlayerInfo(1);
        if (p1 == null || p0 == null)
            return false;
        pc0 = gameController.registerPlayer(
                p0.getSelectedHero(),
                p0.getSelectedDeck(),
                p0.getSelectedInfoPassive());
        pc1 = gameController.registerPlayer(
                p1.getSelectedHero(),
                p1.getSelectedDeck(),
                p1.getSelectedInfoPassive());
        playerData0 = gameController.getModelPool().getPlayerDataById(pc0.getId());
        playerData1 = gameController.getModelPool().getPlayerDataById(pc1.getId());
        return true;
    }

    private PlayerInfoGetter getPlayerInfo(int playerId) {
        SelectHeroDeckDialog dialog = new SelectHeroDeckDialog(getPane(), playerId);
        Optional<ButtonType> msg = dialog.showAndWait();
        if (msg.isEmpty() || msg.get() == ButtonType.CANCEL) {
            super.backPressed(null);
            return null;
        } else if (msg.get() == ButtonType.APPLY) {
            return null;
        }
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
        initBoardDragDetection();
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
    }

    private void bindBurnedCard() {
        ListChangeListener<? super CardObject> changeListener = c -> {
            while (c.next()) {
                c.getAddedSubList().forEach(cardObject -> {
                    hoverCardStand.getChildren().clear();
                    CardView cardView = getCardView(cardObject);
                    hoverCardStand.getChildren().add(cardView);
                    new Hinge(cardView).play();
                });
            }
        };
        playerData0.getBurnedCard().addListener(changeListener);
        playerData1.getBurnedCard().addListener(changeListener);
    }

    private void bindMana() {
        playerData0.manaProperty().addListener((observable) -> {
            manaLabel0.setText(String.format("%d/%d", playerData0.getMana(), playerData0.getManaMax()));
            AnimationUtil.getPulse(manaLabel0).play();
            AnimationUtil.getTada(manaLabel0).play();
        });
        playerData1.manaProperty().addListener((observable) -> {
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
        FXUtil.BindingUtil.mapContent(groundBox0.getChildren(), playerData0.getGroundCard(), this::getCardViewAnimated);
        FXUtil.BindingUtil.mapContent(groundBox1.getChildren(), playerData1.getGroundCard(), this::getCardViewAnimated);
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
        heroView0.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.LINK);
            event.consume();
        });
        applyDragDropEffect(heroView0);
        heroView0.setOnDragDropped(event -> {
            if (event.getAcceptedTransferMode() != TransferMode.LINK)
                return;
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            if (cardObject instanceof MinionObject) {
                if (cardObject.getPlayerId() == 0)
                    System.out.println(pc0.playMinion((MinionObject) cardObject, heroView0.getHeroObject()));
                else
                    System.out.println(pc1.playMinion((MinionObject) cardObject, heroView0.getHeroObject()));
                event.setDropCompleted(true);
                event.consume();
            } else if (cardObject instanceof WeaponObject) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).useWeapon(heroView0.getHeroObject()));
                event.setDropCompleted(true);
                event.consume();
            }
        });

        heroView1.setOnMouseClicked(event -> {
            if (!askTargetMode)
                return;
            endAskForTarget(heroView1.getHeroObject());
        });
        heroView1.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.LINK);
            event.consume();
        });
        applyDragDropEffect(heroView1);
        heroView1.setOnDragDropped(event -> {
            if (event.getAcceptedTransferMode() != TransferMode.LINK)
                return;
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            if (cardObject instanceof MinionObject) {
                if (cardObject.getPlayerId() == 0)
                    System.out.println(pc0.playMinion((MinionObject) cardObject, heroView1.getHeroObject()));
                else
                    System.out.println(pc1.playMinion((MinionObject) cardObject, heroView1.getHeroObject()));
                event.setDropCompleted(true);
                event.consume();
            } else if (cardObject instanceof WeaponObject) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).useWeapon(heroView1.getHeroObject()));
                event.setDropCompleted(true);
                event.consume();
            }
        });
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
            heroView = HeroView.build(heroObject);
            heroCache.put(heroObject, heroView);
        }
        return heroView;
    }

    private CardView getCardView(CardObject cardObject) {
        if (cardObject == null)
            return null;
        CardView cardView = cardCache.get(cardObject);
        if (cardView == null) {
            cardView = CardView.build(cardObject);
            addDragDetection(cardView);
            cardCache.put(cardObject, cardView);
        }
        return cardView;
    }

    /*
     * minions accept link-drag
     * board accepts copy-drag
     * handBoxes accepts move-drag
     * minions on the ground start link-drag
     * cards in hand start move-drag for minions and copy-drag for others
     * for targeted cards, board halts and asks for another minion
     * then we wait for server return code. showing error in case of failure.
     * */

    private void addDragDetection(CardView cardView) {
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
        cardView.setOnDragDetected(event -> {
            if (askTargetMode)
                return;
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);

            Dragboard dragboard;
            CardObject.CardState cardState = cardView.getCardObject().getCardState();
            if (cardState == CardObject.CardState.HAND)
                dragboard = cardView.startDragAndDrop(TransferMode.COPY);
            else if (cardState == CardObject.CardState.GROUND)
                dragboard = cardView.startDragAndDrop(TransferMode.LINK);
            else
                return;

            ClipboardContent content = new ClipboardContent();
            content.putImage(cardView.snapshot(sp, null));
            content.putString(cardView.getCardObject().getId() + "");

            dragboard.setContent(content);
            event.consume();
        });

        cardView.setOnDragOver(event -> {
            if (cardView.getCardObject().getCardState() == CardObject.CardState.GROUND) {
                event.acceptTransferModes(TransferMode.LINK);
                if (event.getAcceptedTransferMode() == TransferMode.LINK)
                    event.consume();
            }
        });
        applyDragDropEffect(cardView);
        cardView.setOnDragDropped(event -> {
            if (event.getAcceptedTransferMode() != TransferMode.LINK)
                return;
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
        });
    }

    private void addDragDetectionSpell(SpellCardView cardView) {
        cardView.setOnDragDetected(event -> {
            if (askTargetMode)
                return;
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);

            Dragboard dragboard;
            CardObject.CardState cardState = cardView.getCardObject().getCardState();
            if (cardState == CardObject.CardState.HAND)
                dragboard = cardView.startDragAndDrop(TransferMode.MOVE);
            else
                return;

            ClipboardContent content = new ClipboardContent();
            content.putImage(cardView.snapshot(sp, null));
            content.putString(cardView.getCardObject().getId() + "");

            dragboard.setContent(content);
            event.consume();
        });
    }

    private void addDragDetectionHeroPower(HeroPowerCardView cardView) {
        cardView.setOnDragDetected(event -> {
            if (askTargetMode)
                return;
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);

            Dragboard dragboard = cardView.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent content = new ClipboardContent();
            content.putImage(cardView.snapshot(sp, null));
            content.putString(cardView.getCardObject().getId() + "");

            dragboard.setContent(content);
            event.consume();
        });
    }

    private void addDragDetectionWeapon(WeaponCardView cardView) {
        cardView.setOnDragDetected(event -> {
            if (askTargetMode)
                return;
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);

            Dragboard dragboard;
            CardObject.CardState cardState = cardView.getCardObject().getCardState();
            if (cardState == CardObject.CardState.HAND)
                dragboard = cardView.startDragAndDrop(TransferMode.MOVE);
            else
                dragboard = cardView.startDragAndDrop(TransferMode.LINK);

            ClipboardContent content = new ClipboardContent();
            content.putImage(cardView.snapshot(sp, null));
            content.putString(cardView.getCardObject().getId() + "");

            dragboard.setContent(content);
            event.consume();
        });
    }

    private void initBoardDragDetection() {
        dimPane.setOnDragOver(Event::consume);
        handCardBox0.setOnDragOver(Event::consume);
        handCardBox1.setOnDragOver(Event::consume);

        groundBox0.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            if (event.getAcceptedTransferMode() == TransferMode.COPY || event.getAcceptedTransferMode() == TransferMode.MOVE)
                event.consume();
        });
        applyDragDropEffect(groundBox0);
        groundBox0.setOnDragDropped(event -> {
            if (event.getAcceptedTransferMode() != TransferMode.COPY && event.getAcceptedTransferMode() != TransferMode.MOVE)
                return;
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            int index = getGroundPosition(groundBox0, new Point2D(event.getSceneX(), event.getSceneY()));
            if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, index, null));
            } else {
                startAskForTarget((target) ->
                        (cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, index, target)
                );
            }
            event.setDropCompleted(true);
            event.consume();
        });


        groundBox1.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            if (event.getAcceptedTransferMode() == TransferMode.COPY || event.getAcceptedTransferMode() == TransferMode.MOVE)
                event.consume();
        });
        applyDragDropEffect(groundBox1);
        groundBox1.setOnDragDropped(event -> {
            if (event.getAcceptedTransferMode() != TransferMode.COPY && event.getAcceptedTransferMode() != TransferMode.MOVE)
                return;
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            int index = getGroundPosition(groundBox1, new Point2D(event.getSceneX(), event.getSceneY()));
            if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, index, null));
            } else {
                startAskForTarget((target) ->
                        (cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, index, target)
                );
            }
            event.setDropCompleted(true);
            event.consume();
        });

        globalDragReceiver.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
        applyDragDropEffect(globalDragReceiver);
        globalDragReceiver.setOnDragDropped(event -> {
            if (event.getAcceptedTransferMode() != TransferMode.MOVE)
                return;
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            if (cardObject.getCardModel().getActionType() == Card.ActionType.GLOBAL) {
                System.out.println((cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, 0, null));
            } else {
                startAskForTarget((target) ->
                        (cardObject.getPlayerId() == 0 ? pc0 : pc1).playCard(cardObject, 0, target)
                );
            }
            event.setDropCompleted(true);
            event.consume();
        });

        changeStand.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            if (event.getAcceptedTransferMode() == TransferMode.COPY || event.getAcceptedTransferMode() == TransferMode.MOVE)
                event.consume();
        });
        applyDragDropEffect(changeStand);
        changeStand.setOnDragDropped(event -> {
            if (event.getAcceptedTransferMode() != TransferMode.COPY && event.getAcceptedTransferMode() != TransferMode.MOVE)
                return;
            CardObject cardObject = (CardObject) gameController.getModelPool()
                    .getGameObjectById(Integer.parseInt(event.getDragboard().getString()));
            GameController.Message message = (cardObject.getPlayerId() == 0 ? pc0 : pc1)
                    .changeCard((cardObject.getPlayerId() == 0 ? playerData0 : playerData1)
                            .getHandCard().indexOf(cardObject));
            System.out.println(message);
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private int getGroundPosition(HBox groundBox, Point2D pos) {
        if (groundBox.getChildren().isEmpty())
            return 0;
        List<Point2D> anchors = new ArrayList<>();
        groundBox.getChildren().forEach(node -> {
            Bounds bounds = node.getParent().localToScene(node.getBoundsInParent());
            anchors.add(new Point2D(bounds.getMinX(), 0));
        });
        Bounds bounds = groundBox.localToScene(groundBox.getChildren().get(0).getBoundsInParent());
        anchors.add(new Point2D(bounds.getMaxX(), 0));
        Point2D point2D = anchors.stream().min((o1, o2) -> (int) (o1.distance(pos) - o2.distance(pos))).get();
        return anchors.indexOf(point2D);
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

    private void applyDragDropEffect(Node node) {
        node.setOnDragEntered(event -> applyDragEnterEffect((Node) event.getSource(), event));
        node.setOnDragExited(event -> applyDragExitEffect((Node) event.getSource(), event));
    }

    private void applyDragEnterEffect(Node node, DragEvent event) {
        if (event.isAccepted())
            animationPool.startAnimation(node, AnimationUtil.getDragHover(node));
    }

    private void applyDragExitEffect(Node node, DragEvent event) {
        animationPool.stopAnimation(node);
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

    private void unbindUI() {
        //todo
        animationPool.clearAll();
        timerUnit.stop();
    }

    @Override
    protected void backPressed(ActionEvent event) {
        Optional<ButtonType> res = FXUtil.showAlert("Game", "Exit"
                , "Are you sure you want to leave the board?", Alert.AlertType.CONFIRMATION);
        if (res.isPresent() && res.get().equals(ButtonType.OK))
            super.backPressed(event);
    }

    public void endTurnButton(ActionEvent event) {
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
