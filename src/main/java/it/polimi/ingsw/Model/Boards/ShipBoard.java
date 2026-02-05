package it.polimi.ingsw.Model.Boards;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Utils.Bank;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Utils.RemoveTile.RemoveTileState;
import it.polimi.ingsw.Utils.RemoveTile.RemoveTileStatePreChoose;
import it.polimi.ingsw.Model.Boards.Utils_Boards.ShipBoard_iterable;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.SSTTypes;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.Model.Tiles.Utils_Tiles.Type_side_connector;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ShipBoard implements Serializable {
    /**
     * A two-dimensional array representing the layout of tiles on a spaceship.
     * Each element in the array is a {@code SpaceShipTile}, representing a specific part
     * or component of the spaceship.
     *
     * The dimensions of the array are fixed at 5 rows and 7 columns.
     * This array serves as the primary matrix for managing and organizing
     * the structure of the spaceship on the game board.
     */
    SpaceShipTile[][] ShipMatrix = new SpaceShipTile[5][7];
    /**
     * Represents an iterable object associated with the shipboard that provides
     * mechanisms to traverse or interact with its ship matrix or other components.
     * This variable is initialized with the current instance of the ShipBoard
     * for appropriate binding and functionality support.
     */
    ShipBoard_iterable ShipBoardIterable = new ShipBoard_iterable(this);
    /**
     * Represents the number of available batteries present on the shipboard.
     * This value determines the ship's capacity to store and utilize power units,
     * which may be consumed for various actions such as activating components or systems.
     */
    private int availableBatteryNumber;
    /**
     * Represents the number of passengers currently available on the ship.
     * This variable tracks the dynamic count of passengers that are still on board
     * during the course of the game or operation. The value may decrease
     * or increase depending on actions or events that occur, such as passengers
     * being removed or added.
     */
    private int availablePassengerNumber;

    /**
     * Represents the number of red goods currently available on the shipboard.
     * This variable is used to track and manage the inventory of red goods,
     * which may influence gameplay or operational functions related to resource
     * management on the ship.
     */
    private int availableRedGoods;
    /**
     * Represents the quantity of available yellow goods in the context of the spaceship board.
     * This value is expected to track the current inventory of yellow goods that can be utilized
     * or managed during the game. The variable may be modified based on game state changes or operations
     * involving goods management.
     */
    private int availableYellowGoods;
    /**
     * Represents the number of available green goods on the shipboard.
     *
     * This variable tracks the current quantity of green goods that can be utilized
     * or allocated within the ship during gameplay. The value may be modified as
     * the game progresses and goods are added or removed.
     */
    private int availableGreenGoods;
    /**
     * Represents the number of available blue goods stored on the ship.
     * This variable tracks the current count of blue-colored goods
     * that are available and managed within the ship's storage system.
     */
    private int availableBlueGoods;

    /**
     * Retrieves the current state associated with the remove tile operation.
     *
     * @return the current RemoveTileState instance representing the state of the remove tile operation.
     */
    public RemoveTileState getRemoveTileState() {
        return this.state;
    }


    /**
     * Represents the total number of cosmic credits available on the shipboard.
     *
     * Cosmic credits are a primary resource in the context of the class,
     * often used to perform various operations, trade, or upgrades during gameplay.
     * This value is managed internally within the class and can be modified or retrieved
     * using dedicated methods.
     */
    private int cosmicCredits;

    /**
     * Represents the flight board associated with the ship.
     * This variable holds information and functionality related to the flight phase of the game,
     * managing aspects such as the ship's interactions during flight and related configurations or states.
     */
    private FlightBoard flightBoard;
    /**
     * Indicates the presence of a Violet Alien on the ship board.
     * This field represents whether the ship has a Violet Alien assigned or not.
     * It is used to track and manage state or actions related to the specific type of alien.
     */
    protected boolean VioletAlien;
    /**
     * Represents the presence of a Brown Alien on the spaceship board.
     * This boolean variable indicates whether a Brown Alien is active or exists
     * in the current state of the ShipBoard.
     */
    protected boolean BrownAlien;
    /**
     * Represents the total number of pieces that have been lost from the spaceship.
     * This variable tracks and manages the state of lost components during gameplay.
     */
    private int LostPieces;
    /**
     * Represents the player associated with the current game state of the shipboard.
     * This variable typically holds the Player instance that interacts with
     * the ShipBoard, their resources, and game mechanics related to the player.
     */
    private Player myPlayer;
    /**
     * Represents the current state of the tile removal process within the shipboard.
     * This variable defines the operational logic and transitions for managing the
     * removal of spaceship tiles based on the specific game state.
     *
     * The state is an instance of the {@link RemoveTileState} class, which provides
     * the methods and rules for the ongoing process, including transitioning to
     * the next state, performing main actions during tile removal, and selecting
     * subtile groups as part of the shipboard configuration.
     *
     * This field is protected and intended to be used internally within the
     * {@code ShipBoard} class and its subclasses.
     */
    protected RemoveTileState state;
    /**
     * A flag indicating whether the state of the ShipBoard has been modified.
     * This variable tracks changes to the internal state of the ShipBoard and can be used
     * to determine whether certain updates or recalculations need to be performed.
     */
    private boolean isModified;
    /**
     * Represents the number of tiles currently present on the shipboard.
     * This field is used to keep track of the total count of tiles on the board,
     * which may be updated as tiles are added or removed during gameplay.
     */
    private int numTile;
    /**
     * A static final comparator used to sort Goods objects in descending order based on their value.
     * This comparator first extracts the integer value of each Goods object using the `getValue` method
     * and then applies a reversed order to facilitate descending sorting.
     */
    //  private boolean isBuilding;
    // Comparator globale in ordine decrescente di valore
    private static final Comparator<Goods> BY_VALUE_DESC =
            Comparator.comparingInt(Goods::getValue).reversed();

    /**
     * Represents the state of whether the selection of sub-ships is allowed or required
     * in the context of the current ShipBoard instance.
     *
     * This variable indicates if the user or system has the capability or is mandated
     * to engage in operations involving sub-ship selection and configuration.
     */
    private boolean choosesubships;

    /**
     * Constructor for the ShipBoard class. Initializes various resources and settings,
     * and associates the shipboard with a flight board and player.
     *
     * @param flightBoard the flight board associated with this shipboard
     * @param myPlayer the player associated with this shipboard
     */
    public ShipBoard(FlightBoard flightBoard, Player myPlayer) {
        this.availableBatteryNumber = 0;
        this.availablePassengerNumber = 0;
        this.availableRedGoods = 0;
        this.availableYellowGoods = 0;
        this.availableGreenGoods = 0;
        this.availableBlueGoods = 0;
        this.cosmicCredits = 0;
        this.flightBoard = flightBoard;
        this.myPlayer = myPlayer;
        this.VioletAlien = false;
        this.BrownAlien = false;
        this.LostPieces = 0;
        //da dire quali caselle sono disponibili
        this.state = new RemoveTileStatePreChoose(this);
        this.numTile = -1;
        // this.isBuilding=true;
        this.choosesubships = false;
    }

    /**
     * Sets the value for the choosesubship state of the ShipBoard.
     *
     * @param choosesubship a boolean indicating whether the choosesubship state should be enabled or disabled
     */
    public void setchoosesubship(boolean choosesubship) {
        this.choosesubships = choosesubship;
    }

    /**
     * Retrieves the current state of the 'choosesubships' flag, indicating whether subship selection is enabled.
     *
     * @return true if subship selection is enabled, false otherwise
     */
    public boolean getchoosesubship() {
        return choosesubships;
    }

    /**
     * Retrieves the number of tiles associated with the shipboard.
     *
     * @return the number of tiles.
     */
    public int getNumTile() {
        return numTile;
    }

    /**
     * Sets the number of tiles for the shipboard.
     *
     * @param numTile the number of tiles to be set on the shipboard
     * @throws Exception if any error occurs during the execution
     */
    public void setNumTile(int numTile) throws Exception {
        this.numTile = numTile;

    }

    /**
     * Advances the current game state to the next state and performs the necessary updates.
     *
     * @param subships         A list of sublists, where each sublist represents a connected block
     *                         in the spaceship layout.
     * @param indextopreserve  An integer representing the index of a specific subship to preserve
     *                         during the state transition.
     * @param playerID         An integer representing the unique ID of the player interacting with
     *                         the spaceship.
     * @param waste            An integer representing the waste generated during the transition to
     *                         the next state.
     * @throws Exception       If any unexpected error occurs during the state transition.
     */
    public void Gonextstate(ArrayList<ArrayList<SpaceShipTile>> subships, int indextopreserve, int playerID, int waste) throws Exception {
        state = state.GetNextState();

        state.StateMain(subships, this, playerID, indextopreserve, waste);
    }

    /**
     * Retrieves the matrix of the spaceship tiles for the current shipboard.
     *
     * @return a 2D array of SpaceShipTile objects representing the structure of the spaceship.
     */
    public SpaceShipTile[][] getShipMatrix() {
        return ShipMatrix;
    }

    /**
     * Retrieves the current player associated with the shipboard.
     *
     * @return the Player instance representing the current player.
     */
    public Player getMyPlayer() {
        return myPlayer;
    }

    /**
     * Checks if the state of the object has been modified.
     *
     * @return true if the object has been modified; false otherwise.
     */
    public boolean isModified() {
        return this.isModified;
    }

    /**
     * Sets the modified state of the shipboard.
     *
     * @param isModified a boolean value indicating whether the shipboard has been modified (true) or not (false)
     */
    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    /**
     * Calculates and returns the total number of available red goods across all
     * special cargo hold tiles within the shipboard.
     *
     * The method iterates through all tiles of type `Tile_SpecialCargoHold`
     * obtained from the `ShipBoardIterable`. It sums up the size of the effective
     * present goods on each tile to determine the total number of red goods
     * available.
     *
     * @return the total number of red goods available in the special cargo hold tiles
     * @throws RuntimeException if an error occurs during the calculation process
     */
    public int getAvailableRedGoods() throws RuntimeException {
        availableRedGoods = 0;
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_SpecialCargoHold)) {
            availableRedGoods += t.getEffectivePresentGoods().size();
        }
        return availableRedGoods;
    }

    /**
     * Calculates and returns the total number of available blue goods present
     * in all cargo hold tiles of the spaceship.
     *
     * The method iterates through all tiles of type "Tile_CargoHold" in the
     * shipboard and sums the quantity of blue goods found in these tiles.
     *
     * @return the total number of blue goods available in the spaceship.
     * @throws RuntimeException if there is an issue while calculating the blue goods.
     */
    public int getAvailableBlueGoods() throws RuntimeException {
        availableBlueGoods = 0;
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_CargoHold)) {
            availableBlueGoods += t.getEffectivePresentGoods().size();
        }
        return availableBlueGoods;

    }

    /**
     * Checks if this entity is identified as a Violet Alien.
     *
     * @return true if the entity is a Violet Alien, false otherwise.
     */
    public abstract boolean isVioletAlien();

    /**
     * Checks whether the shipboard contains a brown alien.
     *
     * @return true if a brown alien is present on the shipboard, false otherwise.
     */
    public abstract boolean isBrownAlien();

    /**
     * Retrieves the current amount of cosmic credits available on the ship.
     *
     * @return the current number of cosmic credits
     */
    public int getCosmicCredits() {
        return cosmicCredits;
    }

    /**
     * Adds the specified amount to the current cosmic credits of the ship.
     *
     * @param i the amount of cosmic credits to add; can be positive or negative.
     */
    public void addCosmicCredits(int i) {
        cosmicCredits += i;
    }

    /**
     * Retrieves the FlightBoard associated with the ShipBoard.
     *
     * @return the current FlightBoard instance associated with this ShipBoard.
     */
    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    /**
     * Sets the flight board for the ShipBoard instance.
     *
     * @param flightBoard the FlightBoard object to be set as the current flight board
     */
    public void setFlightBoard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
    }

    /**
     * Calculates and returns the total number of yellow goods available on the ShipBoard.
     * The method iterates through all cargo hold tiles on the ship, summing up the quantity
     * of yellow goods in each tile.
     *
     * @return the total count of available yellow goods
     * @throws RuntimeException if an error occurs during the calculation
     */
    public int getAvailableYellowGoods() throws RuntimeException {
        availableYellowGoods = 0;
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_CargoHold)) {
            availableYellowGoods += t.getEffectivePresentGoods().size();
        }
        return availableYellowGoods;
    }

    /**
     * Calculates and returns the total number of green goods available on the shipboard
     * by iterating through all cargo hold tiles and summing up their respective green goods.
     *
     * @return the total number of available green goods on the shipboard
     * @throws RuntimeException if an error occurs during the calculation process
     */
    public int getAvailableGreenGoods() throws RuntimeException {
        availableGreenGoods = 0;
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_CargoHold)) {
            availableGreenGoods += t.getEffectivePresentGoods().size();
        }
        return availableGreenGoods;
    }

    /**
     * Calculates and returns the total number of passengers available in all cabin tiles
     * on the ship. The method iterates through all tiles of type "Tile_Cabin"
     * and sums their effective passenger capacity.
     *
     * @return the total number of passengers available on the ship
     * @throws RuntimeException if an error occurs during the calculation
     */
    public int getPassengerNumber() throws RuntimeException {
        availablePassengerNumber = 0;
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Cabin))
            availablePassengerNumber += ShipBoardIterable.getTileEffCapacity(t);
        return availablePassengerNumber;
    }

    /**
     * Provides an iterable representation of the ship's board, allowing iteration over its elements.
     *
     * @return an iterable representing the ship's board
     * @throws RuntimeException if there is an error returning the ship board iterable
     */
    public ShipBoard_iterable getShipBoardIterable() throws RuntimeException {
        return ShipBoardIterable;
    }

    /**
     * Updates the available resources and attributes of the ship board.
     * This method recalculates and refreshes the quantities of goods,
     * passenger count, and battery count by invoking respective getter
     * methods for each attribute. Additionally, it updates the basic
     * firepower and motion power metrics.
     *
     * @throws RuntimeException if any resource retrieval method fails
     */
    public void updateAttributes() throws RuntimeException {
        availableRedGoods = getAvailableRedGoods();
        availableBlueGoods = getAvailableBlueGoods();
        availableYellowGoods = getAvailableYellowGoods();
        availableGreenGoods = getAvailableGreenGoods();
        availableBatteryNumber = getBatteriesNumber();
        availablePassengerNumber = getPassengerNumber();
        getBasicFirePower();
        getBasicMotionPower();
        //TotalResorcesScore = getTotalResourcesScore();
    }

    /**
     * Checks and compares the position of goods before and after the operation,
     * ensuring that the goods are valid and correctly assigned to their respective
     * positions in the defined cargo-hold tiles.
     *
     * @param before the list of goods owned by the player before the operation, cannot be null
     * @param after a nested list representing the goods distributed into sets after the operation,
     *              where each inner list corresponds to a tile's goods
     * @param pos a nested list of integer pairs where each pair represents the coordinates of
     *            a cargo-hold tile in the spaceship
     * @return true if the goods are correctly positioned and validated according to the rules;
     *         false if any condition fails, such as mismatched data, invalid positions, or constraints violations
     */
    public boolean checkAndCompareGoodsPosition(ArrayList<Goods> before, ArrayList<ArrayList<Goods>> after, ArrayList<ArrayList<Integer>> pos) {
        if (before == null) {
            System.out.println("Parameter 'before' cannot be null");
            return false;
        }
        if (pos == null) {                   // nessuna posizione → nessuna merce dopo
            return after == null || after.isEmpty();
        }
        if (after == null || after.size() != pos.size()) {
            System.out.println("Mismatch between positions and goods sets");
            return false;
        }
        ArrayList<Goods> copyBefore = new ArrayList<>(before);
        for (int i = 0; i < after.size(); i++) {
            ArrayList<Goods> pile = after.get(i);
            if (pile == null) {
                System.out.printf("Goods list at index %d is null%n", i);
                return false;
            }
            for (Goods g : pile) {
                if (!copyBefore.remove(g)) {
                    System.out.printf("Good %s not owned by player%n", g);
                    return false;
                }
            }
        }
        for (int i = 0; i < pos.size(); i++) {
            ArrayList<Integer> coord = pos.get(i);
            if (coord == null || coord.size() < 2) {
                System.out.printf("Invalid coordinate at index %d%n", i);
                return false;
            }
            int r = coord.get(0);
            int c = coord.get(1);
            SpaceShipTile tile = ShipMatrix[r][c];
            SSTTypes type = tile.getType();
            if (type != SSTTypes.Tile_CargoHold &&
                    type != SSTTypes.Tile_SpecialCargoHold) {
                System.out.printf("Tile (%d,%d) is not a cargo-hold%n", r, c);
                return false;
            }
            int capacity = tile.getCapacity();
            int goodsCnt = after.get(i).size();
            if (goodsCnt > capacity) {
                System.out.printf("Tile (%d,%d) capacity exceeded (%d/%d)%n",
                        r, c, goodsCnt, capacity);
                return false;
            }
            if (type == SSTTypes.Tile_CargoHold) {
                for (Goods g : after.get(i)) {
                    if (g.getValue() == 4) {
                        System.out.printf(
                                "Radioactive goods not allowed in normal cargo-hold (%d,%d)%n", r, c);
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Defines the tiles on the shipboard that cannot be accessed during the
     * gameplay. This method typically identifies and marks specific areas
     * or zones on the board as non-accessible based on predefined rules or
     * conditions.
     *
     * This is an abstract method and must be implemented by subclasses.
     * The actual implementation will depend on the specific rules or logic
     * associated with the subclass.
     *
     * @throws RuntimeException if the operation encounters an error or
     * if the non-accessible tiles cannot be determined or set due to
     * invalid conditions or data.
     */
    public abstract void setNonAccessibleTiles() throws RuntimeException ;


    /**
     * Increases the count of lost pieces on the ship board.
     *
     * @param i the number of lost pieces to add to the current total
     */
    public void addLostPieces(int i) {
        LostPieces += i;
    }

    /**
     * Adds an alien of the specified color to the ship board.
     *
     * @param alien_colour the color of the alien to be added, represented by the AlienColor enum
     * @throws RuntimeException if adding the alien fails due to an illegal operation or state
     */
    public abstract void addAlien(AlienColor alien_colour) throws RuntimeException;

    /**
     * Removes the tiles marked as "wait" at the end of the building phase of the shipboard.
     *
     * This method is typically invoked during the finalization process of the ship construction
     * to clean up specific placeholder or temporary tiles used during the building phase.
     * The implementation is abstract to allow subclasses to define the exact behavior
     * depending on the context of the specific shipboard mechanics.
     */
    public abstract void removeWaitTilesForEndBuilding();


    /**
     * Inserts a SpaceShipTile into the ship matrix at the specified coordinates.
     *
     * @param tile The SpaceShipTile to be inserted.
     * @param x The x-coordinate where the tile should be placed.
     * @param y The y-coordinate where the tile should be placed.
     */
    public void insertTile(SpaceShipTile tile, int x, int y) {
        ShipMatrix[x][y] = tile;
    }

    /**
     * Removes all passengers from all cabin tiles in the spaceship.
     *
     * This method iterates through all tiles of type SSTTypes.Tile_Cabin on the spaceship board
     * and invokes the removePassenger method on each tile to remove its current number of passengers.
     *
     * @throws Exception if an error occurs while processing the removal of passengers.
     */
    public void removeAllPassengers() throws Exception {
        for (SpaceShipTile st : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Cabin)) {
            st.removePassenger(st.getNumPassengers());
        }
    }
    //public void removeAllTiles(){} non serve


    /**
     * Removes all battery charges from the battery components on the ship board.
     *
     * This method iterates over all tiles of type `Tile_BatteryComponent` within the ship board.
     * For each tile, if it contains any charges, all the charges are removed, effectively
     * depleting the battery component.
     */
    public void looseAllBatteris() {
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_BatteryComponent)) {
            int charges = t.getNumCharges();
            if (charges == 0) continue;                       // niente da fare
            t.removeCharge(charges);                          // svuota il componente
        }
    }

    /**
     * Removes all goods from the spaceship by iterating through tiles
     * that can store goods and transferring their contents.
     *
     * This method processes two types of storage tiles:
     * - Special cargo-hold tiles.
     * - Normal cargo-hold tiles.
     *
     * Each tile of the specified types is located using the
     * ShipBoardIterable utility, and the goods on those tiles are
     * moved using the moveGoods method.
     */
    public void looseAllGoods() {
        // Special cargo-hold
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_SpecialCargoHold)) {
            moveGoods(t);
        }

        // Cargo-hold normale
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_CargoHold)) {
            moveGoods(t);
        }
    }

    /**
     * Moves all goods from the specified spaceship tile to the bank associated with the flight board.
     * Clears the goods present on the given tile after transferring them.
     *
     * @param t the spaceship tile from which goods will be moved
     */
    private void moveGoods(SpaceShipTile t) {
        List<Goods> toRemove = new ArrayList<>(t.getEffectivePresentGoods());
        for (Goods g : toRemove) {
            flightBoard.getBank().addGood(g);
        }
        t.getEffectivePresentGoods().clear();   // oppure t.removeGoods(g) uno ad uno
    }


    /**
     * Adds a unit of battery by recharging all battery components of the spaceship.
     *
     * This method retrieves all tiles of type SSTTypes.Tile_BatteryComponent from the ship board
     * and invokes the RechargeAll method on each of them, ensuring that their battery capacity
     * is fully restored. If any underlying process fails, a RuntimeException is thrown.
     *
     * @throws RuntimeException if an error occurs during the operation
     */
    public void addBatteryUnit() throws RuntimeException {
        ArrayList<SpaceShipTile> tiles = new ArrayList<>(ShipBoardIterable.getTilesOfType(SSTTypes.Tile_BatteryComponent));
        for (SpaceShipTile t : tiles) {
           // flightBoard.getBank().addBattery(-t.getTileBattCapacity());
            t.RechargeAll(); //qui serve aggiungere in banca ? sto refillando tutte le batt
        }
    }

    /**
     * Validates if the total number of people to be removed from the ship matches the provided count,
     * based on the given list of triplets defining removal details. Each triplet contains coordinates
     * and the count of people to be removed from a specific tile in the spaceship.
     *
     * @param triplets A list of triplets where each triplet is an ArrayList containing three integers:
     *                 the x-coordinate, the y-coordinate, and the count of people to be removed from
     *                 the specified tile.
     * @param tot      The total number of people that need to be removed from the spaceship.
     * @return {@code true} if the total number of removals across the specified tiles matches {@code tot},
     *         and all removal requests are valid according to the spaceship's occupancy constraints;
     *         {@code false} otherwise.
     * @throws RuntimeException if any unexpected error or condition occurs during validation.
     */
    public boolean CheckCorrectPeopleToLoose(
            ArrayList<ArrayList<Integer>> triplets,
            int tot
    ) throws RuntimeException {
        System.out.println("[DEBUG] Entering CheckCorrectPeopleToLoose: tot=" + tot + ", triplets=" + triplets);
        if (triplets == null) {
            System.out.println("[DEBUG] triplets è null");
            return false;
        }

        // 1) Raggruppo per coordinata (x,y) e sommo i count
        Map<String, Integer> sumPerTile = new HashMap<>();
        for (ArrayList<Integer> entry : triplets) {
            if (entry.size() != 3) {
                System.out.println("[DEBUG] entry.size() != 3 (size=" + entry.size() + ")");
                return false;
            }
            int x = entry.get(0), y = entry.get(1), count = entry.get(2);
            if (count <= 0) {
                System.out.println("[DEBUG] count non positivo: " + count);
                return false;
            }
            if (x < 0 || x >= ShipMatrix.length || y < 0 || y >= ShipMatrix[0].length) {
                System.out.println("[DEBUG] coordinate fuori range: (" + x + "," + y + ")");
                return false;
            }

            String key = x + "," + y;
            sumPerTile.put(key, sumPerTile.getOrDefault(key, 0) + count);
        }

        // 2) Per ogni tile distinta, controllo che la somma non superi gli occupanti
        int totalRemoved = 0;
        for (Map.Entry<String, Integer> e : sumPerTile.entrySet()) {
            String[] parts = e.getKey().split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int requested = e.getValue();

            SpaceShipTile tile = ShipMatrix[x][y];
            System.out.println("[DEBUG] tile in (" + x + "," + y + ") = " + tile.getType());
            if (tile.getType() != SSTTypes.Tile_Cabin) {
                System.out.println("[DEBUG] tile non è una cabina");
                return false;
            }

            int available = tile.getNumPassengers() + (tile.getIsThereAlien() ? 1 : 0);
            System.out.println("[DEBUG] occupanti disponibili in (" + x + "," + y + ") = " + available);
            if (requested > available) {
                System.out.println("[DEBUG] requested (" + requested + ") > available (" + available + ")");
                return false;
            }

            totalRemoved += requested;
            System.out.println("[DEBUG] totalRemoved parziale = " + totalRemoved);
        }

        // 3) Controllo che la somma di tutte le rimozioni sia proprio 'tot'
        System.out.println("[DEBUG] totalRemoved finale = " + totalRemoved + ", tot richiesto = " + tot);
        boolean result = (totalRemoved == tot);
        System.out.println("[DEBUG] risultato totale = " + result);
        return result;
    }

    /**
     * Retrieves and returns a list of all goods present in the special cargo holds
     * and regular cargo holds of the spaceship, sorted in descending order of their value.
     * The method updates the spaceship's attributes before iterating through these tiles.
     *
     * @return an ArrayList of Goods objects sorted in descending order of value
     * @throws RuntimeException if there is an issue updating attributes or accessing goods
     */
    public ArrayList<Goods> getMySortedGoods() throws RuntimeException {
        updateAttributes();

        ArrayList<Goods> beni = new ArrayList<>();

        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_SpecialCargoHold)) {
            beni.addAll(t.getEffectivePresentGoods());
        }
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_CargoHold)) {
            beni.addAll(t.getEffectivePresentGoods());
        }
        beni.sort(BY_VALUE_DESC);
        return beni;
    }

    /**
     * Sorts a given list of Goods objects in descending order based on their value.
     * The sorting is performed using the predefined comparator BY_VALUE_DESC.
     * If the input list is null, the method does nothing and returns null.
     *
     * @param goods an ArrayList of Goods objects to be sorted. It may be null.
     * @return the same ArrayList of Goods, sorted in descending order based on their value,
     *         or null if the input list is null.
     * @throws RuntimeException if an error occurs during the sorting process.
     */
    public ArrayList<Goods> SortGoods(ArrayList<Goods> goods) throws RuntimeException {
        if (goods != null) {
            goods.sort(BY_VALUE_DESC);
        }
        return goods;
    }

    /**
     * Modifies the current list of goods by either removing a specified number of goods in order of value
     * or adding a provided list of goods while maintaining descending order by value.
     *
     * @param goodsToAdd the list of goods to add; if null or empty, no goods will be added
     * @param goodsToLose the number of goods to remove; the number of goods actually removed
     *                    will not exceed the size of the current list of goods
     * @return a new list of goods after applying the additions or removals based on the input parameters
     */
    // Fix for looseOrAddGoods method - using safe iteration
    public ArrayList<Goods> looseOrAddGoods(ArrayList<Goods> goodsToAdd, int goodsToLose) {
        // Create a defensive copy of the sorted goods list
        ArrayList<Goods> beni = new ArrayList<>(getMySortedGoods());

        /* --- LOSING GOODS --- */
        if (goodsToLose > 0) {
            // Pre-calculate the goods to remove to avoid modification during iteration
            ArrayList<Goods> goodsToRemove = new ArrayList<>();
            int actualGoodsToLose = Math.min(goodsToLose, beni.size());

            for (int i = 0; i < actualGoodsToLose; i++) {
                goodsToRemove.add(beni.get(i));
            }

            // Now safely remove them from the main list
            for (Goods g : goodsToRemove) {
                beni.remove(g);
                flightBoard.getBank().addGood(g);
            }

            return beni;
        }
        if (goodsToAdd != null && !goodsToAdd.isEmpty()) {
            beni.addAll(goodsToAdd);
            beni.sort(BY_VALUE_DESC); // Maintain descending order
        }

        return beni;
    }

    /**
     * Calculates the total number of exposed connectors on a spaceship board.
     * An exposed connector is counted if it is adjacent to a tile of type
     * Tile_NonAccesiblePlace or is on the outer edge and not covered by a smooth side.
     *
     * This method iterates through all tiles of the spaceship board and checks
     * each connector to determine if it is exposed based on the tile's position
     * and its neighboring tiles' types.
     *
     * @return the total number of exposed connectors on the spaceship board.
     * @throws RuntimeException if an unexpected condition occurs during the calculation.
     */
    public int calculateExposedConnectors() throws RuntimeException {
        int exposed = 0;
        ArrayList<SpaceShipTile> alltiles = ShipBoardIterable.Alltiles();
        for (SpaceShipTile t : alltiles) {
            int x = Objects.requireNonNull(ShipBoard_iterable.getTilePosition(t)).getFirst();
            int y = Objects.requireNonNull(ShipBoard_iterable.getTilePosition(t)).get(1);

            for (int i = 0; i < 4; i++) {
                Type_side_connector conn = t.getConnector(i);
                if (conn != Type_side_connector.SMOOTH_SIDE) {

                    if (x < 4 && x > 0 && y > 0 && y < 6) {
                        if (i == 0) {
                            SpaceShipTile adiac = ShipMatrix[x - 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 1) {
                            SpaceShipTile adiac = ShipMatrix[x][y + 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 2) {
                            SpaceShipTile adiac = ShipMatrix[x + 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 3) {
                            SpaceShipTile adiac = ShipMatrix[x][y - 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                    } else if (x == 0 && y > 0 && y < 6) {//se x ==0 non guardo sopra
                        if (i == 0) if (t.getConnector(0) != Type_side_connector.SMOOTH_SIDE) {
                            exposed++;
                        }
                        if (i == 1) {
                            SpaceShipTile adiac = ShipMatrix[x][y + 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 2) {
                            SpaceShipTile adiac = ShipMatrix[x + 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 3) {
                            SpaceShipTile adiac = ShipMatrix[x][y - 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                    } else if (x == 4 && y > 0 && y < 6) {
                        if (i == 2) if (t.getConnector(2) != Type_side_connector.SMOOTH_SIDE) {
                            exposed++;
                        }
                        if (i == 0) {
                            SpaceShipTile adiac = ShipMatrix[x - 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 1) {
                            SpaceShipTile adiac = ShipMatrix[x][y + 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 3) {
                            SpaceShipTile adiac = ShipMatrix[x][y - 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                    } else if (y == 0 && x > 0 && x < 4) {
                        if (i == 3) if (t.getConnector(3) != Type_side_connector.SMOOTH_SIDE) {
                            exposed++;
                        }
                        if (i == 0) {
                            SpaceShipTile adiac = ShipMatrix[x - 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 1) {
                            SpaceShipTile adiac = ShipMatrix[x][y + 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 2) {
                            SpaceShipTile adiac = ShipMatrix[x + 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }

                    } else if (y == 6 && x > 0 && x < 4) {
                        if (i == 1) {
                            if (t.getConnector(1) != Type_side_connector.SMOOTH_SIDE) {
                                exposed++;
                            }
                        }
                        if (i == 0) {
                            SpaceShipTile adiac = ShipMatrix[x - 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 2) {

                            SpaceShipTile adiac = ShipMatrix[x + 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 3) {
                            SpaceShipTile adiac = ShipMatrix[x][y - 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                    } else if (x == 4 && y == 0) {//primo angolo in basso a sx
                        if (i == 0) {
                            SpaceShipTile adiac = ShipMatrix[x - 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 1) {
                            SpaceShipTile adiac = ShipMatrix[x][y + 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 3) if (t.getConnector(3) != Type_side_connector.SMOOTH_SIDE) {
                            exposed++;
                        }
                        if (i == 2) if (t.getConnector(2) != Type_side_connector.SMOOTH_SIDE) {
                            exposed++;
                        }
                    } else if (x == 4 && y == 6) {//angolo in basso a dx
                        if (i == 0) {
                            SpaceShipTile adiac = ShipMatrix[x - 1][y];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 3) {
                            SpaceShipTile adiac = ShipMatrix[x][y - 1];
                            if (adiac.getType() == SSTTypes.Tile_NonAccesiblePlace) {
                                exposed++;
                            }
                        }
                        if (i == 2) if (t.getConnector(2) != Type_side_connector.SMOOTH_SIDE) {
                            exposed++;
                        }
                        if (i == 1) if (t.getConnector(1) != Type_side_connector.SMOOTH_SIDE) {
                            exposed++;
                        }
                    }
                }
            }
        }

        return exposed;
    }

    /**
     * Retrieves the current count of lost pieces associated with the ship.
     *
     * @return the number of lost pieces as an integer.
     */
    public int getLostPieces() {
        return LostPieces;
    }

    /**
     * Removes a tile from the ship at the specified row and column.
     * This method might throw exceptions in case of invalid operations.
     *
     * @param row the row index where the tile is located to be removed
     * @param column the column index where the tile is located to be removed
     * @param FromMistake specifies if the removal was a result of a mistake
     * @throws Exception if the removal operation fails
     */
    public void removeTile(int row, int column, boolean FromMistake) throws Exception {
        throw new RuntimeException("Not supported yet.");
    }

//    private boolean hasAdjacentCabin(int i, int j) throws RuntimeException {
//        List<int[]> matchingPositions = Stream.of(
//                        new int[]{i - 1, j}, new int[]{i + 1, j},
//                        new int[]{i, j - 1}, new int[]{i, j + 1}
//                )
//                .filter(pos -> pos[0] >= 0 && pos[0] < ShipMatrix.length && pos[1] >= 0 && pos[1] < ShipMatrix[0].length)
//                .filter(pos -> ShipMatrix[pos[0]][pos[1]].getType() == SSTTypes.Tile_Cabin)
//                .filter(pos -> ShipMatrix[pos[0]][pos[1]].getNumPassengers() > 0 || ShipMatrix[pos[0]][pos[1]].getIsThereAlien())
//                .toList();
//
//        return !matchingPositions.isEmpty();
//    }

    /**
     * Processes a received shot on the spaceship by removing the corresponding tile
     * if it is not blocked by a cannon or shield and updating ship attributes accordingly.
     *
     * @param tile the {@code SpaceShipTile} instance representing the tile which is hit.
     *             It must not be {@code null}.
     * @throws Exception if an issue occurs while processing the shot, such as retrieving
     *                   the tile's position or removing the tile.
     */
    public void receiveShot(SpaceShipTile tile) throws Exception {
        //togli dopo che mi hanno già effettivamente colpito, quindi non ho bloccato il colpo con un cannone o con uno scudo
        if (tile != null) {
            int row = Objects.requireNonNull(ShipBoard_iterable.getTilePosition(tile)).getFirst();
            int col = Objects.requireNonNull(ShipBoard_iterable.getTilePosition(tile)).get(1);
            removeTile(row, col, false);
            updateAttributes();
        }

    }

    /**
     * Validates if the provided defence configuration is correct based on the game logic.
     *
     * @param values an ArrayList of integers representing the coordinates and conditions
     *               for the defence validation. The list is expected to contain exactly 4 integers:
     *               - values.get(0) and values.get(1): coordinates of the defence tile
     *               - values.get(2) and values.get(3): coordinates of the battery component tile
     *               Values should be within permissible ranges and represent valid game tiles.
     * @return true if the defence configuration is valid, false otherwise.
     * @throws RuntimeException if any unexpected error occurs during the validation process.
     */
    public boolean checkCorrectDefence(ArrayList<Integer> values) throws RuntimeException {
        if (values == null || values.isEmpty()) {
            return true;
        }
        if(values.size()!=4) return false;
        if (values.get(0)<0 || values.get(1)<0 || values.get(2)<0 || values.get(3)<0) {
            return false;
        }
        if (values.get(0)>4 || values.get(1)>6 || values.get(2)>4|| values.get(3)>6) {
            return false;
        }
        SpaceShipTile tile1 = getShipMatrix()[values.get(0)][values.get(1)];
        SpaceShipTile batteryComponent = getShipMatrix()[values.get(2)][values.get(3)];
        if (tile1.getType() != SSTTypes.Tile_Double_Cannon && tile1.getType() != SSTTypes.Tile_ShieldGenerator) {
            return false;
        }
        if (batteryComponent.getType() != SSTTypes.Tile_BatteryComponent) {
            return false;
        }
        return batteryComponent.getNumCharges() != 0;
    }

    /**
     * Manages the process of handling a shot received by the ship. Based on the type
     * of shot, it delegates to the appropriate handling method.
     *
     * @param shot the shot object containing information about the type of shot to be handled
     * @param values an ArrayList of integers representing values necessary for the calculation or logic involved in handling the shot
     * @param dice an integer representing the dice roll outcome that influences the behavior of the shot handling
     * @throws Exception if any error occurs during the handling process of the shot
     */
    public void manageShotReceived(Shot shot, ArrayList<Integer> values, int dice) throws Exception {
        if (shot.getType() == Shot.ShotType.Meteor) {
            manageShotMeteor(shot, values, dice);
        } else if (shot.getType() == Shot.ShotType.Cannon) {
            manageShotCannon(shot, values, dice);
        }
    }



    /**
     * Manages the interaction between a shot and potential meteor impacts on the spaceship tiles based on the shot's
     * rotation and dice roll. The method handles vertical and horizontal shots in various directions and performs actions
     * like consuming battery charges and destroying tiles, depending on the scenario.
     *
     * @param shot The shot object containing information about the shot, including its rotation.
     * @param values A list of integer values that may provide additional parameters, such as tile coordinates.
     * @param dice An integer representing the dice roll associated with the shot.
     * @throws Exception If there is an issue while processing the shot or interacting with spaceship tiles.
     */
    private void manageShotMeteor(Shot shot,
                                  ArrayList<Integer> values,
                                  int dice) throws Exception {

        SpaceShipTile batteryComponent;   // inizializzato qui perché serve in tutti i rami

        if (values != null && !values.isEmpty()) {
            batteryComponent = getShipMatrix()[values.get(2)][values.get(3)];
            batteryComponent.removeCharge(1);
        }

        if (shot.getRotation() == 0 || shot.getRotation() == 180) {
            if (dice < 4 || dice > 10) {
                return;
            }
            dice -= 4;

            if (shot.getRotation() == 180) {
                int i;
                for (i = 0; i < 5; i++) {
                    if (getShipMatrix()[i][dice].getType() != SSTTypes.Tile_NonAccesiblePlace) break;
                }
                if (i >= 5) {
                    return;
                }

                SpaceShipTile tileToRemove = getShipMatrix()[i][dice];
                handleVerticalShot(shot, values, tileToRemove, /*fromTop=*/true);
            } else {  // rotation == 0 (meteorite dal basso verso l'alto)
                int i;
                for (i = 4; i >= 0; i--) {
                    if (getShipMatrix()[i][dice].getType() != SSTTypes.Tile_NonAccesiblePlace) break;
                }
                if (i < 0) {
                    return;
                }

                SpaceShipTile tileToRemove = getShipMatrix()[i][dice];
                handleVerticalShot(shot, values, tileToRemove, /*fromTop=*/false);
            }

        } else {
            if (dice < 5 || dice > 9) {
                return;
            }
            dice -= 5;

            if (shot.getRotation() == 90) { // meteorite da sinistra verso destra
                int i;
                for (i = 0; i < 7; i++) {
                    if (getShipMatrix()[dice][i].getType() != SSTTypes.Tile_NonAccesiblePlace) break;
                }
                if (i >= 7) {
                    return;
                }

                SpaceShipTile tileToRemove = getShipMatrix()[dice][i];
                handleHorizontalShot(shot, values, tileToRemove, /*fromLeft=*/true);
            } else { // rotation == 270 (da destra verso sinistra)
                int i;
                for (i = 6; i >= 0; i--) {
                    if (getShipMatrix()[dice][i].getType() != SSTTypes.Tile_NonAccesiblePlace) break;
                }
                if (i < 0) {
                    return;
                }

                SpaceShipTile tileToRemove = getShipMatrix()[dice][i];
                handleHorizontalShot(shot, values, tileToRemove, /*fromLeft=*/false);
            }
        }
    }

    /**
     * Restituisce le coordinate {row, col} della tessera indicata.
     * @return null se non trovata (caso teorico)
     */
    private int[] findPosition(SpaceShipTile target) {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 7; c++) {
                if (getShipMatrix()[r][c] == target) {
                    return new int[] { r, c };
                }
            }
        }
        return null;
    }

    /**
     * Due indici sono adiacenti quando distano al massimo 1.
     */
    private boolean isAdjacent(int a, int b) {
        return Math.abs(a - b) <= 1;
    }

    /**
     * Cercare un cannone singolo che possa intercettare il meteorite.
     * @param horizontal true se il colpo è orizzontale, altrimenti verticale
     * @param rotationNeed rotazione richiesta al cannone per sparare al colpo
     */
    private boolean hasAdjacentSingleCannon(int targetRow,
                                            int targetCol,
                                            boolean horizontal,
                                            int rotationNeed) {
        if (horizontal) {
            for (int r = Math.max(0, targetRow - 1); r <= Math.min(4, targetRow + 1); r++) {
                for (int c = 0; c < 7; c++) {
                    SpaceShipTile t = getShipMatrix()[r][c];
                    if (t.getType() == SSTTypes.Tile_Cannon && t.getRotation() == rotationNeed) {
                        return true;
                    }
                }
            }
        } else {
            for (int c = Math.max(0, targetCol - 1); c <= Math.min(6, targetCol + 1); c++) {
                for (int r = 0; r < 5; r++) {
                    SpaceShipTile t = getShipMatrix()[r][c];
                    if (t.getType() == SSTTypes.Tile_Cannon && t.getRotation() == rotationNeed) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void handleVerticalShot(Shot shot,
                                    ArrayList<Integer> values,
                                    SpaceShipTile tileToRemove,
                                    boolean fromTop) throws Exception {
        int exposedSide = fromTop ? 0 : 2;
        int okRotation  = fromTop ? 0 : 180;
        int shieldRotA  = fromTop ? 0 : 180;
        int shieldRotB  = fromTop ? 270 : 90;

        int[] targetPos = findPosition(tileToRemove);
        int targetRow   = targetPos[0];
        int targetCol   = targetPos[1];

        if (values == null || values.isEmpty()) {
            if (shot.getIsBig()) {
                if (fromTop) {
                    if (!(tileToRemove.getType() == SSTTypes.Tile_Cannon && tileToRemove.getRotation() == okRotation)) {
                        receiveShot(tileToRemove);
                    }
                } else {
                    if (!hasAdjacentSingleCannon(targetRow, targetCol, /*horizontal=*/false, okRotation)) {
                        receiveShot(tileToRemove);
                    }
                }
            } else {
                if (tileToRemove.getConnector(exposedSide) != Type_side_connector.SMOOTH_SIDE) {
                    receiveShot(tileToRemove);
                }
            }
            return;
        }

        SpaceShipTile tile1 = getShipMatrix()[values.get(0)][values.get(1)];
        int[] chosenPos     = findPosition(tile1);

        if (shot.getIsBig()) {
            if (fromTop) {
                boolean ok = (tile1 == tileToRemove) &&
                        (tile1.getType() == SSTTypes.Tile_Double_Cannon || tile1.getType() == SSTTypes.Tile_Cannon) &&
                        tile1.getRotation() == okRotation;
                if (!ok) {
                    receiveShot(tileToRemove);
                }
            } else { // dal basso
                boolean ok = (tile1.getType() == SSTTypes.Tile_Double_Cannon || tile1.getType() == SSTTypes.Tile_Cannon) &&
                        tile1.getRotation() == okRotation &&
                        isAdjacent(chosenPos[1], targetCol);
                if (!ok) {
                    receiveShot(tileToRemove);
                }
            }
        } else { // meteorite piccolo
            if (tileToRemove.getConnector(exposedSide) != Type_side_connector.SMOOTH_SIDE) {
                boolean shieldOk = tile1.getType() == SSTTypes.Tile_ShieldGenerator &&
                        (tile1.getRotation() == shieldRotA || tile1.getRotation() == shieldRotB);
                if (!shieldOk) {
                    receiveShot(tileToRemove);
                }
            }
        }
    }

    private void handleHorizontalShot(Shot shot,
                                      ArrayList<Integer> values,
                                      SpaceShipTile tileToRemove,
                                      boolean fromLeft) throws Exception {
        int exposedSide = fromLeft ? 3 : 1;
        int okRotation  = fromLeft ? 270 : 90;
        int shieldRotA  = fromLeft ? 270 : 90;
        int shieldRotB  = fromLeft ? 180 : 0;

        int[] targetPos = findPosition(tileToRemove);
        int targetRow   = targetPos[0];
        int targetCol   = targetPos[1];

        if (values == null || values.isEmpty()) {
            if (shot.getIsBig()) {
                if (!hasAdjacentSingleCannon(targetRow, targetCol, /*horizontal=*/true, okRotation)) {
                    receiveShot(tileToRemove);
                }
            } else {
                if (tileToRemove.getConnector(exposedSide) != Type_side_connector.SMOOTH_SIDE) {
                    receiveShot(tileToRemove);
                }
            }
            return;
        }

        SpaceShipTile tile1 = getShipMatrix()[values.get(0)][values.get(1)];
        int[] chosenPos     = findPosition(tile1);

        if (shot.getIsBig()) {
            boolean ok = (tile1.getType() == SSTTypes.Tile_Double_Cannon || tile1.getType() == SSTTypes.Tile_Cannon) &&
                    tile1.getRotation() == okRotation &&
                    isAdjacent(chosenPos[0], targetRow);
            if (!ok) {
                receiveShot(tileToRemove);
            }
        } else {
            if (tileToRemove.getConnector(exposedSide) != Type_side_connector.SMOOTH_SIDE) {
                boolean shieldOk = tile1.getType() == SSTTypes.Tile_ShieldGenerator &&
                        (tile1.getRotation() == shieldRotA || tile1.getRotation() == shieldRotB);
                if (!shieldOk) {
                    receiveShot(tileToRemove);
                }
            }
        }
    }



    /**
     * Manages the logic for handling a cannon shot on the ship, considering the shot's trajectory, size, and impact
     * on various ship components while also adjusting the battery charge if necessary.
     *
     * @param shot The Shot object containing details about the shot, including its rotation and size.
     * @param values An ArrayList of integers representing specific coordinates and data related to the shot's impact on the ship.
     *               Can be null or empty if no specific coordinates are required.
     * @param dice An integer value representing additional shot trajectory details, which affects the logic for determining the impact location.
     * @throws Exception If an error occurs during the handling of the shot or any of its components.
     */
    private void manageShotCannon(Shot shot, ArrayList<Integer> values, int dice) throws Exception {

        SpaceShipTile batteryComponent;
        if (values != null && !values.isEmpty()) {
            batteryComponent = getShipMatrix()[values.get(2)][values.get(3)];
            batteryComponent.removeCharge(1);
        }

        if (shot.getRotation() == 0 || shot.getRotation() == 180) {
            if (dice < 4 || dice > 10) {
                return;
            }
            dice -= 4;

            if (shot.getRotation() == 180) {
                int i;
                for (i = 0; i < 5; i++) {
                    if (getShipMatrix()[i][dice].getType() != SSTTypes.Tile_NonAccesiblePlace) break;
                }
                if (i >= 5) return;

                SpaceShipTile tileToRemove = getShipMatrix()[i][dice];
                if (values == null || values.isEmpty()) {
                    receiveShot(tileToRemove);
                    return;
                }

                SpaceShipTile tile1 = getShipMatrix()[values.get(0)][values.get(1)];

                if (shot.getIsBig()) {
                    receiveShot(tileToRemove);
                } else {
                    if ((tile1.getType() != SSTTypes.Tile_ShieldGenerator) &&
                            tile1.getRotation() != 0 && tile1.getRotation() != 270) {
                        receiveShot(tileToRemove);
                    }
                }

            } else {
                int i;
                for (i = 4; i >= 0; i--) {
                    if (getShipMatrix()[i][dice].getType() != SSTTypes.Tile_NonAccesiblePlace) break;
                }
                if (i < 0) return;

                SpaceShipTile tileToRemove = getShipMatrix()[i][dice];
                if (values == null || values.isEmpty()) {
                    receiveShot(tileToRemove);
                    return;
                }

                SpaceShipTile tile1 = getShipMatrix()[values.get(0)][values.get(1)];

                if (shot.getIsBig()) {
                    receiveShot(tileToRemove);
                } else {
                    if ((tile1.getType() != SSTTypes.Tile_ShieldGenerator) &&
                            tile1.getRotation() != 180 && tile1.getRotation() != 90) {
                        receiveShot(tileToRemove);
                    }
                }
            }

        } else {
            if (dice < 5 || dice > 9) {
                return;
            }
            dice -= 5;

            if (shot.getRotation() == 90) {
                int i;
                for (i = 0; i < 7; i++) {
                    if (getShipMatrix()[dice][i].getType() != SSTTypes.Tile_NonAccesiblePlace) break;
                }
                if (i >= 7) return;

                SpaceShipTile tileToRemove = getShipMatrix()[dice][i];
                if (values == null || values.isEmpty()) {
                    receiveShot(tileToRemove);
                    return;
                }

                SpaceShipTile tile1 = getShipMatrix()[values.get(0)][values.get(1)];

                if (shot.getIsBig()) {
                    receiveShot(tileToRemove);
                } else {
                    if ((tile1.getType() != SSTTypes.Tile_ShieldGenerator) &&
                            tile1.getRotation() != 180 && tile1.getRotation() != 270) {
                        receiveShot(tileToRemove);
                    }
                }

            } else {
                int i;
                for (i = 6; i >= 0; i--) {
                    if (getShipMatrix()[dice][i].getType() != SSTTypes.Tile_NonAccesiblePlace) break;
                }
                if (i < 0) return;

                SpaceShipTile tileToRemove = getShipMatrix()[dice][i];
                if (values == null || values.isEmpty()) {
                    receiveShot(tileToRemove);
                    return;
                }

                SpaceShipTile tile1 = getShipMatrix()[values.get(0)][values.get(1)];

                if (shot.getIsBig()) {
                    receiveShot(tileToRemove);
                } else {
                    if ((tile1.getType() != SSTTypes.Tile_ShieldGenerator) &&
                            tile1.getRotation() != 0 && tile1.getRotation() != 90) {
                        receiveShot(tileToRemove);
                    }
                }
            }
        }
    }


    /**
     * Calculates and returns the total resource score for the ship.
     * The score is computed based on the weighted sum of the available goods:
     * - Red goods contribute 4 points each.
     * - Blue goods contribute 1 point each.
     * - Yellow goods contribute 3 points each.
     * - Green goods contribute 2 points each.
     * The method updates the attributes of the ship before computation.
     *
     * @return the total resource score as an integer.
     * @throws RuntimeException if resource attributes cannot be updated.
     */
    public int getTotalResourcesScore() throws RuntimeException {
        updateAttributes();
        return availableRedGoods * 4 + availableBlueGoods + availableYellowGoods * 3 + availableGreenGoods * 2;
    }

    /**
     *
     */
    public void removePassengers(ArrayList<ArrayList<Integer>> tiles, int numPass) throws Exception {
        SpaceShipTile[][] ship = getShipMatrix();
        for (ArrayList<Integer> t : tiles) {
            int positionx = t.get(0);
            int positiony = t.get(1);
            int passengerToRemove = t.get(2);
            SpaceShipTile batteryTile = ship[positionx][positiony];
            if (numPass < passengerToRemove) {
                batteryTile.removePassenger(numPass);
                break;
            } else {
                batteryTile.removePassenger(passengerToRemove);
                numPass -= passengerToRemove;
                if (numPass == 0) {
                    break;
                }
            }
        }


    }

    /**
     * Checks if a pair of connectors can fit or align with each other based on their type.
     *
     * A connector of type {@code SMOOTH_SIDE} is not compatible with any other connector
     * and always returns {@code false}. A connector of type {@code UNIV_connector} can
     * match any type of connector. Otherwise, the method checks if the two connectors
     * are of the same type.
     *
     * @param c1 the first connector of type {@code Type_side_connector}
     * @param c2 the second connector of type {@code Type_side_connector}
     * @return {@code true} if the connectors can match based on their type;
     *         {@code false} otherwise
     * @throws RuntimeException if an unexpected error occurs during the connector comparison
     */
    public boolean checkPairofConnectors(Type_side_connector c1, Type_side_connector c2) throws RuntimeException {
        if (c1 == Type_side_connector.SMOOTH_SIDE || c2 == Type_side_connector.SMOOTH_SIDE) {
            return false;
        }
        if (c1 == Type_side_connector.UNIV_connector || c2 == Type_side_connector.UNIV_connector) {
            return true;
        }
        return c1 == c2;
    }

    /**
     * Checks if two connectors are positioned in a valid configuration relative to each other.
     * Ensures the connectors adhere to rules regarding smooth sides, universal connectors,
     * and incompatible matches.
     *
     * @param c1 the first connector of type Type_side_connector
     * @param c2 the second connector of type Type_side_connector
     * @return true if the connectors are validly positioned, false otherwise
     */
    public boolean checkConnectorsPosition(Type_side_connector c1, Type_side_connector c2) {
        if (c1 == Type_side_connector.SMOOTH_SIDE && c2 == Type_side_connector.SMOOTH_SIDE) {
            return false;
        }
        if ((c1 == Type_side_connector.UNIV_connector && c2 != Type_side_connector.SMOOTH_SIDE) || (c2 == Type_side_connector.UNIV_connector && c1 != Type_side_connector.SMOOTH_SIDE)) {
            return false;
        }
        return c1 != c2;
    }

    /**
     * Calculates and returns the total number of available battery units on the ship.
     * The method iterates over all tiles of type "BatteryComponent" in the ship's board
     * and sums up their effective capacities.
     *
     * @return the total number of available battery units
     * @throws RuntimeException if an error occurs while retrieving battery information
     */
    public int getBatteriesNumber() throws RuntimeException {
        availableBatteryNumber = 0;
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_BatteryComponent))
            availableBatteryNumber += ShipBoardIterable.getTileEffCapacity(t);
        return availableBatteryNumber;
    }

    /**
     * Removes a block of SpaceShipTile objects from the flight board and updates the associated bank.
     * The method retrieves the position of each tile, processes it according to its type,
     * and replaces it with a placeholder tile. Additionally, it updates the lost pieces count.
     *
     * @param block the list of SpaceShipTile objects to be removed from the flight board
     * @return the total number of tiles removed
     * @throws RuntimeException if the tile position retrieval fails or a null position is encountered
     */
    public int removeBlock(ArrayList<SpaceShipTile> block) throws RuntimeException {
        int removedPieces = 0;
        for (SpaceShipTile tile : block) {
            ArrayList<Integer> xy = Objects.requireNonNull(ShipBoard_iterable.getTilePosition(tile));
            int row = xy.get(0);
            int col = xy.get(1);
            //ridò cose alla banca
            switch (tile.getType()) { //banca domanda : questo metodo è chiamato da removetile statepost choose che è chiamato da remove tile quindi remove tile è safe?
                case SSTTypes.Tile_BatteryComponent ->
                        flightBoard.getBank().addBattery(tile.getNumCharges()); //aggiungo alla banca le cariche sulla batteria
                case SSTTypes.Tile_CargoHold ->
                        flightBoard.getBank().addGoodsFromList(tile.getEffectivePresentGoods());//do good alla banca
            }
            ArrayList<Type_side_connector> connectors = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                connectors.add(Type_side_connector.SMOOTH_SIDE);
            }
            insertTile(new SpaceShipTile(-1, connectors, SSTTypes.Tile_NonAccesiblePlace), row, col);
            removedPieces++;
        }
        this.addLostPieces(removedPieces);
        return removedPieces;
    }

    /**
     * Validates the positions of cannon tiles and battery tiles on a player's spaceship, ensuring
     * that all specified positions correspond to the correct tile types and that there are enough
     * battery components to power the cannons.
     *
     * @param cannonPos a list of positions representing the cannon tiles; each position is an ArrayList
     *                  where the first element is the row index and the second element is the column index.
     * @param batteriesPos a list of positions representing the battery tiles; each position is an ArrayList
     *                     where the first element is the row index, the second element is the column index,
     *                     and the third value is the number of charges to allocate from the tile.
     * @param player the player whose spaceship matrix will be validated.
     * @return true if all cannon and battery positions are valid, the correct types, and with batteries providing
     *         sufficient power for all cannons; otherwise, false.
     * @throws Exception if there is an issue accessing the spaceship matrix or other data during validation.
     */
    public boolean checkCorrectCannonBatteries
            (ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos, Player
                    player) throws Exception {
        SpaceShipTile[][] tiles = player.getMyShip().getShipMatrix();
        int batteryCont = 0;
        int cannonCont = 0;
        if (cannonPos == null) return true;
        //controllo correttezza input. tutti e soli cannoni doppi e tutte e sole batterie. num batterie abbastanza per accendere i cannoni
        for (ArrayList<Integer> cannonPair : cannonPos) {
            int row = cannonPair.get(0);
            int column = cannonPair.get(1);
            SpaceShipTile singleTile = tiles[row][column];
            if (!(singleTile.getType() == SSTTypes.Tile_Double_Cannon)) {
                getFlightBoard().getMygame().getEventBus().wrongInput(player);
                return false;
            }
            cannonCont++;
        }
        for (ArrayList<Integer> batteryPair : batteriesPos) {
            int row = batteryPair.get(0);
            int column = batteryPair.get(1);
            int bat = batteryPair.get(2);
            SpaceShipTile singleTile = tiles[row][column];
            if (!(singleTile.getType() == SSTTypes.Tile_BatteryComponent)) {
                getFlightBoard().getMygame().getEventBus().wrongInput(player);
                return false;
            }
            if ((singleTile.getNumCharges() < bat)) {
                System.out.println("Wrong num of batt: bat: " + bat + ", numC: " + singleTile.getNumCharges());
                return false;//non ho abbastanza batterie nella singola tile
            }
            batteryCont += bat;
        }
        return cannonCont == batteryCont; //input scorretto
    }

    /**
     * Validates whether the given engine positions and battery positions are correctly configured
     * such that the number of engines is equal to the total number of available battery charges.
     * The method ensures that all provided engine and battery positions correspond to valid components
     * on the player's spaceship and checks that batteries have sufficient charges for use.
     *
     * @param motorPos a list of positions specifying the locations of engines. Each position is an ArrayList
     *                 containing row and column indices (in that order) of the engine tile.
     * @param batteriesPos a list of positions specifying the locations and assigned charges of batteries.
     *                     Each position is an ArrayList containing row, column indices, and the number of
     *                     charges being utilized from that battery (in that order).
     * @param player the Player object whose spaceship configuration is being validated.
     * @return true if the number of engines matches the total battery charges provided, and both the engines
     *         and batteries are valid components; false otherwise.
     * @throws RuntimeException if the spaceship tiles or configuration data cause unexpected errors during validation.
     */
    public boolean checkCorrectEngineBatteries(ArrayList<ArrayList<Integer>> motorPos, ArrayList<ArrayList<Integer>> batteriesPos, Player player) throws RuntimeException {
        SpaceShipTile[][] tiles = player.getMyShip().getShipMatrix();
        int batteryCont = 0;
        int motorCont = 0;

        // If there are no engines to check, return true (assuming no engines means no constraints)
        if (motorPos == null) return true;

        // Iterate over all provided engine positions
        for (ArrayList<Integer> motorPair : motorPos) {
            int row = motorPair.get(0);
            int column = motorPair.get(1);
            SpaceShipTile singleTile = tiles[row][column];

            // Check if the tile at the given position is a double engine; if not, return false
            if (!(singleTile.getType() == SSTTypes.Tile_Double_Engine)) return false;
            motorCont++; // Count the number of engines
        }

        // Iterate over all provided battery positions
        for (ArrayList<Integer> batteryPair : batteriesPos) {
            int row = batteryPair.get(0);
            int column = batteryPair.get(1);
            int bat = batteryPair.get(2); // Number of charges assigned from this battery
            SpaceShipTile singleTile = tiles[row][column];

            // Check if the tile at the given position is a battery component; if not, return false
            if (!(singleTile.getType() == SSTTypes.Tile_BatteryComponent)) return false;

            // Check if the battery has enough charges to provide the requested amount
            if (singleTile.getNumCharges() < bat) return false; // Not enough available charges

            batteryCont += bat; // Accumulate total available battery charges
        }

        // The input is valid if the number of engines does not exceed the available battery charges
        return motorCont == batteryCont;
    }

    /**
     * Calculates the total firepower of the ship based on its cannon and battery positions,
     * as well as the current state of the player.
     *
     * @param cannonPos a list of lists representing the positions of the cannons on the ship
     * @param batteriesPos a list of lists representing the positions of the batteries on the ship
     * @param player the player whose ship's firepower is being calculated
     * @return the total firepower value as a floating-point number
     */
    public abstract float getTotalFirePower
            (ArrayList<ArrayList<Integer>> cannonPos, ArrayList<ArrayList<Integer>> batteriesPos, Player player);

    /**
     * Calculates the total motion power of the ship using the specified positions
     * of engines and batteries in conjunction with the player's attributes.
     *
     * @param enginePos a list of lists representing the positions of the engines on the shipboard
     * @param batteriesPos a list of lists representing the positions of the batteries on the shipboard
     * @param player the Player object associated with the ship, whose attributes might impact the motion power
     * @return the calculated total motion power of the ship
     */
    public abstract int getTotalMotionPower(ArrayList<ArrayList<Integer>> enginePos, ArrayList<ArrayList<Integer>> batteriesPos, Player player);

    /**
     *
     */
    public int getBasicMotionPower() throws RuntimeException {
        int totalMotionPower = 0;
        for (SpaceShipTile _ : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Engine)) {
            totalMotionPower += 1;
        }
        return totalMotionPower;
    }

    /**
     * Calculates the basic firepower of the spaceship based on the cannon tiles present on the shipboard.
     * Each cannon tile contributes to the total firepower depending on its orientation:
     * - If the cannon tile's rotation is 0, it provides 1 firepower.
     * - For other rotations, it provides 0.5 firepower.
     *
     * @return the total basic firepower of the spaceship as a floating-point number.
     * @throws RuntimeException if there is an error accessing or processing the cannon tiles.
     */
    public float getBasicFirePower() throws RuntimeException {
        float totalFirePower = 0;
        for (SpaceShipTile tile : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Cannon)) {
            if (tile.getRotation() == 0) {
                totalFirePower++;
            } else totalFirePower += 0.5F;
        }
        return totalFirePower;
    }


    /**
     * Checks if the goods input into the storage tiles is valid and feasible based on the constraints
     * and the current state of the spaceship tiles and card goods.
     *
     * @param storagetiles the list of coordinates (row and column) for the storage tiles where goods are to be placed
     * @param newgoods the corresponding list of goods to be placed in the storage tiles
     * @param cardgoods the list of goods obtained from cards
     * @return true if the goods input is valid and meets the constraints, false otherwise
     * @throws Exception if an error occurs during the process
     */
    public boolean checkGoodsInput
            (ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods, ArrayList<Goods> cardgoods) throws Exception {
        if (storagetiles == null) return true;
        if (storagetiles.size() != newgoods.size()) {
            return false;
        }
        SpaceShipTile[][] board = getShipMatrix();
        for (int i = 0; i < storagetiles.size(); i++) {
            int row = storagetiles.get(i).get(0);
            int column = storagetiles.get(i).get(1);
            SpaceShipTile singleTile = board[row][column];
            if (singleTile.getType() != SSTTypes.Tile_CargoHold && singleTile.getType() != SSTTypes.Tile_SpecialCargoHold) {
                return false;
            }
            if (newgoods.get(i).size() > singleTile.getCapacity()) {
                return false;
            }
            if (singleTile.getType() == SSTTypes.Tile_CargoHold &&
                    newgoods.get(i).stream().anyMatch(g -> g.getValue() == 4 && g.isRadioactive())) {
                return false;
            }
        }

        // Collect all goods from the board and card
        List<Goods> boardGoods = Arrays.stream(board)
                .flatMap(Arrays::stream)
                .filter(tile -> tile.getType() == SSTTypes.Tile_CargoHold || tile.getType() == SSTTypes.Tile_SpecialCargoHold)
                .flatMap(tile -> tile.getEffectivePresentGoods().stream())
                .collect(Collectors.toList());
        boardGoods.addAll(cardgoods);

        // Collect all the proposed new goods
        List<Goods> modifiedGoods = newgoods.stream()
                .flatMap(List::stream)
                .toList();



        // Create a copy of boardGoods that we can modify
        List<Goods> remainingBoardGoods = new ArrayList<>(boardGoods);

        // Check each modified good to see if it can be found in and removed from remainingBoardGoods
        for (Goods good : modifiedGoods) {
            boolean found = false;

            // Find and remove one matching good
            for (int i = 0; i < remainingBoardGoods.size(); i++) {
                if (good.equals(remainingBoardGoods.get(i))) {
                    remainingBoardGoods.remove(i);
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false; // This good is not available
            }
        }

        return true;
    }

    /**
     * Validates if the distribution of batteries across specific ship tiles is correct
     * by checking whether the total quantity matches the expected total and that
     * proper conditions are met for each tile.
     *
     * @param BattxPos A list of entries where each entry represents a battery distribution.
     *                 Each entry must contain exactly three integers:
     *                 the x-coordinate, the y-coordinate, and the quantity of batteries allocated.
     * @param quantTot The expected total quantity of batteries that should be allocated across all entries.
     * @return true if the distribution is valid and meets all checks; false otherwise.
     */
    public boolean checkCorrectBatteriesDistribution(ArrayList<ArrayList<Integer>> BattxPos, int quantTot) {
        System.out.println("[DEBUG] checkCorrectBatteriesDistribution called. Expecting total = " + quantTot
                + ", entries = " + (BattxPos != null ? BattxPos.size() : 0));
        if (BattxPos == null) {
            System.err.println("[ERROR] BattxPos list is null.");
            return false;
        }
        int tot = 0;
        for (int idx = 0; idx < BattxPos.size(); idx++) {
            ArrayList<Integer> entry = BattxPos.get(idx);
            if (entry.size() < 3) {
                System.err.println("[ERROR] Entry " + idx + " invalid size: " + entry);
                return false;
            }
            int x = entry.get(0);
            int y = entry.get(1);
            int qty = entry.get(2);
            System.out.printf("[DEBUG] Processing entry %d -> (%d,%d), qty=%d%n", idx, x, y, qty);
            // Bounds check
            if (x < 0 || y < 0 || x >= ShipMatrix.length || y >= ShipMatrix[0].length) {
                System.err.printf("[ERROR] Out of bounds coordinates at entry %d: (%d,%d)%n", idx, x, y);
                return false;
            }
            SpaceShipTile t = ShipMatrix[x][y];
            System.out.printf("[DEBUG] Tile at (%d,%d): type=%s, availableCharges=%d%n",
                    x, y, t.getType(), t.getNumCharges());
            if (t.getType() != SSTTypes.Tile_BatteryComponent) {
                System.err.printf("[ERROR] Wrong tile type at (%d,%d): %s%n", x, y, t.getType());
                return false;
            }
            if (t.getTileBattCapacity() < qty) {
                System.err.printf("[ERROR] Not enough charges at (%d,%d): available=%d, requested=%d%n",
                        x, y, t.getNumCharges(), qty);
                return false;
            }
            tot += qty;
        }
        System.out.printf("[DEBUG] Total requested sum = %d, expected = %d%n", tot, quantTot);
        boolean result = tot == quantTot;
        if (!result) {
            System.err.printf("[ERROR] Total mismatch: summed=%d, expected=%d%n", tot, quantTot);
        }
        return result;
    }

    /**
     * Updates the charge level of tiles in a spaceship matrix based on the specified battery positions and charges.
     *
     * @param BattxPos A 2D list where each inner list contains three integers:
     *                 the x-coordinate, y-coordinate, and charge value of a battery to be set.
     */
    public void assertBatteryPos(ArrayList<ArrayList<Integer>> BattxPos) {
        System.out.println("[DEBUG] assertBatteryPos called.");
        for (ArrayList<Integer> entry : BattxPos) {
            int x = entry.get(0);
            int y = entry.get(1);
            int qty = entry.get(2);
            System.out.printf("[DEBUG] Setting charge at (%d,%d) to %d%n", x, y, qty);
            SpaceShipTile t = ShipMatrix[x][y];
            t.setCharge(qty);
        }
    }

    /**
     * Updates the storage tiles of the spaceship with new goods, while also
     * synchronizing the changes with the bank inventory. Modifies the list
     * of goods present in the specified storage tiles and adjusts the bank
     * inventory based on the difference between the goods present before and
     * after the modification.
     *
     * @param storagetiles A two-dimensional list representing the positions
     *                     (row, column) of the spaceship's storage tiles to be updated.
     *                     Each inner list should contain exactly two integers, where
     *                     the first value represents the row and the second the column.
     * @param newgoods     A two-dimensional list containing the new goods to be placed
     *                     in the specified storage tiles. Each inner list contains the goods
     *                     that will replace the current goods in the corresponding storage tile.
     */
    public void addGoods(ArrayList<ArrayList<Integer>> storagetiles,
                         ArrayList<ArrayList<Goods>> newgoods) {

        if (storagetiles == null || newgoods == null || storagetiles.isEmpty())
            return;

        SpaceShipTile[][] board = getShipMatrix();

        // -------- snapshot "prima" con contatore --------------------------------
        Map<Goods, Long> beforeCount = Arrays.stream(board)
                .flatMap(Arrays::stream)
                .filter(t -> t.getType() == SSTTypes.Tile_CargoHold ||
                        t.getType() == SSTTypes.Tile_SpecialCargoHold)
                .flatMap(t -> t.getEffectivePresentGoods().stream())
                .collect(Collectors.groupingBy(g -> g, Collectors.counting()));

        // -------- applico le modifiche ------------------------------------------
        for (int i = 0; i < storagetiles.size(); i++) {
            int row = storagetiles.get(i).get(0);
            int col = storagetiles.get(i).get(1);
            SpaceShipTile tile = board[row][col];

            tile.getEffectivePresentGoods().clear();
            tile.getEffectivePresentGoods().addAll(newgoods.get(i));
        }

        // -------- snapshot "dopo" ------------------------------------------------
        Map<Goods, Long> afterCount = Arrays.stream(board)
                .flatMap(Arrays::stream)
                .filter(t -> t.getType() == SSTTypes.Tile_CargoHold ||
                        t.getType() == SSTTypes.Tile_SpecialCargoHold)
                .flatMap(t -> t.getEffectivePresentGoods().stream())
                .collect(Collectors.groupingBy(g -> g, Collectors.counting()));

        // -------- differenza e sincronizzazione Bank ----------------------------
        Bank bank = flightBoard.getBank();

        // Goods rimossi → tornano alla banca
        afterCount.forEach((good, qtyAfter) -> {
            long qtyBefore = beforeCount.getOrDefault(good, 0L);
            if (qtyBefore > qtyAfter) {
                for (long k = 0; k < qtyBefore - qtyAfter; k++) bank.addGood(good);
            }
        });

        // Goods aggiunti → prelevati dalla banca
        beforeCount.forEach((good, qtyBefore) -> {
            long qtyAfter = afterCount.getOrDefault(good, 0L);
            if (qtyAfter > qtyBefore) {
                for (long k = 0; k < qtyAfter - qtyBefore; k++) bank.useGood(good);
            }
        });
    }


    /**
     * Finds and returns a list of connected blocks of SpaceShipTile objects in the ship matrix.
     * A connected block is a group of adjacent tiles that are accessible and belong to the same logical block.
     * The method skips non-accessible tiles or null entries in the matrix.
     *
     * @return A list of connected blocks, where each block is represented as an ArrayList of SpaceShipTile objects.
     * @throws RuntimeException if an error occurs during the operation.
     */
    //data l'intera shipboard ne identifica i sottoblocchi connessi correttamente
    public ArrayList<ArrayList<SpaceShipTile>> findConnectedBlocks() throws RuntimeException {
        ArrayList<ArrayList<SpaceShipTile>> connectedBlocks = new ArrayList<>();
        boolean[][] visited = new boolean[5][7];

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 7; c++) {
                SpaceShipTile tile = getShipMatrix()[r][c];
                if (tile != null && tile.getType() != SSTTypes.Tile_NonAccesiblePlace && !visited[r][c]) {
                    ArrayList<SpaceShipTile> block = new ArrayList<>();
                    exploreBlock(r, c, visited, block);
                    connectedBlocks.add(block);
                }
            }
        }
        return connectedBlocks;
    }


    /**
     * Explores and identifies all interconnected tiles starting from a given tile (r, c)
     * in a matrix, using a recursive flood-fill approach.
     * Adds all valid connected tiles to the provided block list.
     *
     * @param r        The row index of the starting tile.
     * @param c        The column index of the starting tile.
     * @param visited  A 2D boolean array marking visited tiles to prevent revisiting.
     * @param block    A list to collect all interconnected valid tiles.
     * @throws RuntimeException  If an unexpected runtime issue occurs during execution.
     */
    //data una tile, si espande a macchia d'olio per identificare tutte le tiles interconnesse
    public void exploreBlock(int r, int c, boolean[][] visited, ArrayList<SpaceShipTile> block) throws RuntimeException {
        if (r < 0 || r >= 5 || c < 0 || c >= 7 || visited[r][c]) {
            return;
        }

        SpaceShipTile tile = getShipMatrix()[r][c];
        if (tile == null || tile.getType() == SSTTypes.Tile_NonAccesiblePlace) {
            return;
        }

        visited[r][c] = true;
        block.add(tile);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = r + dir[0];
            int newCol = c + dir[1];
            if (isConnected(tile, newRow, newCol)) {
                exploreBlock(newRow, newCol, visited, block);
            }
        }
    }


    /**
     * Determines if a given tile is connected to another tile at a specified position
     * on the shipboard grid based on tile properties and grid constraints.
     *
     * @param tile the current {@code SpaceShipTile} to be checked for connection.
     * @param newRow the row index of the target position to check.
     * @param newCol the column index of the target position to check.
     * @return {@code true} if the {@code tile} is connected to the tile at the specified
     *         position; {@code false} otherwise.
     * @throws RuntimeException if any unexpected error occurs during the connection check.
     */
    public boolean isConnected(SpaceShipTile tile, int newRow, int newCol) throws RuntimeException {
        if (newRow < 0 || newRow >= 5 || newCol < 0 || newCol >= 7) {
            return false;
        }
        SpaceShipTile adjacentTile = getShipMatrix()[newRow][newCol];
        if (adjacentTile == null || adjacentTile.getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            return false;
        }
        return checkAllConnectorsofTiles(tile, adjacentTile);
    }

    /**
     * Checks if all connectors between two given tiles of the spaceship are properly aligned
     * and compatible based on their relative positioning on the shipboard.
     *
     * @param tile1 the first tile to compare.
     * @param tile2 the second tile to compare.
     * @return true if all corresponding connectors of the two tiles are compatible or the tiles are non-accessible,
     *         otherwise returns false.
     * @throws RuntimeException if there is an issue obtaining the tile position or checking connectors.
     */
    public boolean checkAllConnectorsofTiles(SpaceShipTile tile1, SpaceShipTile tile2) throws RuntimeException {
        // Immediately handle non-accessible tiles
        if (tile1.getType().equals(SSTTypes.Tile_NonAccesiblePlace) ||
                tile2.getType().equals(SSTTypes.Tile_NonAccesiblePlace)) {
            return true;
        }

        // Get tile positions
        List<Integer> pos1 = ShipBoard_iterable.getTilePosition(tile1);
        List<Integer> pos2 = ShipBoard_iterable.getTilePosition(tile2);

        int y1 = pos1.get(0);
        int x1 = pos1.get(1);
        int y2 = pos2.get(0);
        int x2 = pos2.get(1);

        // Determine which connector to check based on relative positioning
        if (x1 < x2) {
            // tile1 is to the left of tile2
            return checkPairofConnectors(tile1.getConnector(1), tile2.getConnector(3));
        } else if (x1 > x2) {
            // tile1 is to the right of tile2
            return checkPairofConnectors(tile1.getConnector(3), tile2.getConnector(1));
        } else if (y1 < y2) {
            // tile1 is above tile2
            return checkPairofConnectors(tile1.getConnector(2), tile2.getConnector(0));
        } else if (y1 > y2) {
            // tile1 is below tile2
            return checkPairofConnectors(tile1.getConnector(0), tile2.getConnector(2));
        }

        // Fallback (tiles are in the same position)
        return true;
    }


    /**
     * Checks if the spaceship is completely filled, based on the presence of aliens and passengers in adjacent tiles of specified types.
     *
     * This method iterates through all tiles of type `SSTTypes.Tile_AlienLifeSupport` and verifies
     * if adjacent tiles of type `SSTTypes.Tile_Cabin` meet certain conditions, such as the absence
     * of aliens and passengers being equal to zero.
     *
     * @return true if the spaceship is completely filled based on the described conditions; false otherwise
     * @throws RuntimeException if an error occurs during the processing of tiles
     */
    public boolean isCompletelyFilled() throws RuntimeException {
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_AlienLifeSupport)) {
            ArrayList<Integer> pos = ShipBoard_iterable.getTilePosition(t);
            if (pos.get(1) + 1 < 7 && ShipMatrix[pos.get(0)][pos.get(1) + 1].getType() == SSTTypes.Tile_Cabin) {
                if ((!ShipMatrix[pos.get(0)][pos.get(1) + 1].getIsThereAlien()) && ShipMatrix[pos.get(0)][pos.get(1) + 1].getNumPassengers() == 0) {
                    return true;
                }
                //se non c'è un alieno e i passeggeri sono a zero ritorna zero. significa che deve essere finita di costruire
            }
            if (pos.get(1) - 1 >= 0 && ShipMatrix[pos.get(0)][pos.get(1) - 1].getType() == SSTTypes.Tile_Cabin) {
                if ((!ShipMatrix[pos.get(0)][pos.get(1) - 1].getIsThereAlien()) && ShipMatrix[pos.get(0)][pos.get(1) - 1].getNumPassengers() == 0) {
                    return true;
                }
            }
            if (pos.get(0) + 1 < 5 && ShipMatrix[pos.get(0) + 1][pos.get(1)].getType() == SSTTypes.Tile_Cabin) {
                if ((!ShipMatrix[pos.get(0) + 1][pos.get(1)].getIsThereAlien()) && ShipMatrix[pos.get(0) + 1][pos.get(1)].getNumPassengers() == 0) {
                    return true;
                }
            }
            if (pos.get(0) - 1 >= 0 && ShipMatrix[pos.get(0) - 1][pos.get(1)].getType() == SSTTypes.Tile_Cabin) {
                if ((!ShipMatrix[pos.get(0) - 1][pos.get(1)].getIsThereAlien()) && ShipMatrix[pos.get(0) - 1][pos.get(1)].getNumPassengers() == 0) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Sets the player associated with this ShipBoard instance.
     *
     * @param p1 the Player object to be assigned to this ShipBoard
     */
    public void setMyPlayer(Player p1) {
        this.myPlayer = p1;
    }

    /**
     * Adds passengers to spaceship cabin tiles under specific conditions.
     *
     * This method identifies lonely cabins on the spaceship and populates them with passengers.
     * A cabin is considered "lonely" if it is empty, has no adjacent support tiles with unique alien colors,
     * or if certain predefined conditions about its position are met.
     *
     * The method performs the following operations:
     * 1. Collects a set of alien colors already aboard the spaceship from aliens found in cabin tiles.
     * 2. Iterates over all cabin tiles and skips those already occupied by passengers or aliens.
     * 3. Identifies adjacent support tiles to the current cabin and gathers their alien colors that are not already aboard.
     * 4. Determines whether a cabin is lonely based on the absence of adjacent supports or the lack of free alien colors.
     * 5. Adds two passengers to any cabin identified as lonely or meeting specific position conditions.
     *
     * @throws Exception if an unexpected error occurs during execution.
     */
    public void addLonelyPassengers() throws Exception {
        Set<AlienColor> coloursAlreadyOnBoard = new HashSet<>();
        for (SpaceShipTile alienTile : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Cabin)) {
            if (alienTile.getIsThereAlien()) {
                coloursAlreadyOnBoard.add(alienTile.getAlienColor());
            }
            if (coloursAlreadyOnBoard.size() == 2) {
                break;
            }
        }
        for (SpaceShipTile cabin : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Cabin)) {
            if (cabin.getNumPassengers() > 0) {
                continue;
            }
            if (cabin.getIsThereAlien()) {
                continue;
            }
            List<Integer> pos = ShipBoard_iterable.getTilePosition(cabin);
            int row = pos.get(0);
            int col = pos.get(1);
            if (row == 2 && col == 3) {
                cabin.addPassenger();
                cabin.addPassenger();
                continue;
            }
            List<SpaceShipTile> adjacentSupports = new ArrayList<>();
            addSupportIfAny(adjacentSupports, row, col + 1); // destra
            addSupportIfAny(adjacentSupports, row, col - 1); // sinistra
            addSupportIfAny(adjacentSupports, row - 1, col); // sopra
            addSupportIfAny(adjacentSupports, row + 1, col); // sotto

            Set<AlienColor> freeColours = new HashSet<>();
            for (SpaceShipTile support : adjacentSupports) {
                AlienColor colour = support.getColor();
                if (!coloursAlreadyOnBoard.contains(colour)) {
                    freeColours.add(colour);
                }
            }

            boolean lonely =
                    adjacentSupports.isEmpty()
                            || freeColours.isEmpty();

            if (lonely) {
                cabin.addPassenger();
                cabin.addPassenger();
            }
        }
    }

    /**
     * Adds a SpaceShipTile to the provided list if a tile exists at the specified
     * row and column in the matrix and the tile type is Alien Life Support.
     *
     * @param list the list to which the Alien Life Support tile will be added if present
     * @param r the row index to check in the ShipMatrix
     * @param c the column index to check in the ShipMatrix
     */
    public void addSupportIfAny(List<SpaceShipTile> list, int r, int c) {
        if (r < 0 || r >= 5 || c < 0 || c >= 7) return;
        SpaceShipTile t = ShipMatrix[r][c];
        if (t != null && t.getType() == SSTTypes.Tile_AlienLifeSupport) {
            list.add(t);
        }
    }


    /**
     * Retrieves the first waiting tile of the spaceship.
     *
     * @return the first waiting SpaceShipTile instance.
     */
    public SpaceShipTile getWaitTile1() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Sets the first wait tile for the spaceship.
     *
     * @param waitTile1 the SpaceShipTile object to be set as the first wait tile
     */
    public void setWaitTile1(SpaceShipTile waitTile1) {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Retrieves the second wait tile of the spaceship.
     *
     * @return the second wait tile of the spaceship represented as a SpaceShipTile object
     */
    public SpaceShipTile getWaitTile2() {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Sets the specified SpaceShipTile as the secondary waiting tile.
     *
     * @param waitTile2 the SpaceShipTile to be set as the secondary waiting tile
     *                  for subsequent operations or configurations.
     */
    public void setWaitTile2(SpaceShipTile waitTile2) {
        throw new RuntimeException("Not supported yet.");
    }

    /**
     * Calculates the total number of human passengers across all cabin tiles in the spaceship.
     * Iterates through the spaceship's tile matrix, identifying tiles of type Tile_Cabin,
     * and sums up the number of human passengers in these tiles.
     *
     * @return the total number of human passengers in the spaceship.
     */
    public int calculateNumOfHumans() {
        int total = 0;
        for (SpaceShipTile[] shipMatrix : ShipMatrix) {
            for (SpaceShipTile tile : shipMatrix) {
                if (tile != null && tile.getType() == SSTTypes.Tile_Cabin) {
                    total += tile.getNumPassengers(); // Solo passeggeri umani
                }
            }
        }
        return total;
    }

    /**
     * Calculates and returns the total number of passengers, including any aliens present,
     * on all cabin tiles of the spaceship.
     * Iterates through each cabin tile, adds the number of passengers on the tile,
     * and includes an additional count if an alien is present on the tile.
     *
     * @return the total number of passengers and aliens across all cabin tiles
     */
    public int getTotalPassengers() {
        int total = 0;
        // cicla su tutte le cabine
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Cabin)) {
            // somma passeggeri + eventuale alieno
            total += t.getNumPassengers() + (t.getIsThereAlien() ? 1 : 0);
        }
        return total;
    }

    /**
     * Retrieves all passenger tiles on the spaceship along with their coordinates and
     * the total count of passengers or aliens present on each tile.
     * This method scans the spaceship to identify tiles of type 'Cabin'
     * that have at least one passenger or alien and gathers the required information.
     *
     * @return An ArrayList of ArrayLists where each inner ArrayList contains:
     *         - The row coordinate of the tile (index 0)
     *         - The column coordinate of the tile (index 1)
     *         - The total number of passengers and aliens on the tile (index 2)
     */
    public ArrayList<ArrayList<Integer>> getAllPassengerTiles() {
        ArrayList<ArrayList<Integer>> tiles = new ArrayList<>();
        for (SpaceShipTile t : ShipBoardIterable.getTilesOfType(SSTTypes.Tile_Cabin)) {
            int count = t.getNumPassengers() + (t.getIsThereAlien() ? 1 : 0);
            if (count > 0) {
                // prendo coordinate della tile
                List<Integer> pos = ShipBoard_iterable.getTilePosition(t);
                ArrayList<Integer> entry = new ArrayList<>();
                entry.add(pos.get(0));   // riga
                entry.add(pos.get(1));   // colonna
                entry.add(count);        // quanti da rimuovere se tutti
                tiles.add(entry);
            }
        }
        return tiles;
    }

    /**
     * Evaluates whether the current player should surrender based on the state of their ship.
     *
     * The method determines if the player has met the necessary conditions to continue the game.
     * It checks the following:
     * - If the player's ship contains any tiles of type `Tile_Cabin`.
     * - If the player's ship has a sufficient number of humans.
     *
     * If the ship has no `Tile_Cabin` tiles or the number of humans is zero or less, the method
     * will invoke the surrender action for the current player.
     *
     * @throws Exception if an unexpected error occurs during the surrender check process.
     */
    public void checkSurrender() throws Exception {
        if (getShipBoardIterable().getTilesOfType(SSTTypes.Tile_Cabin).isEmpty() || calculateNumOfHumans() <= 0) {
            getMyPlayer().Surrender();
        }
    }


}


