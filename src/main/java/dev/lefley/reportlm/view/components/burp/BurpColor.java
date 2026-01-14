package dev.lefley.reportlm.view.components.burp;

import javax.swing.UIManager;
import java.awt.Color;

public enum BurpColor
{
    ICON_DEFAULT("Colors.ui.icon.default", "Burp.actionNormal"),
    ICON_HOVER("Colors.ui.icon.hover", "Burp.actionHover"),
    ;

    private final String[] colorKeys;

    BurpColor(String... colorKeys)
    {
        this.colorKeys = colorKeys;
    }

    public Color getColor()
    {
        for (String colorKey : colorKeys)
        {
            Color color = UIManager.getColor(colorKey);
            if (color != null)
            {
                return color;
            }
        }

        return Color.RED;
    }
}
