package _08final.project_model.model;

import java.util.Random;

/**
 * Bird class. AKA Enemy (skull sprite).
 */
public class Bird extends Sprite
{
    private Random number = new Random();
    private int randNum;
    private int heightchanger;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public Bird(int x, int y)
    {
        super(x, y);
        initBird();

        randNum = number.nextInt(3) + 1;
    }

    /**
     * Initializes bird and displays the bird image.
     */
    private void initBird()
    {
        displayImage("skull.png");
        getImageDim();
    }

    /**
     * Moves the bird.
     */
    public void move()
    {
        // When it goes off screen on the left, set the visibility to false.
        if (x < 0)
        {
            vis = false;
        }

        if (y < 1)
        {
            y = 1;
        }

        if (y > (800 - this.height))
        {
            y = 800 - this.height;
        }

        // Move height up and down
        heightchanger = number.nextInt(2);
        if (heightchanger == 0)
        {
            y -= 1;
        }

        else if (heightchanger == 1)
        {
            y += 1;
        }

        // Make the speed vary.
        x -= randNum;
    }
}
