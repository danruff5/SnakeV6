package Main;

/**
 * Allow for the storage of whether the food is special or not.
 * 
 * 16 -> 38
 * (16 / 38) * 100 = 42 %
 *
 * @author Daniel Krauskopf
 */
public class PositionFood extends Position {

    /**
     * The storage of the specialness of this food.
     */
    private final boolean special;

    /**
     * Creates a new PositionFood.
     *
     * @param pos The Position to copy.
     * @param special If the food is special.
     */
    public PositionFood(Position pos, boolean special) {
        super(pos);
        this.special = special;
    }

    /**
     * To get the special of the food.
     *
     * @return The special of the food.
     */
    public boolean getSpecial() {
        return this.special;
    }
}
