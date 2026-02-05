package it.polimi.ingsw.Model.Cards.Epidemic;

import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.EPIDEMIC_STATE_BASE;

public class EpidemicStateBase extends EpidemicState {
    /**
     * Represents the epidemic card associated with the base state of the epidemic gameplay.
     * This card dictates the behavior and effects specific to the EpidemicStateBase class.
     * It is immutable and serves as the context for the various actions and transitions
     * of the epidemic card states.
     */
    private final Card_Epidemic card;
    /**
     * A set to keep track of all the spaceship tiles that have already been visited during the current epidemic state.
     * It ensures that no tile is processed multiple times when handling epidemics.
     * This is particularly useful when applying epidemic effects on specific tiles of the spaceship.
     */
    private final Set<SpaceShipTile> AlreadyVisited = new HashSet<>();

    /**
     * Constructs an instance of the EpidemicStateBase class, initializing it with a specific epidemic card.
     *
     * @param card the epidemic card associated with this state
     * @throws Exception if an error occurs during the initialization of the state
     */
    public EpidemicStateBase(Card_Epidemic card) throws Exception {
        super();
        this.card = card;

    }

    /**
     * Checks if the specified tile in the spaceship has any adjacent cabin tiles
     * that meet specific conditions. An adjacent tile is considered to be a cabin
     * if its type is `Tile_Cabin`, and either it contains one or more passengers,
     * or it has an alien present.
     *
     * @param ship the two-dimensional array representing the spaceship layout
     * @param i the row index of the tile to be checked
     * @param j the column index of the tile to be checked
     * @return true if there is at least one adjacent cabin tile meeting the conditions; false otherwise
     * @throws RuntimeException if any error occurs during processing
     */
    private boolean hasAdjacentCabin(SpaceShipTile[][] ship, int i, int j) throws RuntimeException {
        List<int[]> matchingPositions = Stream.of(
                        new int[]{i - 1, j}, new int[]{i + 1, j},
                        new int[]{i, j - 1}, new int[]{i, j + 1}
                )
                .filter(pos -> pos[0] >= 0 && pos[0] < ship.length && pos[1] >= 0 && pos[1] < ship[0].length)
                .filter(pos -> ship[pos[0]][pos[1]].getType() == SSTTypes.Tile_Cabin)
                .filter(pos -> ship[pos[0]][pos[1]].getNumPassengers() > 0 || ship[pos[0]][pos[1]].getIsThereAlien())
                .toList();

        return !matchingPositions.isEmpty();
    }

    /**
     * Activates the epidemic state on a given flight board. This method processes all non-blocked players' ships,
     * identifies tiles with adjacent cabins that are occupied by passengers or aliens, and applies the epidemic state
     * logic to those tiles. Aliens and passengers are progressively removed from the affected tiles, and the state
     * transitions to a final epidemic state afterwards.
     *
     * @param fb the flight board that contains player rank lists and associated information required for state activation.
     * @throws Exception if an error occurs while processing the epidemic state or interacting with the flight board.
     */
    public void Activate(FlightBoard fb) throws Exception {
        this.card.setStateENUM(card.getGame().getGameFlightBoard().getPlayerRankList(),EPIDEMIC_STATE_BASE);
        card.getGame().getEventBus().epidemicStateBaseActivate(AlreadyVisited);//update
        ArrayList<Player> listaGioca = new ArrayList<>(fb.getPlayerRankList());
        listaGioca.stream().filter(x -> !x.isBlocked()).forEach(player -> {
            SpaceShipTile[][] ship = player.getMyShip().getShipMatrix();
            IntStream.range(0, ship.length).forEach(i ->
                    IntStream.range(0, ship[i].length)
                            .filter(j -> SSTTypes.Tile_Cabin == (ship[i][j].getType()))
                            .mapToObj(j -> new int[]{i, j})
                            .filter(coords -> hasAdjacentCabin(ship, coords[0], coords[1]))
                            .filter(coords -> ship[coords[0]][coords[1]].getNumPassengers() > 0 || ship[coords[0]][coords[1]].getIsThereAlien())
                            .forEach(coords -> {
                                AlreadyVisited.add(ship[coords[0]][coords[1]]);
                                //System.out.println("ho tolto da: "+ coords[0]+","+coords[1]+ "da player "+ player.getPlayerId());
                            })
            );
        });
        for (SpaceShipTile t : AlreadyVisited) {
            //System.out.println("Ho rimosso una persona da "+t.getNumPassengers());
            if (t.getNumPassengers() > 0) {
                t.removePassenger(1);
            }
            if (t.getIsThereAlien()) {
                t.removeAlien();
            }
        }
        card.setState(new EpidemicStateFinal(card));
    }
}