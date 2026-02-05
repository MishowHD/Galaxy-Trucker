package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.COMBAT_ZONE_CHOOSE_BATTERIES;

public class CombatZoneChooseBatteries extends CombatZoneState {
    /**
     * The card field represents the specific combat zone card that is currently
     * associated with the state of the CombatZoneChooseBatteries object. This card
     * encapsulates the causes and consequences specific to the current combat zone
     * situation and serves as the focal point for game state transitions and updates.
     * It integrates game mechanics such as consequences and state changes.
     *
     * The card is immutable for the duration of the CombatZoneChooseBatteries state
     * and provides context for operations, such as updating state enums, notifying
     * the event bus of battery updates, and managing transitions between different
     * combat zone states.
     */
    private final Card_Combat_zone card;
    /**
     * A list of all pending {@link Consequences} that need to be resolved
     * during the current game state. Each element in the list represents
     * a consequence that impacts the game, such as lost crew members,
     * lost goods, or other penalties. The order of the list reflects
     * the sequence in which the consequences should be processed or resolved.
     */
    private final ArrayList<Consequences> remaining; //lista delle conseguenze includa questa
    /**
     * Represents the previous state in the combat zone sequence. The `prevState` variable
     * is used to store a reference to the prior {@link CombatZoneState}. This allows the game flow
     * to revert or transition back to an earlier state if necessary.
     *
     * This is typically utilized in scenarios where a player decision or game interaction
     * requires re-evaluating or moving back to a specific point in the game logic.
     */
    private final CombatZoneState prevState;
    /**
     * Represents the player currently interacting with or affected by the state
     * of the combat zone during a game. This player is responsible for performing
     * actions such as choosing battery positions or responding to state transitions.
     * The player remains consistent throughout the lifecycle of this specific
     * state within the combat zone.
     */
    private final Player player;
    /**
     * Represents the number of batteries available to the player during the
     * current state in the combat zone. This variable is used to determine
     * how many batteries the player can distribute or utilize during their
     * actions in this phase.
     */
    private final int numbatt;

    /**
     * Constructs a new instance of the CombatZoneChooseBatteries state.
     * This state represents a phase where a player needs to choose how
     * many batteries to place during a combat zone interaction.
     *
     * @param cardC The combat zone card associated with this state.
     * @param remaining A list of remaining consequences to be processed in this combat interaction.
     * @param prevState The previous state before transitioning to this state.
     * @param player The player who is currently making decisions in this state.
     * @param numbattoloose The number of batteries required to lose in this phase.
     * @throws Exception If an error occurs during the transition or initialization of this state.
     */
    public CombatZoneChooseBatteries(Card_Combat_zone cardC, ArrayList<Consequences> remaining, CombatZoneState prevState, Player player, int numbattoloose) throws Exception {
        super(cardC);
        this.remaining = remaining;
        this.prevState = prevState;
        this.player = player;
        this.numbatt = numbattoloose;
        card = cardC;
        ArrayList<Player> ps=new ArrayList<>();
        ps.add(player);
        card.setStateENUM(ps,COMBAT_ZONE_CHOOSE_BATTERIES);
        card.getGame().getEventBus().updateBatteriesRemaining(player, numbatt);
        //faccio vedere cose nuove;

        //i can see just the command I can give in this state


    }

    /**
     * Advances the state of the current combat zone by transitioning to the next
     * appropriate state based on the remaining consequences. If no consequences
     * remain, the combat zone transitions to its final state.
     *
     * @param p the player whose state is used during the transition process
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
     * Allows a player to choose and place batteries on their ship during the combat phase.
     * This method checks if the player's input related to battery positioning is valid
     * and progresses the game state or handles incorrect inputs accordingly.
     *
     * @param p The player who is attempting to place the batteries.
     * @param posBatAndNumBattXPos A list of positions and the corresponding number of batteries to be placed.
     *                             Each inner list contains details about the position and battery count.
     * @throws Exception If any error occurs during the validation or state progression process.
     */
    public void chooseToPlaceBatteries(Player p, ArrayList<ArrayList<Integer>> posBatAndNumBattXPos) throws Exception {//pass è array di pos delle pers da perdere
        if (posBatAndNumBattXPos != null) {
            if (p.equals(player) && !p.isBlocked()) {
                //calcolo che le persone siano giuste e nelle pos giuste
                if (p.getMyShip().checkCorrectBatteriesDistribution(posBatAndNumBattXPos, numbatt)) {
                    p.getMyShip().assertBatteryPos(posBatAndNumBattXPos);
                    card.getGame().getEventBus().updateAssertBatteriesPos(p, posBatAndNumBattXPos);//update
                    goNextState(p);
                } else//se non mi da input giusto allora aspetto
                {
                    card.getGame().getEventBus().wrongInput(p);
                }
            } else {

                card.getGame().getEventBus().notYourTurn(p);
            }
        } else {
            card.getGame().getEventBus().wrongInput(p);
        }
    }
}
