package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.View.GUI.GUI;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.View.TUI.TUI;
import javafx.application.Platform;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ClientMain {

    /**
     * The entry point of the application. It initializes the application based on the provided arguments,
     * configures the connection mode, and starts either a text-based user interface (TUI) or a graphical user interface (GUI).
     *
     * @param args Array of command-line arguments.
     *             args[0] specifies the server address (must be a valid IPv4 address).
     *             args[1] specifies the connection mode ("--socket" for Socket or "--rmi" for RMI).
     *             args[2] specifies the interface type ("--TUI" for text-based UI or "--GUI" for graphical UI).
     * @throws Exception if an unexpected error occurs during the initialization or connection.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Missing arguments");
            return;
        }
        int selection;
        boolean socket;
        Scanner scanner = new Scanner(System.in);
        boolean tui;
        System.out.println("""
                                 \u001B[38;5;93m\u001B[0;1;34;94m░█▀▀░█▀█░█░░░█▀█\u001B[0;34m░█░█░█░█\u001B[0m
                                 \u001B[0;1;34;94m░█░█░█▀█\u001B[0;34m░█░░░█▀█░▄▀▄░░█░\u001B[0m
                                 \u001B[0;34m░▀▀▀░▀░▀░▀▀▀░▀░▀\u001B[0;37m░▀░▀░░▀░\u001B[0m\u001B[0m
                               \u001B[38;5;51m\u001B[0;1;34;94m░▀█▀░█▀▄░█░█░█▀▀\u001B[0;34m░█░█░█▀▀░█▀▄\u001B[0m
                               \u001B[0;1;34;94m░░█░░█▀▄\u001B[0;34m░█░█░█░░░█▀▄░█▀▀\u001B[0;37m░█▀▄\u001B[0m
                               \u001B[0;34m░░▀░░▀░▀░▀▀▀░▀▀▀\u001B[0;37m░▀░▀░▀▀▀░▀░▀\u001B[0m\u001B[0m
                           \u001B[38;5;226m✦     ✧    ✦        ✦        ✧     ✦\u001B[0m
                """);
        String input = args[1];
        if (input.equals("--socket")) {
            socket = true;
        } else if (input.equals("--rmi")) {
            socket = false;
        } else {
            System.err.println("You have to choose RMI or Socket!");
            return;
        }
        String serverAddress = args[0];
        String ipv4Pattern =
                "^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)"
                        + "(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}$";


        if (!serverAddress.matches(ipv4Pattern)) {
            System.err.println("Not valid IP address");
            return;
        }
        String Username;
        input = args[2];
        if (input.equals("--TUI")) {
            selection = 0;
        } else if (input.equals("--GUI")) {
            selection = 1;
        } else {
            System.err.println("You have to choose TUI or GUI!");
            return;
        }
        LittleModelRepresentation littlemodel;
        GeneralView generalView;
        if (selection == 0) {
            tui = true;
            generalView = new TUI();
            littlemodel = new LittleModelRepresentation(generalView);
            generalView.setLittleModelRepresentation(littlemodel);
        } else {
            tui = false;
            littlemodel = new LittleModelRepresentation(null);
            GUI.configure(socket, serverAddress, littlemodel);
            GUI.launchGUI();
            Platform.setImplicitExit(false);
            generalView = GUI.getInstance();
            littlemodel.setGeneralView(generalView);
        }
        boolean userNameAcc = false;
        if (tui) { //in gui it's managed in gui class
            if (!socket) {
                while (!userNameAcc) {
                    System.out.println("Insert your username: ");
                    Username = scanner.nextLine();
                    try {
                        GenericClient client = ClientFactory.createClient(littlemodel, false, serverAddress, Username);
                        userNameAcc = true;
                        generalView.setClient(client);
                        client.setview(littlemodel);
                        generalView.Start(Username);
                    } catch (RemoteException e) {
                        if (e.getMessage() != null && e.getMessage().contains("already in use")) {
                            System.out.println("Username already in use, insert another name");
                        } else {
                            throw e;
                        }
                    } catch (NotBoundException | IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                while (!userNameAcc) {
                    System.out.println("Insert your username: ");
                    Username = scanner.nextLine();
                    try {
                        GenericClient client = ClientFactory.createClient(littlemodel, true, serverAddress, Username);
                        while (true) {
                            if (client.isUsernameAccepted()) {
                                userNameAcc = true;
                                client.setview(littlemodel);
                                generalView.Start(Username);
                                break;
                            } else if (client.isUsernameRejected()) {
                                System.out.println("Username already in use, insert another name");
                                client.resetUsernameFlags();
                                break;
                            }
                        }
                    } catch (RemoteException e) {
                        System.out.println("Connection error: " + e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


}

