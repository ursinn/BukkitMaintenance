package de.howaner.BukkitMaintenance.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * Image Utils
 *
 * @author Bai Ben
 */
public class ImageUtils {

    /**
     * Encode image to string
     */
    public static String encodeToString(BufferedImage image, String type) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, type, bos);

        byte[] imageBytes = bos.toByteArray();
        String imageString = Base64.getEncoder().encodeToString(imageBytes);

        bos.close();
        return imageString;
    }

}
