package Main;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage; //  WHAT.. no buffered image in Ready????
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The main program for the game. Warning: It may get complicated. I will try to
 * explain in the best way.
 *
 * Enjoy :)
 *
 * Without comments 433 lines. (I think this number shrank due to removing the action method)
 * With comments 884 lines.
 * (433 / 884 ) * 100 = 49 % increase!!!
 * 
 * Map: (52 / 97) * 100 = 54 %
 * Matter: (200 / 429) * 100 = 47 %
 * Methods: (41 / 98) * 100 = 42 %
 * Position: (55 / 119) * 100 = 46 %
 * PositionFood: (16 / 38) * 100 = 42 %
 * Score: (103 / 203) * 100 = 51 %
 * Snake: (72 / 144) * 100 = 50 %
 * 
 * 
 * Total Uncomment lines:
 * 433 + 52 + 200 + 41 + 55 + 16 + 103 + 72 = 972
 * Total Comment lines:
 * 900 + 97 + 429 + 98 + 119 + 38 + 203 + 144 = 2028
 * (972 / 2028) * 100 = 48 % Total Increase in file length!
 * 
 *
 * @author Daniel Krauskopf
 */
// extends Applet: use of the screen and drawing to it,
// implements Runnable: to allow for the Thread to keep running and keep the snake moving.
// implements KeyListener: allow for the keyboard input.
public class Main extends Applet implements Runnable, KeyListener {

    // The Thread object that is running to continually call the snake motions
    Thread main;

    // The image being drawn on the screen to remove screen flickering.
    // Everything is drawn to this image the the image is drawn to the screen.
    // This is called double buffering (in a cheap way).
    BufferedImage buffer;

    // The size of the window.
    public static final int[] WINDOW_SIZE = {500, 500};

    // The size of each cell or tile in the game (in pixels).
    public static final int CELL_SIZE = 10;
    // You can change both WINDOW_SIZE and CELL_SIZE but ensure that
    // WINDOW_SIZE / CELL_SIZE will give you the size of your map.
    // My maps are 50 by 50. (500 / 10 = 50, 500 / 10 = 50) :)

    // This is the storing of the Matter objects to place on the screen.
    Matter[][] tiles;

    // The map.
    public static Map map;

    // The Position of all of the food with an extra varible.
    // Boolean: true for special food, false for normal.
    PositionFood[] food;

    // The direction the snake is going.
    int direction;

    // the constants for direction.
    //    0
    //  3   1 
    //    2
    public static final int DIRE_UP = 0, DIRE_DOWN = 2, DIRE_LEFT = 3, DIRE_RIGHT = 1;

    // What the game is doing currentally.
    public static int state;

    // The constants for the state.
    // STATE_SETUP = before the menu (Just so that the menu is inactive while the program is still setting up).
    // STATE_MENU =  The main screen with the title and the higgh scores.
    // STATE_GAME = When the user is playingt he game.
    // STATE_PAUSE = When the user presses the space bar to pause the game (stops moving the snake).
    // STATE_SETTINGS = Never used (I wanted the user to change the game through the program).
    // STATE_MESSAGE = Draws the message String to the screen (for dieing and telling the user stuff).
    // STATE_NAME = As soon as the program starts to get the name from from the user.
    public static final int STATE_SETUP = 0, STATE_MENU = 1, STATE_GAME = 2, STATE_PAUSE = 3, STATE_SETTINGS = 4, STATE_MESSAGE = 5, STATE_NAME = 6;

    // The large menu font.
    public static final Font FONT_MENU = new Font(Font.SANS_SERIF, Font.PLAIN, 50);

    // The smaller font for underneith the title and the message String.
    public static final Font FONT_SUBTITLE = new Font(Font.SANS_SERIF, Font.PLAIN, 25);

    // The really small font for the scores.
    public static final Font FONT_SCORE = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

    // The snake to use in the game.
    Snake snake;

    // An array for the highscores.
    Score[] highScores;

    // The String to draw on the message game state.
    public static String message = "";

    // For creating a Score from the current game (the names are what they are).
    public static int level, score, length;

    // You only want to save the score once on the message screen. This makes that so.
    // A bug that I had was that the entire score board would be set to the new score.
    // True -> the score has been saved (dont do it again).
    // False -> the score has not been saveed (you need to do it).
    private boolean scoreSaved;

