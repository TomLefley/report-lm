package dev.lefley.reportlm.view.components.burp;

import javax.swing.UIManager;
import java.awt.Color;
import java.util.Objects;

public enum BurpColor
{
    PRIMARY_BUTTON_FOREGROUND("Burp.primaryButtonForeground"),
    PRIMARY_BUTTON_BACKGROUND("Burp.primaryButtonBackground"),

    ACTION_NORMAL("Burp.actionNormal"),
    ACTION_HOVER("Burp.actionHover"),
    ;

    private final String colorKey;

    BurpColor(String colorKey)
    {
        this.colorKey = colorKey;
    }

    public Color getColor()
    {
        return Objects.requireNonNullElse(UIManager.getColor(colorKey), Color.RED);
    }
}
