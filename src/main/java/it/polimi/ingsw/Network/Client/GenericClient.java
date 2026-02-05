package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.View.TUI.InputManager;
import it.polimi.ingsw.Network.VirtualServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GenericClient extends UnicastRemoteObject implements VirtualServer {
    /**
     * Manages input operations and interactions for the client.
     * Acts as a bridge between the client and its input-related functionalities.
     * Initialized during the construction of the client class and can be used
     * to set up input-related models or tasks.
     */
    protected InputManager inputManager;
    /**
     * Represents the nickname associated with the client.
     * This value is used to identify the client within the application.
     */
    protected String nickName;
    /**
     * A static reference to a {@link LittleModelRepresentation} object used within
     * the application. This variable is shared across all instances of the enclosing
     * class. It is intended to represent or maintain a simplified state or model
     * representation, serving as an abstraction of the application's data model.
     */
    protected static LittleModelRepresentation view;
    /**
     * A unique identifier for the instance of the GenericClient class.
     * This UUID is automatically generated during the instantiation of the object
     * and is used for identifying the instance uniquely among multiple clients.
     */
    protected UUID uuid = UUID.randomUUID();
    /**
     * A map that associates game identifiers with their corresponding UUIDs.
     * The key represents the game's unique identifier (an integer), and the value
     * represents the UUID of the game instance or reference associated with that identifier.
     *
     * This map is typically used to track and manage multiple game sessions
     * or instances within the containing class.
     */
    protected Map<Integer, UUID> games = new HashMap<>();

    /**
     * Retrieves the collection of game mappings where the key represents an integer ID
     * and the value represents the corresponding UUID of a game.
     *
     * @return a map containing game IDs associated with their respective UUIDs
     */
    public Map<Integer, UUID> getGames() {
        return games;
    }

    /**
     * Retrieves the UUID associated with this instance of the GenericClient.
     *
     * @return the UUID of this GenericClient
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Updates the UUID associated with this client.
     *
     * @param uuid the UUID to be set for this client
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Sets the view for the GenericClient.
     *
     * @param view the LittleModelRepresentation instance to be set as the current view
     */
    public void setview(LittleModelRepresentation view) {
        GenericClient.view = view;
    }

    /**
     * Retrieves the current shared LittleModelRepresentation view.
     *
     * @return the shared LittleModelRepresentation instance representing the view.
     */
    public LittleModelRepresentation getview() {
        return view;
    }

    /**
     * Constructs a GenericClient instance and initializes its InputManager.
     *
     * @throws RemoteException if a remote communication error occurs during the creation of the GenericClient instance.
     */
    public GenericClient() throws RemoteException {
        inputManager = new InputManager(this);
    }

    /**
     * Sets the given model representation to the {@code InputManager}.
     *
     * @param view the {@code LittleModelRepresentation} object to set in the {@code InputManager}
     */
    public void setModelToInputManager(LittleModelRepresentation view) {
        inputManager.setModel(view);
    }


    /**
     * Checks whether the username provided to the system is accepted.
     * This method typically evaluates the username against specific validation rules or criteria.
     *
     * @return true if the username is accepted, false otherwise.
     */
    public boolean isUsernameAccepted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determines whether the username has been rejected.
     *
     * @return true if the username is rejected, false otherwise
     */
    public boolean isUsernameRejected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Resets the state or flags related to username validation or processing within the client.
     * This method is typically used to clear any previous state regarding username acceptance
     * or rejection, preparing the system for a fresh username handling operation.
     *
     * Throws an {@code UnsupportedOperationException} if the method is not implemented.
     */
    public void resetUsernameFlags() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
