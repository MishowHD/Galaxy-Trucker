package it.polimi.ingsw.Network.Server.Socket;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.GameControl.GameController;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.View.Utils_View.CommandType;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Utils.stateEnum;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

public class SocketClientHandler implements VirtualViewSocket {
    /**
     * The GameController instance responsible for managing the game's state, behavior,
     * and interactions. It serves as the central point for controlling game logic and
     * coordinating various game elements, such as player actions, game rules, and events.
     */
    GameController controller;
    /**
     * Represents a final instance of a SocketServer that is used to handle
     * server-side socket communications. This variable is immutable once
     * initialized and facilitates listening for and managing client connections
     * through socket-based networking.
     */
    final SocketServer server;
    /**
     * A universally unique identifier (UUID) object used to represent a 128-bit value.
     * The UUID is typically used to uniquely identify objects, records, or entities
     * across a system or network.
     */
    UUID uuid;

    /**
     * A final instance of ObjectOutputStream used to write objects to an output stream.
     * This variable is intended for performing serialization of objects into a stream
     * that can be written to a file or transmitted over a network.
     *
     * The output stream must be properly initialized before use and closed after use
     * to ensure proper resource management.
     */
    final ObjectOutputStream output;
    /**
     * An ObjectInputStream instance used to read objects from an underlying
     * InputStream. It is typically utilized to deserialize objects previously
     * serialized using an ObjectOutputStream.
     *
     * The `input` variable allows for the reconstruction of object data stored as
     * a byte stream, enabling persistence and transportation of objects.
     *
     * Note: The associated InputStream should be properly initialized and
     * closed when no longer needed to avoid resource leaks. Deserialization
     * operations should consider compatibility between serialized and
     * deserialized object versions.
     */
    ObjectInputStream input;

    /**
     * Represents the username of a user in the system.
     * This variable is typically used to store and manage
     * the unique identifier or name associated with a specific user.
     * It can be utilized for authentication, authorization, or
     * personalization purposes within the application.
     */
    String username = "";
    /**
     * Represents the timestamp of the last received "pong" signal, typically used
     * in a heartbeat or connectivity check mechanism to monitor the responsiveness
     * of a remote system or service.
     *
     * The value is stored as the number of milliseconds since the Unix epoch,
     * as returned by {@link System#currentTimeMillis()}.
     *
     * This variable is marked as volatile to ensure visibility of its latest
     * value across multiple threads in a multithreaded environment.
     */
    private volatile long lastPongTime = System.currentTimeMillis();

    /**
     * Constructs a new SocketClientHandler instance.
     *
     * @param uuid the unique identifier for the client
     * @param server the server instance associated with this client handler
     * @param input the input stream to receive data from the client
     * @param output the output stream to send data to the client
     */
    public SocketClientHandler(UUID uuid, SocketServer server, ObjectInputStream input, ObjectOutputStream output) {
        this.uuid = uuid;
        this.server = server;
        this.input = input;
        this.output = output;
        this.controller = null;
    }

    /**
     * Sets the controller for the current game context.
     *
     * @param controller the GameController instance to be associated with this object
     */
    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Sets the UUID for this object.
     *
     * @param uuid the UUID to be assigned
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Retrieves the username associated with this object.
     *
     * @return the username as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the current instance.
     *
     * @param username the username to be set
     */
    private void setUsername(String username) {
        this.username = username;
    }

    /**
     * Notifies the system or service of a shutdown event by sending a shutdown message and a reason.
     *
     * @param reason the reason for the shutdown to be communicated
     */
    public void notifyShutdown(String reason) {
        try {
            output.writeObject("shutdown");
            output.writeObject(new Object[]{reason});
            output.flush();
        } catch (IOException _) {

        }
    }

