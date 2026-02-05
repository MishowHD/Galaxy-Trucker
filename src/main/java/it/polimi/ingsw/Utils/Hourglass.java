package it.polimi.ingsw.Utils;

import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.GameControl.GameState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Hourglass implements Serializable {

    /**
     * Represents the amount of time, in milliseconds, that the Hourglass needs to remain inactive
     * before it can be activated again. This ensures that there is a resting period between activations
     * to prevent continuous or overlapping usage.
     *
     * The resting time is a fixed value and is initialized during the creation of the Hourglass instance.
     * It cannot be modified after initialization, as it is declared final.
     */
    private final int restingTime;
    /**
     * Represents the maximum number of times the hourglass can be activated.
     * This variable determines how many activations are allowed before the hourglass
     * becomes unusable. Each activation reduces this count until it reaches zero.
     * It is initialized during the construction of the Hourglass object.
     */
    private int timesCanBeActivated;
    /**
     * Represents the remaining time for the hourglass in its current state.
     * This variable is used to track how much time is left before the hourglass stops.
     * The time decreases as the hourglass progresses, and when it reaches zero,
     * it can trigger specific game events or mechanisms.
     *
     * This field is managed by the Hourglass class and is updated during the timer's operation.
     */
    private int currentRemainingTime;
    /**
     * Indicates whether the hourglass timer is currently active.
     * This variable is used to track the running state of the timer,
     * which is critical for controlling and managing its operation.
     * When true, the hourglass is in the running state; when false, it is stopped.
     */
    private boolean isRunning;
    /**
     * Represents the player who is currently activating or interacting with the hourglass.
     * This variable is used to track which player initiated the timer activation process.
     */
    private Player activatingPlayer;
    /**
     * A list of observers that will be notified about the state changes of the hourglass.
     * Observers are typically instances of the {@code GameState} class that respond
     * to key events such as the timer completing or other relevant changes.
     * This list is immutable after initialization.
     */
    private final List<GameState> observers;
    /**
     * A thread pool for managing scheduled tasks in the context of the Hourglass class.
     * This variable is used for executing periodic or delayed actions such as starting or stopping timers
     * and notifying observers upon completion of time-based events.
     *
     * It ensures efficient scheduling and execution of time-sensitive tasks, leveraging the
     * ScheduledExecutorService to handle concurrency and timing aspects within the Hourglass.
     */
    private ScheduledExecutorService scheduler;
    /**
     * Represents the current state of the game. This variable is final, meaning its reference cannot
     * be changed once initialized. It is used to track and manage the overall state in relation
     * to the hourglass's functionality and interactions with players or other components.
     *
     * This variable is provided during the initialization of the Hourglass instance and is
     * integral to the proper functioning of the object by allowing state-based actions,
     * such as notifying observers or controlling timer interactions.
     *
     * Being private and final ensures controlled access and immutability of the reference,
     * enhancing the consistency and reliability of the Hourglass instance's behavior throughout
     * its lifecycle.
     */
    private final GameState gameState;

    // TimerObserver interface: defines a method to be called when the timer completes.
    public interface TimerObserver {
    }

    /**
     * Constructs an Hourglass with specified resting time, number of activations, and game state.
     *
     * @param restingTime The total time, in milliseconds, for each activation.
     * @param timesCanBeActivated The number of times the hourglass can be activated.
     * @param gameState The current game state associated with the hourglass.
     */
    // Constructor: initializes the hourglass with a resting time and a number of activations.
    public Hourglass(int restingTime, int timesCanBeActivated, GameState gameState) {
        this.restingTime = restingTime; // Set the total time for each activation
        this.timesCanBeActivated = timesCanBeActivated; // Set how many times the timer can be activated
        this.currentRemainingTime = restingTime; // Initially, remaining time is equal to restingTime
        this.isRunning = false; // Timer is not running at the start
        this.observers = new ArrayList<>(); // Initialize the observers list
        this.scheduler = Executors.newSingleThreadScheduledExecutor(); // Create a scheduler with a single thread
        this.gameState = gameState;
    }

    /**
     * Adds an observer to the list of registered observers for the game state.
     * Ensures the observer is not null and is not already registered before adding it.
     *
     * @param observer the observer to be added to the list. It must implement the GameState interface and be valid.
     */
    // Method to add an observer to the list
    public void addObserver(GameState observer) {
        if (observer != null && !observers.contains(observer)) { // Check if observer is valid and not already added
            observers.add(observer); // Add observer to the list
        }
    }

    /**
     * Notifies all registered observers that the timer has completed.
     *
     * @param isLastActivation a boolean indicating whether this activation was the last one.
     * @throws Exception if an error occurs while notifying the observers.
     */
    // Method to notify all registered observers that the timer has completed.
    // The parameter indicates if this activation was the last one.
    private void notifyObservers(boolean isLastActivation) throws Exception {
        for (GameState observer : observers) { // Iterate over each observer
            observer.onTimerComplete(isLastActivation); // Call the observer's callback method
        }
    }

    /**
     * Starts the timer for the hourglass if it is not already running and activation conditions are met.
     *
     * The method performs preliminary checks to ensure the timer can be activated based on its state
     * and the player provided, initializes the internal state for the countdown, and schedules
     * periodic tasks to decrement the timer and notify observers when the timer reaches zero.
     *
     * @param player the Player object that is activating the timer. It is required for certain
     *               conditions, such as when only one activation remains and the player must be
     *               in a "blocked" state.
     * @return {@code true} if the timer starts successfully; {@code false} if the timer cannot
     *         be started due to being already running or certain activation conditions not being met.
     */
    // === In Hourglass ===
    public synchronized boolean startTimer(Player player) {
        // 1) controlli preliminari
        if (isRunning || timesCanBeActivated <= 0) {
            return false;
        }
        if (timesCanBeActivated == 1 && (player == null || !player.isBlocked())) {
            return false;
        }

        // 2) setup stato interno
        activatingPlayer = player;
        currentRemainingTime = restingTime;
        isRunning = true;

        // 3) (ri)creo lo scheduler in modo pulito
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();

        final long startTime = System.currentTimeMillis();

        // 4) tick ogni secondo
        scheduler.scheduleAtFixedRate(() -> {
            int elapsed = (int) ((System.currentTimeMillis() - startTime) / 1000);
            int newRemaining = Math.max(restingTime - elapsed, 0);

            if (newRemaining != currentRemainingTime) {
                currentRemainingTime = newRemaining;
                System.out.println("Remaining time: " + currentRemainingTime + "s");
            }

            // 5) fine conto alla rovescia
            if (currentRemainingTime == 0) {
                boolean lastActivation = (timesCanBeActivated == 1);

                try {
                    notifyObservers(lastActivation);
                    if (lastActivation) {
                        gameState.endBuildingPhaseForAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();              // conserva la gestione precedente
                }

                timesCanBeActivated--;
                isRunning = false;
                activatingPlayer = null;
                scheduler.shutdown();                 // chiusura ordinata
            }
        }, 0, 1, TimeUnit.SECONDS);

        return true;
    }


    /**
     * Stops the timer if it is currently running.
     *
     * This method checks if the timer is active and the scheduler is operational.
     * If the scheduler is not null and not already shut down, it forces the scheduler
     * to stop executing any further tasks. The internal state of the timer is updated
     * to indicate it is no longer running, and a confirmation message is printed.
     *
     * Preconditions:
     * - The scheduler must be properly initialized before the timer is started.
     *
     * Postconditions:
     * - If the timer was active, the scheduler will be shut down and the timer marked
     *   as stopped.
     * - If the timer was not running, the method will do nothing.
     */
    // Stops the timer if it is currently running.
    public void stopTimer() {
        if (isRunning && scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow(); // Force shutdown of the scheduler
            isRunning = false; // Mark the timer as stopped
            System.out.println("Timer stopped manually.");
        }
    }

}
