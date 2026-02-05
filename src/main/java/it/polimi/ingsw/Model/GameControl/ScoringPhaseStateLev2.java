package it.polimi.ingsw.Model.GameControl;

import it.polimi.ingsw.Utils.stateEnum;

public class ScoringPhaseStateLev2 extends Scoring_Phase_State {

    /**
     * Constructs a new ScoringPhaseStateLev2 object and updates the game state.
     *
     * @param game the game instance used to initialize the scoring phase state
     * @throws Exception if there is an issue during the initialization of the scoring phase state
     */
    public ScoringPhaseStateLev2(Game game) throws Exception {
        super(game);
        game.getEventBus().updateStatus(stateEnum.SCORING);

    }

}
