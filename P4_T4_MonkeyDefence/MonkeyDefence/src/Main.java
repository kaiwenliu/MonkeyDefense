//Imports

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

/*
 * Authors: Ethan Ling, Kevin Chu
 * Period: 4
 * Date: 5/28/2019
 * MonkeyDefence Capstone Project
 * Class: Main
 */

public class Main extends JPanel implements ActionListener, KeyListener, MouseListener {

    // ==FIELDS==
    private static final int DRAWING_WIDTH = 1000; //Width of Window
    private static final int DRAWING_HEIGHT = 750; //Height of Window

    private final Font FONT = new Font("ComicSans", Font.PLAIN, 20);
    private final int BANANA = 1;
    private final int COCONUT = 2;
    private final int POOP = 3;

    private final double SMART_ENEMY_MAX = 20.0;
    private final double CRAZY_ENEMY_MAX = 11.0;
    private final int PLAYER_HEALTH_MAX = 400;

    private Color grassColor = new Color(86, 176, 0);

    private Player monkey;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Weapon> weapons = new ArrayList<>();

    private JButton bananaButton = new JButton("Banana");
    private JButton coconutButton = new JButton("Coconut ($1000)");
    private JButton poopButton = new JButton("Poop ($2000");
    private JButton healButton = new JButton("Heals ($500)");
    private JButton restartButton = new JButton("Restart?");

    private File highScoreFile = new File("src/assets/highScore.txt");
    
    private boolean coconutUnlocked;
    private boolean poopUnlocked;
    private boolean upKeyPressed, downKeyPressed, leftKeyPressed, rightKeyPressed, mousePressed;
    private boolean gameOver = false;

    private int money;
    private int playerHealth;
    private int highScore;
    private int currentWeapon;
    private int score;
    private int mouseX, mouseY;
    private int numberOfEnemies = 1;

    // ==CONSTRUCTOR==
    private Main() {
        money = 0;
        playerHealth = PLAYER_HEALTH_MAX;
        try {
        	System.out.println(highScoreFile.getAbsolutePath());
			BufferedReader f = new BufferedReader(new FileReader(highScoreFile));
			highScore = Integer.parseInt(f.readLine());
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        score = 0;

        coconutUnlocked = false;
        poopUnlocked = false;
        currentWeapon = BANANA;

        monkey = new Player(360, 200, playerHealth);

        playMusic();

        this.setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        //Adds the buttons, but does not draw them
        this.add(bananaButton);
        this.add(coconutButton);
        this.add(poopButton);
        this.add(healButton);
        this.add(restartButton);

        // bananaButton
        bananaButton.setBounds(50 * getWidth() / 800, 9 * getHeight() / 10, 2 * getWidth() / 8, 6 * getHeight() / 80);
        bananaButton.addActionListener(this);

        // coconutButton
        coconutButton.setBounds(310 * getWidth() / 800, 9 * getHeight() / 10, 2 * getWidth() / 8, 6 * getHeight() / 80);
        coconutButton.addActionListener(this);

        // poopButton
        poopButton.setBounds(570 * getWidth() / 800, 9 * getHeight() / 10, 2 * getWidth() / 8, 6 * getHeight() / 80);
        poopButton.addActionListener(this);

        // healButton
        healButton.setBounds(50 * getWidth() / 800, 9 * getHeight() / 10, 2 * getWidth() / 8, 6 * getHeight() / 80);
        healButton.addActionListener(this);

        // restartButton
        restartButton.setBounds(320 * getWidth() / 800, 46 * getHeight() / 100, 2 * getWidth() / 8, 6 * getHeight() / 80);
        restartButton.addActionListener(this);
        restartButton.setVisible(false);
    }

    // ==METHODS==

    //Main Method
    public static void main(String[] args) {

        ImageIcon img = new ImageIcon(Main.class.getResource("/assets/Icon.png"));
        JFrame splashScreen = new JFrame("MonkeyDefense");

        //Splashscreen
        splashScreen.setBounds(150, 0, DRAWING_WIDTH, DRAWING_HEIGHT);
        SplashScreen splashPanel = new SplashScreen();
        splashPanel.setBackground(Color.PINK);
        Container cSplash = splashScreen.getContentPane();
        cSplash.add(splashPanel);
        splashScreen.setIconImage(img.getImage());
        splashScreen.setVisible(true);
        try {
            Thread.sleep(5000); // shows splash screen for 4 seconds
        } catch (InterruptedException exception) {
        }
        splashScreen.dispatchEvent(new WindowEvent(splashScreen, WindowEvent.WINDOW_CLOSING));


        //Window
        JFrame window = new JFrame("MonkeyDefense");
        window.setBounds(150, 0, DRAWING_WIDTH, DRAWING_HEIGHT);
        window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Panel
        Main panel = new Main();
        panel.setBackground(Color.PINK);
        Container c = window.getContentPane();
        c.add(panel);
        window.setIconImage(img.getImage());
        window.setVisible(true);
        panel.run();

    }

    //Used to play the background music
    private void playMusic() {
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Main.class.getResource("/assets/music.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            gainControl.setValue(-15f); //sound control: 15 less decibels
//            gainControl.setValue(-80f); //use if mute

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Paints everything
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //The playing field
        g.setColor(grassColor);
        g.fillRect(0, 0, getWidth(), 2 * getHeight() / 3);
        g.setColor(Color.BLACK);
        g.setFont(FONT);
        g.drawLine(0, 2 * getHeight() / 3, getWidth(), 2 * getHeight() / 3);
        g.drawString("Score: " + score, getWidth() / 30, 140 * getHeight() / 160);
        g.drawString("High Score: " + highScore, getWidth() / 30, 150 * getHeight() / 160);
        g.drawString("Health: " + monkey.getHealth(), getWidth() / 2 - 30, 12 * getHeight() / 16);
        g.drawString("Money: $" + money, getWidth() / 2 - 30, 13 * getHeight() / 16);
        g.drawString("Current Weapon: " + translateWeaponToName(currentWeapon), getWidth() / 2 - 30, 7 * getHeight() / 8);

        //draws the monkey
        monkey.draw(g, this);

        //draws the weapon
        for (int i = 0; i < weapons.size(); i++) {
            weapons.get(i).draw(g, this);
        }

        //crazyEnemy drawing and HP Bar
        for (int t = 0; t < enemies.size(); t++) {
        	if (t < enemies.size()) {
        		int xEnemy = enemies.get(t).getX();
                int yEnemy = enemies.get(t).getY();
                double enemyMaxHealth = 0;
                enemies.get(t).draw(g, this);

                if (enemies.get(t) instanceof SmartEnemy)
                    enemyMaxHealth = SMART_ENEMY_MAX;

                if (enemies.get(t) instanceof CrazyEnemy)
                    enemyMaxHealth = CRAZY_ENEMY_MAX;


                if (enemies.get(t).getHealth() > 2 * enemyMaxHealth / 3) {
                    g.setColor(Color.green);
                } else if (enemies.get(t).getHealth() > enemyMaxHealth / 3) {
                    g.setColor(Color.orange);
                } else {
                    g.setColor(Color.red);
                }

                g.fillRect(xEnemy - 13, yEnemy + 57, (int) (enemies.get(t).getHealth() / enemyMaxHealth * 80), 10);
                g.setColor(Color.BLACK);
                g.drawRect(xEnemy - 14, yEnemy + 56, (80), 11);
        	}
        }

        //Game over when player health hits 0
        if (monkey.getHealth() <= 0) {
            g.setColor(Color.RED);
            g.fillRect(45 * getWidth() / 100 - 20, 40 * getHeight() / 99 - 27, 160, 40);
            g.setColor(Color.BLACK);
            g.drawString("GAME OVER", 45 * getWidth() / 100, 40 * getHeight() / 99);
            for (int i = 0; i < enemies.size(); i++) {
                enemies.remove(i);
            }
            currentWeapon = BANANA;
            if (score >= highScore) {
            	try {
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(highScoreFile)));
					out.print(score);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            	
            gameOver = true;
        }

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    //Run when button is clicked
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();

        //if bananabutton is clicked
        if (b == bananaButton) {
            currentWeapon = BANANA;
            bananaButton.setBackground(Color.YELLOW);

            //if coconutbutton is clicked
        } else if (b == coconutButton) {
            if (coconutUnlocked) {
                currentWeapon = COCONUT;
                coconutButton.setBackground(Color.YELLOW);

            } else if (money >= 1000) {
                coconutUnlocked = true;
                currentWeapon = COCONUT;
                money -= 1000;
            }
            //if poopbutton is clicked
        } else if (b == poopButton) {
            if (poopUnlocked) {
                currentWeapon = POOP;
                poopButton.setBackground(Color.YELLOW);
            } else if (money >= 2000) {
                poopUnlocked = true;
                currentWeapon = POOP;
                money -= 2000;
            }
        } else if (b == healButton) {
            if (money >= 500) {
                monkey.setHealth(monkey.getHealth() + 100);
                money = money - 500;
            }
        }
        //if restartbutton is clicked (respawn)
        if (b == restartButton) {
            gameOver = false;
            bananaButton.setEnabled(true);
            coconutButton.setEnabled(true);
            poopButton.setEnabled(true);
            healButton.setEnabled(true);
            monkey.setHealth(playerHealth + 40);
            restartButton.setVisible(false);
            score = 0;
            money = 0;
            numberOfEnemies = 1;
            coconutUnlocked = false;
            poopUnlocked = false;
        }
    }

    //Runs constantly
    private void run() {
        while (true) {
            // restartButton
            if (gameOver) {
                restartButton.setVisible(true);
                repaint();
                continue;
            }

            // Sets window limits for player
            monkey.applyWindowLimits(getWidth(), 65 * getHeight() / 99);

            //Controls
            if (leftKeyPressed) {
                monkey.walkX(-1);
            }
            if (rightKeyPressed) {
                monkey.walkX(1);
            }
            if (upKeyPressed) {
                monkey.walkY(-1);
            }
            if (downKeyPressed) {
                monkey.walkY(1);
            }

            if (mousePressed) {
                if (currentWeapon == BANANA) {
                    weapons.add(new Banana(monkey.getX(), monkey.getY(), mouseX - monkey.getX(), mouseY - monkey.getY()));
                    bananaButton.setBackground(Color.YELLOW);
                } else if (currentWeapon == COCONUT) {
                    weapons.add(new Coconut(monkey.getX(), monkey.getY(), mouseX - monkey.getX(), mouseY - monkey.getY()));
                    coconutButton.setBackground(Color.YELLOW);
                } else if (currentWeapon == POOP) {
                    weapons.add(new Poop(monkey.getX(), monkey.getY(), mouseX - monkey.getX(), mouseY - monkey.getY()));
                    poopButton.setBackground(Color.YELLOW);
                }
                mousePressed = false;
            }
            //Weapon movement
            for (int i = 0; i < weapons.size(); i++) {
                weapons.get(i).accelerate();
                weapons.get(i).update();
            }

            //Enemy movement
            for (int t = 0; t < enemies.size(); t++) {
                enemies.get(t).chase(monkey);
                enemies.get(t).applyWindowLimits(getWidth(), 65 * getHeight() / 99);
                enemies.get(t).update();
            }

            //Highscore
            if (score > highScore)
                highScore = score;

            //Spawns enemies
            for (int x = 0; x == 0; x++)
                spawnEnemy();

            monkey.update();
            checkWeapons();


            repaint();

            // Delay
            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Checking for player and enemies collision
            for (int t = 0; t < enemies.size(); t++) {

                if (monkey.isIntersect(enemies.get(t)) || enemies.get(t).isIntersect(monkey)) {
                    monkey.setHealth(monkey.getHealth() - 10);
                    enemies.remove(t);
                }
            }

            // Checking for weapon and enemies collision
            for (int w = 0; w < weapons.size(); w++) {
                for (int t = 0; t < enemies.size(); t++) {
                    if (w >= weapons.size()) {
                        break;
                    }
                    int xWeapon = weapons.get(w).getX();
                    int yWeapon = weapons.get(w).getY();

                    int xEnemy = enemies.get(t).getX();
                    int yEnemy = enemies.get(t).getY();

                    if (enemies.get(t).isPointInImage(xWeapon, yWeapon)) {
                        enemies.get(t).loseHealth(weapons.get(w).getDamage());

                        weapons.remove(w);

                        if (enemies.get(t).getHealth() < 0) {
                            enemies.remove(t);
                            money = money + 50;
                            score = score + 50;
                            spawnEnemy();
                            if (score % 150 == 0) //difficulty increase
                                numberOfEnemies++;
                        }
                    }
                }
            }

            //if game is over
            if (gameOver) {
                bananaButton.setEnabled(false);
                coconutButton.setEnabled(false);
                coconutButton.setBackground(new JButton().getBackground());
                poopButton.setEnabled(false);
                poopButton.setBackground(new JButton().getBackground());
                healButton.setEnabled(false);
            }
        }
    }

    //Spawns Enemies
    private void spawnEnemy() {
        Random rand = new Random();
        int enemyType = rand.nextInt(3); //used to pick the type of enemy
        int spawnSide = rand.nextInt(4); //used to pick where the enemies will spawn

        // Spawn at border
        if (enemies.size() < numberOfEnemies) {
            switch (enemyType) {
                case 0: //CrazyEnemy
                    switch (spawnSide) {
                        case 0:
                            enemies.add(new CrazyEnemy(rand.nextInt(getWidth()), 10 * (rand.nextInt(getHeight()) + 5) / 99,
                                    monkey.getX(), monkey.getY(), CRAZY_ENEMY_MAX, 10, 1));
                            break;
                        case 1:
                            enemies.add(new CrazyEnemy(10 * (rand.nextInt(getWidth()) / 99), (rand.nextInt(getHeight()) + 5),
                                    monkey.getX(), monkey.getY(), CRAZY_ENEMY_MAX, 10, 1));
                            break;
                        case 2:
                            enemies.add(new CrazyEnemy(rand.nextInt(getWidth()), 89 * (rand.nextInt(getHeight()) + 5) / 99,
                                    monkey.getX(), monkey.getY(), CRAZY_ENEMY_MAX, 10, 1));
                            break;
                        case 3:
                            enemies.add(new CrazyEnemy(89 * (rand.nextInt(getWidth()) / 99), (rand.nextInt(getHeight()) + 5),
                                    monkey.getX(), monkey.getY(), CRAZY_ENEMY_MAX, 10, 1));
                            break;
                    }
                    break;
                case 1: //SmartEnemy
                    switch (spawnSide) {
                        case 0:
                            enemies.add(new SmartEnemy(rand.nextInt(getWidth()), 10 * (rand.nextInt(getHeight()) + 5) / 99,
                                    monkey.getX(), monkey.getY(), SMART_ENEMY_MAX, 10, 1));
                            break;
                        case 1:
                            enemies.add(new SmartEnemy(10 * (rand.nextInt(getWidth()) / 99), (rand.nextInt(getHeight()) + 5),
                                    monkey.getX(), monkey.getY(), SMART_ENEMY_MAX, 10, 1));
                            break;
                        case 2:
                            enemies.add(new SmartEnemy(rand.nextInt(getWidth()), 89 * (rand.nextInt(getHeight()) + 5) / 99,
                                    monkey.getX(), monkey.getY(), SMART_ENEMY_MAX, 10, 1));
                            break;
                        case 3:
                            enemies.add(new SmartEnemy(89 * (rand.nextInt(getWidth()) / 99), (rand.nextInt(getHeight()) + 5),
                                    monkey.getX(), monkey.getY(), SMART_ENEMY_MAX, 10, 1));
                            break;
                    }
                case 2: //SmartEnemy (2 times more likely to spawn)
                    switch (spawnSide) {
                        case 0:
                            enemies.add(new SmartEnemy(rand.nextInt(getWidth()), 10 * (rand.nextInt(getHeight()) + 5) / 99,
                                    monkey.getX(), monkey.getY(), SMART_ENEMY_MAX, 10, 1));
                            break;
                        case 1:
                            enemies.add(new SmartEnemy(10 * (rand.nextInt(getWidth()) / 99), (rand.nextInt(getHeight()) + 5),
                                    monkey.getX(), monkey.getY(), SMART_ENEMY_MAX, 10, 1));
                            break;
                        case 2:
                            enemies.add(new SmartEnemy(rand.nextInt(getWidth()), 89 * (rand.nextInt(getHeight()) + 5) / 99,
                                    monkey.getX(), monkey.getY(), SMART_ENEMY_MAX, 10, 1));
                            break;
                        case 3:
                            enemies.add(new SmartEnemy(89 * (rand.nextInt(getWidth()) / 99), (rand.nextInt(getHeight()) + 5),
                                    monkey.getX(), monkey.getY(), SMART_ENEMY_MAX, 10, 1));
                            break;
                    }

            }


        }
    }

    //Creates the weapons in the array
    private void checkWeapons() {
        ArrayList<Weapon> weaponsNew = new ArrayList<Weapon>();
        for (int i = 0; i < weapons.size(); i++) {
            int weaponsX = weapons.get(i).getX() + weapons.get(i).getWidth() / 2;
            int weaponsY = weapons.get(i).getY() + weapons.get(i).getHeight() / 2;
            if (!(weaponsX < 0 || weaponsX > getWidth() || weaponsY < 0 || weaponsY > 2 * getHeight() / 3))
                weaponsNew.add(weapons.get(i));
        }
        weapons = weaponsNew;
    }

    //Gets the name of the weapon
    private String translateWeaponToName(int weapon) {
        if (weapon == BANANA) {
            return "Banana";
        } else if (weapon == COCONUT) {
            return "Coconut";
        } else {
            return "Poop";
        }
    }

    //If player clicks the mouse button
    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX() - 15;
        mouseY = e.getY() - 20;
        mousePressed = true;

    }

    //If a keyboard key is pressed
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            leftKeyPressed = true;
        }
        if (key == KeyEvent.VK_D) {
            rightKeyPressed = true;
        }
        if (key == KeyEvent.VK_W) {
            upKeyPressed = true;
        }
        if (key == KeyEvent.VK_S) {
            downKeyPressed = true;
        }
    }

    //When the keyboard key is released
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            leftKeyPressed = false;
        }
        if (key == KeyEvent.VK_D) {
            rightKeyPressed = false;
        }
        if (key == KeyEvent.VK_W) {
            upKeyPressed = false;
        }
        if (key == KeyEvent.VK_S) {
            downKeyPressed = false;
        }
    }

    //Unused Methods
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}