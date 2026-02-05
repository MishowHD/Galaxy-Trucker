package it.polimi.ingsw.Model.Cards.Pirates;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.PIRATES_WINNING_STATE;

public class PiratesWinningState extends PiratesState {
    /**
     * The player who has been determined to be the winner in the current
     * state of the game.
     *
     * This variable is immutable and represents the specific player
     * who is victorious during the game's "winning" state, such as after
     * a successful interaction with pirates. It is set during the creation
     * of the {@code PiratesWinningState} instance and cannot be modified
     * afterwards.
     */
    private final Player winner;
    /**
     * Represents the state of the game when transitioning to or from the calculation
     * phase of the Pirates card effect. This variable holds an instance of the
     * {@code PiratesCalcState} class, allowing the state to be passed or retrieved
     * for facilitating the game's flow.
     *
     * This is especially utilized in decision-making scenarios where the game needs
     * to switch back to the calculation phase after certain actions or events,
     * ensuring the state integrity and continuity.
     */
    PiratesCalcState piratesCalcState;

    /**
     * Constructor for the PiratesWinningState class. This initializes a new state
     * where a player has won against the pirates and can claim rewards or penalties.
     *
     * @param pirates the card representing the pirates encounter.
     * @param winner the player who won against the pirates.
     * @param piratesCalcState the state to transition to after the winning state.
     * @throws Exception if any error occurs while updating the state or setting the card state enumeration.
     */
    public PiratesWinningState(Card pirates, Player winner, PiratesCalcState piratesCalcState) throws Exception {
        super(pirates);
        this.winner = winner;
        this.piratesCalcState = piratesCalcState;
        ArrayList<Player> ps= new ArrayList<>();
        ps.add(winner);
        card.setStateENUM(ps,PIRATES_WINNING_STATE);
    }

    /**
     * Allows a player to choose whether to claim a reward or refuse it after winning a specific event or state in the game.
     * If the player chooses to claim the reward, penalties or bonuses are applied based on the game's logic,
     * and the game transitions to the next state. If the reward is refused, appropriate actions are triggered.
     * This method also verifies if the calling player is authorized to make the choice.
     *
     * @param yOn a boolean indicating the player's choice to claim the reward (true to claim, false to refuse).
     * @param player the player attempting to claim or refuse the reward.
     * @throws Exception if an error occurs during the state transition or reward application.
     */
    public void chooseToClaimReward(boolean yOn, Player player) throws Exception {
        // Applica i premi
        if (player.equals(winner)) {
            if (yOn) {
                player.getMyShip().getFlightBoard().movePlayer(player, -1 * card.getDaysPenalty());
                player.getMyShip().addCosmicCredits(card.getMoneyGained());
            } else {
                card.getGame().getEventBus().refusedReward(player.getUsername());
            }
            card.getGame().getEventBus().piratesChooseToClaimReward(yOn, player);
            card.getGame().getEventBus().effectEnded();

            card.setState(piratesCalcState);
            card.getState().activate(player.getMyShip().getFlightBoard());
        } else {
            card.getGame().getEventBus().wrongPlayer(player);
        }
    }

    /**
     * Retrieves the next state of the card.
     *
     * @return the next {@code Card_State} representing the state of the card.
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }
}