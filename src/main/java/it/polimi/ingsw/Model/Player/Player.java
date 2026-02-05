package it.polimi.ingsw.Model.Player;

import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Boards.Utils_Boards.Ship_Colour;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.io.Serializable;

public class Player implements Serializable {
    /**
     * Represents the unique identifier for a player within the game.
     *
     * This value is assigned during the creation of a Player object
     * and remains constant throughout the player's lifecycle. It is
     * used to distinctly identify and reference a player in various
     * operations and interactions within the game.
     */
    private final int playerId;
    /**
     * Represents the score of a player in the game.
     *
     * The player's score is a floating-point value that tracks their total
     * accumulated points throughout the gameplay. It serves as a critical measure
     * of a player's achievement or success within the game.
     *
     * This value can be updated as the game progresses and is typically used to
     * rank or compare players during or after gameplay.
     */
    private float playerScore;
    /**
     * Stores the username of the player.
     *
     * This variable represents the unique name chosen by or assigned to the player
     * within the game. It is a final field, meaning it cannot be modified after
     * being initialized. The username is used for identifying players, displaying
     * their name in the game interface, and maintaining player-related records.
     */
    private final String username;
    /**
     * Represents the total number of full rounds a player has completed in the game.
     *
     * This variable is used to track the player's progress throughout the game
     * by counting the number of fully completed game rounds.
     */
    private int numFullRounds;
    /**
     * Represents the position of the player on the game board.
     *
     * This variable tracks the player's current position in the game
     * relative to the specific layout or structure of the board.
     * It can be updated as the player moves or progresses during the game.
     */
    private int playerPosOnboard;
    /**
     * Represents the color of the player's ship.
     *
     * This variable is of type {@link Ship_Colour}, an enumeration
     * defining the possible colors a ship can have, such as RED, BLUE,
     * YELLOW, or GREEN. It uniquely identifies the ship associated with
     * the player in the game.
     *
     * Being final, the color of a player's ship remains constant throughout
     * the game and cannot be modified once assigned.
     */
    private final Ship_Colour colour;
    /**
     * Indicates whether the player is currently blocked from taking certain actions.
     *
     * This flag is used to represent the player's state where they are prevented
     * from performing specific actions or moves due to game conditions or rules.
     * If set to true, the player is considered blocked; otherwise, they are not.
     */
    private boolean isBlocked;
    /**
     * Represents the ship board associated with the player.
     * This variable stores the current state of the player's spaceship while in the game.
     * Used to track and manage the player's ship-related functionalities.
     */
    private ShipBoard myShip;
    /**
     * Represents the tile currently held by the player.
     * This tile is temporarily stored when the player picks it up
     * and is not yet placed on the game board.
     */
    private SpaceShipTile tileInHand;
    /**
     * Indicates whether the player currently has a tile in hand.
     *
     * This variable is used to track the possession of a tile by the player
     * during gameplay. It helps determine if the player has successfully picked
     * up a tile and whether they are allowed to perform actions involving a tile
     * in hand, such as placing it on the board.
     *
     * A value of {@code true} signifies that the player has a tile in hand,
     * while {@code false} indicates that the player does not hold any tile.
     */
    private boolean hasTileInHand;
    /**
     * Represents a small deck of cards currently held by a player in the game.
     *
     * This private field is an instance of the {@code Deck} class and is used to
     * store a subset of cards that the player has in their possession. The
     * {@code LittleDeckInHand} may be manipulated or accessed through the
     * corresponding getter and setter methods provided in the {@code Player} class.
     *
     * The cards in this deck can be used strategically during gameplay and their
     * availability or absence can influence the player's decisions or actions.
     *
     * This field is also correlated with the {@code hasLittleDeckInHand} flag,
     * which indicates whether the player currently possesses a little deck.
     */
    private Deck LittleDeckInHand;
    /**
     * Indicates whether the player currently has a small deck of cards in hand.
     *
     * This variable plays a crucial role in tracking the player's possession
     * of the small deck during the game. It is used to determine if the
     * player is holding a deck that can be utilized during gameplay interactions.
     */
    private boolean hasLittleDeckInHand;

    /**
     * Checks if the player currently has a little deck in hand.
     *
     * @return true if the player has a little deck in hand, false otherwise.
     */
    public boolean isHasLittleDeckInHand() {
        return hasLittleDeckInHand;
    }

