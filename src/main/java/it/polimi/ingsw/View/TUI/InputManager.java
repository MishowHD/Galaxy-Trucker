package it.polimi.ingsw.View.TUI;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.View.Utils_View.CommandType;
import it.polimi.ingsw.View.Utils_View.CommandTypeMapper;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.Model.Boards.ShipBoard;

public class InputManager {
    /**
     * Represents a static instance of a {@code GenericClient}.
     * This variable is used to reference a client object and facilitates interaction
     * between the client and the handling mechanism of the application.
     *
     * The {@code client} serves as the primary interface for managing client operations
     * and interactions within the system. It is accessible globally and is initialized
     * when necessary during the application's lifecycle.
     */
    public static GenericClient client;
    /**
     * Represents the current model associated with the InputManager.
     * The model is a static instance of the {@link LittleModelRepresentation} class,
     * shared across the InputManager to maintain consistent data representation.
     * This variable is protected, making it accessible to the same package and subclasses.
     */
    protected static LittleModelRepresentation model;
    /**
     * A protected static final list that contains the allowed method numbers for the current
     * application context. Each entry in the list is of type {@link CommandType}, representing
     * a valid command or action that can be executed within the system.
     *
     * This list can be updated or managed through dedicated methods to ensure the allowed
     * commands align with the current state or requirements of the application.
     *
     * It is initialized as an empty list and may be modified during runtime as per
     * application logic to include the relevant {@link CommandType} elements.
     */
    protected static final ArrayList<CommandType> allowedMethodNumbers = new ArrayList<>();
    /**
     * Represents a static integer variable used to track the creation of a game.
     *
     * This variable may function as a flag or counter within the system to indicate
     * whether a game has been created or to manage the state associated with game creation.
     *
     * It is protected and static, meaning it is shared across all instances of the
     * containing class and can only be accessed within the class itself, its subclasses,
     * or classes in the same package.
     */
    protected static int creategame = 0;
    /**
     * A static instance of the {@code Thread} class used to handle input operations
     * within the InputManager. This thread is responsible for managing specific
     * input-related tasks, allowing asynchronous input handling to occur independently
     * from the main program execution.
     */
    private static Thread inputThread;
    /**
     * Lock object used to synchronize access to input-related operations.
     * Ensures thread-safety by preventing concurrent modifications or
     * interactions with shared input resources.
     */
    private static final Object inputLock = new Object();
    /**
     * A volatile boolean flag used to manage and ensure thread-safe control
     * over the state or behavior of the InputManager. This variable is shared
     * among threads, and its volatile declaration ensures visibility of changes
     * across all threads.
     */
    private static volatile boolean ok = false;

    /**
     * Constructs an InputManager instance and initializes it with the given client.
     * The InputManager manages input operations and interacts with the client's model.
     *
     * @param client the {@code GenericClient} instance used to initialize the InputManager
     */
    public InputManager(GenericClient client) {
        InputManager.client = client;
        model = client.getview();
    }

    /**
     * Sets the LittleModelRepresentation instance for the InputManager class.
     *
     * @param model the LittleModelRepresentation object to be assigned to the InputManager
     */
    public void setModel(LittleModelRepresentation model) {
        InputManager.model = model;
    }

    /**
     * Updates the list of allowed method numbers with the provided list of commands.
     * This operation clears the current list and replaces it with the new list of commands
     * in a thread-safe manner.
     *
     * @param newMethodNumbers the new list of CommandType values to update the allowed method numbers with
     */
    public static void updateAllowedMethodNumbers(ArrayList<CommandType> newMethodNumbers) {
        synchronized (allowedMethodNumbers) {
            allowedMethodNumbers.clear();
            allowedMethodNumbers.addAll(newMethodNumbers);
        }
    }

