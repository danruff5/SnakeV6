package Main;

import java.util.StringTokenizer;

/**
 * Stores the values of the scores.
 * 
 * 103 -> 203
 * (103 / 203) * 100 = 51 %
 *
 * @author Daniel Krauskopf
 */
public class Score {

    /**
     * The name of the user.
     */
    private final String name;
    /**
     * What their level is.
     */
    private final int level;
    /**
     * What their score is.
     */
    private final int score;
    /**
     * The length of the snake.
     */
    private final int length;

    /**
     * Creates everything in the score.
     *
     * @param name The name.
     * @param level The level.
     * @param score The score.
     * @param length The length of the snake.
     */
    public Score(String name, int level, int score, int length) {
        this.name = name;
        this.level = level;
        this.score = score;
        this.length = length;
    }

    /**
     * Creates a score from a single line (from test file) in the form of:
     * "Name" (int)level (int)score (int)length
     *
     * @param lineFromText
     */
    public Score(String lineFromText) {
        // Split the line up.
        StringTokenizer token = new StringTokenizer(lineFromText);

        // The first is the name.
        // But to accomidate spaces in names all spaces become underscores.
        // Here it replaces underscores with spaces.
        this.name = token.nextToken().replace("_", " ");

        // The next token is the level.
        this.level = Integer.parseInt(token.nextToken());

        // the third token is the score.
        this.score = Integer.parseInt(token.nextToken());

        // The last token is the length.
        this.length = Integer.parseInt(token.nextToken());
    }

    /**
     * Access the name given to the score.
     *
     * @return The name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Access the level given to the score.
     *
     * @return The level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Access the score given to the score.
     *
     * @return The score.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * access the length given to the score.
     *
     * @return The length.
     */
    public int getLength() {
        return this.length;
    }

    /**
     * For printing the score to the screen.
     *
     * @return The formatted string.
     */
    public String getAll() {
        // How big must the space be to have everything look proper.
        int numSpace = 20 - this.getName().length();

        // First the name.
        String ret = this.getName();

        // Then a number of spaces to keep everything even.
        for (int i = 0; i < numSpace; i++) {
            ret += " ";
        }

        // Then the level, score, length (always of length two).
        // if the number is less then 10 add a zero in fron of it.
        ret += " " + ((this.getLevel() >= 10) ? Integer.toString(this.getLevel()) : "0" + Integer.toString(this.getLevel()));
        ret += " " + ((this.getScore() >= 10) ? Integer.toString(this.getScore()) : "0" + Integer.toString(this.getScore()));
        ret += " " + ((this.getLength() >= 10) ? Integer.toString(this.getLength()) : "0" + Integer.toString(this.getLength()));

        // Return the complete string.
        return ret;
    }

    /**
     * Mae the score into a format that can be save into a text file.
     *
     * @return The formatted string.
     */
    public String save() {
        // Have to start with something.
        String ret = "";

        // Check to see if the name has any spaces.
        StringTokenizer token = new StringTokenizer(this.getName());
        // If theres only one word (token) then this for loop will not run.
        // Other wise it adds the name with underscores as spaces.
        for (int i = 0; i < token.countTokens() - 1; i++) {
            ret += token.nextToken() + "_";
        }

        // The last part of the name (or first if there are no spaces) plus
        // a space to seperate the data.
        ret += token.nextToken() + " ";

        // Add the level, score, and length all with seperating spaces.
        ret += Integer.toString(this.getLevel()) + " ";
        ret += Integer.toString(this.getScore()) + " ";
        ret += Integer.toString(this.getLength());

        // Return the string.
        return ret;
    }

    /**
     * Sorts the scores by the score value (largest at position one). Cocktail
     * sort.
     *
     * @param toSort The score array to sort.
     */
    public static void sortByScore(Score[] toSort) {
        int count = toSort.length - 1;
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int up = 0; up < count; up++) {
                if (toSort[up].getScore() < toSort[up + 1].getScore()) {
                    swap(toSort, up, up + 1);
                    sorted = false;
                }
            }
            for (int down = count; down < 0; down--) {
                if (toSort[down].getScore() < toSort[down - 1].getScore()) {
                    swap(toSort, down, down - 1);
                    sorted = false;
                }
            }
            count--;
        }
    }
    /**
     * A swap method for score objects.
     * @param toSort The entire array.
     * @param index1 The location of the first one.
     * @param index2 The location of the second one.
     */
    private static void swap(Score[] toSort, int index1, int index2) {
        Score temp = toSort[index1];
        toSort[index1] = toSort[index2];
        toSort[index2] = temp;
    }
}
