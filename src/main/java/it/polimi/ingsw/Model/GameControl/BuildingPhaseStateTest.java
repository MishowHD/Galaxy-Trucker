package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import it.polimi.ingsw.Utils.stateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuildingPhaseStateTest extends Building_Phase_State {

    /**
     * Represents the number of players who have completed their current phase
     * in the building phase state. This variable is used to track progress and
     * potentially determine when the phase should transition to the next state.
     */
    private int endedplayers;
    /**
     * Represents the current count of players who are ready to proceed to the next phase
     * during the building phase of the game.
     *
     * This variable helps in tracking how many players have indicated their readiness
     * to move to the next step of the game flow. It is modified through the associated
     * `setreadytogo` method and is evaluated in the context of game progression logic.
     */
    private int readytogo;

    /**
     * Constructs a new BuildingPhaseStateTest object and initializes it with the given game instance.
     *
     * @param game the Game instance associated with this state
     */
    public BuildingPhaseStateTest(Game game) {
        super(game);
        endedplayers = 0;
        readytogo = -1;
    }
    /**
     * Constructs a demo spaceship configuration for player 0 by sequentially selecting tiles,
     * placing them on the board in predefined positions and orientations, and marking the
     * completion of the building phase for the player.
     *
     * The method makes use of the following actions:
     * - `pickTile`: Selects specific tiles by their unique IDs.
     * - `insertTile`: Places the selected tiles at specified row and column positions with a given rotation.
     * - `endbuilding`: Signals the end of the building phase for the player, assigning their position on the board.
     *
     * This method is primarily intended for testing purposes, simulating a pre-built spaceship setup
     * to validate logic or game behaviors.
     *
     * @throws Exception If any issue arises during the tile selection, placement, or completion process.
     */
    public void buildDemoShip1() throws Exception {
        pickTile(149, 0);
        insertTile(2, 2, 0, 180);

        pickTile(16, 0);
        insertTile(2, 1, 0, 270);


        pickTile(6, 0);
        insertTile(1, 2, 0, 270);


        pickTile(15, 0);
        insertTile(1, 3, 0, 180);

        pickTile(98, 0);
        insertTile(3, 3, 0, 0);

        pickTile(62, 0);
        insertTile(3, 2, 0, 0);

        pickTile(32, 0);
        insertTile(3, 1, 0, 270);


        pickTile(11, 0);
        insertTile(4, 1, 0, 0);

        pickTile(74, 0);
        insertTile(4, 2, 0, 0);

        pickTile(154, 0);
        insertTile(2, 4, 0, 0);

        pickTile(41, 0);
        insertTile(3, 4, 0, 270);


        pickTile(31, 0);
        insertTile(1, 4, 0, 180);



        pickTile(44, 0);
        insertTile(2, 5, 0, 270);

        pickTile(135, 0);
        insertTile(4, 4, 0, 90);


        //player 0


        endbuilding(0,1);


    }
    /**
     * Constructs a demo ship for testing or demonstration purposes by selecting tiles
     * and placing them at specified locations on the game board with specific rotations.
     * This method utilizes various helper methods such as `pickTile` to select a tile
     * and `insertTile` to place the selected tile into the spaceship. The building process
     * concludes with the invocation of `endbuilding`.
     *
     * The method also includes commented-out code representing alternative construction
     * scenarios or optional changes in the ship configuration.
     *
     * Workflow:
     * - Select tiles using the `pickTile` method, which identifies a tile by its unique index.
     * - Place the selected tiles in specified positions on the board using the `insertTile` method.
     * - End the building phase for the player using the `endbuilding` method with appropriate parameters.
     *
     * Error Handling:
     * - Any exceptions thrown during tile selection or placement are propagated to the caller.
     * - The method ensures only valid tiles and placements are used based on the game rules.
     *
     * Assumptions:
     * - Player IDs and tile indices are valid and correspond to correct game states.
     * - The method is executed within the proper phase of the game.
     *
     * @throws Exception If any issues occur during tile selection, placement, or concluding the building phase.
     */
    public void buildDemoShip2() throws Exception {
        pickTile(153, 1);
        insertTile(2, 2, 1, 270);

        pickTile(60, 1);
        insertTile(3, 2, 1, 0);

        pickTile(37, 1);
        insertTile(4, 2, 1, 270);


        pickTile(59, 1);
        insertTile(2, 1, 1, 270);



        pickTile(19, 1);
        insertTile(3, 1, 1, 0);

        pickTile(7, 1);
        insertTile(1, 2, 1, 180);


        pickTile(2, 1);
        insertTile(1, 3, 1, 180);

        pickTile(85, 1);
        insertTile(3, 3, 1, 0);


        pickTile(25, 1);
        insertTile(3, 4, 1, 270);

        pickTile(79, 1);
        insertTile(4, 4, 1, 0);

        pickTile(57, 1);
        insertTile(3, 5, 1, 270);








        pickTile(65, 1);
        insertTile(2, 4, 1, 270);//ok

        pickTile(67, 1);
        insertTile(4, 5, 1, 0);


        pickTile(126, 1);
        insertTile(2, 5, 1, 0);
        endbuilding(1,2);

        //choose alien


    }
    /**
     * Constructs and sets up the ship placement for Player 3 during the building phase
     * by selecting and placing spaceship tiles on the game board.
     *
     * This method programmatically simulates the process of picking specific spaceship tiles,
     * determining their positions, and inserting them into predetermined coordinates on the player's
     * shipboard with specific rotations. After placing all tiles, the method marks the building phase
     * as completed for Player 3 and specifies the next stage for the player.
     *
     * The method utilizes the following operations:
     * - Selecting tiles via the `pickTile` method.
     * - Placing tiles on the game board via the `insertTile` method, specifying the row, column,
     *   rotation, and player ID.
     * - Concluding the building process by invoking the `endbuilding` method.
     *
     * Exceptions will be thrown if any invalid actions occur during the tile picking or placement stages,
     * such as selecting an unavailable tile, placing tiles on invalid coordinates, or other internal game
     * state validation issues.
     *
     * @throws Exception If issues arise during tile picking, placement, or termination of the building phase.
     */
    public void buildDemoShip3() throws Exception{
        //PLAYER 3 SHIPBOARD
        pickTile(18, 2);
        insertTile(2, 2, 2, 270);

        pickTile(63, 2);
        insertTile(2, 1, 2, 0);

        pickTile(111, 2);
        insertTile(3, 3, 2, 180);

        pickTile(47, 2);
        insertTile(3, 2, 2, 0);

        pickTile(23, 2);
        insertTile(3, 1, 2, 0);




        pickTile(56, 2);
        insertTile(1, 2, 2, 270);


        pickTile(40, 2);
        insertTile(1, 3, 2, 90);

        pickTile(156, 2);
        insertTile(2, 4, 2, 0);

        pickTile(42, 2);
        insertTile(3, 4, 2, 90);

        pickTile(97, 2);
        insertTile(4, 4, 2, 0);

        pickTile(58, 2);
        insertTile(2, 5, 2, 90);

        pickTile(5, 2);
        insertTile(3, 5, 2, 0);

        pickTile(89, 2);
        insertTile(4, 5, 2, 0);

        pickTile(132, 2);
        insertTile(0, 3, 2, 270);

        pickTile(1, 2);
        insertTile(1, 4, 2, 270);
        endbuilding(2,3);
    }
    /**
     * Constructs a demonstration spaceship layout for player 4 by picking and inserting various tiles
     * onto the player's game board in predefined positions and orientations.
     * The method follows a specific sequence of operations to assemble the ship and finalizes the building phase.
     *
     * The method performs the following steps:
     * 1. Picks specific tiles from the available pool using their unique identifiers.
     * 2. Inserts these tiles on the game board at predefined positions with specified rotations.
     * 3. Ends the building phase for the player once the ship construction is completed.
     *
     * Preconditions:
     * - The player must be in the building phase and must not have completed it already.
     * - The required tiles must be available in the game's tile pool at their respective indices.
     * - Players must adhere to game rules for tile placement and rotation.
     *
     * Postconditions:
     * - The player's spaceship will be configured with the pre-defined structure for demonstration purposes.
     * - The building phase for the player is marked as completed.
     *
     * @throws Exception If any error occurs during tile picking, insertion, or game state processing.
     */
    public void buildDemoShip4() throws Exception{
        pickTile(3, 3);
        insertTile(2, 2, 3, 270);

        pickTile(49, 3);
        insertTile(2, 1, 3, 270);


        pickTile(134, 3);
        insertTile(3, 3, 3, 180);

        pickTile(30, 3);
        insertTile(3, 2, 3, 90);

        pickTile(64, 3);
        insertTile(3, 1, 3, 180);


        pickTile(96, 3);
        insertTile(4, 1, 3, 0);

        pickTile(90, 3);
        insertTile(4, 2, 3, 0);




        pickTile(36, 3);
        insertTile(1, 3, 3, 180);

        pickTile(13, 3);
        insertTile(1, 4, 3, 270);



        pickTile(152, 3);
        insertTile(2, 4, 3, 0);

        pickTile(54, 3);
        insertTile(2, 5, 3, 90);


        pickTile(55, 3);
        insertTile(3, 4, 3, 0);

        pickTile(95, 3);
        insertTile(4, 4, 3, 0);

        pickTile(119, 3);
        insertTile(4, 5, 3, 90);

        pickTile(66, 3);
        insertTile(3, 5, 3, 90);


        pickTile(116, 3);
        insertTile(0, 3, 3, 0);


        endbuilding(3,4);
    }
    /**
     * Simulates the demo phase of the game by initializing and building ships for players
     * based on the current number of players in the game.
     *
     * This method invokes predefined ship-building processes for each player
     * depending on the size of the player list. Specifically:
     * - For all games, it builds ships for player 0 and player 1.
     * - If the game includes 3 players, it builds a ship for player 2.
     * - If the game includes 4 players, it builds ships for both player 2 and player 3.
     *
     * @throws Exception if an error occurs during the ship-building process.
     */
    public void demoGame() throws Exception {

        buildDemoShip1();
        buildDemoShip2();
        //player 1
        if(game.getPlayers().size()==3) {
            buildDemoShip3();
        }
        if(game.getPlayers().size()==4){
            buildDemoShip3();
            buildDemoShip4();
        }

    }

    /**
     * Allows a player to pick a flipped tile during the building phase if certain conditions are met.
     *
     * @param index the unique identifier of the tile to be picked
     * @param playerID the unique identifier of the player attempting to pick the tile
     * @throws Exception if the tile is not found, or any other validation checks fail during the process
     */
    @Override
    public void pickTileAlreadyFlipped(int index, int playerID) throws Exception {//6
        Player player = game.getPlayer(playerID);
        SpaceShipTile tileToPick = game.getGameTiles().stream()
                .filter(tile -> tile.getID() == index)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tile not found for index: " + index));
        if (!tileToPick.isFlipped()) {
            game.getEventBus().cannotPick(player);
        } else if (player.hasSomethingInHand()) {
            game.getEventBus().cannotPick(player);
        } else if (player.isBlocked()) {
            game.getEventBus().cannotPick(player);
        } else if (isTileintheHandofOther(tileToPick, playerID)) {
            game.getEventBus().cannotPick(player);
        } else {
            game.getEventBus().pickTileLMR(index, player.getUsername());
            game.getGameSSTIH().add(tileToPick);
            player.pickTilePlayer(tileToPick);
        }
    }

    /**
     * Allows a player to pick a tile from the game based on specified conditions.
     *
     * @param index the index of the tile to be picked
     * @param playerID the unique ID of the player attempting to pick the tile
     * @throws Exception if the tile is not found or constraints prevent the player from picking it
     */
    @Override
    public void pickTile(int index, int playerID) throws Exception {
        Player player = game.getPlayer(playerID);
        SpaceShipTile tileToPick = game.getGameTiles().stream()
                .filter(tile -> tile.getID() == index)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tile not found for index: " + index));
        if (player.hasSomethingInHand()) {
            game.getEventBus().cannotPick(player);
        } else if (player.isBlocked()) {
            game.getEventBus().cannotPick(player);
        } else if (isTileintheHandofOther(tileToPick, playerID)) {
            game.getEventBus().cannotPick(player);
        } else {
            game.getEventBus().pickTileLMR(index, player.getUsername());
            tileToPick.SetFlip(true);
            game.getGameSSTIH().add(tileToPick);
            player.pickTilePlayer(tileToPick);
        }
    }

    /**
     * Allows a player to pick a random tile from the list of available tiles, excluding certain invalid tiles.
     * The method ensures that the player meets the conditions required for picking a tile, such as not being
     * blocked or already holding a tile in hand. Based on the number of players in the game, different rules
     * are applied to filter the available tiles. If a valid tile is found, it is picked and marked as flipped;
     * otherwise, the player is notified that picking is not possible.
     *
     * @param playerID the unique identifier of the player who is attempting to pick a tile
     * @throws Exception if an error occurs during the tile-picking process
     */
    @Override
    public void pickTileUnknown(int playerID) throws Exception { //7
        Player player = game.getPlayer(playerID);
        if (player.hasSomethingInHand()) {
            game.getEventBus().cannotPick(player);
            return;
        } else if (player.isBlocked()) {
            game.getEventBus().cannotPick(player);
            return;
        }
        List<SpaceShipTile> availableTiles = switch (game.getNumOfPlayers()) {
            case 2 -> game.getGameTiles().stream()
                    .filter(tile -> !tile.isFlipped() && !isTileintheHandofOther(tile, playerID) && !tile.getType().equals(SSTTypes.Tile_NonAccesiblePlace) && !(tile.getID() == 34) && !(tile.getID() == 61))
                    .toList();
            case 3 -> game.getGameTiles().stream()
                    .filter(tile -> !tile.isFlipped() && !isTileintheHandofOther(tile, playerID) && !tile.getType().equals(SSTTypes.Tile_NonAccesiblePlace) && !(tile.getID() == 34))
                    .toList();
            default -> game.getGameTiles().stream()
                    .filter(tile -> !tile.isFlipped() && !isTileintheHandofOther(tile, playerID) && !tile.getType().equals(SSTTypes.Tile_NonAccesiblePlace))
                    .toList();
        };
        if (availableTiles.isEmpty()) {
            game.getEventBus().cannotPick(player);
        } else {
            SpaceShipTile tileToPick = availableTiles.get(new Random().nextInt(availableTiles.size()));
            game.getEventBus().pickTileLMR(tileToPick.getID(), player.getUsername());
            tileToPick.SetFlip(true);
            game.getGameSSTIH().add(tileToPick);
            player.pickTilePlayer(tileToPick);
        }
    }

    /**
     * Performs an automatic filling operation during the building phase of the game.
     *
     * For each player in the current game, this method performs the following actions:
     * - Adds a battery unit to the player's ship.
     * - Adds lonely passengers to the player's ship.
     *
     * @throws Exception if any issue occurs during the operation.
     */
    @Override
    public void AutomaticFillTile() throws Exception {
        for (Player p : game.getPlayers()) {
            p.getMyShip().addBatteryUnit();
            p.getMyShip().addLonelyPassengers();
        }
    }

    /**
     * Sets the number of players or entities that are ready to proceed to the
     * next phase or state in the game. Throws a {@link RuntimeException} if
     * the operation fails or encounters an unexpected condition.
     *
     * @param readytogo the number indicating the entities ready to move forward.
     *                  This value is used to update the internal state of readiness.
     * @throws RuntimeException if the operation fails due to an unexpected error.
     */
    public void setreadytogo(int readytogo) throws RuntimeException {
        this.readytogo = readytogo;
    }

    /**
     * Transitions the game state from the current `BuildingPhaseStateTest` to the next phase,
     * which is `Flight_Phase_State`. This transition occurs as part of the game's logical progression.
     *
     * @return the next {@link GameState} instance, specifically a new `Flight_Phase_State` object
     *         representing the game's flight phase.
     * @throws RuntimeException if any error occurs during the state transition.
     */
    @Override
    public GameState getNextState() throws RuntimeException {
        return new Flight_Phase_State(game);
    }


    /**
     * Handles the end of the building phase for a specified player, updating their status
     * and notifying the game events accordingly. If all players have completed their positions,
     * the building phase is marked as complete.
     *
     * @param playerid          the unique identifier of the player who is ending their building phase
     * @param positionwheretogo the target position where the player intends to proceed on the game board
     * @throws Exception if an unexpected error occurs during the execution
     */
    @Override
    public void endbuilding(int playerid, int positionwheretogo) throws Exception { //11
        Player player = game.getPlayer(playerid);
        if (player.isBlocked() || game.getGameFlightBoard().getRanking().contains(game.getPlayer(playerid))) {
            game.getEventBus().wrongInput(player);
            return; // Già processato
        }
        int position2 = switch (positionwheretogo) {
            case 1 -> 4;
            case 2 -> 2;
            case 3 -> 1;
            default -> 0;
        };
        if (game.getGameFlightBoard().getBoard().get(position2) != -1) {
            game.getEventBus().wrongInput(player);
            return;
        }
        // Blocca il giocatore e aggiorna il conteggio
        player.setBlocked(true);
        endedplayers++;
        // Gestione posizione
        if (endedplayers <= game.getNumOfPlayers()) {
            game.getGameFlightBoard().initializePlayerPos(player, position2, positionwheretogo);
            // Notifica l'evento DOPO aver aggiornato lo stato
        }
        game.getEventBus().endbuilding(player.getUsername(), positionwheretogo);
        boolean correct = true;
        for (Player _ : game.getPlayers()) {
            if (player.getPositionOnBoard() == -1) {
                correct = false;
                break;
            }
        }
        // Verifica se tutti hanno finito
        if (endedplayers >= game.getNumOfPlayers() && correct) {
            game.getEventBus().completeBuildingPhase();
            completeBuildingPhase();
        }
    }

    /**
     * Completes the building phase of the game by finalizing ship configuration
     * for all players, ensuring any empty spaces in the ship matrix are filled
     * with non-accessible tiles if necessary. Also ensures that all ship tiles
     * are correctly linked to their parent ship.
     *
     * This method performs the following main steps:
     * 1. Invokes the `AutomaticFillTile` method to add predefined components to
     *    each player's ship (e.g., battery units and lonely passengers).
     * 2. For each player's ship matrix, fills all `null` slots with a default
     *    non-accessible `SpaceShipTile` and ensures that each tile is attached
     *    to its respective ship.
     * 3. Executes the building correction logic using `CheckandCorrectBuilding()`
     *    to address logical errors that may have occurred during the ship building
     *    phase. The result of this process is stored to determine subsequent actions.
     * 4. Unblocks all players (allows them to proceed) and updates the game status
     *    if necessary, transitioning to the next game phase.
     * 5. Calls `setreadytogo` with the correction count to set the next state readiness.
     * 6. Transitions the game to the next state via internal logic.
     *
     * The method handles possible changes to the game state and ensures all players
     * are ready before moving on from the building phase. If the correction count
     * indicates no errors (-1), additional tasks such as state updates may occur.
     *
     * @throws Exception if any operations involved in completing the phase encounter issues.
     */
    public void completeBuildingPhase() throws Exception {
        AutomaticFillTile();
        for (Player p : game.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (p.getMyShip().getShipMatrix()[i][j] == null) {
                        ArrayList<Type_side_connector> emptyConnector = new ArrayList<>();
                        for (int k = 0; k < 4; k++) {
                            emptyConnector.add(Type_side_connector.SMOOTH_SIDE);
                        }
                        p.getMyShip().getShipMatrix()[i][j] = new SpaceShipTile(0, emptyConnector, SSTTypes.Tile_NonAccesiblePlace);
                        p.getMyShip().getShipMatrix()[i][j].setAttachedShip(p.getMyShip());
                    }
                    if (p.getMyShip().getShipMatrix()[i][j].getAttachedShip() == null) {
                        p.getMyShip().getShipMatrix()[i][j].setAttachedShip(p.getMyShip());
                    }
                }
            }
            p.setBlocked(false);
        }
        // 2. Chiamata alla tua logica originale di correzione
        int correctionCount = CheckandCorrectBuilding();


        // 3. Sblocca tutti e cambia fase
        for (Player p : game.getPlayers()) {
            p.setBlocked(false);
        }
        setreadytogo(correctionCount);


        if (correctionCount == -1) {
            game.getEventBus().updateStatus(stateEnum.SET_NUM_TILE);
        }
        game.getEventBus().completeBuildingPhase();

        GoNextState();
    }

    /**
     * Manages the transition to the next state in the building phase of the game.
     * This method evaluates the current readiness of players and either remains in the
     * current state, triggers events for tile filling, or progresses the game to the next phase.
     *
     * @throws Exception if any error occurs during the transition process or event triggering
     *
     * Main functionality:
     * - Checks the `readytogo` flag (-1 to skip, 0 for not ready, 1 for ready).
     * - Iterates over all players in the game:
     *   - If a player's ship is completely filled or the player has selected a subship,
     *     triggers an event to ensure empty tiles are filled for that player and sets `readytogo` to 0.
     * - If all players are ready (`readytogo` == 1), unblocks players and transitions the game to the next phase.
     */
    private void GoNextState() throws Exception {
        if (readytogo == -1) {
            return;
        }
        for (Player p : game.getPlayers()) {
            if (p.getMyShip().isCompletelyFilled() || p.getMyShip().getchoosesubship()) {
                game.getEventBus().haveToFillEmptyTiles(p);
                readytogo = 0;
            }
        }
        if (readytogo == 1) {
            for (Player p : game.getPlayers()) {
                p.setBlocked(false);
                // p.getMyShip().setIsBuilding(false);
            }
            this.game.NextPhase();
        }
    }
}