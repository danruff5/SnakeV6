package Main;

import java.awt.Font;
import java.awt.Graphics;

/**
 * Just some basic methods to make the code in Main shorter.
 * 
 * 41 -> 98
 * (41 / 98) * 100 = 42 %
 * 
 * @author Daniel Krauskopf
 */
public class Methods {
    /**
     * Generates a random number within a given range.
     * @param min The lower number.
     * @param max The higher number (the cops are looking for him).
     * @return The random number between the given values.
     */
    public static int rand (int min, int max) {
        // The number.
        int rand;
        do {
            // Make the random number and scale it to the max.
            rand = (int)(Math.random() * max);
        // Do it again if the number is smaller then the min.
        } while (rand < min);
        
        // Give the number back.
        return rand;
    }
    /**
     * Draws a string in the center of the screen (taking into account the size of the font).
     * @param graphics The graphics to draw to.
     * @param string The string to draw.
     * @param font The font to use.
     */
    public static void drawString (Graphics graphics, String string, Font font) {
        // The middle (to draw font) of the screen in x.
        // Window width divided by two subtract font width for the string diveded by two.
        int x = (Main.WINDOW_SIZE[0] / 2) - (graphics.getFontMetrics(font).stringWidth(string) / 2);
        
        // The middle (to draw font) of the screen in y.
        // Window height divided by two subtract font height divided by two.
        int y = (Main.WINDOW_SIZE[1] / 2) + (graphics.getFontMetrics(font).getHeight() / 2);
        
        // Draw the string at the given locations.
        Methods.drawString(graphics, string, font, x, y);
    }
    /**
     * Draws a string in the middle (y only) (taking into account the height of the font).
     * @param graphics The graphics to draw to.
     * @param string The string to draw.
     * @param font The font to use.
     * @param x The x value to draw the string at.
     */
    public static void drawString (Graphics graphics, String string, Font font, int x) {
        // The middle (to draw font) of the screen in y.
        // Window height divided by two subtract font height divided by two.
        int y = (Main.WINDOW_SIZE[1] / 2) + graphics.getFontMetrics(font).getHeight();
        
        // Draw the string at the given location
        Methods.drawString(graphics, string, font, x, y);
    }
    /**
     * Draws a string in the middle (x only) (taking into account the width of the font for the given string).
     * y is at the beginning to allow for correct overloading of the method.
     * @param y The y position to draw the string at.
     * @param graphics The graphics to draw to.
     * @param string The string to draw.
     * @param font The font to use.
     */
    public static void drawString (int y, Graphics graphics, String string, Font font) {
        // The middle (to draw font) of the screen in x.
        // Window width divided by two subtract font width for the string diveded by two.
        int x = (Main.WINDOW_SIZE[0] / 2) - (graphics.getFontMetrics(font).stringWidth(string) / 2);
        
        // Draw the string at the given location.
        Methods.drawString(graphics, string, font, x, y);
    }
    /**
     * Draws a string.
     * @param graphics The graphics to draw to.
     * @param string The string to draw.
     * @param font The font to use.
     * @param x The x location to draw to.
     * @param y The y location to draw to.
     */
    public static void drawString (Graphics graphics, String string, Font font, int x, int y) {
        // set the font of the graphics to the given font
        graphics.setFont(font);
        
        // Draw the string
        graphics.drawString(string, x, y);
    }
}
