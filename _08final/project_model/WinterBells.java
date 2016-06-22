package _08final.project_model;

import javax.swing.*;
import java.awt.*;


/**
 * Main Frame. Contains the main() method that runs the game.
 */

public class WinterBells extends JFrame
{
    // Background image/gif
    private java.net.URL imgUrl = getClass().getResource("output.gif");

    /**
     * Constructor
     */
    public WinterBells()
    {
        final Image IMAGE = new ImageIcon(imgUrl).getImage();
        WinterBellsPanel winterbells = new WinterBellsPanel(IMAGE);
        add(winterbells);

        setResizable(false);
        pack();

        setTitle("100 Seconds");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                WinterBells winterbells = new WinterBells();
                winterbells.setVisible(true);
            }
        });

    }
}
