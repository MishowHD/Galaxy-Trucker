package it.polimi.ingsw.Model.Boards;

import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.GameControl.Game;
import it.polimi.ingsw.Model.Player.Player;

import java.util.ArrayList;
import java.util.List;

public class Lev2FlightBoard extends FlightBoard {
    /**
     * A hidden deck of cards used internally within the Lev2FlightBoard to manage
     * unavailable or secret cards that are not directly accessible by players.
     *
     * This deck is immutable once assigned and may serve as a repository for cards
     * that are not in circulation within the playable area. The hidden deck can be
     * shuffled or manipulated internally, but remains inaccessible from external
     * systems.
     */
    private final Deck hiddenDeck;
    /**
     * A list of decks that are available to be interacted with or displayed during gameplay.
     * These decks are set to a free state upon initialization and can be managed or accessed
     * through relevant methods in the containing class.
     */
    private final List<Deck> showableDecks;
    /**
     * A list of players whose rank has been altered twice in a specific gameplay
     * context. This variable keeps track of players who have undergone certain
     * rank changes, such as being moved up or down the rank multiple times.
     *
     * It is primarily used to maintain a record of players affected by specific
     * rank modifications within the game mechanics.
     */
    private ArrayList<Player> doubledplayers;

    /**
     * Constructs a Lev2FlightBoard object with specified parameters.
     *
     * @param numPlayers    the number of players in the game
     * @param hiddenDeck    the hidden deck that remains inaccessible to players
     * @param showableDecks a list of decks that can be shown to the players
     * @param boardSize     the size of the board
     * @param bank          the bank associated with the game managing goods and resources
     * @param g             the game object that this board is part of
     */
    public Lev2FlightBoard(int numPlayers, Deck hiddenDeck, List<Deck> showableDecks, int boardSize, Bank bank, Game g) {
        super(numPlayers, boardSize, bank, g);
        this.hiddenDeck = hiddenDeck;
        this.showableDecks = showableDecks;
        for (Deck d : showableDecks) {
            d.setFree();
        }
        this.doubledplayers = new ArrayList<>();

    }

    /**
     * Represents the current ranking of players in the game.
     * This list contains players ordered by their rank, with the highest-ranked
     * player at the beginning of the list and the lowest-ranked player at the end.
     * The ranking is dynamically updated based on game events such as advancements
     * or lowering of ranks for specific players.
     */
    ArrayList<Player> playerRank = getPlayerRankList();

    /**
     * Moves a player's rank up by one position in the ranking list.
     * If the player is already at the top rank, they are added to the doubled players list.
     * Otherwise, the player's position is swapped with the player ranked one position above.
     *
     * @param player the player whose rank is to be advanced
     * @throws Exception if the player is not found in the ranking list
     */
    @Override
    public void advanceRank(Player player) throws Exception {
        int index = getPlayerRank(player);

        if (index == -1) {
            throw new RuntimeException("Player not found");
        }
        if (index == 0) {
            DoubledPlayer(playerRank.get(getNumPlayers() - 1));
        } else {
            Player temp = playerRank.get(index - 1);
            playerRank.set(index - 1, playerRank.get(index));
            playerRank.set(index, temp);
        }
    }

    /**
     * Lowers the rank of the specified player by swapping their position
     * with the player ranked immediately below them in the ranking list.
     * If the player is already at the lowest rank, their position remains unchanged.
     *
     * @param player the player whose rank is to be lowered in the ranking list
     * @throws Exception if the specified player is not found in the ranking list
     */
    @Override
    public void lowerRank(Player player) throws Exception {
        int index = getPlayerRank(player);
        System.out.println("rank: " + player.getUsername() + ": " + playerRank.get(index));
        // Controllo se il giocatore è già all'ultima posizione
        if (index >= playerRank.size() - 1) {
            // Se è l'ultimo, non può essere spostato più in basso
            return; // o gestisci come errore
        }
        Player playerAfter = playerRank.get(index + 1);
        if (index == -1) {
            throw new RuntimeException("Player not found");
        }
        if (index == playerRank.size() - 1 && playerAfter.getNumFullRounds() > player.getNumFullRounds()) {
            DoubledPlayer(player);
        } else {
            Player temp = playerRank.get(index + 1);
            playerRank.set(index + 1, playerRank.get(index));
            playerRank.set(index, temp);
        }
    }

    /**
     * Retrieves the list of decks that are considered showable for gameplay.
     *
     * @return a list of {@code Deck} objects representing the decks that can be displayed or used in the game.
     */
    public List<Deck> getShowableDecks() {
        return showableDecks;
    }

    /**
     * Adds the specified player to the list of players who have been doubled.
     *
     * @param player the player to add to the doubled players list
     */
    private void DoubledPlayer(Player player) {
        doubledplayers.add(player);
    }
    /**
     * Retrieves the list of players who have been marked as doubled.
     *
     * @return an ArrayList containing the players marked as doubled.
     */
    public ArrayList<Player> getDoubledplayers(){
        return doubledplayers;
    }
    /**
     * Resets the list of doubled players.
     *
     * This method clears and reinitializes the `doubledplayers` field by assigning a
     * new empty ArrayList. It effectively removes all players that might have been
     * previously added to the list of doubled players.
     */
    public void resetDoubledPlayers(){
        doubledplayers = new ArrayList<>();
    }
    /**
     * Retrieves a specific deck at the given position from the list of showable decks.
     *
     * @param position the index of the deck to retrieve from the list of showable decks
     * @return the deck located at the specified position in the list of showable decks
     */
    @Override
    public Deck getShowableDeck(int position) {
        return showableDecks.get(position);
    }

}
