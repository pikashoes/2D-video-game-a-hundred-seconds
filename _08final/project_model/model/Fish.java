package _08final.project_model.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Fish class. Different types of fish with same attributes.
 */
public class Fish extends Sprite
{
    private Random number = new Random();
    private int randSpeed;
    private String fishidentity; // Which one to show

    /**
     * Constructor. Initializes location.
     * @param x
     * @param y
     */
    public Fish(int x, int y)
    {
        super(x, y);

        randSpeed = number.nextInt(5) + 1; // Random speed generator

        initFish();
    }

    /**
     * Creates a fish.
     */
    private void initFish()
    {
        // Create system for different random fish to show up.
        // Some may show up more often than others in a given session. Life = different opportunities.
        int randomFish = number.nextInt(5) + 1;
        if (randomFish == 1)
        {
            fishidentity = "Heart"; // Love
            displayImage("heart.png"); // heart
            getImageDim();
        }

        else if (randomFish == 2)
        {
            fishidentity = "Book"; // Academics
            displayImage("book.png");
            getImageDim();
        }

        else if (randomFish == 3)
        {
            fishidentity = "Fist";
            displayImage("fist.png");
            getImageDim();
        }

        else if (randomFish == 4)
        {
            fishidentity = "Star";
            displayImage("star.png");
            getImageDim();
        }
    }

    //=====================================
    // Methods for getting counts for fish
    //=====================================
    public String getFishidentity()
    {
        return fishidentity;
    }

    public void move()
    {

        if (x < 0)
        {
            vis = false;
        }

        // Makes the speed vary
        x -= randSpeed;
    }
}
