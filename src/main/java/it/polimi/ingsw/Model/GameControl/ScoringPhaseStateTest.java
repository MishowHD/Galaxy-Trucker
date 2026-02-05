package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Utils.stateEnum;

public class ScoringPhaseStateTest extends Scoring_Phase_State {

    /**
     * Initializes a test instance for the scoring phase state of the game.
     *
     * @param game the game instance to be used for initializing the scoring phase state
     * @throws Exception if an error occurs during initialization
     */
    public ScoringPhaseStateTest(Game game) throws Exception {
        super(game);
        game.getEventBus().updateStatus(stateEnum.SCORING);
    }

}
