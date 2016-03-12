package Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * The map object:
 * creates a retrievable array of what to draw on the background and
 * interact with the snake.
 * 
 * 52 lines -> 97
 * (52 / 97) * 100 = 54% increase.
 * 
 * @author Daniel Krauskopf
 */
public class Map {

    /**
     * The location of the default map.
     */
    private static final String DEFAULT_MAP = "src/Maps/Default.txt";
    /**
     * All of the tiles to be drawn to the screen as a 2D array.
     */
    public Matter[][] tiles;

    /**
     * A constructor that uses the default map.
     */
    public Map() {
	this(Map.DEFAULT_MAP);
    }

    /**
     * A constructor that creates the tiles array with the given location of the map file.
     * @param fileLocation The location of the map (text) file.
     */
    public Map(String fileLocation) {
	try {
	    // read from the map file
	    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));
	    
	    // gets the first line in the map file (size of the map)
	    String mapln = bufferedReader.readLine();
	    
	    // splits the line into x then y sizes
	    StringTokenizer map = new StringTokenizer(mapln);
	    
	    // creates the tiles array with the map sizes
	    tiles = new Matter[Integer.parseInt(map.nextToken())][Integer.parseInt(map.nextToken())];
	    
	    // runs through the x direction of the tiles array
	    for (int i = 0; i < tiles.length; i++) {
		// gets the current line form the map file
		mapln = bufferedReader.readLine();
		
		// slpits the line into each number
		map = new StringTokenizer(mapln);
		
		// runs through the y direction of the tiles array
		for (int a = 0; a < tiles[0].length; a++) {
		    String token = map.nextToken();
		    
		    /* depending on the value in the map file sets the current
		     * tiles location to a new Matter object.
		     */
		    // background
		    if (token.compareToIgnoreCase("0") == 0) {
			tiles[a][i] = new Matter.Backround();
			tiles[a][i].init();
			
		    // bush
		    } else if (token.compareToIgnoreCase("1") == 0) {
			tiles[a][i] = new Matter.Bush();
			tiles[a][i].init();
			
		    // rock
		    } else if (token.compareToIgnoreCase("2") == 0) { // rock
			tiles[a][i] = new Matter.Rock();
			tiles[a][i].init();
		    }
		}
	    }
	/* just to ensure that it all goes well.
	 * I could make it tell the user but I dont think that is 
	 * needed right now.
	 */
	} catch (FileNotFoundException e) {
	} catch (IOException e) {
	}

    }
}
