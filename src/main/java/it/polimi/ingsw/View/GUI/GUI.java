package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class GUI extends Application implements GeneralView {
    /**
     * A `CountDownLatch` initialized with a count of 1 to coordinate synchronization
     * tasks within the GUI application. It can be used to block a thread until the latch
     * count reaches zero, ensuring certain operations are completed before proceeding.
     */
    private static final CountDownLatch latch = new CountDownLatch(1);
    /**
     * A thread-safe list that maintains a collection of old games awaiting further processing or display.
     * This list is synchronized to ensure safe usage across multiple threads.
     */
    private static final List<OldGame> pendingOldGames = Collections.synchronizedList(new ArrayList<>());
    /**
     * A flag indicating whether the application should use a socket-based communication strategy.
     * This variable must be set prior to calling the {@link GUI#launchGUI()} method.
     * It determines the communication method used by the GUI, typically toggling
     * between socket-based and alternative methods.
     */
    // --- Parametri da “iniettare” PRIMA di launchGUI():
    private static boolean useSocketFlag;
    /**
     * Represents the address of the server to which the application connects.
     * Typically, this is in the form of a hostname or an IP address.
     * It is intended to be a static variable, shared across instances.
     */
    private static String serverAddress;

    /**
     * Retrieves the instance of the GenericClient.
     *
     * @return the current instance of GenericClient
     */
    public static GenericClient getClient() {
        return client;
    }

    /**
     * Holds a reference to the GenericClient instance used by the GUI class.
     * This variable is initialized in a thread-safe manner due to its `volatile` modifier,
     * ensuring visibility of changes across threads.
     * It is shared across static methods in the GUI class to manage client interactions.
     */
    // --- Instance fields ---
    private static volatile GenericClient client;
    /**
     * A volatile static reference to the primary {@link Stage} of the application's GUI.
     * This stage represents the main window of the application, typically used to initialize
     * and manage scenes during the application's lifecycle.
     *
     * The use of the `volatile` modifier ensures visibility of updates to this variable
     * across threads, promoting thread safety for operations involving the primary stage.
     *
     * This variable is managed by static getter and setter methods within the containing class,
     * allowing controlled access to set and retrieve its value.
     */
    private static volatile Stage primaryStage;
    /**
     * A static instance of the GUI class used to implement the singleton pattern.
     * Ensures that there is only one global and accessible instance of GUI within
     * the application lifecycle.
     */
    private static GUI instance;
    /**
     * A volatile static variable representing the current model of the system.
     * The use of volatile ensures visibility of changes to the model across threads,
     * enabling safe access in a multithreaded environment.
     * Initialized and updated as part of the GUI configuration process.
     */
    private static volatile LittleModelRepresentation model;
    /**
     * A map containing the loaded scenes.
     * Each entry in the map associates a unique scene identifier, represented as a String,
     * with its corresponding SceneData object.
     */
    // **NEW**: mappa delle scene caricate
    private final Map<String, SceneData> scenes = new HashMap<>();
    /**
     * Represents the current scene key being displayed or managed by the GUI.
     * This variable is used to identify and switch between different scenes in the application.
     */
    private String currentKey;

    // **NEW**: piccolo wrapper per root+controller
    private static class SceneData {
        Parent root;
        GeneralView controller;

        SceneData(Parent r, GeneralView c) {
            root = r;
            controller = c;
        }
    }

    /**
     * Provides the singleton instance of the GUI class.
     *
     * @return the single instance of the GUI class
     */
    public static GUI getInstance() {
        return instance;
    }

    /**
     * Sets the primary stage for the application.
     *
     * @param stage the primary stage to be set. This is generally the main stage of the application.
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Retrieves the primary stage of the application.
     *
     * @return the primary Stage object of the application
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Configures the system with provided parameters.
     *
     * @param useSocket      Determines whether to use a socket connection.
     * @param srvAddr        The server address to be used for configuration.
     * @param littleModel    An instance of LittleModelRepresentation for system setup.
     */
    public static void configure(boolean useSocket,
                                 String srvAddr,
                                 LittleModelRepresentation littleModel) {
        useSocketFlag = useSocket;
        serverAddress = srvAddr;
        model = littleModel;
    }


    /**
     * Initializes and launches the Graphical User Interface (GUI) for the application.
     * This method starts the JavaFX application on a separate thread and waits for the
     * required synchronization to ensure proper initialization.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     *                              for the latch to complete.
     */
    public static void launchGUI() throws InterruptedException {
        new Thread(() -> Application.launch(GUI.class)).start();
        latch.await();
    }

    /**
     * Registers a new scene in the application with a specified key, FXML path, and an initializer
     * for specific controller configuration.
     *
     * @param key          the unique identifier for the scene being registered
     * @param fxmlPath     the path to the FXML file defining the scene layout
     * @param initializer  a Consumer used to initialize the controller with scene-specific logic
     * @throws IOException if there is an issue loading the FXML file
     */
    private void registerScene(String key, String fxmlPath, Consumer<GeneralView> initializer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        GeneralView controller = loader.getController();

        // iniezioni comuni
        controller.setView(this);
        controller.setLittleModelRepresentation(model);

        // iniezioni specifiche (con cast dentro il lambda)
        initializer.accept(controller);

        scenes.put(key, new SceneData(root, controller));
    }

    /**
     * Starts the JavaFX application and initializes the primary stage and scenes.
     * This method sets up the connection between the GUI and the model, registers various
     * scenes, and handles the primary stage close request with a confirmation alert.
     *
     * @param stage the primary stage of the JavaFX application
     * @throws Exception if any error occurs during the initialization of the application
     */
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        primaryStage = stage;
        if (model != null) {
            model.setGeneralView(this); // Collega la GUI alla LittleModel
        }
        registerScene("PLAYER_NAME", "/it/polimi/ingsw/fxml/playerNamePage.fxml", gv -> {
            // gv è GeneralView → castiamo al controller giusto
            PlayerName_Page_Controller ctrl = (PlayerName_Page_Controller) gv;
            ctrl.setUseSocket(useSocketFlag);
            ctrl.setServerAddress(serverAddress);
        });
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Exit Confirmation");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to exit?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });


        showScene("PLAYER_NAME");
        latch.countDown();
    }


    /**
     * Displays the scene associated with the given key, updating the current scene and controller.
     * If a scene with the specified key does not exist, an IllegalArgumentException is thrown.
     *
     * @param key the identifier for the scene to be displayed
     */
    private void showScene(String key) {
        SceneData sd = scenes.get(key);
        if (sd == null) throw new IllegalArgumentException("Scene non registrata: " + key);

        // nascondi vecchio controller
        if (currentKey != null) scenes.get(currentKey).controller.hide();

        currentKey = key;
        sd.controller.show();

        // imposta la nuova root
        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(sd.root));
            primaryStage.getIcons().add(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/images/logo.png"))
            ));
            primaryStage.setTitle("Galaxy Trucker");
        } else {
            primaryStage.getScene().setRoot(sd.root);
        }
        primaryStage.show();
    }

    /**
     * Retrieves the LittleModelRepresentation object.
     *
     * @return the LittleModelRepresentation object representing the model.
     */
    @Override
    public LittleModelRepresentation getLittleModelRepresentation() {
        return model;
    }

    /**
     * Retrieves the current instance of LittleModelRepresentation.
     *
     * @return the current instance of LittleModelRepresentation.
     */
    public static LittleModelRepresentation getModel() {
        return model;
    }

    /**
     * Updates the entity or state based on the provided JSON string.
     * This method parses the JSON input and performs the necessary update operations.
     *
     * @param Json the input JSON string containing the data or parameters
     *             required to perform the update.
     * @throws Exception if an error occurs during the update process,
     *                   such as invalid JSON format or failure in processing the update.
     */
    @Override
    public void Update(String Json) throws Exception {
        // … la tua logica di update
    }

    /**
     * Starts the process by setting the given username as the user's nickname.
     *
     * @param username the username to be set as the user's nickname
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void Start(String username) throws Exception {
        model.setMyNickname(username);
    }

    /**
     * Initializes and displays the create game page for the user.
     *
     * @param username the nickname of the user initiating the game page
     * @throws IOException if there is an issue in loading the resource or displaying the scene
     */
    public void createGamePage(String username) throws IOException {
        model.setMyNickname(username);

        registerScene("CREATE_GAME", "/it/polimi/ingsw/fxml/createGamePage.fxml", gv -> {
            CreateGame_Page_Controller ctrl = (CreateGame_Page_Controller) gv;
            ctrl.setClient(client);
        });
        showScene("CREATE_GAME");
        updateOldGamesIfReady();
    }

    /**
     * Displays a list of old games by buffering them locally and updating the relevant UI component.
     *
     * @param oldGames the list of old games to display. If the list is null or empty, an error message is logged.
     */
    @Override
    public void showOldGames(ArrayList<OldGame> oldGames) {
        if (oldGames == null || oldGames.isEmpty()) {
            System.err.println("[GUI] Lista vuota dal server!");
            return;
        }

        // Bufferizzo sempre (in caso di risposta anticipata)
        synchronized (pendingOldGames) {
            pendingOldGames.clear();
            pendingOldGames.addAll(oldGames);
        }
        System.out.println("[GUI] Bufferizzati " + oldGames.size() + " giochi.");

        // Provo subito ad aggiornare, su FX Thread
        Platform.runLater(() -> {
            SceneData sd = scenes.get("CREATE_GAME");
            if (sd != null && sd.controller instanceof CreateGame_Page_Controller ctrl) {
                List<OldGame> toShow;
                synchronized (pendingOldGames) {
                    toShow = new ArrayList<>(pendingOldGames);
                    pendingOldGames.clear();
                }
                ctrl.updateOldGamesList(toShow);
                System.out.println("[GUI] ListView aggiornata con " + toShow.size() + " giochi");
            } else {
                System.err.println("[GUI] CreateGame_Page_Controller non disponibile, rimando update");
            }
        });
    }

    /**
     * Sets the view component for the controller.
     * This method is intended for use solely by controllers to inject the GUI instance.
     *
     * @param view the GUI instance to be set as the view
     */
    @Override
    public void setView(GUI view) {
        // Questo metodo non serve in GUI stessa,
        // serve solo ai controller per iniettarsi la GUI.
    }

    /**
     * Updates the list of old games in the GUI if the related scene and controller are ready.
     *
     * This method processes the pending old games by transferring them to the UI for display.
     * If the "CREATE_GAME" scene and its corresponding controller are available, it retrieves
     * the list of pending games in a thread-safe manner, clears the list, and updates the UI
     * asynchronously on the JavaFX Application Thread using `Platform.runLater`.
     *
     * If the controller is not yet ready, the method schedules itself to retry after a
     * delay of 50 milliseconds until the scene and controller become available.
     */
    private void updateOldGamesIfReady() {
        SceneData sd = scenes.get("CREATE_GAME");
        if (sd != null && sd.controller instanceof CreateGame_Page_Controller ctrl) {
            List<OldGame> toShow;
            synchronized (pendingOldGames) {
                toShow = new ArrayList<>(pendingOldGames);
                pendingOldGames.clear();
            }
            Platform.runLater(() -> {
                ctrl.updateOldGamesList(toShow);
                System.out.println("[GUI] ListView aggiornata con " + toShow.size() + " giochi");
            });
        } else {
            // se non c’è ancora il controller, riprovo fra 50ms
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    updateOldGamesIfReady();
                }
            }, 50);
        }
    }

    /**
     * Loads and displays the initial user interface based on the current game level.
     *
     * This method determines the appropriate FXML file to use for the initial view
     * based on the game level provided by the model. It registers the selected scene with
     * its corresponding controller and ensures that the scene is properly initialized with
     * the necessary data before rendering it to the user.
     *
     * The method runs on the JavaFX Application Thread using `Platform.runLater` to ensure
     * UI updates are handled on the correct thread.
     *
     * @throws Exception if an error occurs during the scene registration or rendering process.
     */
    @Override
    public void showFirstSight() throws Exception {
        Platform.runLater(() -> {
            try {
                // 1) Scegli il FXML in base al livello
                int level = model.getLev();
                String key = "BUILD_PHASE";
                String fxml = level == 0
                        ? "/it/polimi/ingsw/fxml/flightBoardlevTest.fxml"
                        : "/it/polimi/ingsw/fxml/flightBoardlev2.fxml";

                // 2) Registra (o ricarica) la scena BUILD_PHASE
                registerScene(key, fxml, gv -> {
                    if (gv instanceof FlightBoard_Page_LevTest_Controller) {
                        ((FlightBoard_Page_LevTest_Controller) gv).initData(model);
                    } else {
                        ((FlightBoard_Page_Lev2_Controller) gv).initData(model);
                    }

                });

                // 3) Mostra la scena
                showScene(key);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Sets the static client instance for the GUI.
     *
     * @param client the GenericClient instance to be set as the static client
     */
    public static void setClientStatic(GenericClient client) {
        GUI.client = client;
    }


    /**
     * Sets the client for the GUI and updates the client with the model view.
     *
     * @param client the instance of GenericClient to be assigned and updated with the view
     */
    @Override
    public void setClient(GenericClient client) {
        GUI.client = client;
        client.setview(model);
    }

    /**
     * Sets the LittleModelRepresentation for the GUI.
     *
     * @param littleModelRepresentation the LittleModelRepresentation instance to be set as the current model for the GUI
     */
    @Override
    public void setLittleModelRepresentation(LittleModelRepresentation littleModelRepresentation) {
        GUI.model = littleModelRepresentation;
    }

    /**
     * Moves the specified player on the flightboard from the old position to the new position.
     *
     * @param player The player object to be moved.
     * @param newPos The new position on the flightboard where the player should be moved.
     * @param oldPos The current position of the player on the flightboard before the move.
     * @throws Exception If an error occurs during the player's movement on the flightboard.
     */
    @Override
    public void movePlayerOnFlightboard(Player player, int newPos, int oldPos) throws Exception {
        Platform.runLater(() -> {
            try {
                current().movePlayerOnFlightboard(player, newPos, oldPos);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Adds a tile to the specified position on the ship board.
     *
     * @param tile the tile to be added to the ship board
     * @param row the row index where the tile is to be added
     * @param col the column index where the tile is to be added
     * @param ship the ship board where the tile will be added
     * @throws Exception if an error occurs during the addition of the tile
     */
    @Override
    public void addTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        Platform.runLater(() -> {
            try {
                // delega al controller attivo
                current().addTile(tile, row, col, ship);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Removes a block of spaceship tiles from the given ship board.
     *
     * @param s the ship board from which the block will be removed
     * @param block the list of spaceship tiles that constitutes the block to be removed
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeBLock(ShipBoard s, ArrayList<SpaceShipTile> block) throws Exception {
        Platform.runLater(() -> {
            try {
                // delega al controller attivo
                current().removeBLock(s, block);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Updates the goods stored on the shipboard with the provided new goods configuration.
     *
     * @param s The shipboard instance where the goods are being updated.
     * @param storagetiles A 2D ArrayList representing storage tiles on the shipboard.
     * @param newgoods A 2D ArrayList containing the new goods to be updated in correlation with the storage tiles.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateGoods(ShipBoard s, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        Platform.runLater(() -> {
            try {
                // delega al controller attivo
                current().updateGoods(s, storagetiles, newgoods);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Updates the batteries on the ship board.
     *
     * @param BattxPos a nested list containing the battery positions
     * @param shipBoard an instance of the ShipBoard class where the batteries are updated
     * @param toLoose a boolean flag indicating whether to apply a specific behavior or state during the update
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateBatteries(ArrayList<ArrayList<Integer>> BattxPos, ShipBoard shipBoard, boolean toLoose) throws Exception {
        Platform.runLater(() -> {
            try {
                current().updateBatteries(BattxPos, shipBoard, toLoose);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Removes passengers from the specified ship board according to the given tiles and passenger count.
     *
     * @param s the ShipBoard instance from which passengers will be removed
     * @param tiles a nested ArrayList of Integer values representing the tiles from which passengers will be removed
     * @param numPass the number of passengers to be removed
     * @throws Exception if an error occurs during the removal of passengers
     */
    @Override
    public void removePassengers(ShipBoard s, ArrayList<ArrayList<Integer>> tiles, int numPass) throws Exception {
        Platform.runLater(() -> {
            try {
                current().removePassengers(s, tiles, numPass);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    /**
     * Removes a specified number of passengers from a given tile on the ship board.
     *
     * @param s the ShipBoard instance representing the board from which passengers are removed
     * @param tile the SpaceShipTile representing the tile to remove passengers from
     * @param numPass the number of passengers to be removed from the specified tile
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removePassengersFromTile(ShipBoard s, SpaceShipTile tile, int numPass) throws Exception {
        Platform.runLater(() -> {
            try {
                current().removePassengersFromTile(s, tile, numPass);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Removes an alien from the specified tile on the shipboard. This operation
     * is executed in a JavaFX application thread.
     *
     * @param s the ShipBoard instance from which the alien is to be removed
     * @param tile the SpaceShipTile representing the location of the alien to be removed
     * @throws Exception if the operation encounters an error during execution
     */
    @Override
    public void removeAlienFromTile(ShipBoard s, SpaceShipTile tile) throws Exception {
        Platform.runLater(() -> {
            try {
                current().removeAlienFromTile(s, tile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Retrieves the current GeneralView associated with the current key.
     *
     * @return the GeneralView instance linked to the currentKey.
     */
    private GeneralView current() {
        return scenes.get(currentKey).controller;
    }

    /**
     * Updates the specified "thing" in the hand of the given player.
     * The method schedules the update operation to be executed on the JavaFX application thread.
     *
     * @param p the player whose hand is to be updated
     * @param thing the object to update in the player's hand
     * @throws Exception if an error occurs during the update operation
     */
    @Override
    public void updateThingInHand(Player p, Object thing) throws Exception {
        Platform.runLater(() -> {
            try {
                // delega al controller attivo
                current().updateThingInHand(p, thing);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }


    /**
     * Adds a tile to the flipped state by delegating to the active controller.
     * Executes the operation on the JavaFX Application Thread.
     *
     * @param indextile the index of the tile to be flipped
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void addTileFlipped(int indextile) throws Exception {
        Platform.runLater(() -> {
            try {
                // delega al controller attivo
                current().addTileFlipped(indextile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Notifies the system that a player has deposited a small deck.
     * This method delegates the action to the currently active controller.
     *
     * @param d the deck that has been deposited
     * @param player the player who deposited the deck
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void someoneDepositedLittleDeck(Deck d, Player player) throws Exception {
        Platform.runLater(() -> {
            try {
                // delega al controller attivo
                current().someoneDepositedLittleDeck(d, player);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Handles the event where a player picks a little deck.
     * This method delegates the operation to the currently active controller.
     *
     * @param d      the deck that has been picked
     * @param player the player who picked the deck
     * @throws Exception if an error occurs during the handling of the event
     */
    @Override
    public void someonePickedLittleDeck(Deck d, Player player) throws Exception {
        Platform.runLater(() -> {
            try {
                // delega al controller attivo
                current().someonePickedLittleDeck(d, player);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Removes a flipped tile specified by the given tile identifier.
     * This method executes the removal operation on the JavaFX application thread.
     *
     * @param t the identifier of the tile to be removed
     * @throws Exception if an error occurs during the removal of the tile
     */
    @Override
    public void removeTileFlipped(int t) throws Exception {
        Platform.runLater(() -> {
            try {
                current().removeTileFlipped(t);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Adds a passenger to the specified position on the ship's board.
     *
     * @param s the ship's board where the passenger is to be added
     * @param row the row index on the board where the passenger is to be placed
     * @param column the column index on the board where the passenger is to be placed
     * @throws Exception if there is an error during the operation
     */
    @Override
    public void addPassenger(ShipBoard s, int row, int column) throws Exception {
        Platform.runLater(() -> {
            try {
                current().addPassenger(s, row, column);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Executes the `youPayConsequences` method on the current instance asynchronously on the JavaFX application thread.
     *
     * This method utilizes the `Platform.runLater` mechanism to ensure that the operation
     * is performed on the JavaFX thread, adhering to JavaFX's threading restrictions.
     * It captures any `Exception` and prints the stack trace for debugging purposes.
     *
     * Implementation Notes:
     * - The method retrieves the current instance using the `current()` method and delegates
     *   the `youPayConsequences` invocation to it.
     * - In case of an exception during execution, the stack trace is logged through the standard error stream.
     */
    @Override
    public void youPayConsequences() {
        Platform.runLater(() -> {
            try {
                current().youPayConsequences();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Sends a penalty of a specified type.
     *
     * @param penalty the numeric value of the penalty to be sent
     * @param type the type or category of the penalty
     */
    @Override
    public void sendPenalty(int penalty, String type) {

    }

    /**
     * Adds an alien to the specified shipboard at the given coordinates with the specified color.
     *
     * @param s          The shipboard object where the alien will be added.
     * @param row        The row on the shipboard where the alien will be placed.
     * @param column     The column on the shipboard where the alien will be placed.
     * @param alienColor The color of the alien to be added.
     * @throws Exception If an error occurs while adding the alien.
     */
    @Override
    public void addAlien(ShipBoard s, int row, int column, AlienColor alienColor) throws Exception {
        Platform.runLater(() -> {
            try {
                current().addAlien(s, row, column, alienColor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }


    /**
     * Updates the status of the underlying system or graphical user interface (GUI).
     * This method is intended to ensure that the current status is appropriately refreshed
     * or synchronized with any recent changes.
     *
     * @throws Exception if an error occurs during the status update operation.
     */
    @Override
    public void updateStatus() throws Exception {
        // Implementation needed
        System.out.println("GUI status updated");
    }

    /**
     * This method is triggered when an error occurs in the program's execution or flow.
     * It is designed to handle errors and exceptions by providing a fallback mechanism
     * or appropriate error-handling logic.
     *
     * @throws Exception if an error occurs during the execution of the error-handling logic.
     */
    @Override
    public void onError() throws Exception {

    }

    /**
     * Displays the final scores of the game in a new window.
     *
     * @param finalScores a map where the key represents the player's name and the value represents the player's final score
     * @throws Exception if an error occurs while loading the scoring phase view or updating the UI
     */
    @Override
    public void showFinalScore(HashMap<String, Float> finalScores) throws Exception {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/scoringPhase.fxml"));
                Parent root = loader.load();

                // Passo la view principale e la mappa punteggi al nuovo controller
                ScoringPhase_Page_Controller ctrl = loader.getController();
                ctrl.init(this);
                ctrl.setScores(finalScores);
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Final Score");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Causes the specified ship board to lose all of its goods.
     *
     * @param s the ship board whose goods will be removed
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void looseAllGoods(ShipBoard s) throws Exception {
        Platform.runLater(() -> {
            try {
                current().looseAllGoods(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Causes the specified ShipBoard to lose all its batteries.
     * This method executes the operation on the JavaFX Application Thread.
     *
     * @param s the ShipBoard instance that will lose its batteries
     * @throws Exception if any error occurs during the execution
     */
    @Override
    public void looseAllbatteries(ShipBoard s) throws Exception {
        Platform.runLater(() -> {
            try {
                current().looseAllbatteries(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Executes the middleEffect functionality.
     * This method is intended to perform intermediate processing or operations.
     * It can be overridden in subclasses to provide specific behavior.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    @Override
    public void middleEffect() throws Exception {

    }

    /**
     * Method indicating that an effect or process has been finalized or completed.
     * This method is intended to perform any necessary cleanup or final actions
     * required after the effect has ended.
     *
     * @throws Exception if an error occurs during the finalization process.
     */
    @Override
    public void endedEffect() throws Exception {

    }

    /**
     * Updates the card information in a platform-aware thread-safe manner.
     * This method ensures that the card update operation is executed on the JavaFX Application Thread.
     * Any exceptions encountered during the update process will be logged to the standard error stream.
     *
     * @throws Exception If an error occurs during the card update process.
     */
    @Override
    public void updateCard() throws Exception {
        Platform.runLater(() -> {
            try {
                current().updateCard();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Updates the cosmic credits for a given player.
     * This method modifies the player's cosmic credit balance by the specified amount.
     *
     * @param p the Player object whose cosmic credits are to be updated
     * @param i the amount by which the player's cosmic credits should be adjusted
     *           (positive to add credits, negative to deduct credits)
     * @throws Exception if the update operation fails or encounters an error
     */
    @Override
    public void updateCosmicCredits(Player p, int i) throws Exception {

    }

    /**
     * Updates the state of players currently participating in the game.
     *
     * This method performs necessary operations to synchronize or update the
     * player information for the ongoing game session. It may include tasks
     * such as updating scores, statuses, or other game-related attributes
     * for each player.
     *
     * @throws Exception if an error occurs during the update process.
     */
    @Override
    public void updatePlayersInGame() throws Exception {

    }

    /**
     * Updates the message display by showing an informational dialog with the specified message.
     *
     * @param message The message to be displayed in the dialog. Must not be null.
     * @throws Exception If an error occurs while attempting to display the dialog.
     */
    @Override
    public void updateMessageOnly(String message) throws Exception {
        showInfoDialog("Info!", message);
    }

    /**
     * Updates the remaining goods for the specified player.
     * This method executes the update operation on the JavaFX application thread
     * to ensure thread safety when interacting with the UI.
     *
     * @param p the player whose goods are to be updated
     * @param goodFInali the list of remaining goods to be updated for the player
     */
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) {
        Platform.runLater(() -> {
            try {
                current().updateGoodsRemaining(p, goodFInali);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Updates the current time by invoking the updateTime method on the current object.
     * This method ensures that updates to the time are executed on the JavaFX Application Thread
     * by using the Platform.runLater() method.
     *
     * The method handles any exceptions that occur during the update process by catching them
     * and printing the stack trace.
     *
     * @throws Exception if an error occurs during the execution of the updateTime method.
     */
    @Override
    public void updateTime() throws Exception {
        Platform.runLater(() -> {
            try {
                current().updateTime();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }


    /**
     * Displays the wrong tiles provided in the list for a specific user.
     *
     * @param tiles A list of integers representing the wrong tile indices.
     * @param nickname The nickname of the user associated with the wrong tiles.
     * @param nickEff The nickname effect or decoration associated with the user.
     * @throws Exception If an exception occurs during the execution of the method.
     */
    @Override
    public void showWrongTiles(ArrayList<Integer> tiles, String nickname, String nickEff) throws Exception {
        Platform.runLater(() -> {
            try {
                current().showWrongTiles(tiles, nickname, nickEff);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Updates the game state when a shot is received by a player.
     * This method processes the shot and applies the defense mechanism using provided parameters.
     *
     * @param player The player who is the recipient of the shot.
     * @param shot The shot that is being received.
     * @param howToDefenceFromShots An ArrayList of integers representing the defense mechanism or strategy against the shot.
     * @param dice The dice roll value associated with the defense or attack process.
     * @throws Exception If an error occurs during the handling of the shot or defense process.
     */
    @Override
    public void updateShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {

    }

    /**
     * Sends a message to sub-ships with specific information, likely about the current gameplay state or updates.
     *
     * @param subShip         A nested ArrayList containing SpaceShipTile elements representing the sub-structure of ships.
     * @param playerNickname  The nickname of the player associated with the message being sent.
     * @throws Exception      If an error occurs during message processing or communication.
     */
    @Override
    public void messageSubShips(ArrayList<ArrayList<SpaceShipTile>> subShip, String playerNickname) throws Exception {
        Platform.runLater(() -> {
            try {
                current().messageSubShips(subShip, playerNickname);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }


    /**
     * Handles the selection of a specific sub-ship from a list of sub-ships for a given player.
     *
     * @param playerNickname The nickname of the player who is choosing the sub-ship.
     * @param subShips A collection of sub-ship configurations available for selection.
     * @param indexToPreserve The index of the sub-ship that should be preserved during the selection process.
     * @param waste An additional parameter used during the selection process, representing a potential operation or value associated with the choice.
     * @throws Exception If an error occurs during the selection process.
     */
    @Override
    public void ChooseSubShip(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) throws Exception {

    }

    /**
     * Removes a single tile from the specified location in the game.
     *
     * @param playerNickname the nickname of the player performing the action
     * @param row the row index of the tile to be removed
     * @param col the column index of the tile to be removed
     * @param fromMistake a boolean indicating whether the removal is due to a mistake
     * @param waste the amount of waste generated by the removal
     * @throws Exception if an error occurs during tile removal
     */
    @Override
    public void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) throws Exception {
        Platform.runLater(() -> {
            try {
                current().removeSingleTile(playerNickname, row, col, fromMistake, waste);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Adds a wait tile to the specified location on the ship board.
     *
     * @param tile the space ship tile to be added
     * @param row the row index where the tile should be placed
     * @param col the column index where the tile should be placed
     * @param ship the ship board to which the tile belongs
     * @throws Exception if an error occurs while adding the tile
     */
    @Override
    public void addWaitTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        Platform.runLater(() -> {
            try {
                current().addWaitTile(tile, row, col, ship);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Ends the building phase for a player identified by their nickname.
     *
     * @param playerNick the nickname of the player who has finished the building phase
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void endBuilding(String playerNick) throws Exception {
        Platform.runLater(() -> {
            try {
                current().endBuilding(playerNick);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Executes the haveToFillTiles method on the current context using the JavaFX application thread.
     * This ensures that any UI-related operations or updates happen on the appropriate thread.
     *
     * @throws Exception if an error occurs during the execution of the haveToFillTiles method.
     */
    @Override
    public void haveToFillTiles() throws Exception {
        Platform.runLater(() -> {
            try {
                current().haveToFillTiles();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Removes a player from the flight board and displays a notification alerting of the player's loss.
     *
     * @param player The identifier or name of the player to be removed.
     * @param oldPos The current or previous position of the player on the flight board.
     * @throws Exception If an error occurs during the removal process or while displaying the alert.
     */
    @Override
    public void removePlayerFromFlightboard(String player, int oldPos) throws Exception {
        Platform.runLater(() -> {
            try {
                current().removePlayerFromFlightboard(player, oldPos);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("A PLAYER LOST");
                alert.setHeaderText(null);
                alert.setContentText("Player " + player + " lost!");
                Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
                dialogStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/images/logo.png"))));
                alert.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        });

    }

    /**
     * Displays an error dialog with the specified title and message.
     * The dialog is shown on the JavaFX Application Thread using Platform.runLater.
     *
     * @param title   the title of the error dialog
     * @param message the message content of the error dialog
     */
    public void showErrorDialog(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    /**
     * Displays an informational dialog with the provided title and message.
     * The dialog is executed on the JavaFX Application Thread using {@code Platform.runLater}.
     *
     * @param title   the title of the information dialog
     * @param message the content/message to be displayed in the information dialog
     */
    @Override
    public void showInfoDialog(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/images/logo.png"))));

            alert.showAndWait();
        });
    }

    /**
     * Displays a shutdown alert notifying the user of the application's termination.
     *
     * @param reason the reason for the shutdown, shown in the alert's content
     */
    @Override
    public void showShutdown(String reason) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Shutting down");
            alert.setHeaderText(null);
            alert.setContentText(reason + ". Click to exit the game");
            Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/images/logo.png"))));
            Optional<ButtonType> button = alert.showAndWait();
            if (button.isPresent() && button.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * Inserts a wait tile with LMR1 configuration for the specified player.
     *
     * @param playerNickname the nickname of the player for whom the wait tile is being inserted
     */
    @Override
    public void insertwaittileLMR1(String playerNickname) {
        Platform.runLater(() -> {
            try {
                current().insertwaittileLMR1(playerNickname);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * This method is responsible for handling the insertion of a "wait tile" for the specified player.
     * It is executed on the JavaFX Application Thread using Platform.runLater().
     *
     * @param playerNickname The nickname of the player for whom the wait tile should be inserted.
     */
    @Override
    public void insertwaittileLMR2(String playerNickname) {
        Platform.runLater(() -> {
            try {
                current().insertwaittileLMR2(playerNickname);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Processes and handles the given input message in a thread-safe manner.
     * Executes the input correction logic on the JavaFX Application Thread.
     *
     * @param message the input message to be processed and corrected
     */
    @Override
    public void correctinput(String message) {
        Platform.runLater(() -> {
            try {
                current().correctinput(message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Updates the game state to progress to the next player's turn.
     *
     * @param myNickname the nickname of the current player whose turn is being processed
     */
    @Override
    public void nextPlayerTurn(String myNickname) {
        Platform.runLater(() -> {
            try {
                current().correctinput(myNickname);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Displays an informational alert with a custom message and a timed duration before it fades and closes automatically.
     * The alert includes a styled window with a logo and optional animations for showing and dismissing it.
     *
     * @param text the message text to be displayed in the alert box
     * @param seconds the duration (in seconds) for which the alert should be visible before fading out
     */
    public void showTimedInfo(String text, int seconds) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info!");
            alert.setHeaderText(null);

            // Caricamento sicuro dell'icona
            InputStream stream = getClass().getResourceAsStream("/it/polimi/ingsw/graphics/images/logo.png");
            if (stream != null) {
                ImageView icon = new ImageView(new Image(stream));
                icon.setFitWidth(28);
                icon.setPreserveRatio(true);
                icon.setSmooth(true);
                alert.setGraphic(icon);
            }

            Label content = new Label(text);
            content.setWrapText(true);
            content.setStyle(
                    "-fx-text-fill: white;"
                            + "-fx-font-size: 14px;"
                            + "-fx-padding: 10 18 10 18;"
            );

            DialogPane pane = alert.getDialogPane();
            pane.setContent(content);
            pane.setPrefSize(320, 120);
            pane.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #093BB0, #2F1D7F);"
            );

            // Aggiungi l'icona alla title-bar DOPO la visualizzazione
            alert.setOnShown(e -> {
                Window window = pane.getScene().getWindow();
                if (window instanceof Stage stage) {
                    if (alert.getGraphic() != null) {
                        stage.getIcons().add(((ImageView) alert.getGraphic()).getImage());
                    }
                }
            });

            alert.show();

            // Animazioni
            PauseTransition stay = new PauseTransition(Duration.seconds(seconds));
            FadeTransition fade = new FadeTransition(Duration.millis(400), pane);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            stay.setOnFinished(e -> fade.play());
            fade.setOnFinished(e -> alert.close());
            stay.play();
        });
    }

    /**
     * Updates the number of batteries remaining for a specified player.
     *
     * @param p the player whose battery count is to be updated
     * @param batt the number of batteries remaining to be set for the player
     */
    @Override
    public void updateBatteriesRemaining(Player p, int batt) {
        Platform.runLater(() -> {
            try {
                current().updateBatteriesRemaining(p, batt);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}