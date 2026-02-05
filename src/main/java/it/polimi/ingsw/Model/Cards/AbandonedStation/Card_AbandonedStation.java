package it.polimi.ingsw.Model.Cards.AbandonedStation;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.ABANDONED_STATION_START_STATE;

public class Card_AbandonedStation extends Card {
    /**
     * Represents the penalty in flight days incurred due to the state or effect
     * of an abandoned station card in the game. This penalty impacts the progression
     * of a player's journey by increasing the number of days required to complete certain actions
     * or phases. It is a fixed value assigned during the card's creation and does not change
     * during the game.
     */
    private final int flightDaysPenalty;
    /**
     * Represents the minimum number of crew members required to engage with the
     * abandoned station card during gameplay. This variable determines whether
     * certain actions or transitions involving the card can be performed, based
     * on the availability of sufficient crew members.
     */
    private final int requiredCrew;
    /**
     * A collection that stores instances of the {@code Goods} class.
     * Each element in the list represents an individual good, which may have specific
     * properties such as value and radioactivity. This list typically holds
     * the goods associated with a particular card in the context of the
     * "Abandoned Station" gameplay entity.
     *
     * This list is immutable as it is declared {@code final}. However, the contents of the
     * list can still be modified (added/removed) unless additional restrictions are imposed.
     * Access to this list is typically managed by getter methods and is used
     * in operations related to the card and its effects.
     */
    private final ArrayList<Goods> goodsArray;
    /**
     * Represents the current state associated with the abandoned station card.
     * This variable is an instance of {@code AbandonedStationState} and it
     * determines the card's behavior and transitions during gameplay.
     * The state can be updated or queried depending on the progression of the game.
     */
    private AbandonedStationState stato;

    /**
     * Constructor for the Card_AbandonedStation class, which represents a specific type of card.
     * Initializes the card with its associated properties and assigns a starting state.
     *
     * @param id The unique identifier of the card.
     * @param cardName The name of the card.
     * @param CardLevel The level of the card in the game hierarchy.
     * @param isTest A flag indicating whether the card is being used for testing purposes.
     * @param flightDaysPenalty The penalty in flight days associated with this card.
     * @param requiredCrew The number of crew members required for this card.
     * @param goodsArray A list of goods associated with this card.
     * @throws Exception If there is an error during the creation of the card.
     */
    @JsonCreator
    public Card_AbandonedStation(
            @JsonProperty("id") int id,
            @JsonProperty("cardName") String cardName,
            @JsonProperty("CardLevel") int CardLevel,
            @JsonProperty("isTest") boolean isTest,
            @JsonProperty("flightDaysPenalty") int flightDaysPenalty,
            @JsonProperty("requiredCrew") int requiredCrew,
            @JsonProperty("goodsArray") ArrayList<Goods> goodsArray
    ) throws Exception {
        super(id, cardName, CardLevel, isTest);
        this.flightDaysPenalty = flightDaysPenalty;
        this.requiredCrew = requiredCrew;
        this.goodsArray = goodsArray;
        stato = new AbandonedStationStartState(this);
        State_enum = ABANDONED_STATION_START_STATE;
    }

    /**
     * Retrieves the current state of the card.
     *
     * @return the current state of the card as a {@code Card_State} object.
     */
    // Add getState method to override the parent class method
    @Override
    public Card_State getState() {
        return stato;
    }

    /**
     * Retrieves the number of crew members required for the card associated
     * with an abandoned station gameplay entity.
     *
     * @return the required number of crew members as an integer
     */
    public int getRequiredCrew() {
        return requiredCrew;
    }

    /**
     * Retrieves the list of goods associated with this abandoned station card.
     * The returned list contains instances of the {@code Goods} class,
     * which represent specific items or resources linked to the card.
     *
     * @return an {@code ArrayList} of {@code Goods} representing the items
     *         associated with this card.
     */
    @Override
    public ArrayList<Goods> getGoods() {
        return goodsArray;
    }

//    private Player findLeader(ArrayList<Player> players) throws RuntimeException {
//        return players.stream()
//                .max(Comparator.comparingInt(Player::getNumFullRounds)
//                        .thenComparingInt(Player::getPositionOnBoard))
//                .orElseThrow(() -> new RuntimeException("not found in list"));
//    }
//
//    private ArrayList<Player> playersInOrder(ArrayList<Player> players) throws RuntimeException {
//        ArrayList<Player> orderedPlayers = new ArrayList<>();
//        ArrayList<Player> remainingPlayers = new ArrayList<>(players);
//
//        while (!remainingPlayers.isEmpty()) {
//            Player leader = findLeader(remainingPlayers);
//            if (leader != null) {
//                orderedPlayers.add(leader);
//                remainingPlayers.remove(leader);
//            } else {
//                break;
//            }
//        }
//
//        return orderedPlayers;
//    }

    /**
     * Retrieves the number of penalty days associated with the flight.
     *
     * @return the number of penalty days as an integer
     */
    @Override
    public int getDaysPenalty() {
        return flightDaysPenalty;
    }

    /**
     * Retrieves the number of crew members required to operate or manage this card.
     * This value represents the specific crew requirement for the card's functionality.
     *
     * @return the number of required crew members
     */
    @Override
    public int getCrew() {
        return requiredCrew;
    }


    /**
     * Transitions the current state of the abandoned station card to the next state, if applicable,
     * and activates the new state on the provided flight board.
     *
     * @param flightBoard the flight board on which the new state is activated
     * @throws Exception if transitioning or activating the next state encounters an issue
     */
    public void goNextState(FlightBoard flightBoard) throws Exception {
        AbandonedStationState nextState = stato.getNextState();
        if (nextState != null) {
            stato = nextState;
            stato.activate(flightBoard);
        }
    }

    /**
     * Sets the current state of the AbandonedStation card to the provided state.
     * Ensures that the state is not null before updating.
     *
     * @param state the new state to be assigned to the AbandonedStation card.
     *              Must be a non-null instance of AbandonedStationState.
     */
    public void setState(AbandonedStationState state) {
        if (state != null) {
            stato = state;
        }
    }

    /**
     * Retrieves the number of crew members lost due to the effects of this card.
     *
     * @return the number of crew members lost.
     */
    @Override
    public int getCrewLost() {
        return 0;
    }

    /**
     * Calculates and retrieves the amount of money gained as a result of the current state
     * or actions of the associated abandoned station card.
     *
     * @return the monetary gain value, represented as an integer
     */
    @Override
    public int getMoneyGained() {
        return 0;
    }

    /**
     * Triggers the effect of the state associated with the card on the given flight board.
     *
     * @param flightBoard the flight board on which the effect is activated
     * @throws Exception if an error occurs during the activation of the effect
     */
    public void effect(FlightBoard flightBoard) throws Exception {
        stato.activate(flightBoard);
    }
}