package it.polimi.ingsw.Model.Cards.Utils_Cards;

public enum c_State {
    /**
     * Represents the preparation phase for the "Open Space" card.
     * This state is used to initialize or set up actions related to
     * the "Open Space" card before transitioning to other states.
     */
    // Stati per Card_OpenSpace
    OPEN_SPACE_PREPARATION,
    /**
     * Represents the final state in the OPEN_SPACE card flow of the game.
     * This state signifies the conclusion of all actions specific to the OPEN_SPACE card.
     */
    OPEN_SPACE_FINAL,
    /**
     * Represents the final state of the Stardust card within the game.
     * This enumeration value indicates that all actions or effects
     * associated with the Stardust card have been concluded.
     */
    //STARDUST
    STARDUST_FINAL,
    /**
     * Represents a state where the calculations related to Pirates in the game are performed.
     * This state is typically used to compute effects, outcomes, or interactions specific
     * to the Pirates scenario during the gameplay.
     */
    // Stati per Card_Pirates
    PIRATES_CALC_STATE,
    /**
     * Represents the state in which the effect of a Pirates card is being resolved.
     * This state is part of the state machine for managing game events specific to Pirates cards.
     * It is typically used when actions or consequences of a Pirates card are applied in gameplay.
     */
    PIRATES_EFFECT_STATE,
    /**
     * Represents the state where the Pirates have achieved victory in a game context.
     * This state signals the conclusion of the Pirates' gameplay with a winning result.
     * Typically part of a finite state machine (FSM) managing game card events and transitions.
     */
    PIRATES_WINNING_STATE,
    /**
     * Represents the end state of a game phase involving pirates. This state
     * typically indicates that all actions, calculations, or resolutions related
     * to the pirates' phase have been completed. Transitioning into this state
     * often means the game will move to a subsequent phase or conclude the interaction
     * involving this card or game element.
     */
    PIRATES_END_STATE,
    /**
     * Represents the state where pirates have lost in the game.
     * This constant is part of the game state management system for handling different
     * phases or states associated with the "Pirates" card in the game.
     */
    PIRATES_LOSING_STATE,

    /**
     * Represents the calculation state specific to the Smugglers card, during
     * which the game processes calculations or interactions related to this card.
     * This is one of the distinct states in the Smugglers sequence and often
     * indicates an ongoing step in the resolution or processing tied to this card.
     */
    // Stati per Card_Smugglers
    SMUGGLERS_CALC_STATE,
    /**
     * Represents the state indicating that smugglers have lost batteries.
     * This state is part of the overall card game flow and is specific to
     * the handling of Smugglers-related scenarios within the game.
     * It typically signifies a situation where batteries have been lost during interactions
     * with smugglers or specific game events related to smugglers.
     */
    SMUGGLERS_LOST_BATTERIES_STATE,
    /**
     * Represents a specific state in the game where smugglers have lost goods.
     * This state is part of the Smugglers card scenario, likely indicating
     * a consequence or outcome related to the gameplay involving smugglers.
     */
    SMUGGLERS_LOST_GOODS_STATE,
    /**
     * This state represents the condition in which the smugglers emerge victorious
     * in their specific scenario. It symbolizes the successful outcome for smugglers
     * during gameplay mechanics and transitions.
     */
    SMUGGLERS_WINNING_STATE,
    /**
     * Represents the final state in the sequence of events associated with the Smugglers card.
     * This state indicates the end of all operations and calculations related to the Smugglers card.
     * Typically used to signify the conclusion of the Smugglers card interaction within a gameplay session.
     */
    SMUGGLERS_END_STATE,

    /**
     * Represents the calculation state for the "Slavers" card.
     * This state is used to manage the process associated with
     * determining the effects or outcomes of a "Slavers" card interaction.
     */
    // Stati per Card_Slavers
    SLAVERS_CALC_STATE,
    /**
     * Represents a specific state in the game where the Slavers are in a losing condition.
     * This state might signal that the Slavers' actions or strategies have led to their
     * unfavorable outcome during the gameplay.
     */
    SLAVERS_LOSING_STATE,
    /**
     * Represents the state in which the Slavers have achieved victory.
     * This is part of the game state flow for the Slavers card mechanics.
     */
    SLAVERS_WINNING_STATE,
    /**
     * Represents the final state of the Slavers card in the game.
     * This state signifies the conclusion of interactions or effects related to the Slavers card.
     */
    SLAVERS_END_STATE,
    /**
     * Represents a state where the card effect for the "Meteor Swarm" action
     * is being processed or executed in the game. This state may involve handling
     * the logic necessary for resolving the effects associated with a card
     * of this type during gameplay. It is part of the state management system
     * defined for various card-related operations.
     */
    // Stati per Card_MeteorSwarm
    METEOR_CARD_EFFECT_STATE,
    /**
     * Represents the calculation state within the meteor card workflow.
     * This state is responsible for handling the computations or evaluations
     * that occur when a meteor card is processed during the game.
     */
    METEOR_CARD_CALC_STATE,
    /**
     * Represents the final state for cards associated with meteor-related events.
     * This state signifies the conclusion of the meteor-specific card's lifecycle.
     */
    METEOR_CARD_END_STATE,
    /**
     * Represents the preparation state for an abandoned ship event.
     * This state is part of the defined sequence within the Card_AbandonedShip flow.
     * It is used to initialize or set up conditions before the main event is executed.
     */
    // Stati per Card_AbandonedShip
    ABANDONED_SHIP_PREPARATION,
    /**
     * Represents the state where an abandoned ship has been successfully taken or claimed
     * in the context of the associated game mechanics. This state follows the preparation
     * phase of handling an abandoned ship and indicates that the process of acquiring
     * the abandoned ship is complete.
     */
    ABANDONED_SHIP_TAKEN,

