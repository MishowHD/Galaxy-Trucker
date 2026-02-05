package it.polimi.ingsw.Network.Client.RMI;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Utils.stateEnum;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.Network.Server.RMI.VirtualViewRMI;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.*;


public class RMIClient extends GenericClient implements VirtualViewRMI {
    /**
     * Represents a final reference to a VirtualServerRMI instance.
     * This variable typically holds a remote interface for interacting
     * with a virtual server through RMI (Remote Method Invocation).
     * It is initialized once and cannot be reassigned thereafter.
     */
    final VirtualServerRMI serv;
    /**
     * A thread-safe blocking queue designed to hold and manage runnable tasks.
     * The `commandQueue` is used to store tasks that are awaiting execution.
     * It ensures thread-safe operations, allowing multiple threads to add or remove tasks concurrently.
     * The queue blocks when attempting to add an item if it is full or retrieve an item if it is empty,
     * depending on the capacity and current state of the queue implementation.
     */
    private final BlockingQueue<Runnable> commandQueue = new LinkedBlockingQueue<>();
    /**
     * A protected variable that represents a smaller or lightweight model representation.
     * It is often used as a simplified abstraction of a larger or more complex model within an application.
     * The exact purpose and structure of this representation may vary depending on the context of its usage.
     */
    protected LittleModelRepresentation littleModelRep;
    /**
     * Represents a general view component in the application.
     * This variable may refer to a UI component or a central
     * element responsible for handling or displaying general information.
     * It is protected, indicating it is accessible to this class
     * and subclasses.
     */
    protected GeneralView generalView;
    /**
     * A scheduled executor service designed to handle periodic tasks efficiently.
     * The `heartbeat` variable is initialized as a single-threaded scheduled executor
     * service to ensure sequential execution of tasks in a dedicated thread.
     *
     * This is typically used for scheduling tasks such as health checks,
     * regular updates, or other time-based operations requiring consistent intervals.
     *
     * Being a `final` variable ensures that the `heartbeat` executor cannot be reassigned
     * after initialization. Threads within this executor are managed internally,
     * providing a streamlined approach for scheduling tasks without manual thread management.
     */
    private final ScheduledExecutorService heartbeat = Executors.newSingleThreadScheduledExecutor();
    /**
     * Represents the timestamp of the last successful ping to the server.
     * This value is stored as a long and is initialized to the current system time
     * in milliseconds at the moment of object instantiation.
     *
     * The volatile keyword ensures that updates to this variable are visible to
     * all threads, supporting concurrent access scenarios.
     */
    private volatile long lastServerPing = System.currentTimeMillis();
    private final ExecutorService pingExec = Executors.newSingleThreadExecutor();

    /**
     * Initializes an RMIClient instance and starts a separate thread to
     * process commands from the command queue.
     *
     * @param lm the little model representation to associate with the client
     * @param serv the virtual server RMI instance to communicate with
     * @throws RemoteException if a remote communication problem occurs
     */
    public RMIClient(LittleModelRepresentation lm, VirtualServerRMI serv) throws RemoteException {
        super();
        littleModelRep = lm;
        setModelToInputManager(lm);
        generalView = lm.getGeneralView();
        this.serv = serv;
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    commandQueue.take().run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    /**
     * Starts a heartbeat mechanism to monitor the server's health and connection status.
     * The heartbeat periodically sends a ping to the server and checks whether the server
     * is still reachable within a specified timeout duration.
     *
     * This method uses a scheduled task to execute the heartbeat logic at fixed intervals.
     * If the server fails to respond to the ping or surpasses the timeout threshold,
     * appropriate shutdown notifications are triggered.
     *
     * Timeout for server response is set to 15 seconds. The heartbeat task is executed
     * every 5 seconds.
     *
     * Shutdown notifications are handled via the notifyShutdown method, which is invoked
     * with an appropriate message when a disconnection or timeout is detected.
     *
     * The server is pinged using the serv.ping() method. Exceptions related to remote
     * communication (RemoteException) are caught and handled within this method.
     */
    private void startHeartbeat() {
        final long TIMEOUT = 15_000;    // quanto tolleri in assenza di ping
        heartbeat.scheduleWithFixedDelay(() -> {
            long now = System.currentTimeMillis();
            if (now - lastServerPing > TIMEOUT) {
                try { notifyShutdown("Server unreachable (timeout)"); } catch (RemoteException ignored) {}
                return;
            }
            Future<?> f = pingExec.submit(() -> {
                try {
                    serv.ping(uuid,getName());
                } catch (RemoteException _) {
                }
            });
            try {
                f.get(TIMEOUT, TimeUnit.MILLISECONDS);     // aspetta max 5 s
            } catch (TimeoutException te) {     // ping bloccato
                f.cancel(true);                 // interrompi il thread
                try { notifyShutdown("Server ping timed-out"); } catch (RemoteException ignored) {}
            } catch (Exception e) {             // RemoteException, ecc.
                try { notifyShutdown("Server Disconnected"); } catch (RemoteException ignored) {}
            }
        }, 0, 5, TimeUnit.SECONDS);
    }


    /**
     * Handles error scenarios by invoking the error-handling mechanism
     * implemented in the associated view.
     *
     * @throws Exception if an error occurs during the execution of the error-handling logic
     */
    @Override
    public void error() throws Exception {
        view.onError();
    }

    /**
     * Updates the last server ping timestamp to the current system time.
     * This method is typically used to indicate that a server is responsive.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void ping() throws RemoteException {
        lastServerPing = System.currentTimeMillis();
    }

    /**
     * Notifies the system of a shutdown event. This method ensures that
     * the remote object is properly unexported and performs necessary
     * shutdown operations including stopping the heartbeat and updating
     * the model state.
     *
     * @param message the shutdown message or reason describing the
     *                context or details of the shutdown event
     * @throws RemoteException if a remote communication error occurs
     *                         during the shutdown process
     */
    @Override
    public void notifyShutdown(String message) throws RemoteException {
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            System.err.println("Error unexporting RMI object: " + e.getMessage());
        }
        heartbeat.shutdownNow();
        pingExec.shutdownNow();
        littleModelRep.shutdown(message);
    }

    /**
     * Displays a list of old games and updates the internal game mappings.
     *
     * @param oldGames the list of {@code OldGame} objects to be displayed and processed
     * @throws Exception if an error occurs while processing the old games
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
     * Retrieves the name associated with the current object.
     *
     * @return the nickname of the object as a String
     */
    public String getName() {
        return this.nickName;
    }

    /**
     * Changes the ID to the specified value and updates the relevant mapping entry.
     *
     * @param id the new UUID to be used
     */
    @Override
    public void changeID(UUID id) {
        uuid = id;
        games.put(0, id);
    }

    /**
     * Sets the name for the object. This method assigns the given name
     * to the object's nickname property.
     *
     * @param name the name to be set as the nickname
     * @throws RemoteException if there is a communication-related exception
     *                         during the remote method call
     */
    @Override
    public void setName(String name) throws RemoteException {
        this.nickName = name;
    }

