package it.polimi.ingsw.Network.Client.Socket;

import java.io.*;

public class SocketServerHandler implements Serializable {
    /**
     * A unique identifier for this Serializable class.
     *
     * The `serialVersionUID` is used during the deserialization process
     * to verify that the sender and receiver of a serialized object
     * maintain compatibility with respect to serialization.
     *
     * If this value does not match the `serialVersionUID` of the serialized
     * object, deserialization will result in an `InvalidClassException`.
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * The ObjectOutputStream used to send serialized objects
     * to a receiving end of a socket connection.
     * This field is marked as transient to avoid being serialized,
     * as it is not serializable by nature and should be re-initialized
     * during deserialization.
     *
     * This ObjectOutputStream is primarily used for communication
     * in the socket server handler to propagate messages, commands,
     * or other data structures.
     */
    private final transient ObjectOutputStream output;

    /**
     * Initializes a new instance of the {@code SocketServerHandler} class with the given {@code ObjectOutputStream}.
     * The output stream is flushed during initialization.
     *
     * @param output the {@code ObjectOutputStream} used for communication, must not be null
     */
    public SocketServerHandler(ObjectOutputStream output) {
        this.output = output;
        try {
            this.output.flush();
        } catch (IOException e) {
            System.err.println("Error initializing output stream: " + e.getMessage());
        }
    }

    /**
     * Sends a "commandgiver" identifier followed by the specified arguments to the output stream.
     *
     * @param args the variable-length arguments to be sent with the "commandgiver" message
     * @throws IOException if an I/O error occurs while writing to the output stream
     */
    public void commandGiver(Object... args) throws IOException {
        output.writeObject("commandgiver");
        output.writeObject(args);
        output.flush();
    }


    /**
     * Sends a command to set the username to the connected client through the output stream.
     *
     * @param username the username to be set for the client
     */
    public void setusername(String username) {
        try {
            output.writeObject("setusername");
            output.writeObject(username);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending username: " + e.getMessage());
        }
    }

    /**
     * Closes the output stream associated with this instance.
     * If the output stream is not null, it flushes any remaining data
     * and then closes the stream. Any {@code IOException} that occurs
     * during the process is caught and ignored.
     */
    public void close() {
        try {
            if (output != null) {
                output.flush();
                output.close();
            }
        } catch (IOException _) {
        }
    }

    /**
     * Sends a "ping" message through the ObjectOutputStream.
     * This method sends the string "ping" to the connected output stream and flushes the stream.
     *
     * @throws IOException if an I/O error occurs while writing or flushing the output stream.
     */
    public void ping() throws IOException {
        output.writeObject("ping");
        output.flush();
    }

    /**
     * Sends a "pong" response through the output stream. This is typically used
     * as a response to a "ping" message in client-server communication.
     *
     * @throws IOException if an I/O error occurs while writing the message or flushing the stream.
     */
    public void sendPong() throws IOException {
        output.writeObject("pong");
        output.flush();
    }
}