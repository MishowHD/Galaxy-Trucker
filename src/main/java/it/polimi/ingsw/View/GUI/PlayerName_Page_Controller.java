package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.Client.ClientFactory;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.View.Utils_View.JfxMediaUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * The type Player name page controller.
 */
public class PlayerName_Page_Controller implements GeneralView {

    @FXML
    private TextField usernameField;

    @FXML
    private Button registerButton;
    @FXML
    private StackPane rootStack;

    @FXML
    private MediaView backgroundVideo;

    private GUI view;
    private boolean useSocket;
    private String serverAddress;
    private LittleModelRepresentation littleModel;

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        initializeBackgroundVideo();
        backgroundVideo.fitWidthProperty().bind(rootStack.widthProperty());
        backgroundVideo.fitHeightProperty().bind(rootStack.heightProperty());
        // Add key press event handler to the username field
        usernameField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                register();
                // Consume the event to prevent it from bubbling up
                event.consume();
            }
        });

        // Set focus on the username field when the scene loads
        Platform.runLater(() -> usernameField.requestFocus());
        Platform.runLater(() -> {
            Stage stage = (Stage) rootStack.getScene().getWindow();
            stage.sizeToScene();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.centerOnScreen();
        });
        Platform.runLater(() -> {
            if (rootStack.getScene() != null) {
                rootStack.getScene()
                        .getStylesheets()
                        .add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/css/fxml_class.css")).toExternalForm());
            }
        });
    }



    private void initializeBackgroundVideo() {
        try {
            Media media = JfxMediaUtils.mediaFromResource(
                    getClass(),
                    "/it/polimi/ingsw/graphics/backgrounds/bg_generic.mp4"  // o bg_blue.mp4, ecc.
            );
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setMute(true);
            player.setAutoPlay(true);

            backgroundVideo.setMediaPlayer(player);   // backgroundVideo è il tuo MediaView
        } catch (Exception e) {
            e.printStackTrace();                       // gestisci meglio se vuoi
        }
    }


    // Iniettati da GUI.start(...) subito dopo il loader.load()
    public void setView(GUI view) {
        this.view = view;
    }

    @Override
    public void showErrorDialog(String title, String message) {

    }

    /**
     * Sets use socket.
     *
     * @param useSocket the use socket
     */
    public void setUseSocket(boolean useSocket) {
        this.useSocket = useSocket;
    }

    /**
     * Sets server address.
     *
     * @param serverAddress the server address
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Register.
     */
    @FXML
    public void register() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Per favore inserisci un nome utente.");
            return;
        }

        // Disabilitiamo i controlli mentre tentiamo la registrazione
        setControlsEnabled(false);

        new Thread(() -> {
            try {
                // 1) Creo il client (Socket o RMI)
                GenericClient client = ClientFactory.createClient(
                        littleModel,
                        useSocket,
                        serverAddress,
                        username
                );
                // 2) Se uso socket, faccio il polling una volta sola fino a OK o rifiuto
                if (useSocket) {
                    // registrazione OK
                    while (!client.isUsernameAccepted()) {
                        if (client.isUsernameRejected()) {
                            // alert e reset dei flag, poi esco dal thread
                            Platform.runLater(() ->
                                    showAlert(Alert.AlertType.WARNING, "Username già in uso, inseriscine un altro.")
                            );
                            client.resetUsernameFlags();
                            return;
                        }
                        Thread.sleep(100);
                    }
                }
                // altrimenti, per RMI, se createClient non ha lanciato eccezione, vado avanti

                // 3) Registrazione avvenuta con successo: inietto client e cambio scena
                GUI.getInstance().setClient(client);
                Platform.runLater(() -> {
                    try {
                        view.createGamePage(username);
                    } catch (Exception e) {
                        showError(e);
                    }
                });

            } catch (RemoteException re) {
                // RMI: controllo se è “already in use”
                if (re.getMessage() != null && re.getMessage().contains("already in use")) {
                    Platform.runLater(() ->
                            showAlert(Alert.AlertType.WARNING, "Username già in uso, inseriscine un altro.")
                    );
                } else {
                    Platform.runLater(() -> showError(re));
                }
            } catch (Exception ex) {
                // altri errori (connessione, binding, ecc.)
                Platform.runLater(() -> showError(ex));
            } finally {
                // riabilitiamo sempre i controlli, anche dopo alert o errore
                Platform.runLater(() -> setControlsEnabled(true));
            }
        }).start();
    }

    private void showError(Exception ex) {
        // Riabilitiamo i controlli
        setControlsEnabled(true);

        // Mostriamo un alert con l'errore
        showAlert(Alert.AlertType.ERROR, ex.getMessage());
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }

    private void setControlsEnabled(boolean enabled) {
        Platform.runLater(() -> {
            usernameField.setDisable(!enabled);
            registerButton.setDisable(!enabled);
        });
    }

    @Override
    public LittleModelRepresentation getLittleModelRepresentation() {
        return null;
    }

    @Override
    public void setClient(GenericClient client) {

    }

    @Override
    public void setLittleModelRepresentation(LittleModelRepresentation littleModel) {
        this.littleModel = littleModel;
    }


    @Override
    public void Update(String Json) throws Exception {

    }

    @Override
    public void Start(String Username) throws Exception {

    }

    @Override
    public void movePlayerOnFlightboard(Player player, int newPos, int oldPos) throws Exception {

    }

    @Override
    public void addTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {

    }

    @Override
    public void removeBLock(ShipBoard s, ArrayList<SpaceShipTile> block) throws Exception {

    }

    @Override
    public void updateGoods(ShipBoard s, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {

    }

    @Override
    public void updateBatteries(ArrayList<ArrayList<Integer>> BattxPos, ShipBoard shipBoard, boolean toLoose) throws Exception {

    }

    @Override
    public void removePassengers(ShipBoard s, ArrayList<ArrayList<Integer>> tiles, int numPass) throws Exception {

    }

    @Override
    public void removePassengersFromTile(ShipBoard s, SpaceShipTile tile, int numPass) throws Exception {

    }

    @Override
    public void removeAlienFromTile(ShipBoard s, SpaceShipTile tile) throws Exception {

    }

    @Override
    public void updateThingInHand(Player p, Object thing) throws Exception {

    }

    @Override
    public void addTileFlipped(int indextile) throws Exception {

    }

    @Override
    public void someoneDepositedLittleDeck(Deck d, Player player) throws Exception {

    }

    @Override
    public void someonePickedLittleDeck(Deck d, Player player) throws Exception {

    }

    @Override
    public void removeTileFlipped(int t) throws Exception {

    }

    @Override
    public void addPassenger(ShipBoard s, int row, int column) throws Exception {

    }

    @Override
    public void youPayConsequences() {

    }

    @Override
    public void sendPenalty(int penalty, String type) {

    }

    @Override
    public void addAlien(ShipBoard s, int row, int column, AlienColor alienColor) throws Exception {

    }

    @Override
    public void showFirstSight() throws Exception {

    }

    @Override
    public void updateStatus() throws Exception {

    }

    @Override
    public void onError() throws Exception {

    }

    @Override
    public void showFinalScore(HashMap<String, Float> finalScores) throws Exception {

    }

    @Override
    public void looseAllGoods(ShipBoard s) throws Exception {

    }

    @Override
    public void looseAllbatteries(ShipBoard s) throws Exception {

    }

    @Override
    public void middleEffect() throws Exception {

    }

    @Override
    public void endedEffect() throws Exception {

    }

    @Override
    public void updateCard() throws Exception {

    }

    @Override
    public void updateCosmicCredits(Player p, int i) throws Exception {

    }

    @Override
    public void updatePlayersInGame() throws Exception {

    }

    @Override
    public void updateMessageOnly(String message) throws Exception {

    }

    @Override
    public void updateTime() throws Exception {

    }

    @Override
    public void showWrongTiles(ArrayList<Integer> tiles, String nickname, String nickEff) throws Exception {

    }

    @Override
    public void updateShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {

    }

    @Override
    public void messageSubShips(ArrayList<ArrayList<SpaceShipTile>> subShip, String playerNickname) throws Exception {

    }

    @Override
    public void ChooseSubShip(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) throws Exception {

    }

    @Override
    public void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) throws Exception {

    }

    @Override
    public void addWaitTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {

    }

    @Override
    public void endBuilding(String playerNick) throws Exception {

    }

    @Override
    public void haveToFillTiles() throws Exception {

    }

    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception {

    }

    @Override
    public void removePlayerFromFlightboard(String playerNickname, int oldpos) throws Exception {

    }

    @Override
    public void showInfoDialog(String title, String message) {

    }

    @Override
    public void showShutdown(String reason) {

    }

    @Override
    public void insertwaittileLMR1(String playerNickname) {

    }

    @Override
    public void insertwaittileLMR2(String playerNickname) {

    }

    @Override
    public void correctinput(String message) {

    }

    @Override
    public void nextPlayerTurn(String myNickname) {

    }

    @Override
    public void showTimedInfo(String text, int seconds) {

    }

    @Override
    public void updateBatteriesRemaining(Player p, int batt) throws Exception {

    }


    @Override
    public void showOldGames(ArrayList<OldGame> oldGames) throws Exception {

    }

}