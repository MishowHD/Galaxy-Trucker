package it.polimi.ingsw.Model.Cards.AbandonedStation;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.ABANDONED_STATION_START_STATE;

public class AbandonedStationStartState extends AbandonedStationState {
    /**
     * Represents the active card associated with the current state of an
     * abandoned station. This is a constant reference to an instance of
     * {@code Card_AbandonedStation}, which contains data and functionality
     * specific to the abandoned station card being used in the game.
     *
     * The card may include attributes such as flight day penalties, required
     * crew, available goods, and other properties specific to the abandoned
     * station. The state transitions and effects of this card are determined
     * through its associated state management mechanisms.
     */
    private final Card_AbandonedStation abandonedStation;
    /**
     * Represents a ranking of players in the context of the Abandoned Station state.
     * This list maintains the players involved, sorted based on their ranking or
     * specific criteria defined within the gameplay logic.
     */
    private ArrayList<Player> playerRank;
    /**
     * Represents the number of answers or responses related to an abandoned station.
     * This variable is used to store or track the count of answers provided by players
     * during the interaction with the {@code AbandonedStationStartState}.
     */
    int answers;

    /**
     * Initializes a new instance of the AbandonedStationStartState class.
     * The state represents the start phase for an abandoned station card.
     *
     * @param abandonedStation the abandoned station card associated with this state.
     * @throws Exception if an error occurs during initialization.
     */
    public AbandonedStationStartState(Card_AbandonedStation abandonedStation) throws Exception {
        super(abandonedStation);
        this.abandonedStation = abandonedStation;
        this.playerRank = new ArrayList<>();
        this.answers = 0;

    }

    /**
     * Activates the AbandonedStationStartState, updating the card state and processing the player ranking
     * and goods collection from the board and card. Transitions to the next state if necessary.
     *
     * @param flightBoard the current flight board containing game state information and player rankings
     * @throws Exception if an error occurs during activation or state transition
     */
    @Override
    public void activate(FlightBoard flightBoard) throws Exception {
        card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),ABANDONED_STATION_START_STATE);
        playerRank = flightBoard.getRanking(); // Get the player ranking
        answers = 0; // Reset answers counter
        // Collect all goods from the board and card
        List<Goods> boardGoods = Arrays.stream(playerRank.get(answers).getMyShip().getShipMatrix())
                .flatMap(Arrays::stream)
                .filter(tile -> tile.getType() == SSTTypes.Tile_CargoHold || tile.getType() == SSTTypes.Tile_SpecialCargoHold)
                .flatMap(tile -> tile.getEffectivePresentGoods().stream())
                .toList();

        ArrayList<Goods> totalGoods=new ArrayList<>();
        totalGoods.addAll(boardGoods);
        totalGoods.addAll(card.getGoods());
        card.getGame().getEventBus().updateGoodsRemaining(playerRank.get(answers), totalGoods);
        if (playerRank.isEmpty()) {
            abandonedStation.setState(new AbandonedStationEndState(abandonedStation));
            abandonedStation.goNextState(flightBoard);
        }

    }

    /**
     * Handles the logic when a player chooses to interact with the abandoned station.
     * This includes validating the player's choice and determining the state transitions
     * based on the player's response and game state.
     *
     * @param player        The player making the choice.
     * @param flightBoard   The flight board on which the game is being played.
     * @param yOn           A boolean indicating whether the player accepts or declines interaction with the station.
     * @param storagetiles  A 2D array list representing the storage tiles provided by the player.
     * @param newgoods      A 2D array list representing the new goods the player wants to trade or provide.
     * @throws Exception    If an error occurs during the state handling for the abandoned station.
     */
    public void chooseAbandonedStation(Player player, FlightBoard flightBoard, boolean yOn,
                                       ArrayList<ArrayList<Integer>> storagetiles,
                                       ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        if (playerRank.indexOf(player) == answers) { // Responding player is correct
            if (yOn) { // Player said yes
                if (player.getMyShip().getPassengerNumber() >= abandonedStation.getRequiredCrew()) {
                    if (storagetiles == null || newgoods == null) {
                        card.getGame().getEventBus().chooseAbandonedStationActivate(player, flightBoard, true, storagetiles, newgoods);
                        int posPenalty = -1 * abandonedStation.getDaysPenalty();
                        flightBoard.movePlayer(player, posPenalty);
                        player.getMyShip().looseAllGoods();
                        card.getGame().getEventBus().chooseAbandonedStationActivate(player, flightBoard, true, storagetiles, newgoods);
                        abandonedStation.setState(new AbandonedStationEndState(abandonedStation));
                        abandonedStation.goNextState(flightBoard);
                    } else if (player.getMyShip().checkGoodsInput(storagetiles, newgoods, abandonedStation.getGoods())) {
                        ArrayList<Goods> goodList = card.getGoods();
                        Bank bank = flightBoard.getBank();
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
                        if (enoughGoods) {
                            card.getGame().getEventBus().chooseAbandonedStationActivate(player, flightBoard, true, storagetiles, newgoods);
                            player.getMyShip().addGoods(storagetiles, newgoods);
                            int posPenalty = -1 * abandonedStation.getDaysPenalty();
                            flightBoard.movePlayer(player, posPenalty);
                            abandonedStation.setState(new AbandonedStationEndState(abandonedStation));
                        }else {
                            card.getGame().getEventBus().notEnoughGoods(player);
                        }
                        abandonedStation.goNextState(flightBoard);
                    } else{
                        card.getGame().getEventBus().wrongInput(player);
                    }
                } else {
                    answers++;
                    if (answers >= playerRank.size()) {
                        abandonedStation.setState(new AbandonedStationEndState(abandonedStation));
                        abandonedStation.goNextState(flightBoard);
                    }else {
                        card.getGame().getEventBus().nextPlayerTurn();
                    }
                }
            } else { // Player said no
                answers++;
                if (answers >= playerRank.size()) {
                    abandonedStation.setState(new AbandonedStationEndState(abandonedStation));
                    abandonedStation.goNextState(flightBoard);
                } else {
                    card.getGame().getEventBus().nextPlayerTurn();
                }
            }
        } else {

            card.getGame().getEventBus().notYourTurn(player);
        }
    }

    /**
     * Retrieves the next state for the {@code AbandonedStationStartState}.
     * The method transitions the current state to the {@code AbandonedStationEndState},
     * which represents the next phase in the state lifecycle.
     *
     * @return an instance of {@code AbandonedStationEndState} representing the subsequent state
     * @throws Exception if an error occurs during the state transition
     */
    @Override
    public AbandonedStationState getNextState() throws Exception {
        return new AbandonedStationEndState(abandonedStation);
    }
}