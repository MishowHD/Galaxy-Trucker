package it.polimi.ingsw.Model.Cards.Smugglers;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.SMUGGLERS_WINNING_STATE;

public class SmugglersWinningState extends SmugglersState {
    /**
     * Represents the winner of the game in the context of the
     * SmugglersWinningState. This field stores the Player instance
     * that has been designated the winner after the game has concluded.
     *
     * This variable is final and cannot be modified once initialized.
     * It is set during the creation of the SmugglersWinningState object.
     */
    private final Player winner;

    /**
     * Constructs a new SmugglersWinningState, initializing it with the given card and winner player,
     * and sets the state of the card to SMUGGLERS_WINNING_STATE, associating it with the winner.
     *
     * @param smugglers the card associated with this state
     * @param winner the player who is the winner in this state
     * @throws Exception if an error occurs during state initialization
     */
    public SmugglersWinningState(Card smugglers, Player winner) throws Exception {
        super(smugglers);
        this.winner = winner;
        ArrayList<Player> players = new ArrayList<>();
        players.add(winner);
        card.setStateENUM(players,SMUGGLERS_WINNING_STATE);
    }

    /**
     * Handles the process where a player decides whether to claim a reward after winning in the Smugglers game state.
     * The method includes validating the player's eligibility, checking resources, interacting with the bank,
     * and triggering events related to rewards or penalties.
     *
     * @param flightBoard the current state of the flight board in the game.
     * @param yOn a boolean flag indicating whether the player wants to claim the reward.
     * @param player the player attempting to claim the reward.
     * @param storagetiles the data structure representing the player's storage tiles for goods.
     * @param newgoods the list representing the new goods potentially added to the storage tiles.
     * @throws Exception if an unexpected error occurs during the reward claiming process.
     */
    public void chooseToClaimReward(FlightBoard flightBoard, boolean yOn, Player player, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        // Applica i premi
        if (player.equals(winner)) {
            if (yOn) {
                if (player.getMyShip().checkGoodsInput(storagetiles, newgoods, card.getGoods())) {

                    List<Goods> goodList = card.getGoods();
                    Bank bank = card.getGame().getBank();
                    Map<Integer, Long> requiredByColor = goodList.stream()
                            .collect(Collectors.groupingBy(Goods::getValue, Collectors.counting()));
                    boolean enoughGoods = requiredByColor.entrySet().stream().allMatch(entry -> {
                        int color = entry.getKey();
                        long needed = entry.getValue();
                        return switch (color) {
                            case 4 -> bank.getRedGood() >= needed;
                            case 3 -> bank.getYellowGood() >= needed;
                            case 2 -> bank.getGreenGood() >= needed;
                            case 1 -> bank.getBlueGood() >= needed;
                            default -> false;
                        };
                    });
                    player.getMyShip().getFlightBoard().movePlayer(player, -1 * card.getDaysPenalty());
                    if (enoughGoods) {
                        player.getMyShip().addGoods(storagetiles, newgoods);
                        card.getGame().getEventBus().chooseToClaimRewardSmug(true, player, storagetiles, newgoods);
                        card.setState(new SmugglersEndState(card));
                        card.goNextState(flightBoard);
                        return;
                    }
                    card.getGame().getEventBus().notEnoughGoods(player);
                    card.getGame().getEventBus().chooseToClaimRewardSmug(false, player, storagetiles, newgoods);
                    card.setState(new SmugglersEndState(card));
                    card.goNextState(flightBoard);

                } else {
                    card.getGame().getEventBus().wrongInput(player);
                }
            } else {
                card.getGame().getEventBus().chooseToClaimRewardSmug(false, player, storagetiles, newgoods);
                card.setState(new SmugglersEndState(card));
                card.goNextState(flightBoard);
            }

        } else {
            card.getGame().getEventBus().wrongPlayer(player);
        }
    }

    /**
     * Activates the SmugglersWinningState using the provided flight board.
     *
     * @param flightBoard the flight board involved in the current smugglers' state
     */
    public void activate(FlightBoard flightBoard) {
    }

    /**
     * Retrieves the next state of the card.
     *
     * @return the next state of the card as a Card_State object
     */
    @Override
    public Card_State getNextState() {
        return card.getState();
    }

}