import java.net.URL;

/*
 * Authors: Ethan Ling, Kevin Chu
 * Period: 4
 * Date: 5/15/2019
 * MonkeyDefence Capstone Project
 * Class: Weapon
 */

// This class represents a Weapon, which is thrown at enemies to defeat them
public class Weapon extends MovingImage {
    // FIELDS:
    private int damage, targetX, targetY, quadrant;
    private double xMovement, yMovement, moveRatio, speed;
    private boolean yFavor;

    // CONSTRUCTOR:
    public Weapon(URL location, int damage, double speed, int xPos, int yPos, int targetX, int targetY) {
        super(location, xPos, yPos, 30, 30);
        this.damage = damage;
        this.speed = speed;
        this.targetX = targetX;
        this.targetY = targetY;
        this.moveRatio = Math.abs((double) targetX / (double) targetY);
        if (moveRatio < 1) {
            yFavor = true;
        }

        if (targetX >= 0 && targetY > 0) {
            quadrant = 4;
        } else if (targetX < 0 && targetY > 0) {
            quadrant = 3;
        } else if (targetX < 0 && targetY <= 0) {
            quadrant = 2;
        } else if (targetX >= 0 && targetY <= 0) {
            quadrant = 1;
        }

        xMovement = 0;
        yMovement = 0;
    }

    // METHODS:
    public int getDamage() {
        return damage;
    }

    public void accelerate() {
        switch (quadrant) {
            case 1:
                if (yFavor) {
                    yMovement -= speed * (1 / moveRatio);
                    xMovement += speed;
                } else {
                    xMovement += speed * moveRatio;
                    yMovement -= speed;
                }
                break;
            case 2:
                if (yFavor) {
                    yMovement -= speed * (1 / moveRatio);
                    xMovement -= speed;
                } else {
                    xMovement -= speed * moveRatio;
                    yMovement -= speed;
                }
                break;
            case 3:
                if (yFavor) {
                    yMovement += speed * (1 / moveRatio);
                    xMovement -= speed;
                } else {
                    xMovement -= speed * moveRatio;
                    yMovement += speed;
                }
                break;
            case 4:
                if (yFavor) {
                    yMovement += speed * (1 / moveRatio);
                    xMovement += speed;
                } else {
                    xMovement += speed * moveRatio;
                    yMovement += speed;
                }
                break;
        }
    }

    public void update() {
        moveByAmount((int) xMovement, (int) yMovement);
    }
}