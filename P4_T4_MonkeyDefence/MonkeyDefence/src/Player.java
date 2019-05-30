/*
 * Authors: Ethan Ling, Kevin Chu
 * Period: 4
 * Date: 5/13/2019
 * MonkeyDefence Capstone Project
 * Class: Player
 */

public class Player extends MovingImage {

    // == FIELDS ==
    private int health;
    private double xMovement, yMovement;

    // == CONSTRUCTOR ==
    public Player(int xPos, int yPos, int health) {
        super(Player.class.getResource("/assets/Monkey.png"), xPos, yPos, 50, 50);
        xMovement = 0;
        yMovement = 0;
        this.health = health;
    }

    // == METHODS ==

    //Calulates y-movement
     void walkX(int dir) {
        if (Math.abs(xMovement) < 4)
            xMovement += dir;
    }

    //Calulates x-movement
    public void walkY(int dir) {
        if (Math.abs(yMovement) < 4) {
            yMovement += dir;
        }
    }

    //Moves the player
    public void update() {
        xMovement *= 0.9;
        yMovement *= 0.9;
        moveByAmount((int) xMovement, (int) yMovement);
    }

    //Getters and Setters for health
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}