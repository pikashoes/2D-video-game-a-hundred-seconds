package _08final.project_model.model;

import javax.swing.*;
import java.awt.*;

/**
 * Sprite class
 */
public abstract class Sprite
{
    protected int x;
    protected int y;

    protected int width;
    protected int height;
    protected boolean vis;
    protected Image image;

    /**
     * Constructor. Initializes visibility to true.
     * @param x
     * @param y
     */
    public Sprite(int x, int y)
    {
        this.x = x;
        this.y = y;
        vis = true;
    }

    /**
     * Displays the image based on the image file name.
     * @param imageName
     */
    protected void displayImage(String imageName)
    {
        java.net.URL imgUrl = getClass().getResource(imageName);
        ImageIcon imageIcon = new ImageIcon(imgUrl);
        image = imageIcon.getImage();
    }

    /**
     * Gets the dimensions of the image.
     */
    protected void getImageDim()
    {
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    /**
     * Returns the image.
     * @return
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * Gets X coordinate.
     * @return
     */
    public int getX()
    {
        return x;
    }

    /**
     * Gets y coordinate.
     * @return
     */
    public int getY()
    {
        return y;
    }

    /**
     * Check if visible.
     * @return
     */
    public boolean isVisible()
    {
        return vis;
    }

    /**
     * Set visible.
     * @param visible
     */
    public void setVisible(Boolean visible)
    {
        vis = visible;
    }

    /**
     * Determines the bounds of the image.
     * @return
     */
    // For collision detection
    public Rectangle getBounds()
    {
        return new Rectangle(x, y, width, height);
    }
}
