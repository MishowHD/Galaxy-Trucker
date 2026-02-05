package it.polimi.ingsw.View;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Boards.*;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.CombatZone.Consequences;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Cards.Utils_Cards.c_State;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.Utils_Boards.ShipBoard_iterable;
import it.polimi.ingsw.Model.Boards.Utils_Boards.Ship_Colour;
import it.polimi.ingsw.Model.Tiles.Tile_HousingUnit;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;
import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Utils.stateEnum;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LittleModelRepresentation {
    /**
     * Represents a level or stage indicator, typically used to signify a
     * specific level of operation, configuration, or state within the application.
     *
     * The default value is -1, which may represent an uninitialized or undefined state.
     */
    protected Integer lev = -1;
    /**
     * A list containing the names of players who have not surrendered in the game.
     * This variable is protected, allowing access within the same package or subclasses.
     */
    protected ArrayList<String> PlayersNotSurrended = new ArrayList<>();
    /**
     * A protected list that stores instances of ShipBoard.
     * This list is used to manage and organize multiple ShipBoard objects,
     * which may represent different boards or configurations in a game or
     * application involving ships.
     */
    protected ArrayList<ShipBoard> ShipBoards = new ArrayList<>();
    /**
     * Represents the flight board which may contain information about flights, schedules,
     * or related details. This variable is initialized to null by default and
     * should be assigned appropriately before usage.
     */
    protected FlightBoard flightBoard = null;
    /**
     * Represents a collection of spaceship tiles that are hidden and not immediately visible within the game.
     * This list is used to manage and store `SpaceShipTile` objects which can later be revealed or accessed
     * when necessary during gameplay or specific events.
     */
    protected ArrayList<SpaceShipTile> HiddenTiles = new ArrayList<>();
    /**
     * A collection of SpaceShipTile objects that are currently visible.
     * This list is used to store the tiles that are within the viewer's
     * line of sight or field of view, typically for rendering or interaction purposes.
     * The visibility of tiles may be dynamically updated based on various
     * conditions such as user movement, camera position, or game environment changes.
     */
    protected ArrayList<SpaceShipTile> VisibleTiles = new ArrayList<>();
    /**
     * Represents the collection of SpaceShipTile objects currently held in hand.
     * It is designed to manage and store the tiles available to a player
     * or object during gameplay implementation.
     * The variable is protected, allowing access within its own package
     * and by subclasses.
     */
    protected ArrayList<SpaceShipTile> TilesInHand = new ArrayList<>();
    /**
     * Represents the card currently activated in the system.
     * This variable holds a reference to the activated card,
     * which may be null if no card is currently active.
     */
    protected Card activatedcard = null;
    /**
     * Represents the object currently being held or interacted with.
     * The value of this field can be any object and is initialized to null by default.
     * It can be used to track the state or possession of an item within the context of an application.
     * This field is protected, meaning it can be accessed within the class itself,
     * by subclasses, and by other classes in the same package.
     */
    protected Object ThingInHand = null;
    /**
     * Represents a bank entity within the context of the application.
     * This variable can be used to store and access information or methods
     * related to the bank.
     */
    protected Bank bank;
    /**
     * Represents a flag indicating whether it is time to fill.
     * This variable is used to determine if a specific action
     * or process related to filling needs to occur.
     */
    protected boolean timetofill = false;
    /**
     * Indicates whether the entity or object has the ability to surrender.
     * It represents a state that can either be true or false.
     */
    protected Boolean cansurrend;
    /**
     * Represents the current state of the game.
     * This variable holds a value of the stateEnum type which encapsulates
     * the various states the game can be in (e.g., START, PAUSE, RUNNING, GAME_OVER).
     * It is used to manage and control the game's flow and state transitions.
     */
    protected stateEnum gameState;
    /**
     * Represents the current state of a card within a system.
     * The possible values are defined in the {@code stateEnum} enumeration.
     * This variable is used to track and manage the lifecycle or status
     * of a card, such as active, inactive, or pending states.
     */
    protected stateEnum cardState;
    /**
     * Represents the count of players who have finished the game.
     * This variable is initialized to zero by default and is intended to
     * keep track of the number of players who have completed their objectives
     * or reached the end of a game session.
     */
    private int countingfinishedplayers = 0;
    /**
     * A protected HashMap that stores the final scores for specific items identified by their names.
     * The keys in the map are Strings representing the names, and the values are Floats representing
     * the corresponding scores.
     */
    protected HashMap<String, Float> finalScores;
    /**
     * A protected string variable to hold a message.
     * The purpose or usage of this variable can vary depending on the context of the application.
     */
    protected String message;
    /**
     * Represents a general view component, typically used for
     * displaying or handling common UI elements or interactions
     * within the application.
     *
     * This variable may be used to manage or render a shared visual
     * representation across different modules or components.
     *
     * Accessibility is protected to ensure that it is only available
     * within the class itself, its subclasses, or other classes within
     * the same package.
     */
    protected GeneralView generalView;
    /**
     * Represents the remaining time, typically utilized to track or manage
     * a countdown or duration in a specific process or task.
     *
     * This variable is declared as {@code volatile} to ensure visibility
     * of updates across multiple threads in a concurrent environment.
     * Being {@code protected}, it is accessible within its own package
     * and by subclasses.
     *
     * The time is stored as an integer, where its meaning and unit
     * (e.g., seconds, milliseconds) depend on the context in which
     * it is used.
     */
    protected volatile int timeRemaining;
    /**
     * Represents a user's nickname or short identifier.
     * This variable is intended to store a shortened or alternative name
     * that a user might prefer to be addressed by.
     * It can be used for personalization or display purposes.
     */
    protected String myNickname;
    /**
     * Represents the result of a dice roll. The variable holds the value of the
     * dice, which is typically in the range of standard dice rolls (e.g., 1-6).
     * The default value is -1, indicating that no dice roll has occurred or the
     * dice is uninitialized.
     */
    protected int dice = -1;
    /**
     * Represents the resting duration or state related to an hourglass.
     * This variable potentially indicates the time in seconds or a predefined
     * resting state associated with an hourglass.
     */
    int hourglassResting;


    /**
     * Retrieves the current resting time of the hourglass.
     *
     * @return the hourglass resting time as an integer
     */
    public int getHourglassResting() {
        return hourglassResting;
    }

    /**
     * Represents the duration or time period for which a surrender action is in a resting
     * or inactive state. This variable may indicate a cooldown time before the action
     * can be invoked again or the state of inactivity of the surrender process.
     */
    int surrenderResting;

    /**
     * Retrieves the value of the dice.
     *
     * @return the current value of the dice as an integer.
     */
    public int getDice() {
        return dice;
    }

    /**
     * Sets the value of the dice.
     *
     * @param dice the new value to set for the dice
     */
    public void setDice(int dice) {
        this.dice = dice;
    }

    /**
     * Sets the nickname for the current instance.
     *
     * @param myNickname the nickname to be set; must be a non-null, non-empty string.
     */
    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
    }

    /**
     * Retrieves the nickname associated with the current instance.
     *
     * @return the nickname as a String
     */
    public String getMyNickname() {
        return myNickname;
    }

    /**
     * Retrieves the remaining time.
     *
     * @return the remaining time as an integer
     */
    public int getTimeRemaining() {
        return timeRemaining;
    }

    /**
     * Sets the time remaining for the current operation or process.
     *
     * @param timeRemaining the amount of time remaining, in seconds
     */
    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }


    /**
     * Retrieves the general view of the application.
     *
     * @return the current instance of GeneralView representing the general layout or structure.
     */
    public GeneralView getGeneralView() {
        return generalView;
    }

    /**
     * Sets the general view for this instance.
     *
     * @param generalView the GeneralView instance to be set
     */
    public void setGeneralView(GeneralView generalView) {
        this.generalView = generalView;
    }

    /**
     * Constructs a LittleModelRepresentation instance with a specified GeneralView.
     *
     * @param generalView the general view to be associated with this LittleModelRepresentation
     */
    public LittleModelRepresentation(GeneralView generalView) {
        this.generalView = generalView;
    }


    /**
     * Retrieves the value of the lev property.
     *
     * @return the current value of the lev property as an Integer
     */
    public Integer getLev() {
        return lev;
    }

    /**
     * Retrieves the list of players who have not surrendered.
     *
     * @return an ArrayList of player names who have not surrendered.
     */
    public ArrayList<String> getPlayersNotSurrended() {
        return PlayersNotSurrended;
    }

    /**
     * Retrieves the current flight board instance.
     *
     * @return the current FlightBoard containing flight information.
     */
    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    /**
     * Retrieves the list of hidden spaceship tiles.
     *
     * @return an ArrayList of SpaceShipTile objects representing the hidden tiles.
     */
    public ArrayList<SpaceShipTile> getTiles() {
        return HiddenTiles;
    }

    /**
     * Retrieves the list of hidden tiles.
     *
     * @return An ArrayList of SpaceShipTile objects representing the hidden tiles.
     */
    public ArrayList<SpaceShipTile> getHiddenTiles() {
        return HiddenTiles;
    }

    /**
     * Retrieves a list of visible tiles within the current context.
     *
     * @return an ArrayList containing the visible SpaceShipTile objects.
     */
    public ArrayList<SpaceShipTile> getVisibleTiles() {
        return VisibleTiles;

    }

    /**
     * Notifies the user about the wrong tiles.
     *
     * @param pos the list of positions of the tiles that are incorrect
     * @param nick the nickname of the main user involved
     * @param nickEff the nickname of the affected user
     * @throws Exception if an error occurs while showing the wrong tiles
     */
    public void wrongTiles(ArrayList<Integer> pos, String nick, String nickEff) throws Exception {
        generalView.showWrongTiles(pos, nick, nickEff);
    }

    /**
     * Retrieves the activated card.
     *
     * @return the activated card of type {@code Card}
     */
    public Card getActivatedcard() {
        return activatedcard;
    }

    /**
     * Retrieves the object currently held in hand.
     *
     * @return the object that is being held in hand, or null if none is being held.
     */
    public Object getThingInHand() {
        return ThingInHand;
    }

    /**
     * Retrieves the bank instance.
     *
     * @return the bank instance associated with this object
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Indicates whether it is time to refill.
     *
     * @return true if it is time to refill; false otherwise.
     */
    public boolean isTimetofill() {
        return timetofill;
    }

    /**
     * Retrieves the status indicating whether surrender is possible.
     *
     * @return a Boolean value where true indicates surrender is possible,
     *         and false indicates it is not.
     */
    public Boolean getCansurrend() {
        return cansurrend;
    }

    /**
     * Retrieves the current state of the game.
     *
     * @return the current game state as a value of the stateEnum enumeration.
     */
    public stateEnum getGameState() {
        return gameState;
    }

    /**
     * Retrieves the current state of the card.
     *
     * @return the current state of the card as a stateEnum value.
     */
    public stateEnum getCardState() {
        return cardState;
    }

    /**
     * Retrieves the count of finished players.
     *
     * @return the number of players who have finished.
     */
    public int getCountingfinishedplayers() {
        return countingfinishedplayers;
    }

    /**
     * Retrieves the final scores.
     *
     * @return A HashMap where the keys are Strings representing identifiers (e.g., names, IDs)
     *         and the values are Floats representing the corresponding final scores.
     */
    public HashMap<String, Float> getFinalScores() {
        return finalScores;
    }


    /**
     * Retrieves the message string.
     *
     * @return the message as a String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieves the list of ShipBoard objects.
     *
     * @return an ArrayList containing ShipBoard objects.
     */
    public ArrayList<ShipBoard> getShipBoards() {
        return ShipBoards;
    }

    /**
     * Initializes the bank object with default values.
     *
     * This method sets up the bank instance with a predefined set of values
     * representing the initial state or resources for the bank.
     * The bank is initialized with five numerical parameters, all set to 100.
     */
    public void fillBank() {
        this.bank = new Bank(100, 100, 100, 100, 100);

    }

    /**
     * Builds and returns a list of SpaceShipTile objects by reading data from a JSON file.
     * This method deserializes the JSON file located in the specified resource path into
     * a collection of SpaceShipTile objects.
     *
     * @return An ArrayList containing SpaceShipTile objects if the JSON file is successfully read and parsed,
     * or null if an error occurs during file reading or parsing.
     */
    public ArrayList<SpaceShipTile> buildTiles() {
        ArrayList<SpaceShipTile> tilesHeap = null;
        ObjectMapper mappertiles = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it/polimi/ingsw/json/tiles.json")) {
            if (input == null) {
                System.out.println("File JSON non trovato!");
            }
            tilesHeap = mappertiles.readValue(input, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del mazzo");
        }
        return tilesHeap;

    }

    /**
     * Initializes the game setup and state based on the number of players.
     * This includes configuring the flight board, setting up the bank,
     * and managing the hidden tiles for the game.
     *
     * @param playerSize the number of players in the game. Determines
     *                   the specific setup of hidden tiles and other
     *                   configurations.
     */
    public void initializationTest(int playerSize) {
        fillBank();
        flightBoard = new FlightBoard(playerSize, 18, bank, null);
        HiddenTiles = buildTiles();
        SpaceShipTile tileToPick, tiletopick2;
        switch (playerSize) {
            case 2:
                tileToPick = HiddenTiles.stream()
                        .filter(tile -> tile.getID() == 61)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                tiletopick2 = HiddenTiles.stream()
                        .filter(tile -> tile.getID() == 34)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                HiddenTiles.remove(tileToPick);
                HiddenTiles.remove(tiletopick2);
                break;
            case 3:
                tiletopick2 = HiddenTiles.stream()
                        .filter(tile -> tile.getID() == 34)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                HiddenTiles.remove(tiletopick2);
                break;
        }


    }

    /**
     * Initializes the Level 2 game setup based on the number of players and showable decks.
     *
     * @param playerSize   the number of players in the game
     * @param ShowableDecks a list of decks that are visible and usable in the game
     */
    public void initializationLEV2(int playerSize, List<Deck> ShowableDecks) {
        fillBank();
        flightBoard = new Lev2FlightBoard(playerSize, null, ShowableDecks, 24, bank, null);
        HiddenTiles = buildTiles();
        SpaceShipTile tileToPick, tiletopick2;
        switch (playerSize) {
            case 2:
                tileToPick = HiddenTiles.stream()
                        .filter(tile -> tile.getID() == 61)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                tiletopick2 = HiddenTiles.stream()
                        .filter(tile -> tile.getID() == 34)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                HiddenTiles.remove(tileToPick);
                HiddenTiles.remove(tiletopick2);
                break;
            case 3:
                tiletopick2 = HiddenTiles.stream()
                        .filter(tile -> tile.getID() == 34)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Tile not found"));
                HiddenTiles.remove(tiletopick2);
                break;
        }

    }

    /**
     * Updates the game configuration when a new game is created, initializing game state, players, and ships.
     *
     * @param level the level of the game, where 1 represents a specific mode, 0 represents test mode, and other values
     *              throw an IllegalArgumentException as unsupported levels.
     * @param showableDecks a list of decks that are visible or available to the players during the game.
     * @param players a list of players participating in the game.
     * @param hourglassResting the remaining number of hourglass time markers for the game.
     * @param surrenderResting the remaining number of surrender markers for the game.
     * @throws Exception if there is an error during game initialization, including invalid configurations.
     */
    public void updateGameCreated(int level, List<Deck> showableDecks, List<Player> players, int hourglassResting, int surrenderResting) throws Exception {
        if (level == 1) {
            initializationLEV2(players.size(), showableDecks);
        } else if (level == 0) {
            initializationTest(players.size());
        } else {
            throw new IllegalArgumentException("Unsupported level: " + level);
        }
        this.hourglassResting = hourglassResting;
        this.surrenderResting = surrenderResting;
        Map<Ship_Colour, Integer> colourTileMap = Map.of(
                Ship_Colour.RED, 52,
                Ship_Colour.BLUE, 33,
                Ship_Colour.YELLOW, 61,
                Ship_Colour.GREEN, 34
        );
        for (Player p : players) {
            ShipBoard ship = createShipBoard(level, flightBoard, p);
            int tileId = colourTileMap.getOrDefault(p.getColour(), -1);
            if (tileId < 0) {
                throw new RuntimeException("No start tile defined for colour " + p.getColour());
            }
            placeInitialTile(tileId, ship);
            ShipBoards.add(ship);
            PlayersNotSurrended.add(p.getUsername());
        }
        this.lev = level;
        gameState = stateEnum.BUILDING;
        message = "game started";

        ShipBoards.forEach(sb -> sb.getMyPlayer().setMyShip(sb));
        generalView.showFirstSight();
    }

    /**
     * Creates a ShipBoard instance based on the specified level.
     *
     * @param level        the level of the shipboard to create;
     *                     0 for a test flight, 1 for a level II shipboard.
     * @param flightBoard  the FlightBoard instance associated with the shipboard.
     * @param player       the Player instance associated with the shipboard.
     * @return a ShipBoard instance corresponding to the provided level.
     * @throws IllegalArgumentException if the level is not supported.
     */
    private ShipBoard createShipBoard(int level, FlightBoard flightBoard, Player player) {
        return switch (level) {
            case 1 -> new ShipBoard_LevelII(flightBoard, player);
            case 0 -> new ShipBoard_TestFlight(flightBoard, player);
            default -> throw new IllegalArgumentException("Unsupported level: " + level);
        };
    }

    /**
     * Places an initial tile on the ship's board at a specific position.
     *
     * @param tileId the ID of the tile to be placed
     * @param ship the ShipBoard where the tile will be placed
     * @throws RuntimeException if a tile with the specified ID cannot be found in the HiddenTiles collection
     */
    private void placeInitialTile(int tileId, ShipBoard ship) {
        SpaceShipTile tile = HiddenTiles.stream()
                .filter(t -> t.getID() == tileId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tile not found: ID=" + tileId));
        tile.setFlipped(true);
        HiddenTiles.remove(tile);
        ship.getShipMatrix()[2][3] = tile;
        tile.setAttachedShip(ship);
    }


    /**
     * Deposits the thing (tile or little deck) that the specified player currently has in hand.
     * This method identifies the player's ship board and processes the deposit action accordingly.
     * Updates the general view to reflect the changes.
     *
     * @param playerNickname the nickname of the player who is depositing the thing in hand
     * @throws Exception if there is an issue during the deposit process
     */
    public void depositThingInHand(String playerNickname) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                if (s.getMyPlayer().isHasLittleDeckInHand()) {
                    s.getMyPlayer().depositLittleDeckPlayer();
                    generalView.someoneDepositedLittleDeck(s.getMyPlayer().getLittleDeckInHand(), s.getMyPlayer());
                } else if (s.getMyPlayer().isHasTileInHand()) {
                    VisibleTiles.add(s.getMyPlayer().getTileInHand());
//                   int index= TilesInHand.indexOf(s.getMyPlayer().getTileInHand());
//                   TilesInHand.remove(index);
                    int tile = s.getMyPlayer().getTileInHand().getID();
                    TilesInHand.removeIf(t -> t.getID() == tile);
                    s.getMyPlayer().depositTilePlayer(s.getMyPlayer().getTileInHand());
                    generalView.addTileFlipped(tile);
                }
                generalView.updateThingInHand(s.getMyPlayer(), null);
            }
        }
    }

    /**
     * Selects a tile identified by its index and associates it with a specific player based on their nickname.
     * The method handles both visible and hidden tiles, updating the relevant collections as well as player's state.
     * Additionally, it updates the game state and user interface accordingly.
     *
     * @param TileIndex       the index of the tile to be picked
     * @param playerNickname  the nickname of the player picking the tile
     * @throws Exception      if any errors occur during the tile selection process
     */
    public void pickTileLMR(int TileIndex, String playerNickname) throws Exception {
        message = "You picked a tile!";
        SpaceShipTile tileToManage = null; //= HiddenTiles.get(TileIndex);

        for (SpaceShipTile t : HiddenTiles) {
            if (t.getID() == TileIndex) {
                tileToManage = t;
                break;
            }
        }
        if (tileToManage == null) {
            for (SpaceShipTile t : VisibleTiles) {
                if (t.getID() == TileIndex) {
                    tileToManage = t;
                    break;
                }
            }
        }
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                if (VisibleTiles.contains(tileToManage)) {
                    generalView.removeTileFlipped(Objects.requireNonNull(tileToManage).getID());
                    VisibleTiles.remove(tileToManage);
                } else HiddenTiles.remove(tileToManage);
                s.getMyPlayer().pickTilePlayer(tileToManage);
                Objects.requireNonNull(tileToManage).setFlipped(true);
                TilesInHand.add(tileToManage);
                generalView.updateThingInHand(s.getMyPlayer(), tileToManage);
            }
        }
    }

    /**
     * Inserts a tile into the specified coordinates on a player's ship board, rotates the tile as necessary,
     * and updates the appropriate states for the tile and player.
     *
     * @param TileIndex        The unique identifier of the tile to be inserted from the player's hand.
     * @param r                The row position on the ship board where the tile will be inserted.
     * @param c                The column position on the ship board where the tile will be inserted.
     * @param rotation         The number of 90-degree right rotations to apply to the tile before insertion.
     * @param playerNickname   The nickname of the player whose ship board will receive the tile.
     * @throws Exception       If an error occurs during the insertion or rotation process.
     */
    public void insertTileLMR(int TileIndex, int r, int c, int rotation, String playerNickname) throws Exception {
        SpaceShipTile tileToManage = null;
        for (SpaceShipTile t : TilesInHand) {
            if (t.getID() == TileIndex) {
                tileToManage = t;
                break;
            }
        }
        for (int i = 0; i < rotation; i++) {
            assert tileToManage != null;
            tileToManage.Rotate90right();
        }
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                if (s.getMyPlayer().getTileInHand().equals(tileToManage)) {
                    s.insertTile(tileToManage, r, c);
                    s.getMyPlayer().setTileInHand(null);
                    s.getMyPlayer().setHasTileInHand(false);
                    tileToManage.setAttachedShip(s);
                }
//                depositThingInHand(playerNickname);
                generalView.addTile(tileToManage, r, c, s);
            }
        }
    }

    /**
     * Allows a player to pick a little deck based on the specified deck index
     * and player nickname. The corresponding actions are then updated in the
     * general view to reflect the selection.
     *
     * @param deckIndex the index of the little deck to be picked
     * @param playerNickname the nickname of the player who is making the selection
     * @throws Exception if an error occurs during the deck picking process
     */
    public void pickLittleDeckLMR(int deckIndex, String playerNickname) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                s.getMyPlayer().pickLittleDeck(deckIndex);
                generalView.someonePickedLittleDeck(s.getFlightBoard().getShowableDeck(deckIndex), s.getMyPlayer());
            }
        }
    }

    /**
     * Automatically fills tiles on shipboards with necessary components such as battery units
     * and lonely passengers through predefined methods.
     *
     * This method iterates through a collection of shipboards and ensures
     * that each shipboard has its tiles populated with the following:
     * - Battery units by invoking the {@code addBatteryUnitLMR(ShipBoard)} method.
     * - Lonely passengers by invoking the {@code addLonelyPassengers(ShipBoard)} method.
     *
     * @throws Exception if an error occurs while processing shipboards
     *                   or executing the filling methods on tiles.
     */
    public void AutomaticFillTile() throws Exception {
        for (ShipBoard s : ShipBoards) {
            addBatteryUnitLMR(s);
            addLonelyPassengers(s);
        }
    }

    /**
     * Adds a SpaceShipTile to the provided list if it exists at the specified position
     * on the ship board and its type is SSTTypes.Tile_AlienLifeSupport.
     *
     * @param list the list to which the SpaceShipTile will be added if conditions are met
     * @param r the row index on the ship board
     * @param c the column index on the ship board
     * @param s the ship board containing the SpaceShipTile matrix
     */
    private void addSupportIfAny(List<SpaceShipTile> list, int r, int c, ShipBoard s) {
        if (r < 0 || r >= 5 || c < 0 || c >= 7) return;
        SpaceShipTile t = s.getShipMatrix()[r][c];
        if (t != null && t.getType() == SSTTypes.Tile_AlienLifeSupport) {
            list.add(t);
        }
    }

    /**
     * Adds passengers to empty and lonely cabin tiles in the given spaceship board.
     * A lonely cabin is determined based on its adjacency to other tiles with specific conditions,
     * such as the presence of support tiles or alien colors not already on board.
     *
     * @param s The spaceship board where lonely passengers need to be added.
     *          It contains information about the board layout and tile states.
     * @throws Exception If any error occurs during the process of adding passengers.
     */
    private void addLonelyPassengers(ShipBoard s) throws Exception {
        Set<AlienColor> coloursAlreadyOnBoard = new HashSet<>();
        for (SpaceShipTile alienTile : s.getShipBoardIterable().getTilesOfType(SSTTypes.Tile_Cabin)) {
            if (alienTile.getIsThereAlien()) {
                coloursAlreadyOnBoard.add(alienTile.getAlienColor());
            }
            if (coloursAlreadyOnBoard.size() == 2) {
                break;
            }
        }
        for (SpaceShipTile cabin : s.getShipBoardIterable().getTilesOfType(SSTTypes.Tile_Cabin)) {
            if (cabin.getNumPassengers() > 0) {
                continue;
            }
            if (cabin.getIsThereAlien()) {
                continue;
            }
            List<Integer> pos = ShipBoard_iterable.getTilePosition(cabin);
            int row = pos.get(0);
            int col = pos.get(1);
            if (row == 2 && col == 3) {
                cabin.addPassenger();
                generalView.addPassenger(s, row, col);
                cabin.addPassenger();
                generalView.addPassenger(s, row, col);
                continue;
            }
            List<SpaceShipTile> adjacentSupports = new ArrayList<>();
            addSupportIfAny(adjacentSupports, row, col + 1, s);
            addSupportIfAny(adjacentSupports, row, col - 1, s);
            addSupportIfAny(adjacentSupports, row - 1, col, s);
            addSupportIfAny(adjacentSupports, row + 1, col, s);

            Set<AlienColor> freeColours = new HashSet<>();
            for (SpaceShipTile support : adjacentSupports) {
                AlienColor colour = support.getColor();
                if (!coloursAlreadyOnBoard.contains(colour)) {
                    freeColours.add(colour);
                }
            }

            boolean lonely =
                    adjacentSupports.isEmpty()
                            || freeColours.isEmpty();

            if (lonely) {
                cabin.addPassenger();
                generalView.addPassenger(s, row, col);
                cabin.addPassenger();
                generalView.addPassenger(s, row, col);
            }
        }
    }


    /**
     * Adds battery units from the ship's tile matrix to the general view and triggers the recharge process for battery tiles.
     *
     * This method iterates through the ship's board matrix to identify all tiles of type {@code SSTTypes.Tile_BatteryComponent}.
     * Once identified, it processes their battery capacity and recharges them, updating the general view with their positions
     * and capacities.
     *
     * @param shipBoard The ShipBoard object containing the tile matrix representation of the spaceship.
     * @throws Exception If an error occurs during processing or updating the general view.
     */
    private void addBatteryUnitLMR(ShipBoard shipBoard) throws Exception {
        SpaceShipTile[][] matrix = shipBoard.getShipMatrix();
        for (int riga = 0; riga < 5; riga++) {
            for (int colonna = 0; colonna < 7; colonna++) {
                SpaceShipTile t = matrix[riga][colonna];
                if (t != null && t.getType() == SSTTypes.Tile_BatteryComponent) {
                    t.getTileBattCapacity();
                    //flightBoard.getBank().addBattery(-capacity);
                    t.RechargeAll();
                    // flightBoard.getBank().addBattery(t.getTileBattCapacity());
                    ArrayList<ArrayList<Integer>> posMega = new ArrayList<>();
                    ArrayList<Integer> posLittle = new ArrayList<>();
                    posLittle.add(riga);
                    posLittle.add(colonna);
                    posLittle.add(t.getTileBattCapacity());
                    posMega.add(posLittle);
                    generalView.updateBatteries(posMega, shipBoard, false);
                }
            }
        }
    }

    /**
     * Adds either an alien or humans to a specified tile on the player's ship board,
     * depending on the inputs provided. This action modifies the state of the tile,
     * the player's ship, and updates the game's general view.
     *
     * @param playerNickname the nickname of the player whose ship board is being modified
     * @param wantAlien a boolean indicating whether to add an alien (true) or humans (false)
     * @param alienColor the color of the alien to be added, if adding an alien
     * @param row the row index of the target tile on the player's ship board
     * @param column the column index of the target tile on the player's ship board
     * @throws Exception if any unexpected error occurs during the execution
     */
    public void addAlienOrHumansLMR(String playerNickname, boolean wantAlien, AlienColor alienColor, int row, int column) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (!s.getMyPlayer().getUsername().equals(playerNickname)) {
                continue;
            }
            SpaceShipTile tile = s.getShipMatrix()[row][column];
            ShipBoard_LevelII ship = (ShipBoard_LevelII) s.getMyPlayer().getMyShip();
            if (!(tile instanceof Tile_HousingUnit)
                    || tile.getNumPassengers() != 0
                    || tile.getIsThereAlien()) {
                return;
            }
            Tile_HousingUnit housingUnit = (Tile_HousingUnit) tile;

            if (wantAlien) {
                if (tile.getNumPassengers() > 0) {
                    return;
                }
                if ((alienColor == AlienColor.VIOLET && ship.isVioletAlien())
                        || (alienColor == AlienColor.BROWN && ship.isBrownAlien())) {
                    return;
                }
                SpaceShipTile[][] m = ship.getShipMatrix();
                int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                boolean ok = false;
                for (int[] d : dirs) {
                    int nr = row + d[0], nc = column + d[1];
                    if (nr >= 0 && nr < m.length && nc >= 0 && nc < m[0].length) {
                        SpaceShipTile adj = m[nr][nc];
                        if (adj.getType() == SSTTypes.Tile_AlienLifeSupport
                                && adj.getColor() == alienColor) {
                            ok = true;
                            break;
                        }
                    }
                }
                if (!ok) {
                    return;
                }
                tile.setAlienColor(alienColor);
                ship.addAlien(alienColor);
                tile.setIsThereAlien(true);
                generalView.addAlien(s, row, column, alienColor);

            } else {
                ((Tile_HousingUnit) tile).addPassengerTileHousing();
                ((Tile_HousingUnit) tile).addPassengerTileHousing();
                generalView.addPassenger(s, row, column);
                generalView.addPassenger(s, row, column);
            }
            return;
        }
    }


    /**
     * Shuts down the system or application and displays the provided reason for the shutdown.
     *
     * @param reason The reason for the shutdown, which will be displayed to the user.
     * @throws RemoteException If a remote communication error occurs during the shutdown process.
     */
    public void shutdown(String reason) throws RemoteException {
        generalView.showShutdown(reason);
    }

    /**
     * Sets the position of a player on the flight board based on the player's nickname.
     * Updates the internal state of the flight board, adjusts player rankings, and updates the general view.
     *
     * @param playerNickname the unique nickname of the player whose position is being updated
     * @param pos the position on the flight board to move the player to
     * @throws Exception if an error occurs during the process, such as invalid player data or position updates
     */
    public void setPlayerPosInFlightBoard(String playerNickname, int pos) throws Exception {
        int id = -1;
        Player i = null;
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                id = s.getMyPlayer().getPlayerId();
                i = s.getMyPlayer();
            }
        }
        flightBoard.getBoard().set(pos, id);
        Objects.requireNonNull(i).setPlayerPosOnboard(pos);
        boolean isPresent = false;
        for (Player p : flightBoard.getRanking()) {
            if (p.getUsername().equals(playerNickname)) {
                isPresent = true;
                break;
            }
        }
        if (!isPresent) {
            flightBoard.getRanking().add(i);
        }
        generalView.movePlayerOnFlightboard(i, pos, -1);
    }

    /**
     * Moves the player with the specified nickname to a new position on the flight board.
     *
     * @param playerNickname the nickname of the player to be moved
     * @param pos the new position on the flight board to move the player to
     * @throws Exception if an error occurs during the move operation
     */
    public void movePlayerInFlightBoard(String playerNickname, int pos) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                int oldPos = s.getMyPlayer().getPositionOnBoard();
                flightBoard.movePlayer(s.getMyPlayer(), pos);
                int newPos = s.getMyPlayer().getPositionOnBoard();
                generalView.movePlayerOnFlightboard(s.getMyPlayer(), newPos, oldPos);
                break;
            }
        }
    }

    /**
     * Activates the "abandoned ship taken" effect for a specific player. This process involves removing passengers
     * from the ship, updating cosmic credits for the affected player, applying penalties, and updating the game view.
     *
     * @param player The player who is activating the abandoned ship effect.
     * @param posPers A nested list representing the positions of the passengers to be removed from the ship.
     * @throws Exception If an error occurs during the execution of the effect.
     */
    public void abandonedShipTakenActivate(Player player, ArrayList<ArrayList<Integer>> posPers) throws Exception {
        Player p;

        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                p = s.getMyPlayer();
                s.removePassengers(posPers, activatedcard.viewCrewPenalty());
                generalView.removePassengers(s, posPers, activatedcard.viewCrewPenalty());
                s.addCosmicCredits(activatedcard.viewMoneyEarning());
                generalView.updateCosmicCredits(p, activatedcard.viewMoneyEarning());
                int oldPos = s.getMyPlayer().getPositionOnBoard();
                flightBoard.movePlayer(s.getMyPlayer(), -activatedcard.viewFlightPenalty());
                int newPos = s.getMyPlayer().getPositionOnBoard();
                generalView.movePlayerOnFlightboard(s.getMyPlayer(), newPos, oldPos);
                generalView.endedEffect();
                break;
            }
        }
    }

    /**
     * Activates an abandoned station for a player, modifying their ship and flight board status.
     *
     * @param player The player who is activating the abandoned station.
     * @param fb The flight board where the player's position may be updated.
     * @param yOn A flag indicating whether the activation is on or off.
     * @param storagetiles A list of storage tiles to add to the player's ship if provided.
     * @param newgoods A list of goods to add to the player's ship if provided.
     * @throws Exception If an issue occurs during activation.
     */
    public void chooseAbandonedStationActivate(Player player, FlightBoard fb, boolean yOn,
                                               ArrayList<ArrayList<Integer>> storagetiles,
                                               ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                if (yOn) {
                    if (storagetiles != null && newgoods != null) {
                        s.addGoods(storagetiles, newgoods);
                        generalView.updateGoods(s, storagetiles, newgoods);
                    } else {
                        generalView.looseAllGoods(s);
                    }
                    int posPenalty = -1 * activatedcard.getDaysPenalty();
                    int oldPos = s.getMyPlayer().getPositionOnBoard();
                    flightBoard.movePlayer(s.getMyPlayer(), posPenalty);
                    int newPos = s.getMyPlayer().getPositionOnBoard();
                    generalView.movePlayerOnFlightboard(s.getMyPlayer(), newPos, oldPos);
                }
                break;
            }

        }
    }

    /**
     * Finalizes the building phase for a specific player and updates the game state accordingly.
     *
     * @param playerNickname the nickname of the player who has completed the building phase
     * @param positionwheretogo the position the player intends to move to after ending the building phase
     * @throws Exception if any error occurs during the operation
     */
    public void endbuilding(String playerNickname, int positionwheretogo) throws Exception {
        message = "You ended building!!";
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (s.getShipMatrix()[i][j] == null || s.getShipMatrix()[i][j].getID() == -1) {
                            ArrayList<Type_side_connector> emptyConnector = new ArrayList<>();
                            for (int k = 0; k < 4; k++) {
                                emptyConnector.add(Type_side_connector.SMOOTH_SIDE);
                            }
                            s.getShipMatrix()[i][j] = new SpaceShipTile(0, emptyConnector, SSTTypes.Tile_NonAccesiblePlace);
                            s.getShipMatrix()[i][j].setAttachedShip(s);
                        }
                        if (s.getShipMatrix()[i][j].getAttachedShip() == null) {
                            s.getShipMatrix()[i][j].setAttachedShip(s);
                        }
                    }
                }
                break;
            }
        }
        Player p = null;
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                p = s.getMyPlayer();
                break;
            }

        }
        if (p != null && !p.isBlocked()) {
            p.setBlocked(true);
        }
        generalView.endBuilding(playerNickname);
    }

    /**
     * Completes the building phase for all ship boards in the game.
     *
     * This method iterates through all ship boards associated with their respective players,
     * performs a check for empty or invalid tiles in the ship matrix, and assigns default
     * tiles where necessary. The default tiles are created with a smooth-side connector and
     * marked as non-accessible places. If a tile is not yet associated with an attached ship,
     * it sets the associated ship to the current ship board.
     *
     * Additionally, the player associated with each ship board is unblocked,
     * allowing further actions or progress in the game.
     *
     * Lastly, calls the `AutomaticFillTile` method to perform any additional
     * automated tile-filling tasks.
     *
     * @throws Exception if there is an issue during the completion of the building phase.
     */
    public void completeBuildingPhase() throws Exception {
        for (ShipBoard s : ShipBoards) {
            Player p = s.getMyPlayer();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (s.getShipMatrix()[i][j] == null || s.getShipMatrix()[i][j].getID() == -1) {
                        ArrayList<Type_side_connector> emptyConnector = new ArrayList<>();
                        for (int k = 0; k < 4; k++) {
                            emptyConnector.add(Type_side_connector.SMOOTH_SIDE);
                        }
                        s.getShipMatrix()[i][j] = new SpaceShipTile(0, emptyConnector, SSTTypes.Tile_NonAccesiblePlace);
                        s.getShipMatrix()[i][j].setAttachedShip(s);
                    }
                    if (s.getShipMatrix()[i][j].getAttachedShip() == null) {
                        s.getShipMatrix()[i][j].setAttachedShip(s);
                    }
                }
            }
            p.setBlocked(false);
        }
        AutomaticFillTile();
    }


    /**
     * Ends the building phase for all players by marking them as blocked
     * if they are not included in the flight board ranking.
     *
     * This method iterates through all ShipBoard instances in the collection
     * and checks if the player associated with each ShipBoard is present
     * in the ranking list retrieved from the flightBoard. If a player's ShipBoard
     * is not found in the ranking, the player is marked as blocked by
     * setting their 'blocked' status to true.
     *
     * @throws Exception if an error occurs during execution
     */
    public void endBuildingPhaseForAll() throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (!flightBoard.getRanking().contains(s.getMyPlayer())) {
                s.getMyPlayer().setBlocked(true);
            }
        }
    }

    /**
     * Updates the state of the game and activates a specific card.
     * This method changes game states, sets the active card, and updates the card view.
     *
     * @param card the card to be activated and updated within the game
     * @throws Exception if an error occurs while updating the card
     */
    public void updateCardUse(Card card) throws Exception {
        message = "New card or effect Activated";
        this.dice = -1;
        cardState = stateEnum.CARD_STARDED;
        gameState = stateEnum.FLIGHT;
        this.activatedcard = card;
        generalView.updateCard();

    }

    /**
     * Updates the card state based on the provided state object.
     *
     * @param state the state object that contains the information required to update the card state
     * @throws Exception if an error occurs during the process of updating the card state
     */
    public void updateCardUseSTATE(c_State state) throws Exception {

        this.dice = -1;
        cardState = stateEnum.CARD_STARDED;
        gameState = stateEnum.FLIGHT;
        this.activatedcard.setStatENUMonly(state);
        generalView.updateCard();

    }

    /**
     * Updates the current game status and triggers the status update in the general view.
     *
     * @param stato the new state to set for the game
     * @throws Exception if an error occurs while updating the status
     */
    public void updateStatus(stateEnum stato) throws Exception {
        message = "";
        gameState = stato;
        generalView.updateStatus();
    }

    /**
     * Updates the final scores and displays them in the general view.
     *
     * @param finalScores A HashMap containing the final scores,
     *                    where the key is a String representing a player's name
     *                    and the value is a Float representing the score.
     * @throws Exception If there is an error during the update or display of scores.
     */
    public void updateFinalScores(HashMap<String, Float> finalScores) throws Exception {
        this.finalScores = finalScores;
        generalView.showFinalScore(finalScores);
    }

    /**
     * Updates the positions of batteries for a given player and performs corresponding actions
     * based on the provided positions. If the positions are null, all batteries are set as lost.
     *
     * @param player The player whose battery positions are to be updated.
     * @param posBatAndNumBattXPos A list of lists containing the positions and number of batteries
     *                             for the player. If null, all batteries are considered lost.
     * @throws Exception If any error occurs during the update process.
     */
    public void updateAssertBatteriesPos(Player player, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                if (posBatAndNumBattXPos == null) {
                    s.looseAllBatteris();
                    generalView.looseAllbatteries(s);
                } else {
                    s.assertBatteryPos(posBatAndNumBattXPos);
                    generalView.updateBatteries(posBatAndNumBattXPos, s, false);
                }
                break;
            }
        }
    }

    /**
     * Updates and adds goods to the shipboard of a specified player. This method identifies the
     * specific shipboard associated with the player and updates it with the provided goods data.
     *
     * @param player The player whose shipboard should be updated with the new goods.
     * @param posGoods A list of positions where the goods are to be added, represented
     *                 as a list of integer pairs.
     * @param goodsSets A list of goods to be added, represented as collections of Goods objects.
     * @throws Exception If an error occurs during the update or addition of goods.
     */
    public void updateAddGoods(Player player, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                s.addGoods(posGoods, goodsSets);
                generalView.updateGoods(s, posGoods, goodsSets);
                break;
            }
        }
    }

    /**
     * Updates the game by removing the specified passengers from the given player's shipboard
     * and applies the consequences. This method also updates the general view accordingly.
     *
     * @param player The player whose passengers will be removed.
     * @param c The consequences object containing penalties or effects to apply.
     * @param pass A nested list of integers representing the passengers to be removed.
     * @throws Exception If an error occurs during the update operation.
     */
    public void updateChoosePassengersToLose(Player player, Consequences c, ArrayList<ArrayList<Integer>> pass) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                s.removePassengers(pass, c.getCrewPen());
                generalView.removePassengers(s, pass, c.getCrewPen());
                break;
            }
        }

    }

    /**
     * Activates the financial status effect on the given players list, adjusting their positions
     * on the flight board and updating the general view accordingly. This method applies penalties
     * to eligible players based on an activated card's properties.
     *
     * @param player The list of players to process, where the effect will be applied if criteria are met.
     * @param fb The flight board instance on which players' positions will be adjusted.
     * @throws Exception If there is an error during the execution of the method.
     */
    public void planetFinStatActivate(ArrayList<Player> player, FlightBoard fb) throws Exception {

        Player p = null;
        ArrayList<Player> h = new ArrayList<>(player);
        h.reversed();
        for (Player t : h) {
            for (String pl : PlayersNotSurrended)
                if (pl.equals(t.getUsername())) {
                    p = t;
                    break;
                }
            if (p != null) {
                for (ShipBoard s : ShipBoards) {
                    if (s.getMyPlayer().getUsername().equals(p.getUsername())) {
                        int oldPos = s.getMyPlayer().getPositionOnBoard();
                        flightBoard.movePlayer(s.getMyPlayer(), -1 * activatedcard.getDaysPenalty());
                        int newPos = s.getMyPlayer().getPositionOnBoard();
                        generalView.movePlayerOnFlightboard(s.getMyPlayer(), newPos, oldPos);
                    }
                }
            }
        }
        generalView.endedEffect();
    }

    /**
     * Updates the consequence of lost days for a player on the flight board.
     * This method adjusts the player's position based on the number of lost days
     * and handles any additional effects if the process has finished.
     *
     * @param player The player whose position needs to be updated.
     * @param fb The flight board on which the player's position is updated.
     * @param numDays The number of days lost by the player.
     * @param finished A Boolean indicating if the effect process has ended.
     * @throws Exception If an unexpected error occurs during the operation.
     */
    public void updateConsequenceLostDays(Player player, FlightBoard fb, int numDays, Boolean finished) throws Exception {
        Player p = null;

        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                p = s.getMyPlayer();
                break;
            }
        }
        System.out.println("Rank size: " + flightBoard.getPlayerRankList().size());
        System.out.println("Index of " + Objects.requireNonNull(p).getUsername() + ": " + flightBoard.getPlayerRankList().indexOf(p));
        int oldPos = p.getPositionOnBoard();
        flightBoard.movePlayer(p, -1 * numDays);
        if (finished) {
            generalView.endedEffect();
        }
        int newPos = p.getPositionOnBoard();
        generalView.movePlayerOnFlightboard(p, newPos, oldPos);
    }

    /**
     * Updates the state of all goods for a specific player by triggering the loss of goods
     * across all associated ship boards.
     *
     * @param player The player whose goods are to be updated. The associated ship boards
     *               will be determined based on this player's username.
     * @param finished Indicates whether the current process or operation is completed.
     * @param batttoloose A flag indicating whether the condition to enforce a battle loss is met.
     * @param allbatlost A flag indicating whether all battles have been lost by the player.
     * @throws Exception If any error occurs during the execution of the method.
     */
    public void updateLooseAllGoods(Player player, Boolean finished, Boolean batttoloose, Boolean allbatlost) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                s.looseAllGoods();
                generalView.looseAllGoods(s);
            }
        }


    }

    /**
     * Handles error scenarios by delegating the error handling operation to the general view.
     * This method is invoked when an error state needs to be communicated or processed.
     *
     * @throws Exception if an error occurs during the execution of the error handling logic
     */
    public void onError() throws Exception {
        generalView.onError();
    }

    /**
     * Updates the calculations for the player's smuggler's firepower and processes the
     * consequences based on comparison with the activated card's requirements. This method
     * also handles penalties for goods and batteries when firepower conditions are not met.
     *
     * @param player the player whose smuggler's calculations are to be updated.
     * @param cannonPos a list of lists representing the positions of cannons.
     * @param batteriesPos a list of lists representing the positions of batteries.
     * @throws Exception if any error occurs during the processing of the smuggler's calculations.
     */
    public void updateSmugglersCalc(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                float playerFirePower = s.getTotalFirePower(cannonPos, batteriesPos, player);
                generalView.updateBatteries(batteriesPos, s, false);
                // Confronta con la fire power della carta
                if (!(playerFirePower >= activatedcard.getFirePower())) {
                    int checkprel = s.getMySortedGoods().size() - activatedcard.getGoodsPenalty();
                    if (checkprel > 0) {
                        message = "You have to place your remaining goods";
                    } else if (checkprel == 0) {
                        s.looseAllGoods();
                        generalView.looseAllGoods(s);
                    } else {
                        s.looseAllGoods();
                        generalView.looseAllGoods(s);
                        int battnum = s.getBatteriesNumber();
                        if (-checkprel < battnum) {
                            message = "You have to place your remaining batteries";
                            s.looseAllBatteris();
                            generalView.looseAllbatteries(s);
                        } else {
                            s.looseAllBatteris();
                            generalView.looseAllbatteries(s);
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * Updates the lost batteries for a given player during the game and notifies the appropriate components.
     *
     * @param p                     The player whose lost batteries are being updated.
     * @param posBatAndNumBattXPos  A 2D list containing position and number of batteries per position.
     *                              Each inner list represents a position and the associated battery count.
     * @param numbatt               The total number of batteries to update.
     * @throws Exception            If the battery distribution is incorrect or any other processing error occurs.
     */
    public void updateLostBatteriesSmug(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos, int numbatt) throws Exception {
        message = p.getUsername() + " batteries are updated!, next player!";
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(p.getUsername())) {
                if (s.checkCorrectBatteriesDistribution(posBatAndNumBattXPos, numbatt)) {
                    s.assertBatteryPos(posBatAndNumBattXPos);
                    //you're placing back your remaining batteries
                    generalView.updateBatteries(posBatAndNumBattXPos, s, false);
                    break;
                }
            }
        }
    }


    /**
     * This method updates the goods lost or removed in a smuggling scenario
     * by interacting with the player's shipboard. It validates and compares
     * the positioning of goods and updates the shipboard and the general view accordingly.
     *
     * @param p               The player associated with the shipboard to be updated.
     * @param posGoods        The positions of the goods to be updated on the shipboard.
     * @param goodsSets       The list of all sets of goods organized for comparison.
     * @param goodsListDiPrima The list of goods from the primary or initial state for validation.
     * @throws Exception      If an error occurs while processing or updating goods on the shipboard.
     */
    public void updateLostGoodsSmug(Player p, ArrayList<ArrayList<Integer>> posGoods, ArrayList<ArrayList<Goods>> goodsSets, ArrayList<Goods> goodsListDiPrima) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(p.getUsername())) {
                if (s.checkAndCompareGoodsPosition(goodsListDiPrima, goodsSets, posGoods)) {
                    s.addGoods(posGoods, goodsSets);
                    generalView.updateGoods(s, posGoods, goodsSets);
                    break;
                }
            }
        }
    }

    /**
     * Allows the specified player to choose whether to claim a reward. If the player accepts
     * the reward, it processes the reward by validating the storage tiles and new goods,
     * applying any associated penalties, adding goods to the player's storage, and updating
     * the flight board and game view accordingly. If the player refuses the reward, no changes
     * are made.
     *
     * @param yOn A boolean indicating if the player chooses to claim the reward (true) or refuse it (false).
     * @param player The player who is deciding whether to claim the reward.
     * @param storagetiles The storage tiles representing where goods will be stored if the reward is claimed.
     * @param newgoods A list of new goods to be added to storage if the reward is claimed.
     * @throws Exception If an error occurs during the reward claiming process.
     */
    public void chooseToClaimRewardSmug(boolean yOn, Player player, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        if (yOn) {
            System.out.println("Player accepted reward");
            for (ShipBoard s : ShipBoards) {
                if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                    if (s.checkGoodsInput(storagetiles, newgoods, activatedcard.getGoods())) {
                        int oldPos = s.getMyPlayer().getPositionOnBoard();
                        s.getFlightBoard().movePlayer(s.getMyPlayer(), -1 * activatedcard.getDaysPenalty());
                        s.addGoods(storagetiles, newgoods);
                        int newPos = s.getMyPlayer().getPositionOnBoard();
                        generalView.movePlayerOnFlightboard(s.getMyPlayer(), newPos, oldPos);
                        generalView.updateGoods(s, storagetiles, newgoods);
                    }
                }
            }
        } else {
            System.out.println("Player refused reward");
        }
    }

    /**
     * Determines and processes the cannon battery positions chosen by the player.
     *
     * @param player the player who is choosing cannon battery positions
     * @param cannonPos the list of positions for cannons, represented as a 2D array of integers
     * @param batteriesPos the list of positions for batteries, represented as a 2D array of integers
     * @throws Exception if an error occurs during the process of retrieving or updating data
     */
    public void slaversChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                float playerFirePower = s.getTotalFirePower(cannonPos, batteriesPos, player);
                generalView.updateBatteries(batteriesPos, s, false);
            }
        }
    }

    /**
     * Handles the process of slavers forcing a player to lose passengers on their spaceship.
     * The method determines the number of passengers to be removed based on the player's current state
     * and the specified tiles, and executes the removal operation accordingly.
     *
     * @param player The player whose passengers are to be removed.
     * @param yOn A boolean representing a condition related to the operation (specific use case not detailed).
     * @param tiles A nested list of integers where each inner list represents a tile on the spaceship.
     *              Each inner list should contain three integers:
     *              [positionX, positionY, number of passengers to remove at this tile].
     * @throws Exception If an error occurs during the removal process.
     */
    public void slaversChoosePassengersToLose(Player player, boolean yOn, ArrayList<ArrayList<Integer>> tiles) throws Exception {
        message = "You lost some passengers";
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                int numPass = activatedcard.getCrewLost();

                for (ArrayList<Integer> t : tiles) {
                    int positionx = t.get(0);
                    int positiony = t.get(1);
                    int passengerToRemove = t.get(2);
                    SpaceShipTile batteryTile = s.getShipMatrix()[positionx][positiony];
                    if (numPass < passengerToRemove) {
                        removePassengersLMR(positionx, positiony, numPass, s);
                        break;
                    } else {
                        removePassengersLMR(positionx, positiony, numPass, s);
                        numPass -= passengerToRemove;
                        if (numPass == 0) {
                            break;
                        }
                    }
                }
                generalView.removePassengers(s, tiles, activatedcard.getCrewLost());

            }
        }
    }

    /**
     * Handles the scenario where slavers choose to claim a reward. This method adjusts
     * the player's position and cosmic credits based on the given conditions.
     *
     * @param yOn determines whether the reward claiming process should proceed.
     *            If true, the method executes its logic for the provided player.
     * @param player the player instance whose position and cosmic credits are updated
     *               based on the slavers' decision.
     * @throws Exception if an error occurs during the execution of the reward claiming process.
     */
    public void slaversChooseToClaimReward(boolean yOn, Player player) throws Exception {
        if (yOn) {
            for (ShipBoard s : ShipBoards) {
                if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                    int oldPos = s.getMyPlayer().getPositionOnBoard();
                    s.getFlightBoard().movePlayer(s.getMyPlayer(), -1 * activatedcard.getDaysPenalty());
                    s.addCosmicCredits(activatedcard.getMoneyGained());
                    int newPos = s.getMyPlayer().getPositionOnBoard();
                    generalView.movePlayerOnFlightboard(s.getMyPlayer(), newPos, oldPos);
                    generalView.updateCosmicCredits(player, activatedcard.getMoneyGained());
                }
            }
        }
    }

    /**
     * Allows pirates to choose the positions of cannon batteries on the ship board based on the given player and configurations.
     *
     * @param player The player for whom the cannon battery positions are being chosen.
     * @param cannonPos A nested list of integers representing the positions of cannons.
     * @param batteriesPos A nested list of integers representing the positions of batteries.
     * @throws Exception If an error occurs during the process of choosing positions.
     */
    public void piratesChooseCannonBatteryPos(Player player, ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                float playerFirePower = s.getTotalFirePower(cannonPos, batteriesPos, player);
                generalView.updateBatteries(batteriesPos, s, false);
            }
        }
    }


    /**
     * Handles the decision-making process for how pirates choose to defend against incoming meteor shots.
     *
     * @param player               The player making the decision.
     * @param howToDefenceFromShots An ArrayList specifying the defense strategy against shots.
     * @param shot                 The incoming shot that the player or pirates need to respond to.
     * @param dice                 The dice value that influences the outcome of the defense strategy.
     * @throws Exception           If an error occurs during the decision-making or shot management process.
     */
    public void piratesChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, Shot shot, int dice) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (player.getUsername().equals(s.getMyPlayer().getUsername())) {
                manageShotReceivedLMR(shot, howToDefenceFromShots, dice, s);
                if (s.checkCorrectDefence(howToDefenceFromShots)) {
                    if (howToDefenceFromShots == null) {
                        generalView.updateBatteries(null, s, false);
                    } else {
                        ArrayList<ArrayList<Integer>> defence = new ArrayList<>();
                        ArrayList<Integer> def = new ArrayList<>();
                        def.add(howToDefenceFromShots.get(2));
                        def.add(howToDefenceFromShots.get(3));
                        defence.add(def);
                        generalView.updateBatteries(defence, s, false);
                    }
                }
            }
        }
    }


    /**
     * Allows a pirate player to claim a reward or penalty based on specific game conditions.
     * If the player is part of the current game board and `yOn` is true, this method deducts
     * penalty days and credits cosmic money to the player. It also updates the player's position
     * on the game board and reflects these changes in the view.
     *
     * @param yOn a boolean indicating whether the reward claiming process is enabled
     * @param player the player attempting to claim the reward
     * @throws Exception if any error occurs during the reward claiming process
     */
    public void piratesChooseToClaimReward(boolean yOn, Player player) throws Exception {
        if (yOn) {
            for (ShipBoard s : ShipBoards) {
                if (s.getMyPlayer().getUsername().equals(player.getUsername())) {
                    int oldPos = s.getMyPlayer().getPositionOnBoard();
                    s.getFlightBoard().movePlayer(s.getMyPlayer(), -1 * activatedcard.getDaysPenalty());
                    s.addCosmicCredits(activatedcard.getMoneyGained());
                    int newPos = s.getMyPlayer().getPositionOnBoard();
                    generalView.movePlayerOnFlightboard(s.getMyPlayer(), newPos, oldPos);
                    generalView.updateCosmicCredits(player, activatedcard.getMoneyGained());
                }
            }
        }
    }

    /**
     * Handles the process of selecting engines and batteries to initiate the motor functionality
     * for a player on the flight board. Verifies selections and updates corresponding game state
     * and visuals.
     *
     * @param PLAYER the player who is initiating the process
     * @param flightBoard the current flight board in play
     * @param enginesPos a list of positions specifying the engines chosen by the player
     * @param batteriesPos a list of positions specifying the batteries chosen by the player
     * @throws Exception if an error occurs during the process
     */
    public void openSpaceChooseToStartMotor(Player PLAYER, FlightBoard flightBoard, ArrayList<ArrayList<Integer>> enginesPos, ArrayList<ArrayList<Integer>> batteriesPos) throws Exception {
        message = "You choose a correct input";
        for (ShipBoard s : ShipBoards) {
            if (PLAYER.getUsername().equals(s.getMyPlayer().getUsername())) {
                if (s.checkCorrectEngineBatteries(enginesPos, batteriesPos, PLAYER)) {
                    int totMotPow = s.getTotalMotionPower(enginesPos, batteriesPos, PLAYER);
                    if (totMotPow == 0 && flightBoard instanceof Lev2FlightBoard) {
                        message = "Not enought Motion power, you left the flight!";

                        for (String p : PlayersNotSurrended) {
                            if (p.equals(PLAYER.getUsername())) PlayersNotSurrended.remove(p);
                            break;
                        }

                        generalView.updatePlayersInGame();
                    }
                    generalView.updateBatteries(batteriesPos, s, true);
                }
            }
        }
    }

    /**
     * Handles the player's choice on how to deal with incoming meteors in a game scenario.
     * This method validates the given defense strategy and applies the defense mechanism
     * for the meteor shots. Additionally, it updates the game view based on whether a
     * valid defense strategy is applied or not.
     *
     * @param player The player who is responding to the meteor scenario.
     * @param howToDefenceFromShots A list of integers representing the chosen defensive strategy for the current meteor shots.
     * @param shots A list of shots, each representing an incoming meteor that needs to be addressed.
     * @param dice The result of a dice roll to potentially influence the meteor encounter.
     * @param currentShot The index of the current shot being processed from the list of meteor shots.
     * @throws Exception If any error occurs during the processing of the meteor defense logic.
     */
    public void meteorCardChooseHowToFaceMeteors(Player player, ArrayList<Integer> howToDefenceFromShots, ArrayList<Shot> shots, int dice, int currentShot) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (player.getUsername().equals(s.getMyPlayer().getUsername())) {
                if (s.checkCorrectDefence(howToDefenceFromShots)) {
                    manageShotReceivedLMR(shots.get(currentShot), howToDefenceFromShots, dice, s);
                    if (howToDefenceFromShots == null) {
                        generalView.updateBatteries(null, s, false);
                    } else {
                        ArrayList<ArrayList<Integer>> defence = new ArrayList<>();
                        ArrayList<Integer> def = new ArrayList<>();
                        def.add(howToDefenceFromShots.get(2));
                        def.add(howToDefenceFromShots.get(3));
                        def.add(1);
                        defence.add(def);
                        generalView.updateBatteries(defence, s, false);
                    }
                }
            }
        }
    }

    /**
     * Activates the epidemic state for a set of specified spaceship tiles. This method processes
     * the ships of all active players (who are not blocked). It identifies cabin tiles with adjacent
     * cabins that either have passengers or aliens present and adds them to the AlreadyVisited set.
     * Subsequently, actions are triggered to reduce the number of passengers or remove aliens from
     * the affected tiles.
     *
     * @param AlreadyVisited a set of SpaceShipTile objects that tracks the tiles already visited and
     *                       processed during the activation of the epidemic state
     * @throws Exception if any unexpected issue arises during the processing or manipulation of
     *                   spaceship tiles, such as removing entities or updating views
     */
    public void epidemicStateBaseActivate(Set<SpaceShipTile> AlreadyVisited) throws Exception {
        ArrayList<Player> listaGioca = new ArrayList<>(flightBoard.getPlayerRankList());
        listaGioca.stream().filter(x -> !x.isBlocked()).forEach(player -> {
            SpaceShipTile[][] ship = player.getMyShip().getShipMatrix();
            IntStream.range(0, ship.length).forEach(i ->
                    IntStream.range(0, ship[i].length)
                            .filter(j -> ship[i][j] != null && SSTTypes.Tile_Cabin == (ship[i][j].getType()))
                            .mapToObj(j -> new int[]{i, j})
                            .filter(coords -> hasAdjacentCabin(ship, coords[0], coords[1]))
                            .filter(coords -> ship[coords[0]][coords[1]].getNumPassengers() > 0 || ship[coords[0]][coords[1]].getIsThereAlien())
                            .forEach(coords -> AlreadyVisited.add(ship[coords[0]][coords[1]]))
            );
        });
        for (SpaceShipTile t : AlreadyVisited) {
            if (t.getNumPassengers() > 0) {
                t.removePassenger(1);
                generalView.removePassengersFromTile(t.getAttachedShip(), t, 1);
            } else if (t.getIsThereAlien()) {
                if (t instanceof Tile_HousingUnit) {
                    removeAlienLMR((Tile_HousingUnit) t, t.getAttachedShip());
                    generalView.removeAlienFromTile(t.getAttachedShip(), t);
                }
            }
        }
    }

    /**
     * Checks if there is an adjacent cabin to the given position in the spaceship grid
     * that either has passengers or an alien present.
     *
     * @param ship  the spaceship grid represented as a 2D array of SpaceShipTile objects
     * @param i     the row index of the position being checked
     * @param j     the column index of the position being checked
     * @return true if there is an adjacent cabin with either passengers or an alien, false otherwise
     * @throws RuntimeException if the provided position is invalid or any unexpected condition occurs
     */
    private boolean hasAdjacentCabin(SpaceShipTile[][] ship, int i, int j) throws RuntimeException {
        List<int[]> matchingPositions = Stream.of(
                        new int[]{i - 1, j}, new int[]{i + 1, j},
                        new int[]{i, j - 1}, new int[]{i, j + 1}
                )
                .filter(pos -> pos[0] >= 0 && pos[0] < ship.length && pos[1] >= 0 && pos[1] < ship[0].length)
                .filter(pos -> ship[pos[0]][pos[1]].getType() == SSTTypes.Tile_Cabin)
                .filter(pos -> ship[pos[0]][pos[1]].getNumPassengers() > 0 || ship[pos[0]][pos[1]].getIsThereAlien())
                .toList();

        return !matchingPositions.isEmpty();
    }

    /**
     * Applies the "stardust effect" to all players on the flight board.
     * This method retrieves the current ranking of players, reverses the order,
     * and processes each player based on the number of their ship's exposed connectors.
     * Each player is moved backward on the flight board by the number of exposed connectors.
     * The visual representation of the player's position is updated accordingly.
     *
     * @throws Exception if there is an error during any of the operations performed.
     */
    public void stardustEffect() throws Exception {
        List<Player> playerRank = new ArrayList<>(flightBoard.getPlayerRankList());
        Collections.reverse(playerRank);

        for (Player player : playerRank) {
            int oldPos = player.getPositionOnBoard();
            int exposedConnectors = player.getMyShip().calculateExposedConnectors();
            flightBoard.movePlayer(player, -exposedConnectors);
            int newPos = player.getPositionOnBoard();
            generalView.movePlayerOnFlightboard(player, newPos, oldPos);

        }
    }


    /**
     * Triggers a message indicating that it is not the current user's turn.
     * The method displays a timed informational message with the text "Not your turn!"
     * for a set duration using the general view interface.
     *
     * @throws Exception if an error occurs while showing the message.
     */
    public void notYourTurn() throws Exception {
        message = "Not your turn!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Indicates that the timer has not been started and displays an informational message
     * with a specified duration through the general view.
     *
     * @throws Exception if an error occurs during the process of showing the message.
     */
    public void timerNotStarted() throws Exception {
        message = "Timer not started!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Indicates that a tile has not been flipped by setting an informational message
     * and displaying it using the general view.
     *
     * @throws Exception if the general view's display operation encounters an error
     */
    public void tileNotFlipped() throws Exception {
        message = "Tile not flipped!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Executes the noSurrender action by displaying a timed information message
     * in the general view. This method sets the message to "No surrender!"
     * and shows it for a duration of 4 seconds.
     *
     * @throws Exception if an error occurs while displaying the message
     */
    public void noSurrender() throws Exception {
        message = "No surrender!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Advances the game to the next player's turn.
     *
     * This method updates the game state by setting a message that indicates
     * the next player's turn and triggers a timed visual update in the general view.
     *
     * @throws Exception if an error occurs during the timed display operation.
     */
    public void nextPlayerTurn() throws Exception {
        message = "Next player!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Handles scenarios where the user provides invalid input.
     * This method displays a message to inform the user about the incorrect input,
     * and the message is shown for a limited duration.
     *
     * @throws Exception if an error occurs during the display of the message.
     */
    public void wrongInput() throws Exception {
        message = "Wrong input!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Processes and handles user input, ensuring that it is correct.
     * If the input is valid, it triggers a timed informational message
     * displayed by the general view with the text "Correct input!".
     *
     * @throws Exception if there is an issue during input validation or processing
     */
    public void correctInput() throws Exception {
        message = "Correct input!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Notifies that the wrong player is attempting to make a move
     * or perform an action during the game.
     *
     * This method sets a predefined message indicating the error
     * and displays it to the user using the general view's
     * timed information display method.
     *
     * @throws Exception if an unexpected error occurs while displaying the information.
     */
    public void wrongPlayer() throws Exception {
        message = "Wrong player!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Indicates the start of a card effect by displaying a timed message.
     *
     * This method sets the message to notify the user that the card effect has started
     * and instructs the general view to display the message for a limited duration.
     *
     * @throws Exception if an error occurs during the process of showing the message.
     */
    public void effectStarted() throws Exception {
        message = "Card Effect started!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Notifies that a player has won the game by displaying a timed message
     * with the player's nickname.
     *
     * @param playerNickname the nickname of the player who has won the game
     * @throws Exception if an error occurs while displaying the message
     */
    public void someoneWon(String playerNickname) throws Exception {
        message = playerNickname + " won!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Declares a tie for the game and displays a timed message indicating the player's nickname.
     *
     * @param playerNickname the nickname of the player for whom the tie is declared
     * @throws Exception if there is an issue displaying the message
     */
    public void tie(String playerNickname) throws Exception {
        message = playerNickname + " tied!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Handles the event when a player loses the game.
     * Displays a timed message with the player's nickname indicating they lost.
     *
     * @param playerNickname the nickname of the player who lost the game
     * @throws Exception if there is an issue displaying the message
     */
    public void lost(String playerNickname) throws Exception {
        message = playerNickname + " lost!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Handles the event where a player refuses a reward.
     *
     * @param playerNickname the nickname of the player who refuses the reward
     * @throws Exception if an error occurs while processing the refusal
     */
    public void refusedReward(String playerNickname) throws Exception {
        message = playerNickname + " refused reward!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Indicates that the effect of a card has ended.
     *
     * This method updates the messaging system with a specific announcement
     * and displays a temporary informational message to the view.
     *
     * @throws Exception if any unexpected issue occurs during the message display process
     */
    public void effectEnded() throws Exception {
        message = "Card Effect ended!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Notifies a player that they have lost goods and displays a timed message on the general view.
     *
     * @param p the player who lost goods
     * @throws Exception if an error occurs during the operation
     */
    public void lostGoods(Player p) throws Exception {
        message = "You lost goods!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Updates the value of the dice and updates the related message.
     *
     * @param dice the new value of the dice to be set
     * @throws Exception if an error occurs during message update
     */
    public void updateDice(int dice) throws Exception {
        message = "Dice is:" + dice;
        this.dice = dice;
        generalView.updateMessageOnly(message);
    }


    /**
     * Removes a block of {@link SpaceShipTile} objects from a player's shipboard
     * based on their nickname, updates the lost pieces, and adjusts the general view.
     *
     * @param block           the list of {@link SpaceShipTile} objects to be removed
     * @param playerNickname  the nickname of the player whose shipboard will be modified
     * @throws Exception      if an error occurs during the removal process
     */
    public void removeBlock(ArrayList<SpaceShipTile> block, String playerNickname) throws Exception {
        message = "I've removed a block from " + playerNickname + " shipboard";
        ArrayList<SpaceShipTile> blockNew = new ArrayList<>();

        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {

                for (SpaceShipTile sb : block) {

                    for (int i = 0; i < s.getShipMatrix().length; i++) {
                        for (int j = 0; j < s.getShipMatrix()[i].length; j++) {
                            if (s.getShipMatrix()[i][j].getID() == sb.getID()) {
                                blockNew.add(s.getShipMatrix()[i][j]);
                            }
                        }
                    }
                }

                s.addLostPieces(s.removeBlock(blockNew));
                generalView.removeBLock(s, blockNew);
                break;
            }
        }
    }

    /**
     * Indicates that a deposit action cannot be performed.
     * This method sets an internal message stating "Cannot deposit!"
     * and displays it using a timed information view.
     *
     * @throws Exception if an error occurs while displaying the message or handling operations.
     */
    public void cannotDeposit() throws Exception {
        message = "Cannot deposit!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Handles scenarios where an insert operation cannot be performed.
     *
     * This method sets an appropriate message indicating the failure
     * to insert and displays it using a timed information view.
     *
     * @throws Exception if an issue occurs during the execution of the method
     */
    public void cannotInsert() throws Exception {
        message = "Cannot insert!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Displays a timed informational message indicating an item cannot be picked.
     *
     * @throws Exception if there is an issue displaying the message or during execution.
     */
    public void cannotPick() throws Exception {
        message = "Cannot pick!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Displays a timed information message indicating that a specified action cannot be completed.
     * The method sets a predefined message and invokes the view to show it for a fixed duration.
     *
     * @throws Exception if there is an issue displaying the message.
     */
    public void cannotFill() throws Exception {
        message = "Cannot fill!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Displays a timed information message indicating that the user is blocked.
     * The message is shown for a duration of 4 seconds.
     *
     * @throws Exception if an error occurs while displaying the message
     */
    public void blocked() throws Exception {
        message = "You are blocked!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Stops the building phase of the game.
     *
     * This method marks the termination of the building phase by updating the message
     * and manipulating certain properties of ship objects within the game. Specifically,
     * it processes all ShipBoard objects, and if a ShipBoard instance is of type
     * ShipBoard_LevelII, it removes waiting tiles associated with the end of building.
     * Additionally, it updates the general view to remove specific tiles and display
     * an information message to all players.
     *
     * @throws Exception if any operation within the method encounters a failure.
     */
    public void stopBuilding() throws Exception {
        message = "Building phase stopped!";
        for (ShipBoard s : ShipBoards) {
            if (s instanceof ShipBoard_LevelII) {
                s.removeWaitTilesForEndBuilding();
                generalView.removeSingleTile(s.getMyPlayer().getUsername(), 0, 6, false, 0);
                generalView.removeSingleTile(s.getMyPlayer().getUsername(), 0, 5, false, 0);
            }
        }
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Initiates a countdown timer and displays its status.
     * The method sets the initial message, updates the remaining time value,
     * and begins a separate thread to handle the countdown process.
     *
     * The countdown continues, decrementing the remaining time once per second,
     * until the time runs out or the thread is interrupted.
     *
     * @throws Exception if an error occurs while processing the countdown or updating the UI.
     */
    public void timerStarted() throws Exception {
        message = "Timer started!";
        timeRemaining = hourglassResting;
        generalView.showTimedInfo(message, 4);
        generalView.updateTime();
        new Thread(() -> {
            while (timeRemaining > 0 && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    timeRemaining--;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Notifies the user that the timer cannot be activated because it has already started.
     *
     * @throws Exception if an error occurs while showing the timed information
     */
    public void timerAlreadyStarted() throws Exception {
        message = "You cannot activate timer now";
        generalView.showTimedInfo(message, 4);
    }


    /**
     * Updates the remaining goods for the given player by processing the list of goods
     * and updating the relevant game state and views.
     *
     * @param p the player for whom the goods are being updated
     * @param goodFInali the list of goods to be processed for updating the player's remaining goods
     * @throws Exception if an error occurs during the update process
     */
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception {
        int V4 = 0;
        int V3 = 0;
        int V2 = 0;
        int V1 = 0;
        for (Goods good : goodFInali) {
            if (good.getValue() == 4) V4++;
            if (good.getValue() == 3) V3++;
            if (good.getValue() == 2) V2++;
            if (good.getValue() == 1) V1++;
        }
        String remaingGoodString = "These are the goods you can manage:\nValue 4: " + V4 + "\nValue 3: " + V3 + "\nValue 2: " + V2 + "\nValue 1: " + V1;

        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(p.getUsername())) {
                s.looseAllGoods();
                break;
            }
        }
        message = remaingGoodString;
        generalView.updateGoodsRemaining(p, goodFInali);
    }

    /**
     * This method sets a predefined message, "You pay consequences!", and displays it using
     * a general view with a timed duration of 4 seconds.
     *
     * @throws Exception This method may throw an exception if the underlying display implementation fails.
     */
    public void youPayConsequences() throws Exception {
        message = "You pay consequences!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Sends a penalty notification by updating the message with the provided penalty details.
     *
     * @param penalty the numeric value of the penalty to be conveyed
     * @param type the type or description of the penalty
     * @throws Exception if an error occurs while updating the message
     */
    public void sendPenalty(int penalty, String type) throws Exception {
        message = "Your penalty is " + penalty + " " + type;
        generalView.updateMessageOnly(message);
    }

    /**
     * Handles the process of notifying the user about empty tiles in the game
     * and initiates the necessary steps to fill them. This method updates the
     * game state, displays an informational dialog, and invokes the appropriate
     * view action to address the issue.
     *
     * @throws Exception if any error occurs during the execution of the method.
     */
    public void haveToFillEmptyTiles() throws Exception {
        message = "You have some empty tiles! Choose how you want to fill them!";
        gameState = stateEnum.SET_ALIEN;
        generalView.showInfoDialog("Not completely filled!", message);
        generalView.haveToFillTiles();
    }

    /**
     * Removes a player from the flight board based on their nickname. This method updates the
     * player's status, removes them from rankings, resets their position on the board, and clears
     * their associated ship matrix. If the player does not exist or their position is invalid,
     * no action is taken.
     *
     * @param playerNickname the nickname of the player to be removed from the flight board
     * @throws Exception if an error occurs during the removal process
     */
    public void removePlayerFromFlightboard(String playerNickname) throws Exception {
        Player thePlayer = null;
        int oldPos = -1;
        for (ShipBoard s : ShipBoards) {
            Player p = s.getMyPlayer();
            if (p.getUsername().equals(playerNickname)) {
                thePlayer = p;
                oldPos = p.getPositionOnBoard();
                s.removeAllPassengers();
                break;
            }
        }

        if (!(thePlayer == null || oldPos < 0 || oldPos >= flightBoard.getBoard().size())) {

            thePlayer.setBlocked(true);
            flightBoard.getBoard().set(oldPos, -1);
            thePlayer.setPlayerPosOnboard(-1);
            flightBoard.getRanking().removeIf(p -> p.getUsername().equals(playerNickname));
            for (ShipBoard s : ShipBoards) {
                Player p = s.getMyPlayer();
                if (p.getUsername().equals(playerNickname)) {
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 7; j++) {

                            ArrayList<Type_side_connector> emptyConnector = new ArrayList<>();
                            for (int k = 0; k < 4; k++) {
                                emptyConnector.add(Type_side_connector.SMOOTH_SIDE);
                            }
                            s.getShipMatrix()[i][j] = new SpaceShipTile(0, emptyConnector, SSTTypes.Tile_NonAccesiblePlace);
                            s.getShipMatrix()[i][j].setAttachedShip(s);

                            if (s.getShipMatrix()[i][j].getAttachedShip() == null) {
                                s.getShipMatrix()[i][j].setAttachedShip(s);
                            }
                        }
                    }
                    break;
                }
            }
            generalView.removePlayerFromFlightboard(playerNickname, oldPos);
        }
    }

    /**
     * Handles the event when a timer has ended.
     *
     * @param isLastTimer a Boolean indicating whether the timer that ended is the last timer.
     *                    If true, specific game state transitions and updates are performed.
     * @throws Exception if an error occurs during the state transition or update process.
     */
    public void timerEnded(Boolean isLastTimer) throws Exception {
        if (isLastTimer) {
            if (gameState == stateEnum.BUILDING) {
                message = "Timer ended for all.\nNow you have to choose where to poistion youself on the board!";
                gameState = stateEnum.POSITIONING;
                generalView.updateStatus();
            }
        }

    }

    /**
     * Updates the management of a shot received by a player on a ship board.
     * This method processes the shot, determines how to defend against it,
     * and updates the general view accordingly.
     *
     * @param player the player who received the shot
     * @param shot the shot being received
     * @param howToDefenceFromShots a list of integers representing the strategies or actions to defend against the shot
     * @param dice an integer representing a dice roll that may influence the defense strategy
     * @throws Exception if any error occurs during the process
     */
    public void updateManageShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (player.getUsername().equals(s.getMyPlayer().getUsername())) {
                manageShotReceivedLMR(shot, howToDefenceFromShots, dice, s);
                generalView.updateShotReceived(player, shot, howToDefenceFromShots, dice);
            }
        }

    }


    /**
     * Sends a message to prompt the user to choose a subship.
     *
     * @param subShips A nested list containing subships represented as lists of SpaceShipTile objects.
     * @param nickname The nickname of the user required to choose a subship.
     * @throws Exception If any error occurs during the execution of the message sending process.
     */
    public void messageToChooseSubship(ArrayList<ArrayList<SpaceShipTile>> subShips, String nickname) throws Exception {
        generalView.messageSubShips(subShips, nickname);
    }

    /**
     * Chooses a specific subship for a player and performs necessary actions to update the ship board
     * by removing unwanted subships and managing spaces on the board.
     *
     * @param playerNickname the nickname of the player whose subship needs to be chosen
     * @param subShips a list of subship blocks representing the available subships
     * @param indexToPreserve the index of the subship block to preserve
     * @param waste an initial tracking value for resources or pieces marked as waste
     * @throws Exception if an error occurs while updating the ship state or removing blocks
     */
    public void ChooseSubship(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (s.getShipMatrix()[i][j] == null || s.getShipMatrix()[i][j].getID() == -1) {
                            ArrayList<Type_side_connector> emptyConnector = new ArrayList<>();
                            for (int k = 0; k < 4; k++) {
                                emptyConnector.add(Type_side_connector.SMOOTH_SIDE);
                            }
                            s.getShipMatrix()[i][j] = new SpaceShipTile(0, emptyConnector, SSTTypes.Tile_NonAccesiblePlace);
                            s.getShipMatrix()[i][j].setAttachedShip(s);
                        }
                        if (s.getShipMatrix()[i][j].getAttachedShip() == null) {
                            s.getShipMatrix()[i][j].setAttachedShip(s);
                        }
                    }
                }
            }
        }
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                for (int i = 0; i < subShips.size(); i++) {
                    ArrayList<SpaceShipTile> block = subShips.get(i);
                    ArrayList<SpaceShipTile> blockNew = new ArrayList<>();
                    for (SpaceShipTile sb : block) {
                        for (int p = 0; p < s.getShipMatrix().length; p++) {
                            for (int j = 0; j < s.getShipMatrix()[p].length; j++) {
                                if (s.getShipMatrix()[p][j].getID() == sb.getID()) {
                                    blockNew.add(s.getShipMatrix()[p][j]);
                                }
                            }
                        }
                    }

                    if (i != indexToPreserve) {
                        generalView.removeBLock(s, blockNew);
                        //removeBlock(blockNew,s.getMyPlayer().getUsername());
                        waste += s.removeBlock(blockNew);
                        waste = waste - 1;
                        s.addLostPieces(waste);
                    }
                }
            }
        }
    }

    /**
     * Removes a single tile from the player's ship board matrix at the specified location.
     * If the tile is removed from a test flight board and is not due to a mistake, it increments the waste count.
     * The method updates the ship matrix and handles the removal accordingly.
     *
     * @param playerNickname the nickname of the player whose tile will be removed
     * @param row the row index of the tile to be removed
     * @param col the column index of the tile to be removed
     * @param fromMistake whether the removal is due to a mistake
     * @param waste the current count of waste pieces, which may be incremented by this action
     * @throws Exception if an error occurs during the tile removal process
     */
    public void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                SpaceShipTile tileToRemove = s.getShipMatrix()[row][col];
                if (tileToRemove == null || tileToRemove.getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
                    return;
                }
                s.getShipMatrix()[row][col] = null;
                ArrayList<Type_side_connector> emptyConnector = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    emptyConnector.add(Type_side_connector.SMOOTH_SIDE);
                }
                SpaceShipTile nonAccessibleTile = new SpaceShipTile(0, emptyConnector, SSTTypes.Tile_NonAccesiblePlace);
                s.insertTile(nonAccessibleTile, row, col);
                s.getShipMatrix()[row][col].setAttachedShip(s);
                if (s.getClass() == ShipBoard_TestFlight.class) {
                    if (!fromMistake) {
                        waste++;
                        s.addLostPieces(waste);
                    }

                } else {
                    waste++;
                    s.addLostPieces(waste);
                }
                generalView.removeSingleTile(playerNickname, row, col, fromMistake, waste);
            }
        }
    }

    /**
     * Adds a tile currently held by a player to the wait list on their respective ship board.
     * If the player has a tile in hand and a wait list slot is available, the tile is placed
     * in the first available slot, either WaitTile1 or WaitTile2 on the ship board.
     *
     * @param playerNickname the nickname of the player whose tile should be added to the wait list
     * @param tileIndex the index of the tile to be added to the wait list
     * @throws Exception if the operation fails due to any error in retrieving or updating the player's tile or wait list
     */
    public void addTileToWaitList(String playerNickname, int tileIndex) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                Player player = s.getMyPlayer();
                SpaceShipTile tile = player.getTileInHand();
                if (s.getWaitTile1() == null || s.getWaitTile2() == null) {
                    player.setTileInHand(null);
                    player.setHasTileInHand(false);
                    if (s.getWaitTile1() == null) {
                        s.setWaitTile1(tile);
                        generalView.addWaitTile(tile, 0, 5, s);
                        s.getWaitTile1().setAttachedShip(s);
                    } else {
                        s.setWaitTile2(tile);
                        generalView.addWaitTile(tile, 0, 6, s);
                        s.getWaitTile2().setAttachedShip(s);
                    }
                }
            }
        }
    }

    /**
     * Inserts a wait tile into the player's hand based on the specified index.
     * The wait tile is fetched from the player's ship board if available,
     * and the corresponding position on the ship board is set to null.
     * Updates the general view according to the index and player's nickname.
     *
     * @param playerNickname the nickname of the player whose ship board is being accessed
     * @param index the index of the wait tile to be inserted (0 or 1)
     * @throws Exception if there is an issue processing the operation
     */
    public void insertWaitTileLMR(String playerNickname, int index) throws Exception { //rotation is 0, 90, 180,270 ecc..
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(playerNickname)) {
                Player player = s.getMyPlayer();
                if (index == 0 && s.getWaitTile1() != null) {
                    player.setTileInHand(s.getWaitTile1());
                    s.setWaitTile1(null);
                    generalView.insertwaittileLMR1(playerNickname);
                } else if (index == 1 && s.getWaitTile2() != null) {
                    player.setTileInHand(s.getWaitTile2());
                    s.setWaitTile2(null);
                    generalView.insertwaittileLMR2(playerNickname);
                }
            }
        }
    }

    /**
     * Handles the management of a received shot in the context of a specific ship and its components.
     *
     * @param shot The shot object representing the received shot.
     * @param values A list of integers containing specific indices or values related to the shot, such as coordinates.
     * @param dice An integer representing the dice roll or related value.
     * @param s The ship board object containing the structure of the ship's components and state.
     * @throws Exception If an error occurs during the shot management process.
     */
    public void manageShotReceivedLMR(Shot shot, ArrayList<Integer> values, int dice, ShipBoard s) throws Exception {
        if (values != null && !values.isEmpty()) {
            SpaceShipTile batteryComponent = s.getShipMatrix()[values.get(2)][values.get(3)];
            batteryComponent.removeCharge(1);
        }
    }


    /**
     * Indicates that there are insufficient goods available in the bank.
     * This method sets an appropriate message and displays it to the user for a limited time.
     *
     * @throws Exception if any issue occurs during the execution of the message display process.
     */
    public void notEnoughGoods() throws Exception {
        message = "Not Enough Goods in the Bank!";
        generalView.showTimedInfo(message, 4);
    }

    /**
     * Updates the number of batteries remaining for a player and updates the general view accordingly.
     *
     * @param p the player for whom the battery count is being updated
     * @param batt the number of batteries remaining to be inserted
     * @throws Exception if there is an issue updating the battery count or the view
     */
    public void updateBatteriesRemaining(Player p, int batt) throws Exception {
        message = "You have " + batt + "batteries to insert";
        generalView.updateBatteriesRemaining(p, batt);
    }

    /**
     * Removes an alien from the ship matrix based on the given player's username and the specified coordinates.
     *
     * @param username the username of the player whose alien is being removed
     * @param row the row index in the ship matrix where the alien is located
     * @param col the column index in the ship matrix where the alien is located
     * @throws Exception if the operation fails or the coordinates are invalid
     */
    public void removeAlien(String username, int row, int col) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(username)) {
                removeAlienLMR((Tile_HousingUnit) s.getShipMatrix()[row][col], s);
                return;
            }
        }
    }

    /**
     * Removes a specified number of passengers from a housing unit located at the given
     * row and column on the ship board. If the number of passengers to be removed
     * exceeds the current number of passengers in the housing unit, the number of
     * passengers is set to zero, and further adjustments are handled via the general
     * view system.
     *
     * @param row the row index of the housing unit on the ship board
     * @param col the column index of the housing unit on the ship board
     * @param num the number of passengers to remove from the housing unit
     * @param s the ship board object containing the matrix of tiles and other relevant data
     * @throws Exception if any issue arises during the removal process
     */
    public void removePassengersLMR(int row, int col, int num, ShipBoard s) throws Exception {
        Tile_HousingUnit tile = (Tile_HousingUnit) s.getShipMatrix()[row][col];
        if (tile.getNumPassengers() < num) {
            tile.setNumPassengers(0);
            ArrayList<ArrayList<Integer>> tiles = new ArrayList<>();
            ArrayList<Integer> values = new ArrayList<>();
            values.add(row);
            values.add(col);
            values.add(num);
            tiles.add(values);
            generalView.removePassengers(s, tiles, 0);
        } else {
            int numero = tile.getNumPassengers() - num;
            tile.setNumPassengers(numero);
        }
    }


    /**
     * Removes an alien from the specified tile on the ship board of the player owning the tile.
     * If the alien is present on the given tile, it is removed and the alien-related data is cleared.
     *
     * @param tile The Tile_HousingUnit object representing the tile where the alien is to be removed.
     * @param s The ShipBoard object of the ship associated with the player performing the action.
     * @throws Exception If any error occurs during the process of removing the alien.
     */
    public void removeAlienLMR(Tile_HousingUnit tile, ShipBoard s) throws Exception {
        for (ShipBoard s1 : ShipBoards) {
            if (s1.getMyPlayer().getUsername().equals(s.getMyPlayer().getUsername())) {
                for (SpaceShipTile[] t : s1.getShipMatrix()) {
                    for (SpaceShipTile t2 : t) {
                        if (t2.getID() == tile.getID() && t2.getIsThereAlien()) {
                            t2.setAlienColor(null);
                            t2.setIsThereAlien(false);
                            generalView.removeAlienFromTile(s1, t2);
                        }
                    }
                }
            }
        }
    }

    /**
     * Processes the removal of charges from specified battery positions on the player's shipboard
     * and updates the general view accordingly.
     *
     * @param p The player for whom the battery charges are being removed.
     * @param batteriesToAct A list of battery positions and the respective amounts of charge to remove.
     *                       Each entry contains a list where the first element is the x-coordinate,
     *                       the second element is the y-coordinate, and the third element is the amount of charge to remove.
     * @throws Exception If an error occurs during the processing of the battery charge removal.
     */
    public void lostBatteries(Player p, ArrayList<ArrayList<Integer>> batteriesToAct) throws Exception {
        for (ShipBoard s : ShipBoards) {
            if (s.getMyPlayer().getUsername().equals(p.getUsername())) {
                for (ArrayList<Integer> pos : batteriesToAct) {
                    int x = pos.get(0);
                    int y = pos.get(1);
                    int count = pos.get(2);
                    s.getShipMatrix()[x][y].removeCharge(count);
                }
                generalView.updateBatteries(batteriesToAct, s, true);
                break;
            }
        }

    }
}
