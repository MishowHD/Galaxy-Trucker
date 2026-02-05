package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.View.Utils_View.JfxMediaUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

/**
 * JavaFX controller for the "Scoring Phase" page shown at the very end of a match.
 * <p>
 *     The page displays the final ranking in a {@link TableView} together with a looping
 *     video background. From here the user can either close the application or—if re‑enabled—
 *     return to the lobby to start a new game.
 * </p>
 * <p>
 *     The controller also acts as a (mostly empty) implementation of {@link GeneralView}. Only the
 *     pieces of functionality strictly required in this phase are implemented; everything else is
 *     left as a no‑op because no further interaction with the model is expected after the game is
 *     finished.
 * </p>
 *
 * @author ChatGPT‑o3
 */
public class ScoringPhase_Page_Controller implements Initializable, GeneralView {

    /**
     * Table displaying the ranking. Each row is backed by a {@link ScoreRow} instance that
     * holds the player nickname and the corresponding final score.
     */
    @FXML
    private TableView<ScoreRow> scoreTable;

    /** The column that shows the player nickname. */
    @FXML
    private TableColumn<ScoreRow, String> playerColumn;

    /** Root {@link StackPane} used to bind the background video size and to center the window. */
    @FXML
    private StackPane rootStack;

    /**
     * {@link MediaView} that plays a seamless looping background clip to give the page a
     * polished look.
     */
    @FXML
    private MediaView backgroundVideo;

    /** The column that shows the formatted score for each player. */
    @FXML
    private TableColumn<ScoreRow, String> scoreColumn;

    /** Button that closes the JavaFX application entirely. */
    @FXML
    private Button exitButton;

//    @FXML
//    private Button newGameButton;

    /** Backing list for {@link #scoreTable}. Updated whenever new scores are supplied. */
    private final ObservableList<ScoreRow> rows = FXCollections.observableArrayList();

    /** Lightweight copy of the model used only to read the final scores. */
    private LittleModelRepresentation model;

    /** Reference to the main {@link GUI} so we can navigate back to the lobby (if desired). */
    private GUI rootView;

    // ─────────────────────────────────────────────  LIFECYCLE  ─────────────────────────────────────────────

    /**
     * Injected by the main GUI right after the FXML is loaded. Stores the root view reference and
     * extracts the miniature model in order to populate the table.
     *
     * @param rootView the main GUI façade
     */
    public void init(GUI rootView) {
        this.rootView = rootView;
        this.model = rootView.getLittleModelRepresentation();
        populateTable();
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Prepare background animation
        initializeBackgroundVideo();
        backgroundVideo.fitWidthProperty().bind(rootStack.widthProperty());
        backgroundVideo.fitHeightProperty().bind(rootStack.heightProperty());

        // Configure table columns
        playerColumn.setCellValueFactory(data -> data.getValue().playerProperty());
        scoreColumn.setCellValueFactory(data -> data.getValue().scoreProperty());
        scoreTable.setItems(rows);

        // Close application on click
        exitButton.setOnAction(e -> {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            Platform.exit();
            System.exit(0);
        });

        /*
        newGameButton.setOnAction(e -> {
            if (rootView != null) {
                rootView.returnToLobby();
            }
        });
        */

        // Resize & center once the stage is ready
        Platform.runLater(() -> {
            Stage stage = (Stage) rootStack.getScene().getWindow();
            stage.sizeToScene();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.centerOnScreen();
        });
    }

    // ───────────────────────────────────────────  INTERNAL HELPERS  ──────────────────────────────────────────

