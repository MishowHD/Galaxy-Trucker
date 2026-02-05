package it.polimi.ingsw.Model.GameControl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Utils.EventBus.EventBus;
import it.polimi.ingsw.Utils.Hourglass;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Utils.Dice.DiceRoller;

import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game implements Serializable {
    /**
     * Represents the current state of the game.
     * This variable holds an instance of the {@code GameState} class,
     * which encapsulates the logic, behaviors, and transitions between
     * different phases or states during the game's lifecycle.
     */
    private GameState gameState;
    /**
     * A collection of players participating in the game.
     * Each player represents an individual participant, containing their
     * respective attributes and behaviors within the game.
     */
    private ArrayList<Player> players;
    /**
     * Represents the event deck used in the game. The event deck contains a collection
     * of cards that influence the game's progression by introducing various events,
     * challenges, or rules during gameplay.
     *
     * This deck is a specialized instance of the {@link Deck} class and can be manipulated
     * through methods like shuffling, drawing, or modifying cards. It plays a crucial role in
     * controlling the flow of the game by providing event-specific interactions governed
     * by the cards it holds.
     *
     * The {@code EventDeck} is typically initialized at the start of the game and can be
     * updated or modified depending on the game's state or rules.
     */
    private Deck EventDeck;
    /**
     * Represents the original deck of cards used in the game.
     *
     * This variable holds a reference to a {@link Deck} that represents the primary,
     * unmodified set of cards created at the start of the game. It acts as a baseline
     * or template deck, from which other operations such as shuffling or drawing cards
     * can be derived during gameplay.
     *
     * The `OriginalDeck` remains unaltered throughout the game, ensuring that the initial
     * state of the deck can be referenced or restored if needed. It serves as a key
     * component in maintaining the integrity of the game's card system.
     */
    private Deck OriginalDeck;
    /**
     * Represents the number of players currently involved in the game.
     * This field determines how many participants are actively playing.
     */
    private int NumOfPlayers;
    /**
     * Represents the current level of the game.
     * This variable is used to determine the game's difficulty or progression stage.
     */
    private int Level;
    /**
     * Represents the central in-game bank managing resources such as goods and batteries.
     * The bank is responsible for storing, adding, and using various types of resources.
     */
    private Bank bank;
    /**
     * Indicates whether the currently drawn card has been activated in the game.
     * This flag is used to track the activation state of the card and may influence
     * the progression or logic of the game's current phase.
     */
    private boolean isCurrentCardActivated;
    /**
     * Represents the flight board used in the game.
     * The flight board is a key component that may control or track
     * various spatial and gameplay elements within the game environment.
     */
    private FlightBoard GameFlightBoard;
    /**
     * Represents the collection of spaceship tiles currently active or available in the game.
     * This field holds an ArrayList of {@link SpaceShipTile} objects that are utilized during the game.
     * It is managed and manipulated by various methods within the Game class.
     */
    private ArrayList<SpaceShipTile> GameTiles;
    /**
     * Stores the original set of SpaceShipTile objects for the game.
     * This list represents the initial configuration or collection of tiles
     * before any modifications made during gameplay.
     */
    private ArrayList<SpaceShipTile> OriginalTiles;
    /**
     * Represents the collection of SpaceShipTile objects that are currently in hand during the gameplay.
     * This variable is managed within the Game class to track spaceship tiles that are part of active player actions or phases.
     */
    private ArrayList<SpaceShipTile> GameSSTinHands;
    /**
     * Represents the timer used in the game, powered by the Hourglass class.
     * This timer is responsible for managing countdowns during specific game phases.
     * It can be activated and observed by relevant game components to synchronize actions or enforce time constraints.
     */
    private Hourglass timer;
    /**
     * Indicates whether the game has been started or not.
     * This variable holds a boolean value, where true signifies that the game is currently in progress, and
     * false indicates that the game has not started yet or has been stopped.
     */
    boolean started;
    /**
     * Represents a unique identifier for the current game match.
     * This identifier is generated using {@link UUID#randomUUID()} to ensure uniqueness.
     * It is used to distinguish each game instance from others.
     */
    private final UUID matchId = UUID.randomUUID();
    /**
     * The event bus used for managing and propagating events within the game.
     * <br>
     * It facilitates a publish-subscribe mechanism, allowing various components
     * to communicate through events without being tightly coupled to one another.
     * This implementation is specifically associated with a unique match identified by its ID.
     * <br>
     * This variable is immutable and is initialized with the match ID.
     */
    private final EventBus eventBus = new EventBus(matchId);
    /**
     * Represents the time limit, in seconds, a player has to make the decision to surrender.
     * This timer is used during gameplay to determine when the game automatically applies
     * a surrender action for the concerned player if no action is taken within the limit.
     */
    private final int surrenderTimer;
    /**
     * Represents the time duration of the hourglass in the game.
     * This variable is a critical gameplay mechanic, determining the time available
     * for certain timed actions or phases in the game.
     */
    private final int hourglassTime;


    /**
     * Constructs a new Game object with the specified level, dice roller, surrender timer,
     * and hourglass time. Initializes game states, players, decks, tiles, and other
     * necessary components required to start the game.
     *
     * @param level The difficulty level of the game. Specific game state initialization
     *              is performed based on this value. Valid values are 0 for
     *              InitializationPhaseStateTest and 1 for InitializationPhaseStateLev2.
     * @param dice The DiceRoller object used for dice rolling mechanics in the game.
     * @param surrenderTimer The time allowed before a surrender action is automatically triggered.
     * @param hourglassTime The countdown time for the hourglass, indicating the time constraint
     *                      for specific game phases or actions.
     */
    public Game(int level, DiceRoller dice, int surrenderTimer, int hourglassTime) {
        started = false;
        // Initialize all collections
        players = new ArrayList<>();
        GameSSTinHands = new ArrayList<>();
        OriginalDeck = new Deck(new ArrayList<>()); // Or however Deck is constructed
        OriginalTiles = new ArrayList<>();
        GameTiles = new ArrayList<>();  // Make sure this is initialized too
        this.surrenderTimer = surrenderTimer;
        this.hourglassTime = hourglassTime;

        switch (level) {
            case 0:
                this.gameState = new InitializationPhaseStateTest(this);
                break;  // Add this break
            case 1:
                this.gameState = new InitializationPhaseStateLev2(this);
                break;  // Add this break
            default:
                System.out.println("Invalid level: " + level);
                return;
        }
        // Changed from addFirst to add since ArrayList doesn't have addFirst

        this.Level = level;
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it/polimi/ingsw/json/mazzo.json")) {
            if (input == null) {
                System.out.println("File JSON non trovato!");
                return;
            }
            List<Card> gameCards = mapper.readValue(input, new TypeReference<>() {
            });
            EventDeck = new Deck(gameCards);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del mazzo");
        }
        ObjectMapper mappertiles = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream("/it/polimi/ingsw/json/tiles.json")) {
            if (input == null) {
                System.out.println("File JSON non trovato!");
            }
            GameTiles = mappertiles.readValue(input, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento del mazzo");
        }


        timer = new Hourglass(hourglassTime, 2, gameState);
        GameFlightBoard = null;
        OriginalDeck.getCardList().clear();
        OriginalDeck.getCardList().addAll(EventDeck.getCardList());

        OriginalTiles.clear();
        OriginalTiles.addAll(GameTiles);
        this.dice = dice;
    }

    /**
     * Retrieves the time set for the hourglass in the game.
     *
     * @return the hourglass time as an integer value
     */
    public int getHourglassTime() {
        return hourglassTime;
    }

    /**
     * Retrieves the countdown duration, in seconds, until the surrender action is triggered.
     *
     * @return the surrender timer in seconds
     */
    public int getSurrenderTimer() {
        return surrenderTimer;
    }

    /**
     * Retrieves the DiceRoller object used in the game.
     *
     * @return the DiceRoller instance associated with the game
     */
    public DiceRoller getDice() {
        return dice;
    }

    /**
     * Represents the dice roller used in the game to perform dice rolls.
     * It provides methods for rolling the dice and resetting its state.
     * The dice is an essential component for managing randomness and
     * decision-making processes within the game flow.
     */
    public DiceRoller dice;


    /**
     * Retrieves the unique identifier of the match.
     *
     * @return a UUID representing the unique identifier of the match
     */
    public UUID getMatchId() {
        return matchId;
    }

    /**
     * Retrieves the event bus instance associated with the game.
     *
     * @return the EventBus associated with the game
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Checks if the current card is activated in the game.
     *
     * @return true if the current card is activated; false otherwise.
     */
    public boolean isCurrentCardActivated() {
        return isCurrentCardActivated;
    }

    /**
     * Sets the activation state of the current card in the game.
     *
     * @param currentCardActivated a boolean indicating whether the current card should be activated (true) or deactivated (false)
     */
    public void setCurrentCardActivated(boolean currentCardActivated) {
        isCurrentCardActivated = currentCardActivated;
    }

    /**
     * Determines if the game has started.
     *
     * @return true if the game has started, false otherwise.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Sets the game status to started.
     * This method updates the internal state of the game to indicate
     * that the game has started.
     */
    public void setStarted() {
        started = true;
    }

    /**
     * Retrieves the current game level.
     *
     * @return the game level as an integer
     */
    public int getLevel() {
        return Level;
    }

    /**
     * Sets the number of players for the game.
     *
     * @param numOfPlayers the number of players to set for the game
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.NumOfPlayers = numOfPlayers;
    }

    /**
     * Starts the game and transitions the game state to its main phase.
     * This method is responsible for initializing the game state to begin gameplay.
     *
     * @throws Exception if there is an issue transitioning to the main game state
     */
    public void startGame() throws Exception {
        gameState.StateMain();
    }

    /**
     * Advances the game to the next phase by transitioning the current game state to its next state
     * and executing the main logic associated with the new state.
     *
     * @throws Exception if an error occurs while transitioning to the next state or while executing the main logic of the new state.
     */
    public void NextPhase() throws Exception {
        gameState = gameState.getNextState();
        gameState.StateMain();
    }

    /**
     * Sets the current state of the game.
     *
     * @param gameState the new {@code GameState} to be set for the game
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Returns a list of players participating in the game.
     *
     * @return an ArrayList containing Player objects representing the players in the game.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Sets the event deck for the game.
     *
     * @param eventDeck the deck to be set as the event deck
     */
    public void setEventDeck(Deck eventDeck) {
        EventDeck = eventDeck;
    }

    /**
     * Retrieves the current event deck being used in the game.
     *
     * @return the event deck, which contains event-specific cards for the game.
     */
    public Deck getEventDeck() {
        return EventDeck;
    }

    /**
     * Retrieves the number of players currently in the game.
     *
     * @return the number of players.
     */
    public int getNumOfPlayers() {
        return NumOfPlayers;
    }

    /**
     * Retrieves the current state of the game.
     *
     * @return the current {@code GameState} of the game
     * @throws RuntimeException if the game state retrieval fails
     */
    public GameState getGameState() throws RuntimeException {
        return gameState;
    }

    /**
     * Adds a player to the game if the maximum number of players has not been reached.
     *
     * @param player the Player object to be added to the game
     */
    public void addPlayer(Player player) {
        if (getPlayers().size() == NumOfPlayers) {
            return;
        }
        players.add(player);
    }

    /**
     * Retrieves the original deck of cards associated with the game.
     * This deck represents the initialized state of the cards before any modifications
     * (such as shuffling, drawing, or removing cards) during the gameplay.
     *
     * @return the original Deck object containing the initial list of cards.
     */
    public Deck getOriginalDeck() {
        return OriginalDeck;
    }

    /**
     * Retrieves the list of original tiles for the spaceship in the game.
     *
     * @return an ArrayList of SpaceShipTile objects representing the original tiles.
     */
    public ArrayList<SpaceShipTile> getOriginalTiles() {
        return OriginalTiles;
    }

    /**
     * Retrieves the hourglass associated with the game.
     *
     * @return the Hourglass timer used in the game
     */
    public Hourglass getHourglass() {
        return timer;
    }

    /**
     * Retrieves the unique identifier of the specified player.
     *
     * @param p the Player whose ID is to be retrieved
     * @return the unique identifier (ID) of the given Player
     */
    public int getPlayerID(Player p) {
        return p.getPlayerId();
    }

    /**
     * Retrieves the game's bank instance, which manages resources such as goods and batteries.
     *
     * @return the Bank object associated with the game
     */
    public Bank getGameBank() {
        return bank;
    }

    /**
     * Sets the current game bank to the specified Bank instance.
     *
     * @param bank the Bank object to be assigned as the current game bank
     */
    public void setGameBank(Bank bank) {
        this.bank = bank;
    }

    /**
     * Retrieves the flight board associated with the game.
     *
     * @return the current game flight board, an instance of FlightBoard
     */
    public FlightBoard getGameFlightBoard() {
        return GameFlightBoard;
    }


    /**
     * Sets the game flight board with the provided FlightBoard object.
     *
     * @param gameFlightBoard the FlightBoard object to be set as the game flight board
     */
    public void setGameFlightBoard(FlightBoard gameFlightBoard) {
        GameFlightBoard = gameFlightBoard;
    }

    /**
     * Retrieves a list of SpaceShipTile objects that represent the game tiles.
     *
     * @return an ArrayList of SpaceShipTile objects containing the game tiles.
     */
    public ArrayList<SpaceShipTile> getGameTiles() {
        return GameTiles;
    }

    /**
     * Sets the game tiles for the game.
     *
     * @param gameTiles the list of SpaceShipTile objects to be used as the game's tiles
     */
    public void setGameTiles(ArrayList<SpaceShipTile> gameTiles) {
        GameTiles = gameTiles;
    }

    /**
     * Retrieves a player from the list of players based on their unique ID.
     *
     * @param ID the unique identifier of the player to be retrieved.
     * @return the Player object if a player with the matching ID is found in the list;
     *         null if no player with the given ID exists.
     */
    public Player getPlayer(int ID) {
        for (Player p : players) {
            if (p.getPlayerId() == ID) {
                return p;
            }
        }
        System.out.println("Player ID do not exists");
        return null;
    }

    /**
     * Retrieves the player ID corresponding to the specified player name.
     *
     * @param playerName the name of the player whose ID is to be retrieved
     * @return the ID of the player associated with the given name, or 0 if no player with the specified name is found
     */
    public int getPlayerIDFromName(String playerName) {
        for (Player p : players) {
            if (p.getUsername().equals(playerName)) {
                return p.getPlayerId();
            }
        }
        return 0;
    }

    /**
     * Retrieves a Player object from the list of players based on the provided nickname.
     *
     * @param nickname the nickname of the player to search for
     * @return the Player object with the matching nickname, or null if no match is found
     */
    public Player getPlayerFromNickname(String nickname) {
        for (Player p : players) {
            if (p.getUsername().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Ends the current game session by stopping the timer and notifying all registered event listeners.
     *
     * This method performs the following actions:
     * 1. Stops the timer associated with the game using the {@code stopTimer()} method of the timer object.
     * 2. Notifies all registered listeners of the game's termination using the {@code endGame()} method of the event bus.
     *
     * @throws Exception if an error occurs while notifying registered listeners through the event bus.
     */
    public void endGame() throws Exception {
        timer.stopTimer();
        eventBus.endGame();
    }

    /**
     * Retrieves the list of spaceship tiles currently in the player's hands.
     *
     * @return an ArrayList of SpaceShipTile objects representing the spaceship tiles in the player's hands.
     */
    public ArrayList<SpaceShipTile> getGameSSTIH() {
        return GameSSTinHands;
    }

    /**
     * Sets the game timer with the given Hourglass instance.
     *
     * @param timer the Hourglass instance to set as the game's timer
     */
    public void setTimer(Hourglass timer) {
        this.timer = timer;
    }

    /**
     * Retrieves the Bank object associated with this instance.
     *
     * @return the Bank object
     */
    public Bank getBank() {
        return bank;
    }


}
