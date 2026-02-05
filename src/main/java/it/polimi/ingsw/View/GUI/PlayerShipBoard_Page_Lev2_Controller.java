package it.polimi.ingsw.View.GUI;


import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.AbandonedShip.Card_AbandonedShip;
import it.polimi.ingsw.Model.Cards.AbandonedStation.Card_AbandonedStation;
import it.polimi.ingsw.Model.Cards.CombatZone.Card_Combat_zone;
import it.polimi.ingsw.Model.Cards.Epidemic.Card_Epidemic;
import it.polimi.ingsw.Model.Cards.MeteorSwarm.Card_MeteorSwarm;
import it.polimi.ingsw.Model.Cards.OpenSpace.Card_OpenSpace;
import it.polimi.ingsw.Model.Cards.Pirates.Card_Pirates;
import it.polimi.ingsw.Model.Cards.Planets.Card_PlanetCard;
import it.polimi.ingsw.Model.Cards.Slavers.Card_Slavers;
import it.polimi.ingsw.Model.Cards.Smugglers.Card_Smugglers;
import it.polimi.ingsw.Model.Cards.Stardust.Card_Stardust;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.View.Utils_View.CommandType;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Player.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The type Player ship board page lev 2 controller.
 */
public class PlayerShipBoard_Page_Lev2_Controller implements Initializable, GeneralView {
    private static final double GRID_SCALE = 0.92;


    @FXML
    private Label labelNextPlayer;
    /**
     * The Current item container.
     */
    @FXML
    public StackPane currentItemContainer;
    /**
     * The Card text.
     */
    @FXML
    public Label cardText;
    // ------------------- box16: acceptToLandOnAPlanet (Metodo 16)
    @FXML
    private VBox boxACCEPT_TO_LAND_ON_A_PLANET;
    @FXML
    private Label label16;  // Testo: landing on a planet
    @FXML
    private Button planetBtn0; // Land on planet 0
    @FXML
    private Button planetBtn1; // Land on planet 1
    @FXML
    private Button planetBtn2; // Land on planet 2
    @FXML
    private Button planetBtn3; // Land on planet 3
    @FXML
    private Button noBtn16;    // Skip landing

    // riga 0 è fissa, salviamo solo la colonna (5 o 6) se l’utente ha selezionato quella wait‐cell
    private Integer selectedWaitCol = null;

    // ------------------- box17: chooseCannonBatteryPos (Metodo 17)
    @FXML
    private Button noBtn17;
    @FXML
    private Button yesBtn17;
    @FXML
    private Button doneBtn17;
    @FXML
    private Button selectStorageBtn17;


    @FXML
    private StackPane centerStackPane;   // <StackPane fx:id="gridStackPane">

    @FXML
    private Button handleSelectGood1_17;
    @FXML
    private Button handleSelectGood2_17;
    @FXML
    private Button handleSelectGood3_17;
    @FXML
    private Button handleSelectGood4_17;

    // ------------------- box18: chooseCannonBatteryPos (Metodo 18)
    @FXML
    private VBox boxCHOOSE_CANNON_BATTERY_POS;
    @FXML
    private Label label18;           // Testo: selezione cannoni e batterie
    @FXML
    private Button selectCannonsBtn18; // Attiva modalità selezione cannoni
    @FXML
    private Button selectBatteriesBtn18; // Attiva selezione batterie
    @FXML
    private Button doneBtn18;         // Invia comando per metodo 18

    // ------------------- box19: chooseHowToFaceMeteors (Metodo 19)
    @FXML
    private VBox boxCHOOSE_HOW_TO_FACE_METEORS;
    @FXML
    private Label label19;           // Testo: selezione difese
    @FXML
    private Button selectTilesBtn19; // Attiva modalità difesa
    @FXML
    private Button doneBtn19;        // Invia comando per metodo 19

    // ------------------- box20: choosePassengersToLose (Metodo 20)
    @FXML
    private VBox boxCHOOSE_PASSENGERS_TO_LOSE;
    @FXML
    private Label label20;               // Testo: selezione passeggeri
    @FXML
    private Button selectPassengersBtn20; // Attiva selezione passeggeri
    @FXML
    private Button doneBtn20;            // Invia comando per metodo 20

    // ------------------- box21: chooseToClaimReward (boolean version) (Metodo 21)
    @FXML
    private VBox boxCHOOSE_TO_CLAIM_REWARD;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label label21;         // Testo: scegliere se prendere la ricompensa
    @FXML
    private Button yesBtn21;       // Invoca metodo 21 con true
    @FXML
    private Button noBtn21;        // Invoca metodo 21 con false

    // ------------------- box22: chooseToClaimReward (con storage + goods) (Metodo 22)
// SIMILE A box23 + box26 (combinazione logica)
    @FXML
    private VBox boxCHOOSE_TO_CLAIM_REWARD_WITH_GOODS;
    @FXML
    private Label label22;
    @FXML
    private Button noBtn22;
    @FXML
    private Button yesBtn22;
    @FXML
    private Button doneBtn22;
    @FXML
    private Button handleSelectGood1_22;
    @FXML
    private Button handleSelectGood2_22;
    @FXML
    private Button handleSelectGood3_22;
    @FXML
    private Button handleSelectGood4_22;
    @FXML
    private Button handleSelectGood1;
    @FXML
    private Button handleSelectGood2;
    @FXML
    private Button handleSelectGood3;
    @FXML
    private Button handleSelectGood4;


    // ------------------- box23: chooseToPlaceBatteries (Metodo 23)
// SIMILE a seconda parte di box18
    @FXML
    private VBox boxCHOOSE_TO_PLACE_BATTERIES;
    @FXML
    private Label label23;              // Testo: posizionamento batterie
    @FXML
    private Button selectTilesBtn23;    // Selezione posizioni batterie
    @FXML
    private Button doneBtn23;           // Invia comando per metodo 23

    // ------------------- box24: chooseToStartFirePower (Metodo 24)
// SIMILE a box18
    @FXML
    private VBox boxCHOOSE_TO_START_FIRE_POWER;
    @FXML
    private Label label24;               // Testo: selezione cannoni + batterie
    @FXML
    private Button selectCannonsBtn24;   // Selezione cannoni
    @FXML
    private Button selectBatteriesBtn24; // Selezione batterie
    @FXML
    private Button doneBtn24;            // Invia comando per metodo 24

    // ------------------- box25: chooseToStartMotor (Metodo 25)
// SIMILE a box24 (ma usa motori invece di cannoni)
    @FXML
    private VBox boxCHOOSE_TO_START_MOTOR;
    @FXML
    private Label label25;               // Testo: selezione motori + batterie
    @FXML
    private Button selectEnginesBtn25;   // Selezione motori
    @FXML
    private Button selectBatteriesBtn25; // Selezione batterie
    @FXML
    private Button doneBtn25;            // Invia comando per metodo 25

    // ------------------- box26: chooseWhereToPutGoods (Metodo 26)
// SIMILE a seconda parte di box22
    @FXML
    private VBox boxCHOOSE_WHERE_TO_PUT_GOODS;
    @FXML
    private Label label26;             // Testo: posizionamento merci
    @FXML
    private Button selectStorageBtn26; // Selezione celle per merci
    @FXML
    private Button doneBtn26;          // Invia comando per metodo 26


    @FXML
    private ImageView boardBackground; // Aggiungi questa riga

    @FXML
    private HBox topControls;
    @FXML
    private VBox controlsBox;
    @FXML
    private ListView<?> depositedTilesListView;
    @FXML
    private Label playerBanner;
    @FXML
    private VBox itemsList;
    @FXML
    private GridPane gridMatrix;
    @FXML
    private ImageView currentItem;
    @FXML
    private ImageView WaitTile1 = null;
    @FXML
    private ImageView WaitTile2 = null;
    @FXML
    private ImageView shipBaseImage; // non usato nel codice, ma presente nel FXML
    @FXML
    private ImageView logoImageView;
    @FXML
    private Label timerLabel;
    @FXML
    private Button startTimerButton;
    @FXML
    private ComboBox<Integer> playerNumberComboBox;
    @FXML
    private Button endBuildingButton;
    @FXML
    private Button rotateButton;

    @FXML
    private Button demoButton;
    @FXML
    private Button pickTileButton;

    private Timeline timerTimeline;
    private int remainingTime;
    private boolean isCurrentPlayer;
    private Image tempImage;
    private boolean pickMode = false;
    private boolean canInsertWait1 = false;
    private boolean canInsertWait2 = false;

    private boolean insertMode = false;
    private int numberOfPlayers;
    //-----------------------CARD ZONE------------
    @FXML
    private Pane cardPane;
    private GUI view;
    // dopo tutti gli altri campi
    private StackPane[][] cellPanes;
    //------for card methods

    //v box 17
    @FXML
    private ImageView good1img17;
    @FXML
    private ImageView good2img17;
    @FXML
    private ImageView good3img17;
    @FXML
    private ImageView good4img17;
    private boolean selectingStorageForRewardStation = false;
    private final Map<ArrayList<Integer>, ArrayList<Goods>> rewardGoodsPlacementMapStation = new LinkedHashMap<>();


    // ------------------------ VBox 18 - Cannoni e Batterie ------------------------
    private final List<List<Integer>> selectedCannons = new ArrayList<>();
    private final List<List<Integer>> selectedBatteriesForCannons = new ArrayList<>();
    private boolean selectingCannons = false;
    private boolean selectingBatteriesForCannons = false;

    // ------------------------ VBox 19 - Difesa dai meteoriti ------------------------
    private final List<Integer> selectedMeteorDefenses = new ArrayList<>();
    private boolean selectingMeteorDefenses = false;

    // ------------------------ VBox 20 - Passeggeri da perdere ---------

    private final List<List<Integer>> selectedPassengersToLose = new ArrayList<>();
    private boolean selectingPassengersToLose = false;
    private boolean confirmPassengerLoss = false;

    // ------------------------ VBox 21 - Reward semplice (boolean) ------------------------
    private boolean wantToClaimSimpleReward = false;
    private boolean choosingSimpleReward = false;

    // ------------------------ VBox 22 - Reward con beni e storage ------------------------
    // private final List<List<Integer>> selectedStorageTilesForReward = new ArrayList<>();
    // private final List<List<Integer>> selectedGoodsForReward = new ArrayList<>();
    private boolean selectingStorageForReward = false;
    private final Map<List<Integer>, List<Integer>> rewardGoodsPlacementMap = new LinkedHashMap<>();
    private Integer selectedGoodValue_22 = null;
    @FXML
    private ImageView ivGood1;
    @FXML
    private ImageView ivGood2;
    @FXML
    private ImageView ivGood3;
    @FXML
    private ImageView ivGood4;

    // ------------------------ VBox 23 - Posizionamento batterie ------------------------
    private final List<List<Integer>> selectedBatteryPlacements = new ArrayList<>();
    private boolean selectingBatteryPlacements = false;

    // ------------------------ VBox 24 - Attivazione fuoco: cannoni e batterie ------------------------
    private final List<List<Integer>> selectedFirepowerCannons = new ArrayList<>();
    private final List<List<Integer>> selectedFirepowerBatteries = new ArrayList<>();
    private boolean selectingFirepowerCannons = false;
    private boolean selectingFirepowerBatteries = false;

    // ------------------------ VBox 25 - Attivazione motori e batterie ------------------------
    private final List<List<Integer>> selectedEngines = new ArrayList<>();
    private final List<List<Integer>> selectedEngineBatteries = new ArrayList<>();
    private boolean selectingEngines = false;
    private boolean selectingEngineBatteries = false;

    // ------------------------ VBox 26 - Posizionamento merci ------------------------
    private final Map<List<Integer>, List<Integer>> goodsPlacementMap = new LinkedHashMap<>();
    private Integer goodSelectedToPlace = null;
    private boolean selectingGoodsPositions = false;
    private List<Integer> currentStorageRewardPos = null;
    @FXML
    private ImageView good1img26;
    @FXML
    private ImageView good2img26;
    @FXML
    private ImageView good3img26;
    @FXML
    private ImageView good4img26;

    private static final double TILE_SCALE = 0.96;   // ogni tile occupa il 96 % della cella


    public void setView(GUI view) {
        System.out.println("[Controller] Setting view reference");
        this.view = view;
    }

    @Override
    public void showErrorDialog(String title, String message) {

    }

    @Override
    public void showInfoDialog(String title, String message) {

    }

    @Override
    public void showShutdown(String reason) {

    }

    @Override
    public void insertwaittileLMR1(String playerNickname) {
        for (Node node : gridMatrix.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (r != null && c != null && r == 0 && c == 5) {
                StackPane cell = (StackPane) node;
                // Pulisci eventuali children precedenti
                cell.getChildren().clear();
                break;
            }
        }
    }

    @Override
    public void insertwaittileLMR2(String playerNickname) {
        for (Node node : gridMatrix.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (r != null && c != null && r == 0 && c == 6) {
                StackPane cell = (StackPane) node;
                // Pulisci eventuali children precedenti
                cell.getChildren().clear();
                break;
            }
        }
    }

    @Override
    public void correctinput(String message) {
        labelNextPlayer.setVisible(true);
        hideAllControlBoxes();
    }

    @Override
    public void nextPlayerTurn(String myNickname) {
        //labelNextPlayer.setVisible(true);
        //hideAllControlBoxes();
    }

    @Override
    public void showTimedInfo(String text, int seconds) {

    }


