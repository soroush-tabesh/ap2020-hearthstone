package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.DeckManager;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.*;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.ShopScene;
import ir.soroushtabesh.hearthstone.views.gui.controls.CardTextView;
import ir.soroushtabesh.hearthstone.views.gui.controls.CardView;
import ir.soroushtabesh.hearthstone.views.gui.controls.EditDeckDialog;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CollectionSceneController extends AbstractSceneController {

    @FXML
    private RadioButton toggleAll;
    @FXML
    private RadioButton toggleOwned;
    @FXML
    private RadioButton toggleLocked;
    @FXML
    private TilePane tilePane;
    @FXML
    private Accordion decksAccordion;
    @FXML
    private Button newDeckButton;
    @FXML
    private TextField searchBox;
    @FXML
    private ComboBox<Hero.HeroClass> categoryChoice;
    @FXML
    private ChoiceBox<Integer> manaChoice;
    @FXML
    private ToggleGroup possession;

    private ObjectProperty<Predicate<CardView>> manaFilter, possessionFilter, categoryFilter, nameFilter;
    private FilteredList<CardView> filteredItems;
    private CardView selectedCardView = null;
    private ContextMenu cardContextMenu;
    private ContextMenu cardTextContextMenu;
    private ContextMenu deckContextMenu;
    private TitledPane paneContextTemp;
    private CardTextView cardTextContextTemp;
    private ObservableList<BriefDeck> decks;
    private Object message;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initViews();
        initFilters();
        initContextMenu();
    }

    @Override
    public void onStart(Object message) {
        super.onStart(message);
        this.message = message;
        clearFilters(null);
        new Thread(() -> {
            ObservableList<CardView> list = FXCollections.observableList(CardView
                    .buildAll(CardManager.getInstance().getAllCards()));
            FXUtil.runLater(() -> {
                bindTilePaneToCards(list);
                bindDeckView();
                list.forEach(cardView -> prepareCardView(message, cardView));
            }, 0);
        }).start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Bindings.unbindContent(tilePane.getChildren(), filteredItems);
        clearFilters(null);
    }

    private void initViews() {
        categoryChoice.getItems().addAll(Hero.HeroClass.values());
        manaChoice.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    private void initFilters() {
        initNameFilter();
        initCategoryFilter();
        initManaFilter();
        initPossessionFilter();
    }

    private void initNameFilter() {
        nameFilter = new SimpleObjectProperty<>();
        nameFilter.bind(Bindings.createObjectBinding(() ->
                        cardView -> cardView.getBriefCard().getName().toLowerCase()
                                .contains(searchBox.getText().toLowerCase()),
                searchBox.textProperty()));
    }

    private void initCategoryFilter() {
        categoryFilter = new SimpleObjectProperty<>();
        categoryFilter.bind(Bindings.createObjectBinding(() ->
                        cardView -> categoryChoice.getValue() == null
                                || categoryChoice.getValue() == cardView.getBriefCard().getCard().getHeroClass(),
                categoryChoice.valueProperty()));
    }

    private void initManaFilter() {
        manaFilter = new SimpleObjectProperty<>();
        manaFilter.bind(Bindings.createObjectBinding(() ->
                        cardView -> manaChoice.getValue() == null
                                || manaChoice.getValue() == cardView.getBriefCard().getMana(),
                manaChoice.valueProperty()));
    }

    private void initPossessionFilter() {
        possessionFilter = new SimpleObjectProperty<>();
        possessionFilter.bind(Bindings.createObjectBinding(() ->
                        cardView -> possession.getSelectedToggle() == null
                                || possession.getSelectedToggle() == toggleAll
                                || (possession.getSelectedToggle() == toggleOwned
                                && cardView.getBriefCard().getAmount() != 0)
                                || (possession.getSelectedToggle() == toggleLocked
                                && cardView.getBriefCard().getAmount() == 0),
                possession.selectedToggleProperty()));
    }

    private void initContextMenu() {
        cardContextMenu = generateCardContextMenu();
        cardTextContextMenu = generateCardTextContextMenu();
        deckContextMenu = generateDeckContextMenu();
    }

    private ContextMenu generateCardContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem shopItem = new MenuItem("Shop");
        MenuItem addToDeckItem = new MenuItem("Add to deck");

        shopItem.setOnAction(event -> SceneManager.getInstance()
                .showScene(ShopScene.class, selectedCardView.getBriefCard().getCard()));
        addToDeckItem.setOnAction(event -> {
            TitledPane selectedPane = decksAccordion.getExpandedPane();
            if (selectedPane == null) {
                FXUtil.showAlert("Collection", "Add to deck"
                        , "Select a deck first.", Alert.AlertType.ERROR);
            } else {
                Deck selectedDeck = ((BriefDeck) selectedPane.getProperties().get("deck")).getDeck();
                Card selectedCard = this.selectedCardView.getBriefCard().getCard();
                Message res = DeckManager.getInstance().addCardToDeck(selectedCard, selectedDeck
                        , PlayerManager.getInstance().getToken());
                switch (res) {
                    case SUCCESS:
                        updateSelectedDeckView();
                        break;
                    case FULL:
                        FXUtil.showAlert("Collection", "Add to deck"
                                , "You can't add more. Deck is full", Alert.AlertType.ERROR);
                        break;
                    case INSUFFICIENT:
                        FXUtil.showAlert("Collection", "Add to deck"
                                , "You can't add more of this kind.", Alert.AlertType.ERROR);
                        break;
                    case INCOMPATIBLE:
                        FXUtil.showAlert("Collection", "Add to deck"
                                , "Can't add. Incompatible hero class", Alert.AlertType.ERROR);
                }
            }
        });

        contextMenu.getItems().addAll(shopItem, addToDeckItem);
        return contextMenu;
    }

    private ContextMenu generateCardTextContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeFromDeck = new MenuItem("Remove Card");
        removeFromDeck.setOnAction(event -> {
            TitledPane selectedPane = decksAccordion.getExpandedPane();
            Deck selectedDeck = ((BriefDeck) selectedPane.getProperties().get("deck")).getDeck();
            Card selectedCard = cardTextContextTemp.getBriefCard().getCard();
            boolean res = DeckManager.getInstance().removeCardFromDeck(selectedCard, selectedDeck
                    , PlayerManager.getInstance().getToken());
            if (res) {
                updateSelectedDeckView();
            } else {
                FXUtil.showAlert("Collection", "Add to deck"
                        , "You can't add more.", Alert.AlertType.ERROR);
            }
        });
        contextMenu.getItems().add(removeFromDeck);
        return contextMenu;
    }

    public ContextMenu generateDeckContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeDeck = new MenuItem("Remove Deck");
        MenuItem editDeck = new MenuItem("Edit Deck");
        contextMenu.getItems().addAll(editDeck, removeDeck);
        removeDeck.setOnAction(event -> {
            TitledPane selectedPane = paneContextTemp;
            Deck selectedDeck = ((BriefDeck) selectedPane.getProperties().get("deck")).getDeck();
            Optional<ButtonType> res = FXUtil.showAlert("Collection", "Remove Deck"
                    , "Are you sure you want to remove this deck?", Alert.AlertType.CONFIRMATION);
            if (res.isPresent() && res.get().equals(ButtonType.OK)) {
                DeckManager.getInstance().removeDeck(selectedDeck, PlayerManager.getInstance().getToken());
                decksAccordion.getPanes().remove(selectedPane);
            }
        });
        editDeck.setOnAction(event -> {
            TitledPane selectedPane = paneContextTemp;
            Deck selectedDeck = ((BriefDeck) selectedPane.getProperties().get("deck")).getDeck();
            openEditDeck(selectedDeck);
            updateDeckView(selectedPane);
        });
        return contextMenu;
    }

    private Deck openAddDeck() {
        Deck deck = new Deck();
        EditDeckDialog editDeckDialog = new EditDeckDialog(getPane(), "Collection"
                , "Add Deck", deck);
        Optional<ButtonType> res = editDeckDialog.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            editDeckDialog.updateDeck();
            DeckManager.getInstance().saveNewDeck(deck, PlayerManager.getInstance().getToken());
            return deck;
        }
        return null;
    }

    private void openEditDeck(Deck deck) {
        EditDeckDialog editDeckDialog = new EditDeckDialog(getPane(), "Collection"
                , "Edit Deck: " + deck.getName(), deck);
        Optional<ButtonType> res = editDeckDialog.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            editDeckDialog.updateDeck();
            DeckManager.getInstance().updateDeckProperties(deck, PlayerManager.getInstance().getToken());
        }
    }

    private void updateDeckView(TitledPane target) {
        if (target == null)
            return;
        BriefDeck deck = (BriefDeck) target.getProperties().get("deck");
        deck.refresh();
        generateDeckTitlePane(deck, target);
    }

    private void updateSelectedDeckView() {
        updateDeckView(decksAccordion.getExpandedPane());
    }

    private void bindDeckView() {
        generateBriefDecks();
        FXUtil.runLater(() -> {
            decksAccordion.getPanes().clear();
            decks.forEach(this::addDeckViewToAccordion);
        }, 0);
    }

    private void addDeckViewToAccordion(BriefDeck briefDeck) {
        TitledPane titledPane = new TitledPane();
        generateDeckTitlePane(briefDeck, titledPane);
        decksAccordion.getPanes().add(titledPane);
    }

    private void generateDeckTitlePane(BriefDeck briefDeck, TitledPane titledPane) {
        titledPane.setOnMouseClicked(event -> {
            if (!event.getButton().equals(MouseButton.SECONDARY))
                return;
            event.consume();
            paneContextTemp = titledPane;
            deckContextMenu.show(titledPane, event.getScreenX(), event.getScreenY());
        });
        titledPane.getProperties().put("deck", briefDeck);
        titledPane.setText(String.format("%s (%s)", briefDeck.getName(), briefDeck.getHeroClass()));
        VBox vBox = new VBox();
        vBox.getChildren().addAll(CardTextView.buildAll(briefDeck.getDeck().getCardsInDeck(), briefDeck.getDeck()));
        vBox.setSpacing(2);
        vBox.getChildren().forEach(node -> node.setOnMouseClicked(event -> {
            if (!event.getButton().equals(MouseButton.SECONDARY))
                return;
            event.consume();
            cardTextContextTemp = (CardTextView) node;
            cardTextContextMenu.show(node, event.getScreenX(), event.getScreenY());
        }));
        titledPane.setContent(vBox);
    }

    private void prepareCardView(Object message, CardView cardView) {
        cardView.setOnContextMenuRequested(event ->
                cardContextMenu.show(cardView, event.getScreenX(), event.getScreenY()));
        cardView.setOnMouseClicked(event -> selectCard(cardView));
        cardView.forCollection(true);
        if (cardView.getBriefCard().getAmount() == 0) {
            cardView.disable(true);
        }
        if (cardView.getBriefCard().getCard().equals(message)) {
            selectCard(cardView);
        }
    }

    private void generateBriefDecks() {
        Player player = PlayerManager.getInstance().getPlayer();
        decks = BriefDeck.buildAll(player.getDecks());
    }

    private void bindTilePaneToCards(ObservableList<CardView> list) {
        filteredItems = new FilteredList<>(list);
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(
                () -> nameFilter.get().and(categoryFilter.get()).and(manaFilter.get()).and(possessionFilter.get()),
                nameFilter, categoryFilter, manaFilter, possessionFilter));
        FXUtil.runLater(() -> Bindings.bindContent(tilePane.getChildren(), filteredItems), 0);
    }

    private void selectCard(CardView cardView) {
        if (selectedCardView != null)
            selectedCardView.setStyle("-fx-background-color: transparent");
        selectedCardView = cardView;
        selectedCardView.setStyle("-fx-background-color: rgba(64,186,213,0.29)");
    }

    @FXML
    private void newDeckAction(ActionEvent event) {
        Deck deck = openAddDeck();
        if (deck != null) {
            addDeckViewToAccordion(BriefDeck.build(deck));
        }
    }

    @FXML
    private void clearFilters(ActionEvent event) {
        categoryChoice.setValue(null);
        manaChoice.setValue(null);
        possession.selectToggle(possession.getToggles().get(0));
        searchBox.clear();
    }

    @Override
    protected void backPressed(ActionEvent event) {
        if (message instanceof Class) {
            SceneManager.getInstance().showScene((Class) message);
        } else {
            super.backPressed(event);
        }
    }
}
