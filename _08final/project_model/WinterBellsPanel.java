package _08final.project_model;

import _08final.project_model.model.*;
import _08final.project_model.sounds.Sound;

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * External sources (also above the actual functions): http://www.programcreek.com/2013/03/java-sort-map-by-value/
 * See README for more details.
 *
 * Reset the fish!!
 */

public class WinterBellsPanel extends JPanel implements ActionListener
{
    private Image image;

    //Boolean variables
    private boolean beforeGame; // Check if it's before the game
    private boolean startKey; // Check if start key has been pressed
    private boolean qPressed; // Check if Q is pressed
    private boolean randEnding; // Check if random ending is true
    private boolean inGame; // Check if user is in game
    private boolean showInstructions; // Check if player pressed tab
    private boolean aboutLabelPressed; // Check if player pressed about label
    private boolean mutePressed;

    // Ending determinant
    private int endingNum;
    private ArrayList<Integer> endingsFound;

    // Timer
    private Timer timer;
    private Random rand = new Random();

    // Internal score for levels (levels determine how harder to make it)
    private int internalScore;

    // Menu items
    private JLabel startLabel;
    private JLabel aboutLabel;
    private JLabel homeLabel;
    private JLabel ageLabel;
    private JLabel muteLabel;
    private JLabel unmuteLabel;

    // Cat, birds, fish
    private Cat cat;
    private ArrayList<Bird> birds;
    private ArrayList<Fish> fishies;

    // Timer delay
    private final int DELAY = 10;

    // Cat starting location
    private final int CAT_X = 100;
    private final int CAT_Y = 100;

    // Game dimensions
    private final int GAMEHEIGHT = 800;
    private final int GAMEWIDTH = 1000;

    // Background music
    private Clip clpMusicBackground;

    // For formatting health and other numbers
    private DecimalFormat df = new DecimalFormat();

    // Counts
    protected int loveCount; // The heart
    protected int knowledgeCount;
    protected int powerCount;
    protected int fameCount;

    protected int deathCount;
    protected int killCount;

    // The output
    private String firstLine;
    private String secondLine;
    private String thirdLine;

    /**
     * Constructor. Initializes labels, adds key listener, initializes boolean values, etc.
     * Begins the background music loop.
     * @param image
     */
    public WinterBellsPanel(Image image)
    {
        this.image = image;
        df.setMaximumFractionDigits(0);

        // Add labels, listener, cats
        addLabels();
        addKeyListener(new catAdapter());

        clpMusicBackground = Sound.clipForLoopFactory("loopcat.wav"); // Taken from Sound class in other folder.
        clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);

        add(homeLabel);
        add(aboutLabel);
        add(startLabel);
        add(ageLabel);
        add(muteLabel);
        add(unmuteLabel);
//        add(loveLabel);
//        add(powerLabel);
//        add(knowledgeLabel);
//        add(fameLabel);

        // Initialize visibility of labels and location (since we set the layout to null on the frame)
        homeLabel.setVisible(true);
        aboutLabel.setVisible(true);
        startLabel.setVisible(true);
        ageLabel.setVisible(false);
        muteLabel.setVisible(true);
        unmuteLabel.setVisible(false);

