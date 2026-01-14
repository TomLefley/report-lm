package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.view.components.burp.BurpIcon;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.formdev.flatlaf.FlatClientProperties.STYLE_CLASS;

public class IconHoverButton extends JButton
{
    public IconHoverButton(String text, BurpIcon.Builder iconBuilder)
    {
        super(text);

        BurpIcon icon = iconBuilder.fontSized().build();
        icon.setHover();

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (isEnabled())
                {
                    Dimension preferredSize = getPreferredSize();
                    setText("");
                    setIcon(icon);
                    setPreferredSize(preferredSize);
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (isEnabled())
                {
                    setText(text);
                    setIcon(null);
                }
            }
        });
    }

    public void style(String styleKey)
    {
        putClientProperty(STYLE_CLASS, styleKey);
    }
}
