package it.polimi.ingsw.Utils;

import java.io.Serializable;
import java.util.UUID;

public class OldGame implements Serializable {
    /**
     * Represents a configurable integer value used within the game.
     * This variable can be modified via the setter method and
     * retrieved using the corresponding getter method.
     */
    private int n;
    /**
     * Represents the current number of participants in the game.
     * It is a final value initialized at object construction and cannot be modified afterward.
     */
    private final int joined;
    /**
     * Represents the maximum number of participants allowed in the game.
     * This value is immutable once the object is created.
     */
    private final int max;
    /**
     * Represents the universally unique identifier (UUID) associated with an instance of the OldGame class.
     * This value is used to uniquely identify the game object.
     */
    private UUID uuid;

    /**
     * Constructs an instance of OldGame with the specified parameters.
     *
     * @param n      the current state or value, which is mutable
     * @param joined the number of players currently joined in the game
     * @param max    the maximum number of players allowed in the game
     * @param uuid   the unique identifier for the game instance
     */
    public OldGame(int n, int joined, int max, UUID uuid) {
        this.n = n;
        this.joined = joined;
        this.max = max;
        this.uuid = uuid;
    }

    /**
     * Retrieves the value of the field 'n'.
     *
     * @return the current value of the field 'n'
     */
    // getters e setters
    public int getN() {
        return n;
    }

    /**
     * Sets the value of the field 'n'.
     *
     * @param n the new value to set for the field 'n'
     */
    public void setN(int n) {
        this.n = n;
    }

    /**
     * Retrieves the number of players who have joined the game.
     *
     * @return the number of players currently joined.
     */
    public int getJoined() {
        return joined;
    }

    /**
     * Retrieves the maximum value allowed for this instance of the game.
     *
     * @return the maximum value as an integer.
     */
    public int getMax() {
        return max;
    }

    /**
     * Retrieves the UUID associated with the current instance of the object.
     *
     * @return the UUID of the object
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid field.
     *
     * @param uuid the new UUID to be assigned
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}


