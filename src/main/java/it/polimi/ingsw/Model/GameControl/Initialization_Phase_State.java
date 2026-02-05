package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Boards.ShipBoard_LevelII;
import it.polimi.ingsw.Model.Boards.ShipBoard_TestFlight;
import it.polimi.ingsw.Model.Boards.Utils_Boards.Ship_Colour;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;

public class Initialization_Phase_State extends GameState {

    /**
     * Indicates whether the construction phase has been completed.
     * This variable is utilized to track the state of the construction process within the initialization phase.
     */
    protected boolean finishedconstruction;
    /**
     * Represents the list of ship colors available during the initialization phase of the game.
     *
     * This list is populated with instances of the {@link Ship_Colour} enumeration, which defines
     * the possible colors of ships. It is used to track and manage the remaining colors that can
     * be assigned or utilized during the setup process of the game.
     *
     * The available colors must be initialized before they can be used in any game-related actions.
     * The exact management of this list, including adding or removing colors, is handled by the methods
     * within the containing class.
     */
    private final ArrayList<Ship_Colour> availablecolours = new ArrayList<>();

    /**
     * Constructs a new Initialization_Phase_State object, initializing the game state for the initialization phase.
     * Initializes the available ship colors and sets the construction status to not finished.
     *
     * @param game The game instance associated with the initialization phase state.
     */
    public Initialization_Phase_State(Game game) {
        super(game);
        this.finishedconstruction = false;
        this.availablecolours.add(Ship_Colour.RED);
        this.availablecolours.add(Ship_Colour.BLUE);
        this.availablecolours.add(Ship_Colour.YELLOW);
        this.availablecolours.add(Ship_Colour.GREEN);
    }

    /**
     * Allows a player to join the game by providing a nickname and specifying the number of players.
     * This method initializes the player's ship, assigns a color, and places specific game tiles
     * based on the color. Additionally, it sets up the player's flight board and transitions the
     * game to the next phase once all players have joined.
     *
     * @param nickname       the nickname of the player joining the game
     * @param NumOfPlayers   the total number of players in the game
     * @throws Exception     if there is an issue during the game setup or if necessary resources
     *                       (e.g., tiles) are not found
     */
    @Override
    public void JoinGame(String nickname, int NumOfPlayers) throws Exception {
        if (!this.game.isStarted()) {
            //mod 25 aprile
            this.game.setNumOfPlayers(NumOfPlayers);
            this.game.startGame();
            this.game.setStarted();
        }
        if (finishedconstruction) {
            ShipBoard s1 = new ShipBoard_LevelII(null, null);
            s1 = switch (game.getLevel()) {
                case 0 -> new ShipBoard_TestFlight(null, null);
                case 1 -> new ShipBoard_LevelII(null, null);
                default -> s1;
            };
            Player p1 = new Player(game.getPlayers().size(), nickname, s1, this.availablecolours.getFirst());

            SpaceShipTile tileToPick;
            switch (this.availablecolours.getFirst()) {
                case RED:
                    tileToPick = game.getGameTiles().stream()
                            .filter(tile -> tile.getID() == 52)
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Tile not found"));
                    s1.getShipMatrix()[2][3] = tileToPick;
                    game.getGameTiles().remove(tileToPick);
                    tileToPick.setAttachedShip(s1);
                    break;
                case BLUE:
                    tileToPick = game.getGameTiles().stream()
                            .filter(tile -> tile.getID() == 33)
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Tile not found"));
                    s1.getShipMatrix()[2][3] = tileToPick;
                    game.getGameTiles().remove(tileToPick);
                    tileToPick.setAttachedShip(s1);
                    break;
                case YELLOW:
                    tileToPick = game.getGameTiles().stream()
                            .filter(tile -> tile.getID() == 61)
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Tile not found"));
                    s1.getShipMatrix()[2][3] = tileToPick;
                    game.getGameTiles().remove(tileToPick);
                    tileToPick.setAttachedShip(s1);
                    break;
                case GREEN:
                    tileToPick = game.getGameTiles().stream()
                            .filter(tile -> tile.getID() == 34)
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Tile not found"));
                    s1.getShipMatrix()[2][3] = tileToPick;
                    game.getGameTiles().remove(tileToPick);
                    tileToPick.setAttachedShip(s1);
                    break;
            }
            this.availablecolours.remove(p1.getColour());
            s1.setMyPlayer(p1);
            this.game.addPlayer(p1);
            for (Player p : game.getPlayers()) {
                p.getMyShip().setFlightBoard(game.getGameFlightBoard());


            }
            if (this.game.getPlayers().size() == this.game.getNumOfPlayers()) {
                this.game.NextPhase();
            }
        }
    }


    /**
     * Builds the flight board during the initialization phase of the game.
     * This method is a placeholder and currently not implemented.
     *
     * @throws RuntimeException if called, as the operation is not supported.
     */
    public void buildFlightBoard() throws RuntimeException {
        System.out.println("Not supported yet.");
    }

    /**
     * Initializes and fills the game's bank with predefined quantities of resources.
     * Creates a new {@code Bank} instance containing 100 units each of yellow goods, red goods, green goods,
     * blue goods, and battery, and sets this instance as the game's bank using the {@code setGameBank} method.
     */
    public void fillBank() {
        Bank bank = new Bank(100, 100, 100, 100, 100);
        game.setGameBank(bank);
    }

    /**
     * Builds the set of tiles during the initialization phase of the game.
     * This method is responsible for generating or arranging the tiles needed
     * for the gameplay as part of the initialization sequence.
     *
     * Throws a {@code RuntimeException} indicating that the method is not currently supported.
     * This is likely a placeholder, and the implementation of this behavior
     * is expected in future development.
     *
     * @throws RuntimeException if the method is invoked without implementation.
     */
    public void buildTiles() throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }


    /**
     * Advances the game to the next state based on the number of players.
     * Removes specific game tiles from the game board as part of the state transition.
     *
     * Depending on the number of players in the game:
     * - For 2 players, it removes the tiles with IDs 61 and 34 from the game board.
     * - For 3 players, it removes the tile with ID 34 from the game board.
     *
     * If the specified tiles are not found, a RuntimeException is thrown.
     *
     * This method relies on the structure of the game and its tiles and assumes
     * that the game state and tile configuration are valid before invocation.
     */
    public void goNextState() {
        SpaceShipTile tileToPick, tiletopick2;
        switch (game.getNumOfPlayers()) {
            case 2:
                tileToPick = game.getGameTiles().stream()
                        .filter(tile -> tile.getID() == 61)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                tiletopick2 = game.getGameTiles().stream()
                        .filter(tile -> tile.getID() == 34)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                game.getGameTiles().remove(tileToPick);
                game.getGameTiles().remove(tiletopick2);
                break;
            case 3:
                tiletopick2 = game.getGameTiles().stream()
                        .filter(tile -> tile.getID() == 34)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                game.getGameTiles().remove(tiletopick2);
                break;
        }
    }

}