    /**
     * Finds and returns the local IPv4 address of the machine. The method iterates through
     * all network interfaces and their associated addresses, looking for the first non-loopback,
     * non-link-local IPv4 address. If no such address is found, it defaults to "127.0.0.1".
     *
     * @return The local IPv4 address as a String, or "127.0.0.1" if no suitable address is found.
     * @throws SocketException if an I/O error occurs while retrieving the network interfaces.
     */
    private String findLocalAddress() throws SocketException {
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        while (ifaces.hasMoreElements()) {
            for (InetAddress addr : Collections.list(ifaces.nextElement().getInetAddresses())) {
                if (addr instanceof Inet4Address &&
                        !addr.isLoopbackAddress() &&
                        !addr.isLinkLocalAddress()) {
                    return addr.getHostAddress();
                }
            }
        }
        return "127.0.0.1";
    }

    /**
     * Initiates the execution process for the current instance by setting up
     * the username, determining the local network address, configuring the
     * system property for RMI, establishing a connection, and starting
     * the heartbeat mechanism.
     *
     * @param username the username associated with the current execution context
     * @throws RemoteException if a remote communication error occurs
     * @throws SocketException if there is an error in the underlying socket layer
     */
    public void run(String username) throws RemoteException, SocketException {
        setName(username);
        String host;
        host = findLocalAddress();
        System.setProperty("java.rmi.server.hostname", host);
        this.serv.connect(this, uuid, username);
        startHeartbeat();
    }