    /**
     * Creates a muted, infinitely looping {@link MediaPlayer} and attaches it to {@link #backgroundVideo}.
     * Any exception is logged to {@code stderr}; the UI will simply miss the video in that event.
     */
    private void initializeBackgroundVideo() {
        try {
            Media media = JfxMediaUtils.mediaFromResource(
                    getClass(),
                    "/it/polimi/ingsw/graphics/backgrounds/bg_generic.mp4"  // or bg_blue.mp4, etc.
            );
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setMute(true);
            player.setAutoPlay(true);

            backgroundVideo.setMediaPlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the {@link #rows} list from {@link LittleModelRepresentation#getFinalScores()} if available.
     * Scores are sorted in descending order.
     */
    private void populateTable() {
        if (model == null || model.getFinalScores() == null) return;

        rows.clear();
        model.getFinalScores()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> rows.add(new ScoreRow(entry.getKey(), entry.getValue())));
    }

    // ───────────────────────────────────────────  GENERAL‑VIEW ADAPTER  ──────────────────────────────────────

    /** {@inheritDoc} */
    @Override
    public LittleModelRepresentation getLittleModelRepresentation() {
        return null; // Not required in this phase
    }

    /** {@inheritDoc} */
    @Override
    public void setClient(GenericClient client) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void setLittleModelRepresentation(LittleModelRepresentation littleModelRepresentation) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void Update(String Json) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void Start(String Username) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void movePlayerOnFlightboard(Player player, int newPos, int oldPos) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void addTile(SpaceShipTile tile, int row, int col, ShipBoard ship) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void removeBLock(ShipBoard s, ArrayList<SpaceShipTile> block) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateGoods(ShipBoard s, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateBatteries(ArrayList<ArrayList<Integer>> BattxPos, ShipBoard shipBoard, boolean toLoose) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void removePassengers(ShipBoard s, ArrayList<ArrayList<Integer>> tiles, int numPass) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void removePassengersFromTile(ShipBoard s, SpaceShipTile tile, int numPass) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void removeAlienFromTile(ShipBoard s, SpaceShipTile tile) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateThingInHand(Player p, Object thing) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void addTileFlipped(int indextile) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void someoneDepositedLittleDeck(Deck d, Player player) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void someonePickedLittleDeck(Deck d, Player player) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void removeTileFlipped(int t) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void addPassenger(ShipBoard s, int row, int column) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void youPayConsequences() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void sendPenalty(int penalty, String type) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void addAlien(ShipBoard s, int row, int column, AlienColor alienColor) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void showFirstSight() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateStatus() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void onError() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void showFinalScore(HashMap<String, Float> finalScores) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void looseAllGoods(ShipBoard s) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void looseAllbatteries(ShipBoard s) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void middleEffect() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void endedEffect() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateCard() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateCosmicCredits(Player p, int i) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updatePlayersInGame() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateMessageOnly(String message) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateTime() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void showWrongTiles(ArrayList<Integer> tiles, String nickname, String nickEff) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void messageSubShips(ArrayList<ArrayList<SpaceShipTile>> subShip, String playerNickname) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void ChooseSubShip(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void addWaitTile(SpaceShipTile tile, int row, int col, ShipBoard ship) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void endBuilding(String playerNick) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void haveToFillTiles() { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void removePlayerFromFlightboard(String player, int oldPos) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void showOldGames(ArrayList<OldGame> oldGames) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void setView(GUI view) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void showErrorDialog(String title, String message) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void showInfoDialog(String title, String message) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void showShutdown(String reason) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void insertwaittileLMR1(String playerNickname) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void insertwaittileLMR2(String playerNickname) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void correctinput(String message) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void nextPlayerTurn(String myNickname) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void showTimedInfo(String text, int seconds) { /* no‑op */ }

    /** {@inheritDoc} */
    @Override
    public void updateBatteriesRemaining(Player p, int batt) { /* no‑op */ }

    // ─────────────────────────────────────────────  PUBLIC API  ─────────────────────────────────────────────

    /**
     * Rebuilds the {@link #rows} collection with the supplied map. This variant can be called by the
     * network layer without accessing {@link #model}.
     *
     * @param finalScores map player‑nickname → score expressed as a {@code float}
     */
    public void setScores(HashMap<String, Float> finalScores) {
        rows.clear();
        finalScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> rows.add(new ScoreRow(e.getKey(), e.getValue())));
    }

    // ───────────────────────────────────────────  TABLE ROW BACKING  ────────────────────────────────────────

    /**
     * Simple bean used as the backing model for each row inside {@link #scoreTable}.
     */
    public static class ScoreRow {
        private final SimpleStringProperty player = new SimpleStringProperty();
        private final SimpleStringProperty score = new SimpleStringProperty();

        /**
         * @param player the nickname of the player
         * @param score  the final score expressed as a {@code float}; formatted to two decimals
         */
        public ScoreRow(String player, Float score) {
            this.player.set(player);
            this.score.set(String.format("%.2f", score));
        }

        /** @return the player nickname */
        public String getPlayer() {
            return player.get();
        }

        /** @return JavaFX property for databinding */
        public SimpleStringProperty playerProperty() {
            return player;
        }

        /** @return formatted score */
        public String getScore() {
            return score.get();
        }

        /** @return JavaFX property for databinding */
        public SimpleStringProperty scoreProperty() {
            return score;
        }
    }
}
