package it.polimi.ingsw.Model.Boards;

import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.GameControl.ScoringPhaseStateLev2;
import it.polimi.ingsw.Model.GameControl.ScoringPhaseStateTest;
import it.polimi.ingsw.Model.Player.Player;

import java.io.Serializable;
import java.util.*;
public class FlightBoard implements Serializable {
    /**
     * Represents a game board or grid structure using a dynamically resizable list
     * of integers. Each entry in the list typically corresponds to a position
     * or element on the board.
     *
     * This variable is immutable and cannot be reassigned after its initialization.
     */
    private final ArrayList<Integer> board;
    /**
     * Represents an ordered list of players based on their rank.
     * This collection maintains the ranking of players, allowing access
     * to the ranked players as an ArrayList.
     *
     * It is declared as `final`, meaning the reference to the ArrayList
     * cannot be reassigned. However, the contents of the ArrayList
     * can still be modified (e.g., add/remove players).
     *
     * The ranking logic or criteria used for ordering is determined elsewhere
     * in the application logic.
     */
    private final ArrayList<Player> playerRank;  // Ora è un ArrayList
    /**
     * Represents the number of players participating in a game or activity.
     * This variable stores an integer value indicating the total count of players.
     */
    private int numPlayers;
    /**
     * A list that stores players whose data might have been doubled
     * or duplicated in some context within the application.
     * This collection is used to manage or process those players distinctly.
     */
    private ArrayList<Player> doubledplayers;
    /**
     * Represents an instance of the game being managed or played.
     * This variable is declared as final to ensure it cannot be reassigned after initialization,
     * and transient to indicate that it should not be serialized.
     */
    private final transient Game mygame;
    /**
     * Represents the size of the board.
     * This variable defines the dimensions for the playing board,
     * typically used in games or grid-based layouts.
     * The value is constant and cannot be modified once initialized.
     */
    private final int boardSize;
    /**
     * Represents the bank entity that handles financial transactions.
     * This object is immutable and holds a reference to the bank details.
     */
    private final Bank bank;
    /**
     * A list that maintains the decks currently possessed by other players.
     * This variable holds an ArrayList of Deck objects which represent the
     * collection of decks that are in the hands of players other than the
     * current player.
     *
     * It is used to track the state and distribution of the decks across
     * opponents during the game.
     */
    private final ArrayList<Deck> DecksInTheHandofOthers;
    /**
     * Retrieves the list of players whose points or attributes have been doubled.
     *
     * @return An ArrayList of Player objects representing the doubled players.
     */
    public ArrayList<Player> getDoubledplayers(){
        return doubledplayers;
    }
    /**
     * Resets the list of players who are currently doubled.
     * This method clears the current state of the doubled players by
     * reinitializing the `doubledplayers` list as a new, empty ArrayList.
     */
    public void resetDoubledPlayers(){
        doubledplayers = new ArrayList<>();
    }
    /**
     * Constructs a FlightBoard object.
     *
     * @param numPlayers the number of players participating in the game.
     * @param boardSize the size of the board used in the game.
     * @param bank the Bank object used for handling in-game transactions.
     * @param g the Game object representing the game logic and state.
     */
    public FlightBoard(int numPlayers, int boardSize, Bank bank, Game g) {
        this.boardSize = boardSize;
        this.board = new ArrayList<>(Collections.nCopies(boardSize, -1));
        // Se non c'è un player in una posizione, ho -1. Altrimenti ho il numero della posizione del player nel rank
        this.numPlayers = numPlayers;
        this.playerRank = new ArrayList<>(numPlayers);  // Usa ArrayList per il playerRank
        this.bank = bank;
        this.mygame = g;
        this.DecksInTheHandofOthers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DecksInTheHandofOthers.add(null);
        }
    }

    /**
     * Retrieves the list of players sorted by their rank.
     *
     * @return an ArrayList containing Player objects ordered by rank.
     */
    public ArrayList<Player> getPlayerRankList() {
        return playerRank;
    }

    /**
     * Retrieves the current number of players.
     *
     * @return the number of players as an integer.
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Retrieves the list of decks that are currently in the hands of other players.
     *
     * @return an ArrayList of Deck objects representing the decks held by other players.
     */
    public ArrayList<Deck> getDecksInTheHandofOthers() {
        return DecksInTheHandofOthers;
    }

    /**
     * Retrieves the current game object associated with this instance.
     *
     * @return the instance of the Game object referred to as mygame
     */
    public Game getMygame() {
        return mygame;
    }

    /**
     * Retrieves the list of decks that can be shown or displayed.
     *
     * @return a list of Deck objects that are eligible to be shown
     */
    public List<Deck> getShowableDecks() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Initializes the player's position on the game board and assigns the player to a specific ranking.
     * Validates the position and rank before proceeding, and updates the game board and player properties.
     * If an invalid position or rank is provided, an event is triggered to handle incorrect input.
     *
     * @param player       The player whose position and rank are to be initialized.
     * @param position     The position on the board where the player is to be placed. Must be within valid board boundaries.
     * @param startingRank The starting rank to which the player should be assigned. Must be a valid and unique rank.
     * @throws Exception   If an error occurs during player initialization or the position and rank are invalid.
     */
    public void initializePlayerPos(Player player, int position, int startingRank) throws Exception {
        if (position < 0 || position >= boardSize) {
            mygame.getEventBus().wrongInput(player);
        }
        int index = startingRank - 1;
        while (playerRank.size() <= index) {
            playerRank.add(null);
        }
        if (playerRank.get(index) != null) {
            mygame.getEventBus().wrongInput(player);
            return;
        }
        if (board.get(position) != -1) {
            mygame.getEventBus().wrongInput(player);
            return;
        }
        playerRank.set(index, player);
        board.set(position, player.getPlayerId());
        player.setPlayerPosOnboard(position);
        mygame.getEventBus().setPlayerPosInFlightBoard(player.getUsername(), position);
    }

    /**
     * Retrieves the Bank instance associated with this object.
     *
     * @return the Bank instance
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Retrieves the rank of the specified player within the playerRank list.
     *
     * @param player the Player object whose rank is to be retrieved
     * @return the player's rank index if found; -1 if the player is not present in the playerRank list
     */
    public int getPlayerRank(Player player) {
        for (int i = 0; i < playerRank.size(); i++) {
            if (playerRank.get(i).equals(player)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retrieves the ranking of players as an ArrayList.
     *
     * @return an ArrayList of Player objects representing the current player rankings.
     */
    public ArrayList<Player> getRanking() {
        return playerRank;
    }

    /**
     * Removes the specified player from the ranking list. Updates the number of players
     * in the list after removal. If the player does not exist in the rank, no action is taken.
     *
     * @param player the player to be removed from the ranking list
     * @throws RuntimeException if an error occurs during the operation
     */
    private void removePlayerFromRank(Player player) throws RuntimeException {
        int index = getPlayerRank(player);
        if (index != -1) {
            playerRank.remove(index);
            numPlayers = playerRank.size();
        }
    }

    /**
     * Removes a player from the specified position on a game board.
     *
     * @param player   The player object to be removed.
     * @param position The position of the player on the board to be removed. Must be within valid board bounds.
     * @throws RuntimeException if the specified position is out of bounds.
     */
    public void removePlayerinPosition(Player player, int position) throws RuntimeException {
        if (position < 0 || position >= boardSize) {
            throw new RuntimeException("Position out of bounds");
        }
        removePlayerFromRank(player);
        board.set(position, -1);
    }

    /**
     * Retrieves the username of a player based on their position on the board.
     *
     * @param pos The position on the board to look for.
     * @param play The list of players to search through.
     * @return The username of the player at the specified position.
     *         Returns "NOT FOUND" if no player is found at the given position.
     * @throws RuntimeException if an unexpected error occurs during the operation.
     */
    public String getPlayerNameFromPos(int pos, ArrayList<Player> play) throws RuntimeException {
        for (Player player : play) {
            if (player.getPositionOnBoard() == pos) {
                return player.getUsername();
            }
        }
        return "NOT FOUND";
    }

    /**
     * Retrieves a deck that can be displayed based on the specified position.
     *
     * @param position the position of the deck to be retrieved
     * @return the deck at the specified position that is eligible to be shown
     * @throws RuntimeException if the operation is not supported or any error occurs during retrieval
     */
    public Deck getShowableDeck(int position) throws RuntimeException {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the size of the board.
     *
     * @return the size of the board as an integer
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Advances the rank of the specified player by swapping their position
     * with the player directly above them in the ranking list. If the player
     * is already at the top of the list, no changes are made.
     *
     * @param player The player whose rank is to be advanced.
     * @throws Exception If the specified player is not found in the ranking list.
     */
    public void advanceRank(Player player) throws Exception {
        int index = getPlayerRank(player);
        if (index == -1) {
            throw new RuntimeException("Player not found");
        } else {
            if (index == 0) {
                return;
            }
            Player temp = playerRank.get(index - 1);
            playerRank.set(index - 1, playerRank.get(index));
            playerRank.set(index, temp);
        }
    }

    /**
     * Demotes the rank of the specified player in the ranking system. If the player
     * is not found or is already at the lowest rank, an appropriate action is taken.
     *
     * @param player the player whose rank is to be lowered
     * @throws Exception if the player is not found in the ranking system
     */
    public void lowerRank(Player player) throws Exception {
        int index = getPlayerRank(player);
        if (index == -1) {
            throw new RuntimeException("Player not found");
        }
        if (index != playerRank.size() - 1) {
            Player temp = playerRank.get(index + 1);
            playerRank.set(index + 1, playerRank.get(index));
            playerRank.set(index, temp);
        }
    }

    /**
     * Moves the specified player forward or backward on the game board based on the given number.
     *
     * @param player the Player object representing the player to be moved
     * @param number an integer indicating how many spaces the player should move;
     *               a positive number moves the player forward, and a negative number moves the player backward
     * @throws Exception if any error occurs during the movement process
     */
    public void movePlayer(Player player, int number) throws Exception {
        if (number > 0) {
            movePlayerForward(player, number);
        } else {
            movePlayerBackwards(player, Math.abs(number));
        }
        // Aggiorna la posizione del player sulla plancia di volo
        player.setPlayerPosOnboard(board.indexOf(player.getPlayerId()));
    }

    /**
     * Retrieves a Player object from the player rank list based on the provided player ID.
     *
     * @param ID the unique identifier of the player to be retrieved
     * @return the Player object corresponding to the provided ID if it exists in the rank list
     * @throws RuntimeException if no player with the given ID is found in the rank list
     */
    public Player getPlayerfromID(int ID) {
        for (Player p : playerRank) {
            if (p.getPlayerId() == ID) {
                return p;

            }
        }
        throw new RuntimeException("Player not found in Flightboard rank list");
    }

    /**
     * Moves the player forward on the game board by a specified number of steps. If the player completes a full round on the board, their number of full rounds is incremented. If
     *  the destination position is occupied, the player's rank is advanced before proceeding.
     *
     * @param player The player object to be moved on the game board.
     * @param number The number of steps the player should move forward.
     * @throws Exception If any error occurs during the movement process.
     */
    private void movePlayerForward(Player player, int number) throws Exception {
        int currentPosition = board.indexOf(player.getPlayerId());
        int playerId = player.getPlayerId();
        board.set(currentPosition, -1);
        int movesCompleted = 0;
        int newPosition = currentPosition;
        while (movesCompleted < number) {
            int nextPosition = (newPosition + 1) % boardSize;
            int occupantId = board.get(nextPosition);
            if (occupantId == -1) {
                newPosition = nextPosition;
                movesCompleted++;
                if (newPosition == 0 && currentPosition != 0) {
                    player.setNumFullRounds(player.getNumFullRounds() + 1);
                }
            } else {
                advanceRank(player);
                newPosition = nextPosition;
            }
        }
        board.set(newPosition, playerId);
        player.setPlayerPosOnboard(newPosition);
    }

    /**
     * Moves the player backwards on the board by the specified number of positions.
     * Updates the player's position and adjusts their full round count if necessary.
     * Handles conflicts with occupied positions and adjusts the player's rank accordingly.
     *
     * @param player The player object to be moved backward on the board.
     *               Must have a valid player ID and current board position.
     * @param number The number of positions to move the player backward.
     *               Must be a positive integer.
     * @throws Exception If an error occurs during the movement process or if invalid input is provided.
     */
    private void movePlayerBackwards(Player player, int number) throws Exception {
        int currentPosition = board.indexOf(player.getPlayerId());
        int playerId = player.getPlayerId();
        board.set(currentPosition, -1);
        int movesCompleted = 0;
        int newPosition = currentPosition;
        while (movesCompleted < number) {
            int nextPosition = (newPosition - 1 + boardSize) % boardSize;
            int occupantId = board.get(nextPosition);
            if (occupantId == -1) {
                newPosition = nextPosition;
                movesCompleted++;
                if (newPosition == boardSize - 1 && currentPosition != boardSize - 1) {
                    player.setNumFullRounds(player.getNumFullRounds() - 1);
                }
            } else {
                lowerRank(player);
                newPosition = nextPosition;
            }
        }
        board.set(newPosition, playerId);
        player.setPlayerPosOnboard(newPosition);
    }

    /**
     * Retrieves the current state of the board.
     *
     * @return An ArrayList of integers representing the board state.
     */
    public ArrayList<Integer> getBoard() {
        return board;
    }

    /**
     * Identifies the player with the best ship based on the number of exposed connectors.
     *
     * The method iterates through a list of players, comparing their ships' exposed connectors.
     * The player with the fewest exposed connectors is determined to have the best ship.
     * If the list is empty, the method will return null.
     *
     * @return The player with the best ship, or null if no players are available.
     */
    public Player getPlayerwithbestShip() {
        Player bestPlayer = null;
        for (Player p : playerRank) {
            if (bestPlayer == null) {
                bestPlayer = p;
            } else if (p.getMyShip().calculateExposedConnectors() < bestPlayer.getMyShip().calculateExposedConnectors()) {
                bestPlayer = p;
            }
        }
        return bestPlayer;
    }


}