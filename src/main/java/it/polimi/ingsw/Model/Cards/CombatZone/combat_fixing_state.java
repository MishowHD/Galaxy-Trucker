package it.polimi.ingsw.Model.Cards.CombatZone;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Card_State;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;

import static it.polimi.ingsw.Model.Cards.Utils_Cards.c_State.CHOOSE_DISC_BLOCK;

public class combat_fixing_state extends Card_State {
    /**
     * Represents the card instance currently associated with the combat fixing state.
     * This card is used to manage state transitions, communicate events, and perform
     * actions specific to the combat phase within the game's systems.
     *
     * The `card` is an abstract representation of a game component, which holds the
     * specific logic and behavior regarding state management, event handling, and any
     * penalties, rewards, or actions triggered during gameplay.
     */
    private Card card;
    /**
     * Represents the flight board associated with the combat fixing state.
     * Used to manage and retrieve information regarding the current state of the game board
     * in the context of spaceship combat and sub-ship selection.
     */
    private FlightBoard FB;
    /**
     * Represents the unique identifier associated with a player.
     * This identifier is used for differentiating between players
     * in game-related operations and state management.
     */
    private int PlayerId;
    /**
     * Represents the current player associated with the combat fixing state.
     * This player interacts with the game mechanics, including managing their
     * ship, handling tiles or decks in hand, and advancing game states based on actions.
     *
     * The player's state, properties, and actions influence the resolution
     * of the combat fixing logic, including the selection and management of
     * spaceship blocks.
     *
     * This variable is heavily utilized to ensure logic consistency during
     * the resolution of combat and related game mechanics within the
     * combat_fixing_state class.
     */
    private Player player;
    /**
     * Represents the state of a card within the combat fixing process.
     * This variable stores the previous {@code Card_State} that can be returned to
     * after completing the current operation. It helps in transitioning between
     * different states during the game's execution.
     */
    private Card_State state;
    /**
     * Represents a collection of blocks in the form of a nested list of {@code SpaceShipTile} objects.
     * Each inner list is a group of tiles that forms a block, and the outer list contains all such blocks.
     * Typically used to manage and manipulate subsets of spaceship components in the game.
     */
    private ArrayList<ArrayList<SpaceShipTile>> blocks;

    /**
     * Constructor for the combat_fixing_state class, which initializes the state of card combat fixing.
     * This method sets up the card state, associates it with the player, and triggers events
     * required for choosing sub-ships during gameplay.
     *
     * @param cardO The card object associated with this state.
     * @param oldState The previous state of the card before transitioning into this state.
     * @param playerC The player associated with the card and the current state transition.
     * @param fb The FlightBoard that contains the current state of the game.
     * @param subSHIPS A collection of spaceship tiles representing the sub-ships for player action.
     * @throws Exception If invalid arguments or actions occur within the state initialization.
     */
    public combat_fixing_state(Card cardO, Card_State oldState, Player playerC, FlightBoard fb, ArrayList<ArrayList<SpaceShipTile>> subSHIPS) throws Exception {
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
     * Allows a player to choose one subship to retain while removing the other blocks within the provided blocks list.
     * If the player is not the correct one or blocked, or the index is invalid, the method will notify the event bus
     * of incorrect input or player mismatch. Successfully choosing a subship triggers various updates, such as removing
     * the unwanted blocks, adjusting the lost pieces count, and moving to the next game state.
     *
     * @param index the index of the chosen subship within the blocks list to be retained
     * @param playerID the unique identifier of the player attempting to perform the action
     * @throws Exception if any internal errors occur during block removal, event bus notification, or state updates
     */
    public void chooseOneSubShip(int index, int playerID) throws Exception {

        if (PlayerId == playerID && !player.isBlocked()) {
            if (index < 0 || index >= blocks.size()) {

                card.getGame().getEventBus().wrongInput(FB.getPlayerfromID(playerID));
                return;
            } else {
                int waste = player.getMyShip().getLostPieces();
                for (int i = 0; i < blocks.size(); i++) {
                    if (i != index) {
                        System.out.println("ciao, sono qui e devo rimuovere un blocco");
                        waste += player.getMyShip().removeBlock(blocks.get(i));
                        card.getGame().getEventBus().removeBlock(blocks.get(i), player.getUsername());
                        waste = waste - 1;
                        player.getMyShip().addLostPieces(waste);
                    }
                }
                card.getGame().getEventBus().ChooseSubship(player.getUsername(), blocks, index, player.getMyShip().getLostPieces());
                card.setState(state);
                card.getState().goNextState(player);
            }
        } else {
            System.out.println("wrong player");
            card.getGame().getEventBus().wrongPlayer(FB.getPlayerfromID(playerID));
        }

    }
}
