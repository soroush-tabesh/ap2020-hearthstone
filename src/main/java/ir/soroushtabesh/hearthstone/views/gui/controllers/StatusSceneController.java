package ir.soroushtabesh.hearthstone.views.gui.controllers;

import ir.soroushtabesh.hearthstone.controllers.PlayerManager;
import ir.soroushtabesh.hearthstone.models.BriefDeck;
import ir.soroushtabesh.hearthstone.models.Player;
import ir.soroushtabesh.hearthstone.models.PlayerStats;
import ir.soroushtabesh.hearthstone.views.gui.GameWindow;
import javafx.application.Platform;
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
import java.util.List;
import java.util.ResourceBundle;

public class StatusSceneController extends AbstractSceneController {
    @FXML
    private TableView<PlayerStats> topPlayersTable;
    @FXML
    private TableView<PlayerStats> myRankTable;
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
    @FXML
    private Label cupsLabel;

    private ObservableList<BriefDeck> decks;
    private SortedList<BriefDeck> sortedDecks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        initDeckTable();
        initTopTable();
        initRankTable();
    }

    private void initRankTable() {
        TableColumn<PlayerStats, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rnk"));
        TableColumn<PlayerStats, String> nameColumn = new TableColumn<>("Username");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("un"));
        TableColumn<PlayerStats, String> cupColumn = new TableColumn<>("Cup");
        cupColumn.setCellValueFactory(new PropertyValueFactory<>("cupCount"));
        myRankTable.getColumns().addAll(Arrays.asList(rankColumn
                , nameColumn
                , cupColumn));
        myRankTable.getColumns().forEach(briefDeckTableColumn -> briefDeckTableColumn.setMinWidth(100));
        rankColumn.setMaxWidth(35);
        myRankTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initTopTable() {
        TableColumn<PlayerStats, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rnk"));
        TableColumn<PlayerStats, String> nameColumn = new TableColumn<>("Username");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("un"));
        TableColumn<PlayerStats, String> cupColumn = new TableColumn<>("Cup");
        cupColumn.setCellValueFactory(new PropertyValueFactory<>("cupCount"));
        topPlayersTable.getColumns().addAll(Arrays.asList(rankColumn
                , nameColumn
                , cupColumn));
        topPlayersTable.getColumns().forEach(briefDeckTableColumn -> briefDeckTableColumn.setMinWidth(100));
        rankColumn.setMaxWidth(35);
        topPlayersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initDeckTable() {
        TableColumn<BriefDeck, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<BriefDeck, String> cupCountColumn = new TableColumn<>("Cup");
        cupCountColumn.setCellValueFactory(new PropertyValueFactory<>("cupCount"));
        TableColumn<BriefDeck, String> winCountColumn = new TableColumn<>("Win Count");
        winCountColumn.setCellValueFactory(new PropertyValueFactory<>("winCount"));
        TableColumn<BriefDeck, String> gameCountColumn = new TableColumn<>("Game Count");
        gameCountColumn.setCellValueFactory(new PropertyValueFactory<>("gameCount"));
        TableColumn<BriefDeck, String> winRatioColumn = new TableColumn<>("Win Ratio");
        winRatioColumn.setCellValueFactory(new PropertyValueFactory<>("winRatio"));
        TableColumn<BriefDeck, String> avgManaColumn = new TableColumn<>("Average Mana");
        avgManaColumn.setCellValueFactory(new PropertyValueFactory<>("avgMana"));
        TableColumn<BriefDeck, String> heroClassColumn = new TableColumn<>("Hero Class");
        heroClassColumn.setCellValueFactory(new PropertyValueFactory<>("heroClass"));
        TableColumn<BriefDeck, String> favCardColumn = new TableColumn<>("Favourite Card");
        favCardColumn.setCellValueFactory(new PropertyValueFactory<>("favCard"));
        TableColumn<BriefDeck, String> favCardUsageColumn = new TableColumn<>("Favourite Card Usage");
        favCardUsageColumn.setCellValueFactory(new PropertyValueFactory<>("favCardUsage"));
        deckTable.getColumns().addAll(Arrays.asList(nameColumn
                , winCountColumn
                , cupCountColumn
                , gameCountColumn
                , winRatioColumn
                , avgManaColumn
                , heroClassColumn
                , favCardColumn
                , favCardUsageColumn));
        deckTable.getColumns().forEach(briefDeckTableColumn -> briefDeckTableColumn.setMinWidth(100));
        deckTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @Override
    public void onStart(Object message) {
        super.onStart(message);
        new Thread(() -> {
            GameWindow.addBusyWaiter();
            PlayerManager.getInstance().refreshPlayer();
            Player player = PlayerManager.getInstance().getPlayer();
            decks = BriefDeck.buildAll(player.getDecks());
            PlayerStats myStats = player.getPlayerStats();
            List<PlayerStats> players = PlayerManager.getInstance().getPlayers();
            int i = 1;
            for (PlayerStats playerStats : players)
                playerStats.setRnk(i++);
            int index = players.indexOf(myStats);
            Platform.runLater(() -> {
                sortedDecks = new SortedList<>(decks, (o1, o2) -> new CompareToBuilder()
                        .append(o2.getCupCount(), o1.getCupCount())
                        .append(o2.getWinRatio(), o1.getWinRatio())
                        .append(o2.getWinCount(), o1.getWinCount())
                        .append(o2.getGameCount(), o1.getGameCount())
                        .append(o2.getAvgMana(), o1.getAvgMana())
                        .toComparison());
                gamesWonLabel.setText(myStats.getWinCount() + "");
                gamesPlayedLabel.setText(myStats.getGameCount() + "");
                cupsLabel.setText(myStats.getCupCount() + "");
                coinsLabel.setText(player.getCoin() + "");
                usernameLabel.setText(player.getUsername());
                Bindings.bindContent(deckTable.getItems(), sortedDecks);
                topPlayersTable.getItems().clear();
                topPlayersTable.getItems().addAll(players.subList(0, Math.min(9, players.size() - 1)));
                myRankTable.getItems().clear();
                myRankTable.getItems().addAll(players.subList(Math.max(index - 5, 0)
                        , Math.min(index + 5, players.size() - 1)));
                GameWindow.releaseBusyWaiter();
            });
        }).start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Bindings.unbindContent(deckTable.getItems(), sortedDecks);

    }
}
