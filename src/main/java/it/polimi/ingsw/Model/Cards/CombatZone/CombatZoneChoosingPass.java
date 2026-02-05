package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.COMBAT_ZONE_CHOOSING_PASS;

public class CombatZoneChoosingPass extends CombatZoneState {
    /**
     * Represents a combat zone card that holds details about the current context within the combat zone,
     * including consequences, causes, and interactions relevant to the combat state.
     * This card defines the current state and its various transitions during a game phase.
     *
     * This field is immutable and initialized through the constructor of the {@link CombatZoneChoosingPass}
     * class. It serves as the central reference for actions and state updates that correspond to
     * the combat zone mechanics.
     *
     * The associated {@link Card_Combat_zone} provides access to a collection of consequences
     * and causes, as well as methods to manage state transitions and apply its effects
     * on specific game components such as the flight board.
     */
    private final Card_Combat_zone card;
    /**
     * A list of consequences, defined as objects of type {@code Consequences}, that need to be processed
     * during a specific phase of the combat zone state transition. The list includes the immediate
     * consequence being resolved and any remaining ones, maintaining the sequential order of their
     * application.
     *
     * This variable is marked as {@code final}, ensuring its reference cannot be changed after
     * initialization. Consequently, the contents of the list are manipulated directly when consequences
     * are removed during gameplay transitions.
     *
     * Used within the {@code CombatZoneChoosingPass} state to manage and execute the sequence of
     * consequences associated with the game's events and player decisions.
     */
    private final ArrayList<Consequences> remaining; //lista delle conseguenze includa questa
    /**
     * Represents the previous state of the Combat Zone in a sequence of state transitions.
     * This is used to backtrack or revert to the last active state after completing a specific action
     * or when transitioning back within the Combat Zone's state machine.
     */
    private final CombatZoneState prevState;
    /**
     * The player currently participating in the combat zone state.
     * This variable represents the active player in this phase of the game. It is used to track the specific player
     * involved in decisions and actions required during this state. The player's actions may include choosing losses,
     * triggering transitions to subsequent states, or resolving consequences as dictated by the game mechanics.
     *
     * This field is immutable and initialized during the creation of the CombatZoneChoosingPass instance.
     */
    private final Player player;
    /**
     * The penalty associated with the number of crew members that the player may lose
     * as a consequence during the current state. This value is typically derived
     * from the first consequence in the list of remaining consequences.
     *
     * The crew penalty defines the maximum number of crew members affected and dictates
     * certain gameplay decisions and state transitions concerning passenger handling.
     */
    private final int crewPen;

    /**
     * Constructs a new {@code CombatZoneChoosingPass} state object.
     * This state manages the process where a player selects options to handle
     * consequences within the combat zone. The state transitions and effects
     * are determined based on the player's decisions and the remaining consequences.
     *
     * @param cardC the combat zone card associated with this state
     * @param remainingC the list of remaining consequences to be addressed
     * @param prevState the previous state of the combat zone to revert to after processing
     * @param playerC the player who is currently processing this state
     * @throws Exception if initialization fails due to invalid input or system-related issues
     */
    public CombatZoneChoosingPass(Card_Combat_zone cardC, ArrayList<Consequences> remainingC, CombatZoneState prevState, Player playerC) throws Exception {
        super(cardC);
        this.card = cardC;
        remaining = remainingC;
        crewPen = remaining.getFirst().getCrewPen();
        this.prevState = prevState;
        this.player = playerC;
        ArrayList<Player> ps=new ArrayList<>();
        ps.add(player);
        card.setStateENUM(ps,COMBAT_ZONE_CHOOSING_PASS);


    }


    /**
     * Transitions the game to the next state based on the remaining consequences.
     * If there are no consequences left, it sets the state to the final combat zone.
     * Otherwise, it reverts to the previous state and activates it using the player's flight board.
     *
     * @param p the player involved in the state transition
     * @throws Exception if an error occurs during the state transition
     */
    public void goNextState(Player p) throws Exception {
        if (!remaining.isEmpty()) {
            remaining.removeFirst();//ho tolto la conseguenza
            if (remaining.isEmpty()) {
                card.setState(new CombatZoneFinal(card, p.getMyShip().getFlightBoard()));
            } else {
                //tells about the new state
                card.setState(prevState);
                prevState.Activate(p.getMyShip().getFlightBoard());
            }
        } else {
            card.setState(new CombatZoneFinal(card, p.getMyShip().getFlightBoard()));
        }
    }

    /**
     * Handles the process of choosing passengers to lose as part of the game logic.
     * Ensures the specified player follows rules for removing passengers based on penalties,
     * and transitions to the next game state once the process is complete.
     *
     * @param p the player currently involved in choosing passengers to lose
     * @param yOn a boolean flag indicating additional conditions to be checked during the process
     * @param pass a list of lists representing the positions of passengers to be removed
     * @throws Exception if an error occurs during the execution of the method
     */
    public void choosePassengersToLose(Player p, boolean yOn, ArrayList<ArrayList<Integer>> pass) throws Exception {//pass è array di pos delle pers da perdere
        if (p.equals(player) && !p.isBlocked()) {
            if (p.getMyShip().getPassengerNumber() <= crewPen) {
                p.Surrender();
                goNextState(p);//dopo che ho tolto le persone si è esaurito l'effetto, vado allo stato precedente sennò ho finito
            } else {//calcolo che le persone siano giuste e nelle pos giuste
                if (p.getMyShip().CheckCorrectPeopleToLoose(pass, crewPen)) {
                    player.getMyShip().removePassengers(pass, remaining.getFirst().getCrewPen());
                    card.getGame().getEventBus().updateChoosePassengersToLose(player, remaining.getFirst(), pass);
                    goNextState(p);//dopo che ho tolto le persone si è esaurito l'effetto, vado allo stato precedente sennò ho finito
                } else//se non mi da input giusto allora aspetto
                {

                    card.getGame().getEventBus().wrongInput(p);
                }
            }
        } else {

            card.getGame().getEventBus().notYourTurn(p);
        }
    }
}
