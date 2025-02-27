package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.util.Logger;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;
import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;

public class ReportPane extends JTextPane
{
    private final String readMe;

    private String report;

    public ReportPane()
    {
        setBorder(new EmptyBorder(10, 10, 10, 10));

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
                    Logger.logToError("Could not open report hyperlink", ex);
                }
            }
        });

        this.readMe = loadReadMe();
        showReadMe();
    }

    private String loadReadMe()
    {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream("resources/README.md"))
        {
            String readMeMd = new String(requireNonNull(resource).readAllBytes());
            Node document = Parser.builder().build().parse(readMeMd);

            return HtmlRenderer.builder().build().render(document);
        }
        catch (IOException | NullPointerException e)
        {
            Logger.logToError("Could not load README.md", e);

            return null;
        }
    }

    public void setReport(String report)
    {
        this.report = report;

        setText(report);
    }

    public void showReadMe()
    {
        setText(readMe);
    }

    public void hideReadMe()
    {
        setText(report);
    }
}
