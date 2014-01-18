package Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author Daniel Krauskopf
 */
public class Map {

    private static final String DEFAULT_MAP = "src/Maps/Default.txt";
    public Matter[][] tiles;

    public Map() {
        this(Map.DEFAULT_MAP);
    }

    public Map(String fileLocation) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));

            String mapln = bufferedReader.readLine();
            StringTokenizer map = new StringTokenizer(mapln);
            tiles = new Matter[Integer.parseInt(map.nextToken())][Integer.parseInt(map.nextToken())];

            for (int i = 0; i < tiles.length; i++) {
                mapln = bufferedReader.readLine();
                map = new StringTokenizer(mapln);
                for (int a = 0; a < tiles[0].length; a++) {
                    String token = map.nextToken();
                    if (token.compareToIgnoreCase("0") == 0) { //  backround
                        tiles[a][i] = new Matter.Backround();
                        tiles[a][i].init();
                    } else if (token.compareToIgnoreCase("1") == 0) { // bush
                        tiles[a][i] = new Matter.Bush();
                        tiles[a][i].init();
                    } else if (token.compareToIgnoreCase("2") == 0) { // rock
                        tiles[a][i] = new Matter.Rock();
                        tiles[a][i].init();
                    }
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }


    }
}
