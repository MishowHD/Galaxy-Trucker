package it.polimi.ingsw.Model.Cards.MeteorSwarm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.METEOR_CARD_CALC_STATE;

public class Card_MeteorSwarm extends Card {
    /**
     * A collection of {@link Shot} objects representing the shots associated with the Meteor Swarm card.
     *
     * This list determines the specific configuration and properties of the shots
     * (such as type, size, and rotation) that are utilized during the card's effect.
     * The contents of this list can impact the game state and the interactions
     * caused by the Meteor Swarm card.
     */
    private final ArrayList<Shot> shots;



    /**
     * Constructs a new instance of the Card_MeteorSwarm class, representing a specific type of card
     * with meteor swarm functionality. The card includes information such as its identifier, name,
     * level, test flag, and a list of associated shots.
     *
     * @param id       The unique identifier of the card.
     * @param cardName The name of the card.
     * @param CardLevel The level of the card.
     * @param isTest  A boolean flag indicating whether this card instance is for testing purposes.
     * @param shots   A list of Shot instances associated with the card, which represent specific
     *                meteor or cannon shots. If null, an empty list is assigned by default.
     */
    @JsonCreator
    public Card_MeteorSwarm(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int CardLevel,
            @JsonProperty("isTest") boolean isTest,
            @JsonProperty("shots") ArrayList<Shot> shots
    ) {
        super(id, cardName, CardLevel, isTest);
        this.shots = shots != null ? shots : new ArrayList<>();
        State_enum = METEOR_CARD_CALC_STATE;
    }

    /**
     * Retrieves the list of shots associated with the Card_MeteorSwarm.
     *
     * @return An ArrayList of Shot objects representing the shots associated with the card.
     */
    // Getter method for shots vector
    public ArrayList<Shot> getShots() {
        return shots;
    }

    /**
     * Retrieves the number of crew members lost due to this card's effect.
     * The method currently returns a fixed value of 0, indicating no crew loss.
     *
     * @return the number of crew members lost, always 0
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Returns the amount of money gained as a result of the card's effect.
     *
     * @return the amount of money gained, always 0 for this implementation
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Triggers the effect of the Meteor Swarm card. This method initializes the card's state to
     * a MeteorCardCalcState and activates it to execute the card's effects on the given flight board.
     *
     * @param flightBoard the current flight board containing player rankings and game state information
     * @throws Exception if the state activation encounters any issues
     */
    @Override
    public void effect(FlightBoard flightBoard) throws Exception {
        state = new MeteorCardCalcState(this, flightBoard.getPlayerRankList());
        state.activate(flightBoard);
    }

    /**
     * Advances the current state of the card by invoking the activate method
     * on the current state object with the specified flight board.
     *
     * @param flightBoard the flight board associated with the current game context
     *                     and used to update and process the card's next state logic
     * @throws Exception if an error occurs while processing the state activation
     */
    public void goNextState(FlightBoard flightBoard) throws Exception {
        state.activate(flightBoard);
    }

    /**
     * Sets the current state of the Card_MeteorSwarm to the specified new state.
     *
     * @param newState the new state to be assigned to the Card_MeteorSwarm.
     *                 It must be an instance of MeteorCardState, which defines
     *                 the behavior and attributes for the new state of the card.
     */
    // Method to change state
    public void setState(MeteorCardState newState) {
        this.state = newState;
    }


}