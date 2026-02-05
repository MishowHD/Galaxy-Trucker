package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Goods;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The type Flight board page lev test controller.
 */
public class FlightBoard_Page_LevTest_Controller implements Initializable, GeneralView {

    @FXML
    private TabPane shipTabPane;
    private LittleModelRepresentation lmr;
    private GUI view;
    private String player;
    @FXML
    private StackPane rootStack;

    /**
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * The constant currentController.
     */
    public volatile static PlayerShipBoard_Page_LevTest_Controller currentController;

    /**
     * Sets current controller.
     *
     * @param controller the controller
     */
    public static void setCurrentController(PlayerShipBoard_Page_LevTest_Controller controller) {
        currentController = controller;
    }

    /**
     * Gets current controller.
     *
     * @return the current controller
     */
    public static PlayerShipBoard_Page_LevTest_Controller getCurrentController() {
        return currentController;
    }

    private final Map<String, PlayerShipBoard_Page_LevTest_Controller> playerControllers = new HashMap<>();

    @FXML
    private void handleEndTurn() {
    }

    @FXML
    private ImageView flightboardImageView;
    @FXML
    private MediaView backgroundVideo;
    // ImageView “graphics” all’interno dei bottoni
    @FXML
    private ImageView ellipseImage0, ellipseImage1, ellipseImage2, ellipseImage3, ellipseImage4, ellipseImage5;
    @FXML
    private ImageView ellipseImage6, ellipseImage7, ellipseImage8, ellipseImage9, ellipseImage10, ellipseImage11;
    @FXML
    private ImageView ellipseImage12, ellipseImage13, ellipseImage14, ellipseImage15, ellipseImage16, ellipseImage17;
    @FXML
    private ImageView ellipseImage18;

    private final ImageView[] ellipseImages = new ImageView[24];

    private void initializeEllipseImagesArray() {
        ellipseImages[0] = ellipseImage0;
        ellipseImages[1] = ellipseImage1;
        ellipseImages[2] = ellipseImage2;
        ellipseImages[3] = ellipseImage3;
        ellipseImages[4] = ellipseImage4;
        ellipseImages[5] = ellipseImage5;
        ellipseImages[6] = ellipseImage6;
        ellipseImages[7] = ellipseImage7;
        ellipseImages[8] = ellipseImage8;
        ellipseImages[9] = ellipseImage9;
        ellipseImages[10] = ellipseImage10;
        ellipseImages[11] = ellipseImage11;
        ellipseImages[12] = ellipseImage12;
        ellipseImages[13] = ellipseImage13;
        ellipseImages[14] = ellipseImage14;
        ellipseImages[15] = ellipseImage15;
        ellipseImages[16] = ellipseImage16;
        ellipseImages[17] = ellipseImage17;
        ellipseImages[18] = ellipseImage18;
    }

