package it.polimi.ingsw.View.Utils_View;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class CommandTypeMapper {

    /**
     * A static map that associates each {@link CommandType} with a unique integer code.
     * This mapping is used to encode {@link CommandType} values into integers for easier
     * processing and communication between different system components.
     *
     * The map is implemented as an {@link EnumMap} to leverage the efficient storage and
     * fast lookup provided for enums.
     *
     * Each {@link CommandType} is assigned a distinct integer code, which corresponds to
     * its ordinal value within the enumeration.
     *
     * This map is populated during static initialization of the class using all the enum
     * constants defined in {@link CommandType}.
     */
    private static final Map<CommandType, Integer> CODE_MAP = new EnumMap<>(CommandType.class);

    /**
     * A static map that provides a reverse mapping from an integer code to its corresponding
     * {@link CommandType} enumeration value.
     * This map is populated during the static initialization of the class, where each integer
     * code derived from the ordinal value of a {@link CommandType} is mapped to its respective enum.
     *
     * Used primarily for retrieving the {@link CommandType} associated with a specific integer code.
     * See {@link #fromCode(int)} for its primary use case.
     */
    private static final Map<Integer, CommandType> REVERSE_MAP = new HashMap<>();

    static {
        for (CommandType ct : CommandType.values()) {
            int code = ct.ordinal();       // se vuoi 1-based, usa ct.ordinal()+1
            CODE_MAP.put(ct, code);
            REVERSE_MAP.put(code, ct);
        }
    }


    /**
     * Retrieves the code associated with the given CommandType.
     *
     * @param cmd the CommandType for which to retrieve the corresponding code
     * @return the integer code mapped to the specified CommandType
     */
    public static int getCode(CommandType cmd) {
        return CODE_MAP.get(cmd);
    }


    /**
     * Retrieves the corresponding {@link CommandType} for the given code.
     *
     * @param code the code to be mapped to a {@code CommandType}
     * @return the {@code CommandType} associated with the specified code,
     *         or {@code null} if no matching {@code CommandType} exists
     */
    public static CommandType fromCode(int code) {
        return REVERSE_MAP.get(code);
    }


    /**
     * Checks if the specified method number corresponds to a command
     * that is allowed based on the provided list of allowed commands.
     *
     * @param methodNumber the numeric code representing a command
     * @param allowedMethodNumbers the list of allowed command types
     * @return {@code true} if the command represented by the method number is allowed; {@code false} otherwise
     */
    public static boolean isAllowed(int methodNumber, ArrayList<CommandType> allowedMethodNumbers) {
        CommandType cmd = fromCode(methodNumber);
        return cmd != null && allowedMethodNumbers.contains(cmd);
    }


    /**
     * Maps a list of {@code CommandType} objects to a list of their corresponding integer codes.
     *
     * @param commands the list of {@code CommandType} objects to be converted to integer codes
     * @return a list of integer codes corresponding to the provided {@code CommandType} objects
     */
    public static ArrayList<Integer> mapToCodeList(ArrayList<CommandType> commands) {
        ArrayList<Integer> codes = new ArrayList<>(commands.size());
        for (CommandType cmd : commands) {
            codes.add(getCode(cmd));
        }
        return codes;
    }

}
