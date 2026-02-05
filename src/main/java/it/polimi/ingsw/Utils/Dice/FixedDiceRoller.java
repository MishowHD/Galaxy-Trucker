package it.polimi.ingsw.Utils.Dice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FixedDiceRoller implements DiceRoller, Serializable {
    /**
     * A predefined and ordered list of integers that represents the sequence of dice rolls.
     * This sequence is used to generate fixed values for each roll, ensuring deterministic
     * results in the dice rolling process.
     *
     * Each invocation of the {@link #roll()} method retrieves and advances through the list
     * sequentially. Once the sequence is exhausted, an exception will be thrown unless reset
     * through {@link #reset()}.
     *
     * This field is immutable and initialized upon construction.
     */
    private final List<Integer> sequence;
    /**
     * Tracks the current position in the sequence of predefined dice roll values.
     * Used to determine which value to return or process on the next dice roll.
     * Resets to 0 when the sequence is restarted.
     */
    private int index;

    /**
     * Constructs a FixedDiceRoller instance with a predefined sequence of dice rolls.
     * The initial sequence is determined by the input parameter.
     *
     * @param i an integer used to determine the sequence of dice rolls.
     *          If 0, the sequence will be [9, 5, 7, 2, 3].
     *          Otherwise, the sequence will be [9, 5, 9, 2, 3].
     */
    public FixedDiceRoller(int i) {
        if (i == 0) {
            this.sequence = new ArrayList<>(List.of(9, 5, 7, 2, 3));
        } else {
            this.sequence = new ArrayList<>(List.of(9, 5, 9, 2, 3));
        }
        this.index = 0;
    }

    /**
     * Constructs a FixedDiceRoller with a predefined sequence of integer values.
     * The sequence represents the fixed dice rolls that will be returned when
     * rolling the dice.
     *
     * @param sequence an ArrayList of integers representing the predefined dice rolls
     */
    public FixedDiceRoller(ArrayList<Integer> sequence) {
        this.sequence = sequence;
        this.index = 0;
    }

    /**
     * Simulates rolling a die by returning the next predefined value in the sequence.
     * Throws an exception if there are no more values available in the sequence.
     *
     * @return the next integer value in the predefined sequence of dice rolls
     * @throws IllegalStateException if all values in the sequence have already been used
     */
    @Override
    public int roll() {
        if (index >= sequence.size()) {
            throw new IllegalStateException("Nessun valore disponibile per il lancio #" + index);
        }
        System.out.println("dice before " + sequence.get(index));
        return sequence.get(index++);
    }

    /**
     * Resets the state of the dice roller to its initial position.
     * For FixedDiceRoller, it sets the internal index back to the start of the sequence.
     */
    public void reset() {
        this.index = 0;
    }
}
