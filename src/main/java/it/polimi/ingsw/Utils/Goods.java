package it.polimi.ingsw.Utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Goods implements Serializable {

    /**
     * The integer value representing the specific worth or amount associated with this Goods instance.
     * This value is immutable once assigned during object creation.
     */
    private final int value;
    /**
     * Represents the radioactivity property of the goods.
     * Indicates whether the goods are radioactive or not.
     */
    private final boolean radioactivity;

    /**
     * Constructs a new Goods instance with the specified value and radioactivity status.
     *
     * @param value the value of the goods
     * @param radioactivity indicates whether the goods are radioactive or not
     */
    @JsonCreator
    public Goods(@JsonProperty("value") int value, @JsonProperty("radioactivity") boolean radioactivity) {
        this.value = value;
        this.radioactivity = radioactivity;
    }

    /**
     * Retrieves the value of the Goods object.
     *
     * @return the value of the Goods object as an integer.
     */
    public int getValue() {
        return value;
    }

    /**
     * Indicates whether the item is radioactive.
     *
     * @return true if the item is radioactive, false otherwise.
     */
    public boolean isRadioactive() {
        return radioactivity;
    }

    /**
     * Compares this {@code Goods} object to the specified object for equality.
     * Two {@code Goods} objects are considered equal if they have the same value and
     * radioactivity properties.
     *
     * @param obj the object to compare this {@code Goods} object against
     * @return {@code true} if the given object represents a {@code Goods} object
     *         with the same value and radioactivity as this object; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // Check if the compared object is the same instance
        if (this == obj) {
            return true;
        }

        // Check if the compared object is null or of a different class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Cast the object to Goods
        Goods other = (Goods) obj;

        // Compare the properties that define equality
        return this.value == other.value && this.radioactivity == other.radioactivity;
    }

    /**
     * Computes the hash code for this object. The hash code is generated based on the
     * state of the object's properties that are also used for equality checks in the equals method.
     *
     * @return the computed hash code as an integer
     */
    @Override
    public int hashCode() {
        // Generate a hash code based on the same properties used in equals
        int result = 17;
        result = 31 * result + value;
        result = 31 * result + (radioactivity ? 1 : 0);

        return result;
    }
}