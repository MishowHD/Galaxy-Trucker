package it.polimi.ingsw.Utils;

public enum stateEnum {
    /**
     * Represents the initial state in the state machine or process workflow.
     * This state typically indicates the beginning or default setup phase before
     * any specific actions or transitions have occurred.
     */
    INITIAL,
    /**
     * Represents a state where a process or system is in the phase of being actively built
     * or constructed. This state may involve initialization, setup, or preparation tasks
     * required to bring the system or component to a functional state.
     */
    BUILDING,
    /**
     * Represents the POSITIONING state within the stateEnum enumeration.
     * This state is used to indicate the phase where positioning actions
     * or operations are performed within the application or game logic.
     */
    POSITIONING,
    /**
     * Represents the FLIGHT state in the application's state management system.
     * This state typically indicates that a flight-related operation or phase is active.
     * It is one of the possible states in the stateEnum enumeration,
     * which is used to transition between various stages or phases in the application.
     */
    FLIGHT,
    /**
     * Represents the SCORING state in the stateEnum enumeration.
     * This state is typically used to signify the scoring phase of a process or system.
     * It may involve operations or logic related to calculating or assigning scores.
     */
    SCORING,
    /**
     * Represents the state when the card-related process has started.
     * This enum constant is used to denote a phase in which card initialization,
     * actions, or interactions have commenced in the system's workflow.
     */
    CARD_STARDED,
    /**
     * Represents a state in the game where the number of tiles is being set.
     * This state typically transitions from or to other states within the game's lifecycle
     * and plays a key role in managing the setup or modification of game tiles.
     */
    SET_NUM_TILE,
    /**
     * Represents the state in the system where alien attributes or functionalities are being set.
     * This state might correspond to a specific phase in a process or game where alien-related
     * configurations or operations are initialized or modified.
     */
    SET_ALIEN,
}

