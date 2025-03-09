package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.util.Logger;

import javax.swing.JTextPane;
import java.awt.Desktop;

import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;

public class HtmlPane extends JTextPane
{
    public HtmlPane()
    {
        setEditable(false);
        setContentType("text/html");

        addHyperlinkListener(e -> {
            if (e.getEventType() == ACTIVATED)
            {
                try
                {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                }
                catch (Exception ex)
                {
                    Logger.logToError("Could not open hyperlink", ex);
                }
            }
        });
    }

    public HtmlPane(String content)
    {
        this();

        setText(content);
    }
}
