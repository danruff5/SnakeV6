package Main;

/**
 * Allows for the storing of x and y values for a position. Have an array of
 * Positions instead of a 2D array!
 * 
 * 55 -> 119
 * (55 / 119) * 100 = 46 %
 *
 * @author Daniel Krauskopf
 */
public class Position {

    /**
     * The x value of the position.
     */
    private int x;
    /**
     * The y value of the position.
     */
    private int y;

    /**
     * Creates a Position at (0, 0).
     */
    public Position() {
        this(0, 0);
    }

    /**
     * Creates a new Position.
     *
     * @param width The x value of the Position.
     * @param height The y value of the position.
     */
    public Position(int width, int height) {
        this.x = width;
        this.y = height;
    }

    /**
     * Creates a new Position.
     *
     * @param pos Create a copy of this Position.
     */
    public Position(Position pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    /**
     * To get the x value.
     *
     * @return The x value of the Position.
     */
    public int getX() {
        return x;
    }

    /**
     * To get they value.
     *
     * @return The y value of the Position.
     */
    public int getY() {
        return y;
    }

    /**
     * Increase the x value by one.
     */
    public void addX() {
        this.addX(1);
    }

    /**
     * Increase the x value.
     *
     * @param value The amount to increase by.
     */
    public void addX(int value) {
        this.x += value;
    }

    /**
     * Increase the y value by one.
     */
    public void addY() {
        this.addY(1);
    }

    /**
     * Increase the y value.
     *
     * @param value The amount to increase by.
     */
    public void addY(int value) {
        this.y += value;
    }

    /**
     * Sets the x value.
     *
     * @param value The value to set x to.
     */
    public void setX(int value) {
        this.x = value;
    }

    /**
     * Sets the y value.
     *
     * @param value The value to set y to.
     */
    public void setY(int value) {
        this.y = value;
    }
}
