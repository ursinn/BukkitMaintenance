package de.howaner.bukkitmaintenance.util;

import lombok.Cleanup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Image Utils
 *
 * @author Bai Ben
 */
public class ImageUtils {

    /**
     * Encode image to string
     *
     * @param image Image
     * @param type  formatName
     * @return encoded Image
     * @throws IOException exception
     */
    public static String encodeToString(BufferedImage image, String type) throws IOException {
        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, type, bos);

        byte[] imageBytes = bos.toByteArray();
        String imageString = Base64.getEncoder().encodeToString(imageBytes);

        return imageString;
    }
}
