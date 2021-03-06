package ir.soroushtabesh.hearthstone.views.gui.controls;

import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.Deck;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.InfoPassive;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.util.Logger;
import ir.soroushtabesh.hearthstone.util.gui.FXUtil;
import ir.soroushtabesh.hearthstone.views.gui.BoardScene;
import ir.soroushtabesh.hearthstone.views.gui.CollectionScene;
import ir.soroushtabesh.hearthstone.views.gui.controllers.SceneManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class SelectHeroDeckDialog extends Dialog<ButtonType> implements Initializable, PlayerInfoGetter {

    @FXML
    private ComboBox<InfoPassive> passiveCombo;
    @FXML
    private ComboBox<Hero> heroCombo;
    @FXML
    private ComboBox<Deck> deckCombo;

    private FilteredList<Deck> filteredItems;
    private ObjectProperty<Predicate<Deck>> categoryFilter;


    public SelectHeroDeckDialog(Node owner) {
        Logger.log("Dialog", getClass().getSimpleName());
        initOwner(owner.getScene().getWindow());
        setTitle("Play");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(theClass -> this);
        fxmlLoader.setLocation(getClass().getResource(getClass().getSimpleName() + ".fxml"));
        try {
            getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        setupButtons();
    }

    public SelectHeroDeckDialog(Node owner, int playerId) {
        this(owner);
        setHeaderText("Select Hero and Deck for Player" + (playerId + 1));
    }

    private void setupButtons() {
        getDialogPane().getButtonTypes().add(ButtonType.OK);
        getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        getDialogPane().lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, this::okPressed);
    }

    private void okPressed(ActionEvent event) {
        if (heroCombo.getValue() == null) {
            event.consume();
            FXUtil.showAlertInfo("Play", "Deck", "Hero can't be empty.");
        } else if (deckCombo.getValue() == null) {
            event.consume();
            FXUtil.showAlertInfo("Play", "Deck", "Deck can't be empty.");
        } else if (passiveCombo.getValue() == null) {
            event.consume();
            FXUtil.showAlertInfo("Play", "Deck", "Passive can't be empty.");
        }
//        updatePlayer();
    }


    @Override
    public Deck getSelectedDeck() {
        return deckCombo.getValue();
    }

    @Override
    public Hero getSelectedHero() {
        return heroCombo.getValue();
    }

    @Override
    public InfoPassive getSelectedInfoPassive() {
        return passiveCombo.getValue();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Player player = PlayerManager.getInstance().getPlayer();
        initHeroCombo(player);
        initDeckCombo(player);
        initPassiveCombo();
    }

    private void initHeroCombo(Player player) {
        heroCombo.getItems().addAll(player.getOpenHeroes());
        heroCombo.getSelectionModel().select(0);
    }

    private void initPassiveCombo() {
        List<InfoPassive> passives = CardManager.getInstance().getAllPassives();
        Collections.shuffle(passives);
        passiveCombo.getItems().addAll(passives.subList(0, 3));
        passiveCombo.getSelectionModel().select(0);
    }

    private void initDeckCombo(Player player) {
        categoryFilter = new SimpleObjectProperty<>();
        categoryFilter.bind(Bindings.createObjectBinding(() ->
                        deck -> (heroCombo.getValue() != null)
                                && (heroCombo.getValue().getHeroClass() == Hero.HeroClass.ALL
                                || heroCombo.getValue().getHeroClass() == deck.getHeroClass()),
                heroCombo.valueProperty()));
        filteredItems = new FilteredList<>(FXCollections.observableArrayList(player.getDecks()));
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(categoryFilter::get, categoryFilter));
        Bindings.bindContent(deckCombo.getItems(), filteredItems);
        deckCombo.getSelectionModel().select(0);
    }

    public void goCollection(ActionEvent event) {
        setResult(ButtonType.APPLY);
        close();
        SceneManager.getInstance().showScene(CollectionScene.class, BoardScene.class);
    }
}
