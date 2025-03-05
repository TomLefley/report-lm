package dev.lefley.reportlm.view.components.burp;

import burp.api.montoya.ui.UserInterface;

import java.awt.Font;
import java.util.function.Supplier;

public class BurpFont
{
    private static Supplier<Font> displayFont;

    public static void initialize(UserInterface userInterface)
    {
        displayFont = userInterface::currentDisplayFont;
    }

    public static Font displayFont()
    {
        return displayFont.get();
    }

    public static Font bold()
    {
        return displayFont().deriveFont(Font.BOLD);
    }
}