    // The users name.
    private String name = "";

    // For the typing of the users name. 
    // True -> shift pressed
    // False -> shift not pressed.
    private boolean shift;

    // For use with the message screen. If the name is too long the game state goes
    // to message and the message says that you name is too long. This checks to go
    // back to the menu (from death) or to name (name too long).
    // True -> name has been set correctally.
    // False -> name has not been set correctally.
    private boolean nameCorrect = false;

    // This is for the special food. Every five food you collect the next one becaome a
    // special food. This integer keeps track of that.
    private int foodCount;

    // And thats only the variables.
    /**
     * This method is run from the browser or application when the Applet should
     * start up.
     */
    //@Override
    public void init() {

	// Create the high scores (only 10 in the text file).
	highScores = new Score[10];

	// Get all of the hight scores.
	try {
	    // The BufferedReader for the high scores text file.
	    BufferedReader read = new BufferedReader(new FileReader("src/DataFiles/Highscores.txt"));

	    // Run through each high score position.
	    for (int i = 0; i < highScores.length; i++) {

		// Create a score from the one line from the text file.
		highScores[i] = new Score(read.readLine());
	    }
	    // Find errors?
	} catch (FileNotFoundException e) {
	} catch (IOException e) {
	}

	// The image to draw everything to.
	// Creates to be the same size of the window.
	// With RGB (red green blue) image type.
	buffer = new BufferedImage(Main.WINDOW_SIZE[0], Main.WINDOW_SIZE[1], BufferedImage.TYPE_INT_RGB);

	// Have to get the users name first.
	Main.state = Main.STATE_NAME;

	// Make the screen the correct size.
	resize(Main.WINDOW_SIZE[0], Main.WINDOW_SIZE[1]);

	// Listen to the keys from the keyboard (I wonder what song they are playing).
	addKeyListener(this);

	// Create the rest of the variables and go to level 0.d
	this.start(0);
    }

    /**
     * From Runnable.
     */
    //@Override
    public void start() {
	// Make a thread with this file.
	main = new Thread(this);

	// Start the thread.
	main.start();
    }

    /**
     * Initialize the variables for the given level.
     *
     * @param level The level to play the game in.
     */
    public void start(int level) {
	// Set the current level to the given level.
	Main.level = level;

	// Reset score.
	Main.score = 0;

	// Reset length.
	Main.length = 0;

	// The score has not been saved yet (it still has to be created).
	this.scoreSaved = false;

	// Reset the special food.
	this.foodCount = 0;

	// Make the tiles array (gets valus from the map) form the window size and the cell size.
	tiles = new Matter[Main.WINDOW_SIZE[0] / Main.CELL_SIZE][Main.WINDOW_SIZE[1] / Main.CELL_SIZE];

	// the starting direction of the snake.
	direction = Main.DIRE_RIGHT;

	// Make the snake.
	snake = new Snake(5, new Position(10, 10), direction);

	// The location of the five foods.
	food = new PositionFood[5];

	// Create the map from a text file.
	// Level zero.
	if (Main.level == 0) {
	    // Default map.
	    map = new Map();
	    // Level one.
	} else if (Main.level == 1) {
	    // Level one map.
	    map = new Map("src/Maps/Level_1.txt");
	    // Level two.
	} else if (Main.level == 2) {
	    // Level two map.
	    map = new Map("src/Maps/Level_2.txt");
	}

	// Give values to the tiles from the map.
	for (int i = 0; i < map.tiles.length; i++) {
	    for (int a = 0; a < map.tiles[0].length; a++) {
		tiles[i][a] = map.tiles[i][a];
	    }
	}

	// Now for the food.
	// Run through the food array.
	for (int i = 0; i < food.length; i++) {
	    // To ensure that the food is not positioned on somethin other then a background.
	    while (true) {

		// Creates a randdom Position for a normal food.
		food[i] = new PositionFood(new Position(Methods.rand(0, tiles.length), Methods.rand(0, tiles[0].length)), false);

		// If the random location chosen in the tiles array is a background.
		if (tiles[food[i].getX()][food[i].getY()] instanceof Matter.Backround) {

		    // Make the tiles locationa food.
		    tiles[food[i].getX()][food[i].getY()] = new Matter.Food();

		    // Exit the while
		    break;
		}
	    } // Will create anouther Position if there was something other then a background in that location.
	}

    } // Start

