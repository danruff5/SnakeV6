package Main;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 *
 * @author Daniel Krauskopf
 */
public class Main extends Applet implements Runnable, KeyListener {

    Thread main;
    BufferedImage buffer;
    private final int[] WINDOW_SIZE = {500, 500};
    public static final int CELL_SIZE = 10;
    Matter[][] tiles;
    Map map;
    Position[] snake, food;
    int direction;
    public static final int DIRE_UP = 0, DIRE_DOWN = 2, DIRE_LEFT = 3, DIRE_RIGHT = 1;

    @Override
    public void init() {
        resize(this.WINDOW_SIZE[0], this.WINDOW_SIZE[1]);
        buffer = new BufferedImage(this.WINDOW_SIZE[0], this.WINDOW_SIZE[1], BufferedImage.TYPE_INT_RGB);
        tiles = new Matter[this.WINDOW_SIZE[0] / Main.CELL_SIZE][this.WINDOW_SIZE[1] / Main.CELL_SIZE];
        snake = new Position[20];
        food = new Position[50];

        direction = Main.DIRE_RIGHT;

        for (int i = 0; i < tiles.length; i++) {
            for (int a = 0; a < tiles[0].length; a++) {
                tiles[i][a] = new Matter.Backround();
                tiles[i][a].init();
            }
        }

        map = new Map();
        for (int i = 0; i < map.tiles.length; i ++ ){
            System.arraycopy(map.tiles[i], 0, tiles[i], 0, map.tiles[0].length);
        }

        for (int i = 0; i < snake.length; i++) {
            if (i == 0) {
                snake[i] = new Position((snake.length - 1) - i, 0);
                tiles[snake[i].getX()][snake[i].getY()] = new Matter.SnakeHead(Main.DIRE_RIGHT);
                tiles[snake[i].getX()][snake[i].getY()].init();
            } else {
                snake[i] = new Position((snake.length - 1) - i, 0);
                tiles[snake[i].getX()][snake[i].getY()] = new Matter.SnakeBody();
                tiles[snake[i].getX()][snake[i].getY()].init();
            }
        }

        for (int i = 0; i < food.length; i++) {
            while (true) {
                food[i] = new Position(Methods.rand(0, tiles.length), Methods.rand(0, tiles[0].length));
                if (tiles[food[i].getX()][food[i].getY()] instanceof Matter.Backround) {
                    tiles[food[i].getX()][food[i].getY()] = new Matter.Food();
                    break;
                }
            }
        }

        addKeyListener(this);
    }

    @Override
    public void start() {
        main = new Thread(this);
        main.start();
    }

    @Override
    public void run() {
        while (Thread.currentThread() == main) {

            this.calculate();
            this.render(this.buffer);
            this.drawImage(this.buffer);

            try {
                main.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    public void drawImage(BufferedImage image) {
        try {
            Graphics mainGraphics = getGraphics();
            mainGraphics.drawImage(image, 0, 0, null);
            mainGraphics.dispose();
        } catch (NullPointerException e) {
            // Nothing, Just for closing error caused by the applet closing
            // during the running of the main loop.
        }
    }

    public void render(BufferedImage image) {
        Graphics bufferGraphics = image.getGraphics();

        // draw everything //
        for (int i = 0; i < tiles.length; i++) {
            for (int a = 0; a < tiles[0].length; a++) {
                tiles[i][a].draw(bufferGraphics, new Position(i, a));
            }
        }
        // stop drawing //

        bufferGraphics.dispose();
    }

    public void calculate() {
        for (int i = snake.length - 1; i >= 0; i--) {
            if (i == snake.length - 1) {
                tiles[snake[i].getX()][snake[i].getY()] = map.tiles[snake[i].getX()][snake[i].getY()];;
            }
            if (i == 0) {
                tiles[snake[i].getX()][snake[i].getY()] = new Matter.SnakeBody();
                if (direction == Main.DIRE_UP) {
                    snake[i].addY(-1);
                } else if (direction == Main.DIRE_DOWN) {
                    snake[i].addY();
                } else if (direction == Main.DIRE_LEFT) {
                    snake[i].addX(-1);
                } else if (direction == Main.DIRE_RIGHT) {
                    snake[i].addX();
                }
                tiles[snake[i].getX()][snake[i].getY()].contact();
                tiles[snake[i].getX()][snake[i].getY()] = new Matter.SnakeHead(direction);
                tiles[snake[i].getX()][snake[i].getY()].init();
                
            } else {
                snake[i].setX(snake[i - 1].getX());
                snake[i].setY(snake[i - 1].getY());
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
        if (key == KeyEvent.VK_W && direction != Main.DIRE_DOWN) {
            direction = Main.DIRE_UP;
        } else if (key == KeyEvent.VK_S && direction != Main.DIRE_UP) {
            direction = Main.DIRE_DOWN;
        } else if (key == KeyEvent.VK_A && direction != Main.DIRE_RIGHT) {
            direction = Main.DIRE_LEFT;
        } else if (key == KeyEvent.VK_D && direction != Main.DIRE_LEFT) {
            direction = Main.DIRE_RIGHT;
        } else if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void die (String message) {
        System.out.println(message);
    }
}
