package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.view.components.burp.BurpIcon;

import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimpleIconButton extends JLabel
{
    public SimpleIconButton(BurpIcon icon)
    {
        super(icon);

        MouseAdapter mouseAdapter = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                ((BurpIcon) getIcon()).setHover();

                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                ((BurpIcon) getIcon()).setNormal();

                repaint();
            }
        };

        addMouseListener(mouseAdapter);
    }

    public void addClickListener(Runnable onClick)
    {
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                onClick.run();
            }
        });
    }
}
