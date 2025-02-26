package dev.lefley.reportlm.view.components;

import com.formdev.flatlaf.ui.FlatButtonBorder;

import javax.swing.JButton;
import java.awt.Component;

import static dev.lefley.reportlm.view.components.burp.BurpColor.PRIMARY_BUTTON_BACKGROUND;
import static dev.lefley.reportlm.view.components.burp.BurpColor.PRIMARY_BUTTON_FOREGROUND;
import static dev.lefley.reportlm.view.components.burp.BurpFont.bold;

public class PrimaryButton extends JButton
{
    public PrimaryButton(String text)
    {
        super(text);

        setColors();

        setFont(bold());

        updateUI();
    }

    private void setColors()
    {
        setBackground(PRIMARY_BUTTON_BACKGROUND.getColor());
        setForeground(PRIMARY_BUTTON_FOREGROUND.getColor());
    }

    @Override
    public void updateUI()
    {
        super.updateUI();

        setColors();

        setBorder(new FlatButtonBorder()
        {
            @Override
            protected float getBorderWidth(Component c)
            {
                return c.isEnabled() ? 0F : 1F;
            }
        });
    }

}