    /**
     * Sets the flag indicating whether the player has a little deck in hand.
     *
     * @param hasLittleDeckInHand a boolean value where true indicates the player has a little deck in hand,
     *                            and false indicates the player does not.
     */
    public void setHasLittleDeckInHand(boolean hasLittleDeckInHand) {
        this.hasLittleDeckInHand = hasLittleDeckInHand;
    }

    /**
     * Retrieves the "Little Deck" currently held in the player's hand.
     *
     * @return an instance of {@link Deck} representing the player's "Little Deck" in hand,
     *         or {@code null} if the player does not have a "Little Deck" in hand.
     */
    public Deck getLittleDeckInHand() {
        return LittleDeckInHand;
    }

    /**
     * Sets the little deck in the player's hand.
     *
     * This method updates the current little deck that the player is holding. It is
     * typically used in scenarios where the player picks a deck, deposits it, or the state
     * of the deck in hand changes. The provided deck may consist of a set of cards that the
     * player interacts with as part of the game's logic.
     *
     * @param littleDeckInHand the deck to set as the player's current little deck in hand
     *                         or null to indicate the absence of a deck in hand.
     */
    public void setLittleDeckInHand(Deck littleDeckInHand) {
        LittleDeckInHand = littleDeckInHand;
    }

    /**
     * Checks if the player currently has a tile in their hand.
     *
     * @return true if the player has a tile in hand, false otherwise.
     */
    public boolean isHasTileInHand() {
        return hasTileInHand;
    }

    /**
     * Sets the status indicating whether the player has a tile in hand.
     *
     * @param hasTileInHand a boolean value where {@code true} means the player has a tile in hand,
     *                      and {@code false} means the player does not have a tile in hand.
     */
    public void setHasTileInHand(boolean hasTileInHand) {
        this.hasTileInHand = hasTileInHand;
    }

    /**
     * Retrieves the tile currently held by the player.
     *
     * @return the SpaceShipTile object currently in the player's hand, or null if no tile is held.
     */
    public SpaceShipTile getTileInHand() {
        return tileInHand;
    }

    /**
     * Sets the tile currently in the player's hand.
     *
     * @param tileInHand the SpaceShipTile to be set as the tile in hand
     */
    public void setTileInHand(SpaceShipTile tileInHand) {
        this.tileInHand = tileInHand;
    }

    /**
     * Constructor for the Player class.
     *
     * @param playerId The unique identifier for the player.
     * @param username The username or name associated with the player.
     * @param myShip The ShipBoard object representing the player's ship.
     * @param colour The Ship_Colour object representing the color of the player's ship.
     */
    //ci dovrebbero essere tutti i gli attributi
    public Player(int playerId, String username, ShipBoard myShip, Ship_Colour colour) {
        this.playerId = playerId;
        this.username = username;
        this.myShip = myShip;
        this.isBlocked = false;
        this.colour = colour;
        this.playerPosOnboard = -1;
    }

    /**
     * Allows a player to surrender during the game and performs the necessary actions to update
     * their status and remove them from the game state.
     *
     * This method performs the following operations:
     * 1. Sets the player's state to blocked, preventing further actions.
     * 2. Removes the player from their current position on the flight board.
     * 3. Notifies the game's event system to remove the player from the flight board using their username.
     *
     * @throws Exception if an error occurs during the surrender process.
     */
    public void Surrender() throws Exception {

        setBlocked(true);
        myShip.getFlightBoard().removePlayerinPosition(this, this.getPositionOnBoard());
        myShip.getFlightBoard().getMygame().getEventBus().removePlayerFromFlightboard(this.getUsername());


    }

    /**
     * Retrieves the unique identifier of the player.
     *
     * @return the player's unique ID as an integer.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Assigns a new ShipBoard object to the Player's ship.
     *
     * @param myShipBOARD the ShipBoard object to be assigned as the player's ship
     */
    public void setMyShip(ShipBoard myShipBOARD) {
        myShip = myShipBOARD;
    }

    /**
     * Retrieves the player's current position on the board.
     *
     * @return the player's position on the board as an integer
     */
    public int getPositionOnBoard() {
        return playerPosOnboard;
    }