    /**
     * Starts a new thread for InputManager processing if none is currently running.
     * This method updates the allowed method numbers with the provided list of commands
     * and ensures the InputManager thread is initialized and started.
     *
     * @param comandi an ArrayList of CommandType objects representing new allowed method commands
     */
    public static void startThreaded(ArrayList<CommandType> comandi) {
        updateAllowedMethodNumbers(comandi);
        if (inputThread == null || !inputThread.isAlive()) {
            ok = true;
            inputThread = new Thread(() -> {
                try {
                    InputManager.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "InputManager-Thread");
            inputThread.setDaemon(true);
            inputThread.start();
        }
    }

    /**
     * Stops the input processing thread and resets the input state.
     * This method sets the internal flag to false, indicating that
     * the input should no longer be processed. If there is an active
     * input thread, it interrupts the thread to halt its execution.
     */
    public static void stop() {
        ok = false;
        if (inputThread != null) {
            inputThread.interrupt();
        }
    }

    /**
     * Starts a synchronized loop for handling user input and managing actions based on
     * the provided input. This method supports various commands for controlling a game
     * state, creating games, joining games, activating timers, manipulating game tiles,
     * and other related game functionalities.
     *
     * The method listens for user input on the console, parses the input, processes it,
     * and invokes corresponding client functionalities depending on the command and its
     * parameters.
     *
     * Each input command is validated in terms of format and value ranges before
     * being processed. Feedback is provided to the user when inputs are invalid,
     * and appropriate actions are carried out for recognized and valid commands.
     *
     * Throws an exception in case an error occurs during input processing or
     * execution of commands.
     *
     * This method utilizes synchronization to ensure that input and corresponding
     * operations are thread-safe. It also dynamically interacts with the game model
     * and client based on the command type.
     *
     * Potential commands include but are not limited to:
     * - Creating a game
     * - Joining a game
     * - Activating a game timer
     * - Interacting with tiles (e.g., picking, placing, or flipping tiles)
     * - Viewing final scores
     * - Surrendering the game
     * - Activating effects
     *
     * The input is processed in a loop and the program terminates if specified
     * conditions are met.
     *
     * @throws Exception if an error occurs during command execution or input handling
     */
    public static void start() throws Exception {
        synchronized (inputLock) {
            Scanner scanner = new Scanner(System.in);
            while (ok) {
                String inputLine = scanner.nextLine();
                String[] tokens = inputLine.trim().split("\\s+");
                int methodNumber;

                try {
                    if (!ok) {
                        System.exit(0);
                    }
                    methodNumber = Integer.parseInt(tokens[0]);
                } catch (NumberFormatException e) {
                    System.out.println("First Value must be an integer");
                    continue;
                }

                if (!CommandTypeMapper.isAllowed(methodNumber, allowedMethodNumbers)) {
                    System.out.println("Invalid Method Number");
                    System.out.println("Allowed methods: " + CommandTypeMapper.mapToCodeList(allowedMethodNumbers));
                    continue;
                }

                int id = -1;
                List<String> arguments;
                ArrayList<ArrayList<Integer>> cannons, batteries, posGoods, passengers;
                ArrayList<ArrayList<Goods>> goodsSets;
                ArrayList<Integer> integerArrayList;
                boolean yOn, invalidInput, validCannons, validBatteries;
                int wronginput, i;
                String regexEmptyPairs = "^(\\(\\))$";
                String regexOnlyPairs = "^(\\(\\d+,\\d+\\))+$";
                String regexTriplets = "^(\\(\\d+,\\d+,\\d+\\))+$";
                String regexGoods = "^(\\(\\d+(,\\d+){0,2}\\))+$";

                switch (methodNumber) {
                    case 0:
                        if (tokens.length != 3) {
                            System.out.println("Not valid input");
                            break;
                        }
                        arguments = Arrays.asList(tokens).subList(1, tokens.length);
                        try {
                            int x = Integer.parseInt(arguments.get(0));
                            int y = Integer.parseInt(arguments.get(1));
                            if (x < 0 || x > 1 || y < 2 || y > 4) throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                            break;
                        }
                        client.createGame(Integer.parseInt(tokens[1]),
                                client.getUuid(),
                                Integer.parseInt(tokens[2]),
                                model.getMyNickname());
                        creategame = 1;
                        ok = false;
                        break;

                    case 1:
                        if (tokens.length != 2 || creategame != 0) {
                            System.out.println("Not valid input");
                            break;
                        }
                        try {
                            int idx = Integer.parseInt(tokens[1]);
                            if (idx < 0 || idx >= client.getGames().size()) throw new NumberFormatException();
                            UUID gameId = client.getGames().get(idx);
                            client.JoinGame(model.getMyNickname(),
                                    0,
                                    gameId,
                                    client.getUuid());
                            client.setUuid(gameId);
                            ok = false;
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                        }
                        break;

                    case 2:
                        if (tokens.length != 1) {
                            System.out.println("Not valid input");
                            break;
                        }
                        client.activateTimer(model.getMyNickname(), client.getUuid());
                        ok = false;
                        break;

                    case 3:
                        if (tokens.length != 2) {
                            System.out.println("Not valid input");
                            break;
                        }
                        try {
                            int flag = Integer.parseInt(tokens[1]);
                            if (flag != 0 && flag != 1) throw new NumberFormatException();
                            for (ShipBoard s : model.getShipBoards()) {
                                if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                    id = s.getMyPlayer().getPlayerId();
                                    break;
                                }
                            }
                            client.setNumTile(id, flag, client.getUuid());
                            ok = false;
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                        }
                        break;

                    case 4, 7, 10, 9, 27:
                        if (tokens.length != 1) {
                            System.out.println("Not valid input");
                            break;
                        }
                        for (ShipBoard s : model.getShipBoards()) {
                            if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                id = s.getMyPlayer().getPlayerId();
                                break;
                            }
                        }
                        switch (methodNumber) {
                            case 4:
                                client.Surrend(id, client.getUuid());
                                break;
                            case 7:
                                client.pickTileUnknown(id, client.getUuid());
                                break;
                            case 9:
                                client.depositLittleDeck(id, client.getUuid());
                                break;
                            case 10:
                                client.depositTile(id, client.getUuid());
                                break;
                            case 27:
                                client.addWaitTile(id, client.getUuid());
                                break;
                        }
                        ok = false;
                        break;
                    case 5, 13, 14:
                        if (tokens.length != 1) {
                            System.out.println("Not valid input");
                            break;
                        }
                        switch (methodNumber) {
                            case 5:
                                client.visualizeFinalScores(client.getUuid());
                                break;
                            case 13:
                                client.EffectActivation(client.getUuid());
                                break;
                            case 14:
                                client.getCardinuse(client.getUuid());
                                break;
                        }
                        ok = false;
                        break;

                    case 6:
                        if (tokens.length != 2) {
                            System.out.println("Not valid input");
                            break;
                        }
                        try {
                            int idx = Integer.parseInt(tokens[1]);
                            if (idx < 1 || idx > 156) throw new NumberFormatException();
                            for (ShipBoard s : model.getShipBoards()) {
                                if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                    id = s.getMyPlayer().getPlayerId();
                                    break;
                                }
                            }
                            client.pickTileAlreadyFlipped(idx, id, client.getUuid());
                            ok = false;
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                        }
                        break;

                    case 8:
                        if (tokens.length != 2) {
                            System.out.println("Not valid input");
                            break;
                        }
                        try {
                            int idx = Integer.parseInt(tokens[1]);
                            if (idx < 0 || idx > 2) throw new NumberFormatException();
                            for (ShipBoard s : model.getShipBoards()) {
                                if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                    id = s.getMyPlayer().getPlayerId();
                                    break;
                                }
                            }
                            client.pickLittleDeck(idx, id, client.getUuid());
                            ok = false;
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                        }
                        break;

                    case 11:
                        if (tokens.length != 2) {
                            System.out.println("Not valid input");
                            break;
                        }
                        arguments = Arrays.asList(tokens).subList(1, tokens.length);
                        try {
                            int pos = Integer.parseInt(arguments.getFirst());
                            if (pos < 1 || pos > 4) throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                            break;
                        }
                        for (ShipBoard s : model.getShipBoards()) {
                            if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                id = s.getMyPlayer().getPlayerId();
                                break;
                            }
                        }
                        client.endbuilding(id, Integer.parseInt(tokens[1]), client.getUuid());
                        ok = false;
                        break;

                    case 12:
                        if (tokens.length != 4) {
                            System.out.println("Not valid input");
                            break;
                        }
                        arguments = Arrays.asList(tokens).subList(1, tokens.length);
                        try {
                            int r = Integer.parseInt(arguments.get(0)) - 5;
                            int c = Integer.parseInt(arguments.get(1)) - 4;
                            int rot = Integer.parseInt(arguments.get(2));
                            if (r < 0 || r > 4 || c < 0 || c > 6 || rot % 90 != 0) throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                            break;
                        }
                        for (ShipBoard s : model.getShipBoards()) {
                            if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                id = s.getMyPlayer().getPlayerId();
                                break;
                            }
                        }
                        client.insertTile(Integer.parseInt(tokens[1]) - 5,
                                Integer.parseInt(tokens[2]) - 4,
                                id,
                                Integer.parseInt(tokens[3]), client.getUuid());

