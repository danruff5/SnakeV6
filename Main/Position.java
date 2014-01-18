package Main;

/**
 *
 * @author Daniel Krauskopf
 */

public class Position {

 
    private int x;
  
    private int y;

    public Position() {
        this(0, 0);
    }


    public Position(int width, int height) {
        this.x = width;
        this.y = height;
    }
    

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void addX () {
        this.addX(1);
    }
    
    public void addX (int value) {
        this.x += value;
    }
    
    public void addY () {
        this.addY(1);
    }
    
    public void addY (int value) {
        this.y += value;
    }
    
    public void setX (int value) {
        this.x = value;
    }
    
    public void setY (int value) {
        this.y = value;
    }
}
