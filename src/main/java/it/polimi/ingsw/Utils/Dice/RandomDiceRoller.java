package it.polimi.ingsw.Utils.Dice;

import java.io.Serializable;
import java.util.Random;

public class RandomDiceRoller implements DiceRoller, Serializable {
    /**
     * An instance of the {@link Random} class used to generate random numbers.
     * This field is employed to simulate dice rolls by generating random values
     * within a predefined range. It is initialized during the construction of
     * the {@link RandomDiceRoller} class and remains immutable.
     */
    private final Random random;

    /**
     * Constructs a new RandomDiceRoller instance, which is capable of simulating the roll
     * of two six-sided dice using a random number generator.
     *
     * This constructor initializes an instance of {@link Random} to generate random values
     * for the dice rolls. The randomness ensures a fair and unbiased simulation of dice outcomes.
     */
    public RandomDiceRoller() {
        this.random = new Random();
    }


    /**
     * Simulates rolling a pair of six-sided dice by generating two random integers
     * between 1 and 6 (inclusive) and returning their sum.
     *
     * @return the sum of two random integers, each representing a dice roll,
     *         resulting in a value between 2 and 12 (inclusive)
     */
    @Override
    public int roll() {
        int first = random.nextInt(6) + 1;
        int second = random.nextInt(6) + 1;
        return first + second;
    }

    /**
     * Resets the state of the dice roller to its initial configuration.
     * For RandomDiceRoller, this method is implemented but does not
     * modify any internal state, as the random generation of dice rolls
     * does not rely on a trackable sequence or persistent state.
     */
    @Override
    public void reset() {
    }
}
