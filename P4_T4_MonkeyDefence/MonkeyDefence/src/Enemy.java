import java.net.URL;

/*
 * Authors: Ethan Ling, Kevin Chu
 * Period: 4
 * Date: 5/28/2019
 * MonkeyDefence Capstone Project
 * Class: Enemy
 */

public abstract class Enemy extends MovingImage {

    // == FIELD ==
    public double xMovement, yMovement, speed, damage, health;

    // == CONSTRUCTOR ==
    public Enemy(URL location, int xPos, int yPos, int targetX, int targetY,double health, double damage, double speed) {
        super(location, xPos, yPos, 50, 50);
        xMovement = 0;
        yMovement = 0;
        this.health = health;
        this.damage = damage;
        this.speed = speed;

        xMovement = 0;
        yMovement = 0;
    }

    //Abstract chase method
    public abstract void chase(Player target);

    //Get and Set for Health
    public double getHealth() {
        return health;
    }

    public void loseHealth(int lostHealth) {
        this.health = health - lostHealth;
    }

    //Moves the enemy
    public void update() {
        moveByAmount((int) xMovement, (int) yMovement);
    }
}