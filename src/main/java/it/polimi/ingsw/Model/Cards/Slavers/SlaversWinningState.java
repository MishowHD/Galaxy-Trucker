package it.polimi.ingsw.Model.Cards.Slavers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SLAVERS_WINNING_STATE;

public class SlaversWinningState extends SlaversState {
    /**
     * Represents the winning player in the current state of the game.
     * This variable is immutable once initialized and cannot be altered.
     *
     * The winner is the player who has successfully satisfied the conditions
     * required to achieve victory in the SlaversWinningState of the game.
     */
    private final Player winner;

    /**
     * Constructor for the SlaversWinningState class. Initializes the state with the winning player
     * and updates the card's state to reflect the SlaversWinningState.
     *
     * @param slavers the Card object representing the card in the game, expected to be of type Card_Slavers
     * @param winner the Player object representing the player who has won the current state
     * @throws Exception if there is an issue setting the state of the card or any other process during initialization
     */
    public SlaversWinningState(Card slavers, Player winner) throws Exception {
        super(slavers);
        this.winner = winner;
        ArrayList<Player> players = new ArrayList<>();
        players.add(winner);
        card.setStateENUM(players,SLAVERS_WINNING_STATE);
    }

    /**
     * Handles the process for the given player to choose whether to claim a reward
     * when in the current state. If the player is the winner and chooses to claim
     * the reward, they receive specific benefits such as reduced penalty and monetary
     * gains. If the player refuses the reward or is not the winner, appropriate
     * actions are taken, including state transition and event notifications.
     *
     * @param yOn A boolean indicating whether the player chooses to claim the reward.
     *            If true, the player claims the reward; if false, the player refuses it.
     * @param player The player attempting to claim the reward or refuse it. This player
     *               is validated to check if they are the winner of the current game state.
     * @throws Exception If an error occurs during the reward claiming process or state
     *                   transition handling.
     */
    public void chooseToClaimReward(boolean yOn, Player player) throws Exception {
        // Applica i premi
        if (player.equals(winner)) {
            if (yOn) {
                player.getMyShip().getFlightBoard().movePlayer(player, -1 * card.getDaysPenalty());
                player.getMyShip().addCosmicCredits(card.getMoneyGained());
                card.getGame().getEventBus().effectEnded();
            } else {
                card.getGame().getEventBus().refusedReward(player.getUsername());
            }
            card.getGame().getEventBus().slaversChooseToClaimReward(player, yOn);
            card.setState(new SlaversEndState(card));
            card.goNextState(player.getMyShip().getFlightBoard());
        } else {
            card.getGame().getEventBus().wrongPlayer(player);
        }
    }


    /**
     * Retrieves the next state of the card.
     *
     * @return the current state of the card as a {@code Card_State} object
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}