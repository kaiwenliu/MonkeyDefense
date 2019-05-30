//Imports
import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*
 * Authors: Ethan Ling, Kevin Chu
 * Period: 4
 * Date: 5/28/2019
 * MonkeyDefence Capstone Project
 * Class: SplashScreen
 */


public class SplashScreen extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        String msg ="'";

        g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
        g.drawString("MonkeyDefence ", 20 * getWidth() / 100, 30 * getHeight() / 99);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.drawString("Loading...", 45 * getWidth() / 100, 50 * getHeight() / 99);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        Image gorillo = Toolkit.getDefaultToolkit().getImage(CrazyEnemy.class.getResource("/assets/Gorillo.png"));
        Image monkey = Toolkit.getDefaultToolkit().getImage(CrazyEnemy.class.getResource("/assets/Monkey.png"));
        Image poop = Toolkit.getDefaultToolkit().getImage(CrazyEnemy.class.getResource("/assets/Poop.png"));
        Image image = null;

        Random rand = new Random();
        int tipType = rand.nextInt(5); //used to pick the type of tip
        switch (tipType){
            case 0:
                msg = "Weapons are a one-time purchase! ";
                image = poop;
                break;
            case 1:
                msg = "There are two types of Gorillos: Crazy and Smart ";
                image = gorillo;
                break;
            case 2:
                msg = "Gorillos only come from the edge, Stay Away! ";
                image = gorillo;
                break;
            case 3:
                msg = "Low on health? Buy a Heal!";
                image = monkey;
                break;
            case 4:
                msg = "Kevin Rossel gave inspiration for a Monkey game, kudos to him!";
                image = monkey;
                break;
        }

        g.drawString("Tip: " + msg, 30 * getWidth() / 100, 70 * getHeight() / 99);
        g.drawImage(image,getWidth()/2-50,getHeight()/2+200,100,100,this);

    }

}
