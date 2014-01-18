package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Daniel Krauskopf
 */
public interface Matter {

    public void draw(Graphics g, Position pos);

    public void init();

    public void contact();

    public class Backround implements Matter {

        @Override
        public void draw(Graphics g, Position cell) {
            g.setColor(new Color(210, 187, 0));
            g.fill3DRect(cell.getX() * Main.CELL_SIZE, cell.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE, true);
        }

        @Override
        public void init() {
            // not needed for this object
        }

        @Override
        public void contact() {
            // do nothing (move through backround)
        }
    }

    public class Bush implements Matter {

        BufferedImage bush;

        @Override
        public void draw(Graphics g, Position pos) {
            g.drawImage(bush, pos.getX() * Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE, pos.getX() * Main.CELL_SIZE + Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE + Main.CELL_SIZE, 0, 0, bush.getWidth(), bush.getHeight(), Color.WHITE, null);
        }

        @Override
        public void init() {
            try {
                bush = ImageIO.read(new File("src/Images/Bush.jpg"));
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        @Override
        public void contact() {
            // TODO: die method to kill and reset
            //die("bush");
        }
    }
    
    public class Rock implements Matter {

        @Override
        public void draw(Graphics g, Position pos) {
            Matter.Backround backround = new Matter.Backround();
            backround.draw(g, pos);
            g.setColor(new Color(125, 150, 125));
            g.fillOval(pos.getX() * Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
        }

        @Override
        public void init() {
            // not needed for this object
        }

        @Override
        public void contact() {
            // TODO: die method to kill and reset
            System.out.println("rock");
        }
        
    }

    public class SnakeBody implements Matter {

        @Override
        public void draw(Graphics g, Position pos) {
            Matter.Backround backround = new Matter.Backround();
            backround.draw(g, pos);
            g.setColor(Color.BLACK);
            g.fillOval(pos.getX() * Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
        }

        @Override
        public void init() {
            // not needed for this object
        }

        @Override
        public void contact() {
            // TODO: die method to kill and reset
            System.out.println("yourself");
        }
    }

    public class SnakeHead implements Matter {

        BufferedImage head;
        int direction;

        public SnakeHead(int direction) {
            this.direction = direction;
        }

        @Override
        public void draw(Graphics g, Position pos) {
            g.drawImage(head, pos.getX() * Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE, pos.getX() * Main.CELL_SIZE + Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE + Main.CELL_SIZE, 0, 0, head.getWidth(), head.getHeight(), Color.WHITE, null);
        }

        @Override
        public void init() {
            try {
                head = ImageIO.read(new File("src/Images/Head.jpg"));
                if (direction == Main.DIRE_UP) {
                    // nothing (image is already facing up
                } else if (direction == Main.DIRE_DOWN) {
                    head = Methods.rotate(head, Math.toRadians(180));
                } else if (direction == Main.DIRE_LEFT) {
                    head = Methods.rotate(head, Math.toRadians(270));
                } else if (direction == Main.DIRE_RIGHT) {
                    head = Methods.rotate(head, Math.toRadians(90));
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        @Override
        public void contact() {
            System.out.println("Ummmm.... you are your head. you cannot contact with it as well");
        }
    }

    public class Food implements Matter {

        @Override
        public void draw(Graphics g, Position pos) {
            Matter.Backround backround = new Matter.Backround();
            backround.draw(g, pos);
            g.setColor(Color.red);
            g.fillOval(pos.getX() * Main.CELL_SIZE, pos.getY() * Main.CELL_SIZE, Main.CELL_SIZE, Main.CELL_SIZE);
        }

        @Override
        public void init() {
            // not needed for this object
        }

        @Override
        public void contact() {
            System.out.println("mmmmm... tastey");
        }
    }
}
