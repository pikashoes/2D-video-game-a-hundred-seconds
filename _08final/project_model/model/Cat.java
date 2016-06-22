package _08final.project_model.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Cat class.
 */
public class Cat extends Sprite
{
    private double deltaX, deltaY;
    private double deltaM;
    private ArrayList<Missile> missiles;

    private double health;

    /**
     * Cat constructor.
     * @param x - x coordinate.
     * @param y - y coordinate.
     */
    public Cat(int x, int y)
    {
        super(x, y);
        initCat();
    }

    /**
     * Creates a new cat. Varies the missile and the movement speed based on cat type.
     */
    public void initCat()
    {
        missiles = new ArrayList<>();
        health = 100; // Initial health

        displayImage("flying.gif");
        deltaM = 4;
        getImageDim();

    }

    /**
     * Moves the cat.
     */
    public void moveCat()
    {
        boolean move = true;

        // Prevents the cat from going out of bounds.
        if (x < 1)
        {
            x = 1;
            move = false;
        }

        if (y < 1)
        {
            y = 1;
            move = false;
        }

        if (x > (1000 - this.width))
        {
            x = 1000 - this.width;
            move = false;
        }

        if (y > (800 - this.height))
        {
            y = 800 - this.height;
            move = false;
        }

        if (move)
        {
            x += deltaX;
            y += deltaY;
        }
    }

    public ArrayList getMissiles()
    {
        return missiles;
    }

    public double getHealth()
    {
        return health;
    }

    public double changeHealth(double x)
    {
        return health -= x;
    }

    public void setNewSpeed(double x)
    {
        deltaM = x;
    }


    //===========================================================
    // KEYBOARD EVENTS
    //===========================================================

    /**
     * When key is pressed, if it's up, move up. If it's down, move down.
     * @param e
     */
    public void keyPressed(KeyEvent e)
    {
        int nKey = e.getKeyCode();
        // If up, move up.
        if (nKey == KeyEvent.VK_UP)
        {
            deltaY = -deltaM;
        }

        // If down, move down.
        if (nKey == KeyEvent.VK_DOWN)
        {
            deltaY = deltaM;
        }

        if (nKey == KeyEvent.VK_LEFT)
        {
            deltaX = -deltaM;
        }

        if (nKey == KeyEvent.VK_RIGHT)
        {
            deltaX = deltaM;
        }

        if (nKey == KeyEvent.VK_SPACE)
        {
            fire();
        }
    }

    public void keyReleased(KeyEvent e)
    {
        int nKey = e.getKeyCode();
        // If up, move up.
        if (nKey == KeyEvent.VK_UP)
        {
            deltaY = 0;
        }

        // If down, move down.
        if (nKey == KeyEvent.VK_DOWN)
        {
            deltaY = 0;
        }

        if (nKey == KeyEvent.VK_LEFT)
        {
            deltaX = 0;
        }

        if (nKey == KeyEvent.VK_RIGHT)
        {
            deltaX = 0;
        }
    }

    public void fire()
    {
        missiles.add(new Missile(x + width, y + height / 2));
    }

}
