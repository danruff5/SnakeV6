package Main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Daniel Krauskopf
 */
public class Methods {
    /**
     * Rotates the image
     * from http://flyingdogz.wordpress.com/2008/02/11/image-rotate-in-java-2-easier-to-use/
     * @param image the image to rotate
     * @param angle (Math.toRadians()) the angle to rotate by
     * @return the rotated image
     */
    public static BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
        BufferedImage result = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }
    
    public static int rand (int min, int max) {
        int rand;
        do {
            rand = (int)(Math.random() * max);
        } while (rand < min);
        return rand;
    }
}
