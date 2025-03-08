package dev.lefley.reportlm.view.components;

import javax.swing.Icon;
import javax.swing.Timer;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RotatingIcon implements Icon
{
    private static final float TWO_PI = (float) (2 * Math.PI);

    private final Icon iconToRotate;
    private final int timerDelay;
    private final float rotationSpeed;
    private final boolean singleRotation;

    private float rotation;

    private long lastUpdateTime;
    private Timer timer;

    public RotatingIcon(Icon iconToRotate)
    {
        this(iconToRotate, 20, 1, false);
    }

    public RotatingIcon(Icon iconToRotate, int framesPerSecond, float fullRotationSeconds, boolean singleRotation)
    {
        this.iconToRotate = iconToRotate;
        this.timerDelay = 1000 / framesPerSecond;
        this.rotationSpeed = TWO_PI / fullRotationSeconds;
        this.singleRotation = singleRotation;

        this.lastUpdateTime = System.nanoTime();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        if (singleRotation && rotation >= TWO_PI)
        {
            iconToRotate.paintIcon(c, g, x, y);
            return;
        }

        if (c.isShowing())
        {
            if (timer == null)
            {
                lastUpdateTime = System.nanoTime();

                timer = new Timer(timerDelay, e -> c.repaint());
                timer.start();
            }

            updateRotation();

            Graphics2D g2 = null;

            try
            {
                g2 = getRotatedGraphics(g, x, y);

                iconToRotate.paintIcon(c, g2, x, y);
            }
            finally
            {
                if (g2 != null)
                {
                    g2.dispose();
                }
            }
        }
        else
        {
            timer.stop();
            timer = null;
        }
    }

    private void updateRotation()
    {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000f;
        lastUpdateTime = currentTime;

        rotation += rotationSpeed * deltaTime;

        if (!singleRotation && Float.compare(rotation, TWO_PI) >= 0)
        {
            rotation = rotation - TWO_PI;
        }
    }

    private Graphics2D getRotatedGraphics(Graphics g, int x, int y)
    {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.rotate(rotation, x + getIconWidth() / 2f, y + getIconHeight() / 2f);

        return g2;
    }

    @Override
    public int getIconWidth()
    {
        return iconToRotate.getIconWidth();
    }

    @Override
    public int getIconHeight()
    {
        return iconToRotate.getIconHeight();
    }
}
