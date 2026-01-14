package dev.lefley.reportlm.view.components.burp;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.Color;

public class BurpIcon extends FlatSVGIcon
{
    private static final Color PRIMARY = Color.BLACK;

    private final BurpColor normalColour;
    private final BurpColor hoverColour;

    private final ColourMapping primaryColourMapping;
    private final ColorFilter colorFilter;

    public BurpIcon(BurpIconFile iconFile, float scale, BurpColor normalColour, BurpColor hoverColour)
    {
        super(iconFile.getFile(), scale);

        this.normalColour = normalColour;
        this.hoverColour = hoverColour;

        this.primaryColourMapping = new ColourMapping(PRIMARY);

        this.colorFilter = new ColorFilter();

        setColorFilter(colorFilter);
        setNormal();
    }

    public void setNormal()
    {
        primaryColourMapping.setToColor(normalColour);
    }

    public void setHover()
    {
        primaryColourMapping.setToColor(hoverColour);
    }

    public class ColourMapping
    {
        private final Color fromColor;

        private ColourMapping(Color fromColor)
        {
            this.fromColor = fromColor;
        }

        public void setToColor(BurpColor toColor)
        {
            colorFilter.remove(fromColor);

            if (toColor != null)
            {
                colorFilter.add(fromColor, toColor.getColor());
            }
        }

    }

    public static class Builder
    {
        private final BurpIconFile iconFile;

        private float scale = 1.0f;
        private BurpColor normalColour = BurpColor.ICON_DEFAULT;
        private BurpColor hoverColour = BurpColor.ICON_HOVER;

        private Builder(BurpIconFile iconFile)
        {
            this.iconFile = iconFile;
        }

        public static Builder icon(BurpIconFile iconFile)
        {
            return new Builder(iconFile);
        }

        public Builder fontSized()
        {
            this.scale = BurpFont.displayFont().getSize() / 20f;
            return this;
        }

        public Builder withNormalColour(BurpColor normalColour)
        {
            this.normalColour = normalColour;
            return this;
        }

        public Builder withHoverColour(BurpColor hoverColour)
        {
            this.hoverColour = hoverColour;
            return this;
        }

        public BurpIcon build()
        {
            return new BurpIcon(iconFile, scale, normalColour, hoverColour);
        }
    }
}