    /**
     * From Runnable: this is the loop that runs the snake.
     */
    //@Override
    public void run() {
	// The game loop
	// Will run until the Thread stops.
	while (Thread.currentThread() == main) {

	    // Do some calculating (moveing the snake, checking for food, saving the score.
	    this.calculate();

	    // Draw everything to the buffer image.
	    this.render(this.buffer);

	    // Draw the buffer image toe the screen.
	    this.drawImage(this.buffer);

	    // keep the program running at a constant(ish) speed.
	    try {
		main.sleep(100);
	    } catch (InterruptedException e) {
	    }
	}
    }

    /**
     * Draws the given image to the screen.
     *
     * @param image The image to draw.
     */
    public void drawImage(BufferedImage image) {
	try {
	    // Get the Graphics of the screen.
	    Graphics mainGraphics = getGraphics();

	    // Draw the image to the screen.
	    mainGraphics.drawImage(image, 0, 0, null);

	    // Get rid of the Graphics.
	    mainGraphics.dispose();
	} catch (NullPointerException e) {
	    // Nothing, Just for closing error caused by the applet closing
	    // during the running of the main loop.
	}
    }

    /**
     * Draws everything!
     *
     * @param image The image to draw to.
     */
    public void render(BufferedImage image) {
	// Get the Graphics from the image.
	Graphics bufferGraphics = image.getGraphics();

	// DRAW EVERYTHING //
	// In the game.
	if (Main.state == Main.STATE_GAME) {
	    // Run through each tile.
	    for (int i = 0; i < tiles.length; i++) {
		for (int a = 0; a < tiles[0].length; a++) {

		    // Draw the Matter using the image Graphics and the tile location.
		    tiles[i][a].draw(bufferGraphics, new Position(i, a));
		}
	    }
	    // In the menu.
	} else if (Main.state == Main.STATE_MENU) {

	    // Clear the image.
	    bufferGraphics.setColor(Color.BLACK);
	    bufferGraphics.fillRect(0, 0, Main.WINDOW_SIZE[0], Main.WINDOW_SIZE[1]);

	    // Draw the title with white colour.
	    bufferGraphics.setColor(Color.WHITE);

	    // In the centre.
	    Methods.drawString(bufferGraphics, "Snake Game", FONT_MENU);

	    // Draw the subtitle at a height 1.5 times the height of the font lower then the main title.
	    // (Bottom of main title + (int)height of font * 1.5, ... ).
	    Methods.drawString(((Main.WINDOW_SIZE[1] / 2) + (bufferGraphics.getFontMetrics(FONT_MENU).getHeight() / 2)) + (int) (bufferGraphics.getFontMetrics(FONT_SUBTITLE).getHeight() * 1.5), bufferGraphics, "BY: Daniel Krauskopf", FONT_SUBTITLE);

	    // Draw the labels for the score.
	    // (Bottom of subtitle - the score font height, ... "Name                Lv Sc Le").
	    //                                                  |----20 spaces------| to align evarything correctally (most of the time).
	    Methods.drawString((Main.WINDOW_SIZE[0] / 2) + (bufferGraphics.getFontMetrics(FONT_MENU).getHeight()) + (int) (bufferGraphics.getFontMetrics(FONT_SUBTITLE).getHeight() * 1.5) - (bufferGraphics.getFontMetrics(FONT_SCORE).getHeight()), bufferGraphics, "Name                Lv Sc Le", FONT_SCORE);

	    // Run through the pixel location of the font.
	    // start at 0 got to the number of high scores * the height of each line and increase by hte height of each line.
	    for (int i = 0; i < highScores.length * bufferGraphics.getFontMetrics(FONT_SCORE).getHeight(); i += bufferGraphics.getFontMetrics(FONT_SCORE).getHeight()) {
		// Draw the score.
		// (label location + the for loop offset, ... , String -> the next high score to draw with the getAll(), ... ).
		Methods.drawString((Main.WINDOW_SIZE[0] / 2) + (bufferGraphics.getFontMetrics(FONT_MENU).getHeight()) + (int) (bufferGraphics.getFontMetrics(FONT_SUBTITLE).getHeight() * 1.5) + i, bufferGraphics, highScores[i / bufferGraphics.getFontMetrics(FONT_SCORE).getHeight()].getAll(), FONT_SCORE);
	    }

	    // Draw the instructions on how to start the game.
	    // (height of font, ...). -> the top of the font "box" is at the top of the screen.
	    Methods.drawString(bufferGraphics.getFontMetrics(Main.FONT_SUBTITLE).getHeight(), bufferGraphics, "Press Space", Main.FONT_SUBTITLE);
	    // Game is paused.
	} else if (Main.state == Main.STATE_PAUSE) {
	    // Make it red.
	    bufferGraphics.setColor(Color.red);

	    // Draw the word paused on top of the game.
	    Methods.drawString(bufferGraphics, "Paused", FONT_MENU);

	    // Show the message.
	} else if (Main.state == Main.STATE_MESSAGE) {
	    // Clear the image.
	    bufferGraphics.setColor(Color.BLACK);
	    bufferGraphics.fillRect(0, 0, Main.WINDOW_SIZE[0], Main.WINDOW_SIZE[1]);

	    // Make it red.
	    bufferGraphics.setColor(Color.RED);

	    // Draw the message in the centre.
	    Methods.drawString(bufferGraphics, message, FONT_SUBTITLE);

	    // // Draw the instructions on how to start the game.
	    // (Window height - height of font, ...). -> just above the bottom of the screen.
	    Methods.drawString(Main.WINDOW_SIZE[1] - bufferGraphics.getFontMetrics(Main.FONT_SUBTITLE).getHeight(), bufferGraphics, "Press Space", Main.FONT_SUBTITLE);
	    // Get the name screen.
	} else if (Main.state == Main.STATE_NAME) {
	    // Clear the screen.
	    bufferGraphics.setColor(Color.BLACK);
	    bufferGraphics.fillRect(0, 0, Main.WINDOW_SIZE[0], Main.WINDOW_SIZE[1]);

	    // Make it white.
	    bufferGraphics.setColor(Color.WHITE);

	    // Tell the user what to do.
	    Methods.drawString(bufferGraphics, "Write your name", Main.FONT_MENU);

	    // Draw what they are typing (name).
	    // (Bottom of previos font + (int)subtitle font height * 1.5, ... ).
	    Methods.drawString((Main.WINDOW_SIZE[1] / 2) + (bufferGraphics.getFontMetrics(Main.FONT_MENU).getHeight() / 2) + (int) (bufferGraphics.getFontMetrics(Main.FONT_SUBTITLE).getHeight() * 1.5), bufferGraphics, this.name, Main.FONT_SUBTITLE);
	}

	// STOP DRAWING //
	// Get rid of the Graphics.
	bufferGraphics.dispose();
    }