    /**
     * Method to handle the activation of an abandoned station during the game.
     * This operation queues a command to execute the corresponding method
     * in the internal model representation with the provided parameters.
     *
     * @param player       The player initiating the activation of the abandoned station.
     * @param flightBoard  The game's flight board associated with the current state.
     * @param yOn          A boolean flag indicating a specific condition during activation.
     * @param storagetiles A 2D list representing the storage tiles involved in the activation.
     * @param newgoods     A 2D list of goods to be processed or added as a result of the activation.
     *
     * @throws RemoteException If a communication-related error occurs during the remote method call.
     */
    @Override
    public void chooseAbandonedStationActivate(Player player, FlightBoard flightBoard, boolean yOn, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.chooseAbandonedStationActivate(player, flightBoard, yOn, storagetiles, newgoods);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Initiates the process to claim a reward for smuggling by scheduling the task in the command queue.
     *
     * @param yOn           A boolean indicating whether the player opts to claim the reward.
     * @param player        The player object representing the participant in the game.
     * @param storagetiles  A nested list of integers representing the state of storage tiles.
     * @param newgoods      A nested list of goods representing the new items being added as part of the claim.
     * @throws RemoteException If there is a remote communication error during the operation.
     */
    @Override
    public void chooseToClaimRewardSmug(boolean yOn, Player player, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.chooseToClaimRewardSmug(yOn, player, storagetiles, newgoods);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the action where slavers decide to claim or not claim a reward.
     * This method queues the action to be performed and interacts with the model representation.
     *
     * @param player The player who is initiating the action.
     * @param y      A boolean indicating whether the reward is being claimed (true) or not (false).
     * @throws RemoteException If a communication-related exception occurs during the process.
     */
    @Override
    public void slaversChooseToClaimReward(Player player, boolean y) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.slaversChooseToClaimReward(y, player);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the state of a card using the provided state object. This method
     * adds the update operation to a command queue that executes the update
     * asynchronously.
     *
     * @param stato the state object representing the new state of the card
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void updateCardUseSTATE(c_State stato) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateCardUseSTATE(stato);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method indicates that there are not enough goods available.
     * It adds a command to the commandQueue, which when executed,
     * calls the notEnoughGoods method of the littleModelRep instance.
     *
     * @throws Exception if any exception occurs during the execution of notEnoughGoods in littleModelRep.
     */
    @Override
    public void notEnoughGoods() throws Exception {
        commandQueue.add(() -> {
            try {
                littleModelRep.notEnoughGoods();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    /**
     * Processes the action of losing batteries for the specified player.
     *
     * @param p The player who is losing batteries.
     * @param batteriesToAct A list of battery groups to act on, each group represented by a list of integers.
     * @throws Exception If an error occurs during the process of losing batteries.
     */
    public void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct) throws Exception{
        commandQueue.add(() -> {
            try {
                littleModelRep.lostBatteries(p,batteriesToAct);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the remaining battery count for a specified player.
     * This method schedules the update within a command queue to ensure proper execution order.
     *
     * @param p The player whose remaining battery count is being updated.
     * @param batt The new remaining battery count to be set for the player.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateBatteriesRemaining(Player p, int batt) throws Exception {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateBatteriesRemaining(p,batt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Removes an alien from the specified location for the given user.
     *
     * @param username The username associated with the user trying to remove the alien.
     * @param r The row index of the alien's location to be removed.
     * @param c The column index of the alien's location to be removed.
     * @throws Exception If an error occurs during the removal process.
     */
    @Override
    public void removeAlien(String username, int r, int c) throws Exception {
        commandQueue.add(() -> {
            try {
                littleModelRep.removeAlien(username,r,c);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Processes and corrects the input through a remote call. This method submits a task
     * to the command queue to invoke the `correctInput` method on the `littleModelRep`.
     * If any exception occurs during the execution of the task, it is wrapped and
     * re-thrown as a runtime exception.
     *
     * @throws RemoteException if a remote invocation error occurs
     */
    @Override
    public void correctInput() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.correctInput();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Deposits the item currently in the player's hand into the appropriate location.
     *
     * @param playerNickname the nickname of the player whose item in hand is to be deposited
     * @throws RemoteException if there is a communication-related exception during the operation
     */
    @Override
    public void depositThingInHand(String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.depositThingInHand(playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the end of an effect and delegates the task to execute within the command queue.
     * This method ensures the corresponding action in the littleModelRep is finalized
     * and wraps any exceptions encountered during the process into a RuntimeException.
     *
     * @throws RemoteException if a communication-related error occurs during the execution.
     */
    @Override
    public void effectEnded() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.effectEnded();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Signals the end of a building phase for a specified player and transitions to a new position.
     *
     * @param playerNickname the nickname of the player who has finished the building phase
     * @param positionwheretogo the position to transition to after the building phase
     * @throws RemoteException if a networking-related error occurs during the operation
     */
    @Override
    public void endbuilding(String playerNickname, int positionwheretogo) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.endbuilding(playerNickname, positionwheretogo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the initiation of an effect by adding a task to the command queue.
     * This method ensures that the effect's start logic is executed asynchronously
     * within the command queue. Internally, it calls the effectStarted method on
     * the littleModelRep instance.
     *
     * @throws RemoteException if a communication-related exception occurs during
     *                         remote method invocation.
     */
    @Override
    public void effectStarted() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.effectStarted();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Ends the building phase for all entities in the context of the application.
     *
     * This method queues a command to execute the `endBuildingPhaseForAll` functionality
     * on the `littleModelRep` object. It ensures that any ongoing or pending building
     * phases for all relevant entities are terminated.
     *
     * The operation is performed asynchronously by adding the task to a command queue.
     * If an exception occurs during the execution of the task, it is wrapped and
     * propagated as a `RuntimeException`.
     *
     * @throws RemoteException if a remote method invocation issue occurs.
     */
    @Override
    public void endBuildingPhaseForAll() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.endBuildingPhaseForAll();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Sends a penalty with the specified penalty value and type.
     * This method queues the penalty action to be sent to the remote model representation.
     *
     * @param penalty The penalty value to be sent.
     * @param type The type of penalty to be sent.
     * @throws RemoteException If there is an issue with the remote communication.
     */
    @Override
    public void sendPenalty(int penalty, String type) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.sendPenalty(penalty, type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Activates the abandoned ship taken event for the specified player and position persistence.
     *
     * @param player The player for whom the abandoned ship taken event is to be activated.
     * @param posPers A 2D list representing the position persistence details.
     * @throws Exception If an error occurs during activation.
     */
    @Override
    public void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers) throws Exception {
        commandQueue.add(() -> {
            try {
                littleModelRep.abandonedShipTakenActivate(player, posPers);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Notifies the system that a player has lost in the game.
     *
     * @param playerNickname the nickname of the player who has lost
     * @throws RemoteException if a remote invocation error occurs
     */
    @Override
    public void lost(String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.lost(playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Activates the epidemic state base functionality for the provided set of tiles.
     * This method processes the state activation using asynchronous command execution.
     *
     * @param AlreadyVisited a set of SpaceShipTile objects that have already been visited and need processing during the epidemic state activation
     * @throws RemoteException if a communication-related exception occurs during the remote method invocation
     */
    @Override
    public void epidemicStateBaseActivate(Set<SpaceShipTile> AlreadyVisited) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.epidemicStateBaseActivate(AlreadyVisited);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Inserts a tile into the Little Model Representation (LMR) at the specified position and orientation,
     * associating the operation with the given player's nickname.
     *
     * @param TileIndex the index of the tile to be inserted
     * @param r the row position where the tile should be placed
     * @param c the column position where the tile should be placed
     * @param rotation the rotation to be applied to the tile
     * @param playerNickname the nickname of the player performing this action
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void insertTileLMR(int TileIndex, int r, int c, int rotation, String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.insertTileLMR(TileIndex, r, c, rotation, playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the logic for marking goods as lost for the player's ship board.
     * This method enqueues a command in the command queue that iterates through
     * all ship boards in the model representation. If the ship board belongs
     * to the player associated with the current nickname, it triggers the logic
     * to mark goods as lost for that player.
     *
     * @throws RemoteException if there is an issue with the remote method invocation.
     */
    @Override
    public void lostGoods() throws RemoteException {
        commandQueue.add(() -> {
            try {
                for (ShipBoard s : littleModelRep.getShipBoards()) {
                    if (littleModelRep.getMyNickname().equals(s.getMyPlayer().getUsername())) {
                        littleModelRep.lostGoods(s.getMyPlayer());
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Invokes the `youPayConsequences` method on the `littleModelRep` object
     * asynchronously by adding the command to a `commandQueue`. This method
     * ensures the correct execution of the command within a queued task.
     *
     * The queued task attempts to execute the `youPayConsequences` method of
     * `littleModelRep`. If an exception occurs during execution, it will be
     * wrapped in a `RuntimeException` and thrown.
     *
     * @throws RemoteException if a remote invocation error occurs.
     */
    @Override
    public void youPayConsequences() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.youPayConsequences();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the logic for choosing how to face meteors during the gameplay.
     * This method delegates the action to a model representation to execute
     * meteor card-related actions while supporting async execution via a command queue.
     *
     * @param player                  The player performing the action.
     * @param howToDefenceFromShots   A list of integers representing the defense strategy for incoming shots.
     * @param shots                   A list of shot objects representing the meteor shots to handle.
     * @param dice                    The value of the dice roll associated with the action.
     * @param currentShot             The index or identifier of the current shot being addressed.
     * @throws RemoteException        If an error occurs during remote method invocation.
     */
    @Override
    public void meteorCardChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, ArrayList<Shot> shots, int dice, int currentShot) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.meteorCardChooseHowToFaceMeteors(player, howToDefenceFromShots, shots, dice, currentShot);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Advances the game to the next player's turn.
     * This method queues a task to invoke the nextPlayerTurn operation in the underlying model representation.
     * If an exception occurs while processing the next player's turn in the model, it is wrapped and propagated as a RuntimeException.
     *
     * @throws RemoteException if a communication-related error occurs during the remote method execution.
     */
    @Override
    public void nextPlayerTurn() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.nextPlayerTurn();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the management of a shot received by a player, handling the shot details,
     * the defensive actions, and the dice value associated with this event.
     *
     * @param player the player who received the shot
     * @param shot the shot object representing the shot received
     * @param howToDefenceFromShots an ArrayList containing integers that represent the methods of defense
     * @param dice the integer value of the dice roll associated with this shot
     * @throws RemoteException if a communication-related error occurs during remote method execution
     */
    @Override
    public void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateManageShotReceived(player, shot, howToDefenceFromShots, dice);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Ensures the execution of the `noSurrender` operation on the remote model representation in a synchronized manner.
     * This method queues the operation in the command queue for deferred execution and handles any exceptions
     * that may occur during its execution.
     *
     * @throws RemoteException if an error occurs during the remote method invocation.
     */
    @Override
    public void noSurrender() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.noSurrender();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Moves a player to a specific position on the flight board.
     *
     * @param playerNickname the nickname of the player to move on the flight board
     * @param pos the position to which the player is to be moved on the flight board
     * @throws RemoteException if a network-related error occurs during remote method execution
     */
    @Override
    public void movePlayerInFlightBoard(String playerNickname, int pos) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.movePlayerInFlightBoard(playerNickname, pos);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Notifies the player that it is not their turn to perform an action.
     * This method is executed remotely and enqueues a command to invoke
     * the corresponding operation on the underlying model representation.
     *
     * @throws RemoteException if a communication-related error occurs during the execution of the remote call.
     */
    @Override
    public void notYourTurn() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.notYourTurn();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the model representation by reporting incorrect tile positions
     * involving the specified nicknames.
     *
     * @param pos a list of integers representing the positions of the incorrect tiles
     * @param nick the nickname of the player to whom the incorrect tiles are attributed
     * @param nickEff the nickname of the player causing the effect
     * @throws RemoteException if a remote invocation error occurs
     */
    @Override
    public void wrongTiles(ArrayList<Integer> pos, String nick, String nickEff) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.wrongTiles(pos, nick, nickEff);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the game creation process by delegating the provided game details to the little model representation.
     *
     * @param level the level of the game being updated
     * @param ShowableDecks a list of decks that will be visible during the game
     * @param players a list of players participating in the game
     * @param hourglass the hourglass value for the game
     * @param surrender the surrender parameter for the game
     * @throws RemoteException if a remote communication error occurs during execution
     */
    @Override
    public void updateGameCreated(int level, List<Deck> ShowableDecks, ArrayList<Player> players, int hourglass, int surrender) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateGameCreated(level, ShowableDecks, players, hourglass, surrender);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Finalizes the building phase for the underlying model representation.
     * This method schedules the completion logic to be executed within a command queue.
     *
     * The method uses a queued command to delegate the completeBuildingPhase logic
     * to the littleModelRep instance. If an exception occurs during this process,
     * it will be wrapped and rethrown as a RuntimeException.
     *
     * @throws Exception if an error occurs during the building phase completion. The exception
     *                   may be wrapped in a RuntimeException if it occurs during the command execution.
     */
    @Override
    public void completeBuildingPhase() throws Exception {
        commandQueue.add(() -> {
            try {
                littleModelRep.completeBuildingPhase();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Picks a tile from the game using the specified tile index and assigns it to the player.
     * This method interacts with the littleModelRep to perform the operation and queues the action.
     *
     * @param TileIndex the index of the tile to be picked
     * @param playerNickname the nickname of the player performing the action
     * @throws RemoteException if there is an error during remote communication
     */
    @Override
    public void pickTileLMR(int TileIndex, String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.pickTileLMR(TileIndex, playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Picks a little deck for the specified player based on the provided deck index.
     *
     * @param deckIndex the index of the deck to be picked
     * @param playerNickname the nickname of the player selecting the deck
     * @throws RemoteException if a communication-related exception occurs during the remote method call
     */
    @Override
    public void pickLittleDeckLMR(int deckIndex, String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.pickLittleDeckLMR(deckIndex, playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Adds either an alien or human to the Little Model Representation (LMR) at the specified location.
     * This method schedules the action in a command queue to ensure it is executed properly.
     *
     * @param playerNickname the nickname of the player making the action
     * @param wantAlien boolean indicating whether to add an alien (true) or a human (false)
     * @param alienColor the color of the alien to be added, applicable only if wantAlien is true
     * @param row the row position where the alien or human should be placed
     * @param column the column position where the alien or human should be placed
     * @throws Exception if any error occurs while adding the alien or human
     */
    @Override
    public void addAlienOrHumansLMR(String playerNickname, boolean wantAlien, AlienColor alienColor, int row, int column) throws Exception {
        commandQueue.add(() -> {
            try {
                littleModelRep.addAlienOrHumansLMR(playerNickname, wantAlien, alienColor, row, column);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the position of a player on the flight board.
     *
     * @param playerNickname the nickname of the player whose position is being updated
     * @param pos the new position of the player on the flight board
     * @throws RemoteException if a communication-related exception occurs
     */
    @Override
    public void setPlayerPosInFlightBoard(String playerNickname, int pos) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.setPlayerPosInFlightBoard(playerNickname, pos);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the usage of a specific card by delegating the update to the underlying model representation.
     * The operation is executed through a command queue to ensure thread-safe execution.
     *
     * @param card the card object whose usage needs to be updated
     * @throws RemoteException if there is an issue with remote communication during the update process
     */
    @Override
    public void updateCardUse(Card card) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateCardUse(card);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the status by delegating it to the littleModelRep instance.
     * The method execution is queued as a command which is processed later.
     *
     * @param stato the new state to be updated, provided as an instance of the stateEnum
     * @throws RemoteException if there is an issue with the remote call
     */
    @Override
    public void updateStatus(stateEnum stato) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateStatus(stato);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the final scores in the system using the provided scores map.
     *
     * @param finalScores a map containing the identifiers as keys and the corresponding scores as values
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void updateFinalScores(HashMap<String, Float> finalScores) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateFinalScores(finalScores);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the player's battery positions and asserts correctness by executing the provided
     * logic asynchronously within the command queue.
     *
     * @param p The player whose battery positions need to be updated and asserted.
     * @param posBatAndNumBattXPos A list of lists where each sublist contains integers
     *                             representing battery positions and number of batteries at specific positions.
     * @throws RemoteException If a remote communication error occurs during the update process.
     */
    @Override
    public void updateAssertBatteriesPos(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateAssertBatteriesPos(p, posBatAndNumBattXPos);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the addition of goods for a player by using specified positions of goods
     * and corresponding sets of goods. This method queues the update task to be executed
     * and communicates with the remote model representation to apply the changes.
     *
     * @param player the player whose goods are being updated
     * @param posGoods a list of positions representing the locations where the goods are being added
     * @param goodsSets a list of lists containing the sets of goods to be added at respective positions
     * @throws RemoteException if a remote communication error occurs during the update
     */
    @Override
    public void updateAddGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateAddGoods(player, posGoods, goodsSets);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the choice of passengers to lose for the given player with the provided consequences.
     * Executes the action by adding it to the command queue for processing.
     *
     * @param player the player who is choosing passengers to lose
     * @param c the consequences related to the player's decision
     * @param pass a nested list of integers representing passengers to be considered for loss
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void updateChoosePassengersToLose(Player player, Consequences c, ArrayList<ArrayList<Integer>> pass) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateChoosePassengersToLose(player, c, pass);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Activates the financial state of a planet for the specified players
     * and updates the given flight board with relevant information.
     *
     * @param playersss the list of players involved in the operation
     * @param flightBoard the flight board to be updated
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void planetFinStatActivate(ArrayList<Player> playersss, FlightBoard flightBoard) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.planetFinStatActivate(playersss, flightBoard);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the consequence of lost days for the specified player on the given flight board.
     *
     * @param player The player whose consequence of lost days needs to be updated.
     * @param flightBoard The flight board associated with the update operation.
     * @param numDays The number of days to be updated.
     * @param t A boolean flag representing additional context for the update.
     * @throws RemoteException If a remote method invocation exception occurs.
     */
    @Override
    public void updateConsequenceLostDays(Player player, FlightBoard flightBoard, int numDays, Boolean t) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateConsequenceLostDays(player, flightBoard, numDays, t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the player's status related to losing goods in the game.
     * This method processes changes in the player's state based on whether
     * the game is finished, battles are lost, or all battles are lost.
     *
     * @param player the player whose goods are being updated
     * @param finished indicates if the game or specific operation is finished
     * @param batttoloose indicates if the battle is to be considered lost
     * @param allbatlost indicates if all battles are lost
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void updateLooseAllGoods(Player player, Boolean finished, Boolean batttoloose, Boolean allbatlost) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateLooseAllGoods(player, finished, batttoloose, allbatlost);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Sends a message to prompt the user to choose a subship from the list of available subships.
     *
     * @param player the player who is prompted to choose a subship
     * @param subShips the list of subships available for selection, represented as a nested list of SpaceShipTile objects
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.messageToChooseSubship(subShips, player.getUsername());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Allows the player to choose a subship by specifying their nickname,
     * the list of available subships, their choice, and the related waste value.
     * This method queues a command to execute the selection of a subship.
     *
     * @param playerNickname the nickname of the player making the selection
     * @param subShips the list of available subships represented as a 2D ArrayList of SpaceShipTile objects
     * @param choice the index of the subship chosen by the player
     * @param waste the integer value representing waste associated with the chosen subship
     * @throws RemoteException if a remote communication error occurs during execution
     */
    public void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.ChooseSubship(playerNickname, subShips, choice, waste);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Removes a single tile from the board for a specific player at the given position,
     * with an option to indicate if it was a mistake and the waste generated.
     *
     * @param playerNickname the nickname of the player performing the action
     * @param row the row coordinate of the tile to remove
     * @param column the column coordinate of the tile to remove
     * @param fromMistake a flag indicating whether the removal is due to a mistake
     * @param waste the amount of generated waste as a result of the removal
     * @throws RemoteException if a remote invocation error occurs
     */
    public void removeSingleTile(String playerNickname, int row, int column, boolean fromMistake, int waste) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.removeSingleTile(playerNickname, row, column, fromMistake, waste);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Adds a tile, identified by its index, to the wait list for a specific player.
     *
     * @param playerNickname the nickname of the player requesting the tile to be added to the wait list
     * @param TileIndex the index of the tile to be added
     * @throws RemoteException if a remote communication issue occurs
     */
    public void addTileToWaitList(String playerNickname, int TileIndex) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.addTileToWaitList(playerNickname, TileIndex);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Inserts a wait tile into the Little Model Representation (LMR) by delegating the operation
     * to a queued command for asynchronous execution. This method is intended to update the model
     * representation with a specific tile associated with the provided player nickname.
     *
     * @param playerNickname the nickname of the player for which the tile is to be updated
     * @param TileIndex the index of the tile to be inserted into the model representation
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void insertWaitTileLMR(String playerNickname, int TileIndex) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.insertWaitTileLMR(playerNickname, TileIndex);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Ensures that empty tiles within the game's model representation are filled.
     * This method delegates the execution of the tile-filling logic to the {@code littleModelRep}
     * while wrapping it within a command added to the {@code commandQueue}.
     * The actual operation is executed asynchronously through the queued command.
     *
     * @throws RemoteException if the remote operation encounters an issue.
     */
    @Override
    public void haveToFillEmptyTiles() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.haveToFillEmptyTiles();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Removes a player from the flight board based on their nickname.
     *
     * @param playerNickname the nickname of the player to be removed from the flight board
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void removePlayerFromFlightboard(String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.removePlayerFromFlightboard(playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the state or calculation related to smugglers based on the player's information
     * and positions of cannons and batteries.
     *
     * @param player the player object containing relevant player information
     * @param cannonPos a list of lists representing the positions of cannons
     * @param batteriesPos a list of lists representing the positions of batteries
     * @throws RemoteException if a remote communication issue occurs
     */
    @Override
    public void updateSmugglersCalc(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateSmugglersCalc(player, cannonPos, batteriesPos);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the lost batteries for a player using the provided positions and number of batteries.
     *
     * @param p                    the player object whose lost batteries are being updated
     * @param posBatAndNumBattXPos a nested list representing the positions and their corresponding number of batteries
     * @param numbatt              the total number of batteries that are lost
     * @throws RemoteException     if a remote communication error occurs
     */
    @Override
    public void updateLostBatteriesSmug(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, int numbatt) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateLostBatteriesSmug(p, posBatAndNumBattXPos, numbatt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the lost goods for smuggling in the game. This method adds a command to a queue
     * to execute the update logic, ensuring the game state remains consistent.
     *
     * @param p the player who lost the goods.
     * @param posGoods a nested list representing the positions of goods lost.
     * @param goodsSets a nested list of goods grouped by sets to be updated.
     * @param goodsListDiPrima a list of goods representing the primary list of lost goods.
     * @throws RemoteException if a communication-related exception occurs during the update process.
     */
    @Override
    public void updateLostGoodsSmug(Player p, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, ArrayList<Goods> goodsListDiPrima) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateLostGoodsSmug(p, posGoods, goodsSets, goodsListDiPrima);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Allows Slavers to choose positions for cannon batteries.
     * This method is executed as a command and delegates the action to the model representation.
     *
     * @param player        The player choosing the cannon battery positions.
     * @param cannonPos     A list of positions for cannons, represented as a nested list of integers.
     * @param batteriesPos  A list of positions for batteries, represented as a nested list of integers.
     * @throws RemoteException If a remote communication failure occurs during execution.
     */
    @Override
    public void slaversChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.slaversChooseCannonBatteryPos(player, cannonPos, batteriesPos);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Initiates the process where slavers decide on which passengers to be lost, based on the given parameters.
     *
     * @param player the player instance that is required for the operation
     * @param yOn a boolean value indicating some state or condition affecting the decision
     * @param tiles a 2D list representing tile coordinates or states involved in the operation
     * @throws RemoteException if a communication-related exception occurs during remote invocation
     */
    @Override
    public void slaversChoosePassengersToLose(Player player, boolean yOn, ArrayList<ArrayList<Integer>> tiles) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.slaversChoosePassengersToLose(player, yOn, tiles);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Allows the pirates to choose a position for the cannon and battery. This method queues the command
     * to be executed within the game model representation.
     *
     * @param player         The player who is selecting the cannon and battery positions.
     * @param cannonPos      A list of potential positions for the cannon represented as a list of coordinates.
     * @param batteriesPos   A list of potential positions for the battery represented as a list of coordinates.
     * @throws RemoteException If a remote communication error occurs during execution.
     */
    @Override
    public void piratesChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.piratesChooseCannonBatteryPos(player, cannonPos, batteriesPos);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the decision-making process for pirates on how to face meteors
     * during a game. This method queues the decision logic to be executed
     * asynchronously in the game model representation.
     *
     * @param player the player associated with the pirates making the decision
     * @param howToDefenceFromShots a list of integers representing the defensive actions to take
     * @param shot the shot object related to the current meteor
     * @param dice the dice value influencing the decision-making process
     * @throws RemoteException if there is an issue with remote method invocation
     */
    @Override
    public void piratesChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, Shot shot, int dice) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.piratesChooseHowToFaceMeteors(player, howToDefenceFromShots, shot, dice);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Executes the action where pirates decide to claim a reward within the game logic.
     * The decision is queued and handled asynchronously.
     *
     * @param yOn a boolean indicating the decision outcome; true if pirates choose to claim the reward, false otherwise
     * @param player the Player object representing the player associated with the action
     * @throws RemoteException if there is an issue with remote communication during the operation
     */
    @Override
    public void piratesChooseToClaimReward(boolean yOn, Player player) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.piratesChooseToClaimReward(yOn, player);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the action of opening a space and choosing to start a motor.
     * This method is executed remotely and involves various elements of the game model.
     *
     * @param player The player who is performing the action.
     * @param flightBoard The current state of the flight board.
     * @param enginesPos The positions of the engines on the flight board.
     * @param batteriesPos The positions of the batteries on the flight board.
     * @throws RemoteException If a remote invocation error occurs.
     */
    @Override
    public void openSpaceChooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.openSpaceChooseToStartMotor(player, flightBoard, enginesPos, batteriesPos);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Triggers the stardust effect by delegating the call to the underlying model representation.
     * This method adds a task to the command queue, where it will attempt to invoke the
     * stardustEffect method on the littleModelRep instance.
     *
     * This operation may throw a RuntimeException if the underlying method execution encounters
     * an error. The exception is wrapped to ensure appropriate handling within the command queue context.
     *
     * @throws RemoteException if a remote call issue occurs during the method execution.
     */
    @Override
    public void stardustEffect() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.stardustEffect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the remaining goods for a specific player by queuing the update command
     * for asynchronous execution.
     *
     * @param p the player whose goods are to be updated
     * @param goodFInali the list of goods that represents the updated goods for the player
     * @throws RemoteException if a remote communication error occurs during the update
     */
    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateGoodsRemaining(p, goodFInali);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the scenario where the timer has not started. This method enqueues
     * a command that invokes the `timerNotStarted` method on the `littleModelRep`
     * instance. Any exceptions encountered during the invocation are wrapped and
     * rethrown as a runtime exception.
     *
     * @throws RemoteException if an error occurs during remote method invocation.
     */
    @Override
    public void timerNotStarted() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.timerNotStarted();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method is invoked to indicate that a tile has not been flipped.
     * It uses the command queue to schedule the `tileNotFlipped` operation
     * on the underlying model representation. Any exception during the
     * execution will result in a runtime exception being thrown.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void tileNotFlipped() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.tileNotFlipped();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method is invoked to handle a scenario where an incorrect input is detected.
     * It delegates the handling of the wrong input to the `littleModelRep` instance
     * asynchronously by adding a command to the `commandQueue`.
     *
     * This method wraps any exceptions thrown by the `littleModelRep.wrongInput()` method
     * inside a `RuntimeException` and propagates it.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void wrongInput() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.wrongInput();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Notifies the underlying model representation of an incorrect player action or selection.
     * The method adds a command to the command queue which will execute this notification.
     * Any exceptions occurring during the execution of the command are wrapped in a runtime exception.
     *
     * @throws RemoteException if a remote invocation issue occurs during the process.
     */
    @Override
    public void wrongPlayer() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.wrongPlayer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Notifies that a player has won the game.
     *
     * @param playerNickname the nickname of the player who has won
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void someoneWon(String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.someoneWon(playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Processes the tie action for a specific player by adding the action to the command queue for execution.
     * The action indicates that the player has reached a tie state in the game.
     *
     * @param playerNickname the nickname of the player initiating the tie action
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void tie(String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.tie(playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Handles the refusal of a reward for the specified player.
     *
     * @param playerNickname the nickname of the player who refused the reward
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void refusedReward(String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.refusedReward(playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Updates the dice value by delegating the update operation to a remote model
     * representation. The method ensures that the update is performed asynchronously
     * by adding the operation to a command queue for execution.
     *
     * @param dice the new dice value to be updated
     * @throws RemoteException if a communication-related error occurs during the update
     */
    @Override
    public void updateDice(int dice) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.updateDice(dice);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Executes a command to indicate that a deposit operation cannot be performed.
     * This method is invoked asynchronously by adding the task to a command queue.
     * It communicates with a remote model representation to handle the "cannot deposit" logic.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void cannotDeposit() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.cannotDeposit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Invokes the `cannotInsert` method on the remote model representation
     * within a queued command. This method is executed asynchronously by
     * adding it to the command queue.
     *
     * The `cannotInsert` method on the `littleModelRep` object is called
     * within this queue operation. If any exception occurs during the execution
     * of this remote method, it is wrapped in a RuntimeException and rethrown.
     *
     * @throws RemoteException if a communication-related error occurs during
     *         the execution of the method.
     */
    @Override
    public void cannotInsert() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.cannotInsert();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Invokes the cannotPick action on the LittleModelRep instance asynchronously.
     * This method adds the action to a command queue to be executed at a later point in time.
     * Any exceptions encountered during the execution of the action are wrapped
     * and rethrown as a RuntimeException.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void cannotPick() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.cannotPick();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Indicates that a specific action cannot be fulfilled.
     *
     * This method enqueues a command into the command queue that,
     * when executed, invokes the `cannotFill` method on the
     * `littleModelRep` instance. If an exception occurs during
     * this invocation, it is wrapped in a RuntimeException and thrown.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void cannotFill() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.cannotFill();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Indicates that the model is currently blocked.
     * This method submits a blocking operation to the command queue and executes it asynchronously.
     * The operation is delegated to the `littleModelRep.blocked` method.
     * Handles any exceptions thrown during execution and wraps them into a RuntimeException.
     *
     * @throws RemoteException if there is a communication-related issue.
     */
    @Override
    public void blocked() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.blocked();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Stops the building process by delegating the request through a queued command.
     * The method queues a task to call the stopBuilding method on the littleModelRep object.
     * If an exception occurs during this process, it wraps the exception in a RuntimeException.
     *
     * @throws RemoteException if a remote communication issue occurs while stopping the building process.
     */
    @Override
    public void stopBuilding() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.stopBuilding();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method is called when a timer starts. It adds a command to the
     * command queue which subsequently invokes the `timerStarted` method
     * on the `littleModelRep` instance. If an exception is encountered,
     * it is wrapped and rethrown as a RuntimeException.
     *
     * @throws RemoteException if a remote invocation error occurs
     */
    @Override
    public void timerStarted() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.timerStarted();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Notifies the system that the timer has already started. This method queues the notification
     * to be processed by the command queue, ensuring thread-safe communication with the underlying
     * model representation. Any exceptions encountered during the execution are captured and rethrown
     * as runtime exceptions.
     *
     * @throws RemoteException if a communication-related exception occurs during the remote method call.
     */
    @Override
    public void timerAlreadyStarted() throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.timerAlreadyStarted();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Removes a block of SpaceShipTile objects associated with a specific player.
     * The operation is queued and executed asynchronously.
     *
     * @param block the list of SpaceShipTile objects to be removed
     * @param playerNickname the nickname of the player associated with the block to be removed
     * @throws RemoteException if a remote communication issue occurs
     */
    @Override
    public void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.removeBlock(block, playerNickname);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Indicates that a timer has ended and performs an associated action.
     *
     * @param b a Boolean value indicating the state of the timer when it ends
     * @throws RemoteException if a remote communication error occurs
     */
    public void timerEnded(Boolean b) throws RemoteException {
        commandQueue.add(() -> {
            try {
                littleModelRep.timerEnded(b);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * Creates a new game with the specified parameters.
     *
     * @param level     The difficulty level of the game.
     * @param id        The unique identifier for the game instance.
     * @param numplayrs The number of players participating in the game.
     * @param Nickname  The nickname of the initiating user or player.
     * @throws Exception If there is an issue creating the game.
     */
    @Override
    public void createGame(int level, UUID id, int numplayrs, String Nickname) throws Exception {
        serv.createGame(level, id, numplayrs, Nickname);
    }

    /**
     * Joins a game session with the given parameters.
     *
     * @param NickName    the nickname of the player joining the game
     * @param NumOfPlayers the number of players currently in the game
     * @param id          the unique identifier for the current session
     * @param oldid       the unique identifier for the previous session
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void JoinGame(String NickName, int NumOfPlayers, UUID id, UUID oldid) throws RemoteException {
        serv.JoinGame(NickName, NumOfPlayers, id, oldid);
    }

    /**
     * Activates a timer for the specified player with the given unique identifier.
     *
     * @param player the name of the player for whom the timer is to be activated
     * @param id the unique identifier associated with the timer
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void activateTimer(String player, UUID id) throws RemoteException {
        serv.activateTimer(player, id);
    }

    /**
     * Sets the numerical tile information for a specified player and position.
     *
     * @param playerid the unique identifier of the player
     * @param posTile the positional index of the tile
     * @param id the unique identifier for the tile
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void setNumTile(int playerid, int posTile, UUID id) throws RemoteException {
        serv.setNumTile(playerid, posTile, id);
    }

    /**
     * Handles the surrender action for a player in the game.
     *
     * @param playerID The unique identifier of the player who is surrendering.
     * @param id The unique identifier of the game session or context.
     * @throws RemoteException If a remote invocation error occurs.
     */
    @Override
    public void Surrend(int playerID, UUID id) throws RemoteException {
        serv.Surrend(playerID, id);
    }

    /**
     * Visualizes the final scores for a given identifier.
     *
     * @param id the unique identifier for which the final scores are to be visualized
     * @throws RemoteException if a communication-related error occurs during the remote method call
     */
    @Override
    public void visualizeFinalScores(UUID id) throws RemoteException {
        serv.visualizeFinalScores(id);
    }

    /**
     * Handles the logic for selecting a tile that has already been flipped during the game.
     *
     * @param index   The index of the tile that has been flipped.
     * @param playerID The identifier of the player attempting to pick the tile.
     * @param id      The unique identifier for the game session.
     * @throws RemoteException If a communication-related exception occurs during the remote method call.
     */
    @Override
    public void pickTileAlreadyFlipped(int index, int playerID, UUID id) throws RemoteException {
        serv.pickTileAlreadyFlipped(index, playerID, id);
    }

    /**
     * Allows a player to pick an unknown tile during their turn.
     *
     * @param playerID the unique identifier of the player making the selection
     * @param id the unique identifier of the tile to be picked
     * @throws RemoteException if there is an issue with the remote method call
     */
    @Override
    public void pickTileUnknown(int playerID, UUID id) throws RemoteException {
        serv.pickTileUnknown(playerID, id);
    }

    /**
     * Picks a smaller deck from the available options based on the given index for a specific player.
     *
     * @param index the index of the deck to be picked
     * @param playerID the ID of the player making the selection
     * @param id a unique identifier for the related context or game session
     * @throws RemoteException if a remote communication error occurs during the operation
     */
    @Override
    public void pickLittleDeck(int index, int playerID, UUID id) throws RemoteException {
        serv.pickLittleDeck(index, playerID, id);
    }

    /**
     * Deposits the specified little deck for a player based on their ID and the deck's unique identifier.
     *
     * @param playerID the integer ID of the player who is depositing the little deck.
     * @param id the UUID of the little deck being deposited.
     * @throws RemoteException if a remote communication error occurs during the operation.
     */
    @Override
    public void depositLittleDeck(int playerID, UUID id) throws RemoteException {
        serv.depositLittleDeck(playerID, id);
    }

    /**
     * Deposits a tile for a specific player identified by their player ID.
     *
     * @param playerID the unique identifier for the player
     * @param id the unique identifier of the tile to be deposited
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void depositTile(int playerID, UUID id) throws RemoteException {
        serv.depositTile(playerID, id);
    }

    /**
     * Ends the building process for a player in the game and updates the state accordingly.
     *
     * @param PlayerID          the unique identifier of the player ending the building process
     * @param positionwheretogo the position or location where the player intends to move or take action next
     * @param id                the unique identifier of the game or session
     * @throws RemoteException  if a remote communication error occurs
     */
    @Override
    public void endbuilding(int PlayerID, int positionwheretogo, UUID id) throws RemoteException {
        serv.endbuilding(PlayerID, positionwheretogo, id);
    }

    /**
     * Inserts a tile into the specified position on the board with the given attributes.
     *
     * @param row the row index where the tile is to be inserted
     * @param col the column index where the tile is to be inserted
     * @param playerID the ID of the player performing the insertion
     * @param rotation the rotation angle of the tile
     * @param id the unique identifier for the tile
     * @throws RemoteException if a remote communication error occurs during the operation
     */
    @Override
    public void insertTile(int row, int col, int playerID, int rotation, UUID id) throws RemoteException {
        serv.insertTile(row, col, playerID, rotation, id);
    }

    /**
     * Activates the specified effect identified by the given UUID.
     *
     * @param id a UUID representing the identifier of the effect to be activated
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void EffectActivation(UUID id) throws RemoteException {
        serv.EffectActivation(id);
    }

    /**
     * Retrieves information about the card in use for the specified identifier.
     *
     * @param id the unique identifier of the card whose information is to be retrieved
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void getCardinuse(UUID id) throws RemoteException {
        serv.getCardinuse(id);
    }

    /**
     * Allows a player to choose a specific sub-ship based on the provided index.
     * This method communicates the choice to the server.
     *
     * @param index    the zero-based index of the desired sub-ship to be selected
     * @param playerID the unique identifier of the player making the selection
     * @param id       the unique identifier associated with the sub-ship
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void chooseOneSubShip(int index, int playerID, UUID id) throws RemoteException {
        serv.chooseOneSubShip(index, playerID, id);
    }

    /**
     * Processes the request to land on a planet by delegating it to the corresponding service method.
     *
     * @param p        the name or identifier of the planet to land on.
     * @param yOn      a boolean indicating whether the requester has permission to land.
     * @param NumPlanet the numerical identifier of the planet.
     * @param id       the unique identifier associated with the requester.
     * @throws RemoteException if a remote communication issue occurs during the operation.
     */
    @Override
    public void acceptToLandOnAPlanet(String p, boolean yOn, int NumPlanet, UUID id) throws RemoteException {
        serv.acceptToLandOnAPlanet(p, yOn, NumPlanet, id);
    }

    /**
     * Allows a player to choose an abandoned station, providing the necessary data for the selection process.
     *
     * @param player        the identifier of the player making the selection
     * @param yOn           a boolean indicating whether a certain condition related to the station is active
     * @param storageTiles  a list of storage tiles represented as a nested list of integers
     * @param newGoods      a list of newly available goods represented as a nested list of Goods objects
     * @param id            the unique identifier associated with the station or the selection action
     * @throws RemoteException if a remote communication error occurs during the operation
     */
    @Override
    public void chooseAbandonedStation(String player, boolean yOn, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods, UUID id) throws RemoteException {
        serv.chooseAbandonedStation(player, yOn, storageTiles, newGoods, id);
    }

    /**
     * Executes the action of selecting cannon and battery positions for the player.
     * This operation might involve remote communication.
     *
     * @param player        the name or identifier of the player making the selection
     * @param cannonPos     a list of positions representing the locations of cannons
     * @param batteriesPos  a list of positions representing the locations of batteries
     * @param id            a unique identifier associated with the operation or request
     * @throws RemoteException if a communication-related exception occurs during the remote method call
     */
    @Override
    public void chooseCannonBatteryPos(String player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos, UUID id) throws RemoteException {
        serv.chooseCannonBatteryPos(player, cannonPos, batteriesPos, id);
    }

    /**
     * Determines how a player chooses to defend against meteors in the game.
     *
     * @param player                  the name or identifier of the player making the decision
     * @param howToDefenceFromShots   a list of integers representing the defense strategies or actions selected by the player
     * @param id                      the unique identifier associated with the game or player session
     * @throws RemoteException        if a remote invocation error occurs
     */
    @Override
    public void chooseHowToFaceMeteors(String player, ArrayList<Integer> howToDefenceFromShots, UUID id) throws RemoteException {
        serv.chooseHowToFaceMeteors(player, howToDefenceFromShots, id);
    }

    /**
     * Chooses passengers to lose based on the specified parameters and invokes the corresponding service method.
     *
     * @param p a String parameter representing a key detail required for determining passengers to lose
     * @param yOn a boolean flag indicating specific conditions related to the process
     * @param pass a nested ArrayList of Integer objects representing passenger details or configurations
     * @param id a UUID object identifying the specific context or process instance
     * @throws RemoteException if there are issues related to remote method invocation
     */
    @Override
    public void choosePassengersToLose(String p, boolean yOn, ArrayList<ArrayList<Integer>> pass, UUID id) throws RemoteException {
        serv.choosePassengersToLose(p, yOn, pass, id);
    }

    /**
     * Allows a player to choose whether to claim a reward.
     *
     * @param yOn    A boolean value indicating the player's decision to claim the reward.
     *               True if the reward should be claimed, false otherwise.
     * @param player The name of the player making the decision to claim the reward.
     * @param id     The unique identifier (UUID) associated with the reward or player.
     * @throws RemoteException If there is an error during remote method invocation.
     */
    @Override
    public void chooseToClaimReward(boolean yOn, String player, UUID id) throws RemoteException {
        serv.chooseToClaimReward(yOn, player, id);
    }

    /**
     * Allows a player to choose to claim a reward based on the given parameters.
     *
     * @param yOn               A boolean indicating if the claiming option is active.
     * @param player            The identifier of the player attempting to claim the reward.
     * @param storageTiles      A nested list representing the storage tiles associated with the player.
     * @param newGoods          A nested list containing the new goods to be added as rewards.
     * @param id                A unique identifier associated with the claiming process.
     * @throws RemoteException  If there is an issue with remote method invocation.
     */
    @Override
    public void chooseToClaimReward(boolean yOn, String player, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods, UUID id) throws RemoteException {
        serv.chooseToClaimReward(yOn, player, storageTiles, newGoods, id);
    }

    /**
     * Allows the placement of batteries in the specified positions.
     *
     * @param p                         String identifier representing the placement context or configuration.
     * @param posBatAndNumBattXPos      A list of lists containing integers that represent the positions and the number of batteries to place at each position.
     * @param id                        A UUID representing a unique identifier for the operation.
     * @throws RemoteException          If a remote invocation error occurs.
     */
    @Override
    public void chooseToPlaceBatteries(String p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, UUID id) throws RemoteException {
        serv.chooseToPlaceBatteries(p, posBatAndNumBattXPos, id);
    }

    /**
     * Executes the procedure to initiate firepower based on the provided parameters.
     *
     * @param p The identifier or name representing the entity initiating the firepower.
     * @param DoubFireTriplets A list of triplets, where each triplet contains integers representing specific configurations or parameters for the firepower mechanism.
     * @param BatteriesToAct A list of batteries that need to act as part of the firepower initiation process.
     * @param id The unique identifier associated with the request or entity initiating the firepower.
     * @throws RemoteException If communication-related exceptions occur during the remote method call.
     */
    @Override
    public void chooseToStartFirePower(String p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct, UUID id) throws RemoteException {
        serv.chooseToStartFirePower(p, DoubFireTriplets, BatteriesToAct, id);
    }

    /**
     * Allows a player to choose to start a motor by interacting with specific positions of engines and batteries.
     *
     * @param player       The name or identifier of the player making the choice.
     * @param enginesPos   A 2D list where each inner list represents the positions of engines.
     * @param batteriesPos A 2D list where each inner list represents the positions of batteries.
     * @param id           A unique identifier for the session or operation.
     * @throws RemoteException If a remote invocation error occurs.
     */
    @Override
    public void chooseToStartMotor(String player, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos, UUID id) throws RemoteException {
        serv.chooseToStartMotor(player, enginesPos, batteriesPos, id);
    }

    /**
     * Determines where to place goods for a specific player in the game based on provided positions and goods sets.
     *
     * @param player the name or identifier of the player making the decision
     * @param posGoods a list of possible positions represented as nested lists of integers
     * @param goodsSets a list of goods sets represented as nested lists of Goods objects
     * @param id the unique identifier for the game session or operation
     * @throws RemoteException if there is a problem with remote communication
     */
    @Override
    public void chooseWhereToPutGoods(String player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, UUID id) throws RemoteException {
        serv.chooseWhereToPutGoods(player, posGoods, goodsSets, id);
    }

    /**
     * Adds a wait tile for a specific player.
     *
     * @param playerID the unique identifier of the player for whom the wait tile is being added
     * @param id the unique identifier of the tile to be added to the wait queue
     * @throws RemoteException if a remote communication error occurs during the method call
     */
    @Override
    public void addWaitTile(int playerID, UUID id) throws RemoteException {
        serv.addWaitTile(playerID, id);
    }

    /**
     * Inserts a waiting tile into the game board at the specified position and orientation.
     *
     * @param playerID the unique identifier for the player performing the action
     * @param index the index of the tile to be inserted
     * @param row the row position where the tile will be placed
     * @param col the column position where the tile will be placed
     * @param rotation the rotation angle of the tile
     * @param id the unique identifier for the tile to be inserted
     * @throws RemoteException if there is a communication-related exception during the remote method call
     */
    @Override
    public void insertWaitTile(int playerID, int index, int row, int col, int rotation, UUID id) throws RemoteException {
        serv.insertWaitTile(playerID, index, row, col, rotation, id);
    }

    /**
     * Executes the CommandFillTile operation for the specified player, potentially filling a tile
     * on the board based on the provided parameters.
     *
     * @param playerid   The unique identifier of the player issuing the command.
     * @param wantalien  A boolean indicating whether the command involves an alien tile.
     * @param color      The color of the alien tile, applicable if `wantalien` is true.
     * @param row        The row index of the tile to be filled.
     * @param col        The column index of the tile to be filled.
     * @param id         The unique identifier for the operation or tile.
     * @throws RemoteException If a remote communication error occurs during execution.
     */
    @Override
    public void CommandFillTile(int playerid, boolean wantalien, AlienColor color, int row, int col, UUID id) throws RemoteException {
        serv.CommandFillTile(playerid, wantalien, color, row, col, id);
    }

    /**
     * Retrieves and processes active games associated with the specified unique user identifier.
     *
     * @param uuid the unique identifier of the user whose active games are to be retrieved
     * @throws RemoteException if a communication-related error occurs during the remote method call
     */
    @Override
    public void activeGames(UUID uuid) throws RemoteException {
        serv.activeGames(uuid);
    }

    /**
     * Demonstrates the game session associated with the provided unique identifier.
     *
     * @param uuid The unique identifier of the game session to be demonstrated.
     * @throws IOException If an I/O error occurs during the execution of the method.
     */
    @Override
    public void demoGame(UUID uuid) throws IOException {
        serv.demoGame(uuid);
    }
}

