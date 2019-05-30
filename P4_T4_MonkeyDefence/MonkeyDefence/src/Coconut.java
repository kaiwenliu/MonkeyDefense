/*
 * Authors: Ethan Ling, Kevin Chu
 * Period: 4
 * Date: 5/15/2019
 * MonkeyDefence Capstone Project
 * Class: Coconut
 */

// This class extends Weapon, and it is the second strongest, fastest version of weapon, coconut
public class Coconut extends Weapon {
    // FIELDS:
    public static final int DAMAGE = 15;
    public static final double SPEED = 0.06;

    // CONSTRUCTOR:
    public Coconut(int xPos, int yPos, int targetX, int targetY) {
    	super(Coconut.class.getResource("/assets/Coconut.png"), DAMAGE, SPEED, xPos, yPos, targetX, targetY);
    }

}
