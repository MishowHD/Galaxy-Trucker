package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

public class CombatZoneState extends Card_State {
    /**
     * Represents a combat zone card associated with the current state of the combat zone.
     * This field is immutable and initialized during the construction of the corresponding state.
     *
     * The combat zone card provides access to the causes and consequences associated
     * with it, as well as the ability to interact with its current state and the game events.
     *
     * This field is primarily used to delegate interactions involving combat zone logic
     * to the corresponding card and its behaviors.
     */
    final Card_Combat_zone card;

    /**
     * Constructs a new CombatZoneState associated with the specified Card_Combat_zone.
     * This state represents the behavior or status of a combat zone card within the game.
     *
     * @param cardC the combat zone card for which this state is being initialized
     */
    public CombatZoneState(Card_Combat_zone cardC) {
        this.card = cardC;
    }
    /**
     * Determines the action to start the motor based on the provided parameters.
     * This method is intended to be overridden in classes where it is applicable
     * and is used in scenarios where the player cannot respond.
     *
     * @param p the player attempting to start the motor
     * @param b the flight board associated with the current game state
     * @param DoubleMotPow a list of lists containing data related to double motor power
     * @param BatteriesToAct a list of lists specifying the batteries to be activated
     * @throws Exception if an error occurs or the action cannot be performed
     */
    //these methods are overrided by the correct class in which they work and used in classes where player cannot respond
    public void chooseToStartMotor(Player p, FlightBoard b, ArrayList<ArrayList<Integer>> DoubleMotPow, ArrayList<ArrayList<Integer>> BatteriesToAct) throws Exception {
        card.getGame().getEventBus().notYourTurn(p);
    }
    /**
     * Triggers the process for a player to start firepower actions, but currently throws an exception
     * if it is not the player's turn. This method may validate the player's ability to perform the action
     * and interacts with the event bus to provide feedback.
     *
     * @param p                The player attempting to start the firepower action.
     * @param DoubFireTriplets A list of triplets defining pairs of firepower actions the player may execute.
     * @param BatteriesToAct   A list of batteries or positions available for the action.
     * @throws Exception If the action is performed out of turn or another issue is encountered.
     */
    public void chooseToStartFirePower(Player p, ArrayList<ArrayList<Integer>> DoubFireTriplets, ArrayList<ArrayList<Integer>> BatteriesToAct) throws Exception {
        card.getGame().getEventBus().notYourTurn(p);
    }
    }
