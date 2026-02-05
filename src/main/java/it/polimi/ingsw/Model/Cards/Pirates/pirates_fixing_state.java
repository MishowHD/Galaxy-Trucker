package it.polimi.ingsw.Model.Cards.Pirates;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.CHOOSE_DISC_BLOCK;

public class pirates_fixing_state extends Card_State {
    /**
     * Represents a specific card associated with the pirates_fixing_state.
     * This card is utilized to manage the state and behavior of the game during
     * the pirate fixing phase. It interacts with the game state, player actions,
     * and other game components to facilitate the required logic for this phase.
     * The instance is immutable once initialized.
     */
    private final Card card;
    /**
     * Represents the flight board used within the pirates_fixing_state class.
     * The FlightBoard instance (FB) is used to manage and access information
     * about the game's overall state, player interactions, and other operations
     * related to the game's flight mechanics.
     *
     * This variable is final, which means it is initialized once at the creation
     * of a pirates_fixing_state object and cannot be reassigned.
     */
    private final FlightBoard FB;
    /**
     * Represents the unique identifier of the player interacting with the current game state.
     * This value is immutable and is set during the initialization of the class instance,
     * commonly used to validate player actions and ensure only the appropriate player
     * can interact with the state.
     */
    private final int PlayerId;
    /**
     * Represents a reference to the player associated with the current game state.
     * This player interacts with the game mechanics and state transitions in the
     * pirates_fixing_state class.
     *
     * The player object is immutable and is used to identify and manage the player's actions,
     * such as choosing a sub-ship or responding to game events.
     */
    private final Player player;
    /**
     * Represents the previous state to which the current card state can revert.
     * This variable holds an instance of the {@code Card_State} class and is
     * used to maintain a reference to the state that was active before transitioning
     * to the current state.
     *
     * It is immutable and final, ensuring that the state remains unaltered once
     * it has been set during the initialization process.
     *
     * This is especially useful for implementing transitions or reverting back
     * to earlier states during gameplay, as observed in scenarios such as
     * resolving actions on the flight board or card-related events.
     */
    private final Card_State state;
    /**
     * Represents a collection of groups of SpaceShipTile objects associated
     * with the current game state. Each nested list corresponds to a specific
     * subset of tiles that are used in the context of subship selection or manipulation.
     */
    private final ArrayList<ArrayList<SpaceShipTile>> blocks;

    /**
     * Initializes the pirates_fixing_state which is used to manage the state of a card and
     * its interactions with the player and game board during a specific phase of the game.
     *
     * @param cardO the card object associated with the game state.
     * @param oldState the previous state of the card before entering this state.
     * @param playerC the player associated with the current state.
     * @param fb the game board where the current state operates.
     * @param subSHIPS a list of spaceship tiles grouped into sublists, representing the ship components.
     * @throws Exception if an error occurs during state initialization or event dispatching.
     */
    public pirates_fixing_state(Card cardO, Card_State oldState, Player playerC, FlightBoard fb, ArrayList<ArrayList<SpaceShipTile>> subSHIPS) throws Exception {
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
     * Allows a player to choose one sub-ship from the available blocks.
     * It validates the player's ID and ensures the chosen index is within the valid range.
     * If the player is blocked or inputs an invalid index, appropriate events are triggered.
     * Upon successful selection, all sub-ships except the selected one are removed,
     * and corresponding events are dispatched.
     *
     * @param index the index of the sub-ship to be selected
     * @param playerID the ID of the player making the selection
     * @throws Exception if an issue arises during event dispatch or state transition
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
        card.getGame().getEventBus()
                .ChooseSubship(player.getUsername(), blocks, index,
                        player.getMyShip().getLostPieces());

        card.setState(state);
        card.getState().Continue(FB);
    }
}
