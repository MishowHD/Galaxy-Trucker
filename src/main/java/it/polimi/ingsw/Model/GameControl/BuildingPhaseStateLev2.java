package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.Hourglass;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Tile_HousingUnit;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import it.polimi.ingsw.Utils.stateEnum;

import java.util.*;

public class BuildingPhaseStateLev2 extends Building_Phase_State {

    /**
     * Indicates whether the game is in demo mode.
     * This flag is used to control and activate specific behaviors
     * or functionalities related to a demonstration or tutorial version of the game.
     */
    private boolean demogame = false;
    /**
     * Indicates whether it is time to automatically fill tiles within the game during the building phase.
     * The variable is typically used to trigger tile filling operations and manage game state transitions.
     */
    private boolean timetofill;
    /**
     * Represents a counter for tracking the number of players who have
     * completed a specific phase or task during the building phase of the game.
     * This variable is used to determine the progress of player actions within
     * the current game state.
     */
    private int countingfinishedplayers;
    /**
     * Indicates whether the building phase timer started close to the expected or predefined time.
     * This flag is set to true if the timer initiation timing was nearly accurate, false otherwise.
     */
    private boolean timestartedalmostonetime;
    /**
     * Indicates the readiness state for transitioning to the next phase in the building process.
     * The value represents an integer status used to determine if conditions are met for proceeding.
     */
    private int readytogo;

    /**
     * Constructor for the BuildingPhaseStateLev2 class. This initializes the state
     * with provided game instance and sets up internal variables to manage the building phase.
     * It also registers this state as an observer of the hourglass in the game.
     *
     * @param game an instance of the Game class, representing the current game session for which
     *             this building phase state is being initialized
     */
    public BuildingPhaseStateLev2(Game game) {
        super(game);
        timetofill = false;
        countingfinishedplayers = 0;
        timestartedalmostonetime = false;
        readytogo = -1;
        game.getHourglass().addObserver(this);


    }

    /**
     * Sets the readiness state of the building phase.
     *
     * @param readytogo the readiness state to set, represented as an integer
     */
    public void setreadytogo(int readytogo) {
        this.readytogo = readytogo;
    }

    /**
     * Automatically refills ship systems for all players during the building phase.
     * This method ensures that each player's ship is updated by:
     * - Adding a battery unit.
     * - Handling the addition of any lonely passengers onboard.
     *
     * @throws Exception if an error occurs during the refill process
     */
    // Metodo per ricaricare tutte le batterie dei giocatori
    @Override
    public void AutomaticFillTile() throws Exception {
        for (Player p : game.getPlayers()) {
            p.getMyShip().addBatteryUnit();
            p.getMyShip().addLonelyPassengers();
        }
    }

    /**
     * Commands the game to fill a specific tile on a player's spaceship during the building phase.
     * The method allows adding aliens or passengers to a housing unit tile
     * based on the provided parameters and game rules.
     *
     * @param playerId The ID of the player attempting to fill the tile.
     * @param wantAlien A boolean indicating whether the player wants to add an alien (true) or passengers (false).
     * @param color The color of the alien to be added, applicable if wantAlien is true.
     * @param row The row index of the tile to be filled.
     * @param column The column index of the tile to be filled.
     * @throws Exception If any game-specific issue occurs during execution.
     */
    @Override
    public void CommandFillTile(int playerId, boolean wantAlien, AlienColor color, int row, int column) throws Exception {
        Player player = game.getPlayer(playerId);
        if (!timetofill) {
            game.getEventBus().cannotFill(player);
            return;
        }
        SpaceShipTile tile = player.getMyShip().getShipMatrix()[row][column];
        if (!(tile instanceof Tile_HousingUnit) ||
                tile.getNumPassengers() != 0 ||
                tile.getIsThereAlien()) {
            game.getEventBus().wrongInput(player);
            return;
        }
        if ((color == AlienColor.VIOLET && player.getMyShip().isVioletAlien()) ||
                (color == AlienColor.BROWN && player.getMyShip().isBrownAlien())) {
            game.getEventBus().cannotInsert(player);
            return;
        }
        game.getEventBus().addAlienOrHumansLMR(player.getUsername(), wantAlien, color, row, column);
        if (wantAlien) {
            tile.addAlien(wantAlien, color, row, column);
        } else {
            tile.addPassenger();
            tile.addPassenger();
        }
    }

