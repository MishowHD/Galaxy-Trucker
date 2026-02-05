package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Network.Client.RMI.RMIClient;
import it.polimi.ingsw.Network.Client.RMI.VirtualServerRMI;
import it.polimi.ingsw.Network.Client.Socket.SocketClient;
import it.polimi.ingsw.View.LittleModelRepresentation;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientFactory {
    /**
     * Creates and initializes a client instance based on the type of connection specified.
     * This method supports creation of either a Socket-based client or an RMI-based client,
     * depending on the value of the {@code isSocket} parameter.
     *
     * @param lm the {@code LittleModelRepresentation} instance representing the application's data model.
     * @param isSocket a boolean flag indicating whether the client should use a socket connection
     *                 (if {@code true}) or an RMI connection (if {@code false}).
     * @param host the hostname or IP address of the server to which the client connects.
     * @param username the username to be used by the client during initialization.
     * @return a {@code GenericClient} instance, either a {@code SocketClient} or {@code RMIClient},
     *         depending on the connection type specified.
     * @throws IOException if a communication error occurs during socket or stream initialization.
     * @throws NotBoundException if the RMI registry cannot locate the specified server object.
     */
    public static GenericClient createClient(LittleModelRepresentation lm, boolean isSocket, String host, String username) throws IOException, NotBoundException {
        int port = isSocket ? 25566 : 25565;
        if (isSocket) {
            SocketClient SCL = getSocketClient(lm, host, port);
            SCL.run(username);
            return SCL;
        } else {
            final String serverName = "GameServer";
            Registry registry = LocateRegistry.getRegistry(host, port);
            VirtualServerRMI server = (VirtualServerRMI) registry.lookup(serverName);
            RMIClient rmiClient = new RMIClient(lm, server);
            rmiClient.run(username);
            return rmiClient;
        }
    }

    /**
     * Creates and initializes a {@code SocketClient} instance by establishing a socket connection
     * to the specified host and port and configuring input/output streams for communication.
     *
     * @param lm the {@code LittleModelRepresentation} instance used to initialize the {@code SocketClient}.
     * @param host the hostname or IP address to connect to.
     * @param port the port number on the host to connect to.
     * @return a newly created {@code SocketClient} instance configured for communication.
     * @throws IOException if an I/O error occurs while setting up the socket or streams.
     */
    private static SocketClient getSocketClient(LittleModelRepresentation lm, String host, int port) throws IOException {
        Socket serverSocket = new Socket(host, port);
        ObjectInputStream socketRx = new ObjectInputStream(serverSocket.getInputStream());
        ObjectOutputStream socketTx = new ObjectOutputStream(serverSocket.getOutputStream());
        socketTx.flush();

        return new SocketClient(lm, socketRx, socketTx);
    }
}