    /**
     * Retrieves the current score of the player.
     *
     * @return the player's score as a floating-point value
     */
    public float getPlayerScore() {
        return playerScore;
    }

    /**
     * Retrieves the username of the player.
     *
     * @return the username of the player as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the number of full rounds completed by the player.
     *
     * @return the number of full rounds completed by the player
     */
    public int getNumFullRounds() {
        return numFullRounds;
    }

    /**
     * Retrieves the color of the player's ship.
     *
     * @return the color of the ship as a value of the Ship_Colour enumeration
     */
    public Ship_Colour getColour() {
        return colour;
    }

    /**
     * Checks if the player is currently blocked from performing actions.
     *
     * @return true if the player is blocked, false otherwise.
     */
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * Retrieves the ship associated with the player.
     *
     * @return the player's ship as a ShipBoard instance
     */
    public ShipBoard getMyShip() {
        return myShip;
    }

    /**
     * Checks whether the player is currently holding something in their hand.
     * The player could be holding either a tile or a little deck.
     *
     * @return true if the player has either a tile or a little deck in their hand, false otherwise.
     */
    public boolean hasSomethingInHand() {
        return (hasTileInHand || hasLittleDeckInHand);
    }

    /**
     * Updates the score of the player.
     *
     * @param playerScore the new score to be assigned to the player
     */
    //Metodi setter
    public void setPlayerScore(float playerScore) {
        this.playerScore = playerScore;
    }

    /**
     * Sets the number of full rounds completed by the player.
     *
     * @param numFullRounds the number of full rounds to be set for the player
     */
    public void setNumFullRounds(int numFullRounds) {
        this.numFullRounds = numFullRounds;
    }

    /**
     * Sets the blocked status of the player.
     *
     * @param blocked a boolean value indicating whether the player is blocked (true) or unblocked (false)
     */
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    /**
     * Updates the player's position on the game board.
     *
     * @param playerPosOnboard the new position of the player on the board. This must represent a valid position within the
     *                         game's board boundaries.
     */
    public void setPlayerPosOnboard(int playerPosOnboard) {
        this.playerPosOnboard = playerPosOnboard;
    }

    /**
     * Deposits the "Little Deck" that the player currently has in hand.
     *
     * This method is utilized to remove the reference to the player's "Little Deck"
     * and update the player's state to indicate that they no longer have a "Little Deck" in hand.
     * It sets the `LittleDeckInHand` field to null and the `hasLittleDeckInHand` flag to false.
     *
     * If this operation is invoked without proper validation before calling,
     * it might lead to unexpected behavior; for example, attempting to deposit
     * when no "Little Deck" is held will not throw an exception but can signify a logical error
     * in the flow of operations.
     *
     * @throws RuntimeException if the deposit process encounters an unexpected issue
     */
    public void depositLittleDeckPlayer() throws RuntimeException {
        setLittleDeckInHand(null);
        setHasLittleDeckInHand(false);

    }

    /**
     * Allows the player to pick a tile if they do not already have one in hand.
     * If the player already has a tile in hand, a message is printed, and no changes are made.
     *
     * @param tile the SpaceShipTile object the player attempts to pick up.
     * @throws RuntimeException if any runtime exception occurs during the operation.
     */
    public void pickTilePlayer(SpaceShipTile tile) throws RuntimeException {
        if (getTileInHand() == null) {
            setHasTileInHand(true);
            setTileInHand(tile);
        } else {
            System.out.println("you already have a tile in hand!");
        }

    }

    /**
     * Deposits the tile currently held by the player if it matches the provided tile.
     * If the player does not hold the specified tile, a message is displayed, and the operation fails.
     *
     * @param tile The SpaceShipTile object that the player intends to deposit.
     *             It must be the same as the tile currently held by the player.
     * @throws RuntimeException if an operational error occurs during the deposit process.
     */
    public void depositTilePlayer(SpaceShipTile tile) throws RuntimeException {
        if (getTileInHand() == tile) {
            setTileInHand(null);
            setHasTileInHand(false);
        } else {
            System.out.println("you don't have a tile in hand!");
        }
    }

