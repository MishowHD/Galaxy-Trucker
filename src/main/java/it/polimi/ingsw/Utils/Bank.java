package it.polimi.ingsw.Utils;

import java.io.Serializable;
import java.util.List;

public class Bank implements Serializable {
    /**
     * Represents the number of yellow goods stored in the bank.
     * This value is managed as part of the bank's inventory
     * and can be manipulated using methods within the Bank class.
     * It is used to track yellow goods and can influence actions or game mechanisms.
     */
    private int yellowGood;
    /**
     * Represents the number of red goods available in the system.
     * This variable tracks the quantity of red goods currently stored.
     */
    private int redGood;
    /**
     * Represents the count of green goods stored in the Bank.
     * This variable keeps track of the number of green goods,
     * which may be manipulated through various methods in the Bank class.
     */
    private int greenGood;
    /**
     * Represents the quantity of the blue-colored goods stored in the bank.
     * This variable tracks the current number of blue goods available,
     * which can be modified through related methods in the class.
     */
    private int blueGood;
    /**
     * Represents the number of batteries currently stored in the bank.
     * This value can be increased or consumed based on certain operations
     * within the bank's functionality.
     */
    private int battery;

    /**
     * Constructs a new Bank instance with the specified quantities of goods and batteries.
     *
     * @param yellowGood the quantity of yellow goods to initialize the bank with
     * @param redGood the quantity of red goods to initialize the bank with
     * @param greenGood the quantity of green goods to initialize the bank with
     * @param blueGood the quantity of blue goods to initialize the bank with
     * @param battery the quantity of batteries to initialize the bank with
     */
    public Bank(int yellowGood, int redGood, int greenGood, int blueGood, int battery) {
        this.yellowGood = yellowGood;
        this.redGood = redGood;
        this.greenGood = greenGood;
        this.blueGood = blueGood;
        this.battery = battery;
    }

    /**
     * Adds a good to the bank and increments the corresponding counter based on the value of the good.
     *
     * @param good The good to be added. Its value determines which type of good
     *             (red, yellow, green, or blue) will have its count incremented.
     */
    public void addGood(Goods good) {
        switch (good.getValue()) {
            case 4:
                redGood++;
            case 3:
                yellowGood++;
            case 2:
                greenGood++;
            case 1:
                blueGood++;
        }
    }

    /**
     * Retrieves the current quantity of yellow goods stored.
     *
     * @return the number of yellow goods available
     */
    public int getYellowGood() {
        return yellowGood;
    }

    /**
     * Retrieves the amount of red goods currently available.
     * This method provides access to the internal state of the red goods count.
     *
     * @return the current quantity of red goods
     */
    public int getRedGood() {
        return redGood;
    }

    /**
     * Retrieves the value of the green goods stored.
     *
     * @return the integer value representing the quantity of green goods.
     */
    public int getGreenGood() {
        return greenGood;
    }

    /**
     * Retrieves the current value of blue goods.
     *
     * @return the number of blue goods available
     */
    public int getBlueGood() {
        return blueGood;
    }

    /**
     * Adds a list of goods to the current inventory by categorizing them based on their value.
     * Updates the counts for different types of goods (red, yellow, green, blue) based on the
     * value property of each good.
     *
     * @param goodL the list of goods to be added; each good is categorized by its value
     *              (4 for red, 3 for yellow, 2 for green, 1 for blue)
     */
    public void addGoodsFromList(List<Goods> goodL) {
        for (Goods good : goodL) {
            switch (good.getValue()) {
                case 4:
                    redGood++;
                case 3:
                    yellowGood++;
                case 2:
                    greenGood++;
                case 1:
                    blueGood++;
            }
        }
    }

    /**
     * Adds the specified amount of battery to the existing battery count.
     *
     * @param battery the amount of battery to add
     */
    public void addBattery(int battery) {
        this.battery += battery;
    }

    /**
     * Uses a specific type of good based on its value.
     * Determines the type of good to use (red, yellow, green, or blue) by evaluating
     * the value of the provided Goods object and calls the corresponding private method.
     *
     * @param good the Goods object containing the value that determines the type of good to use
     */
    public void useGood(Goods good) {
        if (good.getValue() == 4) {
            useRedGood();
        } else if (good.getValue() == 3) {
            useYellowGood();
        } else if (good.getValue() == 2) {
            useGreenGood();
        } else if (good.getValue() == 1) {
            useBlueGood();
        }
    }

    /**
     * Reduces the quantity of the `yellowGood` resource by 1 if there is more than 1 available.
     * If the quantity is insufficient, an error message is printed.
     *
     * This method is used to handle resource management specifically for the `yellowGood` type.
     */
    private void useYellowGood() {
        if (yellowGood > 1) {
            yellowGood -= 1;
        } else {
            System.out.println("Error: not enough yellowGood.");
        }
    }

    /**
     * Decrements the value of the redGood field by 1 if its current value is greater than 1.
     * If the value is not sufficient, prints an error message indicating insufficient redGood.
     *
     * This method is intended for internal class use and performs validity checks
     * on the redGood field before decrementing its value.
     *
     * Preconditions:
     * - The redGood field must be initialized.
     *
     * Behavior:
     * - If redGood > 1, its value is decremented by 1.
     * - If redGood <= 1, an error message is printed to indicate insufficient resources.
     *
     * This method does not return any value and directly modifies the state of the redGood field.
     */
    private void useRedGood() {
        if (redGood > 1) {
            redGood -= 1;
        } else {
            System.out.println("Error: not enough redGood.");
        }
    }

    /**
     * Decreases the amount of green goods by 1 if the current value is greater than 1.
     * If there are not enough green goods available, an error message is printed to the console.
     */
    private void useGreenGood() {
        if (greenGood > 1) {
            greenGood -= 1;
        } else {
            System.out.println("Error: not enough greenGood.");
        }
    }

    /**
     * Decreases the quantity of the blueGood resource by 1 if it is greater than 1.
     * If the quantity is not sufficient (i.e., less than or equal to 1), the method logs an error message.
     *
     * This method is called internally and ensures proper handling of the blueGood resource
     * within the context of the class.
     */
    private void useBlueGood() {
        if (blueGood > 1) {
            blueGood -= 1;
        } else {
            System.out.println("Error: not enough blueGood.");
        }
    }

    /**
     * Consumes a specified amount of battery from the available battery in the bank.
     * If the available battery is less than the specified amount, an error message is printed.
     *
     * @param numBattery the amount of battery to consume
     */
    public void useBattery(int numBattery) {
        if (battery > numBattery) {
            battery -= numBattery;
        } else {
            System.out.println("Error: not enough battery.");
        }
    }
}