                        ok = false;
                        break;

                    case 15:
                        if (tokens.length != 2) {
                            System.out.println("Not valid input");
                            break;
                        }
                        try {
                            int idx = Integer.parseInt(tokens[1]);
                            if (idx < 0) throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                            break;
                        }
                        for (ShipBoard s : model.getShipBoards()) {
                            if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                id = s.getMyPlayer().getPlayerId();
                                break;
                            }
                        }
                        client.chooseOneSubShip(Integer.parseInt(tokens[1]), id, client.getUuid());
                        ok = false;
                        break;

                    case 16:
                        if (tokens.length != 2 && tokens.length != 3) {
                            System.out.println("Not valid input");
                            break;
                        }
                        yOn = "1".equals(tokens[1]);
                        if (!yOn) {
                            client.acceptToLandOnAPlanet(model.getMyNickname(), false, 0, client.getUuid());
                            ok = false;
                        } else {
                            try {
                                int planet = Integer.parseInt(tokens[2]);
                                if (planet < 0 || planet > 3) throw new NumberFormatException();
                                client.acceptToLandOnAPlanet(model.getMyNickname(), true, planet, client.getUuid());
                                ok = false;
                            } catch (NumberFormatException e) {
                                System.out.println("Not valid input");
                            }
                        }
                        break;
                    case 17, 22:
                        if (tokens.length != 2 && tokens.length != 4) {
                            System.out.println("Not valid input");
                            break;
                        }
                        yOn = "1".equals(tokens[1]);
                        if (!yOn) {
                            switch (methodNumber) {
                                case 17:
                                    client.chooseAbandonedStation(model.getMyNickname(), false, null, null, client.getUuid());
                                    break;
                                case 22:
                                    client.chooseToClaimReward(false, model.getMyNickname(), null, null, client.getUuid());
                                    break;
                            }
                            ok = false;
                        } else if (tokens.length == 2) {
                            switch (methodNumber) {
                                case 17:
                                    client.chooseAbandonedStation(model.getMyNickname(), true, null, null, client.getUuid());
                                    break;
                                case 22:
                                    client.chooseToClaimReward(true, model.getMyNickname(), null, null, client.getUuid());
                                    break;
                            }
                            ok = false;
                        } else {
                            posGoods = new ArrayList<>();
                            goodsSets = new ArrayList<>();
                            wronginput = 0;
                            invalidInput = false;

                            String posInput = tokens[2].replace("(", "").replace(")", ",");
                            String[] posParts = posInput.split(",");
                            for (i = 0; i < posParts.length; i += 2) {
                                if (posParts[i].isEmpty()) continue;
                                int r = Integer.parseInt(posParts[i]) - 5;
                                int c = Integer.parseInt(posParts[i + 1]) - 4;
                                if (r < 0 || r >= 5 || c < 0 || c >= 7) {
                                    wronginput = 1;
                                    break;
                                }
                                posGoods.add(new ArrayList<>(List.of(r, c)));
                            }

                            String goodsInput = tokens[3].replace("(", "").replace(")", ".");
                            String[] goodsParts = goodsInput.split("\\.");
                            for (String part : goodsParts) {
                                if (part.isEmpty()) continue;
                                ArrayList<Goods> goodsList = new ArrayList<>();
                                String[] goodsTokens = part.split(",");
                                for (String token : goodsTokens) {
                                    int type = Integer.parseInt(token);
                                    if (type < 0 || type > 4) {
                                        invalidInput = true;
                                        break;
                                    }
                                    goodsList.add(new Goods(type, type == 4));
                                }
                                if (invalidInput) break;
                                goodsSets.add(goodsList);
                            }

                            if (wronginput == 1 || invalidInput || posGoods.size() != goodsSets.size()) {
                                System.out.println("Not valid input");
                                break;
                            }
                            switch (methodNumber) {
                                case 17:
                                    client.chooseAbandonedStation(model.getMyNickname(), true, posGoods, goodsSets, client.getUuid());
                                    break;
                                case 22:
                                    client.chooseToClaimReward(true, model.getMyNickname(), posGoods, goodsSets, client.getUuid());
                                    break;
                            }
                            ok = false;
                        }
                        break;

                    case 18, 24, 25:
                        wronginput = 0;
                        if (tokens.length == 1) {
                            switch (methodNumber) {
                                case 18:
                                    client.chooseCannonBatteryPos(model.getMyNickname(), null, null, client.getUuid());
                                    break;
                                case 24:
                                    client.chooseToStartFirePower(model.getMyNickname(), null, null, client.getUuid());
                                    break;
                                case 25:
                                    client.chooseToStartMotor(model.getMyNickname(), null, null, client.getUuid());
                                    break;
                            }
                            ok = false;
                            break;
                        }
                        if (tokens.length != 3) {
                            System.out.println("Not valid input");
                            break;
                        }
                        cannons = new ArrayList<>();
                        batteries = new ArrayList<>();

                        validCannons = tokens[1].matches(regexOnlyPairs) || tokens[1].matches(regexEmptyPairs);
                        validBatteries = tokens[2].matches(regexTriplets) || tokens[2].matches(regexEmptyPairs);

                        if (!validCannons || !validBatteries) {
                            System.out.println("Not valid input");
                            break;
                        }

                        if (!tokens[1].equals("()")) {
                            String raw = tokens[1].substring(1, tokens[1].length() - 1);
                            String[] cannonCoords = raw.split("\\)\\(");
                            for (String coord : cannonCoords) {
                                String[] values = coord.split(",");
                                int r = Integer.parseInt(values[0]) - 5;
                                int c = Integer.parseInt(values[1]) - 4;
                                if (r < 0 || r >= 5 || c < 0 || c >= 7) {
                                    wronginput = 1;
                                    break;
                                }
                                cannons.add(new ArrayList<>(List.of(r, c)));
                            }
                        }

                        if (!tokens[2].equals("()")) {
                            String inner = tokens[2].substring(1, tokens[2].length() - 1);
                            String[] batteryCoords = inner.split("\\)\\(");
                            for (String coord : batteryCoords) {
                                String[] values = coord.split(",");
                                int r = Integer.parseInt(values[0]) - 5;
                                int c = Integer.parseInt(values[1]) - 4;
                                int count = Integer.parseInt(values[2]);
                                if (r < 0 || r >= 5 || c < 0 || c >= 7 || count < 1 || count > 3) {
                                    wronginput = 1;
                                    break;
                                }
                                batteries.add(new ArrayList<>(List.of(r, c, count)));
                            }
                        }

                        if (wronginput == 1) {
                            System.out.println("Not valid input");
                            break;
                        }
                        switch (methodNumber) {
                            case 18:
                                client.chooseCannonBatteryPos(model.getMyNickname(), cannons, batteries, client.getUuid());
                                break;
                            case 24:
                                client.chooseToStartFirePower(model.getMyNickname(), cannons, batteries, client.getUuid());
                                break;
                            case 25:
                                client.chooseToStartMotor(model.getMyNickname(), cannons, batteries, client.getUuid());
                                break;
                        }
                        ok = false;
                        break;

                    case 19:
                        if (tokens.length == 1) {
                            client.chooseHowToFaceMeteors(model.getMyNickname(), null, client.getUuid());
                            ok = false;
                            break;
                        }
                        if (tokens.length != 5) {
                            System.out.println("Not valid input");
                            break;
                        }
                        integerArrayList = new ArrayList<>();
                        try {
                            for (int j = 1; j < 5; j++) {
                                int val = Integer.parseInt(tokens[j]);
                                if (j % 2 == 1) {
                                    val -= 5;
                                    if (val < 0 || val > 6) {
                                        System.out.println("Not valid input");
                                        break;
                                    }
                                } else {
                                    val -= 4;
                                    if (val < 0 || val > 5) {
                                        System.out.println("Not valid input");
                                        break;
                                    }
                                }
                                integerArrayList.add(val);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                            break;
                        }
                        client.chooseHowToFaceMeteors(model.getMyNickname(), integerArrayList, client.getUuid());
                        ok = false;
                        break;

                    case 20:
                        wronginput = 0;
                        if (tokens.length == 1) {
                            client.choosePassengersToLose(model.getMyNickname(), false, null, client.getUuid());
                            ok = false;
                            break;
                        }
                        yOn = "1".equals(tokens[1]);
                        if (!yOn) {
                            client.choosePassengersToLose(model.getMyNickname(), false, null, client.getUuid());
                        } else {
                            if (tokens.length != 3) {
                                System.out.println("Not valid input");
                                break;
                            }
                            passengers = new ArrayList<>();
                            if (!tokens[2].matches(regexTriplets)) {
                                System.out.println("Not valid input");
                                break;
                            }
                            String inner = tokens[2].substring(1, tokens[2].length() - 1);
                            String[] passParts = inner.split("\\)\\(");
                            for (String part : passParts) {
                                String[] values = part.split(",");
                                int r = Integer.parseInt(values[0]) - 5;
                                int c = Integer.parseInt(values[1]) - 4;
                                int count = Integer.parseInt(values[2]);
                                if (r < 0 || r >= 5 || c < 0 || c >= 7 || count < 1 || count > 3) {
                                    wronginput = 1;
                                    break;
                                }
                                passengers.add(new ArrayList<>(List.of(r, c, count)));
                            }
                            if (wronginput == 1) {
                                System.out.println("Not valid input");
                                break;
                            }
                            client.choosePassengersToLose(model.getMyNickname(), true, passengers, client.getUuid());
                        }
                        ok = false;
                        break;

                    case 21:
                        if (tokens.length != 2) {
                            System.out.println("Not valid input");
                            break;
                        }
                        yOn = "1".equals(tokens[1]);
                        client.chooseToClaimReward(yOn, model.getMyNickname(), client.getUuid());
                        ok = false;
                        break;

                    case 23: {
                        if (tokens.length == 1) {
                            client.chooseToPlaceBatteries(model.getMyNickname(), null, client.getUuid());
                            ok = false;
                            break;
                        }
                        arguments = Arrays.asList(tokens).subList(1, tokens.length);
                        Pattern triplePattern = Pattern.compile("\\((\\d+),(\\d+),(\\d+)\\)");
                        Matcher tripleMatcher = triplePattern.matcher(arguments.getFirst());
                        ArrayList<ArrayList<Integer>> batteryPositions = new ArrayList<>();
                        while (tripleMatcher.find()) {
                            int r = Integer.parseInt(tripleMatcher.group(1)) - 5;
                            int c = Integer.parseInt(tripleMatcher.group(2)) - 4;
                            int count = Integer.parseInt(tripleMatcher.group(3));
                            batteryPositions.add(new ArrayList<>(List.of(r, c, count)));
                        }
                        client.chooseToPlaceBatteries(model.getMyNickname(), batteryPositions, client.getUuid());
                        ok = false;
                    }
                    break;

                    case 26:
                        wronginput = 0;
                        if (tokens.length == 1) {
                            client.chooseWhereToPutGoods(model.getMyNickname(), null, null, client.getUuid());
                            ok = false;
                            break;
                        }
                        if (tokens.length != 3) {
                            System.out.println("Not valid input");
                            break;
                        }
                        posGoods = new ArrayList<>();
                        goodsSets = new ArrayList<>();

                        boolean validPos = tokens[1].matches(regexOnlyPairs) || tokens[1].matches(regexEmptyPairs);
                        boolean validGoods = tokens[2].matches(regexGoods) || tokens[2].matches(regexEmptyPairs);

                        if (!validPos || !validGoods) {
                            System.out.println("Not valid input");
                            break;
                        }

                        if (!tokens[1].equals("()")) {
                            String inner = tokens[1].substring(1, tokens[1].length() - 1);
                            String[] posCoords = inner.split("\\)\\(");
                            for (String coord : posCoords) {
                                String[] values = coord.split(",");
                                int r = Integer.parseInt(values[0]) - 5;
                                int c = Integer.parseInt(values[1]) - 4;
                                if (r < 0 || r >= 5 || c < 0 || c >= 7) {
                                    wronginput = 1;
                                    break;
                                }
                                posGoods.add(new ArrayList<>(List.of(r, c)));
                            }
                        }

                        if (!tokens[2].equals("()")) {
                            String inner = tokens[2].substring(1, tokens[2].length() - 1);
                            String[] goodsCoords = inner.split("\\)\\(");
                            for (String coord : goodsCoords) {
                                String[] values = coord.split(",");
                                ArrayList<Goods> goodsList = new ArrayList<>();
                                for (String val : values) {
                                    int type = Integer.parseInt(val);
                                    if (type < 0 || type > 4) {
                                        wronginput = 1;
                                        break;
                                    }
                                    goodsList.add(new Goods(type, type == 4));
                                }
                                if (wronginput == 1) break;
                                goodsSets.add(goodsList);
                            }
                        }

                        if (wronginput == 1 || posGoods.size() != goodsSets.size()) {
                            System.out.println("Not valid input");
                            break;
                        }
                        client.chooseWhereToPutGoods(model.getMyNickname(), posGoods, goodsSets, client.getUuid());
                        ok = false;
                        break;

                    case 28:
                        if (tokens.length != 5) {
                            System.out.println("Not valid input");
                            break;
                        }
                        arguments = Arrays.asList(tokens).subList(1, tokens.length);
                        try {
                            int idx = Integer.parseInt(arguments.get(0));
                            int row = Integer.parseInt(arguments.get(1)) - 5;
                            int col = Integer.parseInt(arguments.get(2)) - 4;
                            int rot = Integer.parseInt(arguments.get(3));
                            if (idx < 0 || idx > 1 || row < 0 || row > 4 || col < 0 || col > 6 || rot % 90 != 0)
                                throw new NumberFormatException();
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                            break;
                        }
                        for (ShipBoard s : model.getShipBoards()) {
                            if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                id = s.getMyPlayer().getPlayerId();
                                break;
                            }
                        }
                        client.insertWaitTile(
                                id,
                                Integer.parseInt(tokens[1]),
                                Integer.parseInt(tokens[2]) - 5,
                                Integer.parseInt(tokens[3]) - 4,
                                Integer.parseInt(tokens[4]), client.getUuid());
                        ok = false;
                        break;

                    case 29:
                        if (tokens.length != 4 && tokens.length != 5) {
                            System.out.println("Not valid input");
                            break;
                        }
                        arguments = Arrays.asList(tokens).subList(1, tokens.length);
                        try {
                            int x = Integer.parseInt(arguments.get(0));
                            int r = Integer.parseInt(arguments.get(1)) - 5;
                            int c = Integer.parseInt(arguments.get(2)) - 4;

                            for (ShipBoard s : model.getShipBoards()) {
                                if (s.getMyPlayer().getUsername().equals(model.getMyNickname())) {
                                    id = s.getMyPlayer().getPlayerId();
                                    break;
                                }
                            }

                            if (x == 1) {
                                int colorIdx = Integer.parseInt(arguments.get(3));
                                if (colorIdx < 0 || colorIdx >= AlienColor.values().length)
                                    throw new NumberFormatException();
                                client.CommandFillTile(id, true, AlienColor.values()[colorIdx], r, c, client.getUuid());
                            } else if (x == 0) {
                                client.CommandFillTile(id, false, null, r, c, client.getUuid());
                            } else throw new NumberFormatException();
                            ok = false;
                        } catch (NumberFormatException e) {
                            System.out.println("Not valid input");
                        }
                        break;

                    case 30:
                        if (tokens.length != 1) {
                            System.out.println("Not valid input");
                            break;
                        }
                        client.activeGames(client.getUuid());
                        allowedMethodNumbers.clear();
                        allowedMethodNumbers.add(CommandType.CREATE_GAME);
                        allowedMethodNumbers.add(CommandType.JOIN_GAME);
                        allowedMethodNumbers.add(CommandType.ACTIVE_GAMES);
                        break;

                    default:
                        System.out.println("Not valid input");
                }
            }
        }
    }
}