    /**
     * Picks a specified little deck from the player's flight board based on its index.
     * If the deck is already occupied, a "wrong input" event is triggered, and the method returns null.
     * Otherwise, the deck is marked as being in the player's hand, and the method returns the deck.
     *
     * @param index the index of the deck to be picked from the flight board
     * @return the picked Deck instance if it is not occupied, or null if the deck is occupied
     * @throws Exception if any error occurs during the retrieval or processing of the deck
     */
    public Deck pickLittleDeck(int index) throws Exception {
        Deck temp = getMyShip().getFlightBoard().getShowableDeck(index);
        if (temp.isOccupied()) {
            getMyShip().getFlightBoard().getMygame().getEventBus().wrongInput(this);
            return null;
        } else {
            setLittleDeckInHand(temp);
            setHasLittleDeckInHand(true);
            return temp;
        }
    }

    /**
     * Checks if there are any accessible tiles adjacent to the specified position on the ship's matrix.
     *
     * The method evaluates the tiles directly above, below, to the left, and to the right of
     * the given position. If any of the adjacent tiles are not null and are not of type
     * {@code SSTTypes.Tile_NonAccesiblePlace}, the method returns {@code true}.
     *
     * @param row the row index of the tile to check adjacency for
     * @param col the column index of the tile to check adjacency for
     * @return {@code true} if there is at least one accessible tile adjacent to the specified position,
     *         {@code false} otherwise
     * @throws RuntimeException if an error occurs during the operation
     */
    public boolean IsThereTilesNextToThis(int row, int col) throws RuntimeException {
        if (row - 1 >= 0 && myShip.getShipMatrix()[row - 1][col] != null && myShip.getShipMatrix()[row - 1][col].getType() != SSTTypes.Tile_NonAccesiblePlace) {
            return true;
        }
        if (row + 1 < 5 && myShip.getShipMatrix()[row + 1][col] != null && myShip.getShipMatrix()[row + 1][col].getType() != SSTTypes.Tile_NonAccesiblePlace) {
            return true;
        }
        if (col - 1 >= 0 && myShip.getShipMatrix()[row][col - 1] != null && myShip.getShipMatrix()[row][col - 1].getType() != SSTTypes.Tile_NonAccesiblePlace) {
            return true;
        }
        return col + 1 < 7 && myShip.getShipMatrix()[row][col + 1] != null && myShip.getShipMatrix()[row][col + 1].getType() != SSTTypes.Tile_NonAccesiblePlace;
    }

    /**
     * Attempts to insert a tile into the player's spaceship grid at the specified position.
     * The tile can only be inserted if the player is holding that specific tile
     * and the specified position is adjacent to at least one existing tile in the grid.
     * If the conditions are not met, an appropriate message is printed,
     * and the insertion fails.
     *
     * @param tile The tile to be inserted into the spaceship grid.
     * @param row The row index where the tile is to be placed.
     * @param column The column index where the tile is to be placed.
     * @return true if the tile is successfully inserted; false otherwise.
     * @throws Exception If there is an unexpected error during insertion.
     */
    public Boolean InsertTile(SpaceShipTile tile, int row, int column) throws Exception {
        if (getTileInHand() == tile) {
            if ((getMyShip().getShipMatrix()[row][column] == null) && IsThereTilesNextToThis(row, column)) {
                getMyShip().insertTile(tile, row, column);
                setTileInHand(null);
                setHasTileInHand(false);
                tile.setAttachedShip(getMyShip());
                return true;
            } else {
                System.out.println("you can't insert a tile next to no tiles!");
                myShip.getFlightBoard().getMygame().getEventBus().cannotInsert(this);
                return false;
            }
        } else {
            System.out.println("you don't have this tile in hand!");
            myShip.getFlightBoard().getMygame().getEventBus().cannotInsert(this);
            return false;
        }
        //boh
    }


    /**
     * Retrieves the object currently held by the player.
     * This can either be a deck or a tile, depending on the player's current state.
     * If the player is not holding anything, null will be returned.
     *
     * @return the object currently in the player's hand, which may be a {@code Deck} if the player is
     *         holding a deck, a {@code SpaceShipTile} if holding a tile, or {@code null} if the player
     *         is not holding anything.
     */
    public Object getThingInHand() {
        if (hasLittleDeckInHand) {
            return LittleDeckInHand;
        } else if (hasTileInHand) {
            return tileInHand;
        } else return null;
    }

}
