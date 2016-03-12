package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Matter Interface: I used an interface so that I could implement the
 * interface and still allow for different objects. As you can see from the map
 * object I created a Matter array (called tiles). Then each different type of
 * Matter can be stored inside the same array. Even though they are all
 * different. :)
 * 
 * int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
 * 
 * 200 -> 429
 * (200 / 429) * 100 = 47 %
 *
 * @author Daniel Krauskopf
 */
public interface Matter {

    /**
     * To be able to show the tile on the screen.
     *
     * @param g The graphics to draw to.
     * @param pos The position object consisting of the x and y tile position.
     */
    public void draw(Graphics g, Position pos);

    /**
     * Some of the Matter has an image associated with them. This just gets the
     * image and stores it in the object.
     */
    public void init();

    /**
     * Gets run when the head of the snake is in the same location as the tile.
     *
     * @param snake A snake object with which to interact with.
     */
    public void contact(Snake snake);

    /**
     * Just the background of the game: Allow snake to go through it (doesn't
     * really do much, just for looks).
     */
    public class Backround implements Matter {

	/**
	 * Draw the background.
	 *
	 * @param g The graphics to draw to.
	 * @param cell The location to draw to.
	 */
	@Override
	public void draw(Graphics g, Position cell) {
	    // Special colour (supposed to be sand).
	    g.setColor(new Color(210, 187, 0));

	    // draw a fancy rectangle.
	    // (pixel location in x, pixel location in y, how big the cell is, tis a square, make it look raised)
	    g.fill3DRect(cell.getX() * Main.CELL_SIZE, cell.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE, true);
	}

	/**
	 * No image for the background (still need to have it for an interface.
	 */
	@Override
	public void init() {
	    // not needed for this object
	}

	/**
	 * Just let the snake move through it.
	 *
	 * @param snake
	 */
	@Override
	public void contact(Snake snake) {
	    // do nothing (move through backround)
	}
	// I told you it doesn't do much.
    }

    /**
     * The bush: Uses an external image! Snake will die when hit.
     */
    public class Bush implements Matter {

	/**
	 * Just the stored image.
	 */
	BufferedImage bush;

	/**
	 * Draws the image.
	 *
	 * @param g The graphics to draw to.
	 * @param cell The location to draw to.
	 */
	@Override
	public void draw(Graphics g, Position cell) {
	    // draw the image.
	    // I have to scale the image to the cell size.
	    // (the bush image, pixel location in x, pixel location in y, maximum pixel location in x, maximum pixel location in y, start pixel location from image in x, start pixel location from image in y, white is clear (in theory), used for telling what is goin on with the image)
	    g.drawImage(bush, cell.getX() * Main.CELL_SIZE, cell.getY() * Main.CELL_SIZE, cell.getX() * Main.CELL_SIZE + Main.CELL_SIZE, cell.getY() * Main.CELL_SIZE + Main.CELL_SIZE, 0, 0, bush.getWidth(), bush.getHeight(), Color.WHITE, null);
	}

	/**
	 * Has to get the image.
	 */
	@Override
	public void init() {
	    try {
		// read the image from the file location and save it.
		this.bush = ImageIO.read(new File("src/Images/Bush.jpg"));
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}

	/**
	 * When you hit a bush you die.
	 *
	 * @param snake the snake that hit the bush.
	 */
	@Override
	public void contact(Snake snake) {
	    // Sets the game state to message.
	    Main.state = Main.STATE_MESSAGE;

	    // A death message.
	    Main.message = "A wild pokemon killed you";
	}
    }

    /**
     * A rock: Snake will die when hit.
     */
    public class Rock implements Matter {

	/**
	 * Draw the rock.
	 *
	 * @param g The graphics to draw to.
	 * @param cell The location to draw to.
	 */
	@Override
	public void draw(Graphics g, Position cell) {
	    // Draw a backround behind
	    Matter.Backround backround = new Matter.Backround();
	    backround.draw(g, cell);

	    // Special colour (tis a gray).
	    g.setColor(new Color(125, 150, 125));

	    // Just a circle.
	    // (pixel location in x, pixel location in y, (Fill the cell!!) ** 2)
	    g.fillOval(cell.getX() * Main.CELL_SIZE, cell.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
	}

	/**
	 * NO!!
	 */
	@Override
	public void init() {
	    // not needed for this object
	}

	/**
	 * Snake die now.
	 *
	 * @param snake The snake to die.
	 */
	@Override
	public void contact(Snake snake) {
	    // Sets the game state to message.
	    Main.state = Main.STATE_MESSAGE;

	    // Death message
	    Main.message = "You are not a bulldozer";
	}
    }

    /**
     * This tis you (well.. most of it).
     */
    public class SnakeBody implements Matter {

	/**
	 * Draw a body piece.
	 *
	 * @param g The graphics to draw to.
	 * @param cell The location to draw to.
	 */
	@Override
	public void draw(Graphics g, Position cell) {
	    // Draw a background (For Inspiration and Recognition in Science and Technology).
	    Matter.Backround backround = new Matter.Backround();
	    backround.draw(g, cell);

	    // The snake colour (I wanted this to come from a pattern but never got the chance).
	    g.setColor(Color.BLACK);

	    // Tis but a circle.
	    // (pixel location in x, pixel location in y, CIRCLE)
	    g.fillOval(cell.getX() * Main.CELL_SIZE, cell.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
	}

	/**
	 * Just uses simple drawing (no image).
	 */
	@Override
	public void init() {
	    // not needed for this object
	}

	/**
	 * The snake dies.
	 *
	 * @param snake The snake.
	 */
	@Override
	public void contact(Snake snake) {
	    // Sets the game state to message.
	    Main.state = Main.STATE_MESSAGE;

	    // cannibalism is not allowed.
	    Main.message = "Ouch!";
	}
    }