    /**
     * Does all of the motion and math stuff for the game.
     */
    public void calculate() {

	// Playing the game.
	if (Main.state == Main.STATE_GAME) {

	    // IF your score become higher then 25 then you go onto the next level.
	    if (Main.score >= 25) {
		// There are only three levels.
		// Level two will not increase the level.
		if (Main.level < 2) {
		    // Increase the level.
		    Main.level++;

		    // Tell the user what has happended.
		    Main.message = "You have moved onto L" + Main.level;
		    Main.state = Main.STATE_MESSAGE;

		    // Start the next level.
		    start(Main.level);
		}
	    }

	    // Move the snake.
	    snake.moveSnake(direction);

	    // Reset the map.
	    for (int i = 0; i < map.tiles.length; i++) {
		for (int a = 0; a < map.tiles[0].length; a++) {
		    tiles[i][a] = map.tiles[i][a];
		}
	    }

	    // Run through each food position. 
	    for (int i = 0; i < food.length; i++) {
		// If its special...
		if (food[i].getSpecial()) {
		    /// Make it soo...
		    tiles[food[i].getX()][food[i].getY()] = new Matter.FoodSpecial();
		    // If its not...
		} else {
		    // Make it rot..
		    tiles[food[i].getX()][food[i].getY()] = new Matter.Food();
		}
	    }

	    // Get the Position's fot he snake.
	    Position[] snakePos = snake.getSnakePosition();

	    // Go through each tail peice (head also).
	    for (int i = snakePos.length - 1; i >= 0; i--) {

		// This is the head.
		if (i == 0) {
		    // You touched the object in front of you.
		    tiles[snakePos[i].getX()][snakePos[i].getY()].contact(this.snake);

		    // Now... All of this mess is for the food.
		    // My problem is that I cannont access the food position array
		    // from the Matter.Food.contact() method. So... Instead I check
		    // to see if the tile at the head location is a Matter.Food (also FoodSpecial).
		    // The instanceof boolean operator is uses for comparing objects.
		    // If the given object is the same as the given it become true
		    // For example:
		    // Card object -> NormalCard (suit, number)
		    // Card object -> Joker (colour)
		    // if (Card instanceof Joker)
		    // The Joker extends Card therefore it can be put into Card type
		    if (tiles[snakePos[i].getX()][snakePos[i].getY()] instanceof Matter.Food || tiles[snakePos[i].getX()][snakePos[i].getY()] instanceof Matter.FoodSpecial) {

			// Now that we have hit a food we now have to find the one the snake hit.
			// Go through the food array.
			for (int a = 0; a < food.length; a++) {

			    // If the stored value of the food is at the location of the head.
			    if (food[a].getX() == snakePos[i].getX() && food[a].getY() == snakePos[i].getY()) {
				// Snake hit this food (food[a]).

				// Add to the food count for the special food.
				this.foodCount++;

				// To ensure the correct placment of the food.
				// The food cant be on top the stuff in the map
				// or the snake.
				while (true) {

				    // Special food.
				    if (this.foodCount >= 5) {

					// Change the food position to a random location.
					// Also its special.
					food[a] = new PositionFood(new Position(Methods.rand(0, tiles.length), Methods.rand(0, tiles[0].length)), true);
					// Normal food.
				    } else {
					// Change the food position to a random location.
					food[a] = new PositionFood(new Position(Methods.rand(0, tiles.length), Methods.rand(0, tiles[0].length)), false);
				    }

				    // Now is the random location going to cover
				    // anything else? If so then the while goes again
				    // to generate a new position.
				    if (tiles[food[a].getX()][food[a].getY()] instanceof Matter.Backround) {
					// Just if the position is a BackGround.

					// Now set the tiles location to the food.
					if (this.foodCount >= 5) {
					    // Special food
					    tiles[food[a].getX()][food[a].getY()] = new Matter.FoodSpecial();

					    // Reset the counter.
					    this.foodCount = 0;
					} else {
					    // Normal food.
					    tiles[food[a].getX()][food[a].getY()] = new Matter.Food();
					}
					// With a correct random location leave the while loop.
					break;
				    }
				}

				// Leave the for loop (the snake can onlt hit
				// one food at a time.
				break;
			    }
			}
		    } // Not a food.

		    // Make the head in the new location.
		    tiles[snakePos[i].getX()][snakePos[i].getY()] = new Matter.SnakeHead(direction);
		    tiles[snakePos[i].getX()][snakePos[i].getY()].init();
		    // Not at position 0 int he for loop.
		} else {
		    // Make it a snake body.
		    tiles[snakePos[i].getX()][snakePos[i].getY()] = new Matter.SnakeBody();
		}
	    } // The end of the check for the snake.

	    // The message screen.
	} else if (Main.state == Main.STATE_MESSAGE) {
	    // Has the score been saved?
	    if (this.scoreSaved == false) {
		// No, save it.

		// Now the score is saved... Well about to be.
		this.scoreSaved = true;

		// Get the length of the current snake.
		Main.length = snake.getSnakePosition().length;

		// Sort the scores by the score value (the middle one).
		Score.sortByScore(highScores);

		// Knowing the scores are in order we can find where the
		// current score goes.
		// Go through each score stored.
		for (int i = 0; i < highScores.length; i++) {
		    // If the score is less then the current score.
		    if (highScores[i].getScore() < Main.score) {

			// Move all of the other scores downward in the list.
			for (int a = highScores.length - 1; a >= i; a--) {

			    // At the correct location for the current score
			    if (a == i) {
				// Set it to the current score.
				highScores[a] = new Score(this.name, Main.level, Main.score, Main.length);
				// Every other time.
			    } else {
				// Move the scores down (Starting at the bottom).
				highScores[a] = highScores[a - 1];
			    }
			}
			// Now the current score has been placed and dont need
			// to find a location for it.
			break;
		    }
		}

		// Get the next game ready to play.
		start(Main.level);
	    }
	}
    } // Done doing calculations.

