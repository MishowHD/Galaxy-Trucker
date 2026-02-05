package it.polimi.ingsw.Model.Cards.Planets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Planet implements Serializable {
    /**
     * A list of {@link Goods} objects associated with the planet. This list represents the resources
     * or items that can be found on the planet. The contents of the list can be retrieved or cleared
     * as part of game logic.
     *
     * The list is initialized when the {@link Planet} object is created and cannot be modified directly.
     * It is managed internally within the class, ensuring controlled access and consistent behavior
     * during gameplay.
     */
    private final List<Goods> goodsList;
    /**
     * Represents the player currently present on the planet.
     * This variable holds a reference to a {@link Player} object if a player is located on the planet,
     * or is set to {@code null} if no player is present.
     *
     * It may be updated to reflect the arrival or departure of a player.
     */
    private Player whoIsThere;

    /**
     * Sets the player currently present on the planet.
     *
     * @param whoIsThere the player to set as present on the planet
     */
    public void setPlayerThere(Player whoIsThere) {
        this.whoIsThere = whoIsThere;
    }

    /**
     * Constructs a new Planet object with the specified list of goods.
     *
     * @param goodsList the list of goods available on the planet. Each element represents a
     *                  specific good available on the planet.
     */
    @JsonCreator
    public Planet(
            @JsonProperty("goodsList") List<Goods> goodsList
    ) {
        this.goodsList = new ArrayList<>(goodsList); // Creiamo una nuova ArrayList
        this.whoIsThere = null;
    }

    /**
     * Retrieves the current list of goods from the planet, clearing the internal goods list
     * in the process. The returned list is a copy of the current goods on the planet.
     *
     * @return a list of Goods objects currently on the planet before the goods list is cleared
     */
    public List<Goods> getGoods() {
        List<Goods> tempGoodsList = new ArrayList<>(goodsList);
        goodsList.clear();
        return tempGoodsList;
    }


    /**
     * Retrieves the Player currently located at the planet.
     * If no Player is present, a message will be printed indicating so.
     *
     * @return the Player currently at the planet, or null if no Player is present
     */
    public Player getWhoIsThere() {
        if (this.whoIsThere == null) {
            System.out.println("No One find there!");
        }
        return this.whoIsThere;
    }
}