    /**
     * Sets ellipse image.
     *
     * @param index     the index
     * @param imagePath the image path
     */
    public void setEllipseImage(int index, String imagePath) {
        if (index >= 0 && index < 19) {
            try {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                ellipseImages[index].setImage(image);
                ellipseImages[index].setVisible(true);
            } catch (Exception e) {
                System.err.println("Errore nel caricare l'immagine: " + imagePath);
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets ellipse image.
     *
     * @param index the index
     * @param image the image
     */
    public void setEllipseImage(int index, Image image) {
        if (index >= 0 && index < 19 && ellipseImages[index] != null) {
            ellipseImages[index].setImage(image);
            ellipseImages[index].setVisible(true);
        }
    }

    /**
     * Nasconde una specifica ImageView dell'ellisse
     *
     * @param index Indice della ImageView (0-23)
     */
    public void hideEllipseImage(int index) {
        if (index >= 0 && index < 19 && ellipseImages[index] != null) {
            ellipseImages[index].setVisible(false);
            ellipseImages[index].setImage(null);
        }
    }

    /**
     * Show ellipse image.
     *
     * @param index the index
     */
    public void showEllipseImage(int index) {
        if (index >= 0 && index < 19 && ellipseImages[index] != null && ellipseImages[index].getImage() != null) {
            ellipseImages[index].setVisible(true);
        }
    }

    /**
     * Nasconde tutte le ImageView dell'ellisse
     */
    public void hideAllEllipseImages() {
        for (int i = 0; i < 19; i++) {
            hideEllipseImage(i);
        }
    }

    /**
     * Gets ellipse image view.
     *
     * @param index the index
     * @return the ellipse image view
     */
    public ImageView getEllipseImageView(int index) {
        if (index >= 0 && index < 19) {
            return ellipseImages[index];
        }
        return null;
    }

    /**
     * Imposta l'opacità di una specifica ImageView dell'ellisse
     *
     * @param index   Indice della ImageView (0-23)
     * @param opacity Valore di opacità (0.0 - 1.0)
     */
    public void setEllipseImageOpacity(int index, double opacity) {
        if (index >= 0 && index < 19 && ellipseImages[index] != null) {
            ellipseImages[index].setOpacity(opacity);
        }
    }

    /**
     * Applica una rotazione a una specifica ImageView dell'ellisse
     *
     * @param index Indice della ImageView (0-23)
     * @param angle Angolo di rotazione in gradi
     */
    public void rotateEllipseImage(int index, double angle) {
        if (index >= 0 && index < 19 && ellipseImages[index] != null) {
            ellipseImages[index].setRotate(angle);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeBackgroundVideo();
        backgroundVideo.fitWidthProperty().bind(rootStack.widthProperty());
        backgroundVideo.fitHeightProperty().bind(rootStack.heightProperty());
        initializeEllipseImagesArray();
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
                    "/it/polimi/ingsw/graphics/backgrounds/bg_blue.mp4"   // o bg_blue.mp4, ecc.
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


    public void setView(GUI view) {
        System.out.println("[Controller] Setting view reference");
        this.view = view;
    }

    @Override
    public void showErrorDialog(String title, String message) {

    }

    /**
     * Init data.
     *
     * @param lmr the lmr
     */
    public void initData(LittleModelRepresentation lmr) {
        this.lmr = lmr;
        populateShipTabs();
    }


    private void populateShipTabs() {
        playerControllers.clear();
        List<ShipBoard> myBoards = new ArrayList<>();
        List<ShipBoard> otherBoards = new ArrayList<>();

        for (ShipBoard s : lmr.getShipBoards()) {
            if (Objects.equals(s.getMyPlayer().getUsername(), lmr.getMyNickname())) {
                myBoards.add(s);
            } else {
                otherBoards.add(s);
            }
        }

// Prima il proprio player
        List<ShipBoard> orderedBoards = new ArrayList<>();
        orderedBoards.addAll(myBoards);
        orderedBoards.addAll(otherBoards);

        for (ShipBoard s : orderedBoards) {
            Player p = s.getMyPlayer();
            String nickname = p.getUsername();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/playerShipTabLevTest.fxml"));
                Node content = loader.load();

                PlayerShipBoard_Page_LevTest_Controller controller = loader.getController();
                controller.setView(view);
                boolean isMyPlayer = Objects.equals(nickname, lmr.getMyNickname());
                if (isMyPlayer) setPlayer(nickname);
                controller.setPlayer(nickname, isMyPlayer, lmr.getShipBoards().size(), s.getShipMatrix()[2][3].getID());

                playerControllers.put(nickname, controller);

                Tab tab = new Tab(isMyPlayer ? "Me: " + nickname : nickname);
                tab.setContent(content);
                shipTabPane.getTabs().add(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateThingInHand(Player p, Object thing) throws Exception {
        if (thing != null) {
            if (thing instanceof SpaceShipTile) {
                // Gestione per le tile: usa il controller del giocatore specifico
                PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(p.getUsername());
                if (controller != null) {
                    controller.updateThingInHand(p, thing);
                }
            }
        } else {
            //forse metto qui il deposit little deck
            PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(p.getUsername());
            if (controller != null) {
                controller.updateThingInHand(p, null);
            }
        }
    }

    @Override
    public void addTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(ship.getMyPlayer().getUsername());
        if (controller != null) {
            controller.addTile(tile, row, col, ship);
        }
    }

    @Override
    public void updateTime() throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.updateTime();
        }
    }


    @Override
    public LittleModelRepresentation getLittleModelRepresentation() {
        return null;
    }

    @Override
    public void setClient(GenericClient client) {

    }

    @Override
    public void setLittleModelRepresentation(LittleModelRepresentation littleModelRepresentation) {

    }

    @Override
    public void Update(String Json) throws Exception {

    }

    @Override
    public void Start(String Username) throws Exception {

    }

    @Override
    public void movePlayerOnFlightboard(Player player, int newPos, int oldPos) throws Exception {
        int id = player.getPlayerId();
        if (id == 0) id = 52;
        else if (id == 1) id = 33;
        else if (id == 2) id = 61;
        else if (id == 3) id = 34;
        if (oldPos == -1) {
            PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(player.getUsername());
            if (controller != null) {
                controller.movePlayerOnFlightboard(player, newPos, oldPos);
            }
            setEllipseImage(newPos, "/it/polimi/ingsw/graphics/mammozzetti/" + id + ".png");
        } else {
            setEllipseImage(oldPos, (Image) null);
            setEllipseImage(newPos, "/it/polimi/ingsw/graphics/mammozzetti/" + id + ".png");
        }
    }

    @Override
    public void removeBLock(ShipBoard s, ArrayList<SpaceShipTile> block) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.removeBLock(s, block);
        }
    }

    @Override
    public void updateGoods(ShipBoard s, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.updateGoods(s, storagetiles, newgoods);
        }
    }

    @Override
    public void updateBatteries(ArrayList<ArrayList<Integer>> BattxPos, ShipBoard s, boolean toLoose) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.updateBatteries(BattxPos, s, toLoose);
        }
    }