    /**
     * When you type a key. Its an interface... You need all of the methods even
     * if you don't use them.
     *
     * @param e The key event.
     */
    //@Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * When you press a key on the keyboard. This is a big one!
     *
     * @param event The key event.
     */
    //@Override
    public void keyPressed(KeyEvent event) {
	// Get the key code as an integer.
	int key = event.getKeyCode();

	// If you pressed shift.
	if (key == KeyEvent.VK_SHIFT) {
	    // Set the shift boolean to true.
	    this.shift = true;

	    // The user needs to type in their name.
	} else if (Main.state == Main.STATE_NAME) {

	    // The user press backspace and the name length is not zero.
	    // You cant backspace when there is nothing there.
	    if (key == KeyEvent.VK_BACK_SPACE && name.length() != 0) {
		// Set the name to a section of the name that is from the 
		// beggining to one minus the end (remove the last character)
		name = name.substring(0, name.length() - 1);
		// The user presses enter to enter their name.
	    } else if (key == KeyEvent.VK_ENTER) {
		// Check the length of their name.
		if (name.length() > 20) {
		    // Make the message.
		    Main.message = "Your name is to long (20 characters or less)";

		    // Show the message.
		    Main.state = Main.STATE_MESSAGE;

		    // Name works!
		} else {
		    // Make the rest of t he program know the name is correct.
		    this.nameCorrect = true;

		    // Go to the menu.
		    Main.state = Main.STATE_MENU;
		}
		// THe user presses escape to close the program.
	    } else if (key == KeyEvent.VK_ESCAPE) {
		// Close the program.
		System.exit(0);

		// The user wants a space.
	    } else if (key == KeyEvent.VK_SPACE) {

		// Add a space.
		name += " ";
		// Every other key.
	    } else {
		// Every letter and number value.
		switch (key) {
		    case KeyEvent.VK_A: // The user pressed a.
			// If shift then capital A else small a.
			name += (this.shift) ? "A" : "a";
			// End of the a case.
			break;
		    // Repeat for each letter and number.
		    // For numbers there is no shift used.
		    case KeyEvent.VK_B:
			name += (this.shift) ? "B" : "b";
			break;
		    case KeyEvent.VK_C:
			name += (this.shift) ? "C" : "c";
			break;
		    case KeyEvent.VK_D:
			name += (this.shift) ? "D" : "d";
			break;
		    case KeyEvent.VK_E:
			name += (this.shift) ? "E" : "e";
			break;
		    case KeyEvent.VK_F:
			name += (this.shift) ? "F" : "f";
			break;
		    case KeyEvent.VK_G:
			name += (this.shift) ? "G" : "g";
			break;
		    case KeyEvent.VK_H:
			name += (this.shift) ? "H" : "h";
			break;
		    case KeyEvent.VK_I:
			name += (this.shift) ? "I" : "i";
			break;
		    case KeyEvent.VK_J:
			name += (this.shift) ? "J" : "j";
			break;
		    case KeyEvent.VK_K:
			name += (this.shift) ? "K" : "k";
			break;
		    case KeyEvent.VK_L:
			name += (this.shift) ? "L" : "l";
			break;
		    case KeyEvent.VK_M:
			name += (this.shift) ? "M" : "m";
			break;
		    case KeyEvent.VK_N:
			name += (this.shift) ? "N" : "n";
			break;
		    case KeyEvent.VK_O:
			name += (this.shift) ? "O" : "o";
			break;
		    case KeyEvent.VK_P:
			name += (this.shift) ? "P" : "p";
			break;
		    case KeyEvent.VK_Q:
			name += (this.shift) ? "Q" : "q";
			break;
		    case KeyEvent.VK_R:
			name += (this.shift) ? "R" : "r";
			break;
		    case KeyEvent.VK_S:
			name += (this.shift) ? "S" : "s";
			break;
		    case KeyEvent.VK_T:
			name += (this.shift) ? "T" : "t";
			break;
		    case KeyEvent.VK_U:
			name += (this.shift) ? "U" : "u";
			break;
		    case KeyEvent.VK_V:
			name += (this.shift) ? "V" : "v";
			break;
		    case KeyEvent.VK_W:
			name += (this.shift) ? "W" : "w";
			break;
		    case KeyEvent.VK_X:
			name += (this.shift) ? "X" : "x";
			break;
		    case KeyEvent.VK_Y:
			name += (this.shift) ? "Y" : "y";
			break;
		    case KeyEvent.VK_Z:
			name += (this.shift) ? "Z" : "z";
			break;
		    case KeyEvent.VK_0:
			name += "0";
			break;
		    case KeyEvent.VK_1:
			name += "1";
			break;
		    case KeyEvent.VK_2:
			name += "2";
			break;
		    case KeyEvent.VK_3:
			name += "3";
			break;
		    case KeyEvent.VK_4:
			name += "4";
			break;
		    case KeyEvent.VK_5:
			name += "5";
			break;
		    case KeyEvent.VK_6:
			name += "6";
			break;
		    case KeyEvent.VK_7:
			name += "7";
			break;
		    case KeyEvent.VK_8:
			name += "8";
			break;
		    case KeyEvent.VK_9:
			name += "9";
			break;
		} // Very Repeat, Much copy, WOW!
	    }
	    // During the game, menu, message states.
	} else {
	    // The program prevents the snake from going back on its self.
	    // However if you are fast enough you can do two motions in one.
	    // One right after the other. This is caused by the KeyListner
	    // methods being called during the running of the rest of the
	    // program.
	    // w / up key and not going down.
	    if ((key == KeyEvent.VK_W && direction != Main.DIRE_DOWN) || (key == KeyEvent.VK_UP && direction != Main.DIRE_DOWN)) {
		// Go up.
		direction = Main.DIRE_UP;

		// s / down key and not going up.
	    } else if ((key == KeyEvent.VK_S && direction != Main.DIRE_UP) || (key == KeyEvent.VK_DOWN && direction != Main.DIRE_UP)) {
		// Go down.
		direction = Main.DIRE_DOWN;

		// a / left key and not going right.
	    } else if ((key == KeyEvent.VK_A && direction != Main.DIRE_RIGHT) || (key == KeyEvent.VK_LEFT && direction != Main.DIRE_RIGHT)) {
		// Go left.
		direction = Main.DIRE_LEFT;

		// d / right key and not going left.
	    } else if ((key == KeyEvent.VK_D && direction != Main.DIRE_LEFT) || (key == KeyEvent.VK_RIGHT && direction != Main.DIRE_LEFT)) {
		// Go right.
		direction = Main.DIRE_RIGHT;

		// The user press escape to quit the game.
	    } else if (key == KeyEvent.VK_ESCAPE) {
		// Now the highscores are saved to the text file.
		try {
		    // Create writeing ability to the text file.
		    BufferedWriter write = new BufferedWriter(new FileWriter("src/DataFiles/Highscores.txt"));

		    // Clear everything in the text file.
		    write.write("");

		    // Run through each saved score.
		    for (int i = 0; i < highScores.length; i++) {
			// Add to the end of the text file the string for the score.
			write.append(highScores[i].save());

			// Go to the next line.
			write.newLine();
		    }

		    // Stop the writing to the text file.
		    write.close();
		    // Prevent errors.
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		// Then close the program.
		System.exit(0);

		// The user presses space.
	    } else if (key == KeyEvent.VK_SPACE) {
		// If the game is paused.
		if (Main.state == Main.STATE_PAUSE) {
		    // Unpause the game.
		    Main.state = Main.STATE_GAME;

		    // If the game is telling the user something.
		} else if (Main.state == Main.STATE_MESSAGE) {
		    // If the name is correct.
		    if (this.nameCorrect) {
			// Go to the menu.
			Main.state = Main.STATE_MENU;

			// The name is not correct.
		    } else {
			// Go to get the name.
			Main.state = Main.STATE_NAME;
		    }

		    // If the game is in the menu.
		} else if (Main.state == Main.STATE_MENU) {
		    // Start the game.
		    Main.state = Main.STATE_GAME;

		    // If the game is started.
		} else if (Main.state == Main.STATE_GAME) {
		    // Pause the game.
		    Main.state = Main.STATE_PAUSE;
		}
	    }
	}
    }

    /**
     * When you release a key. Just used for shift.
     *
     * @param event The key event.
     */
    //@Override
    public void keyReleased(KeyEvent event) {
	// Get the key as an integer.
	int key = event.getKeyCode();

	// If you relased shift.
	if (key == KeyEvent.VK_SHIFT) {

	    // Set shift to false.
	    this.shift = false;
	}
    }
}
 // Whoo!!!
