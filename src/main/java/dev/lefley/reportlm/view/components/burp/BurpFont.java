package dev.lefley.reportlm.view.components.burp;

import burp.api.montoya.ui.UserInterface;

import java.awt.Font;

public class BurpFont
{
    private static Font displayFont;

    public static void initialize(UserInterface userInterface)
    {
        displayFont = userInterface.currentDisplayFont();
    }

    public static Font displayFont()
    {
        return displayFont;
    }

    public static Font bold()
    {
        return displayFont.deriveFont(Font.BOLD);
    }
}
