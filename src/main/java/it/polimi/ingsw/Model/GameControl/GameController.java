package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameController implements Serializable {
    /**
     * Represents the current game instance managed by the GameController.
     * This variable is immutable and serves as the main point of interaction
     * for the game logic and associated operations handled by the GameController.
     */
    protected final Game game;
    /**
     * Manages the communication and events within the game controller.
     * The eventBus is utilized to publish and subscribe to game-related events,
     * facilitating interaction between components and listeners.
     *
     * This variable is declared as {@code final} to ensure it remains constant
     * throughout the lifecycle of the {@code GameController}.
     *
     * Used by various methods to post events related to gameplay actions or system updates.
     */
    protected final EventBus eventBus;

    /**
     * Constructs a new GameController object with the specified game instance and event bus.
     *
     * @param game      the instance of the Game associated with this controller
     * @param eventBus  the EventBus used to handle and propagate events within the game
     */
    public GameController(Game game, EventBus eventBus) {
        this.game = game;
        this.eventBus = eventBus;

    }

    /**
     * Retrieves the current instance of the game being managed by the GameController.
     *
     * @return the current Game instance.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Allows a player to join the game by providing a nickname and the number of players participating.
     * The method synchronizes on the game object, executes the join operation on the game's state,
     * and handles any exceptions that may occur during the process.
     *
     * @param NickName the nickname of the player joining the game
     * @param NumOfPlayers the total number of players participating in the game
     */
    public void JoinGame(String NickName, int NumOfPlayers) {
        synchronized (game) {
            try {
                game.getGameState().JoinGame(NickName, NumOfPlayers);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Activates a timer for the given player within the game's state.
     *
     * @param player the player for whom the timer should be activated
     */
    public void activateTimer(Player player) {
        synchronized (game) {
            try {
                game.getGameState().activateTimer(player);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Sets the specified tile number for the player and completes the building phase.
     *
     * This method synchronizes on the game instance to ensure thread safety and attempts to
     * set the tile number for the given player. If an error occurs during this process,
     * it handles exceptions by notifying errors through the event bus.
     *
     * @param playerid the identifier of the player for whom the tile is being set
     * @param posTile the position or number of the tile to set for the player
     * @throws RuntimeException if an unexpected error occurs during processing
     */
    public void setNumTile(int playerid, int posTile) {
        synchronized (game) {
            try {
                game.getGameState().setNumTile(playerid, posTile);
                game.getGameState().completeBuildingPhase();
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Executes the "fill tile" command for the specified player, optionally using an alien of a specific color,
     * at the specified tile location (row and column). This method ensures game state updates and handles errors
     * that may occur during execution.
     *
     * @param playerid   the unique identifier of the player performing the action
     * @param wantalien  a boolean indicating whether the player wants to involve an alien in the action
     * @param color      the color of the alien to be used (if {@code wantalien} is {@code true})
     * @param row        the row position of the tile to fill
     * @param column     the column position of the tile to fill
     */
    public void CommandFillTile(int playerid, boolean wantalien, AlienColor color, int row, int column) {
        synchronized (game) {
            try {
                game.getGameState().CommandFillTile(playerid, wantalien, color, row, column);
                game.getGameState().completeBuildingPhase();
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Allows a player to surrender from the game. This method synchronizes on the game object
     * to ensure thread safety while accessing the game state and related mechanisms. If an error occurs during
     * the surrender process, it handles exceptions by notifying the event bus of the error.
     *
     * @param playerID the unique identifier of the player who wants to surrender
     */
    public void Surrend(int playerID) {
        synchronized (game) {
            try {
                game.getGameState().Surrend(playerID);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Provides the final scores of the game by retrieving and visualizing them from the game state.
     * Handles any runtime exceptions that may occur during the process by logging the error
     * and triggering an error notification through the event bus. If an additional failure occurs
     * during error handling, it rethrows the exception as a {@code RuntimeException}.
     *
     * @return a {@code HashMap} where keys are player identifiers as {@code String} and
     *         values are their corresponding scores represented as {@code Float}. Returns {@code null}
     *         if an exception prevents the scores from being retrieved.
     */
    public HashMap<String, Float> visualizeFinalScores() {
        synchronized (game) {
            try {
                return game.getGameState().visualizeFinalScores();
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            return null;
        }
    }

    /**
     * Handles the selection of a tile that has already been flipped in the game.
     * Updates the game state and handles any game-related errors during the operation.
     *
     * @param index the index of the tile to select
     * @param playerID the ID of the player making the selection
     * @throws Exception if an error occurs during the process
     */
    public void pickTileAlreadyFlipped(int index, int playerID) throws Exception {
        synchronized (game) {
            try {
                game.getGameState().pickTileAlreadyFlipped(index, playerID);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Allows a player to pick an unknown tile in the game.
     *
     * The method interacts with the game's current state to perform the tile
     * selection for the specified player. This action is synchronized on the
     * game object to ensure thread safety during execution. It handles
     * potential exceptions that may occur during the tile-picking process.
     *
     * @param playerID the unique identifier of the player attempting to pick a tile
     * @throws Exception if an underlying error occurs during the tile-picking process
     */
    public void pickTileUnknown(int playerID) throws Exception {
        synchronized (game) {
            try {
                game.getGameState().pickTileUnknown(playerID);
                System.out.println("tile picked");
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Allows a player to pick a little deck in the game by specifying the deck index and the player's ID.
     * Ensures thread-safety by synchronizing on the game object. Handles potential errors that may
     * occur during the operation by logging and handling exceptions properly.
     *
     * @param index the index of the little deck to be picked
     * @param playerID the unique identifier of the player attempting to pick the little deck
     * @throws Exception if an error occurs during the operation, including runtime, remote, or internal exceptions
     */
    public void pickLittleDeck(int index, int playerID) throws Exception {
        synchronized (game) {
            try {
                game.getGameState().pickLittleDeck(index, playerID);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the deposit of the little deck for a specific player.
     * Ensures thread-safe execution while interacting with the game state.
     * If any runtime or remote exceptions occur, they are properly logged and handled.
     *
     * @param playerID the identifier of the player depositing the little deck
     * @throws Exception if any errors occur during the deposit process
     */
    public void depositLittleDeck(int playerID) throws Exception {
        synchronized (game) {
            try {
                game.getGameState().depositLittleDeck(playerID);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Executes the deposit tile action for the specified player.
     * This method interacts with the game state to allow a player
     * to deposit a tile during the game. It also handles errors that
     * may occur in the process.
     *
     * @param playerID the unique identifier of the player performing the action
     * @throws Exception if an error occurs during the deposit tile process
     */
    public void depositTile(int playerID) throws Exception {
        synchronized (game) {
            try {
                game.getGameState().depositTile(playerID);
                System.out.println("tile deposited");
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Ends the building phase for a specific player and navigates to the specified position.
     *
     * @param PlayerID the identifier of the player ending the building phase
     * @param positionwheretogo the position to navigate to after ending the building phase
     */
    public void endbuilding(int PlayerID, int positionwheretogo) {
        synchronized (game) {
            try {
                game.getGameState().endbuilding(PlayerID, positionwheretogo);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Inserts a tile into the game state at the specified position with the given rotation and player ID.
     * This method is synchronized to ensure thread safety when interacting with the game state.
     * Handles exceptions by notifying an error event through the event bus.
     *
     * @param row      The row where the tile is to be inserted.
     * @param col      The column where the tile is to be inserted.
     * @param playerID The ID of the player attempting to insert the tile.
     * @param rotation The rotation state of the tile to be inserted.
     * @throws Exception If an unforeseen issue occurs during the insertion or error notification handling.
     */
    public void insertTile(int row, int col, int playerID, int rotation) throws Exception {
        synchronized (game) {
            try {
                game.getGameState().insertTile(row, col, playerID, rotation);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Activates the effect processing in the current game state.
     *
     * This method retrieves the current game state and attempts to activate its effect.
     * If a runtime exception occurs during the activation process, an error message
     * is printed to the console and the event bus is notified of the error. If any error
     * arises in the event notification step, a {@link RuntimeException} is thrown.
     *
     * The method ensures thread-safety by synchronizing on the shared {@code game} object.
     *
     * Throws an unchecked {@link RuntimeException} in cases where an unexpected exception
     * occurs during the invocation or error handling process.
     */
    public void EffectActivation() {
        synchronized (game) {
            try {
                game.getGameState().EffectActivation();
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Retrieves the currently active card being used in the game.
     * This method ensures thread safety by synchronizing on the game instance.
     * If an error occurs while accessing the card, it logs the error message and
     * notifies the event bus about the error.
     *
     * @return the active card in use, or null if an error occurs during retrieval
     */
    public Card getCardinuse() {
        synchronized (game) {
            try {
                return game.getGameState().getCardinuse();
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                return null;
            }
        }
    }


    /**
     * Selects one sub-ship based on the given index and player ID.
     * This method ensures thread safety by synchronizing access to the game's state.
     * If an error occurs during the selection process, it handles the exception and attempts
     * to notify listeners of the error via the event bus.
     *
     * @param index the index of the sub-ship to choose
     * @param playerID the ID of the player making the selection
     */
    public void chooseOneSubShip(int index, int playerID) {
        synchronized (game) {
            try {
                game.getGameState().chooseOneSubShip(index, playerID);

            } catch (RuntimeException | RemoteException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Handles the player's request to land on a specific planet in the game.
     * Checks if the player's action is allowed and processes the request accordingly.
     *
     * @param p The player requesting to land on a planet.
     * @param yOn A boolean indicating whether the player accepts certain conditions for landing on the planet.
     * @param NumPlanet The number identifying the planet the player wants to land on.
     */
    public void acceptToLandOnAPlanet(Player p, boolean yOn, int NumPlanet) {
        synchronized (game) {
            try {
                if (p.isBlocked()) {
                    eventBus.wrongInput(p);
                } else {
                    game.getGameState().acceptToLandOnAPlanet(p, yOn, NumPlanet);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Handles the selection of an abandoned station by a player during the game.
     *
     * @param player The player making the selection.
     * @param flightBoard The flight board object that tracks the game's flight data.
     * @param yOn A boolean value indicating a specific game state or condition.
     * @param storageTiles A 2D list representing the arrangement of storage tiles.
     * @param newGoods A 2D list representing the new goods available for placement on the storage tiles.
     */
    public void chooseAbandonedStation(Player player, FlightBoard flightBoard, boolean yOn, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods) {
        synchronized (game) {
            try {
                if (player.isBlocked()) {
                    eventBus.wrongInput(player);
                } else {
                    game.getGameState().chooseAbandonedStation(player, flightBoard, yOn, storageTiles, newGoods);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the selection of cannon and battery positions on the flight board
     * for a specific player during the game. If the player is blocked, their
     * input is rejected. Any exceptions encountered are logged and managed
     * appropriately.
     *
     * @param fb the flight board where positions are to be chosen
     * @param player the player making the decision
     * @param cannonPos a list of positions for placing cannons
     * @param batteriesPos a list of positions for placing batteries
     */
    public void chooseCannonBatteryPos(FlightBoard fb, Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) {
        synchronized (game) {
            try {
                if (player.isBlocked()) {
                    eventBus.wrongInput(player);
                } else {
                    game.getGameState().chooseCannonBatteryPos(fb, player, cannonPos, batteriesPos);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the logic for a player to choose how to respond to incoming meteors during the game.
     * If the player is blocked, an error is reported through the eventBus.
     * Otherwise, the game state processes the player's defense actions.
     *
     * @param player The player who is making the choice on how to face the meteors.
     * @param howToDefenceFromShots A list of integers representing the player's selected defenses against the meteors.
     * @param flightBoard The flight board that provides context for the defense choices during the game.
     */
    public void chooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, FlightBoard flightBoard) {
        synchronized (game) {
            try {
                if (player.isBlocked()) {
                    eventBus.wrongInput(player);
                } else {
                    game.getGameState().chooseHowToFaceMeteors(player, howToDefenceFromShots, flightBoard);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Facilitates the process of selecting the passengers to remove or lose based on the game state
     * and player input. This method ensures thread safety while interacting with the underlying game
     * system and handles any errors that may occur during operation.
     *
     * @param p The player attempting to choose passengers to lose.
     * @param yOn A boolean indicating a specific condition related to the passenger selection process.
     * @param pass A nested list of integers representing the passengers to be selected or affected.
     * @param flightBoard The current state of the flight board used in the decision-making process.
     */
    public void choosePassengersToLose(Player p, boolean yOn, ArrayList<ArrayList<Integer>> pass, FlightBoard flightBoard) {
        synchronized (game) {
            try {
                if (p.isBlocked()) {
                    eventBus.wrongInput(p);
                } else {
                    game.getGameState().choosePassengersToLose(p, yOn, pass, flightBoard);
                }

            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the player's decision to claim a reward during the game.
     * If the player is blocked, the method sends a wrong input event.
     * Otherwise, it delegates the reward processing to the game state.
     *
     * @param yOn a boolean flag indicating whether the player wants to claim the reward (true) or not (false)
     * @param player the player who is making the choice to claim the reward
     */
    public void chooseToClaimReward(boolean yOn, Player player) {
        synchronized (game) {
            try {
                if (player.isBlocked()) {
                    eventBus.wrongInput(player);
                } else {
                    game.getGameState().chooseToClaimReward(yOn, player);
                }

            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Allows a player to claim a reward during the game by interacting with the given flight board and updating
     * their storage tiles and collection of goods. This process is synchronized to ensure thread safety.
     *
     * @param fb the flight board used to determine the context of the reward claim.
     * @param yOn boolean indicating whether a specific condition is active during the claim.
     * @param player the player attempting to claim the reward.
     * @param storageTiles a list of lists representing the player's storage tiles affected by the claim.
     * @param newGoods a list of lists representing the goods to be added to the player's inventory.
     */
    public void chooseToClaimReward(FlightBoard fb, boolean yOn, Player player, ArrayList<ArrayList<Integer>> storageTiles, ArrayList<ArrayList<Goods>> newGoods) {
        synchronized (game) {
            try {
                if (player.isBlocked()) {
                    eventBus.wrongInput(player);
                } else {
                    game.getGameState().chooseToClaimReward(fb, yOn, player, storageTiles, newGoods);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Allows the player to choose positions to place batteries during the gameplay.
     * If the player is blocked, an incorrect input event is triggered.
     * On valid input, the game state is updated with the chosen battery positions.
     * Handles runtime exceptions and notifies errors through the event bus if they occur.
     *
     * @param p The player making the battery placement decision.
     * @param posBatAndNumBattXPos A nested list of integers containing positions of batteries
     *                             and corresponding number of batteries to be placed.
     */
    public void chooseToPlaceBatteries(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) {
        synchronized (game) {
            try {
                if (p.isBlocked()) {
                    eventBus.wrongInput(p);
                } else {
                    game.getGameState().chooseToPlaceBatteries(p, posBatAndNumBattXPos);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Allows the player to initiate the firing power process by specifying
     * the required parameters for double fire triplets and the batteries
     * to be activated.
     *
     * @param p the player who is attempting to start the firepower process
     * @param DoubFireTriplets a nested list of integers representing the
     *        double fire triplets configurations chosen by the player
     * @param BatteriesToAct a nested list of integers representing the
     *        positions of the batteries that the player wants to activate
     */
    public void chooseToStartFirePower(Player p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct) {
        synchronized (game) {
            try {
                if (p.isBlocked()) {
                    eventBus.wrongInput(p);
                } else {
                    game.getGameState().chooseToStartFirePower(p, DoubFireTriplets, BatteriesToAct);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Allows the specified player to choose to start the motor during the game.
     * This action involves specifying positions for engines and batteries.
     *
     * @param player        The player who is selecting to start the motor.
     * @param flightBoard   The flight board on which the game is being played.
     * @param enginesPos    A list of positions representing the engine placements.
     * @param batteriesPos  A list of positions representing the battery placements.
     */
    public void chooseToStartMotor(Player player, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) {
        synchronized (game) {
            try {
                if (player.isBlocked()) {
                    eventBus.wrongInput(player);
                } else {
                    game.getGameState().chooseToStartMotor(player, flightBoard, enginesPos, batteriesPos);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Handles the selection of where to place goods for a player in the game.
     * Invokes the appropriate game state logic or notifies the event bus in case of invalid inputs.
     * Synchronizes on the game instance to ensure thread safety.
     *
     * @param player the player who is choosing where to place the goods
     * @param posGoods the positions where the goods are to be placed, represented as nested lists of integers
     * @param goodsSets the sets of goods being placed, represented as nested lists of goods objects
     */
    public void chooseWhereToPutGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) {
        synchronized (game) {
            try {
                if (player.isBlocked()) {
                    eventBus.wrongInput(player);
                } else {
                    game.getGameState().chooseWhereToPutGoods(player, posGoods, goodsSets);
                }
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Inserts a wait tile for a specific player at the specified position with a given rotation.
     * The action is performed in a synchronized block for thread safety and includes error handling.
     *
     * @param playerID the ID of the player for whom the tile is being inserted
     * @param index the index of the tile to be inserted
     * @param row the row position where the tile should be placed
     * @param col the column position where the tile should be placed
     * @param rotation the rotation of the tile (e.g., 0, 90, 180, or 270 degrees)
     * @throws Exception if any error occurs during the tile insertion or event notification
     */
    public void insertWaitTile(int playerID, int index, int row, int col, int rotation) throws Exception { //rotation is 0, 90, 180,270 ecc..
        synchronized (game) {
            try {
                game.getGameState().insertWaitTile(playerID, index, row, col, rotation);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds a wait tile for the specified player in the game's state. This method ensures thread safety
     * by synchronizing on the game object. If an error occurs during the process, it handles the error
     * by logging the exception and notifying an event bus about the error condition.
     *
     * @param playerID the unique identifier of the player for whom the wait tile is to be added
     * @throws Exception if an unrecoverable error occurs while adding the wait tile
     */
    public void addWaitTile(int playerID) throws Exception {
        synchronized (game) {
            try {
                game.getGameState().addWaitTile(playerID);
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Demonstrates the state or gameplay of the current game object.
     * This method synchronizes on the game object to ensure that the game state is accessed
     * in a thread-safe manner. It performs the demo operation on the game state and handles
     * runtime exceptions or other exceptions that may occur during this process.
     *
     * If a RuntimeException occurs, it logs the error message to the console and notifies
     * the event bus of the error. If any exception is thrown during this notification,
     * it is rethrown as a RuntimeException. Any other exceptions that occur are also
     * wrapped and rethrown as RuntimeExceptions.
     */
    public void demoGame() {
        synchronized (game) {
            try {
                game.getGameState().demoGame();
            } catch (RuntimeException e) {
                try {
                    System.out.println("errore: " + e.getMessage());
                    eventBus.notifyError();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
