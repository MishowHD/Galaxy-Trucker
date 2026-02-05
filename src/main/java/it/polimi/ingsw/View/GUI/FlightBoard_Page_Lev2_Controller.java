package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Model.Tiles.Utils_Tiles.AlienColor;
import it.polimi.ingsw.Model.Cards.Card;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Deck;
import it.polimi.ingsw.Model.Cards.Utils_Cards.Shot;
import it.polimi.ingsw.Utils.Goods;
import it.polimi.ingsw.Network.Client.GenericClient;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.LittleModelRepresentation;
import it.polimi.ingsw.Utils.OldGame;
import it.polimi.ingsw.Model.Player.Player;
import it.polimi.ingsw.Model.Boards.ShipBoard;
import it.polimi.ingsw.Model.Tiles.SpaceShipTile;
import it.polimi.ingsw.View.Utils_View.JfxMediaUtils;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FlightBoard_Page_Lev2_Controller implements Initializable, GeneralView {

    /**
     * Represents a tab pane used to manage and display multiple ship-related views
     * or sections in the flight board interface.
     * This component is part of the FXML layout and is primarily used to organize ship-related tabs.
     */
    @FXML
    private TabPane shipTabPane;

    private LittleModelRepresentation lmr;
    /**
     * A reference to the GUI view associated with the controller.
     * This variable is used to manage and interact with the graphical user interface
     * for the FlightBoard_Page_Lev2_Controller.
     */
    private GUI view;
    /**
     * Represents the current player's identification or name in the context
     * of the FlightBoard_Page_Lev2_Controller class.
     * This variable stores the name or identifier of the player currently interacting with
     * the flight board or associated components.
     */
    private String player;

    /**
     * Sets the name of the player.
     *
     * @param player the name of the player to be set
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * A static volatile variable representing the current instance of
     * PlayerShipBoard_Page_Lev2_Controller associated with this class.
     * This variable is used to globally keep track of the active controller
     * instance in a thread-safe manner.
     */
    public volatile static PlayerShipBoard_Page_Lev2_Controller currentController;

    /**
     * Sets the current controller for the PlayerShipBoard_Page_Lev2_Controller.
     *
     * @param controller the PlayerShipBoard_Page_Lev2_Controller to be set as the current controller
     */
    public static void setCurrentController(PlayerShipBoard_Page_Lev2_Controller controller) {
        currentController = controller;
    }

    /**
     * Retrieves the current controller for the player's ship board at level 2.
     *
     * @return the current instance of PlayerShipBoard_Page_Lev2_Controller
     */
    public static PlayerShipBoard_Page_Lev2_Controller getCurrentController() {
        return currentController;
    }

    /**
     * A mapping of player nicknames to their corresponding PlayerShipBoard_Page_Lev2_Controller instances.
     * Used to manage and associate individual player interactions with their specific controller objects
     * in the context of the flight board view.
     */
    private final Map<String, PlayerShipBoard_Page_Lev2_Controller> playerControllers = new HashMap<>();

    /**
     * Handles the action of ending a player's turn in the game.
     *
     * This method is triggered when the corresponding UI element is interacted with.
     * It performs necessary logic or updates to transition to the next player's turn
     * or progresses the game state beyond the current player's turn.
     *
     * The specific actions and flow depend on the game state and the implementation
     * details of the controller. It may involve updating the game model, notifying
     * other players in a multiplayer setting, or refreshing the user interface.
     */
    @FXML
    private void handleEndTurn() {
    }

    /**
     * Represents an ImageView component used to display the flightboard in the user interface.
     * This field is linked to its corresponding element in the FXML layout file.
     * It provides functionalities for displaying and manipulating the flightboard image within the application's graphical interface.
     * This component may be dynamically updated during gameplay to reflect the state of the flightboard.
     */
    @FXML
    private ImageView flightboardImageView;


    /**
     * Represents a button in the user interface for selecting the first little deck.
     * Defined in the FlightBoard_Page_Lev2_Controller class via FXML.
     * Used to trigger the corresponding action when the button is clicked.
     */
    // Bottoni definiti in FXML
    @FXML
    private Button pickLittleDeckButton1;
    /**
     * FXML Button instance representing the second option to pick a "Little Deck."
     *
     * This button is part of the graphical user interface and is associated with the
     * method {@code handlePickLittleDeck2()} to perform the action of selecting the
     * second "Little Deck" during gameplay or interaction. It is specifically tied
     * to the FlightBoard_Page_Lev2_Controller class.
     *
     * Usage context:
     * - Handles user input to pick a specific deck from the available choices.
     * - Participates in the player's actions within the game screen.
     */
    @FXML
    private Button pickLittleDeckButton2;
    /**
     * Represents the third button in the UI responsible for selecting the "Little Deck."
     * This button is defined in the FXML file and is associated with an event handler
     * that triggers functionality related to handling the selection of a Little Deck.
     */
    @FXML
    private Button pickLittleDeckButton3;
    /**
     * The root StackPane serving as the main container for the flight board page.
     * It organizes its child nodes in a stack, enabling overlapping of elements.
     * This layout structure is commonly used for layering elements, such as
     * background videos, images, and interactive components in the view.
     */
    @FXML
    private StackPane rootStack;

    /**
     * Represents a MediaView component used to display a background video within the UI.
     * This field is a part of the flight board level 2 controller in the application.
     * It is initialized and utilized to provide visual media enhancements in the application's interface.
     * The actual media content can be dynamically managed by the controller's logic.
     */
    @FXML
    private MediaView backgroundVideo;
    /**
     * Represents an ImageView element linked to the graphical representation
     * in one of the buttons within the user interface.
     * Used to visually enhance button components with images.
     * This field is annotated with @FXML to bind it to the corresponding
     * component defined in the FXML layout.
     */
    // ImageView “graphics” all’interno dei bottoni
    @FXML
    private ImageView buttonImage1;
    /**
     * An ImageView control used for displaying the second button image
     * on the FlightBoard_Page_Lev2_Controller interface. This component
     * may be dynamically updated or styled to interact with the user interface
     * elements of the controller, typically representing a button-related image.
     */
    @FXML
    private ImageView buttonImage2;
    /**
     * An ImageView component representing the third button in the user interface.
     * It is associated with the FXML layout and may display an image for a button functionality.
     * This variable is typically utilized within the controller to interact with the image view element in the UI.
     */
    @FXML
    private ImageView buttonImage3;
    /**
     * An ImageView component representing the eleventh button image in the UI.
     * It is likely used for displaying an image or icon associated with a specific button
     * in the second-level flight board view of the application.
     *
     * This field is annotated with @FXML, indicating that it is associated with a
     * corresponding element defined in the FXML file for the FlightBoard_Page_Lev2_Controller.
     */
    @FXML
    private ImageView buttonImage11;

    /**
     * The container used for displaying and managing animation-related elements in the user interface.
     * It serves as a graphical placeholder for animated components or effects within the application.
     */
    @FXML
    private StackPane animationContainer;
    /**
     * Represents a JavaFX ImageView component used for displaying an animated image.
     * This variable is defined in the FXML file associated with the controller and is
     * injected at runtime. It may be used as part of visual animations or other UI
     * elements in the application's flight board interface.
     */
    @FXML
    private ImageView animatedImage1;
    /**
     *
     */
    @FXML
    private ImageView animatedImage2;
    /**
     * Represents the third animated image in the flight board interface.
     * This ImageView is primarily used for rendering graphical elements
     * with animation effects in the application's UI.
     * It is controlled and modified as part of specific animations or UI updates
     * in the FlightBoard_Page_Lev2_Controller.
     */
    @FXML
    private ImageView animatedImage3;
    /**
     * Represents the close button in the FlightBoard_Page_Lev2_Controller.
     * This button is used in the user interface for handling close-related actions.
     * It is linked to an FXML definition for UI binding and functionality.
     */
    @FXML
    private Button closeButton;
    /**
     * Represents the first ImageView in a graphical implementation of an elliptical layout.
     * This ImageView is one element in an array of 24 ImageView components that form the visual
     * representation of an ellipse, used within the FlightBoard_Page_Lev2_Controller class.
     *
     * It is included as part of the user interface managed by JavaFX and is initialized
     * and manipulated within the application to achieve animations or changes in the visual state.
     */
    // Array per gestire le 24 ImageView dell'ellisse
    @FXML
    private ImageView ellipseImage0, /**
     * Represents an ellipse-shaped ImageView UI component used within the FlightBoard_Page_Lev2_Controller class.
     * Typically managed and manipulated through various methods for display, visibility, rotation, and opacity adjustments.
     */
    ellipseImage1, /**
     * Represents an image view component used in the user interface for displaying
     * a specific ellipse image in the flight board section of the application.
     * Typically, this object is manipulated to update, hide, show, or rotate its
     * associated image.
     *
     * This image view is a part of the collection of ellipse images used within
     * the flight board UI and is identified by its specific variable instance among others (e.g., `ellipseImage0`, `ellipseImage1`, etc.).
     */
    ellipseImage2, /**
     * Represents the third ellipse image displayed on the flight board.
     * This image is part of a collection of ellipse images used in the GUI
     * to indicate specific visual elements or statuses.
     *
     * It can be manipulated through methods such as setting its image,
     * changing its opacity, rotating it, or hiding and showing it programmatically.
     */
    ellipseImage3, /**
     * A field representing a specific elliptical image view used within the graphical user interface
     * of the flight board.
     *
     * This variable is part of a collection of similar ellipse image views, which are designed for
     * graphical purposes, including animations and interactions within the flight board context.
     *
     * It can be manipulated or updated using various methods in the containing class, such as setting
     * a specific image, hiding or showing it, changing its opacity, or applying a rotational effect.
     */
    ellipseImage4, /**
     * Represents the fifth image in a series of ellipse images associated with the flightboard.
     * This variable is used within the FlightBoard_Page_Lev2_Controller class to manage a specific
     * ImageView element related to the UI representation of the game.
     *
     * It is part of the ellipseImages array and can be dynamically manipulated, such as being
     * shown, hidden, rotated, or having its properties updated based on game interactions.
     */
    ellipseImage5;
    /**
     * An FXML-mapped field representing the sixth elliptical image element in the FlightBoard_Page_Lev2_Controller.
     * This ImageView is likely included in a collection of ellipse image views (e.g., `ellipseImages`) and can be controlled
     * or modified through the respective methods in the class. It may represent a specific UI element tied to the display of
     * images or animations within the flight board interface of the application.
     */
    @FXML
    private ImageView ellipseImage6, /**
     * The `ellipseImage7` variable represents the seventh ellipse-shaped graphical element in the
     * `FlightBoard_Page_Lev2_Controller`. It is used to display or modify specific graphical elements
     * on the flight board within the user interface.
     *
     * This variable is likely associated with an `ImageView` or similar graphical component
     * and contributes to managing the visual state or animations of the interface.
     */
    ellipseImage7, /**
     * Represents the eighth ellipse-shaped image used within the flight board controller.
     * This variable is part of a collection of ellipse images, likely utilized for UI purposes
     * such as displaying indicators, animations, or graphical elements on the flight board.
     * The intended behavior and characteristics of this image are defined and manipulated
     * through methods within the `FlightBoard_Page_Lev2_Controller` class.
     */
    ellipseImage8, /**
     * Represents the ninth ellipse image in the sequence of ellipse images within the FlightBoard_Page_Lev2_Controller.
     * It is typically used for displaying visual elements related to the application's flight board functionality.
     */
    ellipseImage9, /**
     * Represents the ImageView object used to display a specific ellipse-shaped visual element
     * in the FlightBoard_Page_Lev2_Controller class. This variable is part of a series of
     * ellipse images (e.g., ellipseImage10) used for graphical purposes or tracked states
     * within the flight board interface.
     */
    ellipseImage10, /**
     * Represents an image view associated with the eleventh ellipse in the flight board UI.
     * Used as part of the controller to manage visual elements, such as transformations,
     * animations, or visibility related to the eleventh ellipse.
     */
    ellipseImage11;
    /**
     * Represents the 12th ellipse image in the flightboard interface.
     * This image view can display an ellipse-shaped graphical element
     * as part of the flightboard's visual components. It may be
     * manipulated or updated to reflect changes in the flightboard's
     * state or animations.
     *
     * Used within {@code FlightBoard_Page_Lev2_Controller}.
     * Can be accessed or modified to manage the corresponding
     * ellipse element in the UI.
     */
    @FXML
    private ImageView ellipseImage12, /**
     * Represents the thirteenth ellipse-shaped image in the flight board user interface.
     * This {@code ImageView} is one of a set of ellipse images used for graphical displays or animations.
     * It can be manipulated programmatically using various methods from its containing controller class,
     * such as setting its image, updating its opacity, rotating it, or showing/hiding it as needed.
     */
    ellipseImage13, /**
     *
     */
    ellipseImage14, /**
     * Represents the 15th elliptical image used within the FlightBoard_Page_Lev2_Controller.
     * Typically utilized as part of a series of graphical elements for visual representation,
     * potentially configurable and interactive via associated methods.
     */
    ellipseImage15, /**
     * Represents the sixteenth ellipse image element managed within the flight board page controller.
     * This variable is part of an array of ImageView objects used for visual elements
     * in the application's graphical interface.
     */
    ellipseImage16, /**
     * Represents the 17th ellipse image component used in the graphical user interface
     * of the FlightBoard_Page_Lev2_Controller. This variable is likely related to
     * visual elements and can be manipulated or displayed using the relevant methods
     * available in the controller's class. It may be part of an array or collection
     * of ellipse images used for specific graphical purposes.
     */
    ellipseImage17;
    /**
     * Represents the eighteenth ellipse-shaped ImageView in the flight board widget.
     * It is used to display visual elements such as images or animations related to
     * the game board's state or events.
     *
     * Variable is associated with the FXML view and can be manipulated programmatically
     * for visual updates or interactions tied to the game logic.
     */
    @FXML
    private ImageView ellipseImage18, /**
     * Represents the 19th image in a series of ellipse images displayed within the user interface.
     * It is likely part of a collection used to depict different states, animations, or visual elements
     * on the flight board.
     */
    ellipseImage19, /**
     * Represents the 21st elliptical image component in the flight board page level 2 controller.
     * This variable is likely an `ImageView` object used to display a specific image
     * within the user interface. The exact usage is determined by the associated
     * methods in the controller class.
     */
    ellipseImage20, /**
     *
     */
    ellipseImage21, /**
     *
     */
    ellipseImage22, /**
     * A variable that represents the 23rd ellipse image in the flight board page level 2 controller.
     * It is used in the graphical interface to handle an image or display related functionalities.
     * Typically utilized in methods for setting, showing, hiding, or animating ellipse images.
     */
    ellipseImage23;

    /**
     * An array of {@link ImageView} objects used to represent a pre-defined set of ellipse images.
     * The array has a fixed size of 24 elements.
     * Each element in this array can be manipulated individually, enabling functionalities such as
     * setting an image, adjusting opacity, rotating, hiding, or showing specific ellipses.
     */
    private final ImageView[] ellipseImages = new ImageView[24];

    /**
     * Initializes the `ellipseImages` array by assigning each element to its corresponding
     * pre-defined image variable. Each index in the `ellipseImages` array is associated
     * with a specific `ellipseImage` variable, from `ellipseImage0` to `ellipseImage23`.
     *
     * This method ensures that all elements in the `ellipseImages` array are properly
     * populated with their respective image references.
     */
    private void initializeEllipseImagesArray() {
        ellipseImages[0] = ellipseImage0;
        ellipseImages[1] = ellipseImage1;
        ellipseImages[2] = ellipseImage2;
        ellipseImages[3] = ellipseImage3;
        ellipseImages[4] = ellipseImage4;
        ellipseImages[5] = ellipseImage5;
        ellipseImages[6] = ellipseImage6;
        ellipseImages[7] = ellipseImage7;
        ellipseImages[8] = ellipseImage8;
        ellipseImages[9] = ellipseImage9;
        ellipseImages[10] = ellipseImage10;
        ellipseImages[11] = ellipseImage11;
        ellipseImages[12] = ellipseImage12;
        ellipseImages[13] = ellipseImage13;
        ellipseImages[14] = ellipseImage14;
        ellipseImages[15] = ellipseImage15;
        ellipseImages[16] = ellipseImage16;
        ellipseImages[17] = ellipseImage17;
        ellipseImages[18] = ellipseImage18;
        ellipseImages[19] = ellipseImage19;
        ellipseImages[20] = ellipseImage20;
        ellipseImages[21] = ellipseImage21;
        ellipseImages[22] = ellipseImage22;
        ellipseImages[23] = ellipseImage23;
    }

    /**
     * Sets the image for an ellipse at the specified index using the provided image path.
     * If the index is within the valid range (0 to 23) and the image loading is successful,
     * the ellipse image will be updated and made visible. If an error occurs while loading
     * the image, an error message will be printed to the console.
     *
     * @param index The index of the ellipse to update. Must be between 0 and 23 inclusive.
     * @param imagePath The path to the image resource to be set for the specified ellipse.
     */
    public void setEllipseImage(int index, String imagePath) {
        if (index >= 0 && index < 24) {
            try {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                ellipseImages[index].setImage(image);
                ellipseImages[index].setVisible(true);
            } catch (Exception e) {
                System.err.println("Errore nel caricare l'immagine: " + imagePath);
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the image for an ellipse at the specified index and makes it visible.
     *
     * @param index the index of the ellipse image to be updated; must be between 0 and 23 (inclusive)
     * @param image the new image to set for the specified ellipse
     */
    public void setEllipseImage(int index, Image image) {
        if (index >= 0 && index < 24 && ellipseImages[index] != null) {
            ellipseImages[index].setImage(image);
            ellipseImages[index].setVisible(true);
        }
    }

    /**
     * Hides the ellipse image at the specified index by setting its visibility to false and its image to null.
     *
     * @param index the index of the ellipse image to hide, must be between 0 (inclusive) and 23 (inclusive)
     *              and correspond to a non-null ellipse image.
     */
    public void hideEllipseImage(int index) {
        if (index >= 0 && index < 24 && ellipseImages[index] != null) {
            ellipseImages[index].setVisible(false);
            ellipseImages[index].setImage(null);
        }
    }

    /**
     * Displays an ellipse image at the specified index if it exists and is not null.
     *
     * @param index the index of the ellipse image to be displayed. Must be between 0 and 23 (inclusive).
     */
    public void showEllipseImage(int index) {
        if (index >= 0 && index < 24 && ellipseImages[index] != null && ellipseImages[index].getImage() != null) {
            ellipseImages[index].setVisible(true);
        }
    }

    /**
     *
     */
    public void hideAllEllipseImages() {
        for (int i = 0; i < 24; i++) {
            hideEllipseImage(i);
        }
    }

    /**
     * Retrieves the ImageView object representing an ellipse at the specified index.
     *
     * @param index the index of the ellipse image to retrieve; valid range is 0 to 23.
     * @return the ImageView at the given index if within a valid range, or null if the index is out of bounds.
     */
    public ImageView getEllipseImageView(int index) {
        if (index >= 0 && index < 24) {
            return ellipseImages[index];
        }
        return null;
    }

    /**
     * Sets the opacity of the ellipse image at the specified index.
     *
     * @param index the index of the ellipse image to modify, must be between 0 and 23 inclusive
     * @param opacity the opacity value to set, typically between 0.0 (fully transparent) and 1.0 (fully opaque)
     */
    public void setEllipseImageOpacity(int index, double opacity) {
        if (index >= 0 && index < 24 && ellipseImages[index] != null) {
            ellipseImages[index].setOpacity(opacity);
        }
    }

    /**
     * Rotates the specified ellipse image by a given angle.
     *
     * @param index the index of the ellipse image to rotate. Must be between 0 and 23, inclusive.
     * @param angle the angle in degrees by which the ellipse image will be rotated.
     */
    public void rotateEllipseImage(int index, double angle) {
        if (index >= 0 && index < 24 && ellipseImages[index] != null) {
            ellipseImages[index].setRotate(angle);
        }
    }


    /**
     *
     */
    private void showImagesWithAnimation() {
        try {
            animationContainer.setVisible(true);
            animationContainer.setOpacity(1.0); // Assicura opacità piena

            // Resetta stato iniziale
            animatedImage1.setOpacity(1.0);
            animatedImage2.setOpacity(1.0);
            animatedImage3.setOpacity(1.0);

            // Resetta le rotazioni iniziali
            animatedImage1.setRotate(0);
            animatedImage2.setRotate(0);
            animatedImage3.setRotate(0);

            // Crea le animazioni
            RotateTransition rt1 = createRotation(animatedImage1);
            RotateTransition rt2 = createRotation(animatedImage2);
            RotateTransition rt3 = createRotation(animatedImage3);

            // Mostra le immagini con animazioni sequenziali
            SequentialTransition seqTransition = new SequentialTransition(
                    new ParallelTransition(
                            new FadeTransition(Duration.millis(500), animatedImage1),
                            new FadeTransition(Duration.millis(500), animatedImage2),
                            new FadeTransition(Duration.millis(500), animatedImage3)
                    ),
                    new ParallelTransition(rt1, rt2, rt3)
            );

            seqTransition.play();
        } catch (Exception e) {
            e.printStackTrace();
            animationContainer.setVisible(false);
        }
    }

    /**
     * Creates a RotateTransition animation for the specified ImageView.
     * The animation rotates the image by 360 degrees, with a duration of 1 second,
     * a single cycle, and auto-reverse enabled.
     *
     * @param image The ImageView to which the rotation animation will be applied.
     * @return A RotateTransition instance configured with the specified properties.
     */
    private RotateTransition createRotation(ImageView image) {
        image.setVisible(true);
        RotateTransition rt = new RotateTransition(Duration.millis(1000), image);
        rt.setByAngle(360);
        rt.setCycleCount(1);
        rt.setAutoReverse(true);
        return rt;
    }

    /**
     * Handles the close animation process, including server communication
     * and fade transitions for UI elements. This method sends a command to
     * deposit a little deck for the current player and plays a series of animations
     * to hide visual elements, eventually resetting the animation state.
     *
     * It performs the following steps:
     * 1. Sends a command to the server to deposit the little deck associated
     *    with the current player via a client request.
     * 2. Initializes and configures fade and rotational animations for
     *    various UI elements to transition smoothly out of view.
     * 3. Executes the animations in parallel and disables the animation
     *    container after completion.
     */
    @FXML
    private void handleCloseAnimation() {
        try {
            // Invia il comando prima di iniziare l'animazione
            GenericClient client = GUI.getClient();
            int id = -1;
            for (ShipBoard s : GUI.getModel().getShipBoards()) {
                if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                    id = s.getMyPlayer().getPlayerId();
                    break;
                }
            }
            client.depositLittleDeck(id, client.getUuid());
        } catch (Exception e) {
            e.printStackTrace();
            // Gestisci l'eccezione se necessario
        }

        // Animazione di chiusura
        FadeTransition fadeOut1 = new FadeTransition(Duration.millis(500), animatedImage1);
        FadeTransition fadeOut2 = new FadeTransition(Duration.millis(500), animatedImage2);
        FadeTransition fadeOut3 = new FadeTransition(Duration.millis(500), animatedImage3);
        FadeTransition fadeOutContainer = new FadeTransition(Duration.millis(500), animationContainer);

        for (FadeTransition ft : Arrays.asList(fadeOut1, fadeOut2, fadeOut3, fadeOutContainer)) {
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
        }

        ParallelTransition closeTransition = new ParallelTransition(
                fadeOut1, fadeOut2, fadeOut3, fadeOutContainer,
                createCloseRotation(animatedImage1),
                createCloseRotation(animatedImage2),
                createCloseRotation(animatedImage3)
        );

        closeTransition.setOnFinished(e -> {
            animationContainer.setVisible(false);
            resetAnimationState();
        });

        closeTransition.play();
    }

    /**
     * Resets the state of the animation by hiding all animated images and
     * restoring their default opacity values. Ensures that the animation
     * container's opacity is also reset to its default value.
     *
     * This method is intended to be used to prepare the animation components
     * for a new animation sequence by reverting any changes made during
     * previous animations.
     */
    private void resetAnimationState() {
        animatedImage1.setVisible(false);
        animatedImage2.setVisible(false);
        animatedImage3.setVisible(false);

        // Resetta le proprietà per le prossime animazioni
        animatedImage1.setOpacity(1.0);
        animatedImage2.setOpacity(1.0);
        animatedImage3.setOpacity(1.0);
        animationContainer.setOpacity(1.0);
    }

    /**
     * Creates a RotateTransition to apply a close rotation animation to the specified ImageView.
     *
     * @param image the ImageView to apply the rotation animation to
     * @return a RotateTransition configured to rotate the image by 180 degrees over 500 milliseconds
     */
    private RotateTransition createCloseRotation(ImageView image) {
        RotateTransition rt = new RotateTransition(Duration.millis(500), image);
        rt.setByAngle(180);
        return rt;
    }

    /**
     *
     */
    @FXML
    private void handlePickLittleDeck1() throws Exception {
        System.out.println("[Controller] Pick little deck 1");
        GenericClient client = GUI.getClient();
        int id = -1;
        for (ShipBoard s : GUI.getModel().getShipBoards()) {
            if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                id = s.getMyPlayer().getPlayerId();
                break;
            }
        }
        client.pickLittleDeck(0, id, client.getUuid());
    }

    /**
     *
     */
    @FXML
    private void handlePickLittleDeck2() throws Exception {
        System.out.println("[Controller] Pick little deck 2");
        GenericClient client = GUI.getClient();
        int id = -1;
        for (ShipBoard s : GUI.getModel().getShipBoards()) {
            if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                id = s.getMyPlayer().getPlayerId();
                break;
            }
        }
        client.pickLittleDeck(1, id, client.getUuid());
    }

    /**
     * Handles the event where the third "little deck" is chosen by the user.
     * This method is invoked when the corresponding GUI element is triggered.
     * The method identifies the current player's ID based on their username
     * and sends a request to the generic client to pick the specified little deck.
     *
     * @throws Exception If there is an issue during the handling of the event or
     *                    communication with the client.
     */
    @FXML
    private void handlePickLittleDeck3() throws Exception {
        System.out.println("[Controller] Pick little deck 3");
        GenericClient client = GUI.getClient();
        int id = -1;
        for (ShipBoard s : GUI.getModel().getShipBoards()) {
            if (s.getMyPlayer().getUsername().equals(GUI.getModel().getMyNickname())) {
                id = s.getMyPlayer().getPlayerId();
                break;
            }
        }
        client.pickLittleDeck(2, id, client.getUuid());
    }

    /**
     * Initializes the controller with the specified location and resources.
     * Sets up the background video bindings, buttons, and animated images.
     * Configures the animation container and adjusts the stage properties.
     * Applies the stylesheet to the root scene.
     *
     * @param location the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources the resources used to localize the root object, or null if the resources are not available
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeBackgroundVideo();
        backgroundVideo.fitWidthProperty().bind(rootStack.widthProperty());
        backgroundVideo.fitHeightProperty().bind(rootStack.heightProperty());
        setButtonImage();
        // Inizializza le immagini animate
        animatedImage1.setVisible(false);
        animatedImage2.setVisible(false);
        animatedImage3.setVisible(false);
        animationContainer.setVisible(false);
        initializeEllipseImagesArray();

        // Centra il container
        StackPane.setAlignment(animationContainer, Pos.CENTER);
        Platform.runLater(() -> {
            Stage stage = (Stage) rootStack.getScene().getWindow();
            stage.sizeToScene();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.centerOnScreen();
        });
        Platform.runLater(() -> {
            if (rootStack.getScene() != null) {
                rootStack.getScene()
                        .getStylesheets()
                        .add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/css/fxml_class.css")).toExternalForm());
            }
        });
    }


    /**
     * Initializes the background video for the application.
     *
     * This method sets up a MediaPlayer to play a specific video resource in an infinite loop,
     * with audio muted and auto-play enabled. The associated MediaView (backgroundVideo) is
     * assigned the MediaPlayer for rendering the video. The video file is fetched as a resource
     * from the application's package.
     *
     * If any errors occur during the media initialization process, they are logged to the
     * standard error stream.
     */
    private void initializeBackgroundVideo() {
        try {
            Media media = JfxMediaUtils.mediaFromResource(
                    getClass(),
                    "/it/polimi/ingsw/graphics/backgrounds/bg_purple.mp4"   // o bg_blue.mp4, ecc.
            );
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setMute(true);
            player.setAutoPlay(true);

            backgroundVideo.setMediaPlayer(player);   // backgroundVideo è il tuo MediaView
        } catch (Exception e) {
            e.printStackTrace();                       // gestisci meglio se vuoi
        }
    }


    /**
     * Sets the image for multiple button components in the user interface.
     * The method assigns a predefined image resource located at
     * "/it/polimi/ingsw/graphics/cards/cardBackLev2.jpg" to several button
     * elements.
     *
     * The image is retrieved using the class's resource stream, and its presence
     * is ensured using {@code Objects.requireNonNull}. If the image resource
     * is missing, a {@code NullPointerException} will be thrown.
     *
     * This method updates the following button components with the same image:
     * - buttonImage1
     * - buttonImage2
     * - buttonImage3
     * - buttonImage11
     */
    public void setButtonImage() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/graphics/cards/cardBackLev2.jpg")));
        buttonImage1.setImage(image);
        buttonImage2.setImage(image);
        buttonImage3.setImage(image);
        buttonImage11.setImage(image);
    }


    /**
     * Sets the view reference for this controller.
     *
     * @param view the GUI instance to be set as the view for this controller
     */
    public void setView(GUI view) {
        System.out.println("[Controller] Setting view reference");
        this.view = view;
    }

    /**
     * Displays an error dialog with a specified title and message.
     *
     * @param title   the title of the error dialog
     * @param message the message to be displayed in the error dialog
     */
    @Override
    public void showErrorDialog(String title, String message) {

    }

    /**
     * Initializes data using the provided LittleModelRepresentation.
     *
     * @param lmr the LittleModelRepresentation object to be set and used for populating ship tabs
     */
    public void initData(LittleModelRepresentation lmr) {
        this.lmr = lmr;
        populateShipTabs();
    }


    /**
     * Populates the ship tab UI with data from the player's ship boards and
     * other players' ship boards. This method organizes the ship boards by
     * first adding the current player's board(s) followed by other players' boards.
     * It creates corresponding tabs for each ship board, loads their associated
     * FXML views, initializes controllers, and sets up player-specific details
     * for display.
     *
     * The tabs are named appropriately based on whether the player is the current user
     * or other players. Each tab includes its own content based on the loaded FXML
     * and associated controller.
     *
     * Any IO errors encountered while loading the FXML files are printed to the
     * standard error stream.
     *
     * This method also updates the internal map of player controllers by associating
     * each player's nickname with their respective controller instance, which is used
     * for managing UI interactions.
     */
    private void populateShipTabs() {
        playerControllers.clear();
        List<ShipBoard> myBoards = new ArrayList<>();
        List<ShipBoard> otherBoards = new ArrayList<>();

        for (ShipBoard s : lmr.getShipBoards()) {
            if (Objects.equals(s.getMyPlayer().getUsername(), lmr.getMyNickname())) {
                myBoards.add(s);
            } else {
                otherBoards.add(s);
            }
        }

// Prima il proprio player
        List<ShipBoard> orderedBoards = new ArrayList<>();
        orderedBoards.addAll(myBoards);
        orderedBoards.addAll(otherBoards);

        for (ShipBoard s : orderedBoards) {
            Player p = s.getMyPlayer();
            String nickname = p.getUsername();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/playerShipTabLev2.fxml"));
                Node content = loader.load();

                PlayerShipBoard_Page_Lev2_Controller controller = loader.getController();
                controller.setView(view);
                boolean isMyPlayer = Objects.equals(nickname, lmr.getMyNickname());
                if (isMyPlayer) setPlayer(nickname);
                controller.setPlayer(nickname, isMyPlayer, lmr.getShipBoards().size(), s.getShipMatrix()[2][3].getID());

                playerControllers.put(nickname, controller);

                Tab tab = new Tab(isMyPlayer ? "Me: " + nickname : nickname);
                tab.setContent(content);
                shipTabPane.getTabs().add(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the item currently in hand for the specified player. Depending on the
     * type of the item, specific handling logic is applied. If the item is null,
     * an appropriate default behavior is invoked.
     *
     * @param p the player for whom the item in hand needs to be updated
     * @param thing the item to assign to the player's hand; can be null
     * @throws Exception if the update fails or an issue occurs in the process
     */
    @Override
    public void updateThingInHand(Player p, Object thing) throws Exception {
        if (thing != null) {
            if (thing instanceof SpaceShipTile) {
                // Gestione per le tile: usa il controller del giocatore specifico
                PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(p.getUsername());
                if (controller != null) {
                    controller.updateThingInHand(p, thing);
                }
            }
        } else {
            //forse metto qui il deposit little deck
            PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(p.getUsername());
            if (controller != null) {
                controller.updateThingInHand(p, null);
            }
        }
    }

    /**
     * Loads the image associated with a specific card for the deck.
     *
     * @param card the card for which the image needs to be loaded
     * @return the Image object corresponding to the card
     */
    private Image loadImageForDeck(Card card) {
        // Implementa la logica per caricare l'immagine corretta in base alla carta
        String imagePath = "/it/polimi/ingsw/graphics/cards/cardId_" + card.getId() + ".jpg";
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    /**
     * Adds a tile to the specified position on the ship board.
     *
     * @param tile the SpaceShipTile to be added
     * @param row the row index where the tile should be placed
     * @param col the column index where the tile should be placed
     * @param ship the ship board on which the tile is to be added
     * @throws Exception if an error occurs while adding the tile
     */
    @Override
    public void addTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(ship.getMyPlayer().getUsername());
        if (controller != null) {
            controller.addTile(tile, row, col, ship);
        }
    }

    /**
     * Updates the time by delegating the call to the associated player's controller.
     * This method retrieves the controller associated with the player and invokes
     * its updateTime method. If no controller is found for the player, no action
     * is performed.
     *
     * @throws Exception if an exception occurs during the time update process
     */
    @Override
    public void updateTime() throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.updateTime();
        }
    }


    /**
     * Retrieves the representation of the little model associated with this controller.
     *
     * @return the instance of LittleModelRepresentation representing the current state of the little model,
     *         or null if no representation is available.
     */
    @Override
    public LittleModelRepresentation getLittleModelRepresentation() {
        return null;
    }

    /**
     * Sets the client instance for the controller.
     *
     * @param client the instance of GenericClient to be set
     */
    @Override
    public void setClient(GenericClient client) {

    }

    /**
     * Sets the LittleModelRepresentation for this controller.
     *
     * @param littleModelRepresentation the LittleModelRepresentation to be set
     */
    @Override
    public void setLittleModelRepresentation(LittleModelRepresentation littleModelRepresentation) {

    }

    /**
     * Updates the state of the flight board or related UI components based on the provided JSON input.
     *
     * @param Json a JSON-formatted string containing the data required to update the state or UI components
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void Update(String Json) throws Exception {

    }

    /**
     * Starts a process or event related to the provided username.
     *
     * @param Username the username of the player or entity initiating the Start action
     * @throws Exception if any issue occurs during the execution of the Start method
     */
    @Override
    public void Start(String Username) throws Exception {

    }

    /**
     *
     */
    @Override
    public void movePlayerOnFlightboard(Player player, int newPos, int oldPos) throws Exception {
        int id = player.getPlayerId();
        if (id == 0) id = 52;
        else if (id == 1) id = 33;
        else if (id == 2) id = 61;
        else if (id == 3) id = 34;
        if (oldPos == -1) {
            PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(player.getUsername());
            if (controller != null) {
                controller.movePlayerOnFlightboard(player, newPos, oldPos);
            }
            setEllipseImage(newPos, "/it/polimi/ingsw/graphics/mammozzetti/" + id + ".png");
        } else {
            setEllipseImage(oldPos, (Image) null);
            setEllipseImage(newPos, "/it/polimi/ingsw/graphics/mammozzetti/" + id + ".png");
        }
    }

    /**
     * Removes a specified block of spaceship tiles from the given ship board.
     *
     * @param s the ship board from which the block will be removed
     * @param block the list of spaceship tiles representing the block to be removed
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeBLock(ShipBoard s, ArrayList<SpaceShipTile> block) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.removeBLock(s, block);
        }
    }

    /**
     * Updates the goods on the given ship board by adjusting the storage tiles
     * and replacing them with the provided new goods.
     *
     * @param s The ship board to be updated.
     * @param storagetiles A list of lists representing the storage tile positions
     *                     that need to be updated.
     * @param newgoods A list of lists containing the new goods to replace the
     *                 existing ones on the ship board.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateGoods(ShipBoard s, ArrayList<ArrayList<Integer>> storagetiles, ArrayList<ArrayList<Goods>> newgoods) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.updateGoods(s, storagetiles, newgoods);
        }
    }

    /**
     * Updates the batteries for a given ship board using the provided battery positions.
     *
     * @param BattxPos a nested list containing the positions of the batteries to be updated
     * @param s the ship board object for which the batteries are being updated
     * @param toLoose a boolean indicating whether the update should be performed in a "to loose" state
     * @throws Exception if an error occurs during the battery update process
     */
    @Override
    public void updateBatteries(ArrayList<ArrayList<Integer>> BattxPos, ShipBoard s, boolean toLoose) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.updateBatteries(BattxPos, s, toLoose);
        }
    }

    /**
     * Removes passengers from the specified ship board according to the provided tiles and number of passengers.
     *
     * @param s the ship board from which the passengers will be removed
     * @param tiles a list of tile coordinates indicating the locations on the ship board to remove passengers from
     * @param numPass the number of passengers to be removed
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removePassengers(ShipBoard s, ArrayList<ArrayList<Integer>> tiles, int numPass) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.removePassengers(s, tiles, numPass);
        }
    }

    /**
     * Removes a specified number of passengers from a given tile on the ship's board.
     *
     * @param s the ship board from which passengers will be removed
     * @param tile the specific tile on the ship board from which passengers are removed
     * @param numPass the number of passengers to remove from the specified tile
     * @throws Exception if an error occurs during the passenger removal process
     */
    @Override
    public void removePassengersFromTile(ShipBoard s, SpaceShipTile tile, int numPass) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.removePassengersFromTile(s, tile, numPass);
        }
    }

    /**
     *
     */
    @Override
    public void removeAlienFromTile(ShipBoard s, SpaceShipTile tile) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.removeAlienFromTile(s, tile);
        }
    }

    /**
     * Updates the flightboard by setting the appropriate deck image based on the given deck object.
     *
     * @param deck the deck object used to determine the corresponding flightboard image
     */
    private void updateFlightboard(Object deck) {
//        try {
//            String imagePath = "/graphics/decks/little_deck_" + ((Deck) deck).getType() + ".png";
//            Image deckImage = new Image(getClass().getResourceAsStream(imagePath));
//            flightboardImageView.setImage(deckImage);
//        } catch (Exception e) {
//            System.err.println("Errore nel caricamento del deck: " + e.getMessage());
//        }
        //   }
    }

    /**
     * Adds a flipped tile to the player's game view. This method updates the state
     * of the game by marking a tile as flipped for the specified player's controller.
     *
     * @param indextile The index of the tile to be marked as flipped.
     * @throws Exception If an error occurs during the operation.
     */
    @Override
    public void addTileFlipped(int indextile) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.addTileFlipped(indextile);
        }
    }

    /**
     * Handles the event where a player deposits a smaller deck into the flight board.
     *
     * @param d the deck that was deposited
     * @param player the player who performed the action
     * @throws Exception if an error occurs during the deposit process
     */
    @Override
    public void someoneDepositedLittleDeck(Deck d, Player player) throws Exception {

    }

    /**
     * Handles the event when a player has picked a small deck of cards. It verifies the player's identity,
     * checks the deck size, and triggers the loading and animation of the deck's images.
     *
     * @param d the deck selected by the player. It must be non-null and contain exactly three cards.
     * @param player the player who picked the deck. The player's username must match the one associated with this instance.
     * @throws Exception if an error occurs during the processing of the deck or image loading.
     */
    @Override
    public void someonePickedLittleDeck(Deck d, Player player) throws Exception {
        if (player.getUsername().equals(this.player) && d != null && d.getCardList().size() == 3) {
            // Carica le immagini del deck
            Image img1 = loadImageForDeck(d.getCardList().getFirst());
            Image img2 = loadImageForDeck(d.getCardList().get(1));
            Image img3 = loadImageForDeck(d.getCardList().get(2));
            Platform.runLater(() -> {
                animatedImage1.setImage(img1);
                animatedImage2.setImage(img2);
                animatedImage3.setImage(img3);
                showImagesWithAnimation();
            });
        }
    }

    /**
     * Removes a flipped tile identified by the given tile ID.
     * Delegates the operation to the corresponding player controller, if available.
     *
     * @param t the ID of the tile to be removed
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removeTileFlipped(int t) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.removeTileFlipped(t);
        }
    }

    /**
     * Adds a passenger to a specific location on the ship's board.
     *
     * @param s       the ship board where the passenger is to be added
     * @param row     the row index on the board where the passenger will be placed
     * @param column  the column index on the board where the passenger will be placed
     * @throws Exception if an error occurs while adding the passenger
     */
    @Override
    public void addPassenger(ShipBoard s, int row, int column) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.addPassenger(s, row, column);
        }
    }

    /**
     * Triggers the logic for handling consequences within the flight board controller's context.
     *
     * This method is overridden to provide specific behavior for when consequences need to be paid
     * during the execution of the game's mechanics. It might include penalizing players, enforcing
     * rule-based effects, or handling specific board changes relevant to the game's state.
     *
     * The exact implementation details depend on the current game stage and the conditions defined
     * in the overarching game logic.
     */
    @Override
    public void youPayConsequences() {

    }

    /**
     * Sends a penalty to the specified player or entity within the game context.
     *
     * @param penalty The amount or value of the penalty to apply.
     * @param type The type or category of the penalty.
     */
    @Override
    public void sendPenalty(int penalty, String type) {

    }

    /**
     * Adds an alien to the specified position on the ship board.
     *
     * @param s           the ship board where the alien is to be added
     * @param row         the row position on the ship board
     * @param column      the column position on the ship board
     * @param alienColor  the color of the alien to be added
     * @throws Exception  if the operation fails
     */
    @Override
    public void addAlien(ShipBoard s, int row, int column, AlienColor alienColor) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.addAlien(s, row, column, alienColor);
        }
    }

    /**
     * Displays the first sight of the flight board to the user.
     *
     * This method is part of the FlightBoard_Page_Lev2_Controller and is used to
     * visually present the initial state or view of the flight board interface.
     * It may include animations, images, or elements necessary to set up
     * the user's perspective for gameplay or interaction within the application.
     *
     * @throws Exception if an error occurs during the display process,
     *         such as an issue with graphical elements or data initialization.
     */
    @Override
    public void showFirstSight() throws Exception {

    }

    /**
     * Updates the current status of the view or game state.
     *
     * This method is typically called to refresh or modify the state of the
     * game board or graphical elements based on recent changes in the application's
     * logic or data model. It is overridden from a base class or interface and
     * can be customized to handle specific requirements for updating the status.
     *
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateStatus() throws Exception {

    }

    /**
     * Handles errors encountered during the execution of the application.
     * This method is invoked in case of an error scenario and allows for
     * implementing custom error handling or logging mechanisms.
     *
     * @throws Exception if there is an issue in handling the error.
     */
    @Override
    public void onError() throws Exception {

    }

    /**
     * Displays the final scores of players at the end of the game.
     *
     * @param finalScores a HashMap where the keys are player names (String) and the values are their respective scores (Float)
     * @throws Exception if any error occurs while displaying the scores
     */
    @Override
    public void showFinalScore(HashMap<String, Float> finalScores) throws Exception {

    }

    /**
     * Causes the specified ship board to lose all its goods.
     *
     * @param s the ship board whose goods are to be lost
     * @throws Exception if an error occurs during the process
     */
    @Override
    public void looseAllGoods(ShipBoard s) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.looseAllGoods(s);
        }
    }

    /**
     * Causes the specified ship board to lose all its batteries.
     *
     * @param s the ship board object containing the details of the player's ship
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void looseAllbatteries(ShipBoard s) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(s.getMyPlayer().getUsername());
        if (controller != null) {
            controller.looseAllbatteries(s);
        }
    }

    /**
     * Executes the middle effect phase of the game. This method is responsible for handling any
     * intermediate actions or logic that occurs between the beginning and end phases of the current game state.
     *
     * @throws Exception if an error occurs during the execution of the middle effect logic.
     */
    @Override
    public void middleEffect() throws Exception {

    }

    /**
     * This method is invoked to handle the conclusion of an effect.
     * It is part of the overall effect lifecycle management in the application.
     *
     * This could include performing cleanup operations, resetting states, or triggering the next steps
     * in the sequence of events after the effect has ended.
     *
     * Implementations of this method can throw exceptions if any issues occur during the execution
     * of the effect's conclusion.
     *
     * @throws Exception if an error occurs while ending the effect
     */
    @Override
    public void endedEffect() throws Exception {

    }

    /**
     * Updates the state of the card by disabling, hiding, and unmanaging specific UI components.
     * Additionally, it delegates the updateCard method to a corresponding controller if available.
     *
     * @throws Exception if an error occurs during the update process.
     */
    @Override
    public void updateCard() throws Exception {
        pickLittleDeckButton1.setDisable(true);
        pickLittleDeckButton2.setDisable(true);
        pickLittleDeckButton3.setDisable(true);
        pickLittleDeckButton1.setManaged(false);
        pickLittleDeckButton2.setManaged(false);
        pickLittleDeckButton3.setManaged(false);
        pickLittleDeckButton1.setVisible(false);
        pickLittleDeckButton2.setVisible(false);
        pickLittleDeckButton3.setVisible(false);

        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.updateCard();
        }
    }

    /**
     * Updates the cosmic credits of a specified player.
     *
     * @param p The player whose cosmic credits will be updated.
     * @param i The amount to update the cosmic credits by. Positive values increase credits, and negative values decrease credits.
     * @throws Exception If an error occurs during the update process.
     */
    @Override
    public void updateCosmicCredits(Player p, int i) throws Exception {

    }

    /**
     * Updates the state of players currently in the game.
     * This method is invoked to synchronize or refresh player-related data
     * within the context of the game.
     *
     * @throws Exception if an error occurs during the update process.
     */
    @Override
    public void updatePlayersInGame() throws Exception {

    }

    /**
     * Updates a message to be displayed or processed.
     *
     * @param message the message to be updated; must not be null or empty
     * @throws Exception if an error occurs during the update process
     */
    @Override
    public void updateMessageOnly(String message) throws Exception {

    }

    /**
     * Displays the incorrect tiles on the player's ship board associated with a given nickname.
     * Delegates the display logic to the corresponding controller if available.
     *
     * @param tiles      a list of integers representing the indexes of the incorrect tiles
     * @param nickname   the nickname of the player whose board is to be updated
     * @param nickEff    the effective nickname used for additional processing
     * @throws Exception if there is an issue with the operation
     */
    @Override
    public void showWrongTiles(ArrayList<Integer> tiles, String nickname, String nickEff) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(nickname);
        if (controller != null) {
            controller.showWrongTiles(tiles, nickname, nickEff);
        }
    }

    /**
     * Updates the state of the game or player when a shot is received.
     *
     * @param player The Player object representing the player receiving the shot.
     * @param shot The Shot object representing the shot received.
     * @param howToDefenceFromShots A list of integers detailing the defense strategy against the shot.
     * @param dice An Integer representing the dice roll outcome associated with the shot.
     * @throws Exception If an error occurs while updating the state for the received shot.
     */
    @Override
    public void updateShotReceived(Player player, Shot shot, ArrayList<Integer> howToDefenceFromShots, Integer dice) throws Exception {

    }

    /**
     * Sends a message containing information about sub-ships to a specified player's controller.
     *
     * @param subShip a 2D ArrayList representing the sub-ship components for a player's ship.
     * @param playerNickname the nickname of the player whose controller will receive the message.
     * @throws Exception if an error occurs while messaging the sub-ships.
     */
    @Override
    public void messageSubShips(ArrayList<ArrayList<SpaceShipTile>> subShip, String playerNickname) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(playerNickname);
        if (controller != null) {
            controller.messageSubShips(subShip, playerNickname);
        }
    }

    /**
     * Allows the player to choose a specific subset of spaceship tiles to preserve,
     * while designating the remaining tiles as waste.
     *
     * @param playerNickname the nickname of the player making the choice
     * @param subShips the list of subsets of spaceship tiles available for selection
     * @param indexToPreserve the index of the subset of spaceship tiles to be preserved
     * @param waste the amount of waste tiles generated from the excluded subsets
     * @throws Exception if the provided parameters are invalid or if the selection process fails
     */
    @Override
    public void ChooseSubShip(String playerNickname, ArrayList<ArrayList<SpaceShipTile>> subShips, int indexToPreserve, int waste) throws Exception {

    }

    /**
     * Handles the surrender action for the current player in the game.
     * This method is triggered when the corresponding UI component is interacted with.
     * It likely signifies the player's forfeiture or decision to quit the current round or game.
     */
    @FXML
    private void handleSurrend() {

    }

    /**
     * Removes a single tile from the player's board at the specified location.
     * This method delegates the removal process to the player's specific controller.
     *
     * @param playerNickname the nickname of the player whose tile is to be removed
     * @param row the row index of the tile to be removed
     * @param col the column index of the tile to be removed
     * @param fromMistake a boolean indicating whether the removal is due to a mistake
     * @param waste an integer representing the waste value associated with the removal
     * @throws Exception if an error occurs during the tile removal process
     */
    @Override
    public void removeSingleTile(String playerNickname, int row, int col, boolean fromMistake, int waste) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(playerNickname);
        if (controller != null) {
            controller.removeSingleTile(playerNickname, row, col, fromMistake, waste);
        }
    }

    /**
     * Adds a waiting tile to the specified location on the ship board.
     *
     * @param tile the SpaceShipTile to be added
     * @param row the row index where the tile will be placed
     * @param col the column index where the tile will be placed
     * @param ship the ship board to which the tile will be added
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void addWaitTile(SpaceShipTile tile, int row, int col, ShipBoard ship) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(ship.getMyPlayer().getUsername());
        if (controller != null) {
            controller.addWaitTile(tile, row, col, ship);
        }
    }

    /**
     * Ends the building phase for a player identified by their nickname.
     *
     * @param playerNick the nickname of the player whose building phase is being ended
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void endBuilding(String playerNick) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(playerNick);
        if (controller != null) {
            controller.endBuilding(playerNick);
        }
    }

    /**
     * Determines if tiles need to be filled for the current player and triggers the required action.
     * This method retrieves the GameController for the specified player and delegates the
     * tile-filling operation to the player's controller.
     *
     * @throws Exception if an error occurs during the tile-filling operation in the player's controller
     */
    @Override
    public void haveToFillTiles() throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.haveToFillTiles();
        }
    }

    /**
     * Updates the remaining goods for the specified player by delegating the operation
     * to the corresponding controller.
     *
     * @param p the player whose goods remaining data is to be updated
     * @param goodFInali the list of goods to be updated for the player
     * @throws Exception if an error occurs during the operation
     */
    @Override
    public void updateGoodsRemaining(Player p, ArrayList<Goods> goodFInali) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.updateGoodsRemaining(p, goodFInali);
        }
    }

    /**
     * Removes a player from the flightboard by clearing their current position
     * and notifying the respective player controller to handle the removal.
     *
     * @param player the identifier of the player to be removed
     * @param oldPos the current position of the player on the flightboard
     * @throws Exception if an error occurs during the removal process
     */
    @Override
    public void removePlayerFromFlightboard(String player, int oldPos) throws Exception {
        setEllipseImage(oldPos, (Image) null);
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(player);
        if (controller != null) {
            controller.removePlayerFromFlightboard(player, oldPos);
        }
    }

    /**
     * Displays a list of old game sessions.
     *
     * @param oldGames the list of old game sessions to be displayed
     * @throws Exception if an error occurs while displaying the old games
     */
    @Override
    public void showOldGames(ArrayList<OldGame> oldGames) throws Exception {

    }

    /**
     * Displays an informational dialog with a given title and message.
     *
     * @param title   the title of the dialog
     * @param message the message to be displayed in the dialog
     */
    @Override
    public void showInfoDialog(String title, String message) {

    }

    /**
     * Displays a shutdown message or notification to the user.
     *
     * @param reason A string describing the reason for the shutdown. This could include error details
     *               or any relevant information to communicate why the system or process is being shut down.
     */
    @Override
    public void showShutdown(String reason) {

    }

    /**
     *
     */
    @Override
    public void insertwaittileLMR1(String playerNickname) {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(playerNickname);
        if (controller != null) {
            controller.insertwaittileLMR1(playerNickname);
        }
    }

    /**
     * Inserts a wait tile for a player on the second level of their shipboard.
     *
     * @param playerNickname the nickname of the player for whom the wait tile is to be inserted
     */
    @Override
    public void insertwaittileLMR2(String playerNickname) {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(playerNickname);
        if (controller != null) {
            controller.insertwaittileLMR2(playerNickname);
        }
    }

    /**
     * Processes and handles the input correction based on the provided message.
     *
     * @param message the input message that needs to be corrected or processed
     */
    @Override
    public void correctinput(String message) {

    }

    /**
     * Handles the transition to the next player's turn in the game.
     *
     * @param myNickname The nickname of the current player whose turn is ending.
     */
    @Override
    public void nextPlayerTurn(String myNickname) {

    }

    /**
     * Displays a message on the user interface for a specified duration.
     *
     * @param text the message to be displayed
     * @param seconds the duration in seconds for which the message will be shown
     */
    @Override
    public void showTimedInfo(String text, int seconds) {

    }

    /**
     * Updates the number of batteries remaining for a specified player.
     *
     * @param p the player whose battery count is to be updated
     * @param batt the new number of batteries remaining
     * @throws Exception if an error occurs while updating the battery count
     */
    @Override
    public void updateBatteriesRemaining(Player p, int batt) throws Exception {
        PlayerShipBoard_Page_Lev2_Controller controller = playerControllers.get(p.getUsername());
        if (controller != null) {
            controller.updateBatteriesRemaining(p, batt);
        }
    }


}