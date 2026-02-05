package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Network.Server.RMI.RMIServer;
import it.polimi.ingsw.Network.Server.Socket.SocketServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;

public class ServerMain {
    /**
     * A static instance of the {@link GenericServer} used to manage RMI (Remote Method Invocation) based server operations.
     * This server instance is responsible for handling remote client interactions and providing the necessary
     * back-end functionalities for distributed systems.
     *
     * The implementation of {@code rmiServer} would typically extend the {@link GenericServer} class to provide
     * specific RMI behavior, such as starting the server, stopping it, and managing connected clients alongside
     * game-related operations.
     *
     * The {@code rmiServer} is initialized within the {@link ServerMain#main(String[])} method and starts
     * its execution in a dedicated thread to support concurrent server functionalities.
     */
    private static GenericServer rmiServer, /**
     * Represents a server that manages socket-based connections.
     * This server is responsible for handling incoming client connections
     * and facilitating communication over a socket-based protocol.
     *
     * The specific behavior for starting, stopping, and managing the server's operations
     * is implemented in the {@link GenericServer} subclass.
     *
     * This instance is initialized in the main method of the ServerMain class and
     * is designed to work concurrently with an RMI server instance.
     */
    socketServer;
    /**
     * A static instance of {@link ServerSocket} used to handle incoming socket connections.
     * This socket listens for and accepts incoming connections on a specified port,
     * which is initialized in the server's main execution logic.
     *
     * The {@code ServerSocket} instance here is shared across the application
     * and utilized to facilitate communication between the server and its clients.
     * It is critical to properly initialize and close this socket to avoid resource leaks
     * and to ensure smooth server operation.
     */
    public static ServerSocket socket;

    /**
     * The entry point of the application. Initializes and starts the RMI server, the socket server,
     * and manages the server shutdown process via console input.
     *
     * @param args command-line arguments passed to the program
     * @throws IOException if an I/O error occurs during initialization
     * @throws InterruptedException if the thread is interrupted while waiting for termination
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        rmiServer = new RMIServer();
        int socketPort = 25566;
        socket = new ServerSocket(socketPort);
        socketServer = new SocketServer(socket);
        Thread rmiThread = new Thread(() -> {
            try {
                rmiServer.start(args);
            } catch (IOException e) {
                System.out.println("RMI Server terminated");
            }
        });

        Thread socketThread = new Thread(() -> {
            try {
                socketServer.start(args);
            } catch (IOException e) {
                System.out.println("Socket Server terminated");
            }
        });

        rmiThread.start();
        socketThread.start();
        Thread consoleThread = getThread();
        consoleThread.start();

        new CountDownLatch(1).await();
    }

    /**
     * Creates and returns a daemon thread that listens for console input,
     * allowing the server to be terminated gracefully by typing the "close" command.
     * This thread handles shutting down the RMI server, socket server, and associated resources
     * before exiting the application.
     *
     * @return A daemon Thread instance configured to monitor console input for the "close" command.
     */
    private static Thread getThread() {
        Thread consoleThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Server started. Type 'close' to terminate...");
                while (true) {
                    String input = reader.readLine();
                    if ("close".equalsIgnoreCase(input)) {
                        System.out.println("Shutting down servers...");

                        // Chiusura pulita
                        rmiServer.stop();
                        socketServer.stop();
                        socket.close();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                        System.exit(0);
                    } else {
                        System.out.println("Only 'close' command is allowed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        consoleThread.setDaemon(true);
        return consoleThread;
    }
}