package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.BriefDeck;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.models.PlayerStats;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StatusSceneController extends AbstractSceneController {
    @FXML
    private TableView<BriefDeck> deckTable;
    @FXML
    private Label gamesWonLabel;
    @FXML
    private Label coinsLabel;
    @FXML
    private Label gamesPlayedLabel;
    @FXML
    private Label usernameLabel;

    private TableColumn<BriefDeck, String> nameColumn;
    private TableColumn<BriefDeck, String> winCountColumn;
    private TableColumn<BriefDeck, String> gameCountColumn;
    private TableColumn<BriefDeck, String> winRatioColumn;
    private TableColumn<BriefDeck, String> avgManaColumn;
    private TableColumn<BriefDeck, String> heroNameColumn;
    private TableColumn<BriefDeck, String> heroClassColumn;
    private TableColumn<BriefDeck, String> favCardColumn;
    private TableColumn<BriefDeck, String> favCardUsageColumn;

    private ObservableList<BriefDeck> decks;
    private SortedList<BriefDeck> sortedDecks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        winCountColumn = new TableColumn<>("Win Count");
        winCountColumn.setCellValueFactory(new PropertyValueFactory<>("winCount"));
        gameCountColumn = new TableColumn<>("Game Count");
        gameCountColumn.setCellValueFactory(new PropertyValueFactory<>("gameCount"));
        winRatioColumn = new TableColumn<>("Win Ratio");
        winRatioColumn.setCellValueFactory(new PropertyValueFactory<>("winRatio"));
        avgManaColumn = new TableColumn<>("Average Mana");
        avgManaColumn.setCellValueFactory(new PropertyValueFactory<>("avgMana"));
        heroNameColumn = new TableColumn<>("Hero Name");
        heroNameColumn.setCellValueFactory(new PropertyValueFactory<>("heroName"));
        heroClassColumn = new TableColumn<>("Hero Class");
        heroClassColumn.setCellValueFactory(new PropertyValueFactory<>("heroClass"));
        favCardColumn = new TableColumn<>("Favourite Card");
        favCardColumn.setCellValueFactory(new PropertyValueFactory<>("favCard"));
        favCardUsageColumn = new TableColumn<>("Favourite Card Usage");
        favCardUsageColumn.setCellValueFactory(new PropertyValueFactory<>("favCardUsage"));
        deckTable.getColumns().addAll(Arrays.asList(nameColumn
                , winCountColumn
                , gameCountColumn
                , winRatioColumn
                , avgManaColumn
                , heroNameColumn
                , heroClassColumn
                , favCardColumn
                , favCardUsageColumn));
        deckTable.getColumns().forEach(briefDeckTableColumn -> briefDeckTableColumn.setMinWidth(100));
        deckTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @Override
    public void onStart(Object message) {
        super.onStart(message);
        Player player = PlayerManager.getInstance().getPlayer();
        decks = BriefDeck.buildAll(player.getDecks());
        PlayerStats playerStats = player.getPlayerStats();
        sortedDecks = new SortedList<>(decks, (o1, o2) -> new CompareToBuilder()
                .append(o2.getWinRatio(), o1.getWinRatio())
                .append(o2.getWinCount(), o1.getWinCount())
                .append(o2.getGameCount(), o1.getGameCount())
                .append(o2.getAvgMana(), o1.getAvgMana())
                .toComparison());
        gamesWonLabel.setText(playerStats.getWinCount() + "");
        gamesPlayedLabel.setText(playerStats.getGameCount() + "");
        coinsLabel.setText(player.getCoin() + "");
        usernameLabel.setText(player.getUsername());
        Bindings.bindContent(deckTable.getItems(), sortedDecks);
    }
}
