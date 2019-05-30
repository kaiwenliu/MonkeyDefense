//Imports
import java.util.Random;

/*
 * Authors: Ethan Ling, Kevin Chu
 * Period: 4
 * Date: 5/28/2019
 * MonkeyDefence Capstone Project
 * Class: CrazyEnemy
 */

public class CrazyEnemy extends Enemy {

    // CONSTRUCTOR:
    public CrazyEnemy(int xPos, int yPos, int targetX, int targetY, double health, double damage, double speed) {
    	super(CrazyEnemy.class.getResource("/assets/Gorillo.png"), xPos, yPos, targetX, targetY, health, damage, speed);
        

    }

    // METHODS:
    //Makes enemy move (random movements)
    public void chase(Player target) {

        Random rand = new Random();
        xMovement = rand.nextInt(getWidth());
        yMovement = rand.nextInt(65 * getHeight() / 99);


        int choice = rand.nextInt(4);
        switch (choice) {
            case 0:
                xMovement *= 1;
                yMovement *= 1;
                break;
            case 1:
                xMovement *= -1;
                break;
            case 2:
                xMovement *= -1;
                yMovement *= -1;
                break;
            case 3:
                yMovement *= -1;
                break;
        }
    }
}
