package it.polimi.ingsw.View;

import it.polimi.ingsw.Network.Client.GenericClient;

import java.rmi.RemoteException;

public abstract class GenericClientExtended extends GenericClient {

    /**
     * Indicates whether the username has been accepted by the server or system.
     *
     * This variable is used to track the status of the username validation process.
     * If set to true, it means the username has been successfully validated and accepted.
     * If set to false, it indicates that the username has not yet been accepted or has been reset
     * as part of the username status handling.
     */
    private boolean usernameAccepted = false;
    /**
     * A private boolean variable that indicates whether the username of the client
     * has been rejected. This flag is used to manage the state of username validation
     * during the interaction between the client and the system.
     *
     * When set to true, it signifies that the username provided by the client
     * was rejected, potentially due to validation errors, duplication, or other
     * system-specific reasons. When false, it indicates no rejection occurred.
     */
    private boolean usernameRejected = false;

    /**
     * Constructs a new instance of GenericClientExtended, which extends the functionality
     * of the GenericClient class. This constructor initializes the superclass
     * and may throw a RemoteException if a remote communication error occurs.
     *
     * @throws RemoteException if a remote communication error occurs during initialization.
     */
    public GenericClientExtended() throws RemoteException {
        super();
    }

    /**
     * Determines whether the username has been accepted by the system.
     *
     * @return true if the username is accepted, false otherwise.
     */
    @Override
    public boolean isUsernameAccepted() {
        return usernameAccepted;
    }

    /**
     * Determines if the username has been rejected during the process.
     *
     * @return true if the username was rejected, false otherwise.
     */
    @Override
    public boolean isUsernameRejected() {
        return usernameRejected;
    }

    /**
     * Resets the status flags related to username acceptance and rejection.
     *
     * This method sets both the `usernameAccepted` and `usernameRejected` flags
     * to false. This is typically used to clear any prior username authentication state.
     */
    @Override
    public void resetUsernameFlags() {
        usernameAccepted = false;
        usernameRejected = false;
    }

}