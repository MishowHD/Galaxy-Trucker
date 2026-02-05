package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.GameControl.GameController;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.EventBus.UpdateListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GenericServer extends UnicastRemoteObject implements UpdateListener {
    /**
     * A thread-safe collection that maps unique game identifiers (UUID) to their respective Game instances.
     * This map is used to store and manage all active games on the server.
     */
    protected static final Map<UUID, Game> games = new ConcurrentHashMap<>();
    /**
     * A thread-safe map that stores the association between unique game identifiers (UUID) and
     * their corresponding {@link GameController} instances. This map is utilized to manage and
     * retrieve controllers for active games on the server.
     */
    protected static final Map<UUID, GameController> controllers = new ConcurrentHashMap<>();
    /**
     * A protected integer field used to identify the server instance.
     * This variable can be utilized to manage and differentiate between
     * different instances of the server within the application.
     */
    protected int n = 0;
    /**
     * A thread-safe set containing the usernames of currently connected users.
     * This set is used to track and manage active connections.
     * It provides a shared data structure for monitoring connected clients
     * across different parts of the server implementation.
     */
    public static final Set<String> connectedUsernames = new HashSet<>();

    /**
     * Constructs a new GenericServer instance.
     * This constructor initializes the server by invoking the parent class
     * constructor from {@link UnicastRemoteObject}.
     *
     * @throws RemoteException if an error occurs during the initialization of the server
     */
    protected GenericServer() throws RemoteException {
        super();
    }

    /**
     * Retrieves the set of usernames currently connected to the server.
     *
     * @return A set of strings representing the usernames of connected users.
     */
    public Set<String> getConnectedUsernames() {
        return connectedUsernames;
    }


    /**
     * Retrieves the Game instance associated with the specified unique identifier (UUID).
     *
     * @param id the UUID of the game to retrieve
     * @return the Game object associated with the specified UUID, or null if no game is found
     */
    protected Game game(UUID id) {
        return games.get(id);
    }

    /**
     * Starts the server with the specified arguments.
     *
     * @param args the command-line arguments used to configure the server
     *             or define specific runtime behavior
     * @throws IOException if an I/O error occurs during initialization
     */
    public void start(String[] args) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Stops the server operations, releasing any resources associated with it.
     * This method is intended to be implemented by subclasses to handle
     * specific shutdown procedures required for the server.
     *
     * Subclasses should ensure that this method performs a clean shutdown,
     * which may include closing connections, stopping threads, or freeing resources.
     *
     * Throws an {@link UnsupportedOperationException} if called on the base class
     * without proper overriding in a subclass.
     */
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Updates the current state of card usage in the game for the specified players and match.
     *
     * @param ps the list of players involved in the update.
     * @param state the c_State object representing the new state to be applied.
     * @param matchId the unique identifier of the match for which the state is being updated.
     * @throws Exception if an error occurs during the state update process.
     */
    public abstract void updateCardUseSTATE(ArrayList<Player> ps, c_State state, UUID matchId) throws Exception;


}
