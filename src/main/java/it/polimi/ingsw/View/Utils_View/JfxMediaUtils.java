package it.polimi.ingsw.View.Utils_View;

import javafx.scene.media.Media;

import java.io.*;
import java.nio.file.*;

public final class JfxMediaUtils {

    /**
     * Utility class for handling JavaFX Media operations.
     * This class is not intended to be instantiated.
     */
    private JfxMediaUtils() {}

    /**
     * Creates a {@link Media} object from a resource file in the specified class's package.
     * The resource is copied to a temporary file, which is used to create the media object.
     *
     * @param ctx the class whose classloader is used to access the resource
     * @param resourcePath the path of the resource relative to the class's package
     * @return a {@link Media} object representing the resource file
     * @throws IOException if an I/O error occurs while accessing or processing the resource
     * @throws FileNotFoundException if the specified resource cannot be found
     */
    public static Media mediaFromResource(Class<?> ctx, String resourcePath) throws IOException {
        try (InputStream in = ctx.getResourceAsStream(resourcePath)) {
            if (in == null)
                throw new FileNotFoundException("Resource not found: " + resourcePath);

            String suffix = resourcePath.contains(".")
                    ? resourcePath.substring(resourcePath.lastIndexOf('.'))
                    : ".tmp";

            Path tmp = Files.createTempFile("jfx_media_", suffix);
            tmp.toFile().deleteOnExit();
            Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
            return new Media(tmp.toUri().toString());
        }
    }
}
