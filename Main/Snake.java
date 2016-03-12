package Main;

/**
 * A Snake.
 * 
 * Has movement and ability to add to the length of the snake.
 * 
 * 72 -> 144
 * (72 / 144) * 100 = 50 %
 * 
 * @author Daniel Krauskopf
 */
public class Snake {
    /**
     * The array of Position to store the location of each piece of the snake.
     */
    private Position[] pos;
    /**
     * Creates all of the snakes Position.
     * @param length How long the snake is.
     * @param initPos The start Position of the snake.
     * @param direction Which direction the snake is going to start.
     */
    public Snake(int length, Position initPos, int direction) {
        // The sotorage of the snakes positions.
        pos = new Position[length];
        
        // This if for going through the pos array.
        int count = 0;

        // These for loops set the position of the snake when you firat start.
        // Note: the head is a position 0 in the array.
        // Going up.
        if (direction == Main.DIRE_UP) {
            // For loop to go down in y values in position.
            // down in y is adding to y.
            for (int i = initPos.getY(); i < initPos.getY() + length; i++) {
                // Set the position of the snake.
                pos[count] = new Position(initPos.getX(), i);
                
                // Set the next one.
                count++;
            }
        // Going down.
        } else if (direction == Main.DIRE_DOWN) {
            // Go up in y which is subtracting in y.
            for (int i = initPos.getY(); i > initPos.getY() - length; i--) {
                pos[count] = new Position(initPos.getX(), i);
                count++;
            }
        // Going right.
        } else if (direction == Main.DIRE_RIGHT) {
            // Go left int x which is subtracting in x.
            for (int i = initPos.getX(); i > initPos.getX() - length; i--) {
                pos[count] = new Position(i, initPos.getY());
                count++;
            }
        // Going left.
        } else if (direction == Main.DIRE_LEFT) {
            // Go right in x which is adding in x.
            for (int i = initPos.getX(); i < initPos.getX() + length; i++) {
                pos[count] = new Position(i, initPos.getY());
                count++;
            }
        }        
    }
    /**
     * To increase the snakes position in the current direction.
     * @param direction The direction of movement.
     */
    public void moveSnake(int direction) {
        // Go through each snake position starting at the end (important)!
        for (int i = pos.length - 1; i >= 0; i--) {
            
            // Only the first one moves.
            if (i == 0) {
                // Going up -> subtract in y.
                if (direction == Main.DIRE_UP) {
                    pos[i].addY(-1);
                // Going down -> add in y.
                } else if (direction == Main.DIRE_DOWN) {
                    pos[i].addY();
                // Going left -> subtract in x.
                } else if (direction == Main.DIRE_LEFT) {
                    pos[i].addX(-1);
                // Going right -> add in x.
                } else if (direction == Main.DIRE_RIGHT) {
                    pos[i].addX();
                }
            } else {
                // The snake follows the head by this for loop and the 
                // following code. The last tail piece gets changed to the
                // second last piece. The second to the third... Until the
                // second gets changed to the first. Then the if above if true
                // and the head moves. This makes all of the tail pieces follow
                // the head excatally where it went.
                
                // current position set to the previous tail (next in the for loop)
                // in the snake.
                pos[i].setX(pos[i - 1].getX());
                pos[i].setY(pos[i - 1].getY());
            }
        }
    }
    /**
     * Just to get all of the snakes position to draw to the screen.
     * @return The Position array of the snake.
     */
    public Position[] getSnakePosition() {
        return pos;
    }
    /**
     * Increases the length of the snake by one.
     */
    public void add() {
        this.add(1);
    }
    /**
     * Increases the length of the snake by the given value.
     * @param amount The amount to increase the length of the snake.
     */
    public void add(int amount) {
        
        // Make a copy of the position.
        Position[] oldPos = pos;
        
        // Increase the length of the Position for the snake by the given value.
        pos = new Position[oldPos.length + amount];
        
        // Copy all of the old positions from the copy into the new Position.
        for (int i = 0; i < oldPos.length; i ++) {
            pos[i] = oldPos[i];
        }
        
        // Run through all of the new (empty) Position.
        for (int i = 0; i < amount; i++) {
            
            // Make the new Position the same as the last.
            // The moveSnake will make it work out to follow correctally.
            pos[(oldPos.length) + i] = new Position(oldPos[oldPos.length - 1].getX(), oldPos[oldPos.length - 1].getY());
        }
    }
}