    /**
     * Tis your face.
     */
    public class SnakeHead implements Matter {

	/**
	 * The image of the head.
	 */
	BufferedImage head;
	/**
	 * Which direction you are going.
	 */
	int direction;

	/**
	 * To give the direction a value.
	 *
	 * @param direction The value of the direction the snake is going.
	 */
	public SnakeHead(int direction) {
	    this.direction = direction;
	}

	/**
	 * Draw the head.
	 *
	 * @param g The graphics to draw to.
	 * @param pos The location to draw to.
	 */
	@Override
	public void draw(Graphics g, Position pos) {
	    // Draw the image.
	    // I have to scale the image to the cell size.
	    // (the bush image, pixel location in x, pixel location in y, maximum pixel location in x, maximum pixel location in y, start pixel location from image in x, start pixel location from image in y, white is clear (in theory), used for telling what is going on with the image)
	    g.drawImage(this.head, pos.getX() * Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE, pos.getX() * Main.CELL_SIZE + Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE + Main.CELL_SIZE, 0, 0, head.getWidth(), head.getHeight(), Color.WHITE, null);
	}

	/**
	 * Creates the image depending on the direction.
	 */
	@Override
	public void init() {
	    try {
		// Direction up.
		if (direction == Main.DIRE_UP) {
		    // Up head.
		    this.head = ImageIO.read(new File("src/Images/Head/Up.jpg"));
		    // Direction down.
		} else if (direction == Main.DIRE_DOWN) {
		    // Down head.
		    this.head = ImageIO.read(new File("src/Images/Head/Down.jpg"));
		    // Direction left.
		} else if (direction == Main.DIRE_LEFT) {
		    // Left head.
		    this.head = ImageIO.read(new File("src/Images/Head/Left.jpg"));
		    // Direction right.
		} else if (direction == Main.DIRE_RIGHT) {
		    // Right head.
		    this.head = ImageIO.read(new File("src/Images/Head/Right.jpg"));
		}
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}

	/**
	 * This is just confusing... Detecting the contact based on the head
	 * location.
	 *
	 * @param snake The snake.
	 */
	@Override
	public void contact(Snake snake) {
	    // Sets the game state to message (if something is terriblly wrong???).
	    Main.state = Main.STATE_MESSAGE;

	    // If you see this you broke the game.
	    Main.message = "This can't happen???";
	}
    }

    /**
     * The stuff you eat!
     */
    public class Food implements Matter {

	/**
	 * Draw the food.
	 *
	 * @param g The graphics to draw to.
	 * @param pos The location to draw to.
	 */
	@Override
	public void draw(Graphics g, Position pos) {
	    // Draw in the background.
	    Matter.Backround backround = new Matter.Backround();
	    backround.draw(g, pos);

	    // Make it red (most pleasing colour for food!).
	    g.setColor(Color.red);

	    // Again just a circle.
	    // (pixel location in ..... Do I really need to do the again?).
	    g.fillOval(pos.getX() * Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
	}

	/**
	 * I could just get images inside the constructor then I would not need
	 * this.
	 */
	@Override
	public void init() {
	    // not needed for this object
	}

	/**
	 * Increases the length of the snake and increases score. Whoo!
	 * Something different!
	 *
	 * @param snake
	 */
	@Override
	public void contact(Snake snake) {
	    // Increase the length of the snake by one.
	    snake.add();

	    // Add numbers[zero] to the score.
	    Main.score++;
	}
    }

    /**
     * Special food: blue, random chance to do extra actions.
     */
    public class FoodSpecial implements Matter {
	/**
	 * Draw the special food.
	 * @param g The graphics to draw to.
	 * @param pos The location to draw to.
	 */
	@Override
	public void draw(Graphics g, Position pos) {
	    // MUST GO BACK!!
	    Matter.Backround backround = new Matter.Backround();
	    backround.draw(g, pos);
	    
	    // Make it blue!
	    g.setColor(Color.BLUE);
	    
	    // So much CIRCLE!!!!
	    g.fillOval(pos.getX() * Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
	}
	/**
	 * Ummmmm...
	 */
	@Override
	public void init() {
	    // not needed for this object.
	}
	/**
	 * When you run into a special food it does one of four random events.
	 * @param snake The snake to do stuff to.
	 */
	@Override
	public void contact(Snake snake) {
	    // Generate randomness
	    double rand = Math.random();
	    
	    // 10 % chance.
	    if (rand <= 0.1) {
		// Make the snake ten longer.
		snake.add(10);
		// Add one to score.
		Main.score++;
	    // 20 % chance.
	    } else if (rand <= 0.3) {
		// Add numbers[4] to score.
		Main.score += 5;
	    // 10 % chance.
	    } else if (rand <= 0.4) {
		// Subtract numbers[4] from score.
		Main.score -= 5;
	    // 60 % chance.
	    } else {
		// Make snake longer by one.
		snake.add();
		// Add numbers[0] to score.
		Main.score++;
	    }
	}
    }
}
