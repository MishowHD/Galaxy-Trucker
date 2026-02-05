package it.polimi.ingsw.Model.Cards.AbandonedShip;

import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.ABANDONED_SHIP_TAKEN;

public class AbandonedShipTaken extends AbandonedShipState {
    /**
     * Represents the specific card of type {@code Card_AbandonedShip} associated with the current
     * instance of the {@code AbandonedShipTaken} state. This card contains details about
     * the penalties for flight days, crew members, and monetary rewards specific to an abandoned ship scenario.
     *
     * The {@code card} variable is immutable and is initialized during the construction of an
     * {@code AbandonedShipTaken} instance. The state of this card is updated and manipulated
     * based on game-specific logic, transitioning through different states as the game progresses.
     */
    private final Card_AbandonedShip card;

    /**
     * Constructor for the AbandonedShipTaken class, initializing the card's state
     * to indicate that the abandoned ship has been taken. Updates the state of the
     * card and notifies relevant players and mechanisms in the game.
     *
     * @param card the {@code Card_AbandonedShip} instance associated with this state,
     *             representing the card being transitioned to the "abandoned ship taken" state
     * @throws Exception if an error occurs during the state transition or update process
     */
    public AbandonedShipTaken(Card_AbandonedShip card) throws Exception {
        this.card = card;
        card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),ABANDONED_SHIP_TAKEN);

    }

    /**
     * Activates the Abandoned Ship Taken state for the current game. The method performs several actions,
     * including triggering game events, removing passengers from the player's ship, adding cosmic credits,
     * applying flight penalties, and transitioning the abandoned ship card to its final state.
     *
     * @param player The player who triggered the activation of the abandoned ship.
     * @param posPers A nested list of integers representing the positions of passengers to be removed from the player's ship.
     * @throws Exception If an error occurs while processing game mechanics or events.
     */
    public void Activate(Player player, ArrayList<ArrayList<Integer>> posPers) throws Exception {
        card.getGame().getEventBus().abandonedShipTakenActivate(player, posPers);//notifia anche che è finito l'effetto
        player.getMyShip().removePassengers(posPers, card.viewCrewPenalty());
        player.getMyShip().addCosmicCredits(card.viewMoneyEarning());
        player.getMyShip().getFlightBoard().movePlayer(player, -card.viewFlightPenalty());
        card.setState(new AbShFinal(card));
    }



}
