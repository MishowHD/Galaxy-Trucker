package it.polimi.ingsw.Model.Cards.MeteorSwarm;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.CHOOSE_DISC_BLOCK;

public class meteor_fixing_state extends Card_State {
    /**
     * Represents the card associated with the current state of the meteor fixing process.
     * This card is used to manage the game's behavior and track its current state.
     *
     * The card instance is immutable and acts as a reference to one of the possible
     * card types in the game, such as combat zone, meteor swarm, or abandoned ship cards.
     * It is integral to game logic and ensures proper coordination with events and state transitions.
     */
    private final Card card;
    /**
     * Represents the instance of the flight board used within the current state.
     *
     * This variable stores a reference to the {@code FlightBoard} associated with the
     * game during the meteor fixing state. It is used to interact with the flight
     * board and retrieve necessary information such as player data or perform operations
     * related to the game's board state.
     *
     * The reference is final, ensuring the {@code FlightBoard} instance cannot
     * be reassigned after initialization in the constructor.
     */
    private final FlightBoard FB;
    /**
     * Represents the unique identifier of a player within the game context.
     *
     * This variable is assigned to the player interacting with the current card or game state.
     * It is final, meaning its value is immutable once set, ensuring the integrity of the player's
     * identity during the game's progression.
     *
     * Usage of this variable is typically tied to identifying and validating the player performing
     * specific actions or responding to game events.
     */
    private final int PlayerId;
    /**
     * Represents the specific player involved in the current state of the game.
     * This variable holds a reference to a {@code Player} object,
     * which encapsulates player-specific data such as their ID, username, current ship, and status.
     * It is used to manage player interactions, decisions, and validations within the current game state.
     */
    private final Player player;
    /**
     * Stores the previous state of the card before transitioning into the current state.
     * Utilized to revert back to the original card state after specific operations or state changes.
     * This variable references a {@link Card_State} object, which defines the current behavior
     * and rules associated with the card in a particular state within the game's context.
     */
    private final Card_State state;
    /**
     * Represents a collection of subgroups of spaceship tiles involved in the
     * current meteor fixing state. Each inner list corresponds to a specific section
     * or block of spaceship tiles.
     */
    private final ArrayList<ArrayList<SpaceShipTile>> blocks;

    /**
     * Constructor for the meteor_fixing_state class. This method initializes the state required
     * for the scenario where a player fixes a meteor impact. It sets up necessary resources,
     * associates the player, and configures game-related entities such as the card and subship blocks.
     *
     * @param cardO The card object related to the current meteor fixing state.
     * @param oldState The previous state of the card before transitioning to this state.
     * @param playerC The player interacting with the meteor fixing state.
     * @param fb The flight board instance representing the game's current state.
     * @param subSHIPS A 2D list of spaceship tiles representing the subships affected by the meteor.
     * @throws Exception Throws an exception if an error occurs during initialization.
     */
    public meteor_fixing_state(Card cardO, Card_State oldState, Player playerC, FlightBoard fb, ArrayList<ArrayList<SpaceShipTile>> subSHIPS) throws Exception {
        card = cardO;
        FB = fb;
        PlayerId = playerC.getPlayerId();
        player = playerC;
        blocks = subSHIPS;
        state = oldState;
        ArrayList<Player> ps= new ArrayList<>();
        ps.add(playerC);
        card.setStateENUM(ps,CHOOSE_DISC_BLOCK);
        card.getGame().getEventBus().messageToChooseSubship(player,subSHIPS);
    }

    /**
     * Allows a player to select one sub-ship from the available blocks and removes the other blocks not chosen.
     * Updates the game state and notifies the event bus accordingly.
     * Ensures the player making the selection is valid and that the chosen index is within the allowable range.
     *
     * @param index the index of the sub-ship block to be chosen
     * @param playerID the unique identifier of the player making the selection
     * @throws Exception if an error occurs during the removal of blocks or notification to the event bus
     */
    public void chooseOneSubShip(int index, int playerID) throws Exception {
        if (PlayerId != playerID || player.isBlocked()) {
            System.out.println("wrong player");
            card.getGame().getEventBus().wrongPlayer(FB.getPlayerfromID(playerID));
            return;
        }
        if (index < 0 || index >= blocks.size()) {
            card.getGame().getEventBus().wrongInput(FB.getPlayerfromID(playerID));
            return;
        }

        for (int i = 0; i < blocks.size(); i++) {
            if (i == index) continue;
            player.getMyShip().removeBlock(blocks.get(i));
            card.getGame().getEventBus()
                    .removeBlock(blocks.get(i), player.getUsername());
        }



        card.setState(state);
        card.getState().Continue(FB);
    }

}
