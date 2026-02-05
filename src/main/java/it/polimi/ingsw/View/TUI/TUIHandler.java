package it.polimi.ingsw.View.TUI;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Planets.Planet;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.View.Utils_View.CommandType;
import it.polimi.ingsw.View.Utils_View.CommandTypeMapper;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.Model.Boards.FlightBoard;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Boards.ShipBoard_LevelII;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TUIHandler {
    /**
     * ANSI escape code used to reset terminal text formatting and styles to their default values.
     * It is commonly used to clear any previously applied text color, background color, or style.
     */
    // Codici ANSI per i colori
    public static final String RESET = "\u001B[0m";
    /**
     * ANSI escape code for setting the text color to black in terminal output.
     * This constant can be used in conjunction with other color codes or formatting
     * styles to customize the appearance of text in the terminal.
     */
    // Colori di testo
    public static final String BLACK = "\u001B[30m";
    /**
     * ANSI escape code for setting text color to green in terminal-based text environments.
     * Can be used to colorize output when supported by the terminal.
     */
    public static final String GREEN = "\u001B[32m";
    /**
     * ANSI escape code for setting the text color to yellow in terminal output.
     * Used for text formatting when color output is supported.
     */
    public static final String YELLOW = "\u001B[33m";
    /**
     * ANSI escape code for representing the color blue in terminal output.
     * Can be used to add blue-colored text when printing to a compatible console or terminal.
     * Example usage can involve combining this color with text or other ANSI codes.
     * Note: Proper terminal support for ANSI escape codes is required to display colored output.
     */
    public static final String BLUE = "\u001B[34m";
    /**
     * ANSI escape code for the color violet.
     * This constant can be used to apply violet text color in terminal outputs
     * that support ANSI color codes.
     */
    public static final String VIOLET = "\u001B[35m";
    /**
     * Represents the ANSI escape code for setting text color to white in terminal output.
     * This constant can be used to apply white-colored formatting to console text.
     */
    public static final String WHITE = "\u001B[37m";
    /**
     * ANSI escape code for the color brown.
     * This constant can be used to change the text color to brown in terminal outputs
     * that support ANSI escape codes.
     */
    public static final String BROWN = "\u001B[33m";
    /**
     * ANSI escape code representation for the pink color used in terminal output.
     * This constant defines the color in a 256-color ANSI code format
     * to be used for styling textual elements in console-based applications.
     */
    public static final String PINK = "\u001B[38;5;205m";
    /**
     * A string constant defining the ANSI escape code for the GRAY color.
     * Used to apply the GRAY text color formatting in the console or terminal.
     */
    public static final String GRAY = "\u001B[38;5;250m";

    /**
     * ANSI escape code for setting the background color to red in terminal outputs.
     * This constant can be used to modify the background color of text outputs
     * styled for terminals that support ANSI escape codes.
     */
    public static final String BG_RED = "\u001B[41m";
    /**
     * Represents the ANSI escape code for setting the background color to green in terminal outputs.
     * This constant is used to format text or graphical elements by applying a green background
     * color when displayed in environments that support ANSI escape sequences, such as most terminal emulators.
     */
    public static final String BG_GREEN = "\u001B[42m";
    /**
     * ANSI escape code for setting the background color to yellow in terminal-based text outputs.
     * This constant can be used to change the background color of text to yellow when
     * constructing strings for console applications or terminal interfaces.
     */
    public static final String BG_YELLOW = "\u001B[43m";
    /**
     * Represents the ANSI escape code for setting the background color to blue in terminal output.
     * This constant can be used to format text with a blue background in supported console environments.
     */
    public static final String BG_BLUE = "\u001B[44m";
    /**
     * ANSI escape code for setting the background color to purple in terminal outputs.
     * This constant can be used to style text background in terminal-based interfaces
     * when supported by the environment.
     */
    public static final String BG_PURPLE = "\u001B[45m";
    /**
     * ANSI escape code for setting the background color of the terminal to white.
     * This constant can be used for styling terminal output with a white background.
     */
    public static final String BG_WHITE = "\u001B[47m";
    /**
     * Represents the ANSI escape code for setting the background color
     * to orange in terminal-based user interface output.
     * The specific color corresponds to the 208th color in the extended
     * 256-color palette.
     */
    public static final String BG_ORANGE = "\u001B[48;5;208m";
    /**
     * Represents a background color setting for gray in the terminal using ANSI escape codes.
     * This string can be used to set the background color of text to a light gray tone in supported terminals.
     */
    public static final String BG_GRAY = "\u001B[48;5;250m";
    /**
     * Represents the ANSI escape code for setting the background color to lime green
     * in terminal-based applications that support ANSI color codes.
     * The value "\u001B[48;5;118m" changes the terminal's background color to a lime green shade.
     *
     * This variable is typically used in conjunction with text output or other terminal formatting
     * to visually enhance or highlight elements with a lime green background.
     *
     * It is part of a collection of predefined foreground and background ANSI color constants.
     */
    public static final String BG_LIME_GREEN = "\u001B[48;5;118m";

    /**
     * A constant representing a filled block character `█` used for graphical
     * representations or UI elements, often in text-based displays or terminal
     * interfaces. Typically utilized when a uniform-width or monospace character
     * is required for consistent alignment.
     */
    // Caratteri di larghezza uniforme (preferibilmente monospace)
    public static final char BLOCK = '█';
    /**
     * Represents the Unicode character for an upward arrow (↑).
     * This constant can be used to display directional indicators or navigational elements in the text-based user interface.
     */
    public static final char ARROW_UP = '↑';
    /**
     * Represents the Unicode character for the rightwards arrow (→).
     * This constant can be used for visual representations within the text-based user interface.
     */
    public static final char ARROW_RIGHT = '→';
    /**
     * Represents the Unicode character '↓', visually depicted as a downward arrow.
     * This constant can be used to symbolize a downward direction or movement in a text-based user interface (TUI).
     */
    public static final char ARROW_DOWN = '↓';
    /**
     * Represents the left arrow character '←'.
     * This constant can be used for visual representations, such as
     * directional output or graphical displays in a text-based interface.
     */
    public static final char ARROW_LEFT = '←';
    /**
     * Represents the character symbol for a person.
     * This is a predefined constant with a value of '☺'.
     * It is used in text-based user interfaces to visually depict a person icon.
     */
    public static final char PERSON = '☺';
    /**
     * Represents an alternative character symbol for a person, used for display purposes
     * within the TUIHandler class. The symbol is '☻'.
     */
    public static final char PERSON_ALT = '☻';
    /**
     * Represents the "house" symbol ('⌂') as a constant character.
     * This character can be used in a textual user interface to display a house-like symbol.
     */
    public static final char HOUSE = '⌂';
    /**
     * Represents an empty character used for spacing or blank representation in text-based interfaces.
     * This constant is primarily used for formatting and layout purposes within the TUIHandler class.
     */
    public static final char EMPTY = ' ';
    /**
     * Represents an upward-pointing triangle character symbol ('▲').
     * This constant is primarily used for displaying or styling graphical elements
     * within text-based user interfaces.
     */
    public static final char TRIANGLE_UP = '▲';
    /**
     * Represents a downward-pointing triangle symbol.
     * Commonly used in text-based user interfaces or graphical displays to indicate
     * a downward direction or to represent a specific visual element in the UI.
     */
    public static final char TRIANGLE_DOWN = '▼';
    /**
     * Represents a right-pointing triangle symbol (►).
     *
     * This constant can be used for display purposes in user interfaces
     * or text-based representations that utilize directional symbols.
     */
    public static final char TRIANGLE_RIGHT = '►';
    /**
     * Represents the left-pointing triangle character '◄'.
     * This constant is typically used for text-based user interfaces or
     * console-based graphical representations where a directional symbol
     * is needed.
     */
    public static final char TRIANGLE_LEFT = '◄';
    /**
     * Represents an empty upward-pointing triangle symbol.
     * This constant is used for visual or symbolic representation within the system.
     */
    public static final char TRIANGLE_UP_EMPTY = '△';
    /**
     * Represents an empty downward-pointing triangle symbol in the terminal-based UI.
     * This character is typically used to indicate directional or organizational elements
     * in text-based representations of models or structures within the user interface.
     */
    public static final char TRIANGLE_DOWN_EMPTY = '▽';
    /**
     * Represents a filled circle character ('●') used for visual or symbolic representation
     * in text-based user interfaces or terminal displays.
     * This constant is often used in conjunction with configurable colors or
     * other visual elements to enhance the representation of UI components.
     */
    public static final char CIRCLE_FILLED = '●';
    /**
     * Represents a Unicode character for an empty circle symbol.
     * The symbol can be used to visually display or format elements in terminal or text-based interfaces.
     */
    public static final char CIRCLE_EMPTY = '○';


    /**
     * Returns a string representation of a character formatted with the specified color.
     *
     * @param character the character to be formatted
     * @param color the color code to apply to the character
     * @return the formatted character as a string with the specified color
     */
    public static String getCharacter(char character, String color) {
        return color + character + RESET;
    }

    /**
     * Returns a string representation of a block character with the specified color.
     *
     * @param color the color to apply to the block character
     * @return the block character as a colored string
     */
    public static String block(String color) {
        return getCharacter(BLOCK, color);
    }

    /**
     * Returns a string representation of an upward arrow symbol with the specified color.
     *
     * @param color the color to style the upward arrow symbol; must be a valid color string.
     * @return the colored upward arrow symbol as a string.
     */
    public static String arrowUp(String color) {
        return getCharacter(ARROW_UP, color);
    }

    /**
     * Returns a filled circle character with the specified color applied.
     *
     * @param color the color to be applied to the filled circle character
     * @return a string representing the filled circle character styled with the specified color
     */
    public static String circleFilled(String color) {
        return getCharacter(CIRCLE_FILLED, color);
    }

    /**
     * Returns a string representation of an empty circle character with the specified color applied.
     *
     * @param color the color to be applied to the empty circle character
     * @return a string containing the empty circle character styled with the specified color
     */
    public static String circleEmpty(String color) {
        return getCharacter(CIRCLE_EMPTY, color);
    }

    /**
     * Returns a string representation of the right arrow symbol in a specified color.
     *
     * @param color the color to apply to the right arrow symbol
     * @return a string representing the right arrow symbol in the specified color
     */
    public static String arrowRight(String color) {
        return getCharacter(ARROW_RIGHT, color);
    }

    /**
     * Returns the representation of a downward arrow symbol in the specified color.
     *
     * @param color the color in which the arrow should be rendered
     * @return a string representing a downward arrow symbol in the given color
     */
    public static String arrowDown(String color) {
        return getCharacter(ARROW_DOWN, color);
    }

    /**
     * Returns the representation of an arrow pointing to the left with the specified color.
     *
     * @param color the color to apply to the arrow representation
     * @return a string representing an arrow pointing to the left with the specified color
     */
    public static String arrowLeft(String color) {
        return getCharacter(ARROW_LEFT, color);
    }

    /**
     * Returns a representation of a person symbol with the specified color.
     *
     * @param color the color to be applied to the person symbol
     * @return the formatted person symbol with the given color applied
     */
    public static String person(String color) {
        return getCharacter(PERSON, color);
    }

    /**
     * Generates a string representation of a person alternative icon
     * with the specified color applied.
     *
     * @param color the color to be applied to the person alternative icon
     * @return a string representation of the colored person alternative icon
     */
    public static String getPersonAlt(String color) {
        return color + PERSON_ALT + RESET;
    }

    /**
     * Returns a representation of a house symbol formatted with the specified color.
     *
     * @param color the color to apply to the house symbol
     * @return a string containing the house symbol formatted with the specified color
     */
    public static String house(String color) {
        return getCharacter(HOUSE, color);
    }

    /**
     * Returns the string representation of an empty character with default formatting.
     *
     * @return a string containing the empty character with default reset color formatting
     */
    public static String empty() {
        return getCharacter(EMPTY, RESET);
    }

    /**
     * Returns a string representation of an upward-facing triangle character in the specified color.
     *
     * @param color the color to apply to the triangle. This should be a valid color code or string.
     * @return a string containing the upward triangle character styled with the specified color.
     */
    public static String triangleUp(String color) {
        return getCharacter(TRIANGLE_UP, color);
    }

    /**
     * Returns a string representation of a downward-pointing triangle symbol with the specified color formatting.
     *
     * @param color the color to be applied to the triangle symbol
     * @return the downward-pointing triangle symbol formatted with the given color
     */
    public static String triangleDown(String color) {
        return getCharacter(TRIANGLE_DOWN, color);
    }

    /**
     * Creates a colored representation of a right-facing triangle symbol.
     *
     * @param color the color to be applied to the triangle symbol
     * @return the formatted string representing the colored right-facing triangle symbol
     */
    public static String triangleRight(String color) {
        return getCharacter(TRIANGLE_RIGHT, color);
    }

    /**
     * Returns a string representation of a left-pointing triangle in the specified color.
     *
     * @param color the color to apply to the triangle representation
     * @return the colored string representation of a left-pointing triangle
     */
    public static String triangleLeft(String color) {
        return getCharacter(TRIANGLE_LEFT, color);
    }

    /**
     * Generates a string representation of an upward empty triangle symbol with the specified color.
     *
     * @param color the color to apply to the triangle symbol
     * @return a string representation of the upward empty triangle symbol with the applied color
     */
    public static String triangleUpEmpty(String color) {
        return getCharacter(TRIANGLE_UP_EMPTY, color);
    }

    /**
     * Generates a representation of a downward-facing empty triangle in the specified color.
     *
     * @param color the color to be applied to the triangle representation
     * @return a string representing a downward-facing empty triangle in the specified color
     */
    public static String triangleDownEmpty(String color) {
        return getCharacter(TRIANGLE_DOWN_EMPTY, color);
    }

    /**
     * Adds text color and background color to the given text.
     *
     * @param text the text to be styled with colors
     * @param textColor the color to be applied to the text
     * @param backgroundColor the color to be applied to the background
     * @return a string that combines the specified text, text color, and background color
     */
    public static String colorText(String text, String textColor, String backgroundColor) {
        return textColor + backgroundColor + text + RESET;
    }

    /**
     * Prints the details of a given card based on its type and the associated model representation.
     * The method handles multiple card types and renders appropriate visuals and information specific to each type.
     *
     * @param card The card object containing details about the card being printed.
     * @param lmr  The LittleModelRepresentation object, used to provide additional data such as dice values for certain card types.
     */
    public void printCard(Card card, LittleModelRepresentation lmr) {
        String typeName = card.getCardName();

        // Get colors
        String bgColor = getBgColorForCardType(typeName);
        String textColor = getTextColorForBackground(bgColor);

        // Top border
        printHorizontalBorder("╔", "╗", textColor, bgColor);

        // Card title
        printCenteredLine(getCardTitle(typeName), textColor, bgColor);

        // Title divider
        printHorizontalBorder("╠", "╣", textColor, bgColor);

        // Card content
        switch (typeName) {
            case "Nave Abbandonata":
                printAttribute("🧑‍🚀 CREW PENALTY", Objects.toString(card.viewCrewPenalty()), textColor, bgColor);
                printAttribute("💰 MONEY GAINED", Objects.toString(card.viewMoneyEarning()), textColor, bgColor);
                printAttribute("⏱️ DAYS PENALTY", Objects.toString(card.viewFlightPenalty()), textColor, bgColor);
                break;

            case "Stazione Abbandonata":
                printAttribute("🧑‍🚀 CREW REQUIRED", Objects.toString(card.getCrew()), textColor, bgColor);
                printGoodsSection(card.getGoods(), textColor, bgColor);
                printAttribute("⏱️ DAYS PENALTY", Objects.toString(card.getDaysPenalty()), textColor, bgColor);
                break;

            case "Zona Di Guerra":
                printCombatZoneContent(card, textColor, bgColor);
                break;

            case "Epidemia":
                printAttribute("🦠 EPIDEMIC", "Causes disease among the crew", textColor, bgColor);
                break;

            case "Pioggia Di Meteoriti":
                printShotsSection(card.getShots(), textColor, bgColor, "☄️ SHOTS PENALTY");
                break;

            case "Spazio Aperto":
                printAttribute("🌌 EFFECT", "Clear sailing through empty space", textColor, bgColor);
                break;

            case "Pirati":
                printPiratesContent(card, textColor, bgColor);
                break;

            case "Pianeti":
                printPlanetsContent(card, textColor, bgColor);
                break;

            case "Schiavisti":
                printSlaversContent(card, textColor, bgColor);
                break;

            case "Contrabbandieri":
                printSmugglersContent(card, textColor, bgColor);
                break;

            case "Polvere Stellare":
                printAttribute("✨ STARDUST EFFECT", "For each exposed connector", textColor, bgColor);
                printAttribute("⏱️ PENALTY", "You lose one flight day", textColor, bgColor);
                break;

            default:
                printAttribute("❓ UNKNOWN CARD", typeName, textColor, bgColor);
                break;
        }

        // Bottom border
        printHorizontalBorder("╚", "╝", textColor, bgColor);
        switch (typeName) {
            case "Zona Di Guerra":
                if (lmr != null) {
                    if (lmr.getDice() != -1) {
                        System.out.println("Dice is " + lmr.getDice());
                    }
                }
                break;

            case "Pioggia Di Meteoriti":
                if (lmr != null) {
                    if (lmr.getDice() != -1) {
                        System.out.println("Dice is " + lmr.getDice());
                    }
                }
                break;

            case "Pirati":
                if (lmr != null) {
                    if (lmr.getDice() != -1) {
                        System.out.println("Dice is " + lmr.getDice());
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * Prints a horizontal border with styled text, based on provided start and end characters,
     * text color, and background color.
     *
     * @param start the string to be placed at the beginning of the border
     * @param end the string to be placed at the end of the border
     * @param textColor the color of the text in the border
     * @param bgColor the background color of the border
     */
    private void printHorizontalBorder(String start, String end, String textColor, String bgColor) {
        System.out.println(colorText(
                start + "═".repeat(44 - 2) + end,
                textColor,
                bgColor
        ));
    }

    /**
     * Prints a line of text centered within a fixed-width formatted block, with specified text and background colors.
     *
     * @param content The text content to be printed in the centered line.
     * @param textColor The color of the text to be displayed.
     * @param bgColor The background color to be applied to the printed line.
     */
    private void printCenteredLine(String content, String textColor, String bgColor) {
        int effectiveWidth = 44 - 2;
        int contentLength = content.length();

        int leftPadding = (effectiveWidth - contentLength) / 2;
        int rightPadding = effectiveWidth - contentLength - leftPadding;

        String paddedContent = " ".repeat(leftPadding) + content + " ".repeat(rightPadding);
        System.out.println(colorText("║" + paddedContent + "║", textColor, bgColor));
    }

    /**
     * Prints a formatted attribute with a label and value, applying text color and background color.
     * The output is adjusted to fit within a defined width, wrapping text across multiple lines if necessary.
     *
     * @param label The label to display for the attribute.
     * @param value The value associated with the label. If empty, only the label is printed.
     * @param textColor The color to apply to the text content.
     * @param bgColor The background color to apply around the text.
     */
    private void printAttribute(String label, String value, String textColor, String bgColor) {
        String fullText = label + (value.isEmpty() ? "" : ": " + value);
        int maxContentWidth = 44 - 4;

        List<String> lines = splitIntoLines(fullText, maxContentWidth);

        for (String line : lines) {
            String paddedLine = line + " ".repeat(maxContentWidth - line.length());
            System.out.println(colorText("║" + " " + paddedLine + " " + "║", textColor, bgColor));
        }
    }

    /**
     * Prints the goods section in a formatted style with specified text and background colors.
     * Each good is represented as a visual block, and the section is bounded by decorative borders.
     *
     * @param goods the list of Goods objects to be displayed in the section
     * @param textColor the color to be used for the text in the section
     * @param bgColor the background color to be used for the section
     */
    private void printGoodsSection(ArrayList<Goods> goods, String textColor, String bgColor) {
        String prefix = " 📦 GOODS GAINED: ";
        System.out.print(colorText("║" + prefix, textColor, bgColor));
        int usedSpace = prefix.length();
        for (Goods good : goods) {
            System.out.print(getGoodsBlock(good));
            System.out.print(colorText(" ", textColor, bgColor));
            usedSpace += 4;
        }
        int remainingSpace = 44 - 2 - usedSpace;
        System.out.print(colorText(" ".repeat(Math.max(0, remainingSpace)), textColor, bgColor));
        System.out.println(colorText("║", textColor, bgColor));
    }

    /**
     * Generates a formatted string representation of a Goods object based on its value.
     * The method applies specific color and background formatting to the string depending on the value.
     * If the value does not match defined cases, it returns a default string with no color formatting.
     *
     * @param good the Goods object whose value determines the formatting
     * @return a formatted string representation of the Goods object
     */
    private String getGoodsBlock(Goods good) {
        return switch (good.getValue()) {
            case 4 -> colorText(" 4 ", WHITE, BG_RED);
            case 3 -> colorText(" 3 ", BLACK, BG_YELLOW);
            case 2 -> colorText(" 2 ", BLACK, BG_LIME_GREEN);
            case 1 -> colorText(" 1 ", BLACK, BG_BLUE);
            default -> " ? ";
        };
    }

    /**
     * Prints the shots section with specific formatting, colors, and a title.
     * Each shot is displayed in a formatted manner with consistent alignment within
     * a specified width, and the details are customized with the provided text
     * and background colors.
     *
     * @param shots     a list of {@link Shot} objects to be displayed. Each shot includes
     *                  properties such as its size, type, and angle of rotation.
     * @param textColor the color used for the text in the section. This determines
     *                  the foreground color of the textual content.
     * @param bgColor   the color used for the background in the section. This sets
     *                  the background color behind the text and borders.
     * @param title     the title of the shots section. This is displayed at the top
     *                  of the section with the specified text and background colors.
     */
    private void printShotsSection(ArrayList<Shot> shots, String textColor, String bgColor, String title) {
        printAttribute(title, "", textColor, bgColor);

        for (Shot shot : shots) {
            String shotText = formatShot(shot);
            int spacesCount = Math.max(0, 44 - 5 - shotText.length());
            System.out.println(colorText("║" + "   " + shotText + " ".repeat(spacesCount) + "║", textColor, bgColor));
        }
    }

    /**
     * Prints the content of the combat zone for a given card, displaying textual attributes
     * and the shots section using the provided text and background colors.
     *
     * @param card the card representing the combat zone and its specific attributes
     * @param textColor the color used for the text in the display
     * @param bgColor the background color used for the display
     */
    private void printCombatZoneContent(Card card, String textColor, String bgColor) {
        if (card.getId() == 36) {
            printAttribute("⚠️ EFFECT 1", "Player with less fire power loses 4 flight days", textColor, bgColor);
            printAttribute("⚠️ EFFECT 2", "Player with less motion power loses 3 goods", textColor, bgColor);
            printAttribute("⚠️ EFFECT 3", "Player with less crew receives shots:", textColor, bgColor);
        } else {
            printAttribute("⚠️ EFFECT 1", "Player with less crew loses 3 flight days", textColor, bgColor);
            printAttribute("⚠️ EFFECT 2", "Player with less motion power loses 2 passengers", textColor, bgColor);
            printAttribute("⚠️ EFFECT 3", "Player with fire power receives shots:", textColor, bgColor);
        }
        printShotsSection(card.getConsequences().get(2).getShots(), textColor, bgColor, "");
    }

    /**
     * Prints the content of the planets section from a card, displaying each planet with its associated goods.
     * Additionally, displays a penalty indicator.
     *
     * @param card The card containing the planets to be printed.
     * @param textColor The text color to use for the displayed content.
     * @param bgColor The background color to apply to the displayed content.
     */
    private void printPlanetsContent(Card card, String textColor, String bgColor) {
        printAttribute("🪐 LIST OF PLANETS", "", textColor, bgColor);

        int planetIndex = 0;
        for (Planet planet : card.getPlanetVector()) {
            String prefix = "   Planet " + planetIndex + ": ";
            System.out.print(colorText("║" + prefix, textColor, bgColor));
            int usedSpace = prefix.length();
            for (Goods good : planet.getGoods()) {
                System.out.print(getGoodsBlock(good));
                System.out.print(colorText(" ", textColor, bgColor));
                usedSpace += 4;
            }
            int remainingSpace = 44 - 2 - usedSpace;
            System.out.print(colorText(" ".repeat(Math.max(0, remainingSpace)), textColor, bgColor));
            System.out.println(colorText("║", textColor, bgColor));
            planetIndex++;
        }
        printAttribute("⏱️ DAYS PENALTY", Objects.toString(card.getDaysPenalty()), textColor, bgColor);
    }

    /**
     * Prints the contents of a pirate-themed card, including attributes such as firepower required, shots penalty,
     * money gained, and days penalty. The content is formatted with the specified text and background colors.
     *
     * @param card      the card object containing the pirate-related attributes to be displayed
     * @param textColor the color of the text used for displaying the attributes
     * @param bgColor   the background color used for formatting the display output
     */
    private void printPiratesContent(Card card, String textColor, String bgColor) {
        printAttribute("🔥 FIREPOWER REQUIRED", Objects.toString(card.getFirePower()), textColor, bgColor);
        printShotsSection(card.getShots(), textColor, bgColor, "⚠️ SHOTS PENALTY");
        printAttribute("💰 MONEY GAINED", Objects.toString(card.getMoneyGained()), textColor, bgColor);
        printAttribute("⏱️ DAYS PENALTY", Objects.toString(card.getDaysPenalty()), textColor, bgColor);
    }

    /**
     * Prints detailed content related to slavers for the given card.
     * Information displayed includes firepower requirements, crew penalty, money gained,
     * and days penalty with specified text and background colors.
     *
     * @param card the card containing data related to slavers
     * @param textColor the color of the text
     * @param bgColor the background color for the text
     */
    private void printSlaversContent(Card card, String textColor, String bgColor) {
        printAttribute("🔥 FIREPOWER REQUIRED", Objects.toString(card.getFirePower()), textColor, bgColor);
        printAttribute("🧑‍🚀 CREW PENALTY", Objects.toString(card.getCrewLost()), textColor, bgColor);
        printAttribute("💰 MONEY GAINED", Objects.toString(card.getMoneyGained()), textColor, bgColor);
        printAttribute("⏱️ DAYS PENALTY", Objects.toString(card.getDaysPenalty()), textColor, bgColor);
    }

    /**
     * Prints the contents of a Smuggler's card, including firepower required, goods penalty,
     * goods details, and days penalty with styled text and background colors.
     *
     * @param card      The Smuggler's {@code Card} object containing the relevant attributes to display.
     * @param textColor The color to be applied to the text content.
     * @param bgColor   The background color to be applied behind the text content.
     */
    private void printSmugglersContent(Card card, String textColor, String bgColor) {
        printAttribute("🔥 FIREPOWER REQUIRED", Objects.toString(card.getFirePower()), textColor, bgColor);
        printAttribute("📦 GOODS PENALTY", Objects.toString(card.getGoodsPenalty()), textColor, bgColor);
        printGoodsSection(card.getGoods(), textColor, bgColor);
        printAttribute("⏱️ DAYS PENALTY", Objects.toString(card.getDaysPenalty()), textColor, bgColor);
    }

    /**
     * Determines the appropriate text color to use for given background colors.
     *
     * @param bgColor the background color for which the text color needs to be determined
     * @return the text color (either WHITE or BLACK) that contrasts with the provided background color
     */
    private String getTextColorForBackground(String bgColor) {
        return switch (bgColor) {
            case BG_BLUE, BG_RED, BG_PURPLE, BG_GRAY -> WHITE;
            default -> BLACK;
        };
    }

    /**
     * Splits the given text into a list of lines, each with a maximum specified length.
     * If the text exceeds the maximum length, it is divided into multiple lines.
     *
     * @param text the input string to be split into lines
     * @param maxLength the maximum length of each line
     * @return a list of strings, where each string represents a line of at most the specified maximum length
     */
    private List<String> splitIntoLines(String text, int maxLength) {
        List<String> lines = new ArrayList<>();
        int index = 0;
        while (index < text.length()) {
            int end = Math.min(index + maxLength, text.length());
            lines.add(text.substring(index, end));
            index = end;
        }
        return lines;
    }


    /**
     * Formats the given shot into a string that represents its type, size, and direction.
     *
     * The formatted string contains the type of the shot, an icon indicating whether
     * the shot is "big" or not, and the direction based on its rotation value.
     *
     * @param shot the Shot object to be formatted, containing its type, size, and rotation.
     * @return a formatted string representation of the shot, including its type, size, and direction.
     */
    private String formatShot(Shot shot) {
        String sizeIcon;
        if (shot.getIsBig()) sizeIcon = "🔴";
        else sizeIcon = "⚪";
        String direction = switch (shot.getRotation()) {
            case 180 -> "↓ UPPER";
            case 0 -> "↑ LOWER";
            case 90 -> "→ LEFT";
            case 270 -> "← RIGHT";
            default -> "";
        };
        return " " + shot.getType().toString() +
                " SIZE: " + sizeIcon +
                ", FROM " + direction + " SIDE";
    }


    /**
     * Determines the background color to use based on the provided card type.
     *
     * @param typeName the name of the card type for which the background color is determined
     * @return a string representing the background color associated with the given card type
     */
    // Helper method to get background color based on card type with enhanced colors
    private String getBgColorForCardType(String typeName) {
        return switch (typeName) {
            case "Stazione Abbandonata", "Nave Abbandonata" -> BG_GRAY;
            case "Zona Di Guerra" -> BG_RED;
            case "Epidemia", "Polvere Stellare" -> BG_YELLOW;
            case "Pioggia Di Meteoriti" -> BG_ORANGE;
            case "Spazio Aperto" -> BG_BLUE;
            case "Pirati", "Schiavisti", "Contrabbandieri" -> BG_PURPLE;
            case "Pianeti" -> BG_GREEN;
            default -> BG_WHITE;
        };
    }

    /**
     * Helper method to get a consistent and stylized title for a card based on its type.
     *
     * @param typeName the name of the card type, which determines the associated title style and iconography
     * @return a formatted string representing the styled title of the card
     */
    // Helper method to get proper title for a card with consistent styling
    private String getCardTitle(String typeName) {
        return switch (typeName) {
            case "Stazione Abbandonata" -> "⚓ ABANDONED STATION ⚓";
            case "Nave Abbandonata" -> "🚀 ABANDONED SHIP 🚀";
            case "Zona Di Guerra" -> "⚔️ COMBAT ZONE ⚔️";
            case "Epidemia" -> "🦠 EPIDEMIC 🦠";
            case "Pioggia Di Meteoriti" -> "☄️ METEOR SWARM ☄️";
            case "Spazio Aperto" -> "🌌 OPEN SPACE 🌌";
            case "Pirati" -> "🏴‍☠️ PIRATES 🏴‍☠️";
            case "Pianeti" -> "🪐 PLANETS 🪐";
            case "Schiavisti" -> "⛓️ SLAVERS ⛓️";
            case "Contrabbandieri" -> "🥷 SMUGGLERS 🥷";
            case "Polvere Stellare" -> "✨ STARDUST ✨";
            default -> typeName.toUpperCase();
        };
    }

    /**
     * Prints the flight board to the console in a formatted style based on the size of the board and player details.
     *
     * @param fb The FlightBoard object containing the details of the board, such as its size, player positions,
     *           and other configurations required for displaying the board.
     */
    public void printFlightBoard(FlightBoard fb) {
        int size = fb.getBoardSize();
        ArrayList<Integer> board = fb.getBoard();
        if (size > 20) { //level 2
            for (int i = 0; i < size; i++) {
                int toPrint = i + 1;
                int val = board.get(i);
                if (i % 8 == 0) {
                    System.out.println();
                    System.out.println();
                }
                if (i == 0 || i == 1 || i == 3 || i == 6) {
                    if (val == -1) {
                        System.out.print(colorText(" " + toPrint + " EMPTY ", BLACK, BG_PURPLE));
                    } else {
                        System.out.print(colorText(" " + toPrint + " " + fb.getPlayerNameFromPos(i, fb.getPlayerRankList()) + " ", BLACK, BG_PURPLE));
                    }
                } else {
                    if (val == -1) {
                        System.out.print(colorText(" " + toPrint + " EMPTY ", BLACK, BG_RED));
                    } else {
                        System.out.print(colorText(" " + toPrint + " " + fb.getPlayerNameFromPos(i, fb.getPlayerRankList()) + " ", BLACK, BG_GREEN));
                    }
                }
                if (i < size - 1) {
                    System.out.print(" " + arrowRight(BLACK) + " ");
                }
            }
            System.out.println();
            for (int ind = 0; ind < 3; ind++) {
                System.out.println("Little deck :" + ind);
                if (fb.getDecksInTheHandofOthers().contains(fb.getShowableDeck(ind))) {
                    System.out.println("Someone else has it");
                } else {
                    System.out.print(colorText(" " + 1 + " ", WHITE, BG_BLUE));
                    System.out.print(" ");
                    System.out.print(colorText(" " + 2 + " ", BLACK, BG_PURPLE));
                    System.out.print(" ");
                    System.out.print(colorText(" " + 2 + " ", BLACK, BG_PURPLE));
                    System.out.print(" ");
                    System.out.println(" ");
                }
            }
        } else { //level 1
            for (int i = 0; i < size; i++) {
                int val = board.get(i);
                int toPrint = i + 1;
                if (i % 8 == 0) {
                    System.out.println();
                    System.out.println();
                }
                if (i == 0 || i == 1 || i == 2 || i == 4) {
                    if (val == -1) {
                        System.out.print(colorText(" " + toPrint + " EMPTY ", BLACK, BG_BLUE));
                    } else {
                        System.out.print(colorText(" " + toPrint + " " + fb.getPlayerNameFromPos(i, fb.getPlayerRankList()) + " ", BLACK, BG_BLUE));
                    }
                } else {
                    if (val == -1) {
                        System.out.print(colorText(" " + toPrint + " EMPTY ", BLACK, BG_RED));
                    } else {
                        System.out.print(colorText(" " + toPrint + " " + fb.getPlayerNameFromPos(i, fb.getPlayerRankList()) + " ", BLACK, BG_GREEN));
                    }
                }
                if (i < size - 1) {
                    System.out.print(arrowRight(BLACK));
                }
            }
            System.out.println();
        }
    }

    /**
     * Prints the visual representation of the given ShipBoard.
     *
     * @param shipBoard The ShipBoard object to be printed. This contains details about the ship matrix,
     *                  player information, and waiting tile positions for visualization.
     */
    public void printShipBoard(ShipBoard shipBoard) {
        SpaceShipTile[][] shipMatrix = shipBoard.getShipMatrix();
        switch (shipBoard.getMyPlayer().getColour()) {
            case RED:
                System.out.println(colorText("Player " + shipBoard.getMyPlayer().getUsername() + " ShipBoard", BLACK, BG_RED));
                break;
            case YELLOW:
                System.out.println(colorText("Player " + shipBoard.getMyPlayer().getUsername() + " ShipBoard", BLACK, BG_YELLOW));
                break;
            case GREEN:
                System.out.println(colorText("Player " + shipBoard.getMyPlayer().getUsername() + " ShipBoard", BLACK, BG_GREEN));
                break;
            case BLUE:
                System.out.println(colorText("Player " + shipBoard.getMyPlayer().getUsername() + " ShipBoard", BLACK, BG_BLUE));
                break;
        }
        System.out.println("       4               5               6                7               8               9               10        ");
        for (int r = 0; r < 5; r++) {
            String[][] shipFullRow = new String[5][35];
            for (int c = 0; c < 7; c++) {
                if (shipBoard instanceof ShipBoard_LevelII && r == 0 && (c == 5 || c == 6)) { //if lev 2 shipboard and in waitTilePositions
                    if (c == 5 && shipBoard.getWaitTile1() != null) {
                        prepareTileVisual(shipBoard.getWaitTile1(), shipFullRow, c * 5);
                    } else if (c == 6 && shipBoard.getWaitTile2() != null) { //so c is 6
                        prepareTileVisual(shipBoard.getWaitTile2(), shipFullRow, c * 5);
                    } else {
                        prepareTileVisual(shipMatrix[r][c], shipFullRow, c * 5);
                    }
                } else {
                    prepareTileVisual(shipMatrix[r][c], shipFullRow, c * 5);
                }
            }

            System.out.println("————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
            for (int row = 0; row < 5; row++) {
                int col1 = 0;
                for (int col = 0; col < 35; col++) {
                    if (col == 0 || col == 5 || col == 10 || col == 15 || col == 20 || col == 25 || col == 30) {
                        System.out.print('│');
                    }
                    System.out.print(" " + shipFullRow[row][col] + " ");
                    col1++;
                }
                int toPrint = r + 5;
                if (col1 > 34 && row == 2) {
                    System.out.print("│ " + toPrint + " \n");
                } else System.out.print("│\n");
            }
        }
        System.out.println("————————————————————————————————————————————————————————————————————————————————————————————————————————————————");
        System.out.println("       4               5               6                7               8               9               10        ");
    }


    /**
     * Prepares the visual representation of a spaceship tile and updates the corresponding sections of the
     * full-row matrix, including handling of visual connectors, tile-specific colors, and passenger or alien details.
     *
     * @param tile         The {@link SpaceShipTile} object representing the tile to prepare, which may be null to indicate an empty tile.
     * @param shipFullRow  A 2D array of strings representing the full row of the ship's visual matrix being updated.
     * @param s            The starting column in the ship's visual matrix where the tile visualization is placed.
     */
    private void prepareTileVisual(SpaceShipTile tile, String[][] shipFullRow, int s) {
        //s is representing the start column visualized ad the single tile in the full row matrix
        if (tile == null) { //EMPTY
            for (int row = 0; row < 5; row++) {
                for (int col = s; col < s + 5; col++) {
                    shipFullRow[row][col] = empty();
                }
            }
            return;
        }
        prepareConnectors(tile, shipFullRow, s);
        if (!tile.isFlipped()) {
            shipFullRow[0][s] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[0][s + 1] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[0][s + 2] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[0][s + 3] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[0][s + 4] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[1][s] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[1][s + 1] = colorText("X", BLACK, BG_BLUE);
            shipFullRow[1][s + 2] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[1][s + 3] = colorText("X", BLACK, BG_BLUE);
            shipFullRow[1][s + 4] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[2][s] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[2][s + 1] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[2][s + 2] = colorText("X", BLACK, BG_BLUE);
            shipFullRow[2][s + 3] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[2][s + 4] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[3][s] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[3][s + 1] = colorText("X", BLACK, BG_BLUE);
            shipFullRow[3][s + 2] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[3][s + 3] = colorText("X", BLACK, BG_BLUE);
            shipFullRow[3][s + 4] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[4][s] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[4][s + 1] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[4][s + 2] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[4][s + 3] = colorText(" ", BLACK, BG_BLUE);
            shipFullRow[4][s + 4] = colorText(" ", BLACK, BG_BLUE);
            return;
        }
        int id = tile.getID();
        if (id == 52 || id == 33 || id == 61 || id == 34) {
            String COLOR;
            if (id == 52) { //red
                COLOR = BG_RED;
            } else if (id == 33) { //blue
                COLOR = BG_BLUE;
            } else if (id == 61) { //yellow
                COLOR = BG_YELLOW;
            } else {//if (id == 34) green
                COLOR = BG_GREEN;
            }
            shipFullRow[1][s + 1] = colorText(" ", BLACK, COLOR);
            shipFullRow[1][s + 2] = colorText(" ", BLACK, COLOR);
            shipFullRow[1][s + 3] = colorText(" ", BLACK, COLOR);
            shipFullRow[2][s + 2] = colorText(house(BLACK), BLACK, COLOR);
            shipFullRow[3][s + 2] = colorText(" ", BLACK, COLOR);
            shipFullRow[3][s + 3] = colorText(" ", BLACK, COLOR);
            if (tile.getIsThereAlien()) {
                shipFullRow[2][s + 3] = colorText(" ", BLACK, COLOR);
                if (tile.getAlienColor() == AlienColor.VIOLET) {
                    shipFullRow[2][s + 1] = colorText(getPersonAlt(VIOLET), BLACK, COLOR);
                    shipFullRow[3][s + 1] = colorText(getCharacter('X', VIOLET), BLACK, COLOR);
                }
                if (tile.getAlienColor() == AlienColor.BROWN) {
                    shipFullRow[2][s + 1] = colorText(getPersonAlt(BROWN), BLACK, COLOR);
                    shipFullRow[3][s + 1] = colorText(getCharacter('X', BROWN), BLACK, COLOR);
                }
            } else {
                if (tile.getNumPassengers() == 2) {
                    shipFullRow[2][s + 1] = colorText(getPersonAlt(BLACK), BLACK, COLOR);
                    shipFullRow[3][s + 1] = colorText(" ", BLACK, COLOR);
                    shipFullRow[2][s + 3] = colorText(getPersonAlt(BLACK), BLACK, COLOR);
                } else if (tile.getNumPassengers() == 1) {
                    shipFullRow[2][s + 1] = colorText(getPersonAlt(BLACK), BLACK, COLOR);
                    shipFullRow[3][s + 1] = colorText(" ", BLACK, COLOR);
                    shipFullRow[2][s + 3] = colorText(" ", BLACK, COLOR);
                } else {
                    shipFullRow[2][s + 1] = colorText(" ", BLACK, COLOR);
                    shipFullRow[3][s + 1] = colorText(" ", BLACK, COLOR);
                    shipFullRow[2][s + 3] = colorText(" ", BLACK, COLOR);
                }
            }
            return;
        }

        switch (tile.getType()) {
            case Tile_NonAccesiblePlace:
                //shipFullRow[0][s +1] = empty();shipFullRow[0][s +2] = empty();shipFullRow[0][s +3] = empty();
                shipFullRow[1][s + 1] = "x";
                shipFullRow[1][s + 2] = empty();
                shipFullRow[1][s + 3] = "x";
                shipFullRow[2][s + 1] = empty();
                shipFullRow[2][s + 2] = "x";
                shipFullRow[2][s + 3] = empty();
                shipFullRow[3][s + 1] = "x";
                shipFullRow[3][s + 2] = empty();
                shipFullRow[3][s + 3] = "x";
                //shipFullRow[4][s +1] = empty();shipFullRow[4][s +2] = empty();shipFullRow[4][s +3] = empty();
                break;
            case Tile_StructuralModules:
                //shipFullRow[0][s +1] = empty();shipFullRow[0][s +2] = empty();shipFullRow[0][s +3] = empty();
                shipFullRow[1][s + 1] = empty();
                shipFullRow[1][s + 2] = empty();
                shipFullRow[1][s + 3] = empty();
                shipFullRow[2][s + 1] = empty();
                shipFullRow[2][s + 2] = empty();
                shipFullRow[2][s + 3] = empty();
                shipFullRow[3][s + 1] = empty();
                shipFullRow[3][s + 2] = empty();
                shipFullRow[3][s + 3] = empty();
                // shipFullRow[4][s +1] = empty();shipFullRow[4][s +2] = empty();shipFullRow[4][s +3] = empty();
                break;
            case Tile_Cabin:
                //shipFullRow[0][s +1] = empty();shipFullRow[0][s +2] = empty();shipFullRow[0][s +3] = empty();
                shipFullRow[1][s + 1] = empty();
                shipFullRow[1][s + 2] = empty();
                shipFullRow[1][s + 3] = empty();
                shipFullRow[2][s + 2] = house(BLACK);
                shipFullRow[3][s + 2] = empty();
                shipFullRow[3][s + 3] = empty();
                //shipFullRow[4][s +1] = empty();shipFullRow[4][s +2] = empty();shipFullRow[4][s +3] = empty();
                if (tile.getIsThereAlien()) {
                    shipFullRow[2][s + 3] = empty();
                    if (tile.getAlienColor() == AlienColor.VIOLET) {
                        shipFullRow[2][s + 1] = getPersonAlt(VIOLET);
                        shipFullRow[3][s + 1] = getCharacter('X', VIOLET);
                    }
                    if (tile.getAlienColor() == AlienColor.BROWN) {
                        shipFullRow[2][s + 1] = getPersonAlt(BROWN);
                        shipFullRow[3][s + 1] = getCharacter('X', BROWN);
                    }
                } else {
                    if (tile.getNumPassengers() == 2) {
                        shipFullRow[2][s + 1] = getPersonAlt(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[2][s + 3] = getPersonAlt(BLACK);
                    } else if (tile.getNumPassengers() == 1) {
                        shipFullRow[2][s + 1] = getPersonAlt(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[2][s + 3] = empty();
                    } else {
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[2][s + 3] = empty();
                    }
                }
                break;
            case Tile_AlienLifeSupport:
                if (tile.getColor() == AlienColor.VIOLET)
                    shipFullRow[2][s + 2] = block(VIOLET);
                if (tile.getColor() == AlienColor.BROWN)
                    shipFullRow[2][s + 2] = block(BROWN);

                shipFullRow[1][s + 1] = block(BLACK);
                shipFullRow[1][s + 2] = block(BLACK);
                shipFullRow[1][s + 3] = block(BLACK);
                shipFullRow[2][s + 1] = block(BLACK);
                shipFullRow[2][s + 3] = block(BLACK);
                shipFullRow[3][s + 1] = block(BLACK);
                shipFullRow[3][s + 2] = block(BLACK);
                shipFullRow[3][s + 3] = block(BLACK);
                break;
            case Tile_ShieldGenerator:
                switch (tile.getRotation()) {
                    case 0:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = getCharacter('—', BLACK);
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('S', BLACK);
                        shipFullRow[2][s + 3] = getCharacter('|', BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 90:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('S', BLACK);
                        shipFullRow[2][s + 3] = getCharacter('|', BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = getCharacter('—', BLACK);
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 180:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = getCharacter('|', BLACK);
                        shipFullRow[2][s + 2] = getCharacter('S', BLACK);
                        shipFullRow[2][s + 3] = empty();
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = getCharacter('—', BLACK);
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 270:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = getCharacter('—', BLACK);
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = getCharacter('|', BLACK);
                        shipFullRow[2][s + 2] = getCharacter('S', BLACK);
                        shipFullRow[2][s + 3] = empty();
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                }
                break;
            case Tile_Engine:
                switch (tile.getRotation()) {
                    case 0:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('M', BLACK);
                        shipFullRow[2][s + 3] = arrowDown(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 90:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('M', BLACK);
                        shipFullRow[2][s + 3] = arrowLeft(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 180:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('M', BLACK);
                        shipFullRow[2][s + 3] = arrowUp(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 270:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('M', BLACK);
                        shipFullRow[2][s + 3] = arrowRight(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                }
                break;
            case Tile_Double_Engine:
                switch (tile.getRotation()) {
                    case 0:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = arrowDown(BLACK);
                        shipFullRow[2][s + 2] = getCharacter('M', BLACK);
                        shipFullRow[2][s + 3] = arrowDown(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 90:
                        shipFullRow[1][s + 1] = arrowLeft(BLACK);
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = arrowLeft(BLACK);
                        shipFullRow[2][s + 2] = getCharacter('M', BLACK);
                        shipFullRow[2][s + 3] = empty();
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 180:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = arrowUp(BLACK);
                        shipFullRow[2][s + 2] = getCharacter('M', BLACK);
                        shipFullRow[2][s + 3] = arrowUp(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 270:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = arrowRight(BLACK);
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('M', BLACK);
                        shipFullRow[2][s + 3] = arrowRight(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                }
                break;
            case Tile_Cannon:
                switch (tile.getRotation()) {
                    case 0:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('C', BLACK);
                        shipFullRow[2][s + 3] = arrowUp(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 90:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('C', BLACK);
                        shipFullRow[2][s + 3] = arrowRight(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 180:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('C', BLACK);
                        shipFullRow[2][s + 3] = arrowDown(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 270:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = arrowLeft(BLACK);
                        shipFullRow[2][s + 2] = getCharacter('C', BLACK);
                        shipFullRow[2][s + 3] = empty();
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                }
                break;
            case Tile_Double_Cannon:
                switch (tile.getRotation()) {
                    case 0:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = arrowUp(BLACK);
                        shipFullRow[2][s + 2] = getCharacter('C', BLACK);
                        shipFullRow[2][s + 3] = arrowUp(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 90:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = arrowRight(BLACK);
                        shipFullRow[2][s + 1] = empty();
                        shipFullRow[2][s + 2] = getCharacter('C', BLACK);
                        shipFullRow[2][s + 3] = arrowRight(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 180:
                        shipFullRow[1][s + 1] = empty();
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = arrowDown(BLACK);
                        shipFullRow[2][s + 2] = getCharacter('C', BLACK);
                        shipFullRow[2][s + 3] = arrowDown(BLACK);
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                    case 270:
                        shipFullRow[1][s + 1] = arrowLeft(BLACK);
                        shipFullRow[1][s + 2] = empty();
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 1] = arrowLeft(BLACK);
                        shipFullRow[2][s + 2] = getCharacter('C', BLACK);
                        shipFullRow[2][s + 3] = empty();
                        shipFullRow[3][s + 1] = empty();
                        shipFullRow[3][s + 2] = empty();
                        shipFullRow[3][s + 3] = empty();
                        break;
                }
                break;
            case Tile_BatteryComponent:
                int charges = tile.getNumCharges();
                int capacityB = tile.getTileBattCapacity();  // 2 o 3

                shipFullRow[1][s + 1] = empty();
                shipFullRow[1][s + 2] = empty();
                shipFullRow[1][s + 3] = empty();
                shipFullRow[3][s + 1] = empty();
                shipFullRow[3][s + 2] = empty();
                shipFullRow[3][s + 3] = empty();

                shipFullRow[2][s + 1] = empty();
                shipFullRow[2][s + 2] = empty();
                shipFullRow[2][s + 3] = empty();
                int[] slot = {s + 1, s + 2, s + 3};

                for (int i = 0; i < capacityB; i++) {
                    int col = slot[capacityB == 2 ? i + 1 : i];
                    shipFullRow[2][col] = (i < charges) ? block(GREEN) : block(GRAY);
                }
                break;
            case Tile_CargoHold:
                int capacity = tile.getCapacity();
                shipFullRow[1][s + 1] = empty();//shipFullRow[1][s +2] = empty();
                shipFullRow[2][s + 1] = empty();
                shipFullRow[2][s + 2] = block(BLUE);
                shipFullRow[3][s + 1] = empty();
                shipFullRow[3][s + 2] = block(BLUE);
                List<Goods> effectivePresentGoods = tile.getEffectivePresentGoods();
                if (capacity == 2) {
                    shipFullRow[1][s + 2] = empty();
                    shipFullRow[1][s + 3] = empty();
                    String COLOR;
                    if (effectivePresentGoods.isEmpty()) {
                        shipFullRow[2][s + 3] = empty();
                        shipFullRow[3][s + 3] = empty();
                    } else {
                        int value = effectivePresentGoods.getFirst().getValue();
                        COLOR = switch (value) {
                            case 1 -> BLUE;
                            case 2 -> GREEN;
                            case 3 -> YELLOW;
                            default -> BLACK;
                        };
                        shipFullRow[2][s + 3] = block(COLOR);
                    }
                    if (effectivePresentGoods.size() > 1) {
                        int value = effectivePresentGoods.get(1).getValue();
                        COLOR = switch (value) {
                            case 1 -> BLUE;
                            case 2 -> GREEN;
                            case 3 -> YELLOW;
                            default -> BLACK;
                        };
                        shipFullRow[3][s + 3] = block(COLOR);
                    } else {
                        shipFullRow[3][s + 3] = empty();
                    }
                } else { //capacity == 3
                    shipFullRow[1][s + 2] = block(BLUE);
                    String COLOR;
                    if (effectivePresentGoods.isEmpty()) {
                        shipFullRow[1][s + 3] = empty();
                        shipFullRow[2][s + 3] = empty();
                        shipFullRow[3][s + 3] = empty();
                    } else {
                        int value = effectivePresentGoods.getFirst().getValue();
                        COLOR = switch (value) {
                            case 1 -> BLUE;
                            case 2 -> GREEN;
                            case 3 -> YELLOW;
                            default -> BLACK;
                        };
                        shipFullRow[2][s + 3] = block(COLOR);
                    }
                    switch (effectivePresentGoods.size()) {
                        case 1:
                            shipFullRow[1][s + 3] = empty();
                            shipFullRow[3][s + 3] = empty();
                            break;
                        case 2:
                            int value1 = effectivePresentGoods.get(1).getValue();
                            COLOR = switch (value1) {
                                case 1 -> BLUE;
                                case 2 -> GREEN;
                                case 3 -> YELLOW;
                                default -> BLACK;
                            };
                            shipFullRow[1][s + 3] = block(COLOR);
                            shipFullRow[3][s + 3] = empty();
                            break;
                        case 3:
                            int value2 = effectivePresentGoods.get(1).getValue();
                            COLOR = switch (value2) {
                                case 1 -> BLUE;
                                case 2 -> GREEN;
                                case 3 -> YELLOW;
                                default -> BLACK;
                            };
                            shipFullRow[1][s + 3] = block(COLOR);

                            int value3 = effectivePresentGoods.get(2).getValue();
                            COLOR = switch (value3) {
                                case 1 -> BLUE;
                                case 2 -> GREEN;
                                case 3 -> YELLOW;
                                default -> BLACK;
                            };
                            shipFullRow[3][s + 3] = block(COLOR);
                            break;
                    }
                }
                break;
            case Tile_SpecialCargoHold:
                int cap = tile.getCapacity();
                shipFullRow[1][s + 1] = empty();
                shipFullRow[1][s + 2] = empty();
                shipFullRow[1][s + 3] = empty();
                shipFullRow[2][s + 1] = empty();
                shipFullRow[2][s + 2] = block(PINK);
                shipFullRow[3][s + 1] = empty();
                List<Goods> effectivePresentGoods2 = tile.getEffectivePresentGoods();
                if (cap == 1) {
                    shipFullRow[3][s + 2] = empty();
                    shipFullRow[3][s + 3] = empty();
                    String COLOR;
                    if (effectivePresentGoods2.isEmpty()) {
                        shipFullRow[2][s + 3] = empty();
                    } else {
                        int val = effectivePresentGoods2.getFirst().getValue();
                        COLOR = switch (val) {
                            case 1 -> BLUE;
                            case 2 -> GREEN;
                            case 3 -> YELLOW;
                            default -> BLACK;
                        };
                        shipFullRow[2][s + 3] = block(COLOR);
                    }
                } else { //capacity == 2
                    shipFullRow[3][s + 2] = block(PINK);
                    String COLOR;
                    if (effectivePresentGoods2.isEmpty()) {
                        shipFullRow[2][s + 3] = empty();
                        shipFullRow[3][s + 3] = empty();
                    } else {
                        int val = effectivePresentGoods2.getFirst().getValue();
                        COLOR = switch (val) {
                            case 1 -> BLUE;
                            case 2 -> GREEN;
                            case 3 -> YELLOW;
                            default -> BLACK;
                        };
                        shipFullRow[2][s + 3] = block(COLOR);
                    }
                    if (effectivePresentGoods2.size() > 1) {
                        int val1 = effectivePresentGoods2.get(1).getValue();
                        COLOR = switch (val1) {
                            case 1 -> BLUE;
                            case 2 -> GREEN;
                            case 3 -> YELLOW;
                            default -> BLACK;
                        };
                        shipFullRow[3][s + 3] = block(COLOR);
                    } else {
                        shipFullRow[3][s + 3] = empty();
                    }
                }
                break;
        }
    }

    /**
     * Prepares the ship connectors for the provided tile and updates the given ship rows accordingly.
     * The method determines the type of connectors for each side of the tile and applies the necessary
     * character updates to represent those connectors in the ship's visual structure.
     *
     * @param tile The current SpaceShipTile containing connector information.
     * @param shipFullRow A 2-dimensional array representing the full rows of the spaceship's visual representation.
     * @param s The starting index in the shipFullRow array where the current tile's connectors should be applied.
     */
    private void prepareConnectors(SpaceShipTile tile, String[][] shipFullRow, int s) {
        switch (tile.getConnector(0)) {
            case SINGLE_connector:
                shipFullRow[0][s] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 1] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 2] = getCharacter('|', BLACK);
                shipFullRow[0][s + 3] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 4] = getCharacter(' ', BLACK);
                break;
            case DOUBLE_connector:
                shipFullRow[0][s] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 1] = getCharacter('|', BLACK);
                shipFullRow[0][s + 2] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 3] = getCharacter('|', BLACK);
                shipFullRow[0][s + 4] = getCharacter(' ', BLACK);
                break;
            case UNIV_connector:
                shipFullRow[0][s] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 1] = getCharacter('|', BLACK);
                shipFullRow[0][s + 2] = getCharacter('|', BLACK);
                shipFullRow[0][s + 3] = getCharacter('|', BLACK);
                shipFullRow[0][s + 4] = getCharacter(' ', BLACK);
                break;
            case SMOOTH_SIDE:
                shipFullRow[0][s] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 1] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 2] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 3] = getCharacter(' ', BLACK);
                shipFullRow[0][s + 4] = getCharacter(' ', BLACK);
                break;
        }

        switch (tile.getConnector(2)) {
            case SINGLE_connector:
                shipFullRow[4][s] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 1] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 2] = getCharacter('|', BLACK);
                shipFullRow[4][s + 3] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 4] = getCharacter(' ', BLACK);
                break;
            case DOUBLE_connector:
                shipFullRow[4][s] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 1] = getCharacter('|', BLACK);
                shipFullRow[4][s + 2] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 3] = getCharacter('|', BLACK);
                shipFullRow[4][s + 4] = getCharacter(' ', BLACK);
                break;
            case UNIV_connector:
                shipFullRow[4][s] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 1] = getCharacter('|', BLACK);
                shipFullRow[4][s + 2] = getCharacter('|', BLACK);
                shipFullRow[4][s + 3] = getCharacter('|', BLACK);
                shipFullRow[4][s + 4] = getCharacter(' ', BLACK);
                break;
            case SMOOTH_SIDE:
                shipFullRow[4][s] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 1] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 2] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 3] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 4] = getCharacter(' ', BLACK);
                break;
        }

        switch (tile.getConnector(3)) {
            case SINGLE_connector:
                shipFullRow[0][s] = getCharacter(' ', BLACK);
                shipFullRow[1][s] = getCharacter(' ', BLACK);
                shipFullRow[2][s] = getCharacter('—', BLACK);
                shipFullRow[3][s] = getCharacter(' ', BLACK);
                shipFullRow[4][s] = getCharacter(' ', BLACK);
                break;
            case DOUBLE_connector:
                shipFullRow[0][s] = getCharacter(' ', BLACK);
                shipFullRow[1][s] = getCharacter('—', BLACK);
                shipFullRow[2][s] = getCharacter(' ', BLACK);
                shipFullRow[3][s] = getCharacter('—', BLACK);
                shipFullRow[4][s] = getCharacter(' ', BLACK);
                break;
            case UNIV_connector:
                shipFullRow[0][s] = getCharacter(' ', BLACK);
                shipFullRow[1][s] = getCharacter('—', BLACK);
                shipFullRow[2][s] = getCharacter('—', BLACK);
                shipFullRow[3][s] = getCharacter('—', BLACK);
                shipFullRow[4][s] = getCharacter(' ', BLACK);
                break;
            case SMOOTH_SIDE:
                shipFullRow[0][s] = getCharacter(' ', BLACK);
                shipFullRow[1][s] = getCharacter(' ', BLACK);
                shipFullRow[2][s] = getCharacter(' ', BLACK);
                shipFullRow[3][s] = getCharacter(' ', BLACK);
                shipFullRow[4][s] = getCharacter(' ', BLACK);
                break;
        }

        switch (tile.getConnector(1)) {
            case SINGLE_connector:
                shipFullRow[0][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[1][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[2][s + 4] = getCharacter('—', BLACK);
                shipFullRow[3][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 4] = getCharacter(' ', BLACK);
                break;
            case DOUBLE_connector:
                shipFullRow[0][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[1][s + 4] = getCharacter('—', BLACK);
                shipFullRow[2][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[3][s + 4] = getCharacter('—', BLACK);
                shipFullRow[4][s + 4] = getCharacter(' ', BLACK);
                break;
            case UNIV_connector:
                shipFullRow[0][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[1][s + 4] = getCharacter('—', BLACK);
                shipFullRow[2][s + 4] = getCharacter('—', BLACK);
                shipFullRow[3][s + 4] = getCharacter('—', BLACK);
                shipFullRow[4][s + 4] = getCharacter(' ', BLACK);
                break;
            case SMOOTH_SIDE:
                shipFullRow[0][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[1][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[2][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[3][s + 4] = getCharacter(' ', BLACK);
                shipFullRow[4][s + 4] = getCharacter(' ', BLACK);
                break;
        }
    }


    /**
     * Prints the visual representation of a spaceship tile to the console.
     *
     * @param tile the SpaceShipTile object representing the tile to be printed
     */
    public void printTile(SpaceShipTile tile) {
        String[][] shipFullRow = new String[5][5];
        prepareTileVisual(tile, shipFullRow, 0);
        System.out.println("┌───────────────┐");
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (col == 0) {
                    System.out.print('│');
                }
                System.out.print(" " + shipFullRow[row][col] + " ");
            }
            System.out.print("│\n");
        }
        System.out.println("└───────────────┘");
    }

    /**
     * Displays a graphical representation of an array of tiles, formatted into rows
     * with a maximum number of tiles per row. Each tile is represented visually
     * along with its identifier centered above it.
     *
     * @param tiles an ArrayList of SpaceShipTile objects representing the tiles
     *              to be displayed; if null or empty, a message indicating no tiles
     *              will be displayed
     */
    public void printTilesArray(ArrayList<SpaceShipTile> tiles) {
        if (tiles == null || tiles.isEmpty()) {
            System.out.println("No tiles to display");
            return;
        }

        int tilesPerRow = 6; // Maximum number of tiles per row
        int tilesRemaining = tiles.size();
        int currentIndex = 0;

        while (tilesRemaining > 0) {
            // Calculate how many tiles to display in this row
            int tilesToShow = Math.min(tilesPerRow, tilesRemaining);

            // Print IDs centered over each tile
            for (int i = 0; i < tilesToShow; i++) {
                SpaceShipTile tile = tiles.get(currentIndex + i);
                int id = tile.getID();
                String idString = String.valueOf(id);

                // Each tile display is 17 characters wide (┌───────────────┐)
                // Center the ID in this space
                int padding = (17 - idString.length()) / 2;

                // Print spaces before ID
                for (int p = 0; p < padding; p++) {
                    System.out.print(" ");
                }

                // Print ID
                System.out.print(idString);

                // Print spaces after ID
                for (int p = 0; p < padding; p++) {
                    System.out.print(" ");
                }

                // Add spacing between tiles
                System.out.print("  ");
            }
            System.out.println();

            // Create array for this row of tiles
            String[][] combinedTiles = new String[5][tilesToShow * 5];

            // Prepare visuals for tiles in this row
            for (int i = 0; i < tilesToShow; i++) {
                SpaceShipTile tile = tiles.get(currentIndex + i);
                int startCol = i * 5;
                prepareTileVisual(tile, combinedTiles, startCol);
            }

            // Print top border for this row
            for (int i = 0; i < tilesToShow; i++) {
                System.out.print("┌───────────────┐  ");
            }
            System.out.println();

            // Print tile content for this row
            for (int row = 0; row < 5; row++) {
                for (int tileIndex = 0; tileIndex < tilesToShow; tileIndex++) {
                    int startCol = tileIndex * 5;

                    System.out.print("│"); // Left border

                    // Print the content of this tile for the current row
                    for (int col = 0; col < 5; col++) {
                        System.out.print(" " + combinedTiles[row][startCol + col] + " ");
                    }

                    System.out.print("│  "); // Right border with spacing
                }
                System.out.println();
            }

            // Print bottom border for this row
            for (int i = 0; i < tilesToShow; i++) {
                System.out.print("└───────────────┘  ");
            }
            System.out.println("\n"); // Extra line between rows of tiles

            // Update remaining tiles and current index
            tilesRemaining -= tilesToShow;
            currentIndex += tilesToShow;
        }
    }

    /**
     * Prints a formatted list of available commands along with their descriptions and usage instructions.
     *
     * @param commands a list of CommandType instances representing the commands to be displayed. Each command
     *                 includes a code, description, and optional usage examples or rules to assist the user.
     */
    public void printCommand(List<CommandType> commands) {
        final String RESET = "\u001B[0m";
        final String LIGHT_AZURE = "\u001B[38;5;117m";
        final String BOLD = "\u001B[1m";
        final String WHITE = "\u001B[37m";
        final String YELLOW = "\u001B[33m";
        System.out.println();
        List<List<String>> cmdLines = new ArrayList<>();
        for (CommandType cmd : commands) {
            String desc = switch (cmd) {
                case CREATE_GAME -> "CREATE A GAME -- Not implemented in this command list.";
                case JOIN_GAME ->
                        "JOIN A GAME -- Insert 1, Nickname and number of players.\nInput should be like this << 1 TestName 4 >>";
                case ACTIVATE_TIMER -> "ACTIVATE TIMER -- Insert 2";
                case SET_NUM_TILE ->
                        "SET THE NUM OF THE TILE TO REMOVE -- Insert 3, 0 for the first option, 1 for the second.\nInput should be like this << 3 1 >>";
                case SURREND -> "SURREND -- Insert 4";
                case VISUALIZE_FINAL_SCORES -> "VISUALIZE FINAL SCORES -- Insert 5";
                case PICK_TILE_ALREADY_FLIPPED ->
                        "PICK A TILE ALREADY FLIPPED -- Insert 6, followed by the number of the tile.\nInput should be like this << 6 37 >>";
                case PICK_TILE_UNKNOWN -> "PICK AN UNKNOWN TILE -- Insert 7";
                case PICK_LITTLE_DECK ->
                        "PICK LITTLE DECK -- Insert 8, followed by the index of the little deck you want to pick.\nInput should be like this << 8 1 >>. Little decks are indexed from 0 to 2.";
                case DEPOSIT_LITTLE_DECK -> "DEPOSIT LITTLE DECK IN HAND -- Insert 9";
                case DEPOSIT_TILE -> "DEPOSIT TILE IN HAND -- Insert 10";
                case END_BUILDING ->
                        "END BUILDING -- Insert 11, followed by the starting position you want to have.\nInput should be like this << 11 2 >>";
                case INSERT_TILE ->
                        "INSERT A TILE YOU HAVE IN HAND -- Insert 12, followed by ROW, COLUMN and ROTATION you want to the tile you've picked at. Rotation should be 0, 90, 180, 270.\nInput should be like this << 12 6 10 270 >>";
                case EFFECT_ACTIVATION -> "ACTIVATE THE EFFECT OF THE NEXT CARD -- Insert 13";
                case GET_CARD_IN_USE -> "GET CARD IN USE -- Insert 14";
                case CHOOSE_ONE_SUB_SHIP ->
                        "CHOOSE ONE SUBSHIP TO PRESERVE -- Insert 15, followed by the index of the subship.\nInput should be like this << 15 1 >>";
                case ACCEPT_TO_LAND_ON_A_PLANET ->
                        "ACCEPT TO LAND ON A PLANET -- Insert 16, then 0 to not land, 1 to land.\nIf you choose to land, insert also the number of the planet you've chosen.\nInput should be like this << 16 1 3 >> or << 16 0 >>";
                case CHOOSE_ABANDONED_STATION ->
                        "CHOOSE ABANDONED STATION -- Insert 17, then 0 to not land on the abandoned station, 1 to land.\nIf you choose to land, insert how you want ALL your goods arranged after the card effect.\nGoods arrangement is like this: (x,y)(z,k)... then (1,4)(3).\nInput should be like this << 17 1 (5,8)(7,6) (3,1)(4) >>";
                case CHOOSE_CANNON_BATTERY_POS ->
                        "CHOOSE WHICH ELEMENTS TO ACTIVATE AND WITH WHICH BATTERIES -- Insert 18, followed by row and column of the elements, then row, columns and number of batteries.\nInput: << 18 (4,5)(7,8) (4,7,2) >>";
                case CHOOSE_HOW_TO_FACE_METEORS ->
                        "CHOOSE HOW TO FACE SHOT -- Insert 19, followed by row and column of the element and of where to remove the battery.\nInput: << 19 3 5 7 9 >> or just << 19 >>";
                case CHOOSE_PASSENGERS_TO_LOSE ->
                        "CHOOSE PASSENGERS TO LOSE -- Insert 20, then 0/1 and storage unit info. Example: << 20 1 (4,5,2)(6,8,1) >>";
                case CHOOSE_TO_CLAIM_REWARD -> "CHOOSE TO CLAIM REWARDS -- Insert 21, then 0 or 1";
                case CHOOSE_TO_CLAIM_REWARD_WITH_GOODS ->
                        "CHOOSE TO CLAIM REWARDS WITH GOODS -- Insert 22 1 and goods arrangement.\nExample: << 22 1 (5,8)(7,6) (3,1)(4) >>";
                case CHOOSE_TO_PLACE_BATTERIES ->
                        "CHOOSE TO PLACE BATTERIES -- Insert 23 followed by units and number. Example: << 23 (4,5,2)(6,8,1) >>";
                case CHOOSE_TO_START_FIRE_POWER ->
                        "CHOOSE TO START FIRE POWER -- Insert 24, components and battery units. Example: << 24 (4,5)(7,8) (4,7,2) >>";
                case CHOOSE_TO_START_MOTOR ->
                        "CHOOSE TO START MOTOR -- Insert 25 and components. Example: << 25 (4,5)(7,8) (4,7,2) >>";
                case CHOOSE_WHERE_TO_PUT_GOODS ->
                        "CHOOSE WHERE TO PUT GOODS -- Insert 26 followed by goods arrangement. Example: << 26 (x,y)(z,k) (1,4)(3) >>";
                case ADD_WAIT_TILE -> "PUT THE TILE YOU HAVE IN HAND ON HOLD -- Insert 27";
                case INSERT_WAIT_TILE ->
                        "INSERT A WAIT TILE IN YOUR SHIP -- Insert 28 followed by index, row, column and rotation.";
                case COMMAND_FILL_TILE ->
                        "CHOOSE IF YOU WANT AN ALIEN IN THIS POSITION -- Insert 29 followed by 0/1, row, column, and alien color if 1.  \nClick 1 for BROWN, 0 for VIOLET. \nExample: << 29 1 5 7 1 >>";
                case ACTIVE_GAMES -> "SHOW ACTIVE GAMES -- Not detailed here.";
                case DEMO -> "RUN DEMO -- Not detailed here.";
            };

            String key = BOLD + WHITE + String.format("%02d", CommandTypeMapper.getCode(cmd)) + RESET;
            String[] lines = desc.split("\n");
            List<String> wrapped = new ArrayList<>();
            for (int i = 0; i < lines.length; i++) {
                String prefix = (i == 0) ? ">> [" + key + "] " : "       ";
                wrapped.add(prefix + LIGHT_AZURE + lines[i] + RESET);
            }
            cmdLines.add(wrapped);
        }

        int maxVisibleWidth = cmdLines.stream()
                .flatMap(List::stream)
                .mapToInt(line -> stripAnsi(line).length())
                .max()
                .orElse(50);
        maxVisibleWidth = Math.max(maxVisibleWidth, 50);

        String top = "╔" + "═".repeat(maxVisibleWidth + 4) + "╗" + RESET;
        String bottom = "╚" + "═".repeat(maxVisibleWidth + 4) + "╝" + RESET;
        String empty = "║" + " ".repeat(maxVisibleWidth + 4) + "║" + RESET;

        String decoPart = YELLOW + "✦✧✦✧✦" + RESET;
        String titleText = WHITE + BOLD + "AVAILABLE COMMANDS" + RESET;
        int decoVisibleLen = stripAnsi(decoPart).length();
        int titleVisibleLen = stripAnsi(titleText).length();
        int titleTotalLen = decoVisibleLen * 2 + titleVisibleLen + 4; // 2 spazi prima e dopo il titolo
        int padLeft = (maxVisibleWidth + 4 - titleTotalLen) / 2;
        int padRight = maxVisibleWidth + 4 - titleTotalLen - padLeft;
        String titleLine = "║"
                + " ".repeat(padLeft)
                + decoPart + "  " + titleText + "  " + decoPart
                + " ".repeat(padRight)
                + "║" + RESET;
        System.out.println(top);
        System.out.println(empty);
        System.out.println(titleLine);
        System.out.println(empty);
        for (List<String> block : cmdLines) {
            for (String line : block) {
                String visibleLine = stripAnsi(line);
                int spacesNeeded = maxVisibleWidth - visibleLine.length();
                String paddedLine = line + " ".repeat(spacesNeeded);
                System.out.println("║  " + paddedLine + "  ║" + RESET);
            }
        }
        System.out.println(empty);
        System.out.println(bottom);
    }

    /**
     * Removes ANSI escape codes from a given string.
     * These escape codes are often used for terminal text formatting, such as colorization,
     * but may be undesirable in certain contexts (e.g., plain-text output).
     *
     * @param s the input string potentially containing ANSI escape codes
     * @return a string with the ANSI escape codes removed
     */
    private String stripAnsi(String s) {
        return s.replaceAll("\u001B\\[[\\d;]*m", "");
    }


    /**
     * Prints a supporting hand ASCII art and a given nickname.
     *
     * @param nickname the nickname to be displayed below the supporting hand ASCII art
     */
    public static void printSupportingHand(String nickname) {
        String[] hand = new String[]{
                " ||    ||  ||  ||               ",
                " ||    ||  ||  ||   ||             ",
                "(||)  (||)(||)(||)  ||           ",
                " ||    ||  ||  ||  (||)             ",
                " \\   ||  ||  ||   //           ",
                "  \\__||__||__||__//            ",
                "      |         |               ",
                "      |_________|               ",
                "     /           \\              ",
                "    |_____________|             "
        };

        for (String line : hand) {
            System.out.println(line);
        }
        System.out.println(nickname);

    }


    /**
     * Prints the details of the object the player is currently holding in hand, based on its type.
     * The method supports printing information for either a {@code Deck} of cards or a {@code SpaceShipTile}.
     * If the object is null, an appropriate message will be displayed.
     *
     * @param ThingInHand the object currently in the player's hand, which can be either a {@code Deck} or {@code SpaceShipTile}
     * @param playerNickname the nickname of the player whose hand information is being processed
     * @param lmr the little model representation used to provide additional data for processing the object in hand
     */
    public void printThingInHand(Object ThingInHand, String playerNickname, LittleModelRepresentation lmr) {
        if (ThingInHand != null) {
            if (ThingInHand instanceof Deck) {
                for (Card c : ((Deck) ThingInHand).getCardList()) {
                    printCard(c, lmr);
                    printSupportingHand(playerNickname);
                }
            } else if (ThingInHand instanceof SpaceShipTile) {
                printTile((SpaceShipTile) ThingInHand);
                printSupportingHand(playerNickname);
            }
        } else {
            System.out.println("You have nothing in hand");
        }

    }
}