    /**
     * Handles the event of a player attempting to pick a tile that has already been flipped.
     *
     * The method checks various game conditions before allowing or denying the action:
     * - Verifies if the game has started and actions are allowed.
     * - Ensures the player is not blocked.
     * - Ensures the requested tile exists and has already been flipped.
     * - Ensures the player does not already have something in hand.
     * - Ensures the tile to pick is not currently held by another player.
     *
     * If all the above conditions are met, the player successfully picks the tile,
     * and necessary updates are made to the game state. If any condition fails,
     * an appropriate event is triggered to notify the player.
     *
     * @param index the identifier of the tile to pick.
     * @param playerID the identifier of the player attempting to pick the tile.
     * @throws Exception if critical errors occur, such as tile lookup failure or unexpected states.
     */
    @Override
    public void pickTileAlreadyFlipped(int index, int playerID) throws Exception {
        Player player = game.getPlayer(playerID);
        if (!timestartedalmostonetime) {
            game.getEventBus().cannotPick(player);
            return;
        } else if (player.isBlocked()) {
            game.getEventBus().cannotPick(player);
            return;
        }
//        SpaceShipTile tileToPick = game.getGameTiles().stream()
//                .filter(tile -> tile.getID() == index)
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Tile not found for index: " + index));
        SpaceShipTile tileToPick = null;
        for (SpaceShipTile p : game.getGameTiles()) {
            if (p.getID() == index) {
                tileToPick = p;
                break;
            }
        }
        if (tileToPick == null) {
            game.getEventBus().cannotPick(player);
        }
        if (!Objects.requireNonNull(tileToPick).isFlipped()) {
            game.getEventBus().cannotPick(player);
        } else if (player.hasSomethingInHand()) {
            game.getEventBus().cannotPick(player);

        } else if (isTileintheHandofOther(tileToPick, playerID)) {
            game.getEventBus().cannotPick(player);
        } else {
            game.getGameSSTIH().add(tileToPick);
            player.pickTilePlayer(tileToPick);
            game.getEventBus().pickTileLMR(index, player.getUsername());
        }
    }