    /**
     * Represents the initial state for the "Abandoned Station" card.
     * This state marks the beginning phase where the "Abandoned Station" card's
     * effects or interactions are initialized or processed.
     */
    // Stati per Card_AbandonedStation
    ABANDONED_STATION_START_STATE,
    /**
     * Represents the state at the conclusion of interactions related to an abandoned station.
     * This state is reached after the resolution of all actions or effects associated with the
     * abandoned station card.
     */
    ABANDONED_STATION_END_STATE,
    /**
     * Represents the final state for abandoned gameplay elements, such as
     * ships or stations. This state typically indicates the conclusion of
     * interactions or processes related to abandoned entities.
     */
    ABANDONED_FINAL_STATE,
    /**
     * Represents the initial state of a combat zone interaction in the game.
     * This state typically marks the beginning stage of the combat zone sequence,
     * where initial preparations or setup related to the combat zone are made.
     */
    // Stati per Card_Combat_zone
    COMBAT_ZONE_BEGIN,
    /**
     * Represents a state within the Combat Zone phase of the game where players are choosing
     * whether to pass their turn or take a specific action. This state is used to determine
     * the next steps within the combat resolution process.
     */
    COMBAT_ZONE_CHOOSING_PASS,
    /**
     * Represents a specific state within the Combat Zone phase of the game,
     * where players are required to choose goods as part of their interactions
     * within the combat scenario.
     */
    COMBAT_ZONE_CHOOSE_GOODS,
    /**
     * A state within the Combat Zone phase where the player is required to choose
     * the number of batteries to allocate. This state is used to determine battery
     * distribution and resource management during combat scenarios.
     */
    COMBAT_ZONE_CHOOSE_BATTERIES,
    /**
     * Represents a state of the combat phase where the motion power of entities
     * is calculated or utilized. This state is part of the broader state management
     * for various card types, interacting with the combat zone context specifically.
     *
     * It may involve evaluating the movement-related capabilities or bonuses of
     * entities during the combat phase. Typically used in scenarios related to
     * action resolution within the combat zone.
     */
    COMBAT_MOTION_POWER,
    /**
     * Represents the combat firepower state within the context of a combat zone.
     * This state typically refers to the calculation or usage of firepower during a
     * combat scenario in the game, potentially impacting the outcome based on the
     * available combat resources or actions taken during this phase.
     */
    COMBAT_FIRE_POWER,
    /**
     * Represents the game state related to crew-based combat operations in the combat zone.
     * This state is used to handle and determine the role of crew participation during combat scenarios,
     * including actions that involve leveraging crew members for strategic advantages or resolving specific combat interactions.
     */
    COMBAT_CREW,
    /**
     * Represents the state where the selection of disc blocks is performed.
     * This state is typically part of the combat or resource management process
     * where players make decisions related to discarding, choosing blocks, or
     * managing selected items during gameplay.
     */
    CHOOSE_DISC_BLOCK,
    /**
     * Represents a specific state in the Meteor card handling process that transitions
     * from the combat zone mechanics. This state occurs when the calculation phase for
     * the meteor effects is derived directly from an ongoing combat scenario.
     *
     * This state is part of the `METEOR_CARD` flow and integrates in scenarios where
     * meteor-related mechanics are initiated during or after a combat sequence.
     *
     * Usage Context:
     * Typically used in determining the next logical step within the game logic when
     * transitioning from combat-related calculations to meteor-specific effects or resolutions.
     */
    METEOR_CARD_CALC_STATE_FROM_COMBAT,
    /**
     * Represents the state in which a Meteor card effect is applied during combat situations.
     * This state is specific to the context where the effect originates from a combat scenario.
     */
    METEOR_CARD_EFFECT_STATE_FROM_COMBAT,
    /**
     * Represents the final state of a combat zone card interaction.
     * This state signifies the conclusion of the combat zone sequence and transitions
     * out of combat zone-specific mechanics.
     */
    COMBAT_ZONE_FINAL,

    /**
     * Represents the base state for the Epidemic card within the game.
     * This state indicates the initial phase or setup of an epidemic event.
     * Typically used to transition between related states during the
     * processing of Epidemic card effects.
     */
    // Stati per Card_Epidemic
    EPIDEMIC_STATE_BASE,
    /**
     * Represents the final state of an epidemic card in the game.
     * This state is typically reached when all actions or effects
     * associated with the epidemic card have been resolved.
     */
    EPIDEMIC_STATE_FINAL,

    /**
     * Represents the initial state of a Planet Card during its lifecycle.
     * This state is used at the beginning of the interaction with the Planet Card.
     */
    // Stati per Card_PlanetCard
    PLANET_STATE_INIT,
    /**
     * PLANET_STATE_ADD_GOODS represents the state in which goods are added to a planet
     * during the execution of a PlanetCard in the game flow. This state occurs as part
     * of handling planetary interactions, allowing players to collect resources or fulfill specific actions.
     */
    PLANET_STATE_ADD_GOODS,
    /**
     * Represents the final state for the handling of a planet card in the game's state machine.
     * This state indicates the conclusion of interactions or processes related to a planet card.
     */
    PLANET_FIN_STAT,

    /**
     * Represents a generic and undefined state for the "Card_Stardust".
     * This state is not explicitly defined or handled in the code
     * but is included as a placeholder or fallback state.
     */
    // Stati per Card_Stardust (nessuno esplicito nel codice)
    GENERIC_STATE

}
