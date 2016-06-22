package _08final.project_model.model;

/**
 * Missile class.
 */
public class Missile extends Sprite
{
    private final int BOARD_WIDTH = 1000;
    private final int MISSILE_SPEED = 3;

    /**
     * @param x
     * @param y
     */
    public Missile(int x, int y)
    {
        super(x, y);

        displayImage("starmissile.png");
        getImageDim();
    }

    /**
     * Moves the missile.
     */
    public void move()
    {
        x += MISSILE_SPEED;

        if (x > BOARD_WIDTH)
        {
            vis = false;
        }
    }

}
