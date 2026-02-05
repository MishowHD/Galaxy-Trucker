package it.polimi.ingsw.Network.Server.Socket;

import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.GameControl.GameController;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.Server.GenericServer;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.Dice.RandomDiceRoller;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Utils.stateEnum;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SocketServer extends GenericServer {
    /**
     * The server socket instance used to listen for incoming client connections.
     * This socket remains bound to a specific port and is utilized for accepting
     * connections from clients in a server application. It cannot be modified
     * as it is declared final.
     */
    private final ServerSocket listenSocket;
    /**
     * A thread-safe map that associates a match identifier with a list of socket client handlers.
     * Each UUID key represents the unique identifier of a match, while the corresponding value is
     * a list of {@link SocketClientHandler} instances managing the clients connected to that match.
     *
     * The use of {@link ConcurrentHashMap} ensures safe concurrent access to the map, allowing
     * multiple threads to add, remove, or modify the entries without explicit synchronization.
     */
    private final Map<UUID, List<SocketClientHandler>> clientsByMatch = new ConcurrentHashMap<>();
    /**
     * A ScheduledExecutorService instance used to manage and schedule heartbeat tasks
     * at fixed intervals in a single-threaded context. Ensures periodic execution of tasks
     * related to maintaining active connections, system health checks, or other repetitive operations.
     * The use of a single-thread executor guarantees task execution order and avoids concurrent
     * task processing issues.
     */
    private final ScheduledExecutorService heartbeat = Executors.newSingleThreadScheduledExecutor();

    /**
     * Constructs a SocketServer object with the specified ServerSocket.
     * Initializes the server with the provided listening socket and
     * starts the heartbeat mechanism to handle server communication.
     *
     * @param listenSocket the server socket used to listen for incoming connections
     * @throws RemoteException if an error occurs during remote communication
     */
    public SocketServer(ServerSocket listenSocket) throws RemoteException {
        super();
        this.listenSocket = listenSocket;
        startHeartbeat();
    }

    /**
     * Starts the heartbeat mechanism for monitoring the health of client connections
     * and managing game sessions. The heartbeat runs on a scheduled task, periodically
     * pinging clients and verifying their responsiveness.
     *
     * The method performs the following actions:
     * 1. Iterates through all registered game matches and their associated client connections.
     * 2. Pings each client to check if they are still active.
     * 3. Removes inactive clients whose last response time exceeds a predefined timeout.
     * 4. Ends the game session if a match loses all its clients or becomes unresponsive.
     *
     * Timeout for client responsiveness is defined as 15 seconds.
     * Heartbeat task is scheduled with an initial delay of 0 seconds and repeats every 5 seconds.
     *
     * Any exceptions occurring during the ping or while ending a game are caught and ignored
     * to ensure the heartbeat process continues uninterrupted.
     */
    private void startHeartbeat() {
        heartbeat.scheduleWithFixedDelay(() -> {
                    final long TIMEOUT = 15_000;     // 15s
                    long now = System.currentTimeMillis();
                    for (UUID id : clientsByMatch.keySet()) {
                        List<SocketClientHandler> list = clientsByMatch.get(id);
                        if (list == null) continue;
                        Game g = games.get(id);
                        boolean isMatch = (g != null);
                        AtomicBoolean ended = new AtomicBoolean(false);
                        list.removeIf(handler -> {
                            try {
                                handler.ping();
                                if (now - handler.getLastPongTime() > TIMEOUT) {
                                    if (isMatch && ended.compareAndSet(false, true)) {
                                        try {
                                            g.endGame();
                                        } catch (Exception ignored) {
                                        }
                                    }
                                    return true;  // rimuovi il client
                                }
                                return false;     // client ancora vivo
                            } catch (IOException ex) {
                                if (isMatch && ended.compareAndSet(false, true)) {
                                    try {
                                        g.endGame();
                                    } catch (Exception ignored) {
                                    }
                                }
                                return true;      // rimuovi il client
                            }
                        });
                    }

                },
                0,
                5,
                TimeUnit.SECONDS);
    }


    /**
     * Retrieves or creates a synchronized list of SocketClientHandler objects associated with the given match ID.
     *
     * @param matchId the unique identifier of the match for which to retrieve or initialize the client list
     * @return a synchronized list of SocketClientHandler objects associated with the specified match ID
     */
    private List<SocketClientHandler> clients(UUID matchId) {
        return clientsByMatch.computeIfAbsent(matchId, _ -> Collections.synchronizedList(new ArrayList<>()));
    }

    /**
     * Retrieves a collection of all connected SocketClientHandler instances.
     *
     * @return a collection containing all SocketClientHandler objects from all matches.
     */
    private Collection<SocketClientHandler> allClients() {
        return clientsByMatch.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Starts the server and listens for incoming client connections.
     * For each new connection, it initializes the input and output streams, assigns a unique identifier
     * to the client, and manages client handling in a separate thread.
     *
     * @param args an array of strings containing any command-line arguments passed to the program
     * @throws IOException if an I/O error occurs while accepting connections or interacting with streams
     */
    public void start(String[] args) throws IOException {
        try {
            while (!listenSocket.isClosed()) {
                Socket sock = listenSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(sock.getInputStream());

                UUID provisional = UUID.randomUUID();
                SocketClientHandler handler = new SocketClientHandler(provisional, this, in, out);
                clients(provisional).add(handler);
                new Thread(() -> {
                    try {
                        handler.runVirtualView();
                    } catch (Exception _) {
                    }
                }).start();
            }
        } finally {
            stop();
        }
    }

    /**
     * Stops the server and releases all associated resources.
     *
     * This method performs the following actions:
     * 1. Notifies all connected clients of the server shutdown.
     * 2. Shuts down the heartbeat mechanism for managing client connections.
     * 3. Clears the client-to-match mapping data structure.
     * 4. Closes the listening socket to terminate incoming connection handling.
     *
     * Any exceptions during client notifications or socket closure are caught
     * and ignored to ensure the method completes its primary objective.
     */
    @Override
    public void stop() {
        allClients().forEach(h -> {
            try {
                h.notifyShutdown("Server disconnected");
            } catch (Exception ignored) {
            }
        });
        heartbeat.shutdownNow();
        clientsByMatch.clear();
        try {
            listenSocket.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Creates a new game with the specified configuration and associates it with the corresponding players.
     *
     * @param level the level of the game to be created
     * @param provisionalId a temporary unique identifier for the game session
     * @param numplayer the number of players in the game
     * @param nickname the nickname of the player initiating the game
     */
    public void createGame(int level, UUID provisionalId, int numplayer, String nickname) {
        try {
            Game g = new Game(level, new RandomDiceRoller(), 10, 120);
            EventBus bus = g.getEventBus();
            GameController c = new GameController(g, bus);
            UUID matchId = g.getMatchId();
            games.put(matchId, g);
            controllers.put(matchId, c);
            List<SocketClientHandler> lobby = clientsByMatch.get(provisionalId);
            if (lobby == null) {
                onError(provisionalId);
                return;
            }
            lobby.forEach(h -> {
                try {
                    h.changeID(matchId);      // invia al client il nuovo ID
                    h.setUuid(matchId);       // lato server, aggiorna il campo uuid
                    h.setController(c);
                } catch (Exception ignore) {
                }
            });
            clientsByMatch.remove(provisionalId);
            clientsByMatch.put(matchId, lobby);
            bus.register(this);
            joinGame(nickname, numplayer, matchId, matchId);
        } catch (Exception e) {
            onError(provisionalId);
        }
    }


    /**
     * Handles the process of a player joining a game using their provisional ID.
     * It updates the provisional match's client handlers, replaces the provisional
     * match ID with the actual match ID, and initiates the game joining process.
     *
     * @param playerNickname the nickname of the player who is joining the game
     * @param numPlayers the total number of players in the game
     * @param matchId the unique identifier of the match the player is joining
     * @param provisionalId the provisional identifier of the match before it begins
     */
    public void joinGame(String playerNickname, int numPlayers, UUID matchId, UUID provisionalId) {
        try {
            // 1) Prendo la lista di handler sotto il provisionalId
            List<SocketClientHandler> lobby = clientsByMatch.get(provisionalId);
            if (lobby == null) {
                onError(provisionalId);
                return;
            }
            lobby.forEach(h -> {
                try {
                    h.setUuid(matchId);    // dovrai aggiungere questo setter in SocketClientHandler
                    h.setController(controllers.get(matchId));
                } catch (Exception ignore) {
                }
            });

            clientsByMatch.remove(provisionalId);
            clientsByMatch
                    .computeIfAbsent(matchId, _ -> Collections.synchronizedList(new ArrayList<>()))
                    .addAll(lobby);
            Game g = games.get(matchId);
            EventBus bus = g.getEventBus();
            bus.register(this);
            controllers.get(matchId).JoinGame(playerNickname, numPlayers);
        } catch (Exception e) {
            onError(matchId);
        }
    }

    /**
     * Identifies active games that have available player slots and sends the list
     * of these games to clients in the lobby associated with the given provisional ID.
     *
     * @param provisionalId the unique identifier for the lobby of clients who will
     *                      receive the list of games with open slots
     */
    public void activeGames(UUID provisionalId) {
        ArrayList<OldGame> openGames = new ArrayList<>();
        int idx = 0;
        for (Game g : games.values()) {
            int joined = g.getPlayers().size();
            int max = g.getNumOfPlayers();
            UUID id = g.getMatchId();
            if (joined < max) {
                openGames.add(new OldGame(idx++, joined, max, id));
            }
        }
        List<SocketClientHandler> lobby = clients(provisionalId);
        for (SocketClientHandler h : lobby) {
            try {
                h.showGames(openGames);
            } catch (Exception ignore) {
            }
        }
    }

    // UpdateListener callbacks, each ending with UUID matchId

    /**
     * Handles an error event associated with the specified match identifier.
     * For each client associated with the match, this method invokes the error
     * handler. Exceptions thrown during the handling of errors are silently ignored.
     *
     * @param matchId the unique identifier of the match for which the error occurred
     */
    @Override
    public void onError(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.error();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the game creation details for the specified match and informs all clients connected to the match.
     *
     * @param level the difficulty level of the game
     * @param decks the list of decks available in the game
     * @param players the list of players participating in the game
     * @param hourglass the initial hourglass value for the game
     * @param surrender the surrender limit for the game
     * @param matchId the unique identifier of the match
     */
    @Override
    public void updateGameCreated(int level, List<Deck> decks, ArrayList<Player> players, int hourglass, int surrender, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateGameCreated(level, decks, players, hourglass, surrender);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Deposits the item currently held by the specified player into the appropriate storage
     * based on the ongoing match context.
     *
     * @param nickname the nickname of the player whose held item is to be deposited
     * @param matchId the unique identifier of the match in which the action is performed
     */
    @Override
    public void depositThingInHand(String nickname, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.depositThingInHand(nickname);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Executes the pickTileLMR action for all clients associated with the provided match.
     *
     * @param index the index of the tile to be picked
     * @param nickname the nickname of the player performing the action
     * @param matchId the unique identifier for the match in which the action is taking place
     */
    @Override
    public void pickTileLMR(int index, String nickname, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.pickTileLMR(index, nickname);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Inserts a tile with the specified parameters into the game for the given match.
     *
     * @param index the index of the tile to insert
     * @param r the row position where the tile will be placed
     * @param c the column position where the tile will be placed
     * @param rot the rotation value of the tile
     * @param nickname the nickname associated with the player inserting the tile
     * @param matchId the unique identifier of the match
     */
    @Override
    public void insertTileLMR(int index, int r, int c, int rot, String nickname, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.insertTileLMR(index, r, c, rot, nickname);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Allows a client to pick a little deck in a match using the provided deck index and nickname.
     *
     * @param deckIndex the index of the deck to be picked
     * @param nickname the nickname of the player choosing the deck
     * @param matchId the unique identifier of the match in which the deck is being picked
     */
    @Override
    public void pickLittleDeckLMR(int deckIndex, String nickname, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.pickLittleDeckLMR(deckIndex, nickname);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Adds an alien or human object to the game based on the provided parameters.
     * This method is typically used in the context of multiplayer matches to update
     * all clients about the state of the game.
     *
     * @param nickname  the nickname of the player adding the alien or human
     * @param wantAlien true if an alien should be added, false for a human
     * @param color     the color of the alien being added, applicable only if wantAlien is true
     * @param row       the row position where the alien or human is to be added
     * @param col       the column position where the alien or human is to be added
     * @param matchId   the unique identifier of the match where the alien or human is being added
     */
    @Override
    public void addAlienOrHumansLMR(String nickname, boolean wantAlien, AlienColor color, int row, int col, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.addAlienOrHumansLMR(nickname, wantAlien, color, row, col);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Sends a message to the client to prompt the selection of a subship.
     * This method is used to notify a specific player associated with a given match ID,
     * instructing them to choose a subship from the provided list of options.
     *
     * @param player The player who is to choose a subship.
     * @param subShips A list of available subship options, represented as nested lists of SpaceShipTile objects.
     * @param matchId The unique identifier of the match associated with the player's action.
     * @throws Exception If an error occurs while sending the message to the client.
     */
    @Override
    public void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips, UUID matchId) throws Exception {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(player.getUsername()))
                .forEach(h -> {
                    try {
                        h.messageToChooseSubship(player, subShips);
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Allows a player to select a subship for the match.
     *
     * @param nickname the nickname of the player choosing the subship
     * @param subShips a list of available subships represented as a 2D ArrayList of SpaceShipTile objects
     * @param choice the index of the chosen subship from the list
     * @param waste an integer value representing the waste associated with the choice
     * @param matchId the unique identifier of the match
     * @throws Exception if an error occurs in the subship selection process
     */
    @Override
    public void ChooseSubship(String nickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste, UUID matchId) throws Exception {
        clients(matchId).forEach(h -> {
            try {
                h.ChooseSubship(nickname, subShips, choice, waste);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Removes a single tile from a specific location in the game board for a given player.
     * This method notifies all connected clients about the tile removal action.
     *
     * @param nickname the nickname of the player who is performing the tile removal
     * @param row the row number of the tile to be removed
     * @param column the column number of the tile to be removed
     * @param fromMistake a boolean indicating whether the removal is due to a mistake
     * @param waste the waste value associated with the removal
     * @param matchId the unique identifier of the match in which the tile is being removed
     * @throws Exception if any error occurs while notifying the clients
     */
    @Override
    public void removeSingleTile(String nickname, int row, int column, boolean fromMistake, int waste, UUID matchId) throws Exception {
        clients(matchId).forEach(h -> {
            try {
                h.removeSingleTile(nickname, row, column, fromMistake, waste);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Adds a tile to the waiting list for all clients associated with the specified match.
     *
     * @param nickname  the nickname of the player associated with the tile
     * @param tileIndex the index of the tile being added to the wait list
     * @param matchId   the unique identifier of the match
     * @throws Exception if an error occurs while adding the tile to the wait list
     */
    @Override
    public void addTileToWaitList(String nickname, int tileIndex, UUID matchId) throws Exception {
        clients(matchId).forEach(h -> {
            try {
                h.addTileToWaitList(nickname, tileIndex);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Completes the building phase for the given match identified by the match ID.
     * It triggers the `completeBuildingPhase` method for all clients associated with the specified match.
     * Any exceptions encountered during invocation are ignored.
     *
     * @param matchId the unique identifier of the match for which the building phase is to be completed
     */
    @Override
    public void completeBuildingPhase(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.completeBuildingPhase();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Sets the player's position on the flight board for a specific match.
     *
     * @param nickname the nickname of the player whose position is to be set
     * @param pos the position of the player on the flight board
     * @param matchId the unique identifier of the match
     */
    @Override
    public void setPlayerPosInFlightBoard(String nickname, int pos, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.setPlayerPosInFlightBoard(nickname, pos);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies all clients about the end of the building process associated with a specific match.
     *
     * @param nickname the nickname of the player who ended the building process
     * @param whereToGo an integer representing the destination or next step
     * @param matchId the unique identifier of the match context
     */
    @Override
    public void endbuilding(String nickname, int whereToGo, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.endbuilding(nickname, whereToGo);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Ends the building phase for all clients associated with the specified match.
     *
     * @param matchId the unique identifier of the match for which the building phase
     *                should be ended for all associated clients
     */
    @Override
    public void endBuildingPhaseForAll(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.endBuildingPhaseForAll();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Sends a penalty to all clients associated with the given match.
     *
     * @param penalty the penalty value to be sent
     * @param type the type of penalty
     * @param matchId the unique identifier of the match
     */
    @Override
    public void sendPenalty(int penalty, String type, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.sendPenalty(penalty, type);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Activates the "abandoned ship taken" event for the specified player in a multiplayer match.
     *
     * @param p the player who has taken the abandoned ship
     * @param posPers a list of positions represented as a 2D integer array associated with the event
     * @param matchId the unique identifier of the multiplayer match in which the event occurred
     */
    @Override
    public void abandonedShipTakenActivate(Player p, ArrayList<ArrayList<Integer>> posPers, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.abandonedShipTakenActivate(p, posPers);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the card usage for a given match by notifying all associated clients.
     *
     * @param card    The card whose usage is being updated.
     * @param matchId The unique identifier of the match associated with the card usage.
     */
    @Override
    public void updateCardUse(Card card, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateCardUse(card);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the card usage state for a specific list of players within a match.
     * This method filters clients associated with the provided match ID whose usernames match
     * those in the specified list of players and updates their card state accordingly.
     *
     * @param ps the list of players for whom the card usage state needs to be updated
     * @param state the new card usage state to be applied
     * @param matchId the unique identifier of the match to which the players belong
     */
    @Override
    public void updateCardUseSTATE(ArrayList<Player> ps, c_State state, UUID matchId) {
        // 1) raccogliamo i soli username in un Set per efficienza
        Set<String> targetUsernames = ps.stream()
                .map(Player::getUsername)          // da Player a String
                .collect(Collectors.toSet());

        // 2) filtriamo SOLO i client il cui username è in quel Set
        clients(matchId).stream()
                .filter(client -> targetUsernames.contains(client.getUsername()))
                .forEach(client -> {
                    try {
                        client.updateCardUseSTATE(state);
                    } catch (Exception ignore) {
                        // se proprio vuoi loggare: ignore.printStackTrace();
                    }
                });
    }



    /**
     * Updates the status of all clients associated with the provided match ID.
     *
     * @param status The new state to update to, represented as a {@code stateEnum}.
     * @param matchId The unique identifier of the match for which the status will be updated.
     */
    @Override
    public void updateStatus(stateEnum status, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateStatus(status);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the final scores for a specific match by notifying all related clients.
     *
     * @param finalScores a map containing the final scores where the key is a string identifier
     *                    for the player or team, and the value is the score as a float.
     * @param matchId the unique identifier of the match for which the final scores are to be updated.
     */
    @Override
    public void updateFinalScores(HashMap<String, Float> finalScores, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateFinalScores(finalScores);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Handles the activation of an abandoned station on the game board. This method
     * communicates with clients to perform the necessary logic for the activation.
     *
     * @param p       The player initiating the abandoned station activation.
     * @param fb      The flight board where the action is taking place.
     * @param yOn     A boolean flag indicating a specific condition for the activation.
     * @param tiles   The list of tile information associated with the station activation.
     * @param newg    The list of new goods generated as part of the activation process.
     * @param matchId The unique identifier for the match in which this action is being performed.
     */
    @Override
    public void chooseAbandonedStationActivate(Player p, FlightBoard fb, boolean yOn,
                                               ArrayList<ArrayList<Integer>> tiles,
                                               ArrayList<ArrayList<Goods>> newg,
                                               UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.chooseAbandonedStationActivate(p, fb, yOn, tiles, newg);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the battery positions of a player in the specified match.
     *
     * @param p       The player whose battery positions are to be updated.
     * @param pos     A list of lists containing the integer positions to be updated.
     * @param matchId The unique identifier of the match where the update is to be applied.
     */
    @Override
    public void updateAssertBatteriesPos(Player p, ArrayList<ArrayList<Integer>> pos, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateAssertBatteriesPos(p, pos);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates and adds goods for a player in the specified match.
     *
     * @param p       The player object for whom the goods are being updated.
     * @param posGoods A nested list of integers representing the positions of the goods.
     * @param gs       A nested list of goods objects to be added or updated.
     * @param matchId  The unique identifier of the match where the goods are being updated.
     */
    @Override
    public void updateAddGoods(Player p, ArrayList<ArrayList<Integer>> posGoods,
                               ArrayList<ArrayList<Goods>> gs, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateAddGoods(p, posGoods, gs);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the currently connected clients to execute the action of selecting passengers to lose
     * in response to specific game consequences for a player within a given match.
     *
     * @param p       The player object for whom the passengers to lose are being chosen.
     * @param c       The consequences that require the selection of passengers to lose.
     * @param pass    A nested list of integers representing the passengers to choose for losing.
     * @param matchId The unique identifier of the match where the update is applied.
     */
    @Override
    public void updateChoosePassengersToLose(Player p, Consequences c,
                                             ArrayList<ArrayList<Integer>> pass, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateChoosePassengersToLose(p, c, pass);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Activates the planet financial state for the specified players and flight board
     * in the context of a specific match.
     *
     * @param pls      the list of players for whom the planet financial state is to be activated
     * @param fb       the flight board associated with the players
     * @param matchId  the unique identifier of the match
     */
    @Override
    public void planetFinStatActivate(ArrayList<Player> pls, FlightBoard fb, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.planetFinStatActivate(pls, fb);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the consequence of lost days in the game for a particular player.
     *
     * @param p       The player whose consequence lost days to update.
     * @param fb      The flight board object representing the game state.
     * @param days    The number of days lost as a consequence.
     * @param t       A boolean flag that represents certain game logic related to the update.
     * @param matchId The unique identifier of the match to update.
     */
    @Override
    public void updateConsequenceLostDays(Player p, FlightBoard fb, int days, Boolean t, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateConsequenceLostDays(p, fb, days, t);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the loose state of all goods for a specified player in the context of a given match.
     *
     * @param p      the player whose goods state is being updated
     * @param f      a flag indicating the first condition related to the update
     * @param b      a flag indicating the second condition related to the update
     * @param a      a flag indicating the third condition related to the update
     * @param matchId the unique identifier of the match within which the update occurs
     */
    @Override
    public void updateLooseAllGoods(Player p, Boolean f, Boolean b, Boolean a, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateLooseAllGoods(p, f, b, a);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the players and clients with the details of a shot received in the context
     * of a match. This method notifies all clients associated with the specified match
     * identifier.
     *
     * @param p       The player who is managing the shot received.
     * @param s       The shot object representing the details of the shot received.
     * @param def     A list of integers representing the defense configuration or related details.
     * @param dice    The dice result associated with the shot, if applicable.
     * @param matchId The unique identifier of the match for which the shot details are being updated.
     */
    @Override
    public void updateManageShotReceived(Player p, Shot s, ArrayList<Integer> def, Integer dice, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateManageShotReceived(p, s, def, dice);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the smugglers calculation for a specific player in the context of a match.
     *
     * @param p      the player object for whom the smuggler's calculation is updated
     * @param cPos   the current positions, represented as a nested list of integers
     * @param bPos   the base positions, represented as a nested list of integers
     * @param matchId the unique identifier of the match
     */
    @Override
    public void updateSmugglersCalc(Player p, ArrayList<ArrayList<Integer>> cPos,
                                    ArrayList<ArrayList<Integer>> bPos, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateSmugglersCalc(p, cPos, bPos);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the lost batteries smug for the given player in the specified match.
     * Iterates through all the client handlers for the provided match ID and attempts to update
     * the state of lost batteries smug for each client.
     *
     * @param p the Player object for which the lost batteries smug is being updated
     * @param pos a nested list of integers representing the positions associated with the lost batteries smug
     * @param n an integer parameter that specifies additional information related to the update
     * @param matchId the UUID representing the unique identifier of the match
     */
    @Override
    public void updateLostBatteriesSmug(Player p, ArrayList<ArrayList<Integer>> pos, int n, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateLostBatteriesSmug(p, pos, n);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the lost goods during smuggling for the specified player and sends the updates
     * to all connected clients in the specified match.
     *
     * @param p       The player involved in the smuggling operation.
     * @param pos     A 2D list representing the positions related to the lost goods.
     * @param gs      A 2D list containing groups of goods affected.
     * @param gl      A list containing individual goods lost.
     * @param matchId The unique identifier of the match.
     */
    @Override
    public void updateLostGoodsSmug(Player p, ArrayList<ArrayList<Integer>> pos,
                                    ArrayList<ArrayList<Goods>> gs, ArrayList<Goods> gl, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateLostGoodsSmug(p, pos, gs, gl);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Handles the action of choosing to claim a smuggling reward sent to the relevant clients
     * in the specified match.
     *
     * @param yOn     A boolean specifying whether the player has chosen to claim the reward.
     * @param p       The player making the choice.
     * @param tiles   A 2D array list representing specific tile configurations involved in the action.
     * @param ng      A 2D array list of {@code Goods} associated with the action.
     * @param matchId The unique identifier of the match where the action occurs.
     */
    @Override
    public void chooseToClaimRewardSmug(boolean yOn, Player p,
                                        ArrayList<ArrayList<Integer>> tiles,
                                        ArrayList<ArrayList<Goods>> ng, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.chooseToClaimRewardSmug(yOn, p, tiles, ng);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Method to handle the action where slavers choose cannon battery positions
     * during a game match. This sends the relevant data to all clients associated
     * with the specified match.
     *
     * @param p      The player object representing the slaver making the choice.
     * @param cPos   A list of lists containing cannon positions for selection.
     * @param bPos   A list of lists containing battery positions for selection.
     * @param matchId The unique identifier for the ongoing match.
     */
    @Override
    public void slaversChooseCannonBatteryPos(Player p,
                                              ArrayList<ArrayList<Integer>> cPos,
                                              ArrayList<ArrayList<Integer>> bPos,
                                              UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.slaversChooseCannonBatteryPos(p, cPos, bPos);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Allows the slavers to claim their reward by notifying all related clients of the action.
     *
     * @param p     The Player who is initiating the reward claim.
     * @param yOn   A boolean indicating the choice or state related to claiming the reward.
     * @param matchId The unique identifier for the match or context in which the reward claim is happening.
     */
    @Override
    public void slaversChooseToClaimReward(Player p, boolean yOn, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.slaversChooseToClaimReward(p, yOn);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Handles the process where slavers decide which passengers to lose in the game.
     * This method notifies all clients associated with a specific match about the decision.
     *
     * @param p         The player making the decision or associated with this action.
     * @param yOn       A boolean flag indicating specific gameplay conditions or parameters.
     * @param tiles     A list of tile information, represented as a 2D ArrayList of integers,
     *                  specifying relevant game state or positions.
     * @param matchId   The unique identifier for the match/game context in which the action takes place.
     */
    @Override
    public void slaversChoosePassengersToLose(Player p, boolean yOn,
                                              ArrayList<ArrayList<Integer>> tiles,
                                              UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.slaversChoosePassengersToLose(p, yOn, tiles);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Handles the request for players to choose the cannon battery positions
     * in the game by delegating to connected clients.
     *
     * @param p The player instance making the request.
     * @param cPos A list of cannon positions represented as a nested list of integers.
     * @param bPos A list of predetermined battery positions represented as a nested list of integers.
     * @param matchId The unique identifier of the match during which the action is performed.
     */
    @Override
    public void piratesChooseCannonBatteryPos(Player p,
                                              ArrayList<ArrayList<Integer>> cPos,
                                              ArrayList<ArrayList<Integer>> bPos,
                                              UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.piratesChooseCannonBatteryPos(p, cPos, bPos);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Allows pirates (players) to decide how to face upcoming meteors during the game.
     * This method communicates the choice to all clients associated with the given match.
     *
     * @param p       The player making the decision.
     * @param def     A list of integers representing defense strategies or other parameters.
     * @param s       The shot object containing pertinent information for handling the meteors.
     * @param dice    The result of a dice roll that may influence the decision-making process.
     * @param matchId The unique identifier of the match in which the action is taking place.
     */
    @Override
    public void piratesChooseHowToFaceMeteors(Player p,
                                              ArrayList<Integer> def,
                                              Shot s,
                                              int dice,
                                              UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.piratesChooseHowToFaceMeteors(p, def, s, dice);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * This method broadcasts a decision by pirates to claim a reward to all relevant clients.
     *
     * @param yOn     a boolean indicating the pirates' choice; true if they choose to claim, false otherwise
     * @param p       the player associated with this action
     * @param matchId the unique identifier of the match in which the action is occurring
     */
    @Override
    public void piratesChooseToClaimReward(boolean yOn, Player p, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.piratesChooseToClaimReward(yOn, p);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Moves the player with the given nickname to the specified position on the flight board
     * in the context of the specified match.
     *
     * @param nick the nickname of the player to move
     * @param pos the new position of the player on the flight board
     * @param matchId the unique identifier of the match
     */
    @Override
    public void movePlayerInFlightBoard(String nick, int pos, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.movePlayerInFlightBoard(nick, pos);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Initiates the process of selecting and starting the motor in the open space
     * based on the given player, flight board, and positional information. This
     * method communicates with all clients associated with the specified match ID.
     *
     * @param p       The player who is initiating the action.
     * @param fb      The flight board associated with the current game state.
     * @param ePos    A list of lists that contains the positions of elements.
     * @param bPos    A list of lists that contains the positions of blocks.
     * @param matchId The unique identifier for the match.
     */
    @Override
    public void openSpaceChooseToStartMotor(Player p, FlightBoard fb,
                                            ArrayList<ArrayList<Integer>> ePos,
                                            ArrayList<ArrayList<Integer>> bPos,
                                            UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.openSpaceChooseToStartMotor(p, fb, ePos, bPos);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Allows the player to choose how to face meteors in a game scenario. This method is invoked on all clients
     * associated with a specific match and communicates the details to each client.
     *
     * @param p       the player who is currently deciding how to face the meteors
     * @param def     a list of integers representing the defensive options or parameters chosen by the player
     * @param shots   a list of shot objects representing missile or projectile actions affecting meteors
     * @param dice    the dice roll value influencing the outcome or options available for facing the meteors
     * @param cur     an integer representing the current state, round, or phase in the game
     * @param matchId the unique identifier of the match in which the action is taking place
     */
    @Override
    public void meteorCardChooseHowToFaceMeteors(Player p,
                                                 ArrayList<Integer> def,
                                                 ArrayList<Shot> shots,
                                                 int dice,
                                                 int cur,
                                                 UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.meteorCardChooseHowToFaceMeteors(p, def, shots, dice, cur);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Activates the epidemic state base for the given set of visited SpaceShipTiles
     * within a specific match by notifying connected clients.
     *
     * @param visited a set of SpaceShipTile objects representing the tiles that have been visited
     * @param matchId the unique identifier of the match for which the epidemic state base is activated
     */
    @Override
    public void epidemicStateBaseActivate(Set<SpaceShipTile> visited, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.epidemicStateBaseActivate(visited);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Triggers the stardust effect for all clients associated with the given match ID.
     *
     * @param matchId the unique identifier for the match whose clients should receive the stardust effect
     */
    @Override
    public void stardustEffect(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.stardustEffect();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Updates the remaining goods for a specified player in a match identified by its UUID.
     * Notifies all clients associated with the given match ID by invoking their updateGoodsRemaining method.
     * Any exceptions that occur during the update for individual clients are caught and ignored.
     *
     * @param p       The player for whom the goods remaining are being updated.
     * @param goods   The list of goods to be updated for the specified player.
     * @param matchId The unique identifier of the match associated with the goods update.
     */
    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goods, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateGoodsRemaining(p, goods);
            } catch (Exception ignore) {
            }
        });
    }


    /**
     * Updates the remaining number of batteries for a specific player in a match.
     *
     * @param p       the player for whom the remaining batteries are being updated
     * @param batt    the number of batteries remaining
     * @param matchId the unique identifier for the match
     */
    @Override
    public void updateBatteriesRemaining(Player p, int batt, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.updateBatteriesRemaining(p, batt);
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Notifies a player that it is not their turn in the current match.
     *
     * @param p       the player being notified
     * @param matchId the unique identifier of the match
     */
    @Override
    public void notYourTurn(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.notYourTurn();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Handles the scenario where a timer has not started for a specific match.
     *
     * @param p       The player for whom the timer has not started.
     * @param matchId The unique identifier of the match.
     */
    @Override
    public void timerNotStarted(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.timerNotStarted();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Notifies the clients in a match that a tile has not been flipped.
     *
     * @param p        The player associated with the unflipped tile.
     * @param matchId  The unique identifier of the match in which the action occurred.
     */
    @Override
    public void tileNotFlipped(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.tileNotFlipped();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Prevents the player from surrendering in the match corresponding to the given match ID.
     * This method identifies the client associated with the provided player
     * and invokes the noSurrender action for that client.
     *
     * @param p       the player who is not surrendering
     * @param matchId the unique identifier for the match
     */
    @Override
    public void noSurrender(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.noSurrender();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Advances the game to the next player's turn for a given match.
     *
     * @param matchId the unique identifier of the match for which the player turn should be advanced
     */
    @Override
    public void nextPlayerTurn(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.nextPlayerTurn();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies the client associated with the given player of a wrong input
     * action that occurred during the match identified by the given match ID.
     *
     * @param p the Player object whose client needs to be notified
     * @param matchId the unique identifier of the match where the action occurred
     */
    @Override
    public void wrongInput(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.wrongInput();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Corrects the input for the specified player in the given match.
     *
     * @param p       the player whose input needs to be corrected
     * @param matchId the unique identifier of the match in which the input correction is required
     */
    @Override
    public void correctInput(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.correctInput();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Notifies the specified player in a match that their move was incorrect.
     *
     * @param p       the player who made the wrong move
     * @param matchId the unique identifier of the match in which the wrong move occurred
     */
    @Override
    public void wrongPlayer(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.wrongPlayer();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Notifies all clients associated with the given match ID that an effect has started.
     * This method iterates through the handlers of the specified match, invoking their effectStarted method.
     * Any exceptions thrown by the handlers are caught and ignored.
     *
     * @param matchId the unique identifier of the match for which the effect notification should be sent
     */
    @Override
    public void effectStarted(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.effectStarted();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies all connected clients for the given match that a player has won.
     *
     * @param nick the nickname of the player who won the match
     * @param matchId the unique identifier of the match
     */
    @Override
    public void someoneWon(String nick, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.someoneWon(nick);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Executes the tie operation for the specified match by notifying all clients associated with the given match ID.
     *
     * @param nick the nickname of the user to be passed to the tie operation
     * @param matchId the unique identifier of the match where the tie operation is executed
     */
    @Override
    public void tie(String nick, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.tie(nick);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies all clients associated with the given match that the specified player has lost.
     *
     * @param nick the nickname of the player who lost
     * @param matchId the unique identifier of the match
     */
    @Override
    public void lost(String nick, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.lost(nick);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies all clients associated with the specified matchId that the specified user has refused a reward.
     *
     * @param nick the nickname of the user who refused the reward
     * @param matchId the unique identifier for the match in which the reward was refused
     */
    @Override
    public void refusedReward(String nick, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.refusedReward(nick);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Handles the termination of an effect associated with the given match ID.
     * The method iterates through all clients associated with the match ID and invokes their
     * effectEnded method.
     *
     * @param matchId the unique identifier of the match whose effect has ended and needs to be processed
     */
    @Override
    public void effectEnded(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.effectEnded();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies clients associated with the specified match that a player has lost goods.
     * This method identifies the relevant client(s) based on the username of the player
     * and triggers the lostGoods action for those clients.
     *
     * @param p       The player who has lost goods. The player's username is used
     *                to identify relevant client(s).
     * @param matchId The unique identifier for the match, used to retrieve
     *                associated clients.
     */
    @Override
    public void lostGoods(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.lostGoods();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Executes the "youPayConsequences" action for a specific player in a match.
     * The method identifies the corresponding client for the player based on their username
     * and invokes the associated consequence logic.
     * If an exception occurs during this process, it is silently ignored.
     *
     * @param p The player for whom the "youPayConsequences" action is triggered.
     * @param matchId The unique identifier of the match in which the action is performed.
     */
    @Override
    public void youPayConsequences(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.youPayConsequences();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Updates the dice value for all clients associated with a specific match.
     *
     * @param dice    The new dice value to be updated.
     * @param matchId The unique identifier of the match for which the dice value is updated.
     */
    @Override
    public void updateDice(int dice, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.updateDice(dice);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies the clients in the specified match that the given player cannot deposit.
     *
     * @param p       the player who cannot deposit
     * @param matchId the unique identifier of the match
     */
    @Override
    public void cannotDeposit(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.cannotDeposit();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Notifies all clients associated with the given match ID that the specified player
     * cannot insert. This is achieved by filtering the clients based on the username of the player
     * and invoking the 'cannotInsert' method for each matching client handler.
     *
     * @param p       The player who cannot perform the insert action.
     * @param matchId The unique identifier of the match related to the player.
     */
    @Override
    public void cannotInsert(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.cannotInsert();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Notifies all connected clients in the specified match that the given player
     * cannot make a pick.
     *
     * @param p       the player who is unable to make a pick
     * @param matchId the unique identifier of the match
     */
    @Override
    public void cannotPick(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.cannotPick();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Notifies the relevant clients associated with the specified player and match ID
     * that they cannot fill the required action.
     *
     * @param p       The player who cannot fill the action.
     * @param matchId The unique identifier of the match associated with the notification.
     */
    @Override
    public void cannotFill(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.cannotFill();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Handles the blocked action for a specific player in a match.
     * Iterates through all clients associated with the given match ID, finds the client
     * with the same username as the provided player, and invokes their blocked method.
     *
     * @param p       The player object representing the user to be blocked.
     * @param matchId The unique identifier of the match where the action occurs.
     */
    @Override
    public void blocked(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.blocked();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Stops the building process for a specific match identified by the given match ID.
     * It iterates through all clients associated with the match and invokes their stopBuilding method.
     * Exceptions during this process are caught and ignored.
     *
     * @param matchId the unique identifier for the match for which the building process should be stopped
     */
    @Override
    public void stopBuilding(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.stopBuilding();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies all clients associated with a specific match that the timer has started.
     *
     * @param matchId the unique identifier of the match for which the timer has started
     */
    @Override
    public void timerStarted(UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.timerStarted();
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies the clients associated with the specified match and player that the timer
     * has already started.
     *
     * @param matchId the unique identifier of the match
     * @param player the player whose timer status is being checked
     */
    @Override
    public void timerAlreadyStarted(UUID matchId, Player player) {
        clients(matchId).forEach(h -> {
            try {
                if (player.getUsername().equals(h.getUsername())) {
                    h.timerAlreadyStarted();
                }
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Handles the event when the timer has ended. It notifies all the clients
     * associated with the given match ID.
     *
     * @param last   indicates whether this is the last timer event for the match.
     * @param matchId the unique identifier of the match whose timer ended.
     */
    @Override
    public void timerEnded(Boolean last, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.timerEnded(last);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Removes a block of spaceship tiles associated with a specific player in a particular match.
     *
     * @param block the list of SpaceShipTile objects representing the block to be removed
     * @param nick the nickname of the player associated with the block
     * @param matchId the unique identifier of the match from which the block is to be removed
     */
    @Override
    public void removeBlock(ArrayList<SpaceShipTile> block, String nick, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.removeBlock(block, nick);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies the clients of incorrect tile positions in the game.
     *
     * @param pos A list of integers representing the positions of the tiles that are incorrect.
     * @param p The player who has provided the tile positions.
     * @param matchId The unique identifier of the match in which the tiles are being checked.
     */
    @Override
    public void wrongTiles(ArrayList<Integer> pos, Player p, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.wrongTiles(pos, p.getUsername(), h.getUsername());
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Inserts a wait tile for a specific client in a match.
     *
     * @param nick the nickname of the player for whom the wait tile is inserted
     * @param idx the index position at which the wait tile is inserted
     * @param matchId the unique identifier of the match where the operation is performed
     */
    @Override
    public void insertWaitTileLMR(String nick, int idx, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.insertWaitTileLMR(nick, idx);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Notifies the appropriate client that they need to fill empty tiles
     * in a game match based on the provided player and match details.
     *
     * @param p the player who is associated with the action of filling empty tiles
     * @param matchId the unique identifier of the match where the action is required
     */
    @Override
    public void haveToFillEmptyTiles(Player p, UUID matchId) {
        clients(matchId).stream()
                .filter(h -> h.getUsername().equals(p.getUsername()))
                .forEach(h -> {
                    try {
                        h.haveToFillEmptyTiles();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Removes a player identified by their nickname from the flightboard in a specific match.
     *
     * @param nick    the nickname of the player to be removed from the flightboard
     * @param matchId the unique identifier of the match where the player will be removed
     */
    @Override
    public void removePlayerFromFlightboard(String nick, UUID matchId) {
        clients(matchId).forEach(h -> {
            try {
                h.removePlayerFromFlightboard(nick);
            } catch (Exception ignore) {
            }
        });
    }

    /**
     * Disconnects all clients and associated resources for the given match ID.
     * This method ensures that all players in the specified match are notified about
     * the disconnection, the match and its related data are removed from the system,
     * and internal resources are cleaned up.
     *
     * @param matchId the unique identifier of the match to disconnect all clients from
     */
    public void disconnectAll(UUID matchId) {
        List<SocketClientHandler> list = clientsByMatch.get(matchId);
        if (list != null) {
            for (SocketClientHandler h : list) {
                try {
                    h.notifyShutdown("Game interrupted");
                } catch (Exception ignore) {
                }
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
     * Notifies the client that there are not enough goods associated with the given player and UUID.
     *
     * @param player the player object containing information about the user
     * @param uuid the unique identifier associated with the client
     * @throws Exception if an error occurs while notifying the client
     */
    @Override
    public void notEnoughGoods(Player player, UUID uuid) throws Exception {
        clients(uuid).stream()
                .filter(h -> h.getUsername().equals(player.getUsername()))
                .forEach(h -> {
                    try {
                        h.notEnoughGoods();
                    } catch (Exception ignore) {
                    }
                });
    }

    /**
     * Removes an alien specified by its position and username, applying the operation to multiple clients
     * identified by the provided UUID.
     *
     * @param username the username of the alien to be removed
     * @param r the row position of the alien
     * @param c the column position of the alien
     * @param uuid the unique identifier for the associated clients to apply the removal operation
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeAlien(String username, int r, int c, UUID uuid) throws Exception {
        clients(uuid).forEach(h -> {
            try {
                h.removeAlien(username,r,c);
            } catch (Exception ignore) {
            }
        });
    }
    /**
     * Notifies the clients of a player losing specified batteries and handles the respective actions.
     *
     * @param p the player who lost the batteries
     * @param batteriesToAct a nested list of integers representing the batteries to process
     * @param uuid the unique identifier for the associated client
     * @throws Exception if an error occurs during the notification process
     */
    @Override
    public void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct, UUID uuid)throws Exception{
        clients(uuid).forEach(h -> {
            try {
                h.lostBatteries(p,batteriesToAct);
            } catch (Exception ignore) {
            }
        });
    }
}
