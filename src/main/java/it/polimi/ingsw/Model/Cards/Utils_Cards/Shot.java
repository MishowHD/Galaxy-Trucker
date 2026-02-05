package it.polimi.ingsw.Model.Cards.Utils_Cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Shot implements Serializable {
    /**
     * Indicates whether the shot is classified as "big."
     *
     * This variable is used to determine the size of the shot in the game's logic.
     * A "big" shot may have different interactions within the game compared to
     * other types of shots. The value is immutable and is set once during the
     * creation of the {@code Shot} object.
     *
     * - {@code true}: The shot is considered "big."
     * - {@code false}: The shot is not "big."
     */
    private final boolean isBig;
    /**
     * Represents the angular rotation of a shot in the game.
     *
     * The rotation variable is used to determine the orientation of a shot,
     * typically in degrees, and can influence its behavior or trajectory
     * during gameplay. It is immutable and must be initialized during
     * object creation.
     */
    private final int rotation;
    /**
     * Represents the type of the shot in the game. The shot type is defined by
     * the {@link ShotType} enum, which includes different categories of shots
     * such as Meteor and Cannon.
     *
     * This variable defines the specific behavior or characteristics of the shot
     * based on its type, influencing the overall gameplay mechanics. The value
     * of this field is immutable, ensuring that the shot type cannot be changed
     * once it is set at the time of object creation.
     */
    private final ShotType type;

    // Enum definition for ShotType
    public enum ShotType {
        Meteor,
        Cannon
    }

    /**
     * Creates a new instance of the Shot class, representing a shooting action in the game with specific properties.
     *
     * @param isBig    a boolean indicating whether the shot is of a larger size
     * @param rotation an integer specifying the rotation or angle of the shot
     * @param type     the type of the shot, represented as an instance of the {@link ShotType} enum (e.g., Meteor or Cannon)
     */
    @JsonCreator
    public Shot(
            @JsonProperty("isBig") boolean isBig,
            @JsonProperty("rotation") int rotation,
            @JsonProperty("type") ShotType type
    ) {
        this.isBig = isBig;
        this.rotation = rotation;
        this.type = type;
    }

    /**
     * Retrieves the state of whether the shot is categorized as "big".
     *
     * @return true if the shot is considered big, false otherwise.
     */
    // Existing getters
    public boolean getIsBig() {
        return isBig;
    }

    /**
     * Retrieves the rotation value of the shot.
     *
     * @return the rotation value as an integer.
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Returns the type of the shot.
     *
     * @return the ShotType of the shot, representing its specific type such as {@code Meteor} or {@code Cannon}.
     */
    // New getter for ShotType
    public ShotType getType() {
        return type;
    }
}