    /**
     * Continuously listens for and processes messages received from a connected client
     * through the input stream. This method is responsible for handling different commands
     * based on the type of JSON input received and ensures the proper functionality of
     * communication between the server and the client.
     *
     * The method processes the following types of inputs:
     * - "setusername": Handles user registration by setting the username if it's available,
     *   or sends back an acknowledgment if the username is already taken or accepted.
     * - "commandgiver": Executes commands received from the client using a specified command type
     *   and arguments.
     * - "ping": Responds to the client's ping request by sending a pong message.
     * - "pong": Updates the last recorded pong time to track communication status with the client.
     *
     * In case of an `EOFException`, the method releases resources by closing the input and
     * output streams safely.
     *
     * @throws Exception if there is an issue with object reading or processing during execution.
     */
    public void runVirtualView() throws Exception {
        try {
            String json;
            while ((json = (String) input.readObject()) != null) {
                switch (json) {
                    case "setusername":
                        String username = (String) input.readObject();
                        synchronized (server.getConnectedUsernames()) {
                            if (server.getConnectedUsernames().contains(username)) {
                                output.writeObject("username_taken");
                                output.flush();
                            } else {
                                server.getConnectedUsernames().add(username);
                                setUsername(username);
                                output.writeObject("username_ack");
                                output.flush();
                                System.out.println(username + ": Connected with socket to GameServer");
                            }
                        }
                        break;
                    case "commandgiver":
                        Object[] args = (Object[]) input.readObject();
                        CommandType methodEnum = (CommandType) args[0];
                        handleCommand(methodEnum, args);
                        break;
                    case "ping":
                        sendPong();
                        break;
                    case "pong":
                        lastPongTime = System.currentTimeMillis();
                        break;

                }
            }
        } catch (EOFException _) {
            try {
                input.close();
            } catch (IOException ignored) {
            }
            try {
                output.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Sends a "pong" message over the output stream.
     *
     * This method writes the "pong" message to the output object,
     * ensuring thread safety by synchronizing on the output object.
     * After writing the message, the output stream is flushed.
     *
     * @throws Exception if an error occurs during the write or flush operation
     */
    private void sendPong() throws Exception {
        synchronized (output) {
            output.writeObject("pong");
            output.flush();
        }
    }

    /**
     * Retrieves the timestamp of the last received "pong" response.
     *
     * @return the timestamp of the last "pong" in milliseconds since the epoch.
     */
    public long getLastPongTime() {
        return lastPongTime;
    }

    /**
     * Handles commands by executing specific actions based on the provided command type and arguments.
     *
     * @param methodName The type of command to handle, represented by a {@link CommandType}.
     * @param args An array of arguments required to execute the command.
     *             The structure and data types of the arguments depend on the specific command.
     * @throws Exception If an error occurs while processing the command.
     */
    private void handleCommand(CommandType methodName, Object[] args) throws Exception {
        switch (methodName) {
            case CREATE_GAME:
                server.createGame((int) args[1], uuid, (int) args[3], (String) args[4]);
                break;
            case JOIN_GAME:
                server.joinGame((String) args[1], (Integer) args[2], (UUID) args[3], uuid);
                break;
            case ACTIVATE_TIMER:
                controller.activateTimer(controller.getGame().getPlayer(controller.getGame().getPlayerIDFromName((String) args[1])));
                break;
            case SET_NUM_TILE:
                controller.setNumTile((Integer) args[1], (Integer) args[2]);
                break;
            case SURREND:
                controller.Surrend((Integer) args[1]);
                break;
            case VISUALIZE_FINAL_SCORES:
                controller.visualizeFinalScores();
                break;
            case PICK_TILE_ALREADY_FLIPPED:
                controller.pickTileAlreadyFlipped((Integer) args[1], (Integer) args[2]);
                break;
            case PICK_TILE_UNKNOWN:
                controller.pickTileUnknown((Integer) args[1]);
                break;
            case PICK_LITTLE_DECK:
                controller.pickLittleDeck((Integer) args[1], (Integer) args[2]);
                break;
            case DEPOSIT_LITTLE_DECK:
                controller.depositLittleDeck((Integer) args[1]);
                break;
            case DEPOSIT_TILE:
                controller.depositTile((Integer) args[1]);
                break;
            case END_BUILDING:
                controller.endbuilding((Integer) args[1], (Integer) args[2]);
                break;
            case INSERT_TILE:
                controller.insertTile((Integer) args[1], (Integer) args[2], (Integer) args[3], (Integer) args[4]);
                break;
            case EFFECT_ACTIVATION:
                controller.EffectActivation();
                break;
            case GET_CARD_IN_USE:
                controller.getCardinuse();
                break;
            case CHOOSE_ONE_SUB_SHIP:
                controller.chooseOneSubShip((Integer) args[1], (Integer) args[2]);
                break;
            case ACCEPT_TO_LAND_ON_A_PLANET:
                controller.acceptToLandOnAPlanet(controller.getGame().getPlayerFromNickname((String) args[1]), (Boolean) args[2], (Integer) args[3]);
                break;
            case CHOOSE_ABANDONED_STATION:
                controller.chooseAbandonedStation(controller.getGame().getPlayerFromNickname((String) args[1]), controller.getGame().getGameFlightBoard(), (Boolean) args[2], (ArrayList<ArrayList<Integer>>) args[3], (ArrayList<ArrayList<Goods>>) args[4]);
                break;
            case CHOOSE_CANNON_BATTERY_POS:
                controller.chooseCannonBatteryPos(controller.getGame().getGameFlightBoard(), controller.getGame().getPlayerFromNickname((String) args[1]), (ArrayList<ArrayList<Integer>>) args[2], (ArrayList<ArrayList<Integer>>) args[3]);
                break;
            case CHOOSE_HOW_TO_FACE_METEORS:
                controller.chooseHowToFaceMeteors(controller.getGame().getPlayerFromNickname((String) args[1]), (ArrayList<Integer>) args[2], controller.getGame().getGameFlightBoard());
                break;
            case CHOOSE_PASSENGERS_TO_LOSE:
                controller.choosePassengersToLose(controller.getGame().getPlayerFromNickname((String) args[1]), (Boolean) args[2], (ArrayList<ArrayList<Integer>>) args[3], controller.getGame().getGameFlightBoard());
                break;
            case CHOOSE_TO_CLAIM_REWARD:
                controller.chooseToClaimReward((Boolean) args[1], controller.getGame().getPlayerFromNickname((String) args[2]));
                break;
            case CHOOSE_TO_CLAIM_REWARD_WITH_GOODS:
                controller.chooseToClaimReward(controller.getGame().getGameFlightBoard(), (Boolean) args[1], controller.getGame().getPlayerFromNickname((String) args[2]), (ArrayList<ArrayList<Integer>>) args[3], (ArrayList<ArrayList<Goods>>) args[4]);
                break;
            case CHOOSE_TO_PLACE_BATTERIES:
                controller.chooseToPlaceBatteries(controller.getGame().getPlayerFromNickname((String) args[1]), (ArrayList<ArrayList<Integer>>) args[2]);
                break;
            case CHOOSE_TO_START_FIRE_POWER:
                controller.chooseToStartFirePower(controller.getGame().getPlayerFromNickname((String) args[1]), (ArrayList<ArrayList<Integer>>) args[2], (ArrayList<ArrayList<Integer>>) args[3]);
                break;
            case CHOOSE_TO_START_MOTOR:
                controller.chooseToStartMotor(controller.getGame().getPlayerFromNickname((String) args[1]), controller.getGame().getGameFlightBoard(), (ArrayList<ArrayList<Integer>>) args[2], (ArrayList<ArrayList<Integer>>) args[3]);
                break;
            case CHOOSE_WHERE_TO_PUT_GOODS:
                controller.chooseWhereToPutGoods(controller.getGame().getPlayerFromNickname((String) args[1]), (ArrayList<ArrayList<Integer>>) args[2], (ArrayList<ArrayList<Goods>>) args[3]);
                break;
            case ADD_WAIT_TILE:
                controller.addWaitTile((Integer) args[1]);
                break;
            case INSERT_WAIT_TILE:
                controller.insertWaitTile((Integer) args[1], (Integer) args[2], (Integer) args[3], (Integer) args[4], (Integer) args[5]);
                break;
            case COMMAND_FILL_TILE:
                controller.CommandFillTile(Integer.parseInt(args[1].toString()), (boolean) args[2], (AlienColor) args[3], Integer.parseInt(args[4].toString()), Integer.parseInt(args[5].toString()));
                break;
            case ACTIVE_GAMES:
                server.activeGames(uuid);
                break;
            case DEMO:
                controller.demoGame();
                break;
            default:
                System.out.println("Not valid input");
        }
    }

    /**
     * Sends an error message to the output stream.
     * This method writes the string "error" to the synchronized output
     * object and then flushes the stream to ensure the message is sent.
     *
     * In case of an I/O exception during the operation, it prints the
     * stack trace of the encountered exception to the standard error stream.
     *
     * Thread Safety:
     * This method uses synchronization to ensure thread-safe usage of the
     * output stream.
     */
    @Override
    public void error() {
        synchronized (output) {
            try {
                output.writeObject("error");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "ping" message to the output stream. This method is synchronized
     * to ensure thread-safe use of the {@code output} object.
     *
     * @throws IOException if an I/O error occurs during writing or flushing the message.
     */
    @Override
    public void ping() throws IOException {
        synchronized (output) {
            output.writeObject("ping");
            output.flush();
        }
    }


    /**
     * Changes the ID by sending the new ID to the output stream.
     * This method ensures thread safety by synchronizing on the output object.
     *
     * @param id the new UUID to be communicated as the identifier
     */
    @Override
    public void changeID(UUID id) {
        synchronized (output) {
            try {
                output.writeObject("changeID");
                output.writeObject(new Object[]{id});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Signals the completion of the building phase by writing a specific message
     * ("completeBuildingPhase") to the output stream.
     *
     * This method ensures thread-safe access to the shared output resource
     * by synchronizing on the output object. The message is serialized and
     * flushed to the output stream. If an IOException occurs during this
     * operation, the exception is caught and its stack trace is printed.
     *
     * This method overrides a superclass or interface method.
     */
    @Override
    public void completeBuildingPhase() {
        synchronized (output) {
            try {
                output.writeObject("completeBuildingPhase");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the game state to reflect that the game has been created.
     *
     * @param level the level of the game being created
     * @param ShowableDecks the list of decks that are visible or available in the game
     * @param players list of players participating in the game
     * @param hourglass the number of hourglass tokens available in the game
     * @param surrender the surrender count or flag for the game
     */
    @Override
    public void updateGameCreated(int level, List<Deck> ShowableDecks, ArrayList<Player> players, int hourglass, int surrender) {
        synchronized (output) {
            try {
                output.writeObject("updateGameCreated");
                output.writeObject(new Object[]{level, ShowableDecks, players, hourglass, surrender});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a request to deposit the item currently held by the specified player.
     *
     * @param player the name or identifier of the player whose held item will be deposited
     */
    @Override
    public void depositThingInHand(String player) {
        synchronized (output) {
            try {
                output.writeObject("depositThingInHand");
                output.writeObject(new Object[]{player});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a request to pick a tile in a game represented by the specified tile index
     * and associates it with the provided player's nickname.
     * This method is synchronized to ensure thread-safe access to the output stream.
     *
     * @param TileIndex the index of the tile to be picked
     * @param playerNickname the nickname of the player who is picking the tile
     */
    @Override
    public void pickTileLMR(int TileIndex, String playerNickname) {
        synchronized (output) {
            try {
                output.writeObject("pickTileLMR");
                output.writeObject(new Object[]{TileIndex, playerNickname});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inserts a tile with specified parameters into the game board.
     *
     * @param TileIndex The index of the tile to be inserted.
     * @param r The row position where the tile is to be placed.
     * @param c The column position where the tile is to be placed.
     * @param rotation The rotation configuration of the tile.
     * @param playerNickname The nickname of the player performing the action.
     */
    @Override
    public void insertTileLMR(int TileIndex, int r, int c, int rotation, String playerNickname) {
        synchronized (output) {
            try {
                output.writeObject("insertTileLMR");
                output.writeObject(new Object[]{TileIndex, r, c, rotation, playerNickname});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the selection of a little deck based on the specified index and player nickname.
     *
     * @param deckIndex the index of the little deck to be picked
     * @param playerNickname the nickname of the player making the selection
     */
    @Override
    public void pickLittleDeckLMR(int deckIndex, String playerNickname) {
        synchronized (output) {
            try {
                output.writeObject("pickLittleDeckLMR");
                output.writeObject(new Object[]{deckIndex, playerNickname});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds an alien or human player to the game at the specified location.
     *
     * @param playerNickname the nickname of the player to be added
     * @param wantAlien a boolean indicating whether the player is an alien (true) or a human (false)
     * @param alienColor the color of the alien if wantAlien is true; ignored if wantAlien is false
     * @param row the row position where the player should be placed
     * @param column the column position where the player should be placed
     */
    @Override
    public void addAlienOrHumansLMR(String playerNickname, boolean wantAlien, AlienColor alienColor, int row, int column) {
        synchronized (output) {
            try {
                output.writeObject("addAlienOrHumansLMR");
                output.writeObject(new Object[]{playerNickname, wantAlien, alienColor, row, column});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Updates the player's position on the flight board.
     *
     * @param playerNickname the nickname of the player whose position is to be updated
     * @param pos the new position of the player on the flight board
     */
    @Override
    public void setPlayerPosInFlightBoard(String playerNickname, int pos) {
        synchronized (output) {
            try {
                output.writeObject("setPlayerPosInFlightBoard");
                output.writeObject(new Object[]{playerNickname, pos});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ends the building process for the specified player and provides the next position to proceed.
     * The method sends information about the player's nickname and the target position,
     * ensuring the synchronization of the output object to handle multi-threaded environments.
     *
     * @param playerNickname the nickname of the player who is ending the building process
     * @param positionwheretogo the position where the player should move after finishing the building
     */
    @Override
    public void endbuilding(String playerNickname, int positionwheretogo) {
        synchronized (output) {
            try {
                output.writeObject("endbuilding");
                output.writeObject(new Object[]{playerNickname, positionwheretogo});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Signals the end of the building phase for all participants.
     * This method writes the message "endBuildingPhaseForAll" to the output stream
     * and ensures the message is sent by flushing the stream.
     * The operation is synchronized on the output object to provide thread safety.
     *
     * If an IOException occurs during the writing or flushing process, the exception is caught
     * and its stack trace is printed.
     *
     * Implementation of this method should ensure that multiple threads do not
     * concurrently access the output stream to avoid data corruption.
     */
    @Override
    public void endBuildingPhaseForAll() {
        synchronized (output) {
            try {
                output.writeObject("endBuildingPhaseForAll");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a penalty command with the specified penalty value and type.
     *
     * @param penalty the penalty value to send
     * @param type the type or reason for the penalty
     */
    @Override
    public void sendPenalty(int penalty, String type) {
        synchronized (output) {
            try {
                output.writeObject("sendPenalty");
                output.writeObject(new Object[]{penalty, type});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Activates the "Abandoned Ship Taken" event for the specified player with the given positions.
     * Sends the event details to the output stream for further processing.
     *
     * @param player The player for whom the event is activated.
     * @param posPers A 2D list representing positions corresponding to the event.
     *                Each inner list contains integers defining specific coordinates or references.
     */
    @Override
    public void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers) {
        synchronized (output) {
            try {
                output.writeObject("abandonedShipTakenActivate");
                output.writeObject(new Object[]{player, posPers});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the usage data for a given card by sending the relevant
     * information through the output stream.
     *
     * @param card the Card object that contains the necessary details to be updated
     */
    @Override
    public void updateCardUse(Card card) {
        synchronized (output) {
            try {
                output.writeObject("updateCardUse");
                output.writeObject(new Object[]{card});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the state of a card by sending the updated state to the output stream.
     *
     * @param card the c_State object representing the updated card state to be processed
     * @throws Exception if an error occurs during the write or flush operation
     */
    @Override
    public void updateCardUseSTATE(c_State card) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("updateCardUseSTATE");
                output.writeObject(new Object[]{card});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the current status by sending the given state to the output stream.
     * Ensures thread safety by synchronizing on the output object.
     *
     * @param stato the new status to be updated, represented as a value of the stateEnum enumeration
     */
    @Override
    public void updateStatus(stateEnum stato) {
        synchronized (output) {
            try {
                output.writeObject("updateStatus");
                output.writeObject(new Object[]{stato});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the final scores by sending the provided data to the output stream.
     *
     * @param finalScores a map containing the identifiers as keys and their corresponding scores as values
     */
    @Override
    public void updateFinalScores(HashMap<String, Float> finalScores) {
        synchronized (output) {
            try {
                output.writeObject("updateFinalScores");
                output.writeObject(new Object[]{finalScores});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the activation of an abandoned station by communicating the required parameters
     * and synchronizing the process. This method sends data through an output stream for
     * further processing or decision-making.
     *
     * @param player The player performing the action.
     * @param flightBoard The current state of the flight board.
     * @param yOn A boolean flag indicating a specific condition (true/false).
     * @param storagetiles A list of lists representing the storage tiles involved in the operation.
     * @param newgoods A list of lists representing the new goods to be processed or added.
     */
    @Override
    public void chooseAbandonedStationActivate(Player player, FlightBoard flightBoard, boolean yOn, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) {
        synchronized (output) {
            try {
                output.writeObject("chooseAbandonedStationActivate");
                output.writeObject(new Object[]{player, flightBoard, yOn, storagetiles, newgoods});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the positions of asserted batteries for a given player.
     * This method communicates the updated positions to an external output stream
     * in a synchronized manner to ensure thread safety.
     *
     * @param p                    the player for whom the battery positions are being updated
     * @param posBatAndNumBattXPos a nested list containing information about battery positions
     *                             and the corresponding number of batteries at each position
     */
    @Override
    public void updateAssertBatteriesPos(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) {
        synchronized (output) {
            try {
                output.writeObject("updateAssertBatteriesPos");
                output.writeObject(new Object[]{posBatAndNumBattXPos});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates and adds goods to the player's data and synchronizes it with the output stream.
     *
     * @param player    The player object associated with the goods update.
     * @param posGoods  A list of lists representing the positions of goods to be added or updated.
     * @param goodsSets A list of lists containing the actual goods objects to be added or updated.
     */
    @Override
    public void updateAddGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) {
        synchronized (output) {
            try {
                output.writeObject("updateAddGoods");
                output.writeObject(new Object[]{player, posGoods, goodsSets});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the system with the selection of passengers to lose in a given scenario.
     *
     * @param player The player involved in this operation.
     * @param c The consequences object containing details about the situation.
     * @param pass A nested list of integers representing the passengers to be chosen or affected.
     */
    @Override
    public void updateChoosePassengersToLose(Player player, Consequences c, ArrayList<ArrayList<Integer>> pass) {
        synchronized (output) {
            try {
                output.writeObject("updateChoosePassengersToLose");
                output.writeObject(new Object[]{player, c, pass});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Activates the planet financial status associated with a flight board and a list of players.
     *
     * @param playersss the list of players involved in the activation process
     * @param flightBoard the flight board used to manage and execute the activation
     */
    @Override
    public void planetFinStatActivate(ArrayList<Player> playersss, FlightBoard flightBoard) {
        synchronized (output) {
            try {
                output.writeObject("planetFinStatActivate");
                output.writeObject(new Object[]{playersss, flightBoard});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the consequence of lost days for a player based on a flight board.
     *
     * @param player The player whose lost days consequences are being updated.
     * @param flightBoard The flight board that determines the player's lost days.
     * @param numDays The number of days to be updated as lost.
     * @param t A flag indicating additional context for the update (e.g., true for specific handling, false otherwise).
     */
    @Override
    public void updateConsequenceLostDays(Player player, FlightBoard flightBoard, int numDays, Boolean t) {
        synchronized (output) {
            try {
                output.writeObject("updateConsequenceLostDays");
                output.writeObject(new Object[]{player, flightBoard, numDays, t});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the state of goods when all are lost. Sends an update to the output stream
     * with relevant details about the player status and game conditions.
     *
     * @param player the player object representing the current state of the player
     * @param finished a Boolean indicating if the game has finished
     * @param batttoloose a Boolean indicating if the battle is lost
     * @param allbatlost a Boolean indicating if all battles are lost
     */
    @Override
    public void updateLooseAllGoods(Player player, Boolean finished, Boolean batttoloose, Boolean allbatlost) {
        synchronized (output) {
            try {
                output.writeObject("updateLooseAllGoods");
                output.writeObject(new Object[]{player, finished, batttoloose, allbatlost});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the smuggler's calculations based on the provided player state, cannon positions,
     * and battery positions. This method communicates the updates to the output stream.
     *
     * @param player       The player instance containing relevant player data.
     * @param cannonPos    A nested list of integers representing the positions of cannons.
     * @param batteriesPos A nested list of integers representing the positions of batteries.
     * @throws Exception   If any exception occurs during the update process.
     */
    @Override
    public void updateSmugglersCalc(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("updateSmugglersCalc");
                output.writeObject(new Object[]{player, cannonPos, batteriesPos});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the lost batteries smug information by sending the relevant details through the output stream.
     *
     * @param p The player whose lost batteries smug data is being updated.
     * @param posBatAndNumBattXPos A 2D list containing the positions of the batteries and the number of batteries at each position.
     * @param numbatt The total number of batteries lost.
     */
    @Override
    public void updateLostBatteriesSmug(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, int numbatt) {
        synchronized (output) {
            try {
                output.writeObject("updateLostBatteriesSmug");
                output.writeObject(new Object[]{p, posBatAndNumBattXPos, numbatt});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the information of lost goods during smuggling for the provided player.
     * This method communicates with an external system to process the update.
     *
     * @param p the player for whom the lost goods update is being performed
     * @param posGoods a list of positions (in the form of integers) related to the goods to be updated
     * @param goodsSets a list of groups of goods categorized for processing
     * @param goodsListDiPrima a list of primary goods involved in the update
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateLostGoodsSmug(Player p, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, ArrayList<Goods> goodsListDiPrima) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("updateLostGoodsSmug");
                output.writeObject(new Object[]{p, posGoods, goodsSets, goodsListDiPrima});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the process for choosing to claim a smugglers' reward.
     *
     * @param yOn           A boolean indicating if the player chooses to claim the reward.
     * @param player        The player object initiating the action.
     * @param storagetiles  A 2D list of integers representing the storage tile structure.
     * @param newgoods      A 2D list of goods representing newly acquired items.
     * @throws Exception    If an error occurs during execution.
     */
    @Override
    public void chooseToClaimRewardSmug(boolean yOn, Player player, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("chooseToClaimRewardSmug");
                output.writeObject(new Object[]{yOn, player, storagetiles, newgoods});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the choice of cannon battery positions made by a player.
     * This method communicates the selected cannon and battery positions to the output stream.
     *
     * @param player The player making the selection.
     * @param cannonPos A list of positions selected for cannons.
     * @param batteriesPos A list of positions selected for batteries.
     * @throws Exception If an error occurs during the process.
     */
    @Override
    public void slaversChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("slaversChooseCannonBatteryPos");
                output.writeObject(new Object[]{player, cannonPos, batteriesPos});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the scenario where slavers choose passengers to lose during the game.
     *
     * @param player The player involved in the decision-making process.
     * @param yOn A boolean value indicating a specific condition or state related to the decision.
     * @param tiles A nested list of integers representing the tiles that may be affected.
     */
    @Override
    public void slaversChoosePassengersToLose(Player player, boolean yOn, ArrayList<ArrayList<Integer>> tiles) {
        synchronized (output) {
            try {
                output.writeObject("slaversChoosePassengersToLose");
                output.writeObject(new Object[]{player, yOn, tiles});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the selection of cannon battery positions by pirates.
     *
     * @param player The player instance representing the current player involved in the action.
     * @param cannonPos A nested list of integers representing the coordinates or positions of cannons.
     * @param batteriesPos A nested list of integers representing the coordinates or positions of batteries.
     * @throws Exception If an error occurs during the process of writing the objects or communication.
     */
    @Override
    public void piratesChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("piratesChooseCannonBatteryPos");
                output.writeObject(new Object[]{player, cannonPos, batteriesPos});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Allows pirates to decide how to defend against meteors based on given parameters.
     *
     * @param player The player who is making the decision.
     * @param howToDefenceFromShots A list of integers representing the available defense strategies.
     * @param shot The shot information used to determine the defense.
     * @param dice The dice value involved in the decision-making process.
     * @throws Exception If an error occurs during the operation.
     */
    @Override
    public void piratesChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, Shot shot, int dice) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("piratesChooseHowToFaceMeteors");
                output.writeObject(new Object[]{player, howToDefenceFromShots, shot, dice});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the action where pirates decide whether to claim a reward.
     * This method communicates with an output stream to notify about the action
     * taken and passes the relevant data.
     *
     * @param yOn a boolean indicating whether the pirates choose to claim the reward (true) or not (false)
     * @param player the Player object representing the individual or entity involved in the action
     * @throws Exception if any error occurs during the operation
     */
    @Override
    public void piratesChooseToClaimReward(boolean yOn, Player player) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("piratesChooseToClaimReward");
                output.writeObject(new Object[]{yOn, player});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Moves a player on the flight board to a specified position.
     *
     * @param playerNickname the nickname of the player to be moved
     * @param pos the position to which the player should be moved on the flight board
     */
    @Override
    public void movePlayerInFlightBoard(String playerNickname, int pos) {
        synchronized (output) {
            try {
                output.writeObject("movePlayerInFlightBoard");
                output.writeObject(new Object[]{playerNickname, pos});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initiates the motor start process in an open-space scenario based on the provided parameters.
     *
     * @param player        the player initiating the action
     * @param flightBoard   the flight board containing relevant game state and configurations
     * @param enginesPos    the positions of the engines in the game grid
     * @param batteriesPos  the positions of the batteries in the game grid
     * @throws Exception    if an error occurs during the operation
     */
    @Override
    public void openSpaceChooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("openSpaceChooseToStartMotor");
                output.writeObject(new Object[]{player, flightBoard, enginesPos, batteriesPos});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the decision-making process for determining how to face meteors in the game.
     * This method sends the relevant data regarding the player, defense choices, and shots to the output stream.
     *
     * @param player The player who is deciding how to face meteors.
     * @param howToDefenceFromShots An ArrayList of integers representing the defense strategies chosen by the player for each shot.
     * @param shots An ArrayList of Shot objects representing the incoming shots the player must defend against.
     * @param dice The dice roll result that may influence the player's decision.
     * @param currentShot The index of the current shot being processed.
     * @throws Exception If an error occurs during the data transmission process.
     */
    @Override
    public void meteorCardChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, ArrayList<Shot> shots, int dice, int currentShot) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("meteorCardChooseHowToFaceMeteors");
                output.writeObject(new Object[]{player, howToDefenceFromShots, shots, dice, currentShot});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Activates the epidemic state for a base, marking the process as visited
     * and performing necessary I/O operations to communicate the state.
     *
     * @param AlreadyVisited a set of SpaceShipTile objects indicating the tiles
     *                        that have already been visited during the epidemic state activation process.
     */
    @Override
    public void epidemicStateBaseActivate(Set<SpaceShipTile> AlreadyVisited) {
        synchronized (output) {
            try {
                output.writeObject("epidemicStateBaseActivate");
                output.writeObject(new Object[]{AlreadyVisited});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "stardustEffect" message to the output stream in a thread-safe manner.
     * The method synchronizes on the output object to ensure that only one thread
     * can perform the write operation at a time. It writes the message as an object
     * to the output stream and then flushes the stream to ensure the data is sent
     * immediately. If an IOException occurs during this process, it is caught and
     * its stack trace is printed.
     */
    @Override
    public void stardustEffect() {
        synchronized (output) {
            try {
                output.writeObject("stardustEffect");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the remaining goods associated with a player.
     *
     * @param p the player whose goods information is to be updated
     * @param goodFInali the list of goods to update for the given player
     */
    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) {
        synchronized (output) {
            try {
                output.writeObject("updateGoodsRemaining");
                output.writeObject(new Object[]{p, goodFInali});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "notYourTurn" message to the output stream to indicate that it is not
     * the recipient's turn in the current context. This method ensures thread-safe
     * access to the output stream by synchronizing on it.
     *
     * The method writes the message "notYourTurn" as an object to the output, flushes
     * the stream to guarantee transmission, and handles any potential IOExceptions
     * that may occur.
     */
    @Override
    public void notYourTurn() {
        synchronized (output) {
            try {
                output.writeObject("notYourTurn");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the scenario where a timer has not started and sends a
     * corresponding message over the output stream. The method writes
     * the string "timerNotStarted" to the output and ensures the stream
     * is flushed. If an I/O exception occurs during this process, the
     * exception is caught and its stack trace is printed to the error stream.
     *
     * This method is synchronized on the output object to ensure thread safety
     * during write operations.
     *
     * Note: The output object should be properly initialized and connected
     * to the intended recipient before calling this method to avoid runtime issues.
     */
    @Override
    public void timerNotStarted() {
        synchronized (output) {
            try {
                output.writeObject("timerNotStarted");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "tileNotFlipped" message through the output stream. This method
     * ensures thread-safe access to the output stream by synchronizing on it.
     * If an IOException occurs during the process of writing or flushing the
     * message, the stack trace of the exception is printed.
     *
     * The method is designed for scenarios where notification of the
     * "tileNotFlipped" event needs to be communicated.
     */
    @Override
    public void tileNotFlipped() {
        synchronized (output) {
            try {
                output.writeObject("tileNotFlipped");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "noSurrender" message through the output stream in a thread-safe manner.
     * The method is synchronized on the output object to ensure that multiple threads
     * do not interfere with each other when writing to the stream.
     * In case of an IOException during the write or flush operation, the exception stack trace
     * is printed to the standard error stream.
     */
    @Override
    public void noSurrender() {
        synchronized (output) {
            try {
                output.writeObject("noSurrender");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the transition to the next player's turn in a synchronized manner.
     * Sends a "nextPlayerTurn" signal via the output stream to notify the connected system.
     * Ensures thread safety by synchronizing on the output object.
     * If an I/O error occurs during writing or flushing the output stream, the error stack trace is printed.
     */
    @Override
    public void nextPlayerTurn() {
        synchronized (output) {
            try {
                output.writeObject("nextPlayerTurn");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "wrongInput" message to the output stream to indicate an invalid or unexpected input.
     * This method is synchronized to ensure thread safety when writing to the output stream.
     *
     * The method writes the string "wrongInput" as an object to the output stream and flushes
     * the stream to ensure the message is sent immediately. If an IOException occurs during the
     * operation, it prints the stack trace of the exception.
     *
     * This method overrides a parent class or interface definition.
     */
    @Override
    public void wrongInput() {
        synchronized (output) {
            try {
                output.writeObject("wrongInput");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "correctInput" message through the output stream in a thread-safe manner.
     * The method synchronizes on the output object to ensure that only one thread
     * can access it at a time. It writes the message using the writeObject method
     * of the output stream and flushes the stream to ensure the data is sent immediately.
     * If an IOException is thrown during the process, it is caught and its stack trace
     * is printed.
     */
    @Override
    public void correctInput() {
        synchronized (output) {

            {
                try {
                    output.writeObject("correctInput");
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Notifies the connected client that an invalid player action or turn has occurred.
     * This is achieved by sending a "wrongPlayer" message serialized as an object
     * through the output stream.
     *
     * The method is synchronized on the output stream to ensure thread safety
     * during communication. If an IOException occurs during the write or flush
     * operation, the stack trace is printed to the standard error stream.
     *
     * The primary purpose of this method is to communicate with the client
     * and inform them that the current player is not allowed to make the move,
     * or it is not their turn.
     */
    @Override
    public void wrongPlayer() {
        synchronized (output) {
            try {
                output.writeObject("wrongPlayer");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifies that an effect has started by sending the string "effectStarted"
     * to the output stream in a thread-safe manner. The method synchronizes on
     * the output object to ensure thread safety while writing to the stream.
     *
     * If an IOException occurs during the write or flush operation, the exception
     * stack trace is printed to the error stream.
     */
    @Override
    public void effectStarted() {
        synchronized (output) {
            try {
                output.writeObject("effectStarted");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifies that a player has won the game. This method sends a message
     * indicating the event and the player's name to the output stream.
     *
     * @param player the name of the player who has won the game
     */
    @Override
    public void someoneWon(String player) {
        synchronized (output) {
            try {
                output.writeObject("someoneWon");
                output.writeObject(new Object[]{player});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifies the output stream that a tie has occurred and includes the player information.
     *
     * @param player the player involved in the tie
     */
    @Override
    public void tie(String player) {
        synchronized (output) {
            try {
                output.writeObject("tie");
                output.writeObject(new Object[]{player});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifies that a player has lost the game.
     * Sends a "lost" message followed by the player's identifier to the output stream.
     *
     * @param player the identifier of the player who lost the game
     */
    @Override
    public void lost(String player) {
        synchronized (output) {
            try {
                output.writeObject("lost");
                output.writeObject(new Object[]{player});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a notification that a reward has been refused by a player.
     *
     * @param player The name of the player who refused the reward.
     */
    @Override
    public void refusedReward(String player) {
        synchronized (output) {
            try {
                output.writeObject("refusedReward");
                output.writeObject(new Object[]{player});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Executes logic to signal the end of an effect.
     *
     * This method writes a predefined message, "effectEnded",
     * to a synchronized output stream. It ensures thread safety
     * during the writing process. Upon successful completion,
     * the message is flushed to the output. If an IOException is
     * encountered during writing or flushing, the stack trace of
     * the exception is printed.
     */
    @Override
    public void effectEnded() {
        synchronized (output) {
            try {
                output.writeObject("effectEnded");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Executes the "lostGoods" operation. This method synchronizes on the output
     * object to ensure thread safety during communication. It writes a specific
     * operation identifier ("lostGoods") followed by an empty array of objects
     * to the output stream. The stream is then flushed to ensure data is sent.
     * In case of an IOException during the process, the stack trace is printed.
     */
    @Override
    public void lostGoods() {
        synchronized (output) {
            try {
                output.writeObject("lostGoods");
                output.writeObject(new Object[]{});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the message "youPayConsequences" along with an empty object array
     * to the synchronized output stream. The method ensures thread-safe access
     * to the output stream by synchronizing on it.
     *
     * If an IOException occurs during the write or flush operations, the
     * exception is caught and the stack trace is printed.
     *
     * This method overrides an implementation from a superclass or interface.
     */
    @Override
    public void youPayConsequences() {
        synchronized (output) {
            try {
                output.writeObject("youPayConsequences");
                output.writeObject(new Object[]{});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the state of the dice by sending the specified dice value through the output stream.
     *
     * @param dice the value of the dice to be updated
     */
    @Override
    public void updateDice(int dice) {
        synchronized (output) {
            try {
                output.writeObject("updateDice");
                output.writeObject(new Object[]{dice});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a notification indicating that a deposit cannot be made.
     * This method serializes and writes the "cannotDeposit" message
     * to the output stream, ensuring thread safety through synchronization.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void cannotDeposit() throws RemoteException {
        synchronized (output) {
            try {
                output.writeObject("cannotDeposit");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "cannotInsert" message to the output stream, indicating that an insert operation cannot be performed.
     * This method writes the message to the output stream in a synchronized block to ensure thread safety.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    @Override
    public void cannotInsert() throws RemoteException {
        synchronized (output) {
            try {
                output.writeObject("cannotInsert");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "cannotPick" message through the output stream to indicate that the pick operation cannot proceed.
     * This method is synchronized on the output object to ensure thread safety during the write and flush operations.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void cannotPick() throws RemoteException {
        synchronized (output) {
            try {
                output.writeObject("cannotPick");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles a "cannotFill" operation by writing the string "cannotFill" to the output stream.
     * This method is synchronized on the output object to ensure thread safety during write operations.
     *
     * @throws RemoteException if a remote communication error occurs during the execution.
     */
    @Override
    public void cannotFill() throws RemoteException {
        synchronized (output) {
            try {
                output.writeObject("cannotFill");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a "blocked" message to a remote output stream.
     *
     * This method synchronizes access to the output stream to ensure thread safety.
     * It writes the string "blocked" to the output stream and flushes the stream
     * to ensure the message is sent immediately.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void blocked() throws RemoteException {
        synchronized (output) {
            try {
                output.writeObject("blocked");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Terminates the building process by sending a "stopBuilding" signal
     * through the output stream. This method is synchronized on the
     * output object to ensure thread safety.
     *
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void stopBuilding() throws RemoteException {
        synchronized (output) {
            try {
                output.writeObject("stopBuilding");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called when a timer is started. It synchronizes on the output object,
     * writes the "timerStarted" message to the output stream, and flushes the stream.
     *
     * @throws RemoteException if a remote communication error occurs during the operation.
     */
    @Override
    public void timerStarted() throws RemoteException {
        synchronized (output) {
            try {
                output.writeObject("timerStarted");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifies the client that the timer has already started.
     * This method sends a message "timerAlreadyStarted" to the connected
     * client through the output stream.
     *
     * @throws RemoteException if a remote communication error occurs during
     *         the execution of this method.
     */
    @Override
    public void timerAlreadyStarted() throws RemoteException {
        synchronized (output) {
            try {
                output.writeObject("timerAlreadyStarted");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates and manages the state of a received shot, including defensive strategies and dice rolls.
     *
     * @param player                 the player who received the shot
     * @param shot                   the shot object containing details of the shot
     * @param howToDefenceFromShots  a list of integers representing the defense strategy against the shot
     * @param dice                   the result of the dice roll influencing the shot resolution
     */
    @Override
    public void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) {
        synchronized (output) {
            try {
                output.writeObject("updateManageShotReceived");
                output.writeObject(new Object[]{player, shot, howToDefenceFromShots, dice});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a message to prompt the player to choose a sub-ship.
     *
     * @param player   the player who will choose a sub-ship
     * @param subShips a list of sub-ship options available to the player, represented as a
     *                 2D list of SpaceShipTile objects
     * @throws Exception if there is an error during message transmission
     */
    @Override
    public void messageToChooseSubship(Player player, ArrayList<ArrayList<SpaceShipTile>> subShips) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("messageToChooseSubship");
                output.writeObject(new Object[]{player, subShips});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the process of selecting a subship for a specific player in the game.
     * This method sends the "ChooseSubship" command along with relevant parameters
     * to the output stream for processing.
     *
     * @param playerNickname the nickname of the player making the selection
     * @param subShips a list of lists representing the available subships
     * @param choice the index of the chosen subship
     * @param waste an integer value representing additional data or parameters associated with the player's choice
     * @throws Exception if an error occurs during the execution or communication process
     */
    @Override
    public void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int choice, int waste) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("ChooseSubship");
                output.writeObject(new Object[]{playerNickname, subShips, choice, waste});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes a single tile from the game board based on the specified parameters.
     *
     * @param playerNickname the nickname of the player performing the action
     * @param row the row index of the tile to be removed
     * @param column the column index of the tile to be removed
     * @param fromMistake a boolean indicating if the removal is due to a mistake
     * @param waste the amount of waste generated by the removal
     * @throws Exception if an error occurs during the tile removal process
     */
    @Override
    public void removeSingleTile(String playerNickname, int row, int column, boolean fromMistake, int waste) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("removeSingleTile");
                output.writeObject(new Object[]{playerNickname, row, column, fromMistake, waste});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a tile to the wait list for a specified player.
     * This method sends a request to the output stream with the player's nickname
     * and tile index to perform the required operations.
     *
     * @param playerNickname the nickname of the player who wants to add a tile to the wait list
     * @param TileIndex the index of the tile to be added to the wait list
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void addTileToWaitList(String playerNickname, int TileIndex) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("addTileToWaitList");
                output.writeObject(new Object[]{playerNickname, TileIndex});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a request to insert a wait tile with a specified index for a given player
     * by writing the operation and parameters to the output stream.
     *
     * @param playerNickname the nickname of the player for whom the wait tile is to be inserted
     * @param TileIndex the index of the tile to be inserted
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void insertWaitTileLMR(String playerNickname, int TileIndex) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("insertWaitTileLMR");
                output.writeObject(new Object[]{playerNickname, TileIndex});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a command to indicate that empty tiles need to be filled.
     * This method writes the command "haveToFillEmptyTiles" to an output stream
     * in a synchronized block to ensure thread safety.
     * The method handles potential IOExceptions by printing the stack trace.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    @Override
    public void haveToFillEmptyTiles() throws Exception {
        synchronized (output) {
            try {
                output.writeObject("haveToFillEmptyTiles");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Removes a player from the flightboard.
     *
     * @param playerNickname the nickname of the player to be removed from the flightboard
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removePlayerFromFlightboard(String playerNickname) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("removePlayerFromFlightboard");
                output.writeObject(new Object[]{playerNickname});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Handles the logic when slavers decide to claim or decline a reward.
     * This method sends the respective decision to the output stream.
     *
     * @param player The player making the choice regarding the reward.
     * @param accept A boolean indicating whether the reward is accepted (true) or declined (false).
     * @throws Exception If there is an issue with writing to the output stream.
     */
    @Override
    public void slaversChooseToClaimReward(Player player, boolean accept) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("slaversChooseToClaimReward");
                output.writeObject(new Object[]{player, accept});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a list of old games to the output stream for processing.
     *
     * @param oldGames the list of OldGame objects to be sent
     */
    @Override
    public void showGames(ArrayList<OldGame> oldGames) {
        synchronized (output) {
            try {
                output.writeObject("showGames");
                output.writeObject(new Object[]{oldGames});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifies about the insufficient quantity of goods by sending a specific
     * message ("notEnoughGoods") through the synchronized output stream.
     *
     * This method uses a synchronized block to ensure thread safety while writing
     * to the output stream. It attempts to send the notification message and flush
     * the stream. If an IOException occurs during this operation, it is caught
     * and the stack trace is printed.
     *
     * @throws Exception if any exception occurs during the execution of the method.
     */
    @Override
    public void notEnoughGoods() throws Exception {
        synchronized (output) {
            try {
                output.writeObject("notEnoughGoods");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the remaining batteries count for the specified player.
     *
     * @param p the player whose batteries count is to be updated
     * @param batt the new battery count to be set for the player
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateBatteriesRemaining(Player p, int batt) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("updateBatteriesRemaining");
                output.writeObject(new Object[]{p,batt});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes an alien from a specified position in the game environment.
     * This method communicates with an external system to perform the removal operation.
     *
     * @param username the name of the user performing the operation
     * @param r the row position of the alien to be removed
     * @param c the column position of the alien to be removed
     * @throws Exception if any error occurs during the operation
     */
    @Override
    public void removeAlien(String username, int r, int c) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("removeAlien");
                output.writeObject(new Object[]{username, r, c});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Handles the scenario where a player loses specific batteries during the game.
     * This method synchronizes access to the output stream and sends relevant
     * information about the lost batteries to the connected party.
     *
     * @param p the player who has lost the batteries
     * @param batteriesToAct a nested list containing the details of the batteries that were lost
     * @throws Exception if an error occurs during the execution of the method
     */
    @Override
    public void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("lostBatteries");
                output.writeObject(new Object[]{p, batteriesToAct});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Removes a block of SpaceShipTile objects associated with a specific player.
     *
     * @param block the ArrayList of SpaceShipTile objects to be removed
     * @param playerNickname the nickname of the player associated with the block
     */
    @Override
    public void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname) {
        synchronized (output) {
            try {
                output.writeObject("removeBlock");
                output.writeObject(new Object[]{block, playerNickname});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the event when a timer ends. Writes a predefined message and the timer status
     * to the output stream while ensuring thread safety.
     *
     * @param b a Boolean value indicating the timer status; true if the timer has ended, false otherwise
     * @throws Exception if an error occurs during the execution of the method
     */
    @Override
    public void timerEnded(Boolean b) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("timerEnded");
                output.writeObject(new Object[]{b});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends information about incorrect tiles along with user information to the output stream.
     *
     * @param pos the list of positions of the incorrect tiles
     * @param nick the nickname of the user associated with the incorrect tiles
     * @param nickEff an additional nickname or identifier for the user (unused in method logic)
     * @throws Exception if any unexpected error occurs during processing
     */
    @Override
    public void wrongTiles(ArrayList<Integer> pos, String nick, String nickEff) throws Exception {
        synchronized (output) {
            try {
                output.writeObject("wrongTiles");
                output.writeObject(new Object[]{pos, nick});
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