    private void resizeAllTiles(double width) {
        for (Node node : gridMatrix.getChildren()) {
            if (node instanceof ImageView imageView) {
                imageView.setFitWidth(width);
                imageView.setFitHeight(width);
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logoImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/graphics/images/logo.png")).toExternalForm()));
// Aggiungi binding responsivo per lo ScrollPane
        scrollPane.setFitToHeight(false);
        // 1) Di default cardText è nascosto e non occupa spazio:
        cardText.setVisible(false);
        cardText.managedProperty().bind(cardText.visibleProperty());


// Binding per occupare tutto lo spazio verticale
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
// Binding “reattivo” senza listener manuale:
        itemsList.prefWidthProperty().bind(
                Bindings.createDoubleBinding(
                        () -> scrollPane.getViewportBounds().getWidth() - 20,
                        scrollPane.viewportBoundsProperty()
                )
        );

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Platform.runLater(() -> {
            ScrollBar vBar = (ScrollBar) scrollPane.lookup(".scroll-bar:vertical");
            if (vBar != null) {
                vBar.setOpacity(0);
                vBar.setPrefWidth(0);
                vBar.setMaxWidth(0);
                vBar.setMinWidth(0);
            }
            ScrollBar hBar = (ScrollBar) scrollPane.lookup(".scroll-bar:horizontal");
            if (hBar != null) {
                hBar.setOpacity(0);
                hBar.setPrefHeight(0);
                hBar.setMaxHeight(0);
                hBar.setMinHeight(0);
            }
        });
        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            double availableHeight = scrollPane.getViewportBounds().getHeight();
            resizeAllTiles(availableHeight);
        });

        scrollPane.prefWidthProperty().bind(
                Bindings.createDoubleBinding(
                        () -> itemsList.getWidth() + 10,
                        itemsList.widthProperty()
                )
        );

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        applySciFiStyleToButtons(controlsBox);
        for (Node node : controlsBox.getChildren()) {
            if (node instanceof VBox box && box.getId() != null && box.getId().startsWith("box")) {
                box.setVisible(false);
                box.setManaged(false);

                for (Node child : box.getChildren()) {
                    child.setVisible(false);
                    child.setManaged(false);
                }
            }
        }
        //disattivo tutto
        labelNextPlayer.setVisible(false);
        setGoodsButtonsEnabled(false);
        doneBtn26.setDisable(true);
        selectCannonsBtn24.setDisable(false);
        selectEnginesBtn25.setDisable(false);

        Image ivGood1img = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/mammozzetti/good_1.png"))
        );
        Image ivGood2img = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/mammozzetti/good_2.png"))
        );
        Image ivGood3img = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/mammozzetti/good_3.png"))
        );
        Image ivGood4img = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/mammozzetti/good_4.png"))
        );
        //metodo 17
        handleSelectGood1_17.setDisable(true);

        doneBtn17.setDisable(true);
        //images
        good1img17.setImage(ivGood1img);
        good2img17.setImage(ivGood2img);
        good3img17.setImage(ivGood3img);
        good4img17.setImage(ivGood4img);
        //metodo 18
        selectBatteriesBtn18.setDisable(true);
        doneBtn18.setDisable(true);
        //metodo 19
        doneBtn19.setDisable(true);
        //20
        selectPassengersBtn20.setDisable(true);
        selectPassengersBtn20.setManaged(false);
        selectPassengersBtn20.setVisible(false);
        doneBtn20.setDisable(true);  // Metodo 20
        doneBtn23.setDisable(true);
        doneBtn24.setDisable(true);
        //---25
        doneBtn25.setDisable(false);
        doneBtn25.setVisible(true);
        doneBtn25.setManaged(true);
        //----22
        doneBtn22.setDisable(true);
        handleSelectGood1_22.setDisable(true);

        //images
        ivGood1.setImage(ivGood1img);
        ivGood2.setImage(ivGood2img);
        ivGood3.setImage(ivGood3img);
        ivGood4.setImage(ivGood4img);
        //26
        //images
        good1img26.setImage(ivGood1img);
        good2img26.setImage(ivGood2img);
        good3img26.setImage(ivGood3img);
        good4img26.setImage(ivGood4img);


        currentItem.setOnDragDetected(event -> {
            Image img = currentItem.getImage();
            if (img == null) return;

            Dragboard db = currentItem.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            Integer tileId = (Integer) currentItem.getUserData();
            content.putString(tileId.toString());
            db.setContent(content);

            // Creo un ImageView temporaneo SOLO per il drag‐view, con dimensione uguale a quella di una cella
            ImageView iv = new ImageView(img);
            iv.setRotate(currentItem.getRotate());
            iv.setOpacity(0.6);
            iv.setPreserveRatio(true);

            // Calcolo dimensione “tile” attuale dalla stessa cella di riferimento usata per il binding
            // (immerge il risultato in Platform.runLater solo se serve, ma in pratica cellPanes è già pronto)
            StackPane referenceCell = cellPanes[2][3];
            double targetWidth = referenceCell.getWidth() * TILE_SCALE;
            double targetHeight = referenceCell.getHeight() * TILE_SCALE;

            iv.setFitWidth(targetWidth);
            iv.setFitHeight(targetHeight);

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            WritableImage dragImg = iv.snapshot(params, null);
            db.setDragView(dragImg, dragImg.getWidth() / 2, dragImg.getHeight() / 2);
            event.consume();
        });

        scrollPane.setOnDragOver(event -> {
            // accettiamo il drop se proviene da currentItem e c’è un “string” sul Dragboard
            if (event.getGestureSource() == currentItem && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        scrollPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                // lanciamo handleDeposit() proprio come se fosse uscito dal click del Bottone
                handleDeposit();
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        makeBoardResponsive();
        // — nuovo binding: misura currentItem in base alle celle di gridMatrix —
        Platform.runLater(() -> {
            // 1) Prendo la cella di riferimento (qui, la cella centrale: riga 2, colonna 3)
            StackPane referenceCell = cellPanes[2][3];
            // 2) Rimuovo eventuali bind precedenti (soprattutto in caso di hot-reload)
            currentItem.fitWidthProperty().unbind();
            currentItem.fitHeightProperty().unbind();

            // 3) Lego larghezza/altezza di currentItem a quella della cella di riferimento,
            //    moltiplicata per TILE_SCALE (0.96)
            currentItem.setPreserveRatio(true);
            currentItem.fitWidthProperty().bind(
                    referenceCell.widthProperty().multiply(TILE_SCALE)
            );
            currentItem.fitHeightProperty().bind(
                    referenceCell.heightProperty().multiply(TILE_SCALE)
            );
        });


// Posticipa la prima chiamata a resizeBoard() e l’installazione del listener
        Platform.runLater(() -> {
            resizeBoard();
            installBoardResizer();
        });
        installCellResizeListener();
    }


    /**
     * Sets player.
     *
     * @param nickname        the nickname
     * @param isCurrentPlayer the is current player
     * @param numberOfPlayers the number of players
     * @param id              the id
     */
    public void setPlayer(String nickname, boolean isCurrentPlayer, int numberOfPlayers, int id) {
        this.numberOfPlayers = numberOfPlayers;
        this.isCurrentPlayer = isCurrentPlayer;
        playerBanner.setText(nickname + (isCurrentPlayer ? " (YOU)" : ""));
        initializeGrid(id);
        populateItemsList(view.getLittleModelRepresentation().getVisibleTiles());
        setupPlayerComboBox();
        timerLabel.setVisible(isCurrentPlayer);
        timerLabel.setManaged(isCurrentPlayer);
        controlsBox.setVisible(isCurrentPlayer);
        controlsBox.setManaged(isCurrentPlayer);
        itemsList.setVisible(isCurrentPlayer);
        itemsList.setManaged(isCurrentPlayer);
        startTimerButton.setVisible(isCurrentPlayer);
        startTimerButton.setManaged(isCurrentPlayer);
        playerNumberComboBox.setVisible(isCurrentPlayer);
        playerNumberComboBox.setManaged(isCurrentPlayer);
        endBuildingButton.setVisible(isCurrentPlayer);
        endBuildingButton.setManaged(isCurrentPlayer);
        scrollPane.setVisible(isCurrentPlayer);
        scrollPane.setManaged(isCurrentPlayer);

        // Abilita o disabilita i pulsanti Pick in base al turno
        startTimerButton.setVisible(isCurrentPlayer);
        startTimerButton.setVisible(isCurrentPlayer);
        startTimerButton.setManaged(isCurrentPlayer);


    }

    private void setupPlayerComboBox() {
        playerNumberComboBox.getItems().clear();
        for (int i = 1; i <= numberOfPlayers; i++) {
            playerNumberComboBox.getItems().add(i);
        }

        endBuildingButton.disableProperty().bind(
                playerNumberComboBox.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    private void clearWaitCellHighlight() {
        for (Node node : gridMatrix.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);
            if (row != null && col != null && row == 0 && (col == 5 || col == 6)) {
                node.setStyle("");
            }
        }
    }


    @FXML
    private void handleStartTimer() {
        new Thread(() -> {
            try {
                if (view != null) {
                    GUI.getClient().activateTimer(GUI.getModel().getMyNickname(), GUI.getClient().getUuid());
                } else {
                    Platform.runLater(() -> showAlert("View reference is null"));
                }
            } catch (Exception ex) {
                Platform.runLater(() -> showError(ex));
            }
        }).start();
    }


    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.initModality(Modality.NONE);
        a.show();
    }

    private void showError(Exception ex) {
        showAlert(ex.getMessage());
    }

    /**
     * Handle put in wait.
     */
    @FXML
    public void handlePutInWait() {
        if (selectedWaitCol != null) {
            clearWaitCellHighlight();
            selectedWaitCol = null;
        }

        new Thread(() -> {
            try {
                if (view != null) {
                    GenericClient client = GUI.getClient();
                    int id = -1;
                    for (ShipBoard s : GUI.getModel().getShipBoards()) {
                        if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                            id = s.getMyPlayer().getPlayerId();
                            break;
                        }
                    }
                    GUI.getClient().addWaitTile(id, GUI.getClient().getUuid());
                } else {
                    Platform.runLater(() -> showAlert("View reference is null"));
                }
            } catch (Exception ex) {
                Platform.runLater(() -> showError(ex));
            }
        }).start();
    }

    @FXML
    private void handlePickTile(ActionEvent event) {
        if (selectedWaitCol != null) {
            clearWaitCellHighlight();
            selectedWaitCol = null;
        }

        new Thread(() -> {
            try {
                if (view != null) {
                    GenericClient client = GUI.getClient();
                    int id = -1;
                    for (ShipBoard s : GUI.getModel().getShipBoards()) {
                        if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                            id = s.getMyPlayer().getPlayerId();
                            break;
                        }
                    }
                    client.pickTileUnknown(id, client.getUuid());
                } else {
                    Platform.runLater(() -> showAlert("View reference is null"));
                }
            } catch (Exception ex) {
                Platform.runLater(() -> showError(ex));
            }
        }).start();
    }

    @FXML
    private void handleEndBuilding() throws Exception {
        if (selectedWaitCol != null) {
            clearWaitCellHighlight();
            selectedWaitCol = null;
        }

        int selectedPos = playerNumberComboBox.getValue();
        GenericClient client = GUI.getClient();
        int id = -1;
        for (ShipBoard s : GUI.getModel().getShipBoards()) {
            if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                id = s.getMyPlayer().getPlayerId();
                break;
            }
        }
        client.endbuilding(id, selectedPos, client.getUuid());
    }

    private int draggedWaitIndex = -1;
    private double draggedWaitRotation = 0;

    private void initializeGrid(int id) {
        gridMatrix.getRowConstraints().clear();
        prepareGridSkeleton();
        cellPanes = new StackPane[5][7];

        // Calcolo del centro
        int centerRow = 5 / 2;   // = 2
        int centerCol = 7 / 2;   // = 3

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                StackPane cellContainer = new StackPane();
                cellContainer.setPadding(new Insets(0));
                cellContainer.setMinSize(40, 40);
                cellContainer.setPrefSize(60, 60);
                Image img = null;
                if (row == centerRow && col == centerCol) {
                    try {
                        img = new Image(Objects.requireNonNull(
                                getClass().getResourceAsStream(
                                        "/it/polimi/ingsw/graphics/tiles/tileId_" + id + ".jpg")));
                    } catch (NullPointerException ex) {
                        System.err.println("[initializeGrid] tileId_" + id + ".jpg non trovato");
                    }
                }
                ImageView iv = newTileImage(img, cellContainer);

                cellContainer.getChildren().add(iv);
                StackPane.setAlignment(iv, Pos.CENTER);
                gridMatrix.add(cellContainer, col, row);
                cellPanes[row][col] = cellContainer;

                final int r = row;
                final int c = col;

                // 1) Click sulla cella (stesso di prima)
                cellContainer.setOnMouseClicked(e -> {
                    if (r == 0 && (c == 5 || c == 6) && currentItem.getImage() == null) {
                        if (selectedWaitCol != null && selectedWaitCol != c) {
                            clearWaitCellHighlight();
                        }
                        selectedWaitCol = c;
                        cellContainer.setStyle("-fx-border-color: yellow; -fx-border-width: 2;");
                        return;
                    }
                    if (selectedWaitCol != null) {
                        clearWaitCellHighlight();
                        selectedWaitCol = null;
                    }
                    System.out.println("Clicked on cell: " + r + ", " + c);
                    if (insertMode) {
                        new Thread(() -> {
                            if (view != null) {
                                try {
                                    GenericClient client = GUI.getClient();
                                    int id1 = -1;
                                    for (ShipBoard s : GUI.getModel().getShipBoards()) {
                                        if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                                            id1 = s.getMyPlayer().getPlayerId();
                                            break;
                                        }
                                    }
                                    client.insertTile(r, c, id1, (int) currentItem.getRotate(), client.getUuid());
                                    Platform.runLater(() -> {
                                        insertMode = false;
                                        gridMatrix.setCursor(Cursor.DEFAULT);
                                    });
                                } catch (Exception ex) {
                                    Platform.runLater(() -> showError(ex));
                                }
                            } else {
                                Platform.runLater(() ->
                                        showAlert("View reference is null")
                                );
                            }
                        }).start();
                        insertMode = false;
                    }

                    // === MODALITÀ: VBOX 18 - Cannoni ===
                    if (selectingCannons) {
                        // gridRow è 5..9, gridCol 4..10: porto a 0-based interno 0..4,0..6
                        selectedCannons.add(List.of(r, c));
                        highlightCell(cellContainer);
                        Platform.runLater(() -> selectBatteriesBtn18.setDisable(false));
                        return;
                    }
                    if (selectingBatteriesForCannons) {
                        // il “1” rimane se serve per conteggio batterie
                        //selectedBatteriesForCannons.add(List.of(r, c, 1));
                        boolean fatto = false;
                        for (List<Integer> triplette : selectedBatteriesForCannons) {
                            if (triplette.get(0) == r && triplette.get(1) == c) {
                                fatto = true;
                                triplette.set(2, triplette.get(2) + 1);  // OK perché triplette è un ArrayList
                                break;
                            }
                        }
                        if (!fatto) {
                            ArrayList<Integer> newTriple = new ArrayList<>(List.of(r, c, 1));
                            selectedBatteriesForCannons.add(newTriple);
                        }
                        highlightCell(cellContainer);
                        return;
                    }

                    // === MODALITÀ: VBOX 19 - Difesa dai meteoriti ===
                    if (selectingMeteorDefenses) {

                        selectedMeteorDefenses.add(r);
                        selectedMeteorDefenses.add(c);
                        highlightCell(cellContainer);
                        Platform.runLater(() -> doneBtn19.setDisable(false));
                        return;
                    }

                    // === MODALITÀ: VBOX 20 - Passeggeri da perdere ===
                    if (selectingPassengersToLose) {
                        // porto gridRow 5..9 → 0..4 e gridCol 4..10 → 0..6
                        boolean fatto = false;
                        for (List<Integer> triplette : selectedPassengersToLose) {
                            if (triplette.get(0) == r && triplette.get(1) == c) {
                                fatto = true;
                                triplette.set(2, triplette.get(2) + 1);  // OK perché triplette è un ArrayList
                                break;
                            }
                        }
                        if (!fatto) {
                            ArrayList<Integer> newTriple = new ArrayList<>(List.of(r, c, 1));
                            selectedPassengersToLose.add(newTriple);
                        }

                        highlightCell(cellContainer);
                        Platform.runLater(() -> doneBtn20.setDisable(false));
                        return;
                    }

                    // === MODALITÀ: VBOX 17 - Storage e Goods ===
                    if (selectingStorageForRewardStation && selectedGoodValue_22 != null) {
                        // porto gridRow 5..9→0..4, gridCol 4..10→0..6
                        System.out.println("r0,c0: " + r + ", " + c);
                        ArrayList<Integer> key = new ArrayList<>();
                        key.add(r);
                        key.add(c);

                        rewardGoodsPlacementMapStation
                                .computeIfAbsent(key, k -> new ArrayList<>())
                                .add(new Goods(selectedGoodValue_22, selectedGoodValue_22 == 4));
                        highlightCell(cellContainer);
                        Platform.runLater(() -> doneBtn17.setDisable(false));
                        return;
                    }

                    // === MODALITÀ: VBOX 22 - Storage e Goods ===
                    if (selectingStorageForReward && selectedGoodValue_22 != null) {
                        // porto gridRow 5..9→0..4, gridCol 4..10→0..6
                        System.out.println("r0,c0: " + r + ", " + c);
                        List<Integer> key = List.of(r, c);
                        rewardGoodsPlacementMap
                                .computeIfAbsent(key, k -> new ArrayList<>())
                                .add(selectedGoodValue_22);
                        highlightCell(cellContainer);
                        Platform.runLater(() -> doneBtn22.setDisable(false));
                        return;
                    }
                    // === MODALITÀ: VBOX 23 - Posizionamento batterie ===
                    if (selectingBatteryPlacements) {
                        // porto gridRow 5..9→0..4, gridCol 4..10→0..6
                        // il terzo valore (1) rimane per il count
                        //selectedBatteryPlacements.add(List.of(r, c, 1));
                        boolean fatto = false;
                        for (List<Integer> triplette : selectedBatteryPlacements) {
                            if (triplette.get(0) == r && triplette.get(1) == c) {
                                fatto = true;
                                triplette.set(2, triplette.get(2) + 1);  // OK perché triplette è un ArrayList
                                break;
                            }
                        }
                        if (!fatto) {
                            ArrayList<Integer> newTriple = new ArrayList<>(List.of(r, c, 1));
                            selectedBatteryPlacements.add(newTriple);
                        }
                        highlightCell(cellContainer);
                        Platform.runLater(() -> doneBtn23.setDisable(false));
                        return;
                    }
                    // === MODALITÀ: VBOX 24 - Fuoco ===
                    if (selectingFirepowerCannons) {
                        // translatione coordinate 5..9→0..4, 4..10→0..6
                        selectedFirepowerCannons.add(List.of(r, c));
                        highlightCell(cellContainer);
                        return;
                    }
                    if (selectingFirepowerBatteries) {
                        // terzo valore count=1 per ogni batteria selezionata
                        //selectedFirepowerBatteries.add(List.of(r, c, 1));
                        boolean fatto = false;
                        for (List<Integer> triplette : selectedFirepowerBatteries) {
                            if (triplette.get(0) == r && triplette.get(1) == c) {
                                fatto = true;
                                triplette.set(2, triplette.get(2) + 1);  // OK perché triplette è un ArrayList
                                break;
                            }
                        }
                        if (!fatto) {
                            ArrayList<Integer> newTriple = new ArrayList<>(List.of(r, c, 1));
                            selectedFirepowerBatteries.add(newTriple);
                        }
                        highlightCell(cellContainer);
                        Platform.runLater(() -> doneBtn24.setDisable(false));
                        return;
                    }
                    // === MODALITÀ: VBOX 25 - Motori ===
                    if (selectingEngines) {
                        // porto coord da “reali” (5–9,4–10) a interne (0–4,0–6)
                        selectedEngines.add(List.of(r, c));
                        highlightCell(cellContainer);
                        return;
                    }
                    if (selectingEngineBatteries) {
                        // il “1” indica numero di batterie in quella cella
                        //selectedEngineBatteries.add(List.of(r, c, 1));
                        boolean fatto = false;
                        for (List<Integer> triplette : selectedEngineBatteries) {
                            if (triplette.get(0) == r && triplette.get(1) == c) {
                                fatto = true;
                                triplette.set(2, triplette.get(2) + 1);  // OK perché triplette è un ArrayList
                                break;
                            }
                        }
                        if (!fatto) {
                            ArrayList<Integer> newTriple = new ArrayList<>(List.of(r, c, 1));
                            selectedEngineBatteries.add(newTriple);
                        }
                        highlightCell(cellContainer);
                        Platform.runLater(() -> doneBtn25.setDisable(false));
                        return;
                    }


                    // === MODALITÀ: VBOX 26 - Merci ===
                    if (selectingGoodsPositions && goodSelectedToPlace != null) {
                        // porto gridRow 5..9→0..4 e gridCol 4..10→0..6
                        List<Integer> key = List.of(r, c);
                        goodsPlacementMap
                                .computeIfAbsent(key, k -> new ArrayList<>())
                                .add(goodSelectedToPlace);
                        highlightCell(cellContainer);
                        Platform.runLater(() -> {
                            setGoodsButtonsEnabled(true);
                            doneBtn26.setDisable(false);
                        });
                        e.consume();
                        return;

                    }

                    if (canInsertWait1) {
                        new Thread(() -> {
                            if (view != null) {
                                double rotatione = 0;
                                // Cerca nella cella temporanea della wait tile 1 (riga 0, colonna 5)
                                for (Node node : gridMatrix.getChildren()) {
                                    Integer rov = GridPane.getRowIndex(node);
                                    Integer colm = GridPane.getColumnIndex(node);
                                    if (rov != null && colm != null && rov == 0 && colm == 5) {
                                        StackPane cell = (StackPane) node;
                                        if (!cell.getChildren().isEmpty()) {
                                            rotatione = cell.getChildren().getFirst().getRotate();
                                        }
                                        break;
                                    }
                                }
                                try {
                                    GenericClient client = GUI.getClient();
                                    int id1 = -1;
                                    for (ShipBoard s : GUI.getModel().getShipBoards()) {
                                        if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                                            id1 = s.getMyPlayer().getPlayerId();
                                            break;
                                        }
                                    }
                                    client.insertWaitTile(id1, 0, r, c, (int) rotatione, client.getUuid());
                                    Platform.runLater(() -> {
                                        canInsertWait1 = false;
                                        gridMatrix.setCursor(Cursor.DEFAULT);
                                    });
                                } catch (Exception ex) {
                                    Platform.runLater(() -> showError(ex));
                                }
                            } else {
                                Platform.runLater(() ->
                                        showAlert("View reference is null")
                                );
                            }
                        }).start();
                        canInsertWait1 = false;
                    }
                    if (canInsertWait2) {
                        new Thread(() -> {
                            if (view != null) {
                                double rotatione = 0;
                                // Cerca nella cella temporanea della wait tile 2 (riga 0, colonna 6)
                                for (Node node : gridMatrix.getChildren()) {
                                    Integer rov = GridPane.getRowIndex(node);
                                    Integer colm = GridPane.getColumnIndex(node);
                                    if (rov != null && colm != null && rov == 0 && colm == 6) {
                                        StackPane cell = (StackPane) node;
                                        if (!cell.getChildren().isEmpty()) {
                                            rotatione = cell.getChildren().getFirst().getRotate();
                                        }
                                        break;
                                    }
                                }
                                try {
                                    GenericClient client = GUI.getClient();
                                    int id1 = -1;
                                    for (ShipBoard s : GUI.getModel().getShipBoards()) {
                                        if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                                            id1 = s.getMyPlayer().getPlayerId();
                                            break;
                                        }
                                    }
                                    client.insertWaitTile(id1, 1, r, c, (int) rotatione, client.getUuid());
                                    Platform.runLater(() -> {
                                        canInsertWait2 = false;
                                        gridMatrix.setCursor(Cursor.DEFAULT);
                                    });
                                } catch (Exception ex) {
                                    Platform.runLater(() -> showError(ex));
                                }
                            } else {
                                Platform.runLater(() ->
                                        showAlert("View reference is null")
                                );
                            }
                        }).start();
                        canInsertWait2 = false;
                    }

                    // === MODALITÀ varie (18,19,20,17,22,23,24,25,26) ===
                    // ... (stesso codice di selezione e highlight di prima) ...
                    // basta non toccare il drag‐drop qui, perché lo gestiamo dopo.

                });

                // 2) Nuovo DragDetected per le wait‐tile in (0,5) e (0,6):
                cellContainer.setOnDragDetected(event -> {
                    if (r == 0 && (c == 5 || c == 6)) {
                        if (!cellContainer.getChildren().isEmpty() && cellContainer.getChildren().getFirst() instanceof ImageView waitIv) {
                            // Salvo l’indice e la rotazione corrente:
                            draggedWaitIndex = (c == 5) ? 0 : 1;
                            draggedWaitRotation = waitIv.getRotate();

                            Dragboard db = cellContainer.startDragAndDrop(TransferMode.MOVE);
                            ClipboardContent content = new ClipboardContent();
                            content.putString((c == 5) ? "WAIT1" : "WAIT2");
                            db.setContent(content);

                            SnapshotParameters params = new SnapshotParameters();
                            params.setFill(Color.TRANSPARENT);
                            WritableImage snapshot = waitIv.snapshot(params, null);
                            db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);

                            event.consume();
                        }
                    }
                });

                // 3) Unico DragOver (accetta sia wait‐tile sia currentItem)
                cellContainer.setOnDragOver(ev -> {
                    Dragboard db = ev.getDragboard();
                    // se il drag proviene da una “wait‐tile” (stringa “WAIT1” o “WAIT2”)
                    if (ev.getGestureSource() != cellContainer
                            && db.hasString()
                            && (db.getString().equals("WAIT1") || db.getString().equals("WAIT2"))
                    ) {
                        ev.acceptTransferModes(TransferMode.MOVE);
                    }
                    // oppure se proviene da currentItem (qualsiasi stringa non WAIT1/WAIT2)
                    else if (ev.getGestureSource() == currentItem && db.hasString()) {
                        ev.acceptTransferModes(TransferMode.MOVE);
                    }
                    ev.consume();
                });

                // 4) Unico DragDropped: distingue WAIT1/WAIT2 vs. currentItem
                cellContainer.setOnDragDropped(ev -> {
                    Dragboard db = ev.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {
                        String tag = db.getString();
                        int gridRow = GridPane.getRowIndex(cellContainer);
                        int gridCol = GridPane.getColumnIndex(cellContainer);

                        if (tag.equals("WAIT1") || tag.equals("WAIT2")) {
                            // se rilascio FUORI da (0,5) o (0,6) devo inviare la insertWaitTile
                            if (!(gridRow == 0 && (gridCol == 5 || gridCol == 6))) {

                                /*--- FIX: copia lo stato PRIMA di lanciare il thread ---*/
                                final int waitIdxCopy = draggedWaitIndex;
                                final double rotCopy = draggedWaitRotation;
                                final int targetRow = gridRow;
                                final int targetCol = gridCol;

                                new Thread(() -> {
                                    try {
                                        GenericClient client = GUI.getClient();
                                        int myId = GUI.getModel().getShipBoards().stream()
                                                .filter(s -> s.getMyPlayer().getUsername()
                                                        .equals(GUI.getModel().getMyNickname()))
                                                .findFirst().get()
                                                .getMyPlayer().getPlayerId();

                                        client.insertWaitTile(
                                                myId,
                                                waitIdxCopy,            // INDICE CORRETTO (0 o 1)
                                                targetRow,
                                                targetCol,
                                                (int) rotCopy,
                                                client.getUuid()
                                        );
                                    } catch (Exception ex) {
                                        Platform.runLater(() -> showError(ex));
                                    }
                                }).start();
                            }

                            /*--- reset DOPO aver copiato i valori ---*/
                            draggedWaitIndex = -1;
                            draggedWaitRotation = 0;
                            if (selectedWaitCol != null) {
                                clearWaitCellHighlight();
                                selectedWaitCol = null;
                            }
                            success = true;
                        } else {
                            // assume che sia un drop di currentItem
                            if ((gridRow == 0 && gridCol == 5) || (gridRow == 0 && gridCol == 6)) {
                                Platform.runLater(this::handlePutInWait);
                            } else {
                                new Thread(() -> {
                                    try {
                                        GenericClient client = GUI.getClient();
                                        client.insertTile(
                                                gridRow,
                                                gridCol,
                                                GUI.getModel().getShipBoards().stream()
                                                        .filter(s -> s.getMyPlayer().getUsername()
                                                                .equals(GUI.getModel().getMyNickname()))
                                                        .findFirst().get().getMyPlayer().getPlayerId(),
                                                (int) currentItem.getRotate(),
                                                client.getUuid()
                                        );
                                    } catch (Exception ex) {
                                        Platform.runLater(() -> showError(ex));
                                    }
                                }).start();
                            }
                            success = true;
                        }
                    }
                    ev.setDropCompleted(success);
                    ev.consume();
                });

                // 5) opzionale: alla fine del drag
                cellContainer.setOnDragDone(Event::consume);
            }
        }
    }


    /**
     * Sovrappone un'icona piccola su (r,c).
     *
     * @param img      l'immagine da sovrapporre
     * @param r        riga 0-based
     * @param c        colonna 0-based
     * @param widthPct larghezza in % della cella (es. 0.3 = 30%)
     * @param xPct     orizzontale in % (0=sinistra,1=destra)
     * @param yPct     verticale in % (0=top,1=bottom)
     */
    private void addOverlay(Image img, int r, int c,
                            double widthPct,
                            double xPct,
                            double yPct) {
        StackPane cell = cellPanes[r][c];
        double cw = cell.getPrefWidth(), ch = cell.getPrefHeight();
        ImageView iv = new ImageView(img);
        iv.setPreserveRatio(true);

        // Sostituiamo il binding su larghezza con uno su altezza:
        iv.fitHeightProperty().bind(cell.heightProperty().multiply(0.45));
        iv.fitWidthProperty().bind(iv.fitHeightProperty());

        // Posiziona l'overlay in base ai parametri xPct, yPct
        iv.setLayoutX((cw - iv.getFitWidth()) * xPct);
        iv.setLayoutY((ch - iv.getFitHeight()) * yPct);

        cell.getChildren().add(iv);
    }


    @Override
    public void updateTime() {
        System.out.println("[Controller] updateTime called:  " + view.getLittleModelRepresentation().getHourglassResting());
        if (!isCurrentPlayer) return;

        int seconds = view.getLittleModelRepresentation().getHourglassResting();
        Platform.runLater(() -> {
            if (timerTimeline != null) timerTimeline.stop();
            remainingTime = seconds;
            timerLabel.setText("Time left: " + remainingTime);
            timerLabel.setVisible(true);
            timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                remainingTime--;
                timerLabel.setText(String.valueOf(remainingTime));
                if (remainingTime <= 0) {
                    timerTimeline.stop();
                    System.out.println("[Controller] Timer finished");
                }
            }));
            timerTimeline.setCycleCount(seconds);
            timerTimeline.play();
        });
    }

    private ImageView newTileImage(Image img, StackPane cell) {
        ImageView iv = new ImageView(img);
        iv.setPreserveRatio(true);

        // Set maximum bounds to prevent overflow
        iv.setFitWidth(60); // Set a reasonable default size
        iv.setFitHeight(60);

        // Bind to cell size but with proper constraints
        iv.fitWidthProperty().bind(
                Bindings.min(
                        cell.widthProperty().multiply(TILE_SCALE),
                        cell.heightProperty().multiply(TILE_SCALE)
                )
        );
        iv.fitHeightProperty().bind(iv.fitWidthProperty()); // Keep square aspect ratio

        return iv;
    }


    private void highlightCell(StackPane cell) {
        if (cell != null) {
            cell.setStyle("-fx-border-color: yellow; -fx-border-width: 2;");
        }
    }

    private void resetSelections() {
        selectedCannons.clear();
        selectedBatteriesForCannons.clear();
        selectedMeteorDefenses.clear();
        selectedPassengersToLose.clear();
        rewardGoodsPlacementMap.clear();
        selectedGoodValue_22 = null;
        selectedBatteryPlacements.clear();
        selectedFirepowerCannons.clear();
        selectedFirepowerBatteries.clear();
        selectedEngines.clear();
        selectedEngineBatteries.clear();
        goodsPlacementMap.clear();
        goodSelectedToPlace = null;
        selectedEngineBatteries.clear();
        goodsPlacementMap.clear();
        rewardGoodsPlacementMapStation.clear();
        goodSelectedToPlace = null;


        selectingCannons = false;
        selectingBatteriesForCannons = false;
        selectingMeteorDefenses = false;
        selectingPassengersToLose = false;
        selectingStorageForReward = false;
        selectingStorageForRewardStation = false;
        selectingBatteryPlacements = false;
        selectingFirepowerCannons = false;
        selectingFirepowerBatteries = false;
        selectingEngines = false;
        selectingEngineBatteries = false;
        selectingGoodsPositions = false;
        boolean selectingGoodsSets = false;

        // rimuovi evidenziamenti
        for (Node node : gridMatrix.getChildren()) {
            node.setStyle("");  // reset stile
        }
        gridMatrix.setCursor(Cursor.DEFAULT);
    }

    private void makeBoardResponsive() {
        gridMatrix.prefWidthProperty().bind(boardBackground.fitWidthProperty().multiply(GRID_SCALE));
        gridMatrix.prefHeightProperty().bind(boardBackground.fitHeightProperty().multiply(GRID_SCALE));

        gridMatrix.maxWidthProperty().bind(boardBackground.fitWidthProperty().multiply(GRID_SCALE));
        gridMatrix.maxHeightProperty().bind(boardBackground.fitHeightProperty().multiply(GRID_SCALE));

        gridMatrix.minWidthProperty().bind(boardBackground.fitWidthProperty().multiply(GRID_SCALE));
        gridMatrix.minHeightProperty().bind(boardBackground.fitHeightProperty().multiply(GRID_SCALE));
    }


    @Override
    public void updateThingInHand(Player p, Object thing) throws Exception {
        if (thing != null) {
            if (thing instanceof SpaceShipTile tile) {
                try {
                    String imagePath = "/it/polimi/ingsw/graphics/tiles/tileId_" + tile.getID() + ".jpg";
                    InputStream imageStream = getClass().getResourceAsStream(imagePath);
                    if (imageStream == null) {
                        throw new FileNotFoundException("Image not found: " + imagePath);
                    }
                    Image tileImage = new Image(imageStream);
                    Platform.runLater(() -> {
                        currentItem.setImage(tileImage);
                        currentItem.setUserData(tile.getID());

                    });
                } catch (Exception e) {
                    System.err.println("Error loading tile image: " + e.getMessage());
                    throw e;
                }
            } else {
                //
            }
        } else {
            if (currentItem != null) {
                Platform.runLater(() -> {
                    currentItem.setImage(null);
                    currentItem.setRotate(0);
                });
            }
        }
    }

    private void populateItemsList(List<SpaceShipTile> tiles) {
        itemsList.getChildren().clear();
        for (SpaceShipTile tile : tiles) {
            ImageView iv = new ImageView(
                    new Image(Objects.requireNonNull(
                            getClass().getResourceAsStream("/it/polimi/ingsw/graphics/tiles/tileId_" + tile.getID() + ".jpg")
                    ))
            );
            iv.setPreserveRatio(true);
            iv.fitWidthProperty().bind(currentItem.fitWidthProperty());
            iv.fitHeightProperty().bind(currentItem.fitHeightProperty());
            iv.setPreserveRatio(true);


            iv.setId("tileId_" + tile.getID());
            iv.setOnMouseClicked(e -> {
                if (currentItem.getImage() != null) return;
                try {
                    GenericClient client = GUI.getClient();
                    int myId = GUI.getModel().getShipBoards().stream()
                            .filter(sb -> sb.getMyPlayer().getUsername()
                                    .equals(GUI.getModel().getMyNickname()))
                            .findFirst().get().getMyPlayer().getPlayerId();

                    client.pickTileAlreadyFlipped(tile.getID(), myId, client.getUuid());
                } catch (Exception ex) {
                    Platform.runLater(() -> showError(ex));
                }
            });

            itemsList.getChildren().add(iv);
        }
    }


    /* costanti già presenti */
    private static final int COLS = 7;
    private static final int ROWS = 5;

    private void prepareGridSkeleton() {
        gridMatrix.getRowConstraints().clear();
        gridMatrix.getColumnConstraints().clear();

        // Create equal row constraints
        for (int i = 0; i < 5; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(20); // 100/5 = 20%
            row.setVgrow(Priority.ALWAYS);
            gridMatrix.getRowConstraints().add(row);
        }

        // Create equal column constraints
        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 7); // ~14.28%
            col.setHgrow(Priority.ALWAYS);
            gridMatrix.getColumnConstraints().add(col);
        }
    }

    private void installBoardResizer() {
        ChangeListener<Number> l = (obs, o, n) -> resizeBoard();
        centerStackPane.widthProperty().addListener(l);
        centerStackPane.heightProperty().addListener(l);
        resizeBoard(); // Chiamata iniziale
    }


    private void resizeBoard() {
        // Get the available space in the center StackPane
        double availableWidth = centerStackPane.getWidth();
        double availableHeight = centerStackPane.getHeight();

        if (availableWidth <= 0 || availableHeight <= 0) {
            return; // Not ready yet
        }

        // Calculate the size maintaining aspect ratio
        // Assuming a standard board aspect ratio (you may need to adjust)
        double boardAspectRatio = 7.0 / 5.0; // 7 columns / 5 rows

        double targetWidth, targetHeight;

        if (availableWidth / availableHeight > boardAspectRatio) {
            // Height is the limiting factor
            targetHeight = availableHeight * 0.9; // Leave some margin
            targetWidth = targetHeight * boardAspectRatio;
        } else {
            // Width is the limiting factor
            targetWidth = availableWidth * 0.9; // Leave some margin
            targetHeight = targetWidth / boardAspectRatio;
        }

        // Set the background image size
        boardBackground.setFitWidth(targetWidth);
        boardBackground.setFitHeight(targetHeight);
    }

    private void updateGridConstraints(double gridW, double gridH) {
        gridMatrix.getColumnConstraints().clear();
        gridMatrix.getRowConstraints().clear();

        double colWidth = gridW / COLS;
        double rowHeight = gridH / ROWS;

        for (int c = 0; c < COLS; c++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPrefWidth(colWidth);
            gridMatrix.getColumnConstraints().add(cc);
        }

        for (int r = 0; r < ROWS; r++) {
            RowConstraints rc = new RowConstraints();
            rc.setPrefHeight(rowHeight);
            gridMatrix.getRowConstraints().add(rc);
        }
    }


    @FXML
    private void handleRotate() {
        if (selectedWaitCol != null) {
            int targetCol = selectedWaitCol;
            // scorro la grid per trovare la cella (0, targetCol)
            for (Node node : gridMatrix.getChildren()) {
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);
                if (row != null && col != null && row == 0 && col == targetCol) {
                    StackPane cell = (StackPane) node;
                    if (!cell.getChildren().isEmpty() && cell.getChildren().getFirst() instanceof ImageView iv) {
                        iv.setRotate(iv.getRotate() + 90);
                    }
                    break;
                }
            }
            // Non annullo la selezione qui, così l'utente può premere Rotate più volte consecutive
        } else {
            // se non c’è alcuna wait‐cell selezionata, ruota il currentItem come prima
            currentItem.setRotate(currentItem.getRotate() + 90);
        }
    }


    @FXML
    private void handleInsertTile() {
        insertMode = true;
        gridMatrix.setCursor(Cursor.HAND);
        System.out.println("Insert mode activated");
    }

    @FXML
    private void handleDeposit() {
        if (selectedWaitCol != null) {
            clearWaitCellHighlight();
            selectedWaitCol = null;
        }

        new Thread(() -> {
            try {
                if (view != null) {
                    GenericClient client = GUI.getClient();
                    int id1 = -1;
                    for (ShipBoard s : GUI.getModel().getShipBoards()) {
                        if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                            id1 = s.getMyPlayer().getPlayerId();
                            break;
                        }
                    }
                    client.depositTile(id1, client.getUuid());
                } else {
                    Platform.runLater(() -> showAlert("View reference is null"));
                }
            } catch (Exception ex) {
                Platform.runLater(() -> showError(ex));
            }
        }).start();
    }

    @Override
    public void addTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        Platform.runLater(() -> {
            currentItem.setImage(null);
            currentItem.setRotate(0);
        });
        // 1) Prendi la rotazione dalla tile
        int rotation = tile.getRotation();  // supponendo che getRotation() restituisca l'angolo in gradi

        // 3a) Carica l’immagine del tile
        Image img = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/tiles/tileId_" + tile.getID() + ".jpg"))
        );

        // 3b) Trova il StackPane corrispondente nella GridPane
        for (Node node : gridMatrix.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (r != null && c != null && r == row && c == col) {
                StackPane cell = (StackPane) node;

                // Pulisci eventuali children precedenti
                cell.getChildren().clear();

                ImageView iv = newTileImage(img, cellPanes[r][c]);
                iv.setRotate(rotation);


                // Aggiungi alla cella
                cell.getChildren().add(iv);
                break;
            }
        }
    }


    @Override
    public LittleModelRepresentation getLittleModelRepresentation() {
        return view.getLittleModelRepresentation();
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
        endBuildingButton.disableProperty().unbind();
        endBuildingButton.setDisable(true);
        playerNumberComboBox.setDisable(true);
    }

    @Override
    public void removeBLock(ShipBoard s, ArrayList<SpaceShipTile> block) throws Exception {
        for (SpaceShipTile tile : block) {
            for (int i = 0; i < s.getShipMatrix().length; i++) {
                for (int j = 0; j < s.getShipMatrix()[i].length; j++) {
                    if (tile.getID() == s.getShipMatrix()[i][j].getID() || s.getShipMatrix()[i][j].getID() == 0 || s.getShipMatrix()[i][j].getID() == -1) {
                        for (Node node : gridMatrix.getChildren()) {
                            Integer row = GridPane.getRowIndex(node);
                            Integer col = GridPane.getColumnIndex(node);
                            if (i == row && j == col) {
                                // accedi direttamente al StackPane corrispondente
                                StackPane cell = cellPanes[i][j];
                                // rimuovi graficamente tutto nella UI
                                Platform.runLater(cell.getChildren()::clear);
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public void updateGoods(ShipBoard s,
                            ArrayList<ArrayList<Integer>> storageTiles,
                            ArrayList<ArrayList<Goods>> newGoods) throws Exception {
        if (storageTiles == null || newGoods == null) return;

        Platform.runLater(() -> {
            for (StackPane[] row : cellPanes)
                for (StackPane cell : row)
                    cell.getChildren().removeIf(n -> "good".equals(n.getProperties().get("type")));
            for (int idx = 0; idx < storageTiles.size(); idx++) {
                int r = storageTiles.get(idx).get(0);
                int c = storageTiles.get(idx).get(1);
                List<Goods> goodsList = newGoods.get(idx);
                StackPane cell = cellPanes[r][c];
                for (int i = 0; i < goodsList.size(); i++) {
                    Goods g = goodsList.get(i);
                    String path = "/it/polimi/ingsw/graphics/mammozzetti/good_" + g.getValue() + ".png";
                    Image icon = new Image(
                            Objects.requireNonNull(getClass().getResourceAsStream(path))
                    );
                    ImageView iv = new ImageView(icon);
                    iv.getProperties().put("type", "good");
                    iv.setPreserveRatio(true);

                    // scala sempre al 45%
                    iv.fitHeightProperty().bind(cell.heightProperty().multiply(0.45));
                    iv.fitWidthProperty().bind(iv.fitHeightProperty());

                    Pos align;
                    int count = goodsList.size();
                    if (count == 1) {
                        align = Pos.BOTTOM_CENTER;
                    } else if (count == 2) {
                        align = (i == 0) ? Pos.BOTTOM_LEFT : Pos.BOTTOM_RIGHT;
                    } else if (count == 3) {
                        // tre: alto-centro, basso-sinistra, basso-destra
                        align = switch (i) {
                            case 0 -> Pos.TOP_CENTER;
                            case 1 -> Pos.BOTTOM_LEFT;
                            case 2 -> Pos.BOTTOM_RIGHT;
                            default -> Pos.CENTER;
                        };
                    } else {
                        // quattro: i quattro angoli
                        align = switch (i) {
                            case 0 -> Pos.TOP_LEFT;
                            case 1 -> Pos.TOP_RIGHT;
                            case 2 -> Pos.BOTTOM_LEFT;
                            case 3 -> Pos.BOTTOM_RIGHT;
                            default -> Pos.CENTER;
                        };
                    }
                    StackPane.setAlignment(iv, align);
                    StackPane.setMargin(iv, new Insets(4));

                    cell.getChildren().add(iv);
                }
            }
        });
    }


    @Override
    public void updateBatteries(ArrayList<ArrayList<Integer>> battPositions, ShipBoard shipBoard, boolean toLoose) throws Exception {
        if (battPositions == null) {
            System.err.println("[updateBatteries] Warning: battPositions is null");
            return;
        }
        Platform.runLater(() -> {
            if (toLoose) {
                for (List<Integer> pos : battPositions) {
                    int row = pos.get(0);
                    int col = pos.get(1);
                    int count = pos.get(2);

                    StackPane cell = cellPanes[row][col];
                    for (int i = 0; i < count; i++) {
                        for (Node n : cell.getChildren()) {
                            Object tag = n.getProperties().get("type");
                            if ("battery".equals(tag)) {
                                cell.getChildren().remove(n);
                                break;
                            }
                        }
                    }

                }
            } else {
                Image batteryIcon = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/it/polimi/ingsw/graphics/mammozzetti/battery.png")
                ));

                for (List<Integer> pos : battPositions) {
                    int row = pos.get(0);
                    int col = pos.get(1);
                    int count = pos.get(2);

                    StackPane cell = cellPanes[row][col];

                    // 1) Rimuovo tutte le batterie già presenti
                    cell.getChildren().removeIf(node ->
                            "battery".equals(node.getProperties().get("type"))
                    );

                    // 2) Aggiungo fino a 3 batterie, usando Pos per distribuirle
                    for (int i = 0; i < count && i < 3; i++) {
                        ImageView iv = new ImageView(batteryIcon);
                        // scala a 20% della cella
                        iv.setPreserveRatio(true);
                        iv.fitHeightProperty().bind(cell.heightProperty().multiply(0.45));
                        iv.fitWidthProperty().bind(iv.fitHeightProperty());

                        iv.getProperties().put("type", "battery");
                        cell.getChildren().add(iv);

                        // allineamenti: sinistra, centro, destra
                        Pos align = switch (i) {
                            case 0 -> Pos.CENTER_LEFT;
                            case 1 -> Pos.CENTER;
                            default -> Pos.CENTER_RIGHT;
                        };
                        StackPane.setAlignment(iv, align);
                        StackPane.setMargin(iv, new Insets(5));
                    }
                }
            }
        });
    }

    private void installCellResizeListener() {
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> {
            for (StackPane[] row : cellPanes) {
                for (StackPane cell : row) {
                    // Contiamo quanti "passenger" ci sono nella cella,
                    // per decidere se usarne il 45% (quando sono 2) o il 90% (quando è 1)
                    long passengerCount = cell.getChildren().stream()
                            .filter(node -> "passenger".equals(node.getProperties().get("type")))
                            .count();

                    for (Node child : cell.getChildren()) {
                        if (!(child instanceof ImageView iv)) continue;
                        Object typeObj = iv.getProperties().get("type");
                        if (typeObj == null) continue;
                        String type = typeObj.toString();

                        switch (type) {
                            case "good":
                                // le merci occupano il 90% dell'altezza
                                iv.fitHeightProperty().bind(cell.heightProperty().multiply(0.45));
                                iv.fitWidthProperty().bind(iv.fitHeightProperty());
                                break;

                            case "battery":
                                // le batterie occupano il 90% dell'altezza
                                iv.fitHeightProperty().bind(cell.heightProperty().multiply(0.45));
                                iv.fitWidthProperty().bind(iv.fitHeightProperty());
                                break;

                            case "passenger":
                                // se ci sono due passeggeri, ciascuno occupa il 45% dell'altezza,
                                // altrimenti solo uno occupa il 90%
                                iv.fitHeightProperty().bind(cell.heightProperty().multiply(0.45));
                                iv.fitWidthProperty().bind(iv.fitHeightProperty());
                                break;

                            case "alien":
                                // ogni alieno occupa il 90% dell'altezza
                                // (in addAlien viene chiamato addOverlay senza impostare type,
                                // quindi verificate di aver aggiunto anche iv.getProperties().put("type", "alien")
                                // dentro addOverlay o addAlien) :contentReference[oaicite:0]{index=0}
                                iv.fitWidthProperty().bind(iv.fitHeightProperty());
                                break;

                            // Se in futuro aggiungete altri overlay (es. "ship", "meteor", ecc.),
                            // create ulteriori case analoghi.
                            default:
                                break;
                        }
                    }
                }
            }
        };

        // Registriamo il listener sia sulla larghezza che sull'altezza di gridMatrix
        gridMatrix.widthProperty().addListener(sizeListener);
        gridMatrix.heightProperty().addListener(sizeListener);
    }

    @Override
    public void removePassengers(ShipBoard s, ArrayList<ArrayList<Integer>> tiles, int ignored) throws Exception {
        // 'ignored' non serve più: ogni pos è [r, c, toRemove]
        Platform.runLater(() -> {
            for (List<Integer> pos : tiles) {
                int r = pos.get(0), c = pos.get(1);
                int toRemove = pos.get(2);   // prendo qui quanti passeggeri togliere
                StackPane cell = cellPanes[r][c];
                Iterator<Node> it = cell.getChildren().iterator();
                cell.getChildren().removeIf(n -> "alien".equals(n.getProperties().get("type")));
                int removed = 0;
                while (it.hasNext() && removed < toRemove) {
                    Node n = it.next();
                    if ("passenger".equals(n.getProperties().get("type"))) {
                        it.remove();
                        removed++;
                    }

                }
            }
        });
    }


    @Override
    public void removePassengersFromTile(ShipBoard s, SpaceShipTile tile, int numPass) throws Exception {
        Platform.runLater(() -> {
            // 1) Cerchiamo la posizione (r,c) nel model confrontando gli ID dei tile
            SpaceShipTile[][] matrix = s.getShipMatrix();
            int targetR = -1, targetC = -1;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    SpaceShipTile t = matrix[i][j];
                    if (t != null && t.getID() == tile.getID()) {
                        targetR = i;
                        targetC = j;
                        break;
                    }
                }
                if (targetR >= 0) break;
            }
            if (targetR < 0) return; // non trovato, esco

            // 2) Andiamo a rimuovere fino a numPass immagini "passenger" da cellPanes[targetR][targetC]
            StackPane cell = cellPanes[targetR][targetC];
            Iterator<Node> it = cell.getChildren().iterator();
            int removed = 0;
            while (it.hasNext() && removed < numPass) {
                Node n = it.next();
                if ("passenger".equals(n.getProperties().get("type"))) {
                    it.remove();
                    removed++;
                }
                if ("alien".equals(n.getProperties().get("type"))) {
                    it.remove();
                    removed++;
                }
            }
        });
    }

    @Override
    public void removeAlienFromTile(ShipBoard s, SpaceShipTile tile) throws Exception {
        Platform.runLater(() -> {
            // 1. Trova la posizione (r, c) della tile nel modello
            SpaceShipTile[][] matrix = s.getShipMatrix();
            int targetRow = -1, targetCol = -1;
            outer:
            for (int r = 0; r < matrix.length; r++) {
                for (int c = 0; c < matrix[r].length; c++) {
                    if (matrix[r][c] != null && matrix[r][c].getID() == tile.getID()) {
                        targetRow = r;
                        targetCol = c;
                        break outer;  // esci dai due for
                    }
                }
            }
            if (targetRow >= 0) {
                clearOverlays(targetRow, targetCol);
            }
        });
    }


    // Metodo addTileFlipped aggiornato con binding alle dimensioni di currentItem
    public void addTileFlipped(int tileid) {
        Platform.runLater(() -> {
            try {
                Image tileImage = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/it/polimi/ingsw/graphics/tiles/tileId_" + tileid + ".jpg"
                        )
                ));
                ImageView imageView = new ImageView(tileImage);

                // 1) Mantengo il rapporto d'aspetto
                imageView.setPreserveRatio(true);

                // 2) Llego fitWidth di questa ImageView a itemsList.widthProperty() - 10 (se il VBox ha 5px padding per lato)
                imageView.fitWidthProperty().bind(
                        currentItem.fitWidthProperty()
                );
                imageView.setId("tileId_" + tileid);    // ← qui imposto l’ID
                // 3) Altezza = larghezza per mantenere la forma quadrata
                imageView.fitHeightProperty().bind(currentItem.fitHeightProperty());

                // 4) Aggiungo l'immagine alla lista
                itemsList.getChildren().add(imageView);
                // **ATTACCO IL LISTENER**
                imageView.setOnMouseClicked(e -> {
                    if (currentItem.getImage() != null) return;

                    try {
                        GenericClient client = GUI.getClient();
                        int myId = GUI.getModel().getShipBoards().stream()
                                .filter(sb -> sb.getMyPlayer().getUsername()
                                        .equals(GUI.getModel().getMyNickname()))
                                .findFirst().get().getMyPlayer().getPlayerId();

                        client.pickTileAlreadyFlipped(tileid, myId, client.getUuid());
                    } catch (Exception ex) {
                        Platform.runLater(() -> showError(ex));
                    }
                });
            } catch (NullPointerException e) {
                System.err.println("Tile image missing: ID " + tileid);
            }
        });
    }


    @Override
    public void someoneDepositedLittleDeck(Deck d, Player player) throws Exception {

    }

    @Override
    public void someonePickedLittleDeck(Deck d, Player player) throws Exception {

    }

    @Override
    public void removeTileFlipped(int t) throws Exception {
        System.out.println("sono qui");
        Platform.runLater(() -> {
            String targetId = "tileId_" + t;
            itemsList.getChildren().removeIf(node ->
                    node instanceof ImageView && targetId.equals(node.getId())
            );
        });
    }


    public void addPassenger(ShipBoard s, int r, int c) throws Exception {
        StackPane cell = cellPanes[r][c];

        // carica l’icona fuori dal runLater
        Image icon = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/it/polimi/ingsw/graphics/mammozzetti/passenger.png")
        ));

        Platform.runLater(() -> {
            // 1) conta **adesso** quanti passeggeri ci sono davvero
            long existing = cell.getChildren().stream()
                    .filter(n -> "passenger".equals(n.getProperties().get("type")))
                    .count();
            if (existing >= 2) return;

            // 2) prepara l’ImageView
            double pct = 0.3;
            double cw = cell.getWidth(), ch = cell.getHeight();
            ImageView iv = new ImageView(icon);
            iv.setPreserveRatio(true);
            iv.fitHeightProperty().bind(cell.heightProperty().multiply(0.45));
            iv.fitWidthProperty().bind(iv.fitHeightProperty());

            iv.getProperties().put("type", "passenger");

            // 3) aggiungi e allinea: primo in BOTTOM_LEFT, secondo in BOTTOM_RIGHT
            cell.getChildren().add(iv);
            Pos pos = (existing == 0 ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
            StackPane.setAlignment(iv, pos);
            StackPane.setMargin(iv, new Insets(5));  // margine uniforme
        });
    }

    private void setOpacityOnGridByImage(Image targetImage, double opacity) {
        Platform.runLater(() -> {
            for (StackPane[] cellPane : cellPanes) {
                for (StackPane cell : cellPane) {
                    for (Node node : cell.getChildren()) {
                        if (node instanceof ImageView iv) {
                            // Confronto l'Image effettivamente caricato:
                            Image img = iv.getImage();
                            if (img != null && img == targetImage) {
                                // Se è esattamente la stessa istanza di Image, cambio l'opacità:
                                iv.setOpacity(opacity);
                            }

                        }
                    }
                }
            }
        });
    }

    @Override
    public void youPayConsequences() {
        showInfoDialog("Penalty", "You pay consequences!");
    }

    @Override
    public void sendPenalty(int penalty, String type) {
        showInfoDialog("Penalty", "Your penalty is " + penalty + " " + type);
    }

    @Override
    public void addAlien(ShipBoard s, int row, int column, AlienColor alienColor) throws Exception {
        StackPane cell = cellPanes[row][column];
        String path = "/it/polimi/ingsw/graphics/mammozzetti/" + alienColor + "_alien.png";
        Image icon = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(path)));
        Platform.runLater(() -> {
            // 1) conta **adesso** quanti passeggeri ci sono davvero
            long existing = cell.getChildren().stream()
                    .filter(n -> "alien".equals(n.getProperties().get("type")))
                    .count();
            if (existing >= 1) return;

            // 2) prepara l’ImageView
            double pct = 0.3;
            double cw = cell.getWidth(), ch = cell.getHeight();
            ImageView iv = new ImageView(icon);
            iv.setPreserveRatio(true);
            iv.fitHeightProperty().bind(cell.heightProperty().multiply(0.45));
            iv.fitWidthProperty().bind(iv.fitHeightProperty());

            iv.getProperties().put("type", "passenger");

            // 3) aggiungi e allinea: primo in BOTTOM_LEFT, secondo in BOTTOM_RIGHT
            cell.getChildren().add(iv);
            Pos pos = (existing == 0 ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
            StackPane.setAlignment(iv, pos);
            StackPane.setMargin(iv, new Insets(5));  // margine uniforme
        });

        // 20% cella, in alto-centro
        //addOverlay(icon, row, column, 0.2, 0.5, 0.2);
    }

    /**
     * Rimuove tutti gli overlay (passengers, aliens, icone, ecc.)
     * lasciando intatta l'immagine di base nella cella (r,c).
     *
     * @param r the r
     * @param c the c
     */
    public void clearOverlays(int r, int c) {
        StackPane cell = cellPanes[r][c];
        Platform.runLater(() -> {
            // se ci sono più di un figlio, rimuovo tutti tranne il primo (la base)
            if (cell.getChildren().size() > 1) {
                cell.getChildren().remove(1, cell.getChildren().size());
            }
        });
    }

    /**
     * Svuota completamente la cella (r,c), rimuovendo anche l'immagine di base.
     *
     * @param r the r
     * @param c the c
     */
    public void clearCell(int r, int c) {
        StackPane cell = cellPanes[r][c];
        Platform.runLater(() -> cell.getChildren().clear());
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
        Platform.runLater(() -> {
            // per ogni cella, rimuovi tutti i nodi con proprietà "type" == "good"
            for (StackPane[] cellPane : cellPanes) {
                for (StackPane cell : cellPane) {
                    cell.getChildren().removeIf(node ->
                            "good".equals(node.getProperties().get("type"))
                    );
                }
            }
            // (opzionale) se stai tenendo in memoria una mappa dei goods, puliscila
            goodsPlacementMap.clear();
        });
    }


    @Override
    public void looseAllbatteries(ShipBoard s) throws Exception {
        Platform.runLater(() -> {
            // itera su tutte le celle e rimuovi ogni nodo con proprietà type="battery"
            for (StackPane[] cellPane : cellPanes) {
                for (StackPane cell : cellPane) {
                    cell.getChildren().removeIf(node ->
                            "battery".equals(node.getProperties().get("type"))
                    );
                }
            }
            // se mantieni in memoria qualche lista di batterie, svuotala qui:
            selectedBatteryPlacements.clear();
            selectedFirepowerBatteries.clear();
            selectedEngineBatteries.clear();
        });
    }


    @Override
    public void middleEffect() throws Exception {

    }

    @Override
    public void endedEffect() throws Exception {

    }

    @Override
    public void updateCard() throws Exception {
        Platform.runLater(() -> {
            //disabling last state button
            // 2) Appena il mouse entra in cardPane, mostro cardText:
            cardPane.setOnMouseEntered(evt -> cardText.setVisible(true));
            gridMatrix.setCursor(Cursor.DEFAULT);
            clearWaitCellHighlight();
            // 3) Quando il mouse esce da cardPane, nascondo cardText:
            cardPane.setOnMouseExited(evt -> cardText.setVisible(false));

            labelNextPlayer.setVisible(false);
            demoButton.setDisable(true);        // DEMOotate 2

            startTimerButton.setVisible(false);
            startTimerButton.setManaged(false);
            timerLabel.setVisible(false);
            demoButton.setVisible(false);
            demoButton.setManaged(false);


            pickTileButton.setVisible(false);
            pickTileButton.setManaged(false);

            rotateButton.setVisible(false);
            rotateButton.setManaged(false);

            currentItem.setImage(null);
            currentItem.setDisable(true);
            currentItem.setManaged(false);
            currentItem.setVisible(false);

            LittleModelRepresentation model = view.getLittleModelRepresentation();
            Card activatedCard = model != null ? model.getActivatedcard() : null;
            scrollPane.setVisible(false);
            if (activatedCard != null) {
                String imagePath = "/it/polimi/ingsw/graphics/cards/cardId_" + activatedCard.getId() + ".jpg";
                try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
                    if (imageStream != null) {
                        Image cardImage = new Image(imageStream);
                        ImageView imageView = new ImageView(cardImage);
                        imageView.setFitWidth(145 * 1.7);
                        imageView.setFitHeight(200 * 1.7);
                        imageView.setPreserveRatio(true);
                        cardPane.getChildren().clear();
                        cardPane.getChildren().add(imageView);
                        updatecardText();

                    } else {
                        System.err.println("Immagine non trovata: " + imagePath);
                    }
                } catch (Exception e) {
                    System.err.println("Errore nel caricamento della carta: " + e.getMessage());
                }
            } else {
                cardPane.getChildren().clear(); // Pulisci se non c'è carta
            }
            showOnlyCardRelevantButtons(activatedCard);

        });
    }

    /**
     * Apply sci fi style to buttons.
     *
     * @param parent the parent
     */
    public void applySciFiStyleToButtons(Pane parent) {
        for (Node node : parent.getChildren()) {
            if (node instanceof Button btn) {
                // aggiunge la classe di stile definita nel CSS
                btn.getStyleClass().add("sci-fi-button");
            } else if (node instanceof Pane childPane) {
                // se dentro c’è un altro contenitore, scende più in profondità
                applySciFiStyleToButtons(childPane);
            }
        }
    }

    private void updatecardText() {
        Card c = getLittleModelRepresentation().getActivatedcard();
        String msg = "";

        if (c instanceof Card_AbandonedShip) {
            msg = "Sacrifice the shown crew to claim the derelict’s credits (and lose the listed flight days). "
                    + "First willing player in flight-order takes it; others miss out.";
        } else if (c instanceof Card_AbandonedStation) {
            msg = "If you have enough crew you may dock, load the shown goods, then lose the listed flight days. "
                    + "Leader chooses first, then next players in order.";
        } else if (c instanceof Card_Combat_zone) {
            msg = "Three tests: fewest crew → -3 days, weakest engines → lose 2 crew, weakest firepower → take 1 light + 1 heavy rear shot. "
                    + "Ties go to the ship farthest ahead.";
        } else if (c instanceof Card_Epidemic) {
            msg = "In reverse order each player removes 1 crew from every occupied cabin adjacent to another occupied cabin.";
        } else if (c instanceof Card_MeteorSwarm) {
            msg = "Resolve small then large meteors from top down. Small: may shield. Large: block only with forward cannon; "
                    + "otherwise the hit component is destroyed.";
        } else if (c instanceof Card_OpenSpace) {
            msg = "Declare drive power (may spend batteries for double engines) and advance that many empty spaces, possibly overtaking.";
        } else if (c instanceof Card_Pirates) {
            msg = "Fight in flight-order. Beat their firepower to take credits (-days) or pass; lose and suffer the cannon shots shown.";
        } else if (c instanceof Card_Slavers) {
            msg = "Fight like pirates. Win → take credits (-days). Lose → hand over crew of your choice.";
        } else if (c instanceof Card_Smugglers) {
            msg = "Fight like pirates. Win → take goods (-days). Tie → nothing. Lose → discard 2 most valuable goods (or 1 battery).";
        } else if (c instanceof Card_Stardust) {
            msg = "In reverse order each player loses 1 flight day per exposed connector.";
        }

        cardText.setText(msg);
    }


    private void hideAllControlBoxes() {
        // Se controlsBox è null, esco
        if (controlsBox == null) return;

        for (Node node : controlsBox.getChildren()) {
            if (node instanceof VBox box) {
                box.setVisible(false);
                box.setManaged(false);
                box.setDisable(true);
            }
        }
        // Nascondo anche il contenitore generale (se non vuoi che resti visibile lo spazio vuoto)
        controlsBox.setVisible(false);
        controlsBox.setManaged(false);
        controlsBox.setDisable(true);
    }

    private void showOnlyCardRelevantButtons(Card card) {
        // 1) Prima di tutto, nascondo ogni VBox nel pannello
        for (Node node : controlsBox.getChildren()) {
            node.setVisible(false);
            node.setManaged(false);
            node.setDisable(true);
        }
        List<CommandType> commandIds = commandsPerCard(card); // supponendo metodo statico

        // Per ogni comando valido per questa carta…
        for (CommandType id : commandIds) {
            String targetBoxId = "box" + id;

            // Scorro tutte le VBox dentro controlsBox
            for (Node node : controlsBox.getChildren()) {
                if (node instanceof VBox box && targetBoxId.equals(box.getId())) {
                    // --- 1) Mostro la VBox “corretta” e ne abilito i figli ---
                    box.setVisible(true);
                    box.setManaged(true);
                    box.setDisable(false);

                    for (Node child : box.getChildren()) {
                        // Se il figlio è la VBox “afterChoice”, la nascondo completamente
                        if (child instanceof VBox ac && ac.getId() != null && ac.getId().startsWith("afterChoice")) {
                            ac.setVisible(false);
                            ac.setManaged(false);
                            ac.setDisable(true);

                        }// Altrimenti (Label, yes/no/… o selectStorage) lo mostro/nichilizzo in base all’iniziale
                        else {
                            child.setVisible(true);
                            child.setManaged(true);
                            child.setDisable(false);
                        }
                    }
                } else if (node instanceof VBox box && !targetBoxId.equals(box.getId())) {
                    // Tutte le altre VBox (non rilevanti) le nascondo
                    box.setVisible(false);
                    box.setManaged(false);
                    box.setDisable(true);
                }
            }
        }
        // Rende visibile anche il contenitore generale se qualcosa è stato mostrato
        controlsBox.setVisible(true);
        controlsBox.setManaged(true);
    }

    /**
     * Abilita la sezione “afterChoice” della VBox attualmente visibile in controlsBox.
     * Presuppone che showOnlyCardRelevantButtons abbia impostato esattamente una VBox visibile.
     */
    private void enableAfterChoiceForVisibleVBox() {
        for (Node node : controlsBox.getChildren()) {
            if (node instanceof VBox box && box.isVisible()) {
                // Cerco il child con fx:id="afterChoice"
                for (Node child : box.getChildren()) {
                    if (child instanceof VBox acBox
                            && acBox.getId() != null            // controllo di nullità
                            && acBox.getId().startsWith("afterChoice")) {
                        // Rendo visibile e gestibile la VBox “afterChoice”
                        acBox.setVisible(true);
                        acBox.setManaged(true);
                        acBox.setDisable(false);

                        // Abilito anche tutti i suoi figli (i bottoni “Good 1–4” e “Submit choice”)
                        for (Node grandChild : acBox.getChildren()) {
                            grandChild.setVisible(true);
                            grandChild.setManaged(true);
                            grandChild.setDisable(false);
                        }
                        return; // Ho abilitato la sezione, esco
                    }
                }
            }
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
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.setTitle("Messaggio");
            alert.setHeaderText(null);
            alert.initModality(Modality.NONE);
            alert.show();
        });
    }


    @Override
    public void updateShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {

    }

    // campi di istanza per i vari dialog
    private Dialog<Integer> subShipsDialog;
    private Dialog<Integer> wrongTilesDialog;
    private Dialog<ButtonType> fillTilesDialog;

    /**
     * Commands per card array list.
     *
     * @param card the card
     * @return the array list
     */
    public ArrayList<CommandType> commandsPerCard(Card card) {
        c_State state = card.getStateEnum();
        ArrayList<CommandType> commands = new ArrayList<>();
        switch (card) {
            case Card_OpenSpace cardOpenSpace -> {
                if (state == c_State.OPEN_SPACE_PREPARATION) {
                    return new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_START_MOTOR));
                } else if (state == c_State.OPEN_SPACE_FINAL) {
                    return new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                }
            }

            case Card_Pirates cardPirates -> {
                return switch (state) {
                    case PIRATES_CALC_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_CANNON_BATTERY_POS));
                    case PIRATES_EFFECT_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_HOW_TO_FACE_METEORS));
                    case PIRATES_WINNING_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_CLAIM_REWARD));
                    case PIRATES_END_STATE -> new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    case CHOOSE_DISC_BLOCK ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_ONE_SUB_SHIP));

                    default -> commands;
                };
            }
            case Card_Smugglers cardSmugglers -> {
                return switch (state) {
                    case SMUGGLERS_CALC_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_CANNON_BATTERY_POS));
                    case SMUGGLERS_LOST_BATTERIES_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_PLACE_BATTERIES));
                    case SMUGGLERS_LOST_GOODS_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_WHERE_TO_PUT_GOODS));
                    case SMUGGLERS_WINNING_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_CLAIM_REWARD_WITH_GOODS));
                    case SMUGGLERS_END_STATE -> new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                    default -> commands;
                };
            }
            case Card_Slavers cardSlavers -> {
                return switch (state) {
                    case SLAVERS_CALC_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_CANNON_BATTERY_POS));
                    case SLAVERS_LOSING_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_PASSENGERS_TO_LOSE));
                    case SLAVERS_WINNING_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_CLAIM_REWARD));
                    case SLAVERS_END_STATE -> new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                    default -> commands;
                };
            }
            case Card_MeteorSwarm cardMeteorSwarm -> {
                return switch (state) {
                    case METEOR_CARD_EFFECT_STATE, METEOR_CARD_CALC_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_HOW_TO_FACE_METEORS));
                    case METEOR_CARD_END_STATE ->// sbagliato qua
                            new ArrayList<>(Collections.singletonList(CommandType.SURREND));
                    case CHOOSE_DISC_BLOCK ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_ONE_SUB_SHIP));

                    default -> commands;
                };
            }
            case Card_AbandonedShip cardAbandonedShip -> {
                return switch (state) {
                    case ABANDONED_SHIP_PREPARATION ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_PASSENGERS_TO_LOSE));
                    case ABANDONED_SHIP_TAKEN, ABANDONED_FINAL_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                    default -> commands;
                };
            }
            case Card_AbandonedStation cardAbandonedStation -> {
                return switch (state) {
                    case ABANDONED_STATION_START_STATE ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_ABANDONED_STATION));
                    case ABANDONED_STATION_END_STATE -> new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                    default -> commands;
                };
            }
            case Card_Combat_zone cardCombatZone -> {
                return switch (state) {
                    case COMBAT_ZONE_BEGIN ->
                        // Gestione cause (richiederebbe un'enumerazione aggiuntiva)
                            commands;
                    case COMBAT_ZONE_CHOOSING_PASS ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_PASSENGERS_TO_LOSE));
                    case COMBAT_ZONE_CHOOSE_GOODS ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_WHERE_TO_PUT_GOODS));
                    case COMBAT_ZONE_CHOOSE_BATTERIES ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_PLACE_BATTERIES));
                    case METEOR_CARD_EFFECT_STATE_FROM_COMBAT ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_HOW_TO_FACE_METEORS));
                    case COMBAT_ZONE_FINAL -> new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                    case COMBAT_MOTION_POWER ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_START_MOTOR));
                    case COMBAT_FIRE_POWER ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_TO_START_FIRE_POWER));
                    case CHOOSE_DISC_BLOCK ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_ONE_SUB_SHIP));

                    default -> commands;
                };
            }
            case Card_Epidemic cardEpidemic -> {
                return switch (state) {
                    case EPIDEMIC_STATE_FINAL -> new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                    default -> commands;
                };
            }
            case Card_PlanetCard cardPlanetCard -> {
                return switch (state) {
                    case PLANET_STATE_INIT ->
                            new ArrayList<>(Collections.singletonList(CommandType.ACCEPT_TO_LAND_ON_A_PLANET));
                    case PLANET_STATE_ADD_GOODS ->
                            new ArrayList<>(Collections.singletonList(CommandType.CHOOSE_WHERE_TO_PUT_GOODS));
                    case PLANET_FIN_STAT -> new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                    default -> commands;
                };
            }
            case Card_Stardust cardStardust -> {
                return switch (state) {
                    case STARDUST_FINAL -> new ArrayList<>(Collections.singletonList(CommandType.SURREND));

                    default -> commands;
                };
            }
            default -> {
            }
        }

        return commands;
    }

    @Override
    public void messageSubShips(ArrayList<ArrayList<SpaceShipTile>> subShips,
                                String playerNickname) throws Exception {
        Platform.runLater(() -> {
            if (subShipsDialog != null && subShipsDialog.isShowing()) {
                ((Stage) subShipsDialog.getDialogPane().getScene().getWindow()).toFront();
                return;
            }

            subShipsDialog = new Dialog<>();
            subShipsDialog.initModality(Modality.NONE);
            subShipsDialog.setTitle("Choose Sub-Ship");
            subShipsDialog.setHeaderText(playerNickname + ", select which sub-ship to keep:");
            ButtonType keepBt = new ButtonType("Keep", ButtonBar.ButtonData.OK_DONE);
            subShipsDialog.getDialogPane().getButtonTypes().add(keepBt);

            ObservableList<String> items = FXCollections.observableArrayList();
            for (int x = 0; x < subShips.size(); x++) {
                List<SpaceShipTile> row = subShips.get(x);
                StringBuilder sb = new StringBuilder("Option " + x + ": ");
                if (row != null) {
                    for (SpaceShipTile tile : row) {
                        sb.append(tile.getType().name()).append(" ");
                    }
                }
                items.add(sb.toString());
            }
            ListView<String> list = new ListView<>(items);
            list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            subShipsDialog.getDialogPane().setContent(list);

            Node keepNode = subShipsDialog.getDialogPane().lookupButton(keepBt);
            keepNode.setDisable(true);
            list.getSelectionModel().selectedIndexProperty().addListener((o, old, idx) ->
                    keepNode.setDisable(idx.intValue() < 0)
            );

            subShipsDialog.setOnShowing(ev -> {
                Stage stage = (Stage) subShipsDialog.getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(Event::consume);
            });

            subShipsDialog.setResultConverter(bt ->
                    bt == keepBt ? list.getSelectionModel().getSelectedIndex() : null
            );

            subShipsDialog.setOnHidden(ev -> subShipsDialog = null);

            subShipsDialog.resultProperty().addListener((o, oldV, idx) -> {
                if (idx != null) {
                    try {
                        GenericClient client = GUI.getClient();
                        int id = -1;
                        for (ShipBoard s : GUI.getModel().getShipBoards()) {
                            if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                                id = s.getMyPlayer().getPlayerId();
                                break;
                            }
                        }
                        client.chooseOneSubShip(idx, id, client.getUuid());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            subShipsDialog.show();
        });
    }

    @Override
    public void showWrongTiles(ArrayList<Integer> tiles,
                               String nickname,
                               String nickEff) throws Exception {
        Platform.runLater(() -> {
            if (wrongTilesDialog != null && wrongTilesDialog.isShowing()) {
                ((Stage) wrongTilesDialog.getDialogPane().getScene().getWindow()).toFront();
                return;
            }

            if (!Objects.equals(nickname, nickEff)) {
                showInfoDialog("Attendi", "Waiting for other players' choice");
                return;
            }

            ShipBoard myBoard = view.getLittleModelRepresentation().getShipBoards().stream()
                    .filter(s -> s.getMyPlayer().getUsername().equals(nickname))
                    .findFirst().orElse(null);
            if (myBoard == null) return;

            int r0 = tiles.get(0), c0 = tiles.get(1);
            int r1 = tiles.get(2), c1 = tiles.get(3);
            String desc0 = String.format("%s at (row=%d,col=%d)",
                    myBoard.getShipMatrix()[r0][c0].getType(), r0 + 5, c0 + 4);
            String desc1 = String.format("%s at (row=%d,col=%d)",
                    myBoard.getShipMatrix()[r1][c1].getType(), r1 + 5, c1 + 4);

            wrongTilesDialog = new Dialog<>();
            wrongTilesDialog.initModality(Modality.NONE);
            wrongTilesDialog.setTitle("Wrong Tiles");
            wrongTilesDialog.setHeaderText("Choose which tile to remove:");
            ButtonType removeBt = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
            wrongTilesDialog.getDialogPane().getButtonTypes().add(removeBt);

            wrongTilesDialog.setOnShowing(ev -> {
                Stage stage = (Stage) wrongTilesDialog.getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(Event::consume);
            });

            ToggleGroup tg = new ToggleGroup();
            RadioButton rb0 = new RadioButton("Option 0: " + desc0);
            RadioButton rb1 = new RadioButton("Option 1: " + desc1);
            rb0.setToggleGroup(tg);
            rb1.setToggleGroup(tg);

            VBox content = new VBox(10, rb0, rb1);
            content.setPadding(new Insets(10));
            wrongTilesDialog.getDialogPane().setContent(content);

            Node removeNode = wrongTilesDialog.getDialogPane().lookupButton(removeBt);
            removeNode.setDisable(true);
            tg.selectedToggleProperty().addListener((o, oldV, newV) ->
                    removeNode.setDisable(newV == null)
            );

            wrongTilesDialog.setResultConverter(bt ->
                    bt == removeBt ? (rb0.isSelected() ? 0 : 1) : null
            );

            wrongTilesDialog.setOnHidden(ev -> wrongTilesDialog = null);

            wrongTilesDialog.resultProperty().addListener((o, oldV, idx) -> {
                if (idx != null) {
                    try {
                        GenericClient client = GUI.getClient();
                        int id = -1;
                        for (ShipBoard s : GUI.getModel().getShipBoards()) {
                            if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                                id = s.getMyPlayer().getPlayerId();
                                break;
                            }
                        }
                        client.setNumTile(id, idx, client.getUuid());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            wrongTilesDialog.show();
        });
    }

    @Override
    public void haveToFillTiles() throws Exception {
        Platform.runLater(() -> {
            // Se già aperto, porto in primo piano e non apro un altro
            if (fillTilesDialog != null && fillTilesDialog.isShowing()) {
                ((Stage) fillTilesDialog.getDialogPane().getScene().getWindow()).toFront();
                return;
            }

            fillTilesDialog = new Dialog<>();
            fillTilesDialog.initModality(Modality.NONE);
            fillTilesDialog.setTitle("Choose Alien Placement");
            fillTilesDialog.setHeaderText("Make your choice and press Confirm");

            ButtonType confirmBt = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            fillTilesDialog.getDialogPane().getButtonTypes().add(confirmBt);

            // Impedisci la chiusura con la “X”
            fillTilesDialog.setOnShowing(ev -> {
                Stage stage = (Stage) fillTilesDialog.getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(Event::consume);
            });

            // Costruzione dei controlli
            ComboBox<String> wantCb = new ComboBox<>();
            wantCb.getItems().addAll("No Alien", "Alien");
            wantCb.setPromptText("Alien?");

            ComboBox<Integer> rowCb = new ComboBox<>();
            for (int i = 5; i <= 9; i++) rowCb.getItems().add(i);
            rowCb.setPromptText("Row");

            ComboBox<Integer> colCb = new ComboBox<>();
            for (int j = 4; j <= 10; j++) colCb.getItems().add(j);
            colCb.setPromptText("Col");

            ComboBox<String> colorCb = new ComboBox<>();
            colorCb.getItems().addAll("VIOLET", "BROWN");
            colorCb.setPromptText("Color");
            colorCb.setDisable(true);

            // Abilita color solo se “Alien”
            wantCb.valueProperty().addListener((obs, o, n) ->
                    colorCb.setDisable(!"Alien".equals(n))
            );

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(new Label("Alien?"), 0, 0);
            grid.add(wantCb, 1, 0);
            grid.add(new Label("Row:"), 0, 1);
            grid.add(rowCb, 1, 1);
            grid.add(new Label("Col:"), 0, 2);
            grid.add(colCb, 1, 2);
            grid.add(new Label("Color:"), 0, 3);
            grid.add(colorCb, 1, 3);
            fillTilesDialog.getDialogPane().setContent(grid);

            // Disabilita “Confirm” finché non valido
            Node confirmNode = fillTilesDialog.getDialogPane().lookupButton(confirmBt);
            ChangeListener<Object> validator = (obs, o, n) -> {
                boolean valid = wantCb.getValue() != null
                        && rowCb.getValue() != null
                        && colCb.getValue() != null
                        && (!"Alien".equals(wantCb.getValue()) || colorCb.getValue() != null);
                confirmNode.setDisable(!valid);
            };
            wantCb.valueProperty().addListener(validator);
            rowCb.valueProperty().addListener(validator);
            colCb.valueProperty().addListener(validator);
            colorCb.valueProperty().addListener(validator);
            confirmNode.setDisable(true);

            // Azzera il riferimento quando chiuso
            fillTilesDialog.setOnHidden(ev -> fillTilesDialog = null);

            // Mostro e gestisco il risultato
            fillTilesDialog.resultProperty().addListener((o, oldBtn, btn) -> {
                if (btn == confirmBt) {
                    boolean wantAlien = "Alien".equals(wantCb.getValue());
                    int row = rowCb.getValue() - 5;
                    int col = colCb.getValue() - 4;
                    try {
                        GenericClient client = GUI.getClient();
                        int id1 = -1;
                        for (ShipBoard s : GUI.getModel().getShipBoards()) {
                            if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                                id1 = s.getMyPlayer().getPlayerId();
                                break;
                            }
                        }
                        client.CommandFillTile(id1, wantAlien, getSelectedAlienColor(wantAlien, colorCb), row, col, client.getUuid());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            fillTilesDialog.show();
        });
    }

    @Override
    public void updateBatteriesRemaining(Player p, int batt) throws Exception {
        selectTilesBtn23.setText("Select battery positions (" + batt + " available)");

    }

    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception {
        int V4 = 0;
        int V3 = 0;
        int V2 = 0;
        int V1 = 0;
        for (Goods good : goodFInali) {
            if (good.getValue() == 4) V4++;
            if (good.getValue() == 3) V3++;
            if (good.getValue() == 2) V2++;
            if (good.getValue() == 1) V1++;
        }
        handleSelectGood1_17.setText("Available: " + V1);
        handleSelectGood2_17.setText("Available: " + V2);
        handleSelectGood3_17.setText("Available: " + V3);
        handleSelectGood4_17.setText("Available: " + V4);
        handleSelectGood1_22.setText("Available: " + V1);
        handleSelectGood2_22.setText("Available: " + V2);
        handleSelectGood3_22.setText("Available: " + V3);
        handleSelectGood4_22.setText("Available: " + V4);
        handleSelectGood1.setText("Available: " + V1);
        handleSelectGood2.setText("Available: " + V2);
        handleSelectGood3.setText("Available: " + V3);
        handleSelectGood4.setText("Available: " + V4);
    }


    /**
     * Gets selected alien color.
     *
     * @param wantAlien the want alien
     * @param colorCb   the color cb
     * @return the selected alien color
     */
    public AlienColor getSelectedAlienColor(boolean wantAlien, ComboBox<String> colorCb) {
        if (!wantAlien) {
            return null;  // o Optional.empty() se preferisci non usare null
        }
        String val = colorCb.getValue();
        if ("BROWN".equals(val)) {
            return AlienColor.BROWN;
        } else if ("VIOLET".equals(val)) {
            return AlienColor.VIOLET;
        } else {
            throw new IllegalArgumentException("Colore alieno non valido: " + val);
        }
    }

    @Override
    public void ChooseSubShip(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) throws Exception {

    }

    @Override
    public void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) throws Exception {
        clearCell(row, col);
    }

    @Override
    public void addWaitTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        //prendo la tile
        Image currentTileImage = currentItem.getImage();
        Platform.runLater(() -> {
            currentItem.setImage(null);
            currentItem.setRotate(0);
        });

        // 1) Prendi la rotazione dalla tile
        int rotation = tile.getRotation();

        // 3b) Trova il StackPane corrispondente nella GridPane
        for (Node node : gridMatrix.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (r != null && c != null && r == row && c == col) {
                StackPane cell = (StackPane) node;
                // Pulisci eventuali children precedenti
                cell.getChildren().clear();
                // Crea e configura l’ImageView con la rotazione
                ImageView iv = newTileImage(currentTileImage, cellPanes[r][c]);
                iv.setPreserveRatio(false);
                iv.setRotate(rotation);  // applica la rotazione
                // Aggiungi alla cella
                cell.getChildren().add(iv);
                break;
            }
        }
    }

    @Override
    public void endBuilding(String playerNick) throws Exception {
        pickTileButton.setVisible(false);
        pickTileButton.setDisable(true);
        demoButton.setVisible(false);
        demoButton.setDisable(true);
        rotateButton.setVisible(false);
        rotateButton.setDisable(true);
        currentItem.setImage(null);
        endBuildingButton.setVisible(false);
        endBuildingButton.setDisable(true);
        playerNumberComboBox.setVisible(false);
        playerNumberComboBox.setDisable(true);
        scrollPane.setVisible(false);
        scrollPane.setDisable(true);
        currentItemContainer.setVisible(false);
    }


    @Override
    public void removePlayerFromFlightboard(String playerNickname, int oldpos) throws Exception {
        playerBanner.setText("YOU LEFT THE FLIGHT!");
    }

    @Override
    public void showOldGames(ArrayList<OldGame> oldGames) throws Exception {

    }

    /**
     * Handle take reward.
     *
     * @param actionEvent the action event
     */
    public void handleTakeReward(ActionEvent actionEvent) {
    }

    /**
     * Handle skip reward.
     *
     * @param actionEvent the action event
     */
    public void handleSkipReward(ActionEvent actionEvent) {
    }

    //-----17
    @FXML
    private void handleSelectStorageForGoodsStation() {
        resetSelections();

        setOpacityOnGridByImage(good1img17.getImage(), 0.5);
        setOpacityOnGridByImage(good2img17.getImage(), 0.5);
        setOpacityOnGridByImage(good3img17.getImage(), 0.5);
        setOpacityOnGridByImage(good4img17.getImage(), 0.5);
        selectingStorageForRewardStation = true;
        rewardGoodsPlacementMapStation.clear(); // azzera posizioni+beni
        selectedGoodValue_22 = null;
        gridMatrix.setCursor(Cursor.CROSSHAIR);
        setGoodsButtonsEnabled(true);
    }

    // ID 18 - Cannoni + Batterie
    @FXML
    private void handleSelectCannons() {
        resetSelections(); // resetta eventuali precedenti selezioni
        selectingCannons = true;
        selectingBatteriesForCannons = false;
        selectedCannons.clear();
        selectedBatteriesForCannons.clear();
        gridMatrix.setCursor(Cursor.CROSSHAIR);

    }


    @FXML
    private void handleSelectBatteriesForCannons() {
        selectingCannons = false;
        selectingBatteriesForCannons = true;
        selectedBatteriesForCannons.clear();
        gridMatrix.setCursor(Cursor.CROSSHAIR);

    }


    // ID 19 - Difese dai meteoriti
    @FXML
    private void handleStartMeteorDefense() {
        resetSelections();  // opzionale ma sicuro
        selectingMeteorDefenses = true;
        selectedMeteorDefenses.clear();
        gridMatrix.setCursor(Cursor.CROSSHAIR);

        doneBtn19.setDisable(false);
        doneBtn19.setVisible(true);
        doneBtn19.setManaged(true);
    }


    // ID 20 - Passeggeri da perdere
    @FXML
    private void handleSelectPassengersToLose() {
        resetSelections();
        selectingPassengersToLose = true;
        selectedPassengersToLose.clear();
        gridMatrix.setCursor(Cursor.CROSSHAIR);

        doneBtn20.setDisable(false);
        doneBtn20.setVisible(true);
        doneBtn20.setManaged(true);

    }

    /**
     * Handle accepting.
     */
    @FXML
    public void handleAccepting() {
        enableAfterChoiceForVisibleVBox();
    }


    // ID 22 - Reward con storage

    @FXML
    private void handleSelectStorageForGoods() {
        resetSelections();
        selectingStorageForReward = true;
        rewardGoodsPlacementMap.clear(); // azzera posizioni+beni
        selectedGoodValue_22 = null;
        gridMatrix.setCursor(Cursor.CROSSHAIR);
        setGoodsButtonsEnabled(true);
    }


    // ID 23 - Posizionamento batterie
    @FXML
    private void handleSelectBatteryPositions() {
        resetSelections();  // <-- questa mancava
        selectingBatteryPlacements = true;
        selectedBatteryPlacements.clear();
        gridMatrix.setCursor(Cursor.CROSSHAIR);
    }

    // ID 24 - Attivazione fuoco
    @FXML
    private void handleSelectCannonsFirepower() {
        resetSelections();
        selectingFirepowerCannons = true;
        selectingFirepowerBatteries = false;
        selectedFirepowerCannons.clear();
        gridMatrix.setCursor(Cursor.CROSSHAIR);
    }

    @FXML
    private void handleSelectBatteriesForFirepower() {
        selectingFirepowerCannons = false;
        selectingFirepowerBatteries = true;
        selectedFirepowerBatteries.clear();
        gridMatrix.setCursor(Cursor.CROSSHAIR);
    }

    // ID 25 - Motori + batterie
    @FXML
    private void handleSelectEngines() {
        resetSelections();  // azzera tutto
        selectingEngines = true;
        selectingEngineBatteries = false;
        gridMatrix.setCursor(Cursor.CROSSHAIR);

        // Disabilita cannoni se stai scegliendo motori
        selectCannonsBtn24.setDisable(true);
    }

    @FXML
    private void handleSelectBatteriesForEngines() {
        selectingEngines = false;
        selectingEngineBatteries = true;
        selectedEngineBatteries.clear();
        gridMatrix.setCursor(Cursor.CROSSHAIR);
    }

    // ID 26 - Posizionamento merci
    @FXML
    private void handleSelectGoodsPlacement() {
        //  Azzeriamo le selezioni precedenti
        goodsPlacementMap.clear();
        goodSelectedToPlace = null;

        //  Riattiviamo la modalità di selezione
        selectingGoodsPositions = true;

        //  Cursore visivo
        gridMatrix.setCursor(Cursor.CROSSHAIR);

        //  Riattiva i pulsanti dei good
        setGoodsButtonsEnabled(true);

        //  Riabilita il Done
        doneBtn26.setDisable(false);
        doneBtn26.setVisible(true);
        doneBtn26.setManaged(true);
    }

    private void setGoodsButtonsEnabled(boolean enabled) {
        selectStorageBtn26.setDisable(false); // una volta iniziato, non modifico
        // Attiva/disattiva i 4 pulsanti
        boxCHOOSE_WHERE_TO_PUT_GOODS.lookup("#handleSelectGood1").setDisable(!enabled);
        boxCHOOSE_WHERE_TO_PUT_GOODS.lookup("#handleSelectGood2").setDisable(!enabled);
        boxCHOOSE_WHERE_TO_PUT_GOODS.lookup("#handleSelectGood3").setDisable(!enabled);
        boxCHOOSE_WHERE_TO_PUT_GOODS.lookup("#handleSelectGood4").setDisable(!enabled);
    }

    //16


    @FXML
    private void handleAcceptPlanet0() {
        sendPlanetChoice(0);
        planetBtn1.setVisible(false);
        planetBtn1.setManaged(false);
        planetBtn1.setDisable(true);

        planetBtn2.setVisible(false);
        planetBtn2.setManaged(false);
        planetBtn2.setDisable(true);

        planetBtn3.setVisible(false);
        planetBtn3.setManaged(false);
        planetBtn3.setDisable(true);
    }

    @FXML
    private void handleAcceptPlanet1() {
        sendPlanetChoice(1);
        planetBtn2.setVisible(false);
        planetBtn2.setManaged(false);
        planetBtn2.setDisable(true);

        planetBtn3.setVisible(false);
        planetBtn3.setManaged(false);
        planetBtn3.setDisable(true);

        planetBtn0.setVisible(false);
        planetBtn0.setManaged(false);
        planetBtn0.setDisable(true);
    }

    @FXML
    private void handleAcceptPlanet2() {
        sendPlanetChoice(2);
        planetBtn3.setVisible(false);
        planetBtn3.setManaged(false);
        planetBtn3.setDisable(true);

        planetBtn0.setVisible(false);
        planetBtn0.setManaged(false);
        planetBtn0.setDisable(true);

        planetBtn1.setVisible(false);
        planetBtn1.setManaged(false);
        planetBtn1.setDisable(true);
    }

    @FXML
    private void handleAcceptPlanet3() {
        sendPlanetChoice(3);
        planetBtn0.setVisible(false);
        planetBtn0.setManaged(false);
        planetBtn0.setDisable(true);

        planetBtn1.setVisible(false);
        planetBtn1.setManaged(false);
        planetBtn1.setDisable(true);

        planetBtn2.setVisible(false);
        planetBtn2.setManaged(false);
        planetBtn2.setDisable(true);
    }

    @FXML
    private void handleDeclinePlanet() {
        new Thread(() -> {
            try {
                GUI.getClient().acceptToLandOnAPlanet(GUI.getModel().getMyNickname(), false, 0, GUI.getClient().getUuid());
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();
    }

    @FXML
    private void handleDeclineToFaceMeteors() {
        new Thread(() -> {
            try {
                GUI.getClient().chooseHowToFaceMeteors(
                        GUI.getModel().getMyNickname(),
                        null,
                        GUI.getClient().getUuid()
                );
                selectedMeteorDefenses.clear();
                Platform.runLater(this::resetSelections);
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();
    }

    // metodo ausiliario comune
    private void sendPlanetChoice(int planetNum) {
        new Thread(() -> {
            try {
                GUI.getClient().acceptToLandOnAPlanet(GUI.getModel().getMyNickname(), true, planetNum, GUI.getClient().getUuid());
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();
    }

    /**
     * Demo game.
     */
    public void demoGame() {
        new Thread(() -> {
            try {
                GUI.getClient().demoGame(GUI.getClient().getUuid());
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();
    }

    //-----17

    @FXML
    private void handleAcceptReward17() {
        resetSelections();
        enableAfterChoiceForVisibleVBox();
        //qui si può far vedere i bottoni nuovi dei good e della selezione

        handleSelectGood1_22.setDisable(false);
        handleSelectGood2_22.setDisable(false);
        handleSelectGood3_22.setDisable(false);
        handleSelectGood4_22.setDisable(false);
//
//        doneBtn17.setDisable(false);
//        doneBtn17.setManaged(true);
//        doneBtn17.setVisible(true);
    }


    @FXML
    private void handleDeclineReward17() {
        resetSelections(); // <--- aggiungilo all'inizio

        setOpacityOnGridByImage(good1img17.getImage(), 1);
        setOpacityOnGridByImage(good2img17.getImage(), 1);
        setOpacityOnGridByImage(good3img17.getImage(), 1);
        setOpacityOnGridByImage(good4img17.getImage(), 1);
        new Thread(() -> {
            try {
                GUI.getClient().chooseAbandonedStation(GUI.getClient().getview().getMyNickname(), false, null, null, GUI.getClient().getUuid());
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();
    }

    // ID 17 - Reward con beni e storage
    @FXML
    private void handlechooseAbandonedStationSendReward17() {
        setOpacityOnGridByImage(good1img17.getImage(), 1);
        setOpacityOnGridByImage(good2img17.getImage(), 1);
        setOpacityOnGridByImage(good3img17.getImage(), 1);
        setOpacityOnGridByImage(good4img17.getImage(), 1);
        new Thread(() -> {
            try {
                ArrayList<ArrayList<Integer>> storageTiles;
                ArrayList<ArrayList<Goods>> newGoods;

                if (rewardGoodsPlacementMapStation.isEmpty()) {
                    storageTiles = null;
                    newGoods = null;
                } else {
                    storageTiles = new ArrayList<>();
                    newGoods = new ArrayList<>();
                    for (var entry : rewardGoodsPlacementMapStation.entrySet()) {
                        // chiave = [r0,c0]
                        storageTiles.add(new ArrayList<>(entry.getKey()));
                        // valore = lista di int → lista di Goods
                        ArrayList<Goods> goodsList = new ArrayList<>(entry.getValue());
                        newGoods.add(goodsList);
                    }
                }
                String nickname = GUI.getModel().getMyNickname();
                System.out.println("[DEBUG] <nomeComando> chiamato con:");
                System.out.println("         nickname = " + nickname);
                System.out.println("         <yon> =  t");
                System.out.println("         <storageTiles> = " + storageTiles);
                System.out.println("         <newGoods> = " + newGoods);
                System.out.println("         …");

                GUI.getClient().chooseAbandonedStation(
                        GUI.getModel().getMyNickname(),
                        true,
                        storageTiles,
                        newGoods,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                Platform.runLater(() -> {
                    resetSelections();
                    rewardGoodsPlacementMapStation.clear();
                    selectedGoodValue_22 = null;
                });
            }
        }).start();
    }

    // ID 18 - Cannoni + Batterie
    @FXML
    private void handleSendCannonBatteryActivation() {
        new Thread(() -> {
            try {
                // 1) Se non ho selezionato nulla mando null
                ArrayList<ArrayList<Integer>> cannonPos = selectedCannons.isEmpty()
                        ? null
                        : selectedCannons.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toCollection(ArrayList::new));

                ArrayList<ArrayList<Integer>> batteriesPos = selectedBatteriesForCannons.isEmpty()
                        ? null
                        : selectedBatteriesForCannons.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toCollection(ArrayList::new));

                // 2) Chiamata diretta al client
                GenericClient client = GUI.getClient();
                client.chooseCannonBatteryPos(
                        GUI.getModel().getMyNickname(),
                        cannonPos,
                        batteriesPos,
                        client.getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                // 3) Ripristino stato
                Platform.runLater(this::resetSelections);
            }
        }).start();
    }

    @FXML
    private void handleDeclineCannonBatteryActivation() {
        new Thread(() -> {
            try {
                GenericClient client = GUI.getClient();
                client.chooseCannonBatteryPos(
                        GUI.getModel().getMyNickname(),
                        null,
                        null,
                        client.getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                // 3) Ripristino stato
                Platform.runLater(this::resetSelections);
            }
        }).start();
    }


    // ID 19 - Difesa dai meteoriti
    @FXML
    private void handleSendMeteorDefense() {
        new Thread(() -> {
            try {
                selectingMeteorDefenses = false;
                // se non ho scelto nulla mando null
                ArrayList<Integer> defence = selectedMeteorDefenses.isEmpty()
                        ? null
                        : new ArrayList<>(selectedMeteorDefenses);

                // chiamo direttamente il client
                GUI.getClient().chooseHowToFaceMeteors(
                        GUI.getModel().getMyNickname(),
                        defence,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                // resetto selezioni e UI
                //resetSelections();
                selectedMeteorDefenses.clear();
                Platform.runLater(this::resetSelections);
            }
        }).start();
    }


    // ID 20 - Passeggeri da perdere
    @FXML
    private void handleSendPassengerLoss() {
        new Thread(() -> {
            try {
                // se non ho scelto nulla mando null
                ArrayList<ArrayList<Integer>> passList = selectedPassengersToLose.isEmpty()
                        ? null
                        : selectedPassengersToLose.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toCollection(ArrayList::new));

                // invoco direttamente il metodo sul client
                GUI.getClient().choosePassengersToLose(
                        GUI.getModel().getMyNickname(),
                        passList != null,      // yOn = true se ho almeno una selezione
                        passList,              // may be null
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                // resetto selezioni e UI
                Platform.runLater(this::resetSelections);
            }
        }).start();
    }


    // ID 21 - Reward semplice
    @FXML
    private void handleClaimRewardYes() {
        new Thread(() -> {
            try {
                GUI.getClient().chooseToClaimReward(
                        true,
                        GUI.getModel().getMyNickname(),
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();
    }


    @FXML
    private void handleClaimRewardNo() {
        new Thread(() -> {
            try {
                GUI.getClient().chooseToClaimReward(
                        false,
                        GUI.getModel().getMyNickname(),
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();

        // Disabilita i pulsanti dei goods, per sicurezza
        Platform.runLater(() -> setGoodsButtonsEnabled(false));
    }


    // ID 22 - Reward con beni e storage
    @FXML
    private void handleSendRewardGoodsPlacement() {
        new Thread(() -> {
            try {
                ArrayList<ArrayList<Integer>> storageTiles;
                ArrayList<ArrayList<Goods>> newGoods;

                if (rewardGoodsPlacementMap.isEmpty()) {
                    storageTiles = null;
                    newGoods = null;
                } else {
                    storageTiles = new ArrayList<>();
                    newGoods = new ArrayList<>();
                    for (var entry : rewardGoodsPlacementMap.entrySet()) {
                        // chiave = [r0,c0]
                        storageTiles.add(new ArrayList<>(entry.getKey()));
                        // valore = lista di int → lista di Goods
                        ArrayList<Goods> goodsList = new ArrayList<>();
                        for (Integer v : entry.getValue()) {
                            goodsList.add(new Goods(v, v == 4));
                        }
                        newGoods.add(goodsList);
                    }
                }

                GUI.getClient().chooseToClaimReward(
                        true,
                        GUI.getModel().getMyNickname(),
                        storageTiles,
                        newGoods,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                Platform.runLater(() -> {
                    resetSelections();
                    rewardGoodsPlacementMap.clear();
                    selectedGoodValue_22 = null;
                });
            }
        }).start();
    }


    // ID 23 - Posizionamento batterie
    @FXML
    private void handleSendBatteryPlacement() {
        new Thread(() -> {
            try {
                ArrayList<ArrayList<Integer>> battList = selectedBatteryPlacements.isEmpty()
                        ? null
                        : selectedBatteryPlacements.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toCollection(ArrayList::new));

                GUI.getClient().chooseToPlaceBatteries(
                        GUI.getModel().getMyNickname(),
                        battList,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                Platform.runLater(this::resetSelections);
            }
        }).start();
    }


    // ID 24 - Attivazione fuoco
    @FXML
    private void handleSendFirepowerActivation() {
        new Thread(() -> {
            try {
                ArrayList<ArrayList<Integer>> cannons = selectedFirepowerCannons.isEmpty()
                        ? null
                        : selectedFirepowerCannons.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toCollection(ArrayList::new));

                ArrayList<ArrayList<Integer>> batteries = selectedFirepowerBatteries.isEmpty()
                        ? null
                        : selectedFirepowerBatteries.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toCollection(ArrayList::new));

                GUI.getClient().chooseToStartFirePower(
                        GUI.getModel().getMyNickname(),
                        cannons,
                        batteries,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                // pulisco selezioni e UI
                Platform.runLater(this::resetSelections);
            }
        }).start();
    }

    @FXML
    private void handleDeclineBatteriesForFirepower() {
        new Thread(() -> {
            try {

                GUI.getClient().chooseToStartFirePower(
                        GUI.getModel().getMyNickname(),
                        null,
                        null,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                // pulisco selezioni e UI
                Platform.runLater(this::resetSelections);
            }
        }).start();
    }


    // ID 25 - Attivazione motori
    @FXML
    private void handleSendEngineActivation() {
        new Thread(() -> {
            try {
                ArrayList<ArrayList<Integer>> engines = selectedEngines.isEmpty()
                        ? null
                        : selectedEngines.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toCollection(ArrayList::new));

                ArrayList<ArrayList<Integer>> batteries = selectedEngineBatteries.isEmpty()
                        ? null
                        : selectedEngineBatteries.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toCollection(ArrayList::new));

                GUI.getClient().chooseToStartMotor(
                        GUI.getModel().getMyNickname(),
                        engines,
                        batteries,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                // pulisco selezioni e UI
                Platform.runLater(() -> {
                    resetSelections();
                    // riattiva eventuale pulsante cannoni se serve
                    selectCannonsBtn24.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    private void handleDeclineEngineActivation() {
        new Thread(() -> {
            try {
                GUI.getClient().chooseToStartMotor(
                        GUI.getModel().getMyNickname(),
                        null,
                        null,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                // pulisco selezioni e UI
                Platform.runLater(this::resetSelections);
            }
        }).start();
    }


    // ID 26 - Posizionamento merci
    @FXML
    private void handleSendGoodsPlacement() {
        new Thread(() -> {
            try {
                ArrayList<ArrayList<Integer>> posList;
                ArrayList<ArrayList<Goods>> goodsSets;

                if (goodsPlacementMap.isEmpty()) {
                    posList = null;
                    goodsSets = null;
                } else {
                    posList = new ArrayList<>();
                    goodsSets = new ArrayList<>();
                    for (var entry : goodsPlacementMap.entrySet()) {
                        // coordinate
                        posList.add(new ArrayList<>(entry.getKey()));
                        // insieme di Goods
                        ArrayList<Goods> oneSet = new ArrayList<>();
                        for (Integer v : entry.getValue()) {
                            oneSet.add(new Goods(v, v == 4));
                        }
                        goodsSets.add(oneSet);
                    }
                }

                GUI.getClient().chooseWhereToPutGoods(
                        GUI.getModel().getMyNickname(),
                        posList,
                        goodsSets,
                        GUI.getClient().getUuid()
                );
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            } finally {
                Platform.runLater(() -> {
                    resetSelections();
                    goodsPlacementMap.clear();
                    goodSelectedToPlace = null;
                });
            }
        }).start();
    }


    @FXML
    private void handleSurrend() {
        new Thread(() -> {
            try {
                GUI.getClient().Surrend(GUI.getModel().getShipBoards().stream()
                        .filter(s -> s.getMyPlayer().getUsername()
                                .equals(GUI.getModel().getMyNickname()))
                        .findFirst().get().getMyPlayer().getPlayerId(), GUI.getClient().getUuid());
            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();
    }

    @FXML
    private void handleSelectGood1() {
        goodSelectedToPlace = 1;
    }

    @FXML
    private void handleSelectGood2() {
        goodSelectedToPlace = 2;
    }

    @FXML
    private void handleSelectGood3() {
        goodSelectedToPlace = 3;
    }

    @FXML
    private void handleSelectGood4() {
        goodSelectedToPlace = 4;
    }

    //-----22
    @FXML
    private void handleSelectGood1_22() {
        selectedGoodValue_22 = 1;
    }

    @FXML
    private void handleSelectGood2_22() {
        selectedGoodValue_22 = 2;
    }

    @FXML
    private void handleSelectGood3_22() {
        selectedGoodValue_22 = 3;
    }

    @FXML
    private void handleSelectGood4_22() {
        selectedGoodValue_22 = 4;
    }


    @FXML
    private void handleAcceptReward22() {
        resetSelections(); // <--- aggiungilo all'inizio
        enableAfterChoiceForVisibleVBox();
        handleSelectGood1_22.setDisable(false);
        handleSelectGood2_22.setDisable(false);
        handleSelectGood3_22.setDisable(false);
        handleSelectGood4_22.setDisable(false);
        //qui si può far vedere i bottoni nuovi dei good e della selezione

    }


    @FXML
    private void handleDeclineReward22() {
        resetSelections(); // <--- aggiungilo all'inizio

        //qui si può far vedere i bottoni nuovi dei good e della selezione
        handleSelectGood1_22.setVisible(false);
        handleSelectGood1_22.setDisable(true);
        handleSelectGood1_22.setManaged(false);
        handleSelectGood2_22.setVisible(false);
        handleSelectGood2_22.setDisable(true);
        handleSelectGood2_22.setManaged(false);
        handleSelectGood3_22.setVisible(false);
        handleSelectGood3_22.setDisable(true);
        handleSelectGood3_22.setManaged(false);
        handleSelectGood4_22.setVisible(false);
        handleSelectGood4_22.setDisable(true);
        handleSelectGood4_22.setManaged(false);
        handleSelectGood1_22.setDisable(true);
        handleSelectGood2_22.setDisable(true);
        handleSelectGood3_22.setDisable(true);
        handleSelectGood4_22.setDisable(true);
        //anche il no btn
        noBtn22.setDisable(false);
        noBtn22.setManaged(true);
        noBtn22.setVisible(true);
        //anche il yes btn
        yesBtn22.setDisable(false);
        yesBtn22.setManaged(true);
        yesBtn22.setVisible(true);
        //attivo il done
        doneBtn22.setDisable(true);
        doneBtn22.setManaged(false);
        doneBtn22.setVisible(false);


        new Thread(() -> {
            try {
                //   view.sendGUI("22 0");
                GUI.getClient().chooseToClaimReward(false, GUI.getClient().getview().getMyNickname(), null, null, GUI.getClient().getUuid());

            } catch (Exception e) {
                Platform.runLater(() -> showError(e));
            }
        }).start();
    }


}
