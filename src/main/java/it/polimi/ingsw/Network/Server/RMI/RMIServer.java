package it.polimi.ingsw.Network.Server.RMI;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.GameControl.GameController;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.Dice.RandomDiceRoller;
import it.polimi.ingsw.Network.Client.RMI.VirtualServerRMI;
import it.polimi.ingsw.Network.Server.GenericServer;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Utils.stateEnum;

import java.net.*;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class RMIServer extends GenericServer implements VirtualServerRMI {
    /**
     * A thread-safe map that associates a unique match identified by a UUID to a list of VirtualViewRMI
     * instances representing the clients participating in the match.
     *
     * This map maintains the relationship between matches and their respective clients, allowing
     * efficient access and management of the client views for each match in a synchronized manner.
     */
    private final Map<UUID, List<VirtualViewRMI>> clientsByMatch = new ConcurrentHashMap<>();
    /**
     * A thread-safe queue that holds pending runnable tasks to be executed.
     * This queue employs a blocking mechanism, ensuring threads attempting to
     * add tasks or retrieve tasks will be properly synchronized.
     * The queue uses a {@link LinkedBlockingQueue}, which allows for an
     * optionally bounded capacity and follows the First-In-First-Out (FIFO)
     * order for processing tasks.
     */
    private final BlockingQueue<Runnable> requestQueue = new LinkedBlockingQueue<>();
    /**
     * A scheduled executor service used to manage and execute recurring tasks
     * required to maintain the application's heartbeat functionality. This
     * ensures periodic execution of tasks in a single-threaded environment.
     * The executor is designed to guarantee that tasks are executed sequentially
     * and one at a time.
     */
    private final ScheduledExecutorService heartbeat = Executors.newSingleThreadScheduledExecutor();
    /**
     * The TIMEOUT constant represents the maximum duration (in milliseconds)
     * for a certain operation to complete before it is considered as timed out.
     * This value is set to 15,000 milliseconds, which equals 15 seconds.
     * It is declared as a static final variable, making it immutable
     * and applicable at the class level.
     */
    private static final long TIMEOUT = 15_000;
    /**
     * A thread-safe map that associates each VirtualViewRMI object with a timestamp
     * (in milliseconds) indicating the last time a "pong" signal was received.
     * This data structure is used for tracking the liveliness of virtual views
     * in the system by storing the most recent "pong" response time for each view.
     */
    private final Map<VirtualViewRMI, Long> lastPongTime = new ConcurrentHashMap<>();
    private final ExecutorService pingExec = Executors.newCachedThreadPool();

    /**
     * Retrieves the list of VirtualViewRMI instances associated with the specified match ID.
     * If no list exists for the given ID, a new synchronized list is created, associated with the ID, and returned.
     *
     * @param id the unique identifier of the match
     * @return the list of VirtualViewRMI instances associated with the given match ID
     */
    private List<VirtualViewRMI> clients(UUID id) {
        return clientsByMatch.computeIfAbsent(id, _ -> Collections.synchronizedList(new ArrayList<>()));
    }

    /**
     * Retrieves a collection of all clients in the system by flattening the values
     * of the clientsByMatch map, which contains lists of clients grouped by match.
     *
     * @return a collection of VirtualViewRMI objects representing all clients.
     */
    private Collection<VirtualViewRMI> allClients() {
        return clientsByMatch.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Constructs an instance of the RMIServer class.
     * This constructor starts a thread responsible for continuously processing tasks
     * from the request queue. It uses a loop to retrieve and execute tasks from
     * the queue until the thread is interrupted. If interrupted, the thread will terminate.
     *
     * Additionally, the constructor initializes the heartbeat process by invoking
     * the method responsible for managing server heartbeat tasks.
     *
     * @throws RemoteException if the creation of the RMI server object fails.
     *         This may occur due to network errors or issues in the remote communication setup.
     */
    public RMIServer() throws RemoteException {
        super();

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    requestQueue.take().run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
        startHeartbeat();
    }

    /**
     * Initiates a periodic heartbeat mechanism to monitor the state of client connections and games.
     * <ul>
     * This method uses a scheduled task to perform the following operations at fixed intervals:
     *
     * - Iterates through a map of clients grouped by their matched games.
     * - For each connected client, it performs a ping to check for responsiveness.
     * - If a client does not respond within a defined timeout period, it is removed from the client list.
     * - If the game state is active and all clients have been removed, the game is ended.
     *
     * This ensures that unresponsive clients are cleaned up and any ongoing games are managed appropriately.
     *
     * The method schedules the task to run every 5 seconds with a fixed delay between executions.
     */
    private void startHeartbeat() {
        heartbeat.scheduleWithFixedDelay(() -> {
            for (UUID id : clientsByMatch.keySet()) {
                Game g = games.get(id);
                boolean isMatch = (g != null);
                List<VirtualViewRMI> list = clientsByMatch.get(id);
                if (list == null) continue;
                AtomicBoolean ended = new AtomicBoolean(false);
                list.removeIf(v -> {
                    long last = lastPongTime.getOrDefault(v, 0L);
                    Future<?> f = pingExec.submit(() -> {
                        try { v.ping(); } catch (RemoteException ignored) {}
                    });
                    try {
                        f.get(TIMEOUT, TimeUnit.MILLISECONDS);   // aspetta max 15 s
                        long nowPing = System.currentTimeMillis();
                        if (nowPing - last > TIMEOUT) {
                            if (isMatch && ended.compareAndSet(false, true)) {
                                g.endGame();
                            }
                            return true;            // rimuovi client
                        }
                        return false;               // client vivo
                    } catch (TimeoutException | ExecutionException e) { // non ha risposto
                        f.cancel(true);
                        if (isMatch && ended.compareAndSet(false, true)) {
                            try { g.endGame(); } catch (Exception ignored) {}
                        }
                        return true;                // rimuovi client
                    } catch (Exception ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                });
            }
        }, 0, 5, TimeUnit.SECONDS);
    }



    /**
     * Disconnects all players and terminates the game associated with the given match ID.
     * Notifies all clients of the shutdown due to game interruption, cleans up game data,
     * and removes related information from stored collections.
     *
     * @param matchId the unique identifier of the match to be terminated
     */
    public void disconnectAll(UUID matchId) {
        List<VirtualViewRMI> list = clientsByMatch.get(matchId);
        for (VirtualViewRMI v : list) {
            try {
                v.notifyShutdown("Game interrupted");
            } catch (RemoteException ignore) {
            }
        }
        Game g = games.get(matchId);
        List<Player> players = g.getPlayers();
        List<String> usernames = new ArrayList<>();
        for (Player p : players) {
            usernames.add(p.getUsername());
        }
        games.remove(matchId);
        controllers.remove(matchId);
        usernames.forEach(connectedUsernames::remove);
        clientsByMatch.remove(matchId);
        System.out.println("Match " + matchId + " terminated");
    }

    /**
     * Notifies the client associated with the specified player that there are not enough goods.
     *
     * @param player the player who needs to be notified about insufficient goods
     * @param id the unique identifier for the session or client connection
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void notEnoughGoods(Player player, UUID id) throws Exception {
        for (VirtualViewRMI v : clients(id)) {
            if (player.getUsername().equals(v.getName())) {
                v.notEnoughGoods();
            }
        }
    }

    /**
     * Removes an alien from the specified location and notifies all associated clients.
     *
     * @param username the username of the player performing the action
     * @param r the row coordinate of the alien to be removed
     * @param c the column coordinate of the alien to be removed
     * @param uuid the unique identifier of the session or context
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeAlien(String username, int r, int c, UUID uuid) throws Exception {
        for (VirtualViewRMI v : clients(uuid)) {
                v.removeAlien(username,r,c);
        }
    }

    /**
     * Notifies all clients associated with the specified UUID that a player has lost certain batteries.
     *
     * @param p the player who lost batteries
     * @param batteriesToAct a list of batteries that were lost, structured as a list of integer lists
     * @param uuid the unique identifier used to find the relevant clients
     * @throws Exception if an error occurs during the notification process
     */
    public void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct, UUID uuid) throws Exception {
        for (VirtualViewRMI v : clients(uuid)) {
            v.lostBatteries(p,batteriesToAct);
        }
    }

    /**
     * Finds the local IPv4 address of the machine that is not a loopback or link-local address.
     * If no such address is found, it defaults to returning "127.0.0.1".
     *
     * @return the local IPv4 address as a String, or "127.0.0.1" if no suitable address is found
     * @throws SocketException if an I/O error occurs while retrieving the network interfaces
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
     * Initializes and starts the RMI server by setting the hostname,
     * creating the registry, and binding the server object.
     *
     * @param args Command-line arguments passed to the method,
     *             typically unused in this implementation.
     * @throws RemoteException If an error occurs during registry creation
     *                         or binding the server instance.
     */
    public void start(String[] args) throws RemoteException {
//        String ip = "172.20.10.4";
//        System.setProperty("java.rmi.server.hostname", ip);
        try {
            String ip = findLocalAddress();
            System.setProperty("java.rmi.server.hostname", ip);
            System.out.println("RMI hostname set to: " + ip);
        } catch (SocketException e) {
            System.err.println("Warning: not found RMI ip address, use default");
        }
        Registry r = LocateRegistry.createRegistry(25565);
        r.rebind("GameServer", this);
    }

    /**
     * Creates a new game instance and sets up necessary configurations, including
     * managing the event bus, game controllers, and player associations. The method
     * assigns a new game ID and manages transitioning clients from a lobby
     * to the new game instance.
     *
     * @param level       The level or difficulty of the game.
     * @param uuid        The unique identifier associated with the game creation request.
     * @param numplayers  The number of players who will participate in the game.
     * @param nickname    The nickname of the player initiating the game creation.
     * @throws Exception  If an error occurs during the game creation process.
     */
    public void createGame(int level, UUID uuid, int numplayers, String nickname) throws Exception {
        requestQueue.add(() -> {
            try {
                Game g = new Game(level, new RandomDiceRoller(), 10, 120);
                EventBus eventBus = g.getEventBus();
                GameController c = new GameController(g, eventBus);
                UUID id = g.getMatchId();
                games.put(id, g);
                controllers.put(id, c);
                changeID(id, uuid);
                List<VirtualViewRMI> lobbyViews = clientsByMatch.remove(uuid);
                clientsByMatch
                        .computeIfAbsent(id, _ -> Collections.synchronizedList(new ArrayList<>()))
                        .addAll(lobbyViews);
                g.getEventBus().register(this);
                System.out.println("Game Created!!!");
                JoinGame(nickname, numplayers, id, id);
            } catch (Exception e) {
                try {
                    onError(uuid);
                } catch (Exception _) {
                }
            }
        });
    }


    /**
     * Establishes a connection with the game server using RMI and performs necessary setup
     * for maintaining the connection and tracking the user.
     *
     * @param v the VirtualViewRMI object representing the remote interface for the client
     * @param id the UUID of the client to uniquely identify it
     * @param u the username of the client attempting to connect
     * @throws RemoteException if the username is already in use or if any remote communication error occurs
     */
    public void connect(VirtualViewRMI v, UUID id, String u) throws RemoteException {
        if (!connectedUsernames.add(u)) throw new RemoteException("Username already in use");
        clients(id).add(v);
        lastPongTime.put(v, System.currentTimeMillis());
        System.out.println(u + ": Connected with rmi to GameServer");
    }

    /**
     * Stops the server and performs cleanup operations.
     *
     * This method notifies all connected clients of a server shutdown,
     * shuts down the heartbeat executor to stop ongoing tasks,
     * clears client and heartbeat tracking data, and attempts
     * to unexport the remote object associated with the server.
     *
     * Silent exception handling is employed for both
     * client notifications and remote object unexporting,
     * ensuring the stop process continues despite potential
     * issues with individual components.
     */
    public void stop() {
        for (VirtualViewRMI v : allClients())
            try {
                v.notifyShutdown("Server disconnected");
            } catch (RemoteException ignored) {
            }
        heartbeat.shutdownNow();
        clientsByMatch.clear();
        lastPongTime.clear();
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ignored) {
        }
    }

    /**
     * Changes the ID for all virtual views associated with the provided ID.
     *
     * @param matchId The new UUID that will replace the current one for the associated views.
     * @param id The UUID used to retrieve the associated virtual views for modification.
     * @throws Exception If an error occurs while changing the ID for any virtual view.
     */
    private void changeID(UUID matchId, UUID id) throws Exception {
        for (VirtualViewRMI v : clients(id)) {
            v.changeID(matchId);
        }
    }


    /**
     * Handles error scenarios for the specified match.
     *
     * @param matchId the unique identifier of the match where the error occurred
     * @throws Exception if an error occurs during error handling
     */
    @Override
    public void onError(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.error();
        }
    }

    /**
     * Updates the creation state of a game and notifies all clients associated with the given match ID.
     *
     * @param level      the level or difficulty of the game being created
     * @param decks      the list of decks used in the game
     * @param players    the list of players participating in the game
     * @param hourglass  the hourglass value or timer setting for the game
     * @param surrender  the surrender value or limit for the game
     * @param matchId    the unique identifier for the match
     * @throws Exception if an exception occurs during the update or client notification
     */
    @Override
    public void updateGameCreated(int level, List<Deck> decks, ArrayList<Player> players, int hourglass, int surrender, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateGameCreated(level, decks, players, hourglass, surrender);
        }
    }

    /**
     * Deposits the thing in hand for the specified player within the given match.
     *
     * @param nickname the nickname of the player whose thing in hand is to be deposited
     * @param matchId the unique identifier of the match in which the operation is to be performed
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void depositThingInHand(String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.depositThingInHand(nickname);
        }
    }

    /**
     * Executes the pickTileLMR operation for the specified player in a match.
     * The method iterates through all connected clients for the match and invokes their
     * respective pickTileLMR implementation.
     *
     * @param index the index of the tile to be picked
     * @param nickname the nickname of the player performing the action
     * @param matchId the unique identifier for the match
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void pickTileLMR(int index, String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.pickTileLMR(index, nickname);
        }
    }

    /**
     * Inserts a tile at a specified position with a specific rotation and notifies connected clients.
     *
     * @param index the index of the tile to be inserted
     * @param r the row position where the tile is to be placed
     * @param c the column position where the tile is to be placed
     * @param rotation the rotation to apply to the tile
     * @param nickname the nickname associated with the action
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void insertTileLMR(int index, int r, int c, int rotation, String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.insertTileLMR(index, r, c, rotation, nickname);
        }
    }

    /**
     * Allows a player to pick a little deck in the context of a match using the specified deck index and nickname.
     *
     * @param deckIndex the index of the deck to be picked
     * @param nickname the nickname of the player picking the little deck
     * @param matchId the unique identifier of the match associated with the pick
     * @throws Exception if an error occurs while processing the deck selection
     */
    @Override
    public void pickLittleDeckLMR(int deckIndex, String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.pickLittleDeckLMR(deckIndex, nickname);
        }
    }

    /**
     * Adds an alien or human to the specified location within the match.
     *
     * @param nickname the nickname of the player adding the entity
     * @param wantAlien specifies whether to add an alien (true) or a human (false)
     * @param color the color associated with the alien, ignored if adding a human
     * @param row the row coordinate where the entity is being added
     * @param column the column coordinate where the entity is being added
     * @param matchId the unique identifier of the match where the entity should be added
     * @throws Exception if an error occurs while adding the entity
     */
    @Override
    public void addAlienOrHumansLMR(String nickname, boolean wantAlien, AlienColor color, int row, int column, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.addAlienOrHumansLMR(nickname, wantAlien, color, row, column);
        }
    }

    /**
     * Completes the building phase for a specific match by notifying all connected clients.
     *
     * @param matchId the unique identifier of the match for which the building phase should be completed
     * @throws Exception if an error occurs while notifying the clients
     */
    @Override
    public void completeBuildingPhase(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.completeBuildingPhase();
        }
    }

    /**
     * Updates the player's position on the flight board for all virtual views
     * associated with the given match.
     *
     * @param nickname The nickname of the player whose position is being updated.
     * @param pos The new position of the player on the flight board.
     * @param matchId The unique identifier of the match to which the player belongs.
     * @throws Exception If an error occurs while setting the player's position.
     */
    @Override
    public void setPlayerPosInFlightBoard(String nickname, int pos, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.setPlayerPosInFlightBoard(nickname, pos);
        }
    }

    /**
     * Notifies all clients associated with the specified match about the end of a building action.
     *
     * @param nickname the nickname of the player who completed the building action
     * @param whereToGo an integer indicating the specific destination or action related to ending the building
     * @param matchId the unique identifier of the match in which the building action occurred
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void endbuilding(String nickname, int whereToGo, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.endbuilding(nickname, whereToGo);
        }
    }

    /**
     * Ends the building phase for all clients associated with the given match.
     *
     * @param matchId the UUID of the match for which the building phase should be ended
     * @throws Exception if an error occurs while ending the building phase for any client
     */
    @Override
    public void endBuildingPhaseForAll(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.endBuildingPhaseForAll();
        }
    }

    /**
     * Sends a penalty to all clients associated with a given match.
     *
     * @param penalty the penalty value to be sent
     * @param type the type of penalty being sent
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs while sending the penalty
     */
    @Override
    public void sendPenalty(int penalty, String type, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.sendPenalty(penalty, type);
        }
    }

    /**
     * Activates the abandoned ship taken process for the specified player within the given match.
     *
     * @param player the player who is activating the abandoned ship process
     * @param posPers a list of positions and related details relevant to the abandoned ship process
     * @param matchId the unique identifier of the match where the process is being activated
     * @throws Exception if an error occurs during the activation process
     */
    @Override
    public void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.abandonedShipTakenActivate(player, posPers);
        }
    }

    /**
     * Updates the card usage across all clients associated with the given match.
     *
     * @param card the card whose usage is being updated
     * @param matchId the unique identifier of the match for which the update is being performed
     * @throws Exception if an error occurs while updating the card use
     */
    @Override
    public void updateCardUse(Card card, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateCardUse(card);
        }
    }

    /**
     * Updates the card state for a list of players in the context of a specific match.
     *
     * @param ps the list of players whose card states need to be updated
     * @param state the new state to be applied to the players' cards
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateCardUseSTATE(ArrayList<Player> ps, c_State state, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {

            for (Player p : ps) {
                if (v.getName().equals(p.getUsername())) v.updateCardUseSTATE(state);
            }

        }
    }

    /**
     * Updates the status of the match identified by the given match ID.
     * Notifies all associated clients of the updated status.
     *
     * @param status The new status to be updated, represented as a value of the stateEnum.
     * @param matchId The unique identifier of the match whose status needs to be updated.
     * @throws Exception If an error occurs while notifying clients or updating the status.
     */
    @Override
    public void updateStatus(stateEnum status, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateStatus(status);
        }
    }

    /**
     * Updates the final scores for all clients associated with a specific match.
     *
     * @param finalScores a map where the key is the player's identifier as a String
     *                    and the value is the player's final score as a Float
     * @param matchId the unique identifier of the match whose final scores are being updated
     * @throws Exception if an error occurs during the score update process
     */
    @Override
    public void updateFinalScores(HashMap<String, Float> finalScores, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateFinalScores(finalScores);
        }
    }

    /**
     * Activates the process of choosing an abandoned station for the specified player within the game.
     *
     * @param player         The player initiating the action.
     * @param fb             The FlightBoard instance associated with the game.
     * @param yOn            A boolean indicating certain game state or condition.
     * @param storageTiles   A nested list representing the storage tiles configuration.
     * @param newGoods       A nested list of goods to be applied during the process.
     * @param matchId        The unique identifier of the match.
     * @throws Exception     If any issue occurs during the operation.
     */
    @Override
    public void chooseAbandonedStationActivate(Player player, FlightBoard fb, boolean yOn,
                                               ArrayList<ArrayList<Integer>> storageTiles,
                                               ArrayList<ArrayList<Goods>> newGoods, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.chooseAbandonedStationActivate(player, fb, yOn, storageTiles, newGoods);
        }
    }

    /**
     * Updates the list of asserted battery positions for a given player during a match.
     *
     * @param player the player for whom the battery positions are being updated
     * @param pos a list of lists containing integer positions to update
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateAssertBatteriesPos(Player player, ArrayList<ArrayList<Integer>> pos, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateAssertBatteriesPos(player, pos);
        }
    }

    /**
     * Updates the addition of goods for a specific player within the game, with specified positions
     * and sets of goods, broadcasting the update to all clients in the match.
     *
     * @param player the player whose goods are being updated
     * @param posGoods a nested list of integers representing the positions of the goods
     * @param goodsSets a nested list of goods objects representing the sets of goods to be added
     * @param matchId the unique identifier of the match where the update is taking place
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateAddGoods(Player player, ArrayList<ArrayList<Integer>> posGoods,
                               ArrayList<ArrayList<Goods>> goodsSets, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateAddGoods(player, posGoods, goodsSets);
        }
    }

    /**
     * Updates the list of passengers to lose for a specified player by notifying
     * all virtual clients associated with the given match ID.
     *
     * @param player the player who is affected by the update
     * @param c the consequences object containing details about the impact on the player
     * @param pass the list of potential passengers to lose, represented as an array of integer lists
     * @param matchId the unique identifier for the match to which the update applies
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateChoosePassengersToLose(Player player, Consequences c,
                                             ArrayList<ArrayList<Integer>> pass, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateChoosePassengersToLose(player, c, pass);
        }
    }

    /**
     * Activates the final planetary status for a given match and notifies all connected clients.
     *
     * @param players a list of Player objects representing the players involved in the match
     * @param fb the FlightBoard object containing the state of the flight board
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the activation or notification process
     */
    @Override
    public void planetFinStatActivate(ArrayList<Player> players, FlightBoard fb, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.planetFinStatActivate(players, fb);
        }
    }

    /**
     * Updates the consequence of lost days for a player within the context of a specific flight board and match.
     *
     * @param player The player whose lost days need to be updated.
     * @param fb The flight board context in which the update occurs.
     * @param numDays The number of days to be updated as lost.
     * @param t A flag indicating specific conditions related to the update.
     * @param matchId The unique identifier of the match for which the updating process applies.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateConsequenceLostDays(Player player, FlightBoard fb, int numDays, Boolean t, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateConsequenceLostDays(player, fb, numDays, t);
        }
    }

    /**
     * Updates the state of all goods to indicate they are lost for a specified player in a match.
     *
     * @param player The player for whom the update is being performed.
     * @param finished Indicates whether the process or match has finished.
     * @param batLose Indicates if the player has lost a specific battle.
     * @param allBatLost Indicates if all battles for the player have been lost.
     * @param matchId The unique identifier of the match where the update is applied.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateLooseAllGoods(Player player, Boolean finished, Boolean batLose, Boolean allBatLost, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateLooseAllGoods(player, finished, batLose, allBatLost);
        }
    }

    /**
     * Updates the smugglers calculation for a given player in a specific match.
     * This method iterates over all connected clients associated with the match
     * and triggers the necessary updates for the smugglers' calculation.
     *
     * @param player The player for whom the smugglers calculation is being updated.
     * @param cannonPos The positions of the cannon to be considered in the calculation.
     * @param batteriesPos The positions of the batteries to be considered in the calculation.
     * @param matchId The unique identifier for the match in which the update is being made.
     * @throws Exception If any issue occurs during the update process.
     */
    @Override
    public void updateSmugglersCalc(Player player, ArrayList<ArrayList<Integer>> cannonPos,
                                    ArrayList<ArrayList<Integer>> batteriesPos, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateSmugglersCalc(player, cannonPos, batteriesPos);
        }
    }

    /**
     * Updates the lost batteries for a smuggler in the game. This method sends the updated
     * battery information to all clients associated with the given match ID.
     *
     * @param player the player whose lost batteries information needs to be updated
     * @param pos a list of lists containing the positions related to the lost batteries
     * @param numbatt the number of lost batteries to be updated
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs while updating the lost batteries
     */
    @Override
    public void updateLostBatteriesSmug(Player player, ArrayList<ArrayList<Integer>> pos, int numbatt, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateLostBatteriesSmug(player, pos, numbatt);
        }
    }

    /**
     * Updates the lost goods information for smugglers and notifies all connected clients.
     *
     * @param player The player associated with the lost goods update.
     * @param posGoods A list of lists containing positions of goods to be updated.
     * @param goodsSets A list of lists containing sets of goods affected by the update.
     * @param goodsList A list of goods related to the update.
     * @param matchId The unique identifier of the match associated with the update.
     * @throws Exception If an error occurs during the update process or while notifying clients.
     */
    @Override
    public void updateLostGoodsSmug(Player player, ArrayList<ArrayList<Integer>> posGoods,
                                    ArrayList<ArrayList<Goods>> goodsSets,
                                    ArrayList<Goods> goodsList, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateLostGoodsSmug(player, posGoods, goodsSets, goodsList);
        }
    }

    /**
     * Allows the player to choose whether to claim a smuggling reward in the game.
     * This method interacts with connected clients to perform the operation.
     *
     * @param yOn              A boolean indicating the player's choice to claim the reward (true for yes, false for no).
     * @param player           The player making the choice.
     * @param storageTiles     A 2D array list representing the storage tiles in the game.
     * @param newGoods         A 2D array list of goods representing new items that can be added to the storage.
     * @param matchId          The unique identifier of the match.
     * @throws Exception       If an error occurs while executing the operation.
     */
    @Override
    public void chooseToClaimRewardSmug(boolean yOn, Player player,
                                        ArrayList<ArrayList<Integer>> storageTiles,
                                        ArrayList<ArrayList<Goods>> newGoods, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.chooseToClaimRewardSmug(yOn, player, storageTiles, newGoods);
        }
    }

    /**
     * Notifies all clients in the specified match to choose cannon and battery positions for the slavers.
     * This method propagates the given cannon and battery positions to the clients.
     *
     * @param player       the player who is responsible for choosing the cannon and battery positions
     * @param cannonPos    a list of lists of integers representing the positions of the cannon
     * @param batteriesPos a list of lists of integers representing the positions of the batteries
     * @param matchId      the unique identifier of the match where the action is taking place
     * @throws Exception   if an error occurs during the notification process to the clients
     */
    @Override
    public void slaversChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos,
                                              ArrayList<ArrayList<Integer>> batteriesPos, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.slaversChooseCannonBatteryPos(player, cannonPos, batteriesPos);
        }
    }

    /**
     * Informs the connected clients to process the action of slavers choosing passengers to lose.
     * This method is invoked for each connected client in a match.
     *
     * @param player The player initiating the action.
     * @param yOn A flag indicating a specific condition related to the passengers being chosen.
     * @param tiles A list of tile coordinates where the action will take place.
     * @param matchId The unique identifier for the match in which the action is performed.
     * @throws Exception If an error occurs while notifying the clients.
     */
    @Override
    public void slaversChoosePassengersToLose(Player player, boolean yOn,
                                              ArrayList<ArrayList<Integer>> tiles, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.slaversChoosePassengersToLose(player, yOn, tiles);
        }
    }

    /**
     * Allows the pirates to select cannon and battery positions during a game match.
     *
     * @param player       The player associated with the action.
     * @param cannonPos    A list of lists containing the potential cannon positions.
     * @param batteriesPos A list of lists containing the potential battery positions.
     * @param matchId      The unique identifier of the match in progress.
     * @throws Exception   If there is an issue performing the operation.
     */
    @Override
    public void piratesChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos,
                                              ArrayList<ArrayList<Integer>> batteriesPos, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.piratesChooseCannonBatteryPos(player, cannonPos, batteriesPos);
        }
    }

    /**
     * Allows the pirates to make a decision on how to face incoming meteors in the game.
     * This method communicates with the virtual views of the match to handle the choice
     * made by the pirates.
     *
     * @param player the player who needs to make the decision
     * @param defences a list of defence options available to the player
     * @param shot the shot object representing the current meteor attack
     * @param dice the dice roll value influencing the decision
     * @param matchId the unique identifier for the match
     * @throws Exception if an error occurs during the execution
     */
    @Override
    public void piratesChooseHowToFaceMeteors(Player player, ArrayList<Integer> defences, Shot shot, int dice, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.piratesChooseHowToFaceMeteors(player, defences, shot, dice);
        }
    }

    /**
     * Notifies all connected clients whether the pirates choose to claim a reward.
     *
     * @param yOn     a boolean indicating the pirates' decision on claiming the reward
     * @param player  the player associated with the action
     * @param matchId the unique identifier for the match
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void piratesChooseToClaimReward(boolean yOn, Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.piratesChooseToClaimReward(yOn, player);
        }
    }

    /**
     * Moves a player to a specified position on the flight board within a match.
     *
     * @param nickname the nickname of the player to be moved
     * @param pos the position to which the player should be moved
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void movePlayerInFlightBoard(String nickname, int pos, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.movePlayerInFlightBoard(nickname, pos);
        }
    }

    /**
     * Opens the space to allow the player to choose to start the motor in the game.
     *
     * @param player       The player who is initiating the action to start the motor.
     * @param fb           The flight board associated with the action.
     * @param enginesPos   The list of positions for the engines.
     * @param batteriesPos The list of positions for the batteries.
     * @param matchId      The unique identifier for the match.
     * @throws Exception If an error occurs during the operation.
     */
    @Override
    public void openSpaceChooseToStartMotor(Player player, FlightBoard fb,
                                            ArrayList<ArrayList<Integer>> enginesPos,
                                            ArrayList<ArrayList<Integer>> batteriesPos, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.openSpaceChooseToStartMotor(player, fb, enginesPos, batteriesPos);
        }
    }

    /**
     * This method handles the player's choice on how to face meteors in the game.
     * It determines the action to be taken by the player based on the provided parameters
     * and communicates with the relevant clients in the match.
     *
     * @param player      The player who is confronted with the meteor event.
     * @param defences    An ArrayList of integers representing the player's defensive options.
     * @param shots       An ArrayList of Shot objects representing the shots available for the player.
     * @param dice        An integer value representing the current dice roll.
     * @param currentShot An integer indicating the index of the current shot being processed.
     * @param matchId     The unique identifier of the match.
     * @throws Exception If an error occurs during the execution of the method.
     */
    @Override
    public void meteorCardChooseHowToFaceMeteors(Player player, ArrayList<Integer> defences,
                                                 ArrayList<Shot> shots, int dice, int currentShot, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.meteorCardChooseHowToFaceMeteors(player, defences, shots, dice, currentShot);
        }
    }

    /**
     * Activates the epidemic state base for a given match and updates the state of visited tiles.
     *
     * @param visited a set of SpaceShipTile objects representing the tiles that have been visited
     * @param matchId the unique identifier for the match in which the epidemic state is being activated
     * @throws Exception if an error occurs during the activation process
     */
    @Override
    public void epidemicStateBaseActivate(Set<SpaceShipTile> visited, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.epidemicStateBaseActivate(visited);
        }
    }

    /**
     * Triggers the stardust effect for all connected clients associated with the specified match.
     *
     * @param matchId the unique identifier of the match whose clients will receive the stardust effect
     * @throws Exception if an error occurs while performing the stardust effect on any client
     */
    @Override
    public void stardustEffect(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.stardustEffect();
        }
    }

    /**
     * Updates the remaining goods for a specified player in a given match.
     *
     * @param player the player whose goods are being updated
     * @param goods the list of goods to update for the player
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateGoodsRemaining(Player player, ArrayList<Goods> goods, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateGoodsRemaining(player, goods);
        }
    }

    /**
     * Updates the remaining number of batteries for the specified player in the given match.
     *
     * @param player      the player whose batteries count is to be updated
     * @param batteriesRem the number of remaining batteries to be set
     * @param matchId     the unique identifier of the match
     * @throws Exception  if an error occurs during the update process
     */
    @Override
    public void updateBatteriesRemaining(Player player, int batteriesRem, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateBatteriesRemaining(player, batteriesRem);
        }
    }

    /**
     * Notifies the specified player that it is not their turn to play in a given match.
     *
     * @param player the player to be notified
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void notYourTurn(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.notYourTurn();
        }
    }

    /**
     * Notifies the client associated with the given player that the timer has not started.
     *
     * @param player the player whose associated client will be notified
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs while notifying the client
     */
    @Override
    public void timerNotStarted(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.timerNotStarted();
        }
    }

    /**
     * Notifies a player that a tile was not flipped during the game match.
     *
     * @param player  The player who should receive the notification.
     * @param matchId The identifier of the game match.
     * @throws Exception If an error occurs during the notification process.
     */
    @Override
    public void tileNotFlipped(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.tileNotFlipped();
        }
    }

    /**
     * Executes the noSurrender action for a given player within a specified match.
     * This method is intended to notify all virtual clients associated with the match
     * that the specified player chooses not to surrender.
     *
     * @param player the player who performs the noSurrender action
     * @param matchId the unique identifier of the match in which the noSurrender action occurs
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void noSurrender(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.noSurrender();
        }
    }

    /**
     * Notifies all clients associated with the specified match to proceed to the next player's turn.
     *
     * @param matchId the unique identifier of the match for which the next player's turn is triggered
     * @throws Exception if an error occurs while notifying the clients
     */
    @Override
    public void nextPlayerTurn(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.nextPlayerTurn();
        }
    }

    /**
     * Notifies the client associated with the specified player about an incorrect input
     * during a match identified by the given match ID.
     *
     * @param player the player who made the incorrect input.
     * @param matchId the unique identifier for the match in which the incorrect input occurred.
     * @throws Exception if an error occurs while notifying the client.
     */
    @Override
    public void wrongInput(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.wrongInput();
        }
    }

    /**
     * Ensures that the input provided by the player is valid and corrects it if necessary,
     * by invoking the correct input method on the corresponding VirtualViewRMI instance.
     *
     * @param player the player whose input is to be validated and corrected
     * @param matchId the identifier of the match to locate the relevant VirtualViewRMI instances
     * @throws Exception if an unexpected error occurs during the input correction process
     */
    @Override
    public void correctInput(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.correctInput();
        }
    }

    /**
     * Notifies the relevant client(s) about a "wrong player" scenario
     * for a specific match and player.
     *
     * @param player the player object containing details of the current player
     * @param matchId the unique identifier of the match in which the event occurred
     * @throws Exception if an error occurs while notifying clients
     */
    @Override
    public void wrongPlayer(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.wrongPlayer();
        }
    }

    /**
     * Notifies all virtual views associated with the specified match that an effect has been started.
     *
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs while notifying the virtual views
     */
    @Override
    public void effectStarted(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.effectStarted();
        }
    }

    /**
     * Notifies all connected clients that a player has won the match.
     *
     * @param nickname the nickname of the player who won the match
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void someoneWon(String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.someoneWon(nickname);
        }
    }

    /**
     * Sends a message to a specific player in a match to choose their subship.
     * This method iterates through all virtual views associated with the specified match
     * and finds the one corresponding to the given player's username, then invokes the
     * message to prompt them to choose a subship.
     *
     * @param player the player who needs to choose a subship
     * @param subShips a nested list of SpaceShipTile objects representing the available subships
     * @param matchId the unique identifier of the match
     * @throws Exception if there is an issue during the process of sending the message
     */
    @Override
    public void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.messageToChooseSubship(player, subShips);
        }
    }

    /**
     * Allows the selection of a subship based on the provided choice, notifying all associated clients.
     *
     * @param nickname the nickname of the user selecting the subship
     * @param subShips the list of subships available for selection
     * @param choice the index representing the chosen subship
     * @param waste an additional parameter representing a value associated with the selection
     * @param matchId the unique identifier of the match where the selection is taking place
     * @throws Exception if any error occurs during the subship selection process
     */
    @Override
    public void ChooseSubship(String nickname, ArrayList<ArrayList<SpaceShipTile>> subShips,
                              int choice, int waste, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.ChooseSubship(nickname, subShips, choice, waste);
        }
    }

    /**
     * Removes a single tile from a specified position and notifies all clients in a match.
     *
     * @param nickname the nickname of the player initiating the tile removal
     * @param row the row of the tile to be removed
     * @param column the column of the tile to be removed
     * @param fromMistake a flag indicating if the removal is due to a mistake
     * @param waste the waste points associated with the removal, if any
     * @param matchId the unique identifier of the match where the tile is being removed
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void removeSingleTile(String nickname, int row, int column, boolean fromMistake, int waste, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.removeSingleTile(nickname, row, column, fromMistake, waste);
        }
    }

    /**
     * Adds a tile specified by its index to the waitlist for a given match.
     *
     * @param nickname  the nickname of the player adding the tile to the waitlist
     * @param tileIndex the index of the tile to be added to the waitlist
     * @param matchId   the unique identifier of the match
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void addTileToWaitList(String nickname, int tileIndex, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.addTileToWaitList(nickname, tileIndex);
        }
    }

    /**
     * Notifies all clients of a specific match that a player has tied in the game.
     *
     * @param nickname the nickname of the player who has tied
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs while notifying the clients
     */
    @Override
    public void tie(String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.tie(nickname);
        }
    }

    /**
     * Notifies all clients associated with the given match that a player has lost.
     *
     * @param nickname the nickname of the player who has lost the match
     * @param matchId the unique identifier of the match in which the player has lost
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void lost(String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.lost(nickname);
        }
    }

    /**
     * Handles the refusal of a reward by a player in a specific match.
     *
     * @param nickname The nickname of the player who refused the reward.
     * @param matchId The unique identifier of the match associated with the refusal.
     * @throws Exception If an error occurs during the notification process.
     */
    @Override
    public void refusedReward(String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.refusedReward(nickname);
        }
    }

    /**
     * Notifies all virtual views associated with the given match that an effect has ended.
     *
     * @param matchId the unique identifier of the match whose views need to be notified
     * @throws Exception if there is an error during notification
     */
    @Override
    public void effectEnded(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.effectEnded();
        }
    }

    /**
     * Notifies all virtual views associated with the given match that the specified player has lost goods.
     *
     * @param player the player who lost goods
     * @param matchId the UUID of the match
     * @throws Exception if an error occurs during notification
     */
    @Override
    public void lostGoods(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.lostGoods();
        }
    }

    /**
     * Triggers the "youPayConsequences" action for a specific player in a given match.
     * Iterates through all virtual views associated with the specified match ID
     * and invokes the action for the player matching the provided username.
     *
     * @param player  the player who will face the consequences; it specifies the target user.
     * @param matchId the unique identifier of the match in which the action is executed.
     * @throws Exception if an error occurs during the execution of the actions on the virtual views.
     */
    @Override
    public void youPayConsequences(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.youPayConsequences();
        }
    }

    /**
     * Updates the dice value for all connected clients associated with a specific match.
     *
     * @param dice    the new dice value to be updated
     * @param matchId the unique identifier for the match whose clients need the dice update
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateDice(int dice, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateDice(dice);
        }
    }

    /**
     * Notifies the client that the player cannot perform a deposit action.
     *
     * @param player the player attempting the deposit action
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void cannotDeposit(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.cannotDeposit();
        }
    }

    /**
     * Notifies the clients that the specified player cannot insert into the system.
     *
     * @param player the player who cannot insert
     * @param matchId the unique identifier of the match to which the player belongs
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void cannotInsert(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.cannotInsert();
        }
    }

    /**
     * Notifies the appropriate virtual views that the specified player cannot make a pick.
     *
     * @param player the player who cannot pick
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void cannotPick(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.cannotPick();
        }
    }

    /**
     * Notifies the virtual view associated with the specified player and match that they cannot fill.
     *
     * @param player the player who cannot fill
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void cannotFill(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.cannotFill();
        }
    }

    /**
     * Notifies all clients associated with the specified match ID about the incorrect tile positions.
     *
     * @param pos      The list of integer positions of the incorrect tiles.
     * @param player   The player object associated with the current action.
     * @param matchId  The unique identifier for the current match.
     * @throws Exception If an error occurs during the notification process.
     */
    @Override
    public void wrongTiles(ArrayList<Integer> pos, Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.wrongTiles(pos, player.getUsername(), v.getName());
        }
    }

    /**
     * Notifies all clients associated with a given match to insert a wait tile
     * in the left-middle-right position for a specific player.
     *
     * @param nickname the nickname of the player for whom the wait tile is being inserted
     * @param index the index indicating the position for the wait tile
     * @param matchId the unique identifier of the match associated with the operation
     * @throws Exception if an error occurs during communication with clients
     */
    @Override
    public void insertWaitTileLMR(String nickname, int index, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.insertWaitTileLMR(nickname, index);
        }
    }

    /**
     * Ensures that the specified player fills any empty tiles in the game.
     *
     * @param player the current player who may need to fill empty tiles
     * @param matchId the unique identifier of the match associated with the player
     * @throws Exception if an error occurs during the process of filling empty tiles
     */
    @Override
    public void haveToFillEmptyTiles(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.haveToFillEmptyTiles();
        }
    }

    /**
     * Removes a player from the flightboard in a specified match.
     *
     * @param nickname the nickname of the player to be removed
     * @param matchId the unique identifier of the match from which the player is to be removed
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removePlayerFromFlightboard(String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.removePlayerFromFlightboard(nickname);
        }
    }

    /**
     * Facilitates the action where slavers decide whether to claim a reward or not in a given match.
     *
     * @param player The player associated with the slavers making the decision.
     * @param yOn A boolean indicating the choice to claim the reward.
     * @param matchId The unique identifier for the match in which the action is performed.
     * @throws Exception If an error occurs during the execution of the action.
     */
    @Override
    public void slaversChooseToClaimReward(Player player, boolean yOn, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.slaversChooseToClaimReward(player, yOn);
        }
    }

    /**
     * Notifies all virtual views associated with the given match ID that the specified player is blocked.
     *
     * @param player  The player who has been blocked.
     * @param matchId The unique identifier of the match related to the blocked player.
     * @throws Exception If an error occurs while notifying the clients.
     */
    @Override
    public void blocked(Player player, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) v.blocked();
        }
    }

    /**
     * Updates the state of the match to manage a shot received by a player.
     *
     * @param player the player who received the shot
     * @param shot the shot object containing details about the shot
     * @param howToDefenceFromShots a list of integers representing the defense strategy
     * @param dice the dice value rolled to determine the defense outcome
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateManageShotReceived(Player player, Shot shot,
                                         ArrayList<Integer> howToDefenceFromShots,
                                         Integer dice, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.updateManageShotReceived(player, shot, howToDefenceFromShots, dice);
        }
    }

    /**
     * Stops the building process for all clients associated with the specified match.
     *
     * @param matchId the unique identifier of the match whose building process should be stopped
     * @throws Exception if an error occurs while stopping the building process
     */
    @Override
    public void stopBuilding(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.stopBuilding();
        }
    }

    /**
     * Notifies all clients associated with the given match ID that the timer has started.
     *
     * @param matchId the unique identifier for the match
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void timerStarted(UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.timerStarted();
        }
    }

    /**
     * Removes a block from the gameplay by notifying all relevant clients associated with the given matchId.
     *
     * @param block    the list of SpaceShipTile objects representing the block to be removed
     * @param nickname the nickname of the player associated with the block
     * @param matchId  the unique identifier for the match
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeBlock(ArrayList<SpaceShipTile> block, String nickname, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.removeBlock(block, nickname);
        }
    }

    /**
     * Notifies the client that the timer has already started for the given player.
     *
     * @param matchId the unique identifier of the match
     * @param player the player for whom the timer notification is being sent
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void timerAlreadyStarted(UUID matchId, Player player) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            if (player.getUsername().equals(v.getName())) {
                v.timerAlreadyStarted();
                break;
            }
        }
    }

    /**
     * Notifies the virtual views associated with the given match ID that the timer has ended.
     *
     * @param isLastActivation a Boolean indicating whether this is the last activation of the timer.
     * @param matchId the unique identifier of the match for which the timer has ended.
     * @throws Exception if an error occurs during the notification process.
     */
    @Override
    public void timerEnded(Boolean isLastActivation, UUID matchId) throws Exception {

        for (VirtualViewRMI v : clients(matchId)) {
            v.timerEnded(isLastActivation);
        }
    }

    /**
     * Allows a player to join a game session identified by its unique game ID.
     * Handles reassignment when the player is moving from an old game session to a new one.
     * The game controllers and event bus are updated accordingly.
     *
     * @param n the name of the player attempting to join the game.
     * @param num the unique number identifying the player.
     * @param id the unique UUID of the game session to join.
     * @param old the unique UUID of the game session the player is leaving.
     * @throws RemoteException if an issue occurs during remote communication.
     */
    public void JoinGame(String n, int num, UUID id, UUID old) throws RemoteException {
        requestQueue.add(() -> {
            try {
                if (!id.equals(old)) {
                    List<VirtualViewRMI> lobbyViews = clientsByMatch.remove(old);
                    clientsByMatch
                            .computeIfAbsent(id, _ -> Collections.synchronizedList(new ArrayList<>()))
                            .addAll(lobbyViews);
                }
                Game g = games.get(id);
                EventBus bus = g.getEventBus();
                bus.register(this);
                controllers.get(id).JoinGame(n, num);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Activates the timer for the player associated with the provided nickname in the game
     * associated with the given UUID. If an error occurs during processing, an error handler
     * is invoked for the specified game.
     *
     * @param p  the nickname of the player whose timer needs to be activated
     * @param id the UUID associated with the game in which the timer activation is performed
     * @throws RemoteException if a remote communication error occurs
     */
    public void activateTimer(String p, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.activateTimer(c.getGame().getPlayerFromNickname(p));
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Sets the number tile for a player at the specified position.
     *
     * @param pid the player ID for which the number tile is being set
     * @param pos the position where the number tile is to be set
     * @param id the unique identifier of the controller to handle the request
     * @throws RemoteException if there is an issue with remote communication
     */
    public void setNumTile(int pid, int pos, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).setNumTile(pid, pos);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Handles the surrender process for a given player identified by their ID.
     * The action is queued and executes surrender logic for the specified player and controller.
     * If an error occurs during execution, it triggers error handling for the specified controller.
     *
     * @param pid The player's identifier who is surrendering.
     * @param id  The unique identifier of the controller associated with the player.
     * @throws RemoteException If a remote invocation error occurs during the process.
     */
    public void Surrend(int pid, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).Surrend(pid);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Visualizes the final scores of a specific entity identified by the given UUID.
     * The method enqueues the visualization task and handles potential errors.
     *
     * @param id the UUID identifying the specific entity for which the final scores should be visualized
     * @throws RemoteException if there is a communication-related exception
     */
    public void visualizeFinalScores(UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).visualizeFinalScores();
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Handles the selection of a tile that has already been flipped during the game.
     * This method adds the action to a request queue and processes it asynchronously.
     * It communicates with the appropriate game controller to manage the action.
     * If an exception occurs during the execution, an error handling mechanism is invoked.
     *
     * @param i    The index of the tile being selected.
     * @param pid  The player ID who is making the selection.
     * @param id   The unique identifier for the game session or controller.
     * @throws RemoteException If there is a remote communication error during the execution.
     */
    public void pickTileAlreadyFlipped(int i, int pid, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).pickTileAlreadyFlipped(i, pid);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Queues a request to pick an unknown tile for the specified player and processes the request
     * through the associated controller by player ID and UUID. If an error occurs, it triggers the
     * error handling mechanism.
     *
     * @param pid the ID of the player for whom the tile is to be picked
     * @param id the unique identifier of the controller managing this player's tile picking logic
     * @throws RemoteException if a remote communication error occurs during the operation
     */
    public void pickTileUnknown(int pid, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).pickTileUnknown(pid);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Handles the operation of picking a smaller deck based on the given parameters.
     * This method is executed asynchronously and utilizes a request queue to manage execution.
     *
     * @param i      The index of the deck to pick.
     * @param pid    The player ID associated with the operation.
     * @param id     The unique identifier for the controller.
     * @throws RemoteException If a remote method call fails during processing.
     */
    public void pickLittleDeck(int i, int pid, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).pickLittleDeck(i, pid);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Deposits a little deck for the given player ID via the identified controller.
     * If an exception occurs while processing, an error handler is invoked.
     *
     * @param pid the identifier of the player for whom the little deck is being deposited
     * @param id the unique identifier associated with the controller responsible for handling the action
     * @throws RemoteException if a remote communication error occurs
     */
    public void depositLittleDeck(int pid, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).depositLittleDeck(pid);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Deposits a tile for a player identified by a unique player ID.
     *
     * @param pid The player ID corresponding to the player attempting to deposit the tile.
     * @param id The unique identifier representing the specific controller managing the action.
     * @throws RemoteException If a remote invocation error occurs during the operation.
     */
    public void depositTile(int pid, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).depositTile(pid);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Ends the building process for a specific entity identified by the provided parameters.
     * Executes the endbuilding operation in a queued fashion, ensuring thread safety.
     * Handles potential exceptions during the operation by invoking an error-handling mechanism.
     *
     * @param pid the unique identifier for the process that needs to end the building
     * @param wt the weight or a related parameter affecting the building process
     * @param id the UUID of the controller corresponding to the building operation
     * @throws RemoteException if a remote communication error occurs during the operation
     */
    public void endbuilding(int pid, int wt, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).endbuilding(pid, wt);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Inserts a tile into the game grid at the specified location with the provided attributes.
     * The operation is queued and executed, allowing interaction with a remote game controller.
     *
     * @param r    The row index where the tile should be inserted.
     * @param c    The column index where the tile should be inserted.
     * @param pid  The player ID associated with the tile.
     * @param rot  The rotation value for the tile.
     * @param id   The unique identifier of the client making the request.
     * @throws RemoteException If the remote operation fails during the process.
     */
    public void insertTile(int r, int c, int pid, int rot, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).insertTile(r, c, pid, rot);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Activates the effect associated with the given unique identifier (UUID).
     * The method will add the activation logic to a request queue and attempt
     * to invoke the effect activation on the controller corresponding to the provided ID.
     * If an error occurs during the process, an error-handling procedure is executed for the ID.
     *
     * @param id the unique identifier (UUID) associated with the effect to be activated
     * @throws RemoteException if a remote invocation issue occurs
     */
    public void EffectActivation(UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).EffectActivation();
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Retrieves the card in use for the given ID. This method submits a task to a request queue
     * which will invoke the corresponding controller's getCardinuse method. If an exception occurs,
     * the onError method is called for error handling.
     *
     * @param id the unique identifier of the entity for which the card in use is to be retrieved
     * @throws RemoteException if a communication-related exception occurs during the process
     */
    public void getCardinuse(UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).getCardinuse();
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Handles the process of choosing one specific sub-ship for a given player ID and sub-ship index.
     * This method adds a task to the request queue to perform the operation.
     *
     * @param i the index of the sub-ship to be chosen
     * @param pid the player ID for whom the sub-ship is to be chosen
     * @param id the unique identifier associated with the controller handling the request
     * @throws RemoteException if a remote invocation error occurs
     */
    public void chooseOneSubShip(int i, int pid, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).chooseOneSubShip(i, pid);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Handles a request to allow or deny a player to land on a planet within a game.
     * This method adds a task to a queue to process the request asynchronously.
     *
     * @param p  the nickname of the player attempting to land on the planet
     * @param y  a boolean indicating whether the landing is accepted (true) or denied (false)
     * @param np the identifier of the planet where the landing attempt is being made
     * @param id the unique identifier of the game session
     * @throws RemoteException if a remote invocation error occurs during the process
     */
    public void acceptToLandOnAPlanet(String p, boolean y, int np, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.acceptToLandOnAPlanet(c.getGame().getPlayerFromNickname(p), y, np);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Handles the action to choose an abandoned station in the game. It triggers the associated
     * game logic for the specified player and updates the necessary game state.
     *
     * @param p The nickname of the player choosing the abandoned station.
     * @param y A flag indicating whether a specific condition related to the abandoned station is true.
     * @param st A nested list of integers representing the current state or configuration related to stations.
     * @param ng A nested list of Goods objects representing the new goods associated with the stations.
     * @param id The unique identifier (UUID) of the game session or player making this request.
     * @throws RemoteException If there is a remote method invocation issue when processing the operation.
     */
    public void chooseAbandonedStation(String p, boolean y, ArrayList<ArrayList<Integer>> st, ArrayList<ArrayList<Goods>> ng, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseAbandonedStation(c.getGame().getPlayerFromNickname(p), c.getGame().getGameFlightBoard(), y, st, ng);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Allows a player to choose the positions for cannon batteries on the game board.
     * This method adds a task to the request queue for processing the specified positions.
     *
     * @param p The nickname of the player making the selection.
     * @param cp The positions to be used as cannon placements, represented as a list of coordinate pairs.
     * @param bp The positions to be used as battery placements, represented as a list of coordinate pairs.
     * @param id The unique identifier (UUID) associated with the requesting player.
     * @throws RemoteException If there is a communication-related exception during method execution.
     */
    public void chooseCannonBatteryPos(String p, ArrayList<ArrayList<Integer>> cp, ArrayList<ArrayList<Integer>> bp, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseCannonBatteryPos(c.getGame().getGameFlightBoard(), c.getGame().getPlayerFromNickname(p), cp, bp);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Processes the choice of how to face meteors for a specific player during the game.
     * This method queues the action to be performed by the game controller and
     * handles any errors that may occur during the operation.
     *
     * @param p     the nickname of the player making the choice
     * @param d     the list of directions (as integers) chosen by the player to face the meteors
     * @param id    the unique identifier (UUID) of the client or game session
     * @throws RemoteException if a communication-related error occurs during execution
     */
    public void chooseHowToFaceMeteors(String p, ArrayList<Integer> d, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseHowToFaceMeteors(c.getGame().getPlayerFromNickname(p), d, c.getGame().getGameFlightBoard());
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Handles the selection of passengers to lose during the game and performs the necessary game updates.
     * This method adds the operation to a request queue to ensure thread-safe execution.
     *
     * @param p The nickname of the player making the selection.
     * @param y A boolean flag to represent the player's choice in the context of passenger loss.
     * @param pa A list of lists representing the passengers to lose, where each inner list contains IDs or relevant data of passengers.
     * @param id The unique identifier of the game session.
     * @throws RemoteException If a remote exception occurs during the process.
     */
    public void choosePassengersToLose(String p, boolean y, ArrayList<ArrayList<Integer>> pa, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.choosePassengersToLose(c.getGame().getPlayerFromNickname(p), y, pa, c.getGame().getGameFlightBoard());
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Allows the user to choose whether to claim a reward in the game. The decision
     * is processed asynchronously through a request queue.
     *
     * @param y   a boolean indicating the choice to claim the reward (true to claim, false otherwise)
     * @param p   the player's nickname who is making the decision
     * @param id  the unique identifier (UUID) of the game session
     * @throws RemoteException if there is an error during the remote method invocation
     */
    public void chooseToClaimReward(boolean y, String p, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseToClaimReward(y, c.getGame().getPlayerFromNickname(p));
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Processes the player's choice to claim a reward in the game. This method adds the task
     * to a request queue and interacts with the game controller to execute the action. If
     * an error occurs during processing, an error callback is triggered.
     *
     * @param y                  A boolean indicating whether the reward should be claimed.
     * @param p                  The nickname of the player attempting to claim the reward.
     * @param st                 A nested list of integers representing the state information
     *                           relevant to the reward claim process.
     * @param ng                 A nested list of objects of type Goods representing the goods
     *                           involved in the reward claim decision.
     * @param id                 A UUID identifying the specific game session or client making
     *                           the request.
     * @throws RemoteException   Thrown if there is a failure in remote method invocation during
     *                           the handling of this task.
     */
    public void chooseToClaimReward(boolean y, String p, ArrayList<ArrayList<Integer>> st, ArrayList<ArrayList<Goods>> ng, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseToClaimReward(c.getGame().getGameFlightBoard(), y, c.getGame().getPlayerFromNickname(p), st, ng);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Initiates a request to place batteries for a player in the game.
     * The method queues this action for execution and handles potential errors during the process.
     *
     * @param p   The nickname of the player who is placing the batteries.
     * @param pos A 2D list of integers representing the positions where the batteries will be placed.
     * @param id  The unique identifier for the session or client making the request.
     * @throws RemoteException If there is an issue with the remote communication during the execution of the request.
     */
    public void chooseToPlaceBatteries(String p, ArrayList<ArrayList<Integer>> pos, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseToPlaceBatteries(c.getGame().getPlayerFromNickname(p), pos);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Initiates the process to start firepower within the game by delegating the action
     * to the appropriate game controller for the specified player.
     *
     * @param p The nickname of the player who is attempting to start the firepower process.
     * @param tr The list of targets, represented as an ArrayList of ArrayLists of integers.
     * @param ba The base coordinates or related parameters, represented as an ArrayList of ArrayLists of integers.
     * @param id The unique identifier (UUID) of the game session or controller responsible for managing the process.
     * @throws RemoteException If a remote communication error occurs during the operation.
     */
    public void chooseToStartFirePower(String p, ArrayList<ArrayList<Integer>> tr, ArrayList<ArrayList<Integer>> ba, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseToStartFirePower(c.getGame().getPlayerFromNickname(p), tr, ba);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Allows a player to choose to start the motor and execute corresponding actions.
     * This method adds the selection request to the request queue for asynchronous processing.
     *
     * @param p The nickname of the player making the choice.
     * @param ep A list of lists representing the engine parameters.
     * @param bp A list of lists representing the board parameters.
     * @param id The unique identifier of the game controller instance handling the request.
     * @throws RemoteException If a network-related exception occurs during the process.
     */
    public void chooseToStartMotor(String p, ArrayList<ArrayList<Integer>> ep, ArrayList<ArrayList<Integer>> bp, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseToStartMotor(c.getGame().getPlayerFromNickname(p), c.getGame().getGameFlightBoard(), ep, bp);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Handles the logic to allow a player to select where to place goods in the game.
     *
     * @param p  the nickname of the player making the selection
     * @param pg a list of lists containing integer values representing placement groups
     * @param gs a list of lists of Goods objects representing the goods to be placed
     * @param id a unique identifier for the game session
     * @throws RemoteException if there is an issue with remote method invocation
     */
    public void chooseWhereToPutGoods(String p, ArrayList<ArrayList<Integer>> pg, ArrayList<ArrayList<Goods>> gs, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                GameController c = controllers.get(id);
                c.chooseWhereToPutGoods(c.getGame().getPlayerFromNickname(p), pg, gs);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Adds a wait tile associated with the provided process ID and unique identifier.
     * This method queues the operation to be executed and handles exceptions if they occur.
     *
     * @param pid the process ID associated with the wait tile
     * @param id the unique identifier of the controller managing the wait tile
     * @throws RemoteException if an error occurs during remote execution
     */
    public void addWaitTile(int pid, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).addWaitTile(pid);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Inserts a wait tile into the game board at the specified location with the given parameters.
     *
     * @param pid the identifier of the player performing the action
     * @param idx the index of the tile to be inserted
     * @param r the row where the tile should be inserted
     * @param c the column where the tile should be inserted
     * @param rot the rotation of the tile to be inserted
     * @param id the unique identifier associated with the controller
     * @throws RemoteException if a communication error occurs while trying to perform this operation
     */
    public void insertWaitTile(int pid, int idx, int r, int c, int rot, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).insertWaitTile(pid, idx, r, c, rot);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Executes a fill tile command for a specific player on the server.
     * Places the command in a request queue to be processed.
     * Handles potential errors during execution and triggers error handling if necessary.
     *
     * @param pid   The player ID executing the command.
     * @param wa    A boolean flag indicating a specific behavior or condition related to the command.
     * @param col   The color to fill the tile with, represented as an AlienColor object.
     * @param r     The row of the tile to be filled.
     * @param c     The column of the tile to be filled.
     * @param id    The UUID associated with the operation or session.
     * @throws RemoteException  If a communication-related exception occurs during execution.
     */
    public void CommandFillTile(int pid, boolean wa, AlienColor col, int r, int c, UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).CommandFillTile(pid, wa, col, r, c);
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Notifies all clients in the lobby identified by the provided matchId about the currently active games.
     * An active game is defined as a game where the number of joined players is less than the maximum number of players allowed.
     * The list of active games is sent to the clients in the form of a list of OldGame objects.
     *
     * @param matchId the unique identifier of the lobby for which the active games are to be sent
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void activeGames(UUID matchId) throws RemoteException {
        // 1) Costruisco la lista di OldGame
        ArrayList<OldGame> openGames = new ArrayList<>();
        int index = 0;
        for (Map.Entry<UUID, Game> entry : games.entrySet()) {
            Game game = entry.getValue();
            int joined = game.getPlayers().size();
            int max = game.getNumOfPlayers();
            UUID uuid = game.getMatchId();

            if (joined < max) {
                openGames.add(new OldGame(index, joined, max, uuid));
                index++;
            }
        }
        // 2) Invio la lista a ciascun client nella lobby identificata da matchId
        for (VirtualViewRMI v : clients(matchId)) {
            try {
                v.showGames(openGames);
            } catch (Exception e) {
                // se un client è caduto, lo ignoro o posso rimuoverlo da clientsByMatch
                System.err.println("Errore notify client showGames: " + e.getMessage());
            }
        }
    }

    /**
     * Demonstrates the game for a specific user identified by the provided UUID.
     * This method executes the demo functionality, manages exceptions,
     * and invokes error handling if needed.
     *
     * @param id The unique identifier (UUID) of the user for whom the game demo is to be executed.
     * @throws RemoteException If a communication-related exception occurs.
     */
    @Override
    public void demoGame(UUID id) throws RemoteException {
        requestQueue.add(() -> {

            try {
                controllers.get(id).demoGame();
            } catch (Exception ignored) {
                try {
                    onError(id);
                } catch (Exception ignored2) {
                }
            }
        });
    }

    /**
     * Sends a ping signal to confirm connectivity or check if the remote object is responsive.
     *
     * @throws RemoteException if a communication-related error occurs during the remote method call.
     */
    @Override
    public void ping(UUID uuid, String name) throws RemoteException {

            try {
                long nowPing = System.currentTimeMillis();
                for (VirtualViewRMI v : clients(uuid)) {
                    if(v.getName().equals(name)) {
                        lastPongTime.put(v, nowPing);
                    }
                }
            } catch (Exception ignored) {
                try {
                    onError(uuid);
                } catch (Exception ignored2) {
                }
            }
    }

}