    @Override
    public void removePassengers(ShipBoard s, ArrayList<ArrayList<Integer>> tiles, int numPass) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.removePassengers(s, tiles, numPass);
        }
    }

    @Override
    public void removePassengersFromTile(ShipBoard s, SpaceShipTile tile, int numPass) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.removePassengersFromTile(s, tile, numPass);
        }
    }

    @Override
    public void removeAlienFromTile(ShipBoard s, SpaceShipTile tile) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.removeAlienFromTile(s, tile);
        }
    }

    private void updateFlightboard(Object deck) {
//        try {
//            String imagePath = "/graphics/decks/little_deck_" + ((Deck) deck).getType() + ".png";
//            Image deckImage = new Image(getClass().getResourceAsStream(imagePath));
//            flightboardImageView.setImage(deckImage);
//        } catch (Exception e) {
//            System.err.println("Errore nel caricamento del deck: " + e.getMessage());
//        }
        //   }
    }

    @Override
    public void addTileFlipped(int indextile) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.addTileFlipped(indextile);
        }
    }

    @Override
    public void someoneDepositedLittleDeck(Deck d, Player player) throws Exception {

    }

    @Override
    public void someonePickedLittleDeck(Deck d, Player player) throws Exception {

    }

    @Override
    public void removeTileFlipped(int t) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.removeTileFlipped(t);
        }
    }

    @Override
    public void addPassenger(ShipBoard s, int row, int column) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.addPassenger(s, row, column);
        }
    }

    @Override
    public void youPayConsequences() {

    }

    @Override
    public void sendPenalty(int penalty, String type) {

    }

    @Override
    public void addAlien(ShipBoard s, int row, int column, AlienColor alienColor) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.addAlien(s, row, column, alienColor);
        }
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
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.looseAllGoods(s);
        }
    }

    @Override
    public void looseAllbatteries(ShipBoard s) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.looseAllbatteries(s);
        }
    }

    @Override
    public void middleEffect() throws Exception {

    }

    @Override
    public void endedEffect() throws Exception {

    }

    @Override
    public void updateCard() throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.updateCard();
        }
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
    public void showWrongTiles(ArrayList<Integer> tiles, String nickname, String nickEff) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(nickname);
        if (controller != null) {
            controller.showWrongTiles(tiles, nickname, nickEff);
        }
    }

    @Override
    public void updateShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {

    }

    @Override
    public void messageSubShips(ArrayList<ArrayList<SpaceShipTile>> subShip, String playerNickname) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(playerNickname);
        if (controller != null) {
            controller.messageSubShips(subShip, playerNickname);
        }
    }

    @Override
    public void ChooseSubShip(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) throws Exception {

    }

    @FXML
    private void handleSurrend() {

    }

    @Override
    public void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(playerNickname);
        if (controller != null) {
            controller.removeSingleTile(playerNickname, row, col, fromMistake, waste);
        }
    }

    @Override
    public void addWaitTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(ship.getMyPlayer().getUsername());
        if (controller != null) {
            controller.addWaitTile(tile, row, col, ship);
        }
    }

    @Override
    public void endBuilding(String playerNick) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(playerNick);
        if (controller != null) {
            controller.endBuilding(playerNick);
        }
    }

    @Override
    public void haveToFillTiles() throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.haveToFillTiles();
        }
    }

    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.updateGoodsRemaining(p, goodFInali);
        }
    }

    @Override
    public void removePlayerFromFlightboard(String player, int oldPos) throws Exception {
        setEllipseImage(oldPos, (Image) null);
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.removePlayerFromFlightboard(player, oldPos);
        }
    }

    @Override
    public void showOldGames(ArrayList<OldGame> oldGames) throws Exception {

    }

    @Override
    public void showInfoDialog(String title, String message) {

    }

    @Override
    public void showShutdown(String reason) {

    }

    @Override
    public void insertwaittileLMR1(String playerNickname) {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(playerNickname);
        if (controller != null) {
            controller.insertwaittileLMR1(playerNickname);
        }
    }

    @Override
    public void insertwaittileLMR2(String playerNickname) {
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(playerNickname);
        if (controller != null) {
            controller.insertwaittileLMR2(playerNickname);
        }
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
        PlayerShipBoard_Page_LevTest_Controller controller = playerControllers.get(p.getUsername());
        if (controller != null) {
            controller.updateBatteriesRemaining(p, batt);
        }
    }


}