        // Set panel details
        setFocusable(true);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(GAMEWIDTH, GAMEHEIGHT));

        // Initialize values
        beforeGame = true;
        startKey = true;
        qPressed = false;
        showInstructions = false;

        // Endings
        endingsFound = new ArrayList<>();

        firstLine = "";
        secondLine = "";
        thirdLine = "";

        // Call paint component
        repaint();
    }

    /**
     * Adds labels for menu items.
     */
    public void addLabels()
    {
        // Start Label
        java.net.URL imgUrl = getClass().getResource("start.png");
        ImageIcon imageIcon = new ImageIcon(imgUrl);
        startLabel = new JLabel(imageIcon);

        startLabel.addMouseListener(new mouseAdapter());

        // About Label
        java.net.URL imgUrl2 = getClass().getResource("about.png");
        ImageIcon imageIcon2 = new ImageIcon(imgUrl2);
        aboutLabel = new JLabel(imageIcon2);

        aboutLabel.addMouseListener(new mouseAdapter());

        // Home Label (to allow user to return home after clicking About)
        java.net.URL imgUrl3 = getClass().getResource("home.png");
        ImageIcon imageIcon3 = new ImageIcon(imgUrl3);
        homeLabel = new JLabel(imageIcon3);

        homeLabel.addMouseListener(new mouseAdapter());

        // Label that shows which stage the person is in
        Font small = new Font("Monaco", Font.BOLD, 30);

        ageLabel = new JLabel();
        ageLabel.setFont(small);
        ageLabel.setText("Baby Stages");

        // Mute label
        java.net.URL imgUrl4 = getClass().getResource("mute.png");
        ImageIcon imageIcon4 = new ImageIcon(imgUrl4);
        muteLabel = new JLabel(imageIcon4);

        muteLabel.addMouseListener(new mouseAdapter());

        // Unmute label
        java.net.URL imgUrl5 = getClass().getResource("unmute.png");
        ImageIcon imageIcon5 = new ImageIcon(imgUrl5);
        unmuteLabel = new JLabel(imageIcon5);

        unmuteLabel.addMouseListener(new mouseAdapter());

//        java.net.URL imgUrl6 = getClass().getResource("lovelabel.png");
//        ImageIcon imageIcon6 = new ImageIcon(imgUrl6);
//        loveLabel = new JLabel(imageIcon6);

        // THINGS TO DO: ADD THE REST OF THE LABELS
    }

    /**
     * Initializes enemies (birds), pick-up items (fish), and determines which cat the user chose.
     * Initializes the cat based on this cat type.
     */
    public void startGame()
    {
        initBirds();
        initFish();

        cat = new Cat(CAT_X, CAT_Y);

//        number = 5; // Initialize the number of birds that start flying towards you
        internalScore = 0;
//        level = 1;

        // Start timer
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Initializes birds by determining how many to add at the start of game..
     */
    public void initBirds()
    {
        birds = new ArrayList<>();

        for (int i = 0; i < 5; i++)
        {
            addBirds();
        }
    }

    /**
     * Adds birds in a random position.
     */
    public void addBirds()
    {
        int position = rand.nextInt(GAMEHEIGHT - 1) + 1;
        birds.add(new Bird(GAMEWIDTH - 1, GAMEHEIGHT - position));
    }

    /**
     * Initializes fish by determining how many to add at the start of game.
     */
    public void initFish()
    {
        fishies = new ArrayList<>();

        for (int i = 0; i < 10; i++)
        {
            addFish();
        }
    }

    /**
     * Adds fish at a random position.
     */
    public void addFish()
    {
        int position = rand.nextInt(GAMEHEIGHT - 1) + 1;
        Fish fish = new Fish(GAMEWIDTH - 1, GAMEHEIGHT - position);
        fishies.add(fish);
    }

    /**
     * Determines what to draw. Paints based on timer.
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Draws the background.

        if (inGame)
        {
            doDraw(g);
        }

        // Starting menu
        else if (!inGame && beforeGame)
        {
            displayTextOnScreen(g);
            startLabel.setVisible(true);
            aboutLabel.setVisible(true);
            homeLabel.setVisible(true);
            ageLabel.setVisible(false);
        }

        // If no longer in game and it's not beforeGame, do the end.
        else
        {
            doEnd(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Determines how many cats, missiles, birds, fish to draw.
     * @param g
     */
    private void doDraw(Graphics g)
    {
        // If cat is not visible, it is not drawn again.
        if (cat.isVisible())
        {
            g.drawImage(cat.getImage(), cat.getX(), cat.getY(), this);
        }

        // Get the cat's missiles
        ArrayList<Missile> missiles = cat.getMissiles();

        // Draws missiles
        for (Missile missile : missiles)
        {
            if (missile.isVisible())
            {
                g.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
            }
        }

        // Draws birds (enemies a.k.a. skull image)
        for (Bird bird : birds)
        {
            if (bird.isVisible())
            {
                g.drawImage(bird.getImage(), bird.getX(), bird.getY(), this);
            }
        }

        // Draws fish (things to pick up)
        for (Fish fish : fishies)
        {
            if (fish.isVisible())
            {
                g.drawImage(fish.getImage(), fish.getX(), fish.getY(), this);
            }
        }

        // Show health bar at the upper left corner
        g.setColor(Color.BLACK);

        java.net.URL imgUrl = getClass().getResource("healthbar.png");
        ImageIcon imageIcon = new ImageIcon(imgUrl);
        Image healthbar = imageIcon.getImage();
        g.drawImage(healthbar, 5, 15, this);

        // Health text
        g.drawString("HEALTH: " + df.format(cat.getHealth()), 5, 15);

        // Change the health bar color based on how much health is left
        if (cat.getHealth() > 50)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.GREEN);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
            g2d.fillRect(6, 17, ((int) cat.getHealth() * 2) - 1, healthbar.getHeight(null) - 4);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal
        }

        else if (cat.getHealth() <= 50 && cat.getHealth() > 20)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.YELLOW);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
            g2d.fillRect(6, 17, ((int) cat.getHealth() * 3), healthbar.getHeight(null) - 4);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal
        }

        else if (cat.getHealth() <= 20)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
            g2d.fillRect(6, 17, ((int) cat.getHealth() * 3), healthbar.getHeight(null) - 4);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal
        }

        else if (cat.getHealth() < 5)
        {
            Sound.playSound("Timer.wav");
        }

        // Draw poison bar
        g.drawImage(healthbar, 5, 45, this);

        // Poison text
        g.setColor(Color.BLACK);
        String poison = "POISON: " + Integer.toString(deathCount);
        g.drawString(poison, 5, 45);

        // Change the health bar color based on how much health is left
        if (deathCount < 20)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
            g2d.fillRect(6, 47, (deathCount * 3), healthbar.getHeight(null) - 4);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal
        }

        else if (deathCount >= 20)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
            g2d.fillRect(6, 47, (deathCount * 3), healthbar.getHeight(null) - 4);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal
        }

        // Draw power bar
        g.drawImage(healthbar, 750, 15, this);

        // Power text
        g.setColor(Color.BLACK);
        String power = "POWER: " + Integer.toString(powerCount);
        g.drawString(power, 750, 15);

        // Increase power bar
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
        g2d.fillRect(752, 17, (powerCount * 4), healthbar.getHeight(null) - 4);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal

        // Draw knowledge bar
        g.drawImage(healthbar, 750, 45, this);

        // Knowledge text
        g.setColor(Color.BLACK);
        String know = "KNOWLEDGE: " + Integer.toString(knowledgeCount);
        g.drawString(know, 750, 45);

        // Increase knowledge bar
        Graphics2D g2d2 = (Graphics2D) g;
        g2d.setColor(Color.GREEN);

        g2d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
        g2d2.fillRect(752, 47, (knowledgeCount * 4), healthbar.getHeight(null) - 4);
        g2d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal

        // Draw love bar
        g.drawImage(healthbar, 750, 75, this);

        // Love text
        g.setColor(Color.BLACK);
        String love = "LOVE: " + Integer.toString(loveCount);
        g.drawString(love, 750, 75);

        // Increase power bar
        Graphics2D g2d3 = (Graphics2D) g;
        g2d3.setColor(Color.PINK);

        g2d3.fillRect(752, 77, (loveCount * 4), healthbar.getHeight(null) - 4);

        // Draw star bar
        g.drawImage(healthbar, 750, 105, this);

        // Power text
        g.setColor(Color.BLACK);
        String star = "STAR: " + Integer.toString(fameCount);
        g.drawString(star, 750, 105);

        // Increase power bar
        Graphics2D g2d4 = (Graphics2D) g;
        g2d.setColor(Color.YELLOW);

        g2d4.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
        g2d4.fillRect(752, 107, (fameCount * 4), healthbar.getHeight(null) - 4);
        g2d4.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal
    }

    /**
     * Ends the game
     * @param g
     */
    private void doEnd(Graphics g)
    {
        ageLabel.setText("Baby Stages");
        ageLabel.setVisible(false);
        //==============================
        //Creates a transparent rectangle
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 5 * 0.1f));
        g2d.fillRect(150, 250, 700, 400);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal
        //==============================

        // The different fonts to use
        Font small = new Font("Monaco", Font.BOLD, 30);
        FontMetrics fm = getFontMetrics(small);

        Font fntScore = new Font("Monaco", Font.PLAIN, 15);
        FontMetrics fm2 = getFontMetrics(fntScore);

        Font finalComment = new Font("Monaco", Font.ITALIC + Font.BOLD, 15);
        FontMetrics fm3 = getFontMetrics(finalComment);

        // "You Have Died" declaration
        g.setColor(Color.BLACK);
        g.setFont(small);

        String gameover = "You Have Died";
        g.drawString(gameover, (GAMEWIDTH - fm.stringWidth(gameover))/2, (GAMEHEIGHT/2) - 180);

        // Get the ending - firstLine, secondLine, and thirdLine are determined
        getLines();

        // Write the three ending lines
        g.setColor(Color.BLACK);
        g.setFont(fntScore);

        g.drawString(firstLine, (GAMEWIDTH - fm2.stringWidth(firstLine))/2, (GAMEHEIGHT/2) - 100);
        g.drawString(secondLine, (GAMEWIDTH - fm2.stringWidth(secondLine))/2, (GAMEHEIGHT/2) - 60);
        g.drawString(thirdLine, (GAMEWIDTH - fm2.stringWidth(thirdLine))/2, (GAMEHEIGHT/2) - 20);

        // Endings found
        String endingCount = "You have found " + endingsFound.size() + " out of 30 possible endings.";
        g.drawString(endingCount, (GAMEWIDTH - fm2.stringWidth(endingCount))/2, (GAMEHEIGHT/2) + 20);

        // Write the final comment
        String finalString1 = "There is an unavoidable timer on life.";
        String finalString2 = "But we can choose how we spend it. Choose wisely.";
        g.setFont(finalComment);

        g.drawString(finalString1, (GAMEWIDTH - fm3.stringWidth(finalString1))/2, (GAMEHEIGHT/2) + 100);
        g.drawString(finalString2, (GAMEWIDTH - fm3.stringWidth(finalString2))/2, (GAMEHEIGHT/2) + 120);

        // Give player option to play again
        String playAgain = "Reincarnate? Press R.";
        g.setFont(small);
        g.drawString(playAgain, (GAMEWIDTH - fm.stringWidth(playAgain))/2, (GAMEHEIGHT/2) + 200);

    }

    /**
     * Based on which ending, gets the final lines to output at the end.
     */
    private void getLines()
    {
        int ending;

        // Check first if the player has randomly died.
        if (randEnding)
        {
            ending = 31;
        }

        // Else, get the ending number.
        else
        {
            ending = getEnding();
        }

        // Keeps a counter of endings found
        if (!endingsFound.contains(ending))
        {
            endingsFound.add(ending);
        }

        // Goes through various endings and sets the lines based on the ending number
        if (ending == 1)
        {
            firstLine = "In your 100-second life, you were a social justice warrior.";
            secondLine = "You may not have been well known, but you impacted hundreds of lives.";
            thirdLine = "You died of old age, and those hundreds of people came to pay their respects.";
        }

        else if (ending == 0)
        {
            firstLine = "In your 100-second life, you did nothing.";
            secondLine = "You had no hobbies, no friends, no job, no ups, no downs.";
            thirdLine = "You died of boredom.";
        }

        else if (ending == 2)
        {
            firstLine = "In your 100-second life, you were a a generous philanthropist.";
            secondLine = "Your anonymous donations to charities empowered others to give more.";
            thirdLine = "You died, leaving your enormous estate to orphaned kittens around the world.";
        }

        else if (ending == 3)
        {
            firstLine = "In your 100-second life, you were That One Famous Activist From TV.";
            secondLine = "Those who agreed with you looked to you as their leader.";
            thirdLine = "You died leaving behind a legacy of people committed to your cause.";
        }

        else if (ending == 4)
        {
            firstLine = "In your 100-second life, you were a medical researcher.";
            secondLine = "You loved the joy of contributing to science, one dead mouse as time.";
            thirdLine = "You died when the mice became smart, rebelled, and took over the lab.";
        }

        else if (ending == 5)
        {
            firstLine = "In your 100-second life, you became PRESIDENT of WORLD.";
            secondLine = "You were always so busy taking care of Important Business.";
            thirdLine = "You died overseas from an exploding sheep.";
        }

        else if (ending == 6)
        {
            firstLine = "In your 100-second life, you were the turtle version of Tony Stark.";
            secondLine = "You created suits for the Ninja Turtles and made an AI became a turtle god.";
            thirdLine = "You died, but then you just came back to life because you are Tony Stark.";
        }

        else if (ending == 7)
        {
            firstLine = "In your 100-second life, you were a doctor.";
            secondLine = "You spent your time saving lives by performing complex surgeries.";
            thirdLine = "You died realizing that your power over life was only temporary.";
        }

        else if (ending == 8)
        {
            firstLine = "In your 100-second life, you were a CEO of a major tech company.";
            secondLine = "You developed technological innovations for everyone young and old.";
            thirdLine = "You died when your creativity and brilliance was needed in the afterlife.";
        }

        else if (ending == 9)
        {
            firstLine = "In your 100-second life, you were a powerful judge.";
            secondLine = "You gave verdicts on who took the cookie from the cookie jar.";
            thirdLine = "You died after overseeing 1000 cookie cases.";
        }

        else if (ending == 10)
        {
            firstLine = "In your 100-second life, you were a famous chef.";
            secondLine = "People loved watching your TV shows about giving food to the needy.";
            thirdLine = "You died leaving behind a culinary, philanthropy legacy.";
        }

        else if (ending == 11)
        {
            firstLine = "In your 100-second life, you were a famous writer.";
            secondLine = "Your books were met with millions of fans and several movies and parodies.";
            thirdLine = "You died surrounded by your imaginary friends.";
        }

        else if (ending == 12)
        {
            firstLine = "In your 100-second life, you took over the world.";
            secondLine = "Well, on TV. You were in every film that featured someone taking over the world.";
            thirdLine = "You died while taking over the world in your sleep, too.";
        }

        else if (ending == 13)
        {
            firstLine = "In your 100-second life, you learned as many languages as possible.";
            secondLine = "You traveled, made friends in multiple countries, and helped everyone you saw.";
            thirdLine = "When you died, your funeral was conducted in 19 languages.";
        }

        else if (ending == 14)
        {
            firstLine = "In your 100-second life, you fought many battles for your cause.";
            secondLine = "Except you usually acted before thinking things through.";
            thirdLine = "You died while charging into a battlefield on accident.";
        }

        else if (ending == 15)
        {
            firstLine = "In your 100-second life, you were a hopeless romantic.";
            secondLine = "You dated many people, and your were famous around town for declarations of love.";
            thirdLine = "You died experiencing the strongest of loves, and the worst of heartbreaks.";
        }

        else if (ending == 16)
        {
            firstLine = "In your 100-second life, you were constantly creating computer games.";
            secondLine = "You loved making people laugh and cry with games you spent months on.";
            thirdLine = "You died when your horror computer game came to life. You coded it too well.";
        }

        else if (ending == 17)
        {
            firstLine = "In your 100-second life, you were an ULTRA COOL 'HACKER' SPY.";
            secondLine = "Everyone thought you were a drunkard. But you were actually undercover.";
            thirdLine = "You died from accidentally playing your part too well.";
        }

        else if (ending == 18)
        {
            firstLine = "In your 100-second life, you were a professional hard-core gamer.";
            secondLine = "You stayed inside and practiced games for up to 12 hours a day.";
            thirdLine = "You died from forgetting to eat while playing League of Legends.";
        }

        else if (ending == 19)
        {
            firstLine = "In your 100-second life, you were a politician.";
            secondLine = "Charismatic and kind of stupid at the same time, you somehow got elected.";
            thirdLine = "You died because your head exploded from all the air in it.";
        }

        else if (ending == 20)
        {
            firstLine = "In your 100-second life, you were powerful book worm.";
            secondLine = "All you did was read, read, and ... read. You knew a lot, but saw little sun.";
            thirdLine = "You died from all sorts of vitamin deficiencies.";
        }

        else if (ending == 21)
        {
            firstLine = "In your 100-second life, you were a competitive food eater.";
            secondLine = "Your world record was 200 tuna cans in 2 minutes.";
            thirdLine = "You died from mercury poisoning.";
        }

        else if (ending == 22)
        {
            firstLine = "In your 100-second life, you were a very broke detective.";
            secondLine = "You took on cases for free out of the goodness of your heart.";
            thirdLine = "You died without realizing just how much you helped people, at your own cost.";
        }

        else if (ending == 23)
        {
            firstLine = "In your 100-second life, you were a famous mad scientist. MWAHAHA!";
            secondLine = "You ended up re-creating Frankenstein, which you spent kitty jail time for doing.";
            thirdLine = "You died while scheming up an escape plan in your cell.";
        }

        else if (ending == 24)
        {
            firstLine = "In your 100-second life, you were a corrupt judge.";
            secondLine = "You often ruled in favor of whoever looked like the cooler cat.";
            thirdLine = "People found out about your corruption after you died, and your name was ruined.";
        }

        // Ending 25?

        else if (ending == 26)
        {
            firstLine = "In your 100-second life, you did not have a 100-second life.";
            secondLine = "Because you pressed 'Q' to quit.";
            thirdLine = "If you want to know all the endings just ask the developer, yeesh.";
        }

        else if (ending == 27)
        {
            firstLine = "In your 100-second life, you were constantly living a series of unfortunate events.";
            secondLine = "Basically nothing worked out for you.";
            thirdLine = "You just ... died.";
        }

        else if (ending == 28)
        {
            firstLine = "In your 100-second life, you lived life a little imbalanced.";
            secondLine = "You consistently ignored one part of your life.";
            thirdLine = "You died never having experienced everything you could have.";
        }

        else if (ending == 29)
        {
            firstLine = "In your 100-second life, you were a famous rapper addicted to catnip.";
            secondLine = "You lived your life caring about catnip, and rapping about catnip only.";
            thirdLine = "You died from catnip overdose, but hey, you died happy.";
        }

        // Ending 30?

        else if (ending == 31)
        {
            firstLine = "Unfortunately, you have died prematurely.";
            secondLine = "Not everyone gets to live until the end.";
            thirdLine = "Luckily, you can try again and hope you are luckier!";
        }

        else if (ending == 32)
        {
            firstLine = "In 100-second life, you lived life seeking danger.";
            secondLine = "You constantly sought adventure and tried thrilling new things.";
            thirdLine = "You died prematurely while battling a crazy bear in the wild WHILE sky diving.";
        }

    }

    /**
     * Determines the ending.
     * @return
     */
    private int getEnding()
    {
        // Put the traits in a hashmap so that we can sort it
        HashMap<String, Integer> endingTraits = new HashMap<>();
        endingTraits.put("Heart", loveCount);
        endingTraits.put("Book", knowledgeCount);
        endingTraits.put("Fist", powerCount);
        endingTraits.put("Star", fameCount);

        // Sort the map by value and put it into a TreeMap
        TreeMap<String, Integer> sortedTraits = sortMapByValue(endingTraits);
        String first = (String) sortedTraits.keySet().toArray()[0];
        String second = (String) sortedTraits.keySet().toArray()[1];

        // If the player has picked up nothing AND this is not an ending caused by player pressing Q
        if (((loveCount + knowledgeCount + powerCount + fameCount) == 0) && !qPressed && deathCount < 25)
        {
            if (deathCount < 5)
            {
                endingNum = 0;
            }

            else
            {
                endingNum = 27;
            }
        }

        // If the player has pressed Q
        else if (qPressed)
        {
            endingNum = 26;
        }

        else if ((loveCount == 0 || knowledgeCount == 0 || powerCount == 0
                || fameCount == 0) && !qPressed && (deathCount < 25)
                && (loveCount + knowledgeCount + powerCount + fameCount) < 8)
        {
            if (deathCount < 10)
            {
                endingNum = 28;
            }

            else
            {
                endingNum = 29;
            }
        }

        else if (deathCount >= 25) // If the player hits too many skulls (Birds)
        {
            endingNum = 32;
        }

        // If it's a normal death
        else
        {
            // Get various ending numbers based on what is the most picked up, second most picked up
            if (deathCount < 7)
            {
                if (first.equals("Heart"))
                {
                    if (second.equals("Book"))
                    {
                        endingNum = 1; // The social justice warrior
                    } else if (second.equals("Fist"))
                    {
                        endingNum = 2; // The generous philanthropist
                    } else if (second.equals("Star"))
                    {
                        endingNum = 3; // The famous activist you read about in TV
                    }

                } else if (first.equals("Book"))
                {
                    if (second.equals("Heart"))
                    {
                        endingNum = 4; // The medical researcher
                    } else if (second.equals("Fist"))
                    {
                        endingNum = 5; // The lawyer
                    } else if (second.equals("Star"))
                    {
                        endingNum = 6; // The Tony Stark
                    }
                } else if (first.equals("Fist"))
                {
                    if (second.equals("Heart"))
                    {
                        endingNum = 7; // The doctor
                    } else if (second.equals("Book"))
                    {
                        endingNum = 8; // The CEO
                    } else if (second.equals("Star"))
                    {
                        endingNum = 9; // The mega ultra famouse movie star
                    }
                } else if (first.equals("Star"))
                {
                    if (second.equals("Heart"))
                    {
                        endingNum = 10; // The New York Times Opinions writer who got famous
                    } else if (second.equals("Book"))
                    {
                        endingNum = 11; // The ultra famous writer
                    } else if (second.equals("Fist"))
                    {
                        endingNum = 12; // The TV personality judge
                    }
                }
            } else if (deathCount >= 7 && deathCount < 25)
            {
                if (first.equals("Heart"))
                {
                    if (second.equals("Book"))
                    {
                        endingNum = 13; // The archeologist?
                    } else if (second.equals("Fist"))
                    {
                        endingNum = 14; // The secret activist who is trying to overthrow the world
                    } else if (second.equals("Star"))
                    {
                        endingNum = 15;
                    }

                } else if (first.equals("Book"))
                {
                    if (second.equals("Heart"))
                    {
                        endingNum = 16; // Game dev
                    } else if (second.equals("Fist"))
                    {
                        endingNum = 17; // The lawyer
                    } else if (second.equals("Star"))
                    {
                        endingNum = 18; // The Tony Stark
                    }
                } else if (first.equals("Fist"))
                {
                    if (second.equals("Heart"))
                    {
                        endingNum = 19; // The doctor
                    } else if (second.equals("Book"))
                    {
                        endingNum = 20; // The CEO
                    } else if (second.equals("Star"))
                    {
                        endingNum = 21; // The mega ultra famous movie star
                    }
                } else if (first.equals("Star"))
                {
                    if (second.equals("Heart"))
                    {
                        endingNum = 22; // The New York Times Opinions writer who got famous
                    } else if (second.equals("Book"))
                    {
                        endingNum = 23; // The ultra famous writer
                    } else if (second.equals("Fist"))
                    {
                        endingNum = 24; // The TV personality judge
                    }
                }
            }
        }
        return endingNum;
    }

    /**
     * Source: http://www.programcreek.com/2013/03/java-sort-map-by-value/
     *
     * Takes in a hashmap and sorts it by value. Returns a treemap of the result.
     * @param anyMap
     * @return
     */
    private TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> anyMap)
    {
        Comparator<String> comparator = new ValueComparator(anyMap);
        TreeMap<String, Integer> result = new TreeMap<>(comparator);
        result.putAll(anyMap);
        return result;
    }

    /**
     * Source: http://www.programcreek.com/2013/03/java-sort-map-by-value/
     *
     * The class that does the actual value comparison.
     */
    class ValueComparator implements Comparator<String>
    {
        HashMap<String, Integer> map = new HashMap<>();
        public ValueComparator(HashMap<String, Integer> map)
        {
            this.map.putAll(map);
        }

        @Override
        public int compare(String string1, String string2)
        {
            if (map.get(string1) > map.get(string2))
            {
                return -1;
            }

            else
            {
                return 1;
            }
        }
    }

    /**
     * ActionListener for the class.
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        // Check if still in game
        inGame();

        // Update all the components and repaint
        updateCat();
        updateMissiles();
        updateBirds();
        updateFish();
        repaint(); // TTD: This may not be necessary. Try commenting out.

        // Check for collisions, check for random ending
        checkCollisions();
        checkRandEnd();

        repaint();
    }

    /**
     * Randomly (1 out of 100000 chance) determines if the player will have a premature death.
     * Yes, this is one of the 30 endings.
     */
    private void checkRandEnd()
    {
        int random = rand.nextInt(100000);
        if (random == 1)
        {
            inGame = false;
            randEnding = true;
        }
    }

    /**
     * Checks if the player is still in game. If not, stop the timer.
     */
    private void inGame()
    {
        if (!inGame)
        {
            timer.stop();
        }
    }

    /**
     * Updates missiles. If not visible, removes them. If visible, moves them to the right.
     */
    private void updateMissiles()
    {
        ArrayList<Missile> missiles = cat.getMissiles();
        for (int i = 0; i < missiles.size(); i++)
        {
            Missile m = missiles.get(i);

            if (m.isVisible())
            {
                m.move();
            }

            else
            {
                missiles.remove(i);
            }
        }
    }

    /**
     * Updates cat. If visible, decrement the health by 0.02 and allow the cat to move around.
     */
    private void updateCat()
    {
        if (cat.isVisible())
        {
            cat.changeHealth(0.02); // Constantly decrements health ("life time") of cat.
            cat.moveCat();
        }

        if (cat.getHealth() <= 0)
        {
            cat.setVisible(false);
            inGame = false;
        }

        if (deathCount > 25) // Too many deaths
        {
            cat.setVisible(false);
            inGame = false;
        }

        if (cat.getHealth() > 80 && cat.getHealth() <= 90)
        {
            ageLabel.setText("Adolescence");
            cat.setNewSpeed(3.5);
        }

        else if (cat.getHealth() <= 80 && cat.getHealth() > 70)
        {
            ageLabel.setText("Young Adulthood");
            cat.setNewSpeed(3);
        }

        else if (cat.getHealth() <= 70 && cat.getHealth() > 50)
        {
            ageLabel.setText("Early Adult Life");
            cat.setNewSpeed(2.5);
        }

        else if (cat.getHealth() <= 50 && cat.getHealth() > 30)
        {
            ageLabel.setText("Late Adult Life");
            cat.setNewSpeed(2);
        }

        else if (cat.getHealth() <= 30)
        {
            ageLabel.setText("Old Age");
            cat.setNewSpeed(1.5);
        }
    }

    /**
     * Updates birds. If visible, moves. If not, removed. Set to not visible when it reaches the left side of the screen.
     */
    private void updateBirds()
    {
        for (int i = 0; i < birds.size(); i++)
        {
            Bird bird = birds.get(i);
            if (bird.isVisible())
            {
                bird.move();
            }

            else
            {
                birds.remove(i);
            }
        }

        // Adds a random number of birds based on the number variable
        if (cat.getHealth() > 90 && birds.size() < 3)
        {
            int randomNumber = rand.nextInt(3) + 1;
            for (int i = 0; i < randomNumber; i++)
            {
                addBirds();
            }
        }

        else if (cat.getHealth() <= 90 && cat.getHealth() > 70 && birds.size() < 5)
        {
            int randomNumber = rand.nextInt(5) + 1;
            for (int i = 0; i < randomNumber; i++)
            {
                addBirds();
            }
        }

        else if (cat.getHealth() <= 70 && cat.getHealth() > 40 && birds.size() < 7)
        {
            int randomNumber = rand.nextInt(7) + 1;
            for (int i = 0; i < randomNumber; i++)
            {
                addBirds();
            }
        }

        else if (cat.getHealth() <= 40 && birds.size() < 10)
        {
            int randomNumber = rand.nextInt(10) + 1;
            for (int i = 0; i < randomNumber; i++)
            {
                addBirds();
            }
        }
    }

    /**
     * Updates the fish. If visible, moves. If not, removed. Set to not visible when it reaches the left side of the screen.
     */
    private void updateFish()
    {
        for (int i = 0; i < fishies.size(); i++)
        {
            Fish f = fishies.get(i);
            if (f.isVisible())
            {
                f.move();
            }

            else
            {
                fishies.remove(i);
            }
        }

        if (cat.getHealth() > 90 && fishies.size() < 10)
        {
            int randomNumber = rand.nextInt(15) + 1;
            for (int i = 0; i < randomNumber; i++)
            {
                addFish();
            }
        }

        else if (cat.getHealth() <= 90 && cat.getHealth() > 70 && fishies.size() < 7)
        {
            int randomNumber = rand.nextInt(10) + 1;
            for (int i = 0; i < randomNumber; i++)
            {
                addFish();
            }
        }

        else if (cat.getHealth() <= 70 && cat.getHealth() > 20 && fishies.size() < 5)
        {
            int randomNumber = rand.nextInt(7) + 1;
            for (int i = 0; i < randomNumber; i++)
            {
                addFish();
            }
        }

        else if (cat.getHealth() <= 20 && fishies.size() < 3)
        {
            int randomNumber = rand.nextInt(5) + 1;
            for (int i = 0; i < randomNumber; i++)
            {
                addFish();
            }
        }

    }

    /**
     * Checks for collisions.
     */
    public void checkCollisions()
    {
        Rectangle rCat = cat.getBounds();

        // Bird-Cat
        for (Bird bird : birds)
        {
            Rectangle rBird = bird.getBounds();

            if (rCat.intersects(rBird))
            {
                deathCount++; // Increases death count (Things to Do: Can add this as a factor for ending)
                Sound.playSound("kapow.wav");
                cat.changeHealth(2); // Loses -2 life when player hits a bird (skull)

                bird.setVisible(false);
                inGame = true;
            }
        }


        // Fish-Cat
        for (Fish fish : fishies)
        {
            Rectangle rFish = fish.getBounds();

            if (rCat.intersects(rFish))
            {
                Sound.playSound("Ting.wav");

                // Increments the count of what has been picked up
                if (fish.getFishidentity().equals("Heart")) // Love
                {
                    loveCount++;
                }

                else if (fish.getFishidentity().equals("Book")) // Knowledge
                {
                    knowledgeCount++;
                }

                else if (fish.getFishidentity().equals("Fist")) // Power
                {
                    powerCount++;
                }

                else if (fish.getFishidentity().equals("Star")) // Fame
                {
                    fameCount++;
                }

                // Every time a cat picks up something, score goes up and increases the level, which determines number of birds & fish
                internalScore++;
                fish.setVisible(false);

                // To level up (remove if I'm not doing anything with this)
                if (internalScore == 5)
                {
                    internalScore = 0;
                }
            }
        }

        // Missile-Bird
        ArrayList<Missile> missile = cat.getMissiles();
        for (Missile m : missile)
        {
            Rectangle rMissile = m.getBounds();

            for (Bird bird : birds)
            {
                Rectangle rBird = bird.getBounds();

                if (rMissile.intersects(rBird))
                {
                    Sound.playSound("LoseLife.wav");
                    killCount++; // TTD: Incorporate kill count into endings.
                    m.setVisible(false);
                    bird.setVisible(false);
                }
            }
        }
    }

    /**
     * Displays the starting menu.
     * @param g
     */
    private void displayTextOnScreen(Graphics g)
    {
        String strDisplay;

        //==============================
        //Creates a transparent rectangle
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 5 * 0.1f));
        g2d.fillRect(150, 230, 700, 400);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 10 * 0.1f)); // Reset to normal
        //==============================

        // Fonts to use for the starting screen
        Font font = new Font("Orator STD", Font.BOLD, 16);
        FontMetrics fmt = getFontMetrics(font);

        Font fontTitle = new Font("Orator STD", Font.BOLD, 30);
        FontMetrics title = getFontMetrics(fontTitle);

        Font start = new Font("Orator STD", Font.BOLD, 20);
        FontMetrics starttitle = getFontMetrics(start);

        if (aboutLabelPressed)
        {
            Font aboutFont = new Font("Monaco", Font.BOLD, 14);
            FontMetrics fmt2 = getFontMetrics(aboutFont);

            g.setColor(Color.BLACK);
            g.setFont(fontTitle);

            strDisplay = "one hundred seconds";
            g.drawString(strDisplay,
                    (GAMEWIDTH - title.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4);

            g.setFont(start);

            strDisplay = "About";
            g.drawString(strDisplay,
                    (GAMEWIDTH - starttitle.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                            + starttitle.getHeight() + 50);

            g.setFont(aboutFont);

            strDisplay = "Created by: Susie Yi for UChicago MPCS Java Programming, Spring 2016.";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 90);

            g.setFont(font);
            strDisplay = "Dedicated to: Suh Soon-Ja (서순자), may you rest in peace.";
            g.drawString(strDisplay, (GAMEWIDTH - fmt.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt.getHeight() + 120);

            g.setFont(aboutFont);

            strDisplay = "This game was created as a lighthearted way to represent life (and death).";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 150);

            strDisplay = "We live day to day life, forgetting that today is only a moment. We ignore";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 170);

            strDisplay = "the 'poisons' in our life. We pursue different personal virtues and goals mindlessly.";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 190);

            strDisplay = "Sometimes, we forget that time doesn't stop for us. With the limited time we have,";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 210);

            strDisplay = "whether we are a flying turtle or a normal human being, we can achieve";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 230);

            strDisplay = "so many different things, beyond what this game can even imagine. The";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 250);

            strDisplay = "point of this game is not to paint a picture of hopelessness. Instead, it";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 270);

            strDisplay = "is meant to empower us to choose to remember to LIVE, so that when we do";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 290);

            strDisplay = "reach the end of this life, we can look forward, without regrets,";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 310);

            strDisplay = "to the next adventure. I hope you enjoy playing!";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 330);

            strDisplay = "Turtle: http://kineticpsychiatrist.deviantart.com/";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 360);

            strDisplay = "Skull: http://www.clipartbest.com/skull-and-bones-cartoon";
            g.drawString(strDisplay, (GAMEWIDTH - fmt2.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                    + fmt2.getHeight() + 380);

        }

        else
        {
            if (showInstructions)
            {
                g.setColor(Color.BLACK);
                g.setFont(fontTitle);

                strDisplay = "one hundred seconds";
                g.drawString(strDisplay,
                        (GAMEWIDTH - title.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4);

                g.setFont(start);

                strDisplay = "Instructions";
                g.drawString(strDisplay,
                        (GAMEWIDTH - starttitle.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                                + starttitle.getHeight() + 80);

                g.setFont(font);

                strDisplay = "Q to quit.";
                g.drawString(strDisplay, (GAMEWIDTH - fmt.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                        + fmt.getHeight() + 140);

                strDisplay = "Spacebar to shoot.";
                g.drawString(strDisplay, (GAMEWIDTH - fmt.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                        + fmt.getHeight() + 180);

                strDisplay = "Arrow keys to move around.";
                g.drawString(strDisplay, (GAMEWIDTH - fmt.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                        + fmt.getHeight() + 220);

                g.setFont(start);

                strDisplay = "SKULL = POISON";
                g.drawString(strDisplay, (GAMEWIDTH - starttitle.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                        + starttitle.getHeight() + 320);
            }

            else if (!showInstructions)
            {
                g.setColor(Color.BLACK);
                g.setFont(fontTitle);

                strDisplay = "one hundred seconds";
                g.drawString(strDisplay,
                        (GAMEWIDTH - title.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4);

                g.setFont(font);

                strDisplay = "You have 100 seconds to live.";
                g.drawString(strDisplay,
                        (GAMEWIDTH - fmt.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                                + fmt.getHeight() + 60);

                strDisplay = "How will you spend the small amount of time you have?";
                g.drawString(strDisplay,
                        (GAMEWIDTH - fmt.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                                + fmt.getHeight() + 100);

                strDisplay = "Will you pursue... Power? Love? Knowledge? Fame?";
                g.drawString(strDisplay,
                        (GAMEWIDTH - fmt.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                                + fmt.getHeight() + 140);

                strDisplay = "More importantly, can you discover all the endings?";
                g.drawString(strDisplay,
                        (GAMEWIDTH - fmt.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                                + fmt.getHeight() + 180);

                g.setFont(start);

                strDisplay = "Press 'T' to show instructions!";
                g.drawString(strDisplay,
                        (GAMEWIDTH - starttitle.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                                + starttitle.getHeight() + 260);

                strDisplay = "Press the start button (or S) to begin!";
                g.drawString(strDisplay,
                        (GAMEWIDTH - starttitle.stringWidth(strDisplay)) / 2, GAMEHEIGHT / 4
                                + starttitle.getHeight() + 300);
            }
        }
    }

    /**
     * Stops looping sounds.
     * */
    private static void stopLoopingSounds(Clip clpClips)
    {
        clpClips.stop();
    }

    /**
     * KeyAdapter listens for player's keyboard input
     */
    private class catAdapter extends KeyAdapter
    {
        @Override
        public void keyReleased(KeyEvent e)
        {
            if (e.getKeyCode() == KeyEvent.VK_T && beforeGame && startKey)
            {
                showInstructions = false;
            }

            else
            {
                cat.keyReleased(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == 81 && !beforeGame) // Q = Quit
            {
                Sound.playSound("Click.wav");
                inGame = false;
                beforeGame = false;
                qPressed = true;
            }

            // R - restarts game
            if (e.getKeyCode() == KeyEvent.VK_R && !startKey && !beforeGame) // Restarts the game (R)
            {
                Sound.playSound("Click.wav");
                beforeGame = true;
                inGame = false;
                startKey = true; // So that the player can choose a cat again
                qPressed = false;
                randEnding = false;

                deathCount = 0;

                repaint();
            }

            // Tab - shows instructions
            if (e.getKeyCode() == KeyEvent.VK_T && beforeGame && startKey)
            {
                Sound.playSound("Click.wav");
                showInstructions = true;
            }

            // Starts the game (same as pressing start button)
            if ((e.getKeyCode() == KeyEvent.VK_S) && startKey && beforeGame)
            {
                Sound.playSound("Click.wav");
                startGame();
                beforeGame = false;
                inGame = true;
                startKey = false; // So that the player can't accidentally restart.
                ageLabel.setVisible(true);
                startLabel.setVisible(false);
                aboutLabel.setVisible(false);
                homeLabel.setVisible(false);
            }

            else
            {
                cat.keyPressed(e);
            }

        }
    }

    /**
     * MouseListener for picking a cat in the starting menu
     */
    private class mouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if ((e.getSource() == startLabel) && startKey)
            {
                Sound.playSound("Click.wav");
                startGame();
                beforeGame = false;
                inGame = true;
                startKey = false; // So that the player can't accidentally restart.
                ageLabel.setVisible(true);
            }

            else if ((e.getSource() == aboutLabel) && startKey)
            {
                Sound.playSound("Click.wav");
                aboutLabelPressed = true;
                ageLabel.setVisible(false);
            }

            else if ((e.getSource() == homeLabel) && startKey)
            {
                Sound.playSound("Click.wav");
                aboutLabelPressed = false;
                ageLabel.setVisible(false);
            }

            else if ((e.getSource() == muteLabel))
            {
                stopLoopingSounds(clpMusicBackground);
                mutePressed = true;
                muteLabel.setVisible(false);
                unmuteLabel.setVisible(true);
            }

            else if ((e.getSource() == unmuteLabel) && mutePressed)
            {
                mutePressed = false;
                clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
                muteLabel.setVisible(true);
                unmuteLabel.setVisible(false);
            }

            startLabel.setVisible(false);
            aboutLabel.setVisible(false);
            homeLabel.setVisible(false);
        }

    }

}
