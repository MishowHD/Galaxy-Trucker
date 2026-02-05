package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Boards.FlightBoard;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.COMBAT_ZONE_FINAL;

public class CombatZoneFinal extends CombatZoneState {

    /**
     * A list of potential consequences triggered by actions or events
     * occurring within the combat zone. These consequences are subclasses
     * of the abstract {@link Consequences} class and represent various
     * outcomes such as loss of crew, days, goods, or other damaging effects
     * defined by specific consequence types.
     *
     * The {@link Consequences} objects in this list may include:
     * - {@link Consequences_Lost_crew}: Represents the penalty of losing crew members.
     * - {@link Consequences_Lost_days}: Represents the penalty of losing time or days.
     * - {@link Consequences_Lost_goods}: Represents the penalty of losing goods or resources.
     * - {@link Consequences_Shots}: Represents damage dealt through shots or aggression.
     *
     * This list is utilized to determine and apply the corresponding repercussions
     * during specific states or transitions in the Combat Zone.
     */
    private ArrayList<Consequences> consequences;
    /**
     * A private instance variable that stores a list of causes associated with a specific state
     * or condition in the combat zone.
     *
     * This list can include various types of cause objects, as defined by subclasses of the
     * {@link Causes} abstract class. These subclasses represent specific types of causes,
     * such as crew-related causes, fire power, or motion power. The polymorphic nature of the
     * class allows different behaviors and attributes to be defined for each cause type.
     *
     * Used in the context of managing or evaluating components or events occurring in the combat
     * zone, potentially influencing the game state or triggering related consequences.
     */
    private ArrayList<Causes> causes;

    /**
     * Constructor for the CombatZoneFinal class. Represents the final state of a card in the combat zone,
     * initiating the surrender timer and updating the game's state to reflect the transition to the COMBAT_ZONE_FINAL state.
     *
     * @param cardC The combat zone card associated with this state.
     *              Represents the specific card whose state is being transitioned.
     * @param flightBoard The flight board associated with the game, used for managing the game's progression and interactions.
     * @throws Exception If an error occurs during the initialization of the combat zone state or the game's state transition.
     */
    public CombatZoneFinal(Card_Combat_zone cardC, FlightBoard flightBoard) throws Exception {
        super(cardC);
        super.card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),COMBAT_ZONE_FINAL);
        //here I change the card stare
        card.getGame().getGameState().startSurrenderTimer();
    }

}
