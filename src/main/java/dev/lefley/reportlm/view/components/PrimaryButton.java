package dev.lefley.reportlm.view.components;

import com.formdev.flatlaf.ui.FlatButtonBorder;
import dev.lefley.reportlm.view.components.burp.BurpIcon;

import java.awt.Component;

import static dev.lefley.reportlm.view.components.burp.BurpColor.PRIMARY_BUTTON_BACKGROUND;
import static dev.lefley.reportlm.view.components.burp.BurpColor.PRIMARY_BUTTON_FOREGROUND;
import static dev.lefley.reportlm.view.components.burp.BurpFont.bold;

public class PrimaryButton extends IconHoverButton
{
    public PrimaryButton(String text, BurpIcon.Builder icon)
    {
        super(
                text,
                icon.fontSized()
                        .withNormalColour(PRIMARY_BUTTON_FOREGROUND)
                        .withHoverColour(PRIMARY_BUTTON_FOREGROUND)
                        .build()
        );

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
