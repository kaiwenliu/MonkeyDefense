//Imports
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;

/*
 * Represents a moving, appearing/disappearing image.
 *
 * by: Shelby
 * on: 5/3/13
 *
 *  Altered by Ethan Ling and Kevin Chu for MongyDefence
 */

public class MovingImage {

    // FIELDS

    private int x, y;
    private int width, height;
    private Image image;
    private boolean isVisible;

    // CONSTRUCTORS

    public MovingImage(String filename, int x, int y, int w, int h) {
        this((new ImageIcon(filename)).getImage(), x, y, w, h);
    }
    
    public MovingImage(URL location, int x, int y, int w, int h) {
    	this((new ImageIcon(location)).getImage(), x, y, w, h);
    }

    public MovingImage(Image img, int x, int y, int w, int h) {
        image = img;
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        isVisible = true;
    }

    // METHODS
    //Moves image my amount
    public void moveByAmount(int x, int y) {
        this.x += x;
        this.y += y;
    }
    //Apllies the limits that the enitity can move
    public void applyWindowLimits(int windowWidth, int windowHeight) {
        x = Math.min(x, windowWidth - this.width);
        y = Math.min(y, windowHeight - this.height);
        x = Math.max(0, x);
        y = Math.max(0, y);
    }

    //Checks if a point is in an image
    public boolean isPointInImage(int mouseX, int mouseY) {
        if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height)
            return true;
        return false;
    }

    //Checks if image is in image
    public boolean isIntersect(MovingImage image) {
        if (isPointInImage(image.getX(), image.getY()) ||
                isPointInImage(image.getX(), image.getY() + image.getHeight()) ||
                isPointInImage(image.getX() + getWidth(), image.getY()) ||
                isPointInImage(image.getX() + image.getWidth(), image.getY() + image.getHeight())) {
            return true;
        } else {
            return false;
        }
    }

    //Draws the image
    public void draw(Graphics g, ImageObserver io) {
        if (isVisible)
            g.drawImage(image, x, y, width, height, io);
    }

    //Getters and Setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isVisible() {
        return isVisible;
    }

}