package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Utils.Hourglass;
import it.polimi.ingsw.Model.Player.Player;

import java.util.HashMap;

public class Scoring_Phase_State extends GameState {

    /**
     * A map that stores the final scores of players in the game.
     * The key represents the username of a player, and the value represents
     * the player's final score as a floating-point number.
     * This variable is computed during the scoring phase of the game and
     * is used to display or update players' final scores.
     */
    private final HashMap<String, Float> finalScores;

    /**
     * Constructor for the Scoring_Phase_State class, initializing the scoring phase of the game.
     *
     * @param game The game instance associated with the current game state.
     *             This object contains game data and manages the progression of the game phases.
     */
    public Scoring_Phase_State(Game game) {
        super(game);
        finalScores = new HashMap<>();

    }

    /**
     * Executes the main scoring logic for the scoring phase of the game.
     *
     * This method calculates the final scores for each player based on ranking,
     * ship attributes, resources, and other factors. The scores are then stored
     * in a final scores map and passed to the game's event bus for further updates.
     * Finally, it invokes a method to visualize the final scores.
     *
     * @throws Exception if any issue arises during the execution of the scoring process.
     */
    @Override
    public void StateMain() throws Exception {
        // Check if all cards have been processed

        // Calculate scores and proceed to next phase
        for (Player p : game.getPlayers()) {
            int pointbyranking = 0;
            int pointbybeautifulship = 0;
            if (game.getGameFlightBoard().getPlayerwithbestShip() == p) {
                pointbybeautifulship = 2;
            }

            for (int i = 0; i < game.getGameFlightBoard().getRanking().size(); i++) {
                if (p == game.getGameFlightBoard().getRanking().get(i)) {
                    pointbyranking = 4 - i;
                }
            }
            p.setPlayerScore(
                    pointbyranking - p.getMyShip().getLostPieces() + (p.getMyShip().getTotalResourcesScore()) + p.getMyShip().getCosmicCredits() + pointbybeautifulship
            );
        }

        for (Player p : game.getPlayers()) {
            finalScores.put(p.getUsername(), p.getPlayerScore());
        }
        game.getEventBus().updateFinalScores(finalScores);
        visualizeFinalScores();
    }


    /**
     * Retrieves the final scores of the players at the end of the scoring phase.
     * The scores are calculated and stored as a mapping of player usernames to their
     * respective scores.
     *
     * @return a HashMap containing the usernames of the players as keys and their final scores as values
     * @throws RuntimeException if an error occurs while retrieving the final scores
     */
    @Override
    public HashMap<String, Float> visualizeFinalScores() throws RuntimeException {
        return finalScores;
    }
}
