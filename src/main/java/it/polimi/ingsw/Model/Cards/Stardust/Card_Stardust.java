package it.polimi.ingsw.Model.Cards.Stardust;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.*;

public class Card_Stardust extends Card {
    /**
     * Constructs a Card_Stardust object with the provided parameters. This class extends the Card class
     * and represents a specific type of card with unique characteristics.
     *
     * @param id The unique identifier for the card.
     * @param cardName The name of the card.
     * @param cardLevel The level of the card, representing its difficulty or rank.
     * @param isTest A flag indicating whether this card is a test card, used for testing purposes.
     */
    @JsonCreator
    public Card_Stardust(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int cardLevel,
            @JsonProperty("isTest") boolean isTest
    ) {
        this.id = id;
        this.cardName = cardName;
        this.CardLevel = cardLevel;
        this.isTest = isTest;
        State_enum = GENERIC_STATE;
    }

    /**
     * Retrieves the number of crew members lost for this card.
     *
     * @return the number of crew members lost, which is 0 by default for this card.
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Retrieves the amount of money gained by the card when invoked.
     *
     * @return the amount of money gained, represented as an integer.
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Applies the effect of the Stardust card on the game. Triggers the Stardust
     * event, processes players in reverse ranking order, and moves them backward
     * based on the number of exposed connectors on their ship. Updates the game
     * state and starts the surrender timer at the end of the effect.
     *
     * @param flightBoard the game flight board containing the state of the
     *                    players and their positions.
     * @throws Exception if there is an error during the execution of the effect.
     */
    @Override
    public void effect(FlightBoard flightBoard) throws Exception {

        game.getEventBus().stardustEffect();
        // Get the list of players in ranking order
        List<Player> playerRank = new ArrayList<>(flightBoard.getPlayerRankList());
        // Reverse the order, starting with the last player
        Collections.reverse(playerRank);

        for (Player player : playerRank) {
            // Get the number of exposed connectors from the player's shipboard
            int exposedConnectors = player.getMyShip().calculateExposedConnectors();
            System.out.println("il player " + player.getPlayerId() + " è in pos: " + player.getPositionOnBoard() + " e si muove di " + exposedConnectors);
            // Move the player backward by the number of exposed connectors
            flightBoard.movePlayer(player, -exposedConnectors);

            //System.out.println(player.getUsername() + " has " + exposedConnectors + " exposed connectors and moves back " + exposedConnectors + " spaces.");
        }
        this.setStateENUM(this.getGame().getGameFlightBoard().getPlayerRankList(),STARDUST_FINAL);
        getGame().getGameState().startSurrenderTimer();

    }


}
