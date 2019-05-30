/*
 * Authors: Ethan Ling, Kevin Chu
 * Period: 4
 * Date: 5/28/2019
 * MonkeyDefence Capstone Project
 * Class: Enemy
 */

public class SmartEnemy extends Enemy {

    // == CONSTRUCTOR ==
    public SmartEnemy(int xPos, int yPos, int targetX, int targetY, double health, double damage, double speed) {
    	super(SmartEnemy.class.getResource("/assets/SmartGorillo.png"), xPos, yPos, targetX, targetY, health, damage, speed);
    }

    // == METHODS ==
    //Calulates chasing a player (following)
    public void chase(Player target) {
        int chaseTargetX = target.getX();
        int chaseTargetY = target.getY();
        int xDiff, yDiff;
        xDiff = chaseTargetX - getX();
        yDiff = chaseTargetY - getY();

        //To fix a divide by zero bug
        if (xDiff == 0) {
            xDiff = 1;
        }
        double slope = yDiff / xDiff;

        //Caculates x and y Movement
        if (getX() < chaseTargetX) {
            xMovement = speed;
            yMovement = slope * xMovement;
        } else if (getX() > chaseTargetX) {
            xMovement = -1 * speed;
            yMovement = slope * speed;
        } else {
            if (getY() > chaseTargetY) {
                xMovement = 0;
                yMovement = -1 * speed;
            } else {
                xMovement = 0;
                yMovement = speed;
            }
        }
        //used to slow down enemy when slope is around 2
        if (xMovement != 0 && (yMovement != 0) && (Math.abs(yMovement) > 2 * xMovement)) {
            yMovement = 2 * xMovement * (yMovement / Math.abs(yMovement));
        }
    }
}
