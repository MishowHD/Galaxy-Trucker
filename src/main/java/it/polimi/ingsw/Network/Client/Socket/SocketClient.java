package it.polimi.ingsw.Network.Client.Socket;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.View.Utils_View.CommandType;
import it.polimi.ingsw.Network.Server.Socket.VirtualViewSocket;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Utils.stateEnum;

import java.io.*;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketClient extends GenericClient implements VirtualViewSocket {
    /**
     * An input stream used to deserialize objects from a binary stream.
     * This is a private and final member, ensuring that the object it references
     * remains constant and cannot be reassigned after initialization.
     */
    private final ObjectInputStream input;
    /**
     * A protected static instance representing a view of the LittleModelRepresentation.
     * This variable can be shared across different parts of the application that require
     * access to the LittleModelRepresentation.
     */
    //private final int level;
    protected static LittleModelRepresentation view;
    /**
     * Indicates whether the username has been accepted.
     * This variable is volatile to ensure visibility of changes across threads.
     * It is initialized to false and can be updated during the application flow
     * when the username validation process is completed successfully.
     */
    private volatile boolean usernameAccepted = false;
    /**
     * A flag indicating whether the provided username has been rejected.
     * This variable is marked as volatile to ensure that changes to its
     * value are immediately visible to all threads, ensuring thread safety.
     * It is initialized to {@code false} by default.
     */
    private volatile boolean usernameRejected = false;
    /**
     * This variable represents an instance of `SocketServerHandler`.
     * It is marked as `final`, indicating that its reference cannot be changed once initialized.
     * The `output` variable could be used to manage server-side socket communication
     * or to handle operations related to socket connections.
     */
    final SocketServerHandler output;
    /**
     * A scheduled executor service responsible for periodically
     * executing tasks related to maintaining or monitoring the system's
     * heartbeat functionality. This executor operates with a single
     * thread to ensure sequential execution of scheduled tasks.
     */
    private final ScheduledExecutorService heartbeat = Executors.newSingleThreadScheduledExecutor();

    /**
     * Checks if the username has been accepted.
     *
     * @return true if the username is accepted, false otherwise
     */
    public boolean isUsernameAccepted() {
        return usernameAccepted;
    }

    /**
     * Checks if the username has been rejected.
     *
     * @return true if the username is rejected; false otherwise
     */
    public boolean isUsernameRejected() {
        return usernameRejected;
    }

    /**
     * Resets the state of the username flags.
     * <p>
     * This method sets the flags associated with username acceptance and rejection to false.
     * It is typically used to clear any prior state related to username validation
     * before performing a new operation or validation process.
     */
    public void resetUsernameFlags() {
        usernameAccepted = false;
        usernameRejected = false;
    }

    /**
     * Stores the timestamp of the last successful server ping in milliseconds.
     * The value is initialized to the system's current time when the instance is created.
     * This variable is marked as volatile to ensure visibility of updates across threads.
     */
    private volatile long lastServerPing = System.currentTimeMillis();

    /**
     * Constructs a new SocketClient instance, initializing the view model,
     * input stream, and output stream handler.
     *
     * @param lm the LittleModelRepresentation instance used for the client view
     * @param input the input stream for receiving data
     * @param output the output stream for sending data
     * @throws RemoteException if a remote communication error occurs
     */
    public SocketClient(LittleModelRepresentation lm, ObjectInputStream input, ObjectOutputStream output) throws RemoteException {
        super();
        view = lm;
        setModelToInputManager(lm);
        this.input = input;
        //  this.level = Lev;
        this.output = new SocketServerHandler(output);
    }

    /**
     * Starts a periodic heartbeat task to monitor the connection with the server.
     *
     * The method schedules a repeating task that:
     * 1. Sends a ping signal to the server at regular intervals.
     * 2. Checks if the server has responded within the timeout period.
     *
     * If the server does not respond within the specified timeout period, or if an
     * IOException occurs while sending the ping, the connection will be terminated
     * and a shutdown sequence will be initiated with an appropriate reason.
     *
     * The heartbeat task is initiated with a fixed delay interval of 5 seconds between
     * successive executions.
     */
    private void startHeartbeat() {
        final long TIMEOUT = 15_000;     // 15s
        heartbeat.scheduleWithFixedDelay(() -> {
            long now = System.currentTimeMillis();
            try {
                output.ping();
            } catch (IOException e) {
                shutdown("Server Disconnected");
            }
            if (now - lastServerPing > TIMEOUT) {
                shutdown("Server unreachable (timeout)");
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * Executes the process to start a virtual server in a new thread,
     * sets the username for output handling, and initiates a heartbeat mechanism.
     *
     * @param username the username to be set for output handling
     * @throws RemoteException if a remote call error occurs
     */
    public void run(String username) throws RemoteException {

        new Thread(
                () -> {
                    try {
                        runVirtualServer();
                    } catch (Exception _) {
                    }
                }
        ).start();
        output.setusername(username);
        startHeartbeat();

    }


    /**
     * Handles the execution of the virtual server by processing incoming commands
     * and delegating to appropriate methods based on the command type. This method
     * listens for commands on an input stream, processes their associated data, and
     * performs actions as specified by the received instructions.
     *
     * @throws Exception if an error occurs while reading input or performing any of the actions
     *
     * Command Processing:
     * - "username_ack": Marks the username as accepted.
     * - "username_taken": Marks the username as rejected.
     * - "shutdown": Terminates the server with the specified shutdown message.
     * - "error": Triggers a generic error handling routine.
     * - "abandonedShipTakenActivate": Activates abandoned ship functionality with player and grid data.
     * - "addAlienOrHumansLMR": Adds aliens or humans with specified characteristics to the game.
     * - "updateGameCreated": Updates the details of an already created game.
     * - "depositThingInHand": Deposits an item into the player's hand.
     * - "pickTileLMR": Picks a tile based on player and tile IDs.
     * - "insertTileLMR": Inserts a tile into the game at the specified position.
     * - "pickLittleDeckLMR": Allows a player to pick a smaller deck by its ID.
     * - "setPlayerPosInFlightBoard": Updates the player's position on the flight board.
     * - "endBuildingPhaseForAll": Signals the end of the building phase for all players.
     * - "sendPenalty": Enforces a specified penalty on a player.
     * - "completeBuildingPhase": Completes the building phase for the game.
     * - "updateCardUse": Updates the status of a card being used by a player.
     * - "updateCardUseSTATE": Updates the state of a card.
     * - "updateStatus": Updates the status of the game to the provided state.
     * - "updateFinalScores": Finalizes and updates the scores for all players.
     * - "chooseAbandonedStationActivate": Handles activation of abandoned stations.
     * - "updateAssertBatteriesPos": Adjusts battery positions for a specified player.
     * - "updateAddGoods": Updates goods associated with a player or a board section.
     * - "lostBatteries": Handles battery losses for a player.
     * - "updateChoosePassengersToLose": Updates decisions made about passengers during losses.
     * - "planetFinStatActivate": Activates final planetary station statistics.
     * - "updateConsequenceLostDays": Updates consequences related to days lost for a player.
     * - "updateLooseAllGoods": Handles scenarios where a player loses all goods.
     * - "updateSmugglersCalc": Manages smuggler calculations for a player.
     * - "updateLostBatteriesSmug": Updates data on battery losses during a smuggler encounter.
     * - "updateLostGoodsSmug": Updates data on goods lost during a smuggler encounter.
     * - "chooseToClaimRewardSmug": Processes the choice to claim smuggler-related rewards.
     * - "slaversChooseCannonBatteryPos": Handles cannon and battery positions for a slaver's interaction.
     * - "slaversChoosePassengersToLose": Processes choices related to passengers during a slaver's encounter.
     * - "piratesChooseCannonBatteryPos": Handles cannon and battery positions for pirate encounters.
     * - "piratesChooseHowToFaceMeteors": Processed player choices related to meteor interactions.
     * - "piratesChooseToClaimReward": Handles reward claiming in pirate interactions.
     * - "movePlayerInFlightBoard": Adjusts player position on the flight board based on movement input.
     * - "openSpaceChooseToStartMotor": Processes the player's choice to start a motor in open space scenarios.
     */
    public void runVirtualServer() throws Exception {
        String json;
        Object[] args;
        while ((json = (String) input.readObject()) != null) {
            switch (json) {
                case "username_ack":
                    this.usernameAccepted = true;
                    break;
                case "username_taken":
                    this.usernameRejected = true;
                    break;
                case "shutdown":
                    args = (Object[]) input.readObject();
                    shutdown((String) args[0]);
                    break;
                case "error":
                    this.error();
                    break;
                case "abandonedShipTakenActivate":
                    args = (Object[]) input.readObject();
                    this.abandonedShipTakenActivate((Player) args[0], (ArrayList<ArrayList<Integer>>) args[1]);
                    break;

                case "addAlienOrHumansLMR":
                    args = (Object[]) input.readObject();
                    this.addAlienOrHumansLMR((String) args[0], (boolean) args[1], (AlienColor) args[2], (int) args[3], (int) args[4]);
                    break;

                case "updateGameCreated":
                    args = (Object[]) input.readObject();
                    this.updateGameCreated((int) args[0], (List<Deck>) args[1], (ArrayList<Player>) args[2], (int) args[3], (int) args[3]);
                    break;

                case "depositThingInHand":
                    args = (Object[]) input.readObject();
                    this.depositThingInHand((String) args[0]);
                    break;

                case "pickTileLMR":
                    args = (Object[]) input.readObject();
                    this.pickTileLMR((int) args[0], (String) args[1]);
                    break;

                case "insertTileLMR":
                    args = (Object[]) input.readObject();
                    this.insertTileLMR((int) args[0], (int) args[1], (int) args[2], (int) args[3], (String) args[4]);
                    break;

                case "pickLittleDeckLMR":
                    args = (Object[]) input.readObject();
                    this.pickLittleDeckLMR((int) args[0], (String) args[1]);
                    break;

                case "setPlayerPosInFlightBoard":
                    args = (Object[]) input.readObject();
                    this.setPlayerPosInFlightBoard((String) args[0], (int) args[1]);
                    break;

                case "endBuildingPhaseForAll":
                    this.endBuildingPhaseForAll();
                    break;

                case "sendPenalty":
                    args = (Object[]) input.readObject();
                    this.sendPenalty((int) args[0], (String) args[1]);
                    break;

                case "completeBuildingPhase":
                    this.completeBuildingPhase();
                    break;

                case "updateCardUse":
                    args = (Object[]) input.readObject();
                    this.updateCardUse((Card) args[0]);
                    break;
                case "updateCardUseSTATE":
                    args = (Object[]) input.readObject();
                    this.updateCardUseSTATE((c_State) args[0]);
                    break;

                case "updateStatus":
                    args = (Object[]) input.readObject();
                    this.updateStatus((stateEnum) args[0]);
                    break;

                case "updateFinalScores":
                    args = (Object[]) input.readObject();
                    this.updateFinalScores((HashMap<String, Float>) args[0]);
                    break;

                case "chooseAbandonedStationActivate":
                    args = (Object[]) input.readObject();
                    this.chooseAbandonedStationActivate((Player) args[0], (FlightBoard) args[1], (boolean) args[2],
                            (ArrayList<ArrayList<Integer>>) args[3], (ArrayList<ArrayList<Goods>>) args[4]);
                    break;

                case "updateAssertBatteriesPos":
                    args = (Object[]) input.readObject();
                    this.updateAssertBatteriesPos((Player) args[0], (ArrayList<ArrayList<Integer>>) args[1]);
                    break;

                case "updateAddGoods":
                    args = (Object[]) input.readObject();
                    this.updateAddGoods((Player) args[0], (ArrayList<ArrayList<Integer>>) args[1], (ArrayList<ArrayList<Goods>>) args[2]);
                    break;
                case "lostBatteries":
                    args = (Object[]) input.readObject();
                    this.lostBatteries((Player) args[0], (ArrayList<ArrayList<Integer>>) args[1]);
                    break;

                case "updateChoosePassengersToLose":
                    args = (Object[]) input.readObject();
                    this.updateChoosePassengersToLose((Player) args[0], (Consequences) args[1], (ArrayList<ArrayList<Integer>>) args[2]);
                    break;

                case "planetFinStatActivate":
                    args = (Object[]) input.readObject();
                    this.planetFinStatActivate((ArrayList<Player>) args[0], (FlightBoard) args[1]);
                    break;

                case "updateConsequenceLostDays":
                    args = (Object[]) input.readObject();
                    this.updateConsequenceLostDays((Player) args[0], (FlightBoard) args[1], (int) args[2], (Boolean) args[3]);
                    break;

                case "updateLooseAllGoods":
                    args = (Object[]) input.readObject();
                    this.updateLooseAllGoods((Player) args[0], (Boolean) args[1], (Boolean) args[2], (Boolean) args[3]);
                    break;


                case "updateSmugglersCalc":
                    args = (Object[]) input.readObject();
                    this.updateSmugglersCalc((Player) args[0], (ArrayList<ArrayList<Integer>>) args[1], (ArrayList<ArrayList<Integer>>) args[2]);
                    break;

                case "updateLostBatteriesSmug":
                    args = (Object[]) input.readObject();
                    this.updateLostBatteriesSmug((Player) args[0], (ArrayList<ArrayList<Integer>>) args[1], (int) args[2]);
                    break;

                case "updateLostGoodsSmug":
                    args = (Object[]) input.readObject();
                    this.updateLostGoodsSmug((Player) args[0], (ArrayList<ArrayList<Integer>>) args[1],
                            (ArrayList<ArrayList<Goods>>) args[2], (ArrayList<Goods>) args[3]);
                    break;

                case "chooseToClaimRewardSmug":
                    args = (Object[]) input.readObject();
                    this.chooseToClaimRewardSmug((boolean) args[0], (Player) args[1],
                            (ArrayList<ArrayList<Integer>>) args[2], (ArrayList<ArrayList<Goods>>) args[3]);
                    break;

                case "slaversChooseCannonBatteryPos":
                    args = (Object[]) input.readObject();
                    this.slaversChooseCannonBatteryPos((Player) args[0],
                            (ArrayList<ArrayList<Integer>>) args[1], (ArrayList<ArrayList<Integer>>) args[2]);
                    break;

                case "slaversChoosePassengersToLose":
                    args = (Object[]) input.readObject();
                    this.slaversChoosePassengersToLose((Player) args[0], (boolean) args[1], (ArrayList<ArrayList<Integer>>) args[2]);
                    break;

                case "piratesChooseCannonBatteryPos":
                    args = (Object[]) input.readObject();
                    this.piratesChooseCannonBatteryPos((Player) args[0],
                            (ArrayList<ArrayList<Integer>>) args[1], (ArrayList<ArrayList<Integer>>) args[2]);
                    break;

                case "piratesChooseHowToFaceMeteors":
                    args = (Object[]) input.readObject();
                    this.piratesChooseHowToFaceMeteors((Player) args[0],
                            (ArrayList<Integer>) args[1], (Shot) args[2], (int) args[3]);
                    break;

                case "piratesChooseToClaimReward":
                    args = (Object[]) input.readObject();
                    this.piratesChooseToClaimReward((boolean) args[0], (Player) args[1]);
                    break;

                case "movePlayerInFlightBoard":
                    args = (Object[]) input.readObject();
                    this.movePlayerInFlightBoard((String) args[0], (int) args[1]);
                    break;

                case "openSpaceChooseToStartMotor":
                    args = (Object[]) input.readObject();
                    this.openSpaceChooseToStartMotor((Player) args[0], (FlightBoard) args[1],
                            (ArrayList<ArrayList<Integer>>) args[2], (ArrayList<ArrayList<Integer>>) args[3]);
                    break;

                case "meteorCardChooseHowToFaceMeteors":
                    args = (Object[]) input.readObject();
                    this.meteorCardChooseHowToFaceMeteors((Player) args[0], (ArrayList<Integer>) args[1],
                            (ArrayList<Shot>) args[2], (int) args[3], (int) args[4]);
                    break;

                case "epidemicStateBaseActivate":
                    args = (Object[]) input.readObject();
                    this.epidemicStateBaseActivate((Set<SpaceShipTile>) args[0]);
                    break;

                case "stardustEffect":
                    this.stardustEffect();
                    break;

                case "updateGoodsRemaining":
                    args = (Object[]) input.readObject();
                    this.updateGoodsRemaining((Player) args[0], (ArrayList<Goods>) args[1]);
                    break;

                case "notYourTurn":
                    this.notYourTurn();
                    break;

                case "timerNotStarted":
                    this.timerNotStarted();
                    break;

                case "tileNotFlipped":
                    this.tileNotFlipped();
                    break;

                case "noSurrender":
                    this.noSurrender();
                    break;

                case "nextPlayerTurn":
                    this.nextPlayerTurn();
                    break;

                case "wrongInput":
                    this.wrongInput();
                    break;

                case "correctInput":
                    this.correctInput();
                    break;

                case "wrongPlayer":
                    this.wrongPlayer();
                    break;

                case "effectStarted":
                    this.effectStarted();
                    break;

                case "someoneWon":
                    args = (Object[]) input.readObject();
                    this.someoneWon((String) args[0]);
                    break;

                case "tie":
                    args = (Object[]) input.readObject();
                    this.tie((String) args[0]);
                    break;

                case "lost":
                    args = (Object[]) input.readObject();
                    this.lost((String) args[0]);
                    break;

                case "refusedReward":
                    args = (Object[]) input.readObject();
                    this.refusedReward((String) args[0]);
                    break;

                case "effectEnded":
                    this.effectEnded();
                    break;

                case "lostGoods":
                    this.lostGoods();
                    break;

                case "youPayConsequences":
                    this.youPayConsequences();
                    break;

                case "updateDice":
                    args = (Object[]) input.readObject();
                    this.updateDice((int) args[0]);
                    break;

                case "cannotDeposit":
                    this.cannotDeposit();
                    break;

                case "cannotInsert":
                    this.cannotInsert();
                    break;

                case "cannotPick":
                    this.cannotPick();
                    break;

                case "cannotFill":
                    this.cannotFill();
                    break;

                case "blocked":
                    this.blocked();
                    break;

                case "stopBuilding":
                    this.stopBuilding();
                    break;

                case "timerStarted":
                    this.timerStarted();
                    break;

                case "timerAlreadyStarted":
                    this.timerAlreadyStarted();
                    break;

                case "removeBlock":
                    args = (Object[]) input.readObject();
                    this.removeBlock((ArrayList<SpaceShipTile>) args[0], (String) args[1]);
                    break;

                case "endbuilding":
                    args = (Object[]) input.readObject();
                    this.endbuilding((String) args[0], (int) args[1]);
                    break;
                case "wrongTiles":
                    args = (Object[]) input.readObject();
                    this.wrongTiles((ArrayList<Integer>) args[0], (String) args[1], view.getMyNickname());
                    break;
                case "updateManageShotReceived":
                    args = (Object[]) input.readObject();
                    this.updateManageShotReceived((Player) args[0], (Shot) args[1], (ArrayList<Integer>) args[2], (Integer) args[3]);
                    break;
                case "timerEnded":
                    args = (Object[]) input.readObject();
                    this.timerEnded((Boolean) args[0]);
                    break;
                case "messageToChooseSubship":
                    args = (Object[]) input.readObject();
                    this.messageToChooseSubship((Player) args[0], (ArrayList<ArrayList<SpaceShipTile>>) args[1]);
                    break;
                case "ChooseSubship":
                    args = (Object[]) input.readObject();
                    this.ChooseSubship((String) args[0], (ArrayList<ArrayList<SpaceShipTile>>) args[1], (int) args[2], (int) args[3]);
                    break;
                case "removeSingleTile":
                    args = (Object[]) input.readObject();
                    this.removeSingleTile((String) args[0], (int) args[1], (int) args[2], (boolean) args[3], (int) args[4]);
                    break;
                case "addTileToWaitList":
                    args = (Object[]) input.readObject();
                    this.addTileToWaitList((String) args[0], (int) args[1]);
                    break;
                case "insertWaitTileLMR":
                    args = (Object[]) input.readObject();
                    this.insertWaitTileLMR((String) args[0], (int) args[1]);
                    break;
                case "haveToFillEmptyTiles":
                    this.haveToFillEmptyTiles();
                    break;
                case "removePlayerFromFlightboard":
                    args = (Object[]) input.readObject();
                    this.removePlayerFromFlightboard(String.valueOf(args[0]));
                    break;
                case "slaversChooseToClaimReward":
                    args = (Object[]) input.readObject();
                    this.slaversChooseToClaimReward((Player) args[0], (boolean) args[1]);
                    break;
                case "changeID":
                    args = (Object[]) input.readObject();
                    this.changeID((UUID) args[0]);
                    break;
                case "showGames":
                    args = (Object[]) input.readObject();
                    this.showGames((ArrayList<OldGame>) args[0]);
                    break;
                case "ping":
                    lastServerPing = System.currentTimeMillis();
                    output.sendPong();
                    break;
                case "pong":
                    lastServerPing = System.currentTimeMillis();
                    break;
                case "notEnoughGoods":
                    this.notEnoughGoods();
                    break;
                case "updateBatteriesRemaining":
                    args = (Object[]) input.readObject();
                    this.updateBatteriesRemaining((Player) args[0], (Integer) args[1]);
                    break;
                case "removeAlien":
                    args = (Object[]) input.readObject();
                    this.removeAlien((String) args[0], (int) args[1], (int) args[2]);
                default:
                    System.out.println("Unknown command received: " + json);
            }
        }
    }

    /**
     * Initiates the start process of the application or service.
     * The behavior or actions taken during the start process
     * are determined by the specific implementation of this method.
     *
     * This method may set up necessary resources, initialize components,
     * or perform other operations required to prepare the application or service
     * for its intended use. Its execution blocks may depend on the context
     * or specific requirements defined in the implementation.
     *
     * Note: Ensure that all dependencies or pre-requisites are met
     * before invoking this method to avoid unexpected behavior or errors.
     */
    public void start() {
    }


    /**
     * Shuts down the system by closing resources and terminating processes.
     *
     * @param reason The reason for the shutdown, which can be used for logging or debugging purposes.
     */
    public void shutdown(String reason) {
        try {
            input.close();
            output.close();
            heartbeat.shutdownNow();
            view.shutdown(reason);

        } catch (Exception _) {
        }
    }

    /**
     * Activates the abandoned ship taken event for the specified player.
     *
     * @param player the player who has taken the abandoned ship
     * @param posPers a 2D list of integers representing positions or characteristics
     *                associated with the abandoned ship
     * @throws Exception if an error occurs during the activation process
     */
    @Override
    public void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers) throws Exception {
        view.abandonedShipTakenActivate(player, posPers);
    }

    /**
     * Adds an alien or a human to the game at the specified location.
     *
     * @param playerNickname the nickname of the player adding the entity
     * @param wantAlien a boolean indicating whether to add an alien (true) or a human (false)
     * @param alienColor the color of the alien being added, ignored if adding a human
     * @param row the row position where the entity should be added
     * @param column the column position where the entity should be added
     * @throws Exception if there is an error during the addition process
     */
    @Override
    public void addAlienOrHumansLMR(String playerNickname, boolean wantAlien, AlienColor alienColor, int row, int column) throws Exception {
        view.addAlienOrHumansLMR(playerNickname, wantAlien, alienColor, row, column);
    }


    /**
     * Activates the abandoned station selection process for the specified player, flight board, and state.
     *
     * @param player        The player object initiating the abandoned station activation.
     * @param flightBoard   The flight board associated with the current game state.
     * @param yOn           A boolean indicating whether a specific state or condition is active.
     * @param storagetiles  A 2D list representing the current state of storage tiles.
     * @param newgoods      A 2D list containing goods to be processed or added during the activation.
     * @throws Exception    Throws an exception if an error occurs during the activation process.
     */
    @Override
    public void chooseAbandonedStationActivate(Player player, FlightBoard flightBoard, boolean yOn, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        view.chooseAbandonedStationActivate(player, flightBoard, yOn, storagetiles, newgoods);
    }

    /**
     * Updates the management of a shot received by a player during the game.
     *
     * @param player The player who received the shot.
     * @param shot The shot details that need to be processed.
     * @param howToDefenceFromShots List indicating ways the player can defend against the shot.
     * @param dice The dice value used for determining the result of the shot.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {
        view.updateManageShotReceived(player, shot, howToDefenceFromShots, dice);
    }

    /**
     * Handles the process of claiming rewards for smuggling based on the specified boolean flag
     * and updates the player's storage tiles and goods list accordingly.
     *
     * @param yOn a boolean flag indicating whether the reward claiming process is active or not
     * @param player the player object for which the reward claiming process is performed
     * @param storagetiles a 2D list of integers representing the storage tiles associated with the player
     * @param newgoods a 2D list of Goods objects to be processed or updated during the reward claiming process
     * @throws Exception if any error occurs during the execution of the reward claiming process
     */
    @Override
    public void chooseToClaimRewardSmug(boolean yOn, Player player, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        view.chooseToClaimRewardSmug(yOn, player, storagetiles, newgoods);
    }


    /**
     * Handles the process of correcting input by delegating the call
     * to the corresponding method in the view layer. This method acts
     * as a bridge to ensure the input is correctly validated or modified
     * as needed.
     *
     * @throws Exception if an error occurs during the input correction process
     */
    @Override
    public void correctInput() throws Exception {
        view.correctInput();
    }

    /**
     * Deposits the thing currently held in hand by the specified user.
     *
     * @param nick the nickname of the user holding the thing to be deposited
     * @throws Exception if an error occurs during the deposit process
     */
    @Override
    public void depositThingInHand(String nick) throws Exception {
        view.depositThingInHand(nick);
    }

    /**
     * Handles the end of an effect by delegating the call to the associated view's effectEnded method.
     * This method ensures that the completion of the effect is properly propagated to the view layer.
     *
     * @throws Exception if an error occurs during the execution of the associated view's effectEnded method
     */
    @Override
    public void effectEnded() throws Exception {
        view.effectEnded();
    }

    /**
     * Ends the building phase for a given player and determines the next position to transition to.
     *
     * @param playerNickname the nickname of the player who is ending the building phase
     * @param positionwheretogo the position to transition to after the building phase ends
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void endbuilding(String playerNickname, int positionwheretogo) throws Exception {
        view.endbuilding(playerNickname, positionwheretogo);
    }

    /**
     * Sends a message to the user prompting them to choose a sub-ship from the available options.
     *
     * @param player The player who is required to make a choice.
     * @param subShips A list of lists representing the available sub-ship options.
     * @throws Exception if an error occurs while attempting to send the message.
     */
    @Override
    public void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips) throws Exception {
        view.messageToChooseSubship(subShips, player.getUsername());
    }

    /**
     * Allows a player to choose a subset of spaceship tiles.
     *
     * @param playerNickname The nickname of the player making the selection.
     * @param subShips A list of subship configurations represented as a nested list of SpaceShipTile objects.
     * @param choice The index of the chosen subship configuration from the subShips list.
     * @param waste An integer representing any unused or discarded resources during the selection process.
     * @throws Exception If an error occurs during the subship selection operation.
     */
    public void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste) throws Exception {
        view.ChooseSubship(playerNickname, subShips, choice, waste);
    }

    /**
     * Removes a single tile from the specified position on the board for the given player.
     *
     * @param playerNickname the nickname of the player from whom the tile will be removed
     * @param row the row index of the tile to be removed
     * @param column the column index of the tile to be removed
     * @param fromMistake indicates whether the removal is due to a mistake or not
     * @param waste the number of tiles to be added to the waste after removal
     * @throws Exception if the operation cannot be completed due to an error
     */
    public void removeSingleTile(String playerNickname, int row, int column, boolean fromMistake, int waste) throws Exception {
        view.removeSingleTile(playerNickname, row, column, fromMistake, waste);
    }

    /**
     * Adds a tile identified by its index to the waitlist for a specific player.
     *
     * @param playerNickname the nickname of the player to whom the tile belongs
     * @param TileIndex the index of the tile to be added to the waitlist
     * @throws Exception if an error occurs while adding the tile to the waitlist
     */
    public void addTileToWaitList(String playerNickname, int TileIndex) throws Exception {
        view.addTileToWaitList(playerNickname, TileIndex);
    }

    /**
     * Inserts a wait tile for a specific player by their nickname and tile index.
     *
     * @param playerNickname the nickname of the player for whom the wait tile is to be inserted
     * @param TileIndex the index of the tile to be inserted
     * @throws Exception if an error occurs during the insertion of the wait tile
     */
    @Override
    public void insertWaitTileLMR(String playerNickname, int TileIndex) throws Exception {
        view.insertWaitTileLMR(playerNickname, TileIndex);
    }

    /**
     * Ensures that empty tiles on the user interface are filled as required.
     * This method is typically responsible for invoking the corresponding functionality
     * in the view layer to handle the UI updates necessary for filling empty tiles.
     *
     * @throws Exception if an error occurs during the process of filling tiles.
     */
    @Override
    public void haveToFillEmptyTiles() throws Exception {
        view.haveToFillEmptyTiles();
    }

    /**
     * Removes a player from the flightboard based on their nickname.
     *
     * @param playerNickname the nickname of the player to be removed from the flightboard
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removePlayerFromFlightboard(String playerNickname) throws Exception {
        view.removePlayerFromFlightboard(playerNickname);
    }

    /**
     * Allows slavers to claim rewards based on the player's choice.
     *
     * @param player The player making the decision to claim the reward or not.
     * @param accept A boolean indicating whether the player accepts the reward (true) or declines it (false).
     * @throws Exception if an error occurs during the process of claiming the reward.
     */
    @Override
    public void slaversChooseToClaimReward(Player player, boolean accept) throws Exception {
        view.slaversChooseToClaimReward(accept, player);
    }

    /**
     * Displays the list of old games and populates the games map with their details.
     *
     * @param oldGames a list of OldGame objects representing the old games to be displayed
     * @throws Exception if an error occurs while displaying the games
     */
    @Override
    public void showGames(ArrayList<OldGame> oldGames) throws Exception {
        view.getGeneralView().showOldGames(oldGames);
        games.clear();
        for (OldGame og : oldGames) {
            games.put(og.getN(), og.getUuid());
        }
    }

    /**
     * Indicates that there are not enough goods available to perform the desired operation.
     * This method delegates to the view layer to handle the specific representation or action
     * for insufficient goods.
     *
     * @throws Exception if an error occurs during the execution of the operation.
     */
    @Override
    public void notEnoughGoods() throws Exception {
        view.notEnoughGoods();
    }

    /**
     * Updates the remaining number of batteries for a given player.
     * This method delegates the update operation to the associated view component.
     *
     * @param p The player for whom the battery count is to be updated.
     * @param batt The new number of remaining batteries to be set for the player.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateBatteriesRemaining(Player p, int batt) throws Exception {
        view.updateBatteriesRemaining(p, batt);
    }

    /**
     * Removes an alien from a specified position in the game grid.
     *
     * @param username the name of the user requesting the removal
     * @param r the row index of the alien's position
     * @param c the column index of the alien's position
     * @throws Exception if an error occurs during the removal operation
     */
    @Override
    public void removeAlien(String username, int r, int c) throws Exception {
        view.removeAlien(username, r, c);
    }

    /**
     * Handles the process of losing batteries for the specified player based on the given list of batteries.
     *
     * @param p The player who is losing the batteries.
     * @param batteriesToAct A nested list of integers representing the batteries that are being lost.
     * @throws Exception If an error occurs during the process.
     */
    @Override
    public void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct) throws Exception {
        view.lostBatteries(p, batteriesToAct);
    }

    /**
     * Handles the start of an effect by delegating the call to the corresponding view implementation.
     *
     * This method is triggered when an effect begins, and it ensures that the associated view
     * is notified by invoking its `effectStarted` method. Any exceptions encountered during
     * this operation are propagated to the caller.
     *
     * @throws Exception if an error occurs during the effect start process in the view.
     */
    @Override
    public void effectStarted() throws Exception {
        view.effectStarted();
    }

    /**
     * Signals the end of the building phase for all participants or entities within the context
     * managed by the view. This method invokes the corresponding functionality in the
     * associated view component to finalize the building phase.
     *
     * @throws Exception if an error occurs during the process of ending the building phase.
     */
    @Override
    public void endBuildingPhaseForAll() throws Exception {
        view.endBuildingPhaseForAll();
    }

    /**
     * Sends a penalty with the specified value and type.
     *
     * @param penalty the penalty value to be sent
     * @param type the type of penalty to be sent
     * @throws Exception if an error occurs while sending the penalty
     */
    @Override
    public void sendPenalty(int penalty, String type) throws Exception {
        view.sendPenalty(penalty, type);
    }

    /**
     * Handles the event when a specific user is marked as "lost" in the system.
     * This method delegates the action to the view layer.
     *
     * @param nick the nickname of the user who is identified as lost
     * @throws Exception if an error occurs while processing the event
     */
    @Override
    public void lost(String nick) throws Exception {
        view.lost(nick);
    }

    /**
     * Activates the base epidemic state operation for the given set of already visited spaceship tiles.
     *
     * @param AlreadyVisited a set of SpaceShipTile objects that have already been visited
     * @throws Exception if an error occurs during the activation process
     */
    @Override
    public void epidemicStateBaseActivate(Set<SpaceShipTile> AlreadyVisited) throws Exception {
        view.epidemicStateBaseActivate(AlreadyVisited);
    }

    /**
     * Notifies the view about the tiles that are misplaced or incorrect.
     *
     * @param pos a list of integers representing the positions of the incorrect tiles
     * @param nick a string representing the nickname of the player
     * @param nickEff a string representing the nickname of the player responsible for the error
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void wrongTiles(ArrayList<Integer> pos, String nick, String nickEff) throws Exception {
        view.wrongTiles(pos, nick, nickEff);
    }

    /**
     * Inserts a tile into the specified position with a given rotation and associates it with the player's nickname.
     *
     * @param TileIndex the index of the tile to be inserted
     * @param r the row coordinate where the tile will be placed
     * @param c the column coordinate where the tile will be placed
     * @param rotation the rotation to be applied to the tile
     * @param playerNickname the nickname of the player performing the insertion
     * @throws Exception if an error occurs during the tile insertion process
     */
    @Override
    public void insertTileLMR(int TileIndex, int r, int c, int rotation, String playerNickname) throws Exception {
        view.insertTileLMR(TileIndex, r, c, rotation, playerNickname);
    }

    /**
     * Handles the loss of goods for the current player.
     * This method iterates through the list of ship boards provided by the view to find the
     * current player's board. Once the board matching the current player is identified, it
     * notifies the view that the player has lost goods by invoking the appropriate method.
     *
     * @throws Exception if an error occurs during the execution of the method
     */
    @Override
    public void lostGoods() throws Exception {
        for (ShipBoard s : view.getShipBoards()) {
            if (view.getMyNickname().equals(s.getMyPlayer().getUsername())) {
                view.lostGoods(s.getMyPlayer());
                break;
            }
        }
    }

    /**
     * Invokes the `youPayConsequences` method on the `view` object.
     *
     * This method is overridden to provide functionality for handling
     * consequences in a defined manner. The exact behavior is determined
     * by the implementation of the `view` object's `youPayConsequences` method.
     *
     * @throws Exception if an error occurs during the execution of the `view`'s
     *         `youPayConsequences` method.
     */
    @Override
    public void youPayConsequences() throws Exception {
        view.youPayConsequences();
    }

    /**
     * Allows the player to choose how to defend against meteors using various strategies.
     *
     * @param player the player who is deciding how to face meteors
     * @param howToDefenceFromShots a list representing the chosen defense strategies for each shot
     * @param shots a list of shots representing incoming meteors
     * @param dice the result from the dice roll affecting defense options
     * @param currentShot the index of the current shot being faced in the shots list
     * @throws Exception if an error occurs during the defense decision process
     */
    @Override
    public void meteorCardChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, ArrayList<Shot> shots, int dice, int currentShot) throws Exception {
        view.meteorCardChooseHowToFaceMeteors(player, howToDefenceFromShots, shots, dice, currentShot);
    }

    /**
     * Advances the game to the next player's turn.
     *
     * This method delegates the responsibility to the view layer by invoking
     * its `nextPlayerTurn` method. Any exception thrown during this operation
     * will be propagated to the caller.
     *
     * @throws Exception if an error occurs during the transition to the next player's turn
     */
    @Override
    public void nextPlayerTurn() throws Exception {
        view.nextPlayerTurn();
    }

    /**
     * Invokes the noSurrender method on the view object.
     * This method is an overridden implementation and can throw an exception
     * if an error occurs during its execution.
     *
     * @throws Exception if an error occurs while executing the noSurrender method on the view
     */
    @Override
    public void noSurrender() throws Exception {
        view.noSurrender();
    }

    /**
     * Moves the player with the specified nickname to the given position on the flight board.
     *
     * @param playerNickname the nickname of the player to be moved
     * @param pos the position on the flight board to move the player to
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void movePlayerInFlightBoard(String playerNickname, int pos) throws Exception {
        view.movePlayerInFlightBoard(playerNickname, pos);
    }

    /**
     * Indicates that it is not the current user's turn to perform any game-related action.
     *
     * This method delegates to the view layer to display the appropriate feedback or message
     * to the user, indicating that they must wait for their turn.
     *
     * @throws Exception if an error occurs during the execution of the view method
     */
    @Override
    public void notYourTurn() throws Exception {
        view.notYourTurn();
    }


    /**
     * Invokes the error handling mechanism of the associated view. This method
     * is intended to notify the view layer about an error occurrence, enabling
     * it to respond appropriately.
     *
     * @throws Exception if an unexpected issue occurs during the error handling process.
     */
    @Override
    public void error() throws Exception {
        view.onError();
    }

    /**
     * Sends a ping to verify or test the availability and responsiveness of a service or endpoint.
     * This method is typically used to ensure connectivity or to maintain a connection alive.
     * Override this method to define custom ping behavior as needed.
     */
    @Override
    public void ping() {

    }

    /**
     * Updates the identifier of the object and associates it with a specific game.
     *
     * @param id the new UUID to set as the identifier
     */
    @Override
    public void changeID(UUID id) {
        uuid = id;
        games.put(0, id);

    }

    /**
     * Updates the game state after it is created by delegating the parameters to the view.
     *
     * @param level the current level of the game
     * @param ShowableDecks the list of decks that can be shown in the current game
     * @param players the list of players participating in the game
     * @param hourglass the number of hourglasses available in the game
     * @param surrender the number of surrenders available in the game
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateGameCreated(int level, List<Deck> ShowableDecks, ArrayList<Player> players, int hourglass, int surrender) throws Exception {
        view.updateGameCreated(level, ShowableDecks, players, hourglass, surrender);
    }

    /**
     * Selects and processes a tile based on the given index and the player's nickname.
     *
     * @param TileIndex the index of the tile to be picked
     * @param playerNickname the nickname of the player picking the tile
     * @throws Exception if there is an error during the tile selection process
     */
    @Override
    public void pickTileLMR(int TileIndex, String playerNickname) throws Exception {
        view.pickTileLMR(TileIndex, playerNickname);
    }

    /**
     * Allows the player to pick a little deck based on the provided index.
     *
     * @param deckIndex        the index of the little deck to be picked
     * @param playerNickname   the nickname of the player making the selection
     * @throws Exception       if an error occurs during the operation
     */
    @Override
    public void pickLittleDeckLMR(int deckIndex, String playerNickname) throws Exception {
        view.pickLittleDeckLMR(deckIndex, playerNickname);
    }

    /**
     * Sets the player's position on the flight board.
     *
     * @param playerNickname the nickname of the player whose position is to be updated
     * @param pos the new position of the player on the flight board
     * @throws Exception if an error occurs while updating the player's position
     */
    @Override
    public void setPlayerPosInFlightBoard(String playerNickname, int pos) throws Exception {
        view.setPlayerPosInFlightBoard(playerNickname, pos);
    }


    /**
     * Updates the usage details of the specified card.
     *
     * @param card The card object whose usage details need to be updated.
     * @throws Exception If any error occurs during the update process.
     */
    @Override
    public void updateCardUse(Card card) throws Exception {
        view.updateCardUse(card);
    }

    /**
     * Updates the card state with the given c_State object.
     *
     * @param card the c_State object containing the new state information for the card
     * @throws Exception in case of any issues updating the card state
     */
    @Override
    public void updateCardUseSTATE(c_State card) throws Exception {
        view.updateCardUseSTATE(card);
    }

    /**
     * Updates the current status by invoking the view's updateStatus method.
     *
     * @param stato the new state to be updated, represented as an enum value
     *              from stateEnum
     * @throws Exception if an error occurs while updating the status
     */
    @Override
    public void updateStatus(stateEnum stato) throws Exception {
        view.updateStatus(stato);
    }

    /**
     * Updates the final scores by passing the provided map of scores to the view.
     *
     * @param finalScores a HashMap where the keys represent identifiers (e.g., player names, IDs, etc.),
     *                    and the values are the corresponding final scores as Float.
     * @throws Exception if an error occurs while updating the view with the final scores.
     */
    @Override
    public void updateFinalScores(HashMap<String, Float> finalScores) throws Exception {
        view.updateFinalScores(finalScores);
    }

    /**
     * Updates the battery positions for assertions linked to a specific player.
     *
     * @param p The player for whom the battery positions are being updated.
     * @param posBatAndNumBattXPos A list of lists representing the positions of batteries and associated numeric values.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateAssertBatteriesPos(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception {
        view.updateAssertBatteriesPos(p, posBatAndNumBattXPos);
    }

    /**
     * Completes the building phase of the application or module.
     * This method delegates the operation to the associated view
     * by invoking its completeBuildingPhase method.
     * It ensures the building stage is appropriately marked
     * as finished within the system.
     *
     * @throws Exception if an error occurs during the completion of the building phase
     */
    @Override
    public void completeBuildingPhase() throws Exception {
        view.completeBuildingPhase();
    }

    /**
     * Updates and adds goods for the specified player based on the provided positions and goods sets.
     *
     * @param player    the player for whom goods are being updated and added
     * @param posGoods  a list of positions where goods are to be added
     * @param goodsSets a list of goods sets to be added at the specified positions
     * @throws Exception if there is an error during the update or addition process
     */
    @Override
    public void updateAddGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        view.updateAddGoods(player, posGoods, goodsSets);
    }

    /**
     * Updates the view to handle the process of choosing passengers to lose.
     *
     * @param player The player making the decision regarding passengers to lose.
     * @param c The consequences associated with losing passengers.
     * @param pass A list of passenger groups, where each group is represented by an ArrayList of integers.
     * @throws Exception If an error occurs while updating the process.
     */
    @Override
    public void updateChoosePassengersToLose(Player player, Consequences c, ArrayList<ArrayList<Integer>> pass) throws Exception {
        view.updateChoosePassengersToLose(player, c, pass);
    }

    /**
     * Activates the financial status of a planet for the specified players and handles it through the provided flight board.
     *
     * @param playersss A list of Player objects representing the players whose planet financial status is to be activated.
     * @param flightBoard The FlightBoard object that manages the activation process.
     * @throws Exception If an error occurs during the activation process.
     */
    @Override
    public void planetFinStatActivate(ArrayList<Player> playersss, FlightBoard flightBoard) throws Exception {
        view.planetFinStatActivate(playersss, flightBoard);
    }

    /**
     * Updates the consequence for lost days in the game by delegating the update
     * operation to the view layer.
     *
     * @param player The player object for which the lost days consequence is being updated.
     * @param flightBoard The flight board associated with the player in the game.
     * @param numDays The number of days to adjust as part of the consequence.
     * @param t A Boolean flag indicating additional context for updating lost days.
     * @throws Exception If an error occurs during the update operation.
     */
    @Override
    public void updateConsequenceLostDays(Player player, FlightBoard flightBoard, int numDays, Boolean t) throws Exception {
        view.updateConsequenceLostDays(player, flightBoard, numDays, t);
    }

    /**
     * Updates the state of all goods lost for a player based on the specified parameters.
     *
     * @param player       The player whose goods status is being updated.
     * @param finished     A Boolean flag indicating if the update process is finished.
     * @param batttoloose  A Boolean flag indicating if battle-related goods are to be considered lost.
     * @param allbatlost   A Boolean flag indicating if all battle-related goods are lost.
     * @throws Exception   If an error occurs during the update process.
     */
    @Override
    public void updateLooseAllGoods(Player player, Boolean finished, Boolean batttoloose, Boolean allbatlost) throws Exception {
        view.updateLooseAllGoods(player, finished, batttoloose, allbatlost);
    }


    /**
     * Updates the smugglers calculation based on the player's current state, cannon positions,
     * and battery positions. This method delegates the update logic to the view.
     *
     * @param player the player whose smugglers calculation needs to be updated
     * @param cannonPos a list of lists containing the positions of the cannons
     * @param batteriesPos a list of lists containing the positions of the batteries
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateSmugglersCalc(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        view.updateSmugglersCalc(player, cannonPos, batteriesPos);
    }

    /**
     * Updates the lost batteries by smug operation for a player.
     *
     * @param p The player for whom the operation is being performed.
     * @param posBatAndNumBattXPos A nested list containing positions of batteries and their count for each position.
     * @param numbatt The number of batteries affected by the operation.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateLostBatteriesSmug(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, int numbatt) throws Exception {
        view.updateLostBatteriesSmug(p, posBatAndNumBattXPos, numbatt);
    }

    /**
     * Updates the player's lost goods data associated with smuggling activities.
     *
     * @param p the player whose lost goods are being updated
     * @param posGoods a list of positions representing the lost goods
     * @param goodsSets a list of sets of goods categorized for smuggling operations
     * @param goodsListDiPrima a list of specific goods relevant to the operation
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateLostGoodsSmug(Player p, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, ArrayList<Goods> goodsListDiPrima) throws Exception {
        view.updateLostGoodsSmug(p, posGoods, goodsSets, goodsListDiPrima);
    }

    /**
     * Allows slavers to choose positions for cannon and batteries.
     *
     * @param player The player requiring the cannon and battery position selection.
     * @param cannonPos A list of lists where each sub-list contains integers
     *                  representing possible cannon positions.
     * @param batteriesPos A list of lists where each sub-list contains integers
     *                     representing possible battery positions.
     * @throws Exception If there is an issue during the process of choosing positions.
     */
    @Override
    public void slaversChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        view.slaversChooseCannonBatteryPos(player, cannonPos, batteriesPos);
    }

    /**
     * Executes the process where slavers choose passengers to lose based on the given parameters.
     *
     * @param player The player object representing the current player interacting with the slavers.
     * @param yOn A boolean flag specifying whether a specific condition is active or not.
     * @param tiles A nested ArrayList containing integers that represent the tile data utilized in the selection process.
     * @throws Exception If an error occurs during the execution of the process.
     */
    @Override
    public void slaversChoosePassengersToLose(Player player, boolean yOn, ArrayList<ArrayList<Integer>> tiles) throws Exception {
        view.slaversChoosePassengersToLose(player, yOn, tiles);
    }

    /**
     * Facilitates the pirate decision-making process to choose positions for cannons and batteries.
     *
     * @param player the player object representing the pirate making the decision
     * @param cannonPos a list of positions available for placing the cannons
     * @param batteriesPos a list of positions available for placing the batteries
     * @throws Exception if an error occurs during the decision-making process
     */
    @Override
    public void piratesChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        view.piratesChooseCannonBatteryPos(player, cannonPos, batteriesPos);
    }

    /**
     * Determines how pirates choose to face meteors, using given game parameters.
     *
     * @param player The current player making the decision.
     * @param howToDefenceFromShots A list of integers representing possible defense strategies.
     * @param shot The shot instance the pirates need to handle.
     * @param dice The dice roll value influencing the decision-making process.
     * @throws Exception If an error occurs during the operation.
     */
    @Override
    public void piratesChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, Shot shot, int dice) throws Exception {
        view.piratesChooseHowToFaceMeteors(player, howToDefenceFromShots, shot, dice);
    }

    /**
     * Executes the logic for pirates deciding whether to claim a reward.
     *
     * @param yOn a boolean indicating the choice regarding the reward
     * @param player the Player object representing the pirate making the decision
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void piratesChooseToClaimReward(boolean yOn, Player player) throws Exception {
        view.piratesChooseToClaimReward(yOn, player);
    }

    /**
     * Facilitates the process of choosing and starting the motor in an open space environment.
     *
     * @param player the player initiating the action
     * @param flightBoard the flight board representing the current state of the game or environment
     * @param enginesPos a list of positions of the engines available in the open space
     * @param batteriesPos a list of positions of the batteries available in the open space
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void openSpaceChooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        view.openSpaceChooseToStartMotor(player, flightBoard, enginesPos, batteriesPos);
    }

    /**
     * Triggers a stardust effect in the associated view. This effect is typically
     * visual in nature and may be used to enhance user experience by providing
     * visual feedback or decoration.
     *
     * @throws Exception if an error occurs while executing the stardust effect.
     */
    @Override
    public void stardustEffect() throws Exception {
        view.stardustEffect();
    }

    /**
     * Updates the remaining goods for the specified player.
     *
     * @param p the player whose goods remaining are to be updated
     * @param goodFInali the list of goods to update for the player
     * @throws Exception if an error occurs while updating goods
     */
    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception {
        view.updateGoodsRemaining(p, goodFInali);
    }

    /**
     * This method is invoked when a timer has not started. It triggers the
     * associated behavior in the view by calling the `timerNotStarted` method
     * on the view instance.
     *
     * @throws Exception if an error occurs during the execution of the method
     */
    @Override
    public void timerNotStarted() throws Exception {
        view.timerNotStarted();
    }

    /**
     * Handles the scenario where a tile is not flipped in the game logic.
     * This method invokes the corresponding tileNotFlipped action in the view layer.
     *
     * @throws Exception if an error occurs during the processing of the tileNotFlipped action.
     */
    @Override
    public void tileNotFlipped() throws Exception {
        view.tileNotFlipped();
    }

    /**
     * Handles the scenario where an incorrect input is provided.
     * This method is responsible for delegating the behavior
     * to the view layer to manage and display the appropriate response
     * for wrong user input.
     *
     * @throws Exception if an error occurs during the execution of the view logic
     */
    @Override
    public void wrongInput() throws Exception {
        view.wrongInput();
    }

    /**
     * Indicates that an incorrect player has been detected during game play.
     * This method triggers the associated view to handle the "wrong player" scenario.
     *
     * @throws Exception if an error occurs while processing the wrong player event.
     */
    @Override
    public void wrongPlayer() throws Exception {
        view.wrongPlayer();
    }

    /**
     * Notifies the view that a player has won the game.
     *
     * @param nick the nickname of the player who has won the game
     * @throws Exception if an error occurs during processing
     */
    @Override
    public void someoneWon(String nick) throws Exception {
        view.someoneWon(nick);
    }

    /**
     * Invokes the tie operation with the provided nickname.
     *
     * @param nick the nickname to be used for the tie operation
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void tie(String nick) throws Exception {
        view.tie(nick);
    }

    /**
     * Handles the scenario where a reward is refused by the specified user.
     *
     * @param nick the nickname of the user who refused the reward
     * @throws Exception if an issue occurs while processing the refusal
     */
    @Override
    public void refusedReward(String nick) throws Exception {
        view.refusedReward(nick);
    }


    /**
     * Updates the dice value in the view.
     *
     * @param dice The new value of the dice to be updated.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateDice(int dice) throws Exception {
        view.updateDice(dice);
    }

    /**
     * Invokes the view's cannotDeposit method to handle scenarios
     * where a deposit operation cannot be performed.
     *
     * @throws Exception if an error occurs during the execution of this method
     */
    @Override
    public void cannotDeposit() throws Exception {
        view.cannotDeposit();
    }

    /**
     * Handles the scenario where an insertion operation cannot be performed.
     * This method delegates the action to the corresponding view layer to manage
     * the behavior or display appropriate feedback when the insertion fails.
     *
     * @throws Exception if an error occurs during the execution of the operation
     */
    @Override
    public void cannotInsert() throws Exception {
        view.cannotInsert();
    }

    /**
     * Notifies the associated view layer that a selection or pick action
     * cannot be performed. This could be used to disable certain UI
     * interactions or provide feedback to the user regarding invalid
     * operations or selections.
     *
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void cannotPick() throws Exception {
        view.cannotPick();
    }

    /**
     * This method is an overridden implementation that triggers the `cannotFill`
     * behavior on the associated view component. It handles scenarios where data
     * or content cannot be filled as expected.
     *
     * @throws Exception if an error occurs during the execution of the operation.
     */
    @Override
    public void cannotFill() throws Exception {
        view.cannotFill();
    }

    /**
     * Invokes the blocked functionality on the associated view.
     * Typically called when certain operations or actions are restricted
     * or unavailable to the user. This method delegates the blocked
     * state handling to the view component.
     *
     * @throws Exception if an error occurs while invoking the blocked
     *         method on the view
     */
    @Override
    public void blocked() throws Exception {
        view.blocked();
    }

    /**
     * Stops the building process by invoking the stopBuilding method on the associated view.
     *
     * @throws Exception if an error occurs during the stopBuilding process
     */
    @Override
    public void stopBuilding() throws Exception {
        view.stopBuilding();
    }

    /**
     * Indicates that a timer has started and performs related actions.
     *
     * This method is called to notify the associated view that the timer
     * has been started. It delegates the call to the view's `timerStarted`
     * method to handle any necessary updates or procedures related
     * to the timer's start event.
     *
     * @throws Exception if an error occurs during the execution.
     */
    @Override
    public void timerStarted() throws Exception {
        view.timerStarted();
    }

    /**
     * Notifies that the timer has already been started.
     * This method invokes the corresponding timer handling functionality in the view.
     *
     * @throws Exception if an error occurs during the handling of the timer notification
     */
    @Override
    public void timerAlreadyStarted() throws Exception {
        view.timerAlreadyStarted();
    }

    /**
     * Removes a block of spaceship tiles associated with a specific player.
     *
     * @param block          the list of SpaceShipTile objects representing the block to be removed
     * @param playerNickname the nickname of the player associated with the block
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname) throws Exception {
        view.removeBlock(block, playerNickname);
    }

    /**
     * Method called when the timer has ended.
     *
     * @param b a Boolean indicating the timer state. True if the timer has ended, false otherwise.
     * @throws Exception if an error occurs while handling the timer end process.
     */
    @Override
    public void timerEnded(Boolean b) throws Exception {
        view.timerEnded(b);
    }

    /**
     * Creates a new game with the specified parameters.
     *
     * @param level The game level to be set for the new game.
     * @param id The unique identifier for the game session.
     * @param numplayrs The number of players participating in the game.
     * @param Nickname The nickname of the player creating the game.
     * @throws Exception If an error occurs during the game creation process.
     */
    @Override
    public void createGame(int level, UUID id, int numplayrs, String Nickname) throws Exception {
        try {
            output.commandGiver(CommandType.CREATE_GAME, level, id, numplayrs, Nickname);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sends a command to join the game with the provided parameters.
     *
     * @param NickName     The nickname of the player attempting to join the game.
     * @param NumOfPlayers The number of players expected in the game.
     * @param id           The unique identifier of the game session.
     * @param oldid        The unique identifier of the previous game session.
     * @throws IOException If an I/O error occurs while sending the command.
     */
    @Override
    public void JoinGame(String NickName, int NumOfPlayers, UUID id, UUID oldid) throws IOException {
        try {
            output.commandGiver(CommandType.JOIN_GAME, NickName, NumOfPlayers, id, oldid);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Activates a timer for a specified player.
     * Sends a command to activate the timer identified by the player's name and an associated UUID.
     * Handles potential socket and I/O exceptions during communication.
     *
     * @param player The name of the player for whom the timer is being activated.
     * @param id The unique identifier (UUID) associated with the timer to be activated.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void activateTimer(String player, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.ACTIVATE_TIMER, player, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sets the numeric tile for a specific player and position, identified by a UUID.
     *
     * @param playerid the ID of the player for whom the tile is being set
     * @param posTile the position of the tile to be set
     * @param id the unique identifier of the tile to be associated
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void setNumTile(int playerid, int posTile, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.SET_NUM_TILE, playerid, posTile, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sends a surrender command for the specified player and game session.
     *
     * @param playerID The identifier of the player who is surrendering.
     * @param id The unique identifier of the game session.
     * @throws RemoteException If a communication-related exception occurs during the remote method call.
     */
    @Override
    public void Surrend(int playerID, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.SURREND, playerID, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Visualizes the final scores of a specific entity identified by the given UUID.
     * This method sends a command to initiate the visualization process and handles potential
     * exceptions related to network issues or input/output errors.
     *
     * @param id the unique identifier of the entity for which the final scores should be visualized
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void visualizeFinalScores(UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.VISUALIZE_FINAL_SCORES, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Handles the action when a player selects a tile that has already been flipped.
     *
     * @param index   The index of the tile that is already flipped.
     * @param playerID The ID of the player attempting to pick the tile.
     * @param id      The unique identifier for the game session.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void pickTileAlreadyFlipped(int index, int playerID, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.PICK_TILE_ALREADY_FLIPPED, index, playerID, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sends a command to pick a tile with an unknown value for the given player.
     *
     * @param playerID the identifier of the player performing the action
     * @param id the unique identifier of the tile being picked
     * @throws RemoteException if a communication-related exception occurs during remote method execution
     */
    @Override
    public void pickTileUnknown(int playerID, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.PICK_TILE_UNKNOWN, playerID, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sends a command to pick a little deck during the game.
     *
     * @param index the index of the little deck to be picked
     * @param playerID the unique identifier of the player making the choice
     * @param id the UUID associated with the current session or game instance
     * @throws RemoteException if a communication-related error occurs
     */
    @Override
    public void pickLittleDeck(int index, int playerID, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.PICK_LITTLE_DECK, index, playerID, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Executes the depositLittleDeck command by sending the appropriate command type and
     * related player and deck information to the server.
     *
     * @param playerID the ID of the player performing the action
     * @param id the unique identifier of the deck being deposited
     * @throws RemoteException if a remote communication issue occurs
     */
    @Override
    public void depositLittleDeck(int playerID, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.DEPOSIT_LITTLE_DECK, playerID, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Handles the deposit of a tile for a specific player by sending the appropriate command.
     *
     * @param playerID The ID of the player performing the deposit action.
     * @param id The unique identifier of the tile to be deposited.
     * @throws RemoteException If a remote communication error occurs during the operation.
     */
    @Override
    public void depositTile(int playerID, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.DEPOSIT_TILE, playerID, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Ends the building process for a specific player and redirects to a given position.
     *
     * @param PlayerID The unique identifier of the player who is ending the building process.
     * @param positionwheretogo The position the player should move to after ending the building.
     * @param id The unique identifier associated with the process.
     * @throws RemoteException If a remote communication error occurs during the operation.
     */
    @Override
    public void endbuilding(int PlayerID, int positionwheretogo, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.END_BUILDING, PlayerID, positionwheretogo, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Inserts a tile into the game board at the specified location and orientation.
     *
     * @param row       The row index where the tile is to be inserted.
     * @param col       The column index where the tile is to be inserted.
     * @param playerID  The unique identifier of the player making the move.
     * @param rotation  The rotation of the tile to be inserted, typically in degrees or steps.
     * @param id        The unique identifier for the tile being inserted.
     * @throws RemoteException If a communication-related exception occurs during the remote method call.
     */
    @Override
    public void insertTile(int row, int col, int playerID, int rotation, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.INSERT_TILE, row, col, playerID, rotation, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Activates an effect by sending a command with the corresponding unique identifier.
     * Handles socket and I/O exceptions during the command transmission process.
     *
     * @param id the unique identifier associated with the effect to be activated
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void EffectActivation(UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.EFFECT_ACTIVATION, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sends a command to retrieve information about the card currently in use
     * identified by the given UUID.
     *
     * @param id the UUID of the card to be queried
     * @throws RemoteException if a communication-related exception occurs
     */
    @Override
    public void getCardinuse(UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.GET_CARD_IN_USE, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Executes the action to choose one sub-ship based on the provided parameters.
     * This method communicates with the server to send a command indicating the
     * selected sub-ship for a specific player.
     *
     * @param index   the index of the sub-ship to be chosen
     * @param playerID the ID of the player making the selection
     * @param id      the unique identifier related to the current game or context
     * @throws RemoteException if a communication-related error occurs during command transmission
     */
    @Override
    public void chooseOneSubShip(int index, int playerID, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_ONE_SUB_SHIP, index, playerID, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Accepts the spacecraft or object to land on a specified planet.
     *
     * @param p The name or identifier of the entity requesting permission to land.
     * @param yOn A boolean that indicates whether the request is active (true) or not (false).
     * @param NumPlanet The identifier or index of the planet where landing is requested.
     * @param id A unique identifier (UUID) for the landing request or spacecraft.
     * @throws RemoteException If there is an error in the remote method call.
     */
    @Override
    public void acceptToLandOnAPlanet(String p, boolean yOn, int NumPlanet, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.ACCEPT_TO_LAND_ON_A_PLANET, p, yOn, NumPlanet, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Allows the specified player to choose an abandoned station
     * by interacting with provided storage tiles and goods in the game.
     *
     * @param player the name or identifier of the player making the choice
     * @param yOn a boolean flag indicating a particular state or condition
     * @param storageTiles a 2D list representing the storage tiles available
     * @param newGoods a 2D list containing the goods to be used or set during the operation
     * @param id the unique identifier for the current game session or operation
     * @throws RemoteException if a network-related exception occurs during remote communication
     */
    @Override
    public void chooseAbandonedStation(String player, boolean yOn, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_ABANDONED_STATION, player, yOn, storageTiles, newGoods, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Allows a player to choose the position of cannon batteries during the game.
     * This method sends the relevant command with associated parameters to the output handler.
     *
     * @param player       The identifier for the player selecting the cannon battery positions.
     * @param cannonPos    A list of coordinates representing the positions of the cannons.
     * @param batteriesPos A list of coordinates representing the positions of the batteries.
     * @param id           A unique identifier for the current operation or session.
     * @throws RemoteException If there is an error with the remote operation.
     */
    @Override
    public void chooseCannonBatteryPos(String player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_CANNON_BATTERY_POS, player, cannonPos, batteriesPos, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Allows the player to choose how to defend against incoming meteors by specifying defense actions.
     *
     * @param player the name of the player making the defense choice
     * @param howToDefenceFromShots a list of integers representing the defense strategy against meteors
     * @param id the unique identifier associated with the player or session
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void chooseHowToFaceMeteors(String player, ArrayList<Integer> howToDefenceFromShots, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_HOW_TO_FACE_METEORS, player, howToDefenceFromShots, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Handles the logic to choose passengers to lose during a specific command operation.
     * Sends the command along with its parameters to a remote server for processing.
     * Handles potential communication issues via appropriate exceptions.
     *
     * @param p         The identifier or related data of the player or subject making the choice.
     * @param yOn       A boolean flag indicating a specific condition or state related to the operation.
     * @param pass      A list of lists representing the passenger configurations or groupings.
     * @param id        The unique identifier corresponding to the session or entity associated with the operation.
     * @throws RemoteException If a communication-related exception occurs during the remote operation.
     */
    @Override
    public void choosePassengersToLose(String p, boolean yOn, ArrayList<ArrayList<Integer>> pass, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_PASSENGERS_TO_LOSE, p, yOn, pass, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Executes the process for choosing to claim a reward in the game.
     * Sends the appropriate command to the server when invoked.
     * Handles exceptions related to connection issues.
     *
     * @param yOn boolean value indicating whether the reward should be claimed or not
     * @param player the name of the player choosing to claim the reward
     * @param id the unique identifier (UUID) of the player
     * @throws RemoteException if an error occurs during a remote method call
     */
    @Override
    public void chooseToClaimReward(boolean yOn, String player, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_TO_CLAIM_REWARD, yOn, player, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sends a command to claim a reward with goods to the server.
     *
     * @param yOn             A boolean indicating whether the player chooses to claim the reward.
     * @param player          The name of the player attempting to claim the reward.
     * @param storageTiles    A list of storage tiles represented as a nested list of integers.
     * @param newGoods        A list of new goods represented as a nested list of Goods objects.
     * @param id              A unique identifier (UUID) for the request or player.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void chooseToClaimReward(boolean yOn, String player, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_TO_CLAIM_REWARD_WITH_GOODS, yOn, player, storageTiles, newGoods, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Allows the user to choose positions to place batteries during gameplay.
     *
     * @param p                     the player's identifier or name
     * @param posBatAndNumBattXPos  a list of positions and the associated counts of batteries to be placed
     * @param id                    the unique identifier of the player
     * @throws RemoteException      if a remote communication error occurs
     */
    @Override
    public void chooseToPlaceBatteries(String p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_TO_PLACE_BATTERIES, p, posBatAndNumBattXPos, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Executes the CHOOSE_TO_START_FIRE_POWER command for a given player, managing
     * the specified firepower triplets and batteries to activate.
     *
     * @param p The identifier or name of the player choosing to start firepower.
     * @param DoubFireTriplets A list of triplet combinations indicating potential
     *                         firepower configurations to choose from.
     * @param BatteriesToAct A list of batteries to be activated during this operation.
     * @param id The unique identifier of the current operation or session.
     * @throws RemoteException If a remote invocation error occurs.
     */
    @Override
    public void chooseToStartFirePower(String p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_TO_START_FIRE_POWER, p, DoubFireTriplets, BatteriesToAct, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sends a command to start the motor with the provided configurations.
     *
     * @param player the name of the player initiating the motor start command.
     * @param enginesPos a nested list representing the positions of the engines involved.
     * @param batteriesPos a nested list representing the positions of the batteries involved.
     * @param id the unique identifier associated with the player's session or transaction.
     * @throws RemoteException if a communication-related error occurs.
     */
    @Override
    public void chooseToStartMotor(String player, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_TO_START_MOTOR, player, enginesPos, batteriesPos, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Handles the process of deciding where to place goods in the game by sending
     * the necessary command to the output mechanism.
     *
     * @param player the name of the player making the decision
     * @param posGoods a list of possible positions where goods can be placed
     * @param goodsSets a list of sets containing the goods available for placement
     * @param id the unique identifier for the operation or player session
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void chooseWhereToPutGoods(String player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.CHOOSE_WHERE_TO_PUT_GOODS, player, posGoods, goodsSets, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Adds a tile to the waiting list for a specific player identified by their ID.
     * This method communicates the ADD_WAIT_TILE command to the server.
     *
     * @param playerID the unique identifier of the player for whom the wait tile is being added
     * @param id the unique identifier of the tile to be added
     * @throws RemoteException if a remote communication issue occurs
     */
    @Override
    public void addWaitTile(int playerID, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.ADD_WAIT_TILE, playerID, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Inserts the wait tile at the specified position on the board for a given player.
     *
     * @param playerID the ID of the player making the insertion
     * @param index the index of the tile to be inserted
     * @param row the row where the tile is to be inserted
     * @param col the column where the tile is to be inserted
     * @param rotation the rotation angle of the tile (in degrees)
     * @param id the unique identifier of the tile
     * @throws RemoteException if a communication-related exception occurs during the remote method call
     */
    @Override
    public void insertWaitTile(int playerID, int index, int row, int col, int rotation, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.INSERT_WAIT_TILE, playerID, index, row, col, rotation, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Executes the CommandFillTile operation to fill a tile on the game board.
     * This method sends a command with the provided parameters to fill a tile,
     * which may include placing an alien of a specified color on the board.
     * Handles potential communication issues, such as socket errors or IO exceptions.
     *
     * @param playerid   The unique identifier of the player issuing the command.
     * @param wantalien  A boolean indicating if the player wants to place an alien.
     * @param color      The color of the alien to be placed if wantalien is true.
     * @param row        The row index of the tile to be filled.
     * @param col        The column index of the tile to be filled.
     * @param id         The unique identifier for the operation or command.
     * @throws RemoteException If a communication-related error occurs during execution.
     */
    @Override
    public void CommandFillTile(int playerid, boolean wantalien, AlienColor color, int row, int col, UUID id) throws RemoteException {
        try {
            output.commandGiver(CommandType.COMMAND_FILL_TILE, playerid, wantalien, color, row, col, id);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Sends a command to retrieve active games associated with the provided UUID.
     * Handles potential communication errors, such as connection loss or I/O exceptions.
     *
     * @param uuid the unique identifier of the user for which to retrieve active games
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void activeGames(UUID uuid) throws RemoteException {
        try {
            output.commandGiver(CommandType.ACTIVE_GAMES, uuid);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

    /**
     * Executes a demo game command for a specific session identified by the given UUID.
     * Handles any connection losses or command-sending errors.
     *
     * @param uuid the unique identifier of the session to execute the demo game
     * @throws IOException if an I/O error occurs during communication
     */
    @Override
    public void demoGame(UUID uuid) throws IOException {
        try {
            output.commandGiver(CommandType.DEMO, uuid);
        } catch (SocketException e) {
            System.out.println("Connessione persa...");
            shutdown("Server shutdown");
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
        }
    }

}