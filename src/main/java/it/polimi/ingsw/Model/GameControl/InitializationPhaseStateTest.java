package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitializationPhaseStateTest extends Initialization_Phase_State {

    /**
     * Constructs an instance of InitializationPhaseStateTest.
     *
     * @param game the game instance that this state belongs to
     */
    public InitializationPhaseStateTest(Game game) {
        super(game);
    }

    /**
     * Extracts a subset of cards from the event deck, shuffles them, and updates the
     * event deck within the game with the newly prepared subset. The resulting event deck
     * is restricted to a fixed number of cards and ensures that only test cards are included.
     *
     * The method performs the following operations:
     * - Retrieves the current event deck from the game.
     * - Assigns the game to each card in the deck to establish the context.
     * - Shuffles the entire event deck.
     * - Iterates through the cards in the deck and selects a fixed number of test cards.
     * - Creates a new deck consisting of the selected cards and shuffles it.
     * - Updates the game's event deck with the reconstructed and shuffled deck.
     * - Reassigns the game context to each card in the updated deck.
     *
     * This method enforces the condition that the event deck contains exactly eight test
     * cards after its execution.
     */
    @Override
    public void extractDeck() {
        Deck deck = game.getEventDeck();
        for (Card c : deck.getCardList()) {
            c.setGame(game);
        }
        deck.shuffle();
        List<Card> cards = deck.getCardList();
        List<Card> goodCards = new ArrayList<>();
        Card prov;
        int count = 0;
        while (count != 8) {
            prov = cards.getFirst();
            cards.removeFirst();
            if (prov.isTest()) {
                count++;
                goodCards.add(prov);
            }
        }
        deck = new Deck(goodCards);
        deck.shuffle();
        game.setEventDeck(deck);
        for (Card c : deck.getCardList()) {
            c.setGame(game);
        }
    }

    /**
     * Builds the flight board for the game during the initialization phase.
     *
     * This method creates an instance of the FlightBoard with the specified
     * number of players, maximum board size (18), the game's bank, and a reference
     * to the game itself. The newly created FlightBoard is then set as the game's
     * current flight board.
     */
    @Override
    public void buildFlightBoard() {
        FlightBoard fb = new FlightBoard(game.getNumOfPlayers(), 18, game.getBank(), game);
        game.setGameFlightBoard(fb);

    }

    /**
     * Executes the main logic of the InitializationPhaseStateTest by performing several setup operations
     * and preparing the game for the next phase. This method is called during the initialization phase
     * of the game and transitions the game to the next state when the setup is complete.
     *
     * The execution includes the following steps:
     * - Calls {@code fillBank()} to initialize and populate the game's resource bank.
     * - Calls {@code buildFlightBoard()} to initialize the flight board specific to the game configuration.
     * - Calls {@code buildTiles()} to shuffle and filter spaceship tiles based on predefined criteria.
     * - Calls {@code extractDeck()} to process the event deck used during gameplay.
     * - Sets the {@code finishedconstruction} flag to {@code true} indicating that the construction phase is complete.
     * - Finally, transitions the game to the next state by invoking {@code goNextState()}.
     *
     * This method assumes proper setup of the game context and its associated components before invocation.
     *
     * @throws Exception if any error occurs during the execution of initialization steps or state transition
     */
    @Override
    public void StateMain() throws Exception {
        fillBank();
        buildFlightBoard();
        buildTiles();
        extractDeck();
        finishedconstruction = true;
        goNextState();
        //game.setGameState(new BuildingPhaseStateTest(game));
        //game.getGameState().StateMain();
    }

    /**
     * Builds and initializes the spaceship tiles for the game during the initialization phase.
     *
     * This method retrieves the list of tiles from the game's data, shuffles them, and removes
     * tiles that are of specific types that are not meant to be accessible or used in the game.
     * After filtering, it reshuffles the remaining tiles and updates the game's tile set with
     * the modified list.
     *
     * Key processing steps:
     * - Retrieves the spaceship tiles.
     * - Shuffles the initial list of tiles.
     * - Removes tiles of types `Tile_NonAccesiblePlace` and `Tile_AlienLifeSupport`.
     * - Reshuffles the filtered list of tiles.
     * - Sets the updated list back to the game.
     */
    @Override
    public void buildTiles() {
        ArrayList<SpaceShipTile> tilesheap = game.getGameTiles();
        Collections.shuffle(tilesheap);
        tilesheap.removeIf(tile -> tile.getType() == SSTTypes.Tile_NonAccesiblePlace);
        tilesheap.removeIf(tile -> tile.getType() == SSTTypes.Tile_AlienLifeSupport);
        Collections.shuffle(tilesheap);
        game.setGameTiles(tilesheap);
    }

    /**
     * Returns the next game state, transitioning from the current state
     * to the BuildingPhaseStateTest.
     *
     * @return the next {@code GameState}, specifically an instance of
     *         {@code BuildingPhaseStateTest}, which represents the building phase
     *         of the game.
     */
    @Override
    public GameState getNextState() {
        return new BuildingPhaseStateTest(game);
    }


}