    /**
     * Allows a player to pick a tile from the available game tiles based on the specified index.
     * The method validates whether the player can pick the tile, checks the availability of the tile,
     * and updates the game state accordingly.
     *
     * @param index The index of the tile the player wants to pick.
     * @param playerID The unique identifier of the player attempting to pick the tile.
     * @throws Exception If the tile is not found, or other conditions prevent the action.
     */
    @Override
    public void pickTile(int index, int playerID) throws Exception {
        Player player = game.getPlayer(playerID);
        if (!timestartedalmostonetime) {
            game.getEventBus().cannotPick(player);
            return;
        } else if (player.isBlocked()) {
            game.getEventBus().cannotPick(player);
            return;
        }
        SpaceShipTile tileToPick = game.getGameTiles().stream()
                .filter(tile -> tile.getID() == index)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tile not found for index: " + index));
        if (player.hasSomethingInHand()) {
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
     * Handles the action of picking an unknown spaceship tile for a specified player
     * during the building phase of the game.
     *
     * @param playerID the ID of the player attempting to pick the tile
     * @throws Exception if an error occurs during the action execution
     */
    @Override
    public void pickTileUnknown(int playerID) throws Exception {
        Player player = game.getPlayer(playerID);
        if (!timestartedalmostonetime) {
            game.getEventBus().cannotPick(player);
            return;
        } else if (player.isBlocked()) {
            game.getEventBus().cannotPick(player);
            return;
        } else if (player.hasSomethingInHand()) {
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
     * Transitions the game to the next state in the progression.
     *
     * @return a new instance of Flight_Phase_State, representing the next phase of the game.
     */
    @Override
    public GameState getNextState() {
        return new Flight_Phase_State(game);
    }


    /**
     * Activates the timer for the specified player. This method attempts to start the
     * hourglass timer for the player and triggers appropriate events based on whether
     * the timer was successfully started or was already running.
     *
     * @param player the player for whom the timer is to be activated
     * @throws Exception if an error occurs while starting the timer
     */
    //    @Override
//    public void StartTimer(int PlayerID)  throws RuntimeException {
//        if (game.getHourglass().() || game.getHourglass().() <= 0) {
//            throw new RuntimeException("Not running yet");
//        }
//    }
    // === Nel Controller ===
    @Override
    public void activateTimer(Player player) throws Exception {
        Hourglass hourglass = game.getHourglass();
        // Provo sempre ad avviare il timer.
        if (hourglass.startTimer(player)) {
            // imposto il flag solo se il timer è davvero partito
            this.timestartedalmostonetime = true;
            game.getEventBus().timerStarted();
        } else {
            game.getEventBus().timerAlreadyStarted(player);
        }
    }


    /**
     * This method is called when the timer has expired. Depending on whether it is the last
     * activation of the timer or not, it takes appropriate actions such as locking or unlocking
     * players and transitioning phases.
     *
     * @param isLastActivation a boolean flag indicating whether this timer expiration is the
     *                          last activation. If true, it transitions the game to the next phase
     *                          and locks players. If false, it unlocks players who are not yet ranked
     *                          on the FlightBoard.
     * @throws Exception if an error occurs during the handling of the timer expiration or
     *                   game state transitions.
     */
    // Metodo per chiamare quando il timer è esaurito (scaduto per l'ultima volta)
    public void onTimerComplete(boolean isLastActivation) throws Exception {
        game.getEventBus().timerEnded(isLastActivation);
        //se ci sono già tutti i player sulla fightboard, non serve chiamare questa cosa
        if (game.getGameFlightBoard().getPlayerRankList().size() != game.getNumOfPlayers()) {
            if (isLastActivation) {
                // Blocca i giocatori solo se è l'ultima attivazione
                //vado nella fase dove posso solo positionarmi
                game.getEventBus().timerEnded(true);
                //non faccio più endibuilding
                endBuildingPhaseForAll();
            } else {
                // Sblocca i giocatori se non è l'ultima attivazione
                for (Player player : game.getPlayers()) {
                    if (!game.getGameFlightBoard().getRanking().contains(player)) {
                        player.setBlocked(false); // Sblocca solo i giocatori non ancora in FlightBoard
                    }
                }
            }
        }
    }

    /**
     * Allows a player to pick a little deck during the building phase of the game,
     * provided specific conditions are met, such as the deck being unoccupied and
     * the player not being blocked or holding another item.
     *
     * @param index the index of the little deck to be picked. Must be within the bounds
     *              of the showable decks in the game flight board.
     * @param playerid the unique identifier of the player attempting to pick the little deck.
     * @throws Exception if an error occurs during the operation or if the conditions required
     *                   to pick the little deck are not satisfied.
     */
    @Override
    public void pickLittleDeck(int index, int playerid) throws Exception {
        Player player = game.getPlayer(playerid);
        if (!timestartedalmostonetime) {
            game.getEventBus().cannotPick(player);
            return;
        }
        if (index < 0 || index >= game.getGameFlightBoard().getShowableDecks().size()) {
            game.getEventBus().wrongInput(player);
            return;
        }
        Deck deckToPick = game.getGameFlightBoard().getShowableDeck(index);
        if (timetofill ||
                player.isBlocked() ||
                player.hasSomethingInHand() ||
                deckToPick.isOccupied()
        ) {
            game.getEventBus().cannotPick(player);
        } else {
            game.getEventBus().pickLittleDeckLMR(index, player.getUsername());
            player.pickLittleDeck(index);
            deckToPick.setOccupied();

        }
    }

    /**
     * Constructs a demo spaceship configuration for demonstration purposes
     * within a game. This method programmatically picks and places tiles
     * on a game board to form a predefined ship layout.
     *
     * The tiles are selected and inserted sequentially at
     * specific coordinates with defined orientations.
     * The process is completed by marking the end of the building phase
     * for a specific player.
     *
     * @throws Exception if any error occurs during the tile picking
     *                   or placement process, or when transitioning
     *                   to the next phase.
     */
    public void buildDemoShip1() throws Exception {
        demogame = true;
        pickTile(149, 0);
        insertTile(2, 2, 0, 180);

        pickTile(16, 0);
        insertTile(2, 1, 0, 270);

        pickTile(103, 0);
        insertTile(2, 0, 0, 270);

        pickTile(120, 0);
        insertTile(1, 1, 0, 270);

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

        pickTile(26, 0);
        insertTile(3, 0, 0, 180);

        pickTile(75, 0);
        insertTile(4, 0, 0, 0);

        pickTile(11, 0);
        insertTile(4, 1, 0, 0);

        pickTile(74, 0);
        insertTile(4, 2, 0, 0);

        pickTile(154, 0);
        insertTile(2, 4, 0, 0);

        pickTile(41, 0);
        insertTile(3, 4, 0, 270);

        pickTile(145, 0);
        insertTile(4, 4, 0, 90);

        pickTile(31, 0);
        insertTile(1, 4, 0, 180);

        pickTile(104, 0);
        insertTile(0, 4, 0, 0);

        pickTile(135, 0);
        insertTile(1, 5, 0, 0);

        pickTile(44, 0);
        insertTile(2, 5, 0, 270);

        pickTile(1, 0);
        insertTile(3, 5, 0, 180);

        pickTile(142, 0);
        insertTile(4, 5, 0, 90);

        pickTile(35, 0);
        insertTile(4, 6, 0, 90);

        pickTile(28, 0);
        insertTile(3, 6, 0, 0);

        pickTile(68, 0);
        insertTile(2, 6, 0, 270);

        pickTile(71, 0);
        insertTile(0, 2, 0, 90);
        //player 0


        endbuilding(0, 1);

    }

    /**
     * Builds a pre-defined demonstration spaceship layout for a specific player.
     * The method utilizes a sequence of tile selections and placements,
     * setting up the ship structure by specifying the position and rotation of each tile.
     * This function assumes a controlled, step-by-step deterministic process suitable
     * for demonstration or testing purposes.
     *
     * The method executes the following steps:
     * - Picks specific tiles by their index corresponding to specific configurations.
     * - Inserts these tiles onto the game board at precise locations with rotations defined.
     * - Ends the building process for the player at a specified position.
     *
     * Key operations include:
     * - `pickTile`: Selects a specific tile to be placed on the player's spaceship.
     * - `insertTile`: Places the selected tile at a designated row, column, and rotation.
     * - `endbuilding`: Marks the completion of the building phase for the player.
     *
     * @throws Exception If any issues occur during tile picking, insertion, or
     *                   ending the building phase.
     */
    public void buildDemoShip2() throws Exception {
        pickTile(153, 1);
        insertTile(2, 2, 1, 270);

        pickTile(60, 1);
        insertTile(3, 2, 1, 0);

        pickTile(37, 1);
        insertTile(4, 2, 1, 270);

        pickTile(138, 1);
        insertTile(4, 1, 1, 0);

        pickTile(146, 1);
        insertTile(4, 0, 1, 180);

        pickTile(76, 1);
        insertTile(3, 0, 1, 180);

        pickTile(59, 1);
        insertTile(2, 1, 1, 270);

        pickTile(45, 1);
        insertTile(2, 0, 1, 0);

        pickTile(19, 1);
        insertTile(3, 1, 1, 0);

        pickTile(50, 1);
        insertTile(1, 1, 1, 270);

        pickTile(7, 1);
        insertTile(1, 2, 1, 180);

        pickTile(21, 1);
        insertTile(0, 2, 1, 270);

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
        insertTile(2, 5, 1, 90);//ok


        pickTile(4, 1);
        insertTile(1, 5, 1, 90);

        pickTile(67, 1);
        insertTile(1, 4, 1, 90);

        pickTile(113, 1);
        insertTile(0, 4, 1, 0);


        pickTile(99, 1);
        insertTile(4, 6, 1, 0);


        endbuilding(1, 2);

        //choose alien


    }

    /**
     * Builds a predefined demo spaceship layout for player 3 in the game.
     * This method follows a specific sequence of tile selections and placements
     * to create the demo ship. Each tile is picked by its index and inserted
     * at a designated position on the player's shipboard with the specified rotation.
     *
     * The method concludes by signaling the end of the building phase for player 3.
     *
     * @throws Exception if any issue occurs during tile selection, tile insertion,
     *                   or the endbuilding phase.
     */
    public void buildDemoShip3() throws Exception {
        //PLAYER 3 SHIPBOARD
        pickTile(18, 2);
        insertTile(2, 2, 2, 270);

        pickTile(63, 2);
        insertTile(2, 1, 2, 0);

        pickTile(155, 2);
        insertTile(2, 0, 2, 180);

        pickTile(111, 2);
        insertTile(3, 3, 2, 180);

        pickTile(47, 2);
        insertTile(3, 2, 2, 0);

        pickTile(23, 2);
        insertTile(3, 1, 2, 0);

        pickTile(46, 2);
        insertTile(3, 0, 2, 0);

        pickTile(38, 2);
        insertTile(4, 0, 2, 90);

        pickTile(141, 2);
        insertTile(4, 1, 2, 0);

        pickTile(132, 2);
        insertTile(4, 2, 2, 180);

        pickTile(69, 2);
        insertTile(1, 1, 2, 180);

        pickTile(56, 2);
        insertTile(1, 2, 2, 270);

        pickTile(109, 2);
        insertTile(0, 2, 2, 0);

        pickTile(40, 2);
        insertTile(1, 3, 2, 90);

        pickTile(143, 2);
        insertTile(1, 4, 2, 0);


        pickTile(82, 2);
        insertTile(1, 5, 2, 270);

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

        pickTile(51, 2);
        insertTile(2, 6, 2, 270);

        pickTile(12, 2);
        insertTile(3, 6, 2, 0);

        pickTile(29, 2);

        insertTile(4, 6, 2, 90);

        endbuilding(2, 3);
    }

    /**
     * Builds a demonstration spaceship layout by sequentially picking and placing tiles
     * at specific positions with defined orientations on the spaceship grid.
     *
     * The method utilizes a series of calls to the pickTile and insertTile methods
     * to select tiles from a predefined set and place them on the grid. Some placements
     * are currently commented out, signifying unused or placeholder tiles.
     *
     * At the end of the tile placement process, the building phase is concluded
     * by invoking the endbuilding method.
     *
     * @throws Exception if any issue occurs during tile selection or placement.
     */
    public void buildDemoShip4() throws Exception {
        pickTile(3, 3);
        insertTile(2, 2, 3, 270);

        pickTile(49, 3);
        insertTile(2, 1, 3, 270);

        pickTile(137, 3);
        insertTile(2, 0, 3, 0);

        pickTile(134, 3);
        insertTile(3, 3, 3, 180);

        pickTile(30, 3);
        insertTile(3, 2, 3, 90);

        pickTile(64, 3);
        insertTile(3, 1, 3, 180);

//        pickTile(10, 3);
//        insertTile(3, 0, 3, 90);

        pickTile(96, 3);
        insertTile(4, 1, 3, 0);

        pickTile(151, 3);
        insertTile(4, 0, 3, 180);


        pickTile(90, 3);
        insertTile(4, 2, 3, 0);

        pickTile(144, 3);
        insertTile(1, 2, 3, 90);

//        pickTile(86, 3);
//        insertTile(1, 1, 3, 0);

        pickTile(131, 3);
        insertTile(0, 2, 3, 0);

        pickTile(36, 3);
        insertTile(1, 3, 3, 180);

        pickTile(13, 3);
        insertTile(1, 4, 3, 270);

        pickTile(116, 3);
        insertTile(0, 4, 3, 0);

        pickTile(43, 3);
        insertTile(1, 5, 3, 270);

        pickTile(152, 3);
        insertTile(2, 4, 3, 0);

        pickTile(54, 3);
        insertTile(2, 5, 3, 90);

        pickTile(73, 3);
        insertTile(2, 6, 3, 270);

        pickTile(55, 3);
        insertTile(3, 4, 3, 0);

        pickTile(95, 3);
        insertTile(4, 4, 3, 0);

        pickTile(119, 3);
        insertTile(4, 5, 3, 90);

        pickTile(66, 3);
        insertTile(3, 5, 3, 90);

        pickTile(22, 3);
        insertTile(3, 6, 3, 0);

//        pickTile(140, 3);
//        insertTile(4, 6, 3, 90);

        endbuilding(3, 4);
    }

    /**
     * Sets up a demo game environment depending on the number of players in the game.
     *
     * This method activates the timer for the first player and, based on the total number of players,
     * builds demo ships and fills specified tiles with aliens of particular colors and coordinates.
     * It also completes the building phase after performing the setup.
     *
     * @throws Exception if an error occurs during the demo game setup.
     */
    public void demoGame() throws Exception {
        game.getGameState().activateTimer(game.getPlayers().getFirst());
        if (game.getPlayers().size() == 2) {
            buildDemoShip1();
            buildDemoShip2();
            game.getGameState().CommandFillTile(0, true, AlienColor.BROWN, 4, 6);
            game.getGameState().CommandFillTile(0, true, AlienColor.VIOLET, 3, 4);
            game.getGameState().CommandFillTile(1, true, AlienColor.BROWN, 4, 2);
            game.getGameState().completeBuildingPhase();
        }
        //player 1
        if (game.getPlayers().size() == 3) {
            buildDemoShip1();
            buildDemoShip2();
            buildDemoShip3();
            game.getGameState().CommandFillTile(0, true, AlienColor.BROWN, 4, 6);
            game.getGameState().CommandFillTile(0, true, AlienColor.VIOLET, 3, 4);
            game.getGameState().CommandFillTile(1, true, AlienColor.BROWN, 4, 2);
            game.getGameState().CommandFillTile(2, true, AlienColor.BROWN, 4, 0);
            game.getGameState().CommandFillTile(2, true, AlienColor.VIOLET, 1, 3);
            game.getGameState().completeBuildingPhase();
        }
        if (game.getPlayers().size() == 4) {
            buildDemoShip1();
            buildDemoShip2();
            buildDemoShip3();
            buildDemoShip4();
            game.getGameState().CommandFillTile(0, true, AlienColor.BROWN, 4, 6);
            game.getGameState().CommandFillTile(0, true, AlienColor.VIOLET, 3, 4);
            game.getGameState().CommandFillTile(1, true, AlienColor.BROWN, 4, 2);
            game.getGameState().CommandFillTile(2, true, AlienColor.BROWN, 4, 0);
            game.getGameState().CommandFillTile(2, true, AlienColor.VIOLET, 1, 3);
            game.getGameState().CommandFillTile(3, true, AlienColor.BROWN, 2, 1);
            game.getGameState().CommandFillTile(3, true, AlienColor.VIOLET, 1, 3);
            game.getGameState().completeBuildingPhase();
        }

    }

    /**
     * Allows a player to deposit their "Little Deck" if conditions permit.
     * The method checks various preconditions to ensure the deposit is valid
     * (e.g., whether it's time to fill, if the player is blocked, or if they have something in hand).
     * If the player cannot deposit, an event is triggered to notify the state.
     * Otherwise, the player's "Little Deck" is deposited, and the relevant events are triggered.
     *
     * @param playerID the ID of the player attempting to deposit their "Little Deck"
     * @throws Exception if there is an issue during the deposit process
     */
    @Override
    public void depositLittleDeck(int playerID) throws Exception {
        Player player = game.getPlayer(playerID);
        if (timetofill || player.isBlocked() || !player.hasSomethingInHand()) {
            game.getEventBus().cannotDeposit(player);
        } else {
            for (Deck deck : game.getGameFlightBoard().getShowableDecks()) {
                if (deck.equals(player.getLittleDeckInHand())) deck.setFree();
            }
            player.depositLittleDeckPlayer();
            game.getEventBus().depositThingInHand(player.getUsername());
        }
    }


    /**
     * Manages the end of the building phase for a specific player in the game.
     * If the player has already been processed or if the selected position is invalid, an error event is triggered.
     * Otherwise, the player's board position is updated, and their status is marked as blocked.
     * Upon all players completing the building phase, the phase is concluded and transitioned to the next state.
     *
     * @param playerid           The unique identifier of the player completing the building phase.
     * @param positionwheretogo  The chosen position on the board where the player intends to go.
     * @throws Exception         If any game-related exception occurs during the process.
     */
    @Override
    public void endbuilding(int playerid, int positionwheretogo) throws Exception {
        Player player = game.getPlayer(playerid);
        if (player.getMyShip().getFlightBoard().getPlayerRankList().contains(player) || game.getGameFlightBoard().getRanking().contains(game.getPlayer(playerid))) {
            game.getEventBus().wrongInput(player);
            return; // Già processato
        }
        int position2 = switch (positionwheretogo) {
            case 1 -> 6;
            case 2 -> 3;
            case 3 -> 1;
            default -> 0;
        };
        if (game.getGameFlightBoard().getBoard().get(position2) != -1) {
            game.getEventBus().wrongInput(player);
            return;
        }

        // Blocca il giocatore e aggiorna il conteggio
        player.setBlocked(true);
        countingfinishedplayers++;
        // Gestione posizione
        if (countingfinishedplayers <= game.getNumOfPlayers()) {
            game.getGameFlightBoard().initializePlayerPos(player, position2, positionwheretogo);
            // Notifica l'evento DOPO aver aggiornato lo stato
        }
        game.getEventBus().endbuilding(player.getUsername(), positionwheretogo);
        // Verifica se tutti hanno finito
        boolean correct = true;
        for (Player _ : game.getPlayers()) {
            if (player.getPositionOnBoard() == -1) {
                correct = false;
                break;
            }
        }
        if (countingfinishedplayers >= game.getNumOfPlayers() && correct) {
            completeBuildingPhase();
        }
    }

    /**
     * Completes the building phase in the game by performing necessary actions to ensure the transition
     * to the next phase. This method manages tasks such as updating the game state, validating
     * player ships, and notifying all players about the phase shift.
     *
     * Actions performed by this method include:
     * 1. Setting a flag to indicate the automatic tile filling should begin.
     * 2. Automatically filling player ships by ensuring every cell is initialized with a default
     *    inaccessible tile if no other tile is placed at that position.
     * 3. Invoking logic to check and correct the building phase adjustments, which ensures player ships
     *    meet the required conditions for the game rules.
     * 4. Releasing any blocked players, allowing them to proceed to the next game phase.
     * 5. Updating the readiness state based on the result of the building phase corrections.
     * 6. Depending on validation results, transitioning to the appropriate game state, which may include
     *    updating the number of tiles.
     * 7. Notifying the game's event bus that the building phase is complete.
     */
    @Override
    public void completeBuildingPhase() throws Exception {
        timetofill = true;
        AutomaticFillTile();
        for (Player p : game.getPlayers()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (p.getMyShip().getShipMatrix()[i][j] == null) {
                        ArrayList<Type_side_connector> connector = new ArrayList<>(Collections.nCopies(4, Type_side_connector.SMOOTH_SIDE));
                        p.getMyShip().getShipMatrix()[i][j] = new SpaceShipTile(0, connector, SSTTypes.Tile_NonAccesiblePlace);
                    }
                    p.getMyShip().getShipMatrix()[i][j].setAttachedShip(p.getMyShip());
                }
            }
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
        // Notifiche finali

    }

    /**
     * Ends the building phase for all players in the game.
     *
     * This method triggers the event to conclude the building phase
     * for all players through the event bus. It also evaluates each player
     * to check whether they are already included in the flight board ranking.
     * Any player not currently ranked will be marked as blocked, preventing
     * further actions in this phase.
     *
     * @throws Exception if an error occurs while ending the building phase
     */
    public void endBuildingPhaseForAll() throws Exception {
        // Forza il completamento per i giocatori non bloccati
        game.getEventBus().endBuildingPhaseForAll();
        for (Player p : game.getPlayers()) {
            if (!game.getGameFlightBoard().getRanking().contains(p)) {
                p.setBlocked(true);
            }
        }
    }

    /**
     * Transitions the game to the next state or phase if certain conditions are met during the building phase.
     *
     * This method checks whether players have completed their ship construction or chosen a sub-ship. If all players
     * are ready, the building phase is terminated, and the game transitions to the next phase. Specific actions
     * are performed, such as notifying players to fill empty tiles, unblocking players, removing wait tiles,
     * and surrendering players with no passengers.
     *
     * Throws:
     * - Exception: If an issue occurs during the state transition or any required action.
     *
     * Preconditions:
     * - The method is triggered only when `readytogo` is set to 1.
     *
     * Postconditions:
     * - If any player's ship is either completely filled or they have chosen a sub-ship and `demogame` is false,
     *   an event is triggered to fill empty tiles.
     * - The building phase ends for all players who meet the criteria, and game-specific actions are performed,
     *   such as clearing wait tiles and managing unready players.
     * - Transitions the game to the next phase by invoking related methods in the game state.
     * - Stops the hourglass timer once the transition is initiated.
     */
    private void GoNextState() throws Exception {
        if (readytogo == 1) {
            for (Player p : game.getPlayers()) {
                if (p.getMyShip().isCompletelyFilled() || p.getMyShip().getchoosesubship()) {
                    if (!demogame) {
                        game.getEventBus().haveToFillEmptyTiles(p);
                    }
                    readytogo = 0;
                    return;
                }
            }
            game.getEventBus().stopBuilding();
            for (Player p : game.getPlayers()) {
                p.setBlocked(false);
                p.getMyShip().removeWaitTilesForEndBuilding();
                //p.getMyShip().setIsBuilding(false);
                if (p.getMyShip().getPassengerNumber() == 0) {
                    p.Surrender();
                }
            }
            System.out.println("inizio le carte");
            this.game.NextPhase();
            this.game.getHourglass().stopTimer();
        }
    }


    /**
     * Inserts a tile from the player's wait stack into the specified position on the game board.
     * The tile must be properly rotated and meet all constraints before placement.
     * If any condition is not met, the insertion attempt is rejected, and appropriate events are triggered.
     *
     * @param playerID The unique identifier of the player attempting the insertion.
     * @param index The index of the wait tile (0 or 1) that the player wants to insert.
     * @param row The row on the game board where the tile is to be placed.
     * @param col The column on the game board where the tile is to be placed.
     * @param rotation The rotation of the tile in degrees (must be a multiple of 90, e.g., 0, 90, 180, 270).
     * @throws Exception If an unexpected error occurs during the insertion process.
     */
    //nuovi metodi
    public void insertWaitTile(int playerID, int index, int row, int col, int rotation) throws Exception { //rotation is 0, 90, 180,270 ecc..
        Player player = game.getPlayer(playerID);
        if (player.hasSomethingInHand()) {
            game.getEventBus().cannotInsert(player);
            return;
        }
        if (rotation % 90 != 0) {
            game.getEventBus().wrongInput(player);
            return;
        }
        if (((row < 0 || row > 4) || (col < 0) || col > 6)) {
            game.getEventBus().wrongInput(player);
            return;
        }
        if (player.isBlocked()) {
            game.getEventBus().cannotInsert(player);
        } else {
            if (index == 0 && player.getMyShip().getWaitTile1() != null) {
                SpaceShipTile tile = player.getMyShip().getWaitTile1();
                int rot = (rotation % 360) / 90; //i have either 0, 1, 2 or 3
                for (int i = 0; i < rot; i++) {
                    tile.Rotate90right();
                }
                player.setTileInHand(tile);
                if (player.InsertTile(tile, row, col)) {
                    game.getEventBus().insertWaitTileLMR(player.getUsername(), index);
                    game.getEventBus().insertTileLMR(tile.getID(), row, col, rot, player.getUsername());
                    tile.setAttachedShip(player.getMyShip());
                    player.getMyShip().setWaitTile1(null);
                }
            } else if (index == 1 && player.getMyShip().getWaitTile2() != null) {
                SpaceShipTile tile = player.getMyShip().getWaitTile2();
                int rot = (rotation % 360) / 90;
                for (int i = 0; i < rot; i++) {
                    tile.Rotate90right();
                }
                player.setTileInHand(tile);
                if (player.InsertTile(tile, row, col)) {
                    game.getEventBus().insertWaitTileLMR(player.getUsername(), index);
                    game.getEventBus().insertTileLMR(tile.getID(), row, col, rot, player.getUsername());
                    tile.setAttachedShip(player.getMyShip());
                    player.getMyShip().setWaitTile2(null);
                }
            } else {
                game.getEventBus().cannotInsert(player);
            }
        }
    }


    /**
     * Adds a wait tile to the specified player's ship if certain conditions are met.
     * If the player cannot add a tile (e.g., no tile in hand or player is blocked) or both wait tile slots are occupied,
     * a corresponding event is triggered to notify that the action cannot be performed.
     *
     * @param playerID the unique identifier of the player attempting to add a wait tile
     * @throws Exception if an error occurs during the process of adding the wait tile
     */
    public void addWaitTile(int playerID) throws Exception {
        Player player = game.getPlayer(playerID);
        if (!player.hasSomethingInHand() || player.isBlocked()) {
            game.getEventBus().cannotInsert(player);
        } else {
            SpaceShipTile tile = player.getTileInHand();
            ShipBoard s = player.getMyShip();
            if (s.getWaitTile1() != null && s.getWaitTile2() != null) {
                game.getEventBus().cannotInsert(player);
            } else {
                game.getGameSSTIH().remove(tile);
                game.getGameTiles().remove(tile);
                player.setTileInHand(null);
                player.setHasTileInHand(false);
                if (s.getWaitTile1() == null) {
                    game.getEventBus().addTileToWaitList(player.getUsername(), tile.getID());
                    s.setWaitTile1(tile);
                    s.getWaitTile1().setAttachedShip(s);
                } else {
                    game.getEventBus().addTileToWaitList(player.getUsername(), tile.getID());
                    s.setWaitTile2(tile);
                    s.getWaitTile2().setAttachedShip(s);
                }
            }
        }
    }
}
