package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.model.Report;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Markdown;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class ReportPanel extends JPanel
{
    private static final String REPORT_CARD = "report";
    public static final String README_CARD = "readme";

    private final CardLayout cardLayout;
    private final HtmlPane reportPane;

    public ReportPanel()
    {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        setBorder(new EmptyBorder(10, 10, 10, 10));

        HtmlPane readme = new HtmlPane(loadReadMe());
        JScrollPane readmeScrollPane = new JScrollPane(readme);
        readmeScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(readmeScrollPane, README_CARD);

        reportPane = new HtmlPane();
        JScrollPane reportScrollPane = new JScrollPane(reportPane);
        reportScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(reportScrollPane, REPORT_CARD);

        showReadMe();
    }

    private String loadReadMe()
    {
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream("resources/README.md"))
        {
            String readMeMd = new String(requireNonNull(resource).readAllBytes());

            return Markdown.renderMarkdownAsHtml(readMeMd);
        }
        catch (IOException | NullPointerException e)
        {
            Logger.logToError("Could not load README.md", e);

            return null;
        }
    }

    public void showReport(Report report)
    {
        try
        {
            reportPane.setPage(report.getIndex().toUri().toURL());
            showReport();
        }
        catch (IOException e)
        {
            Logger.logToError("Could not render report", e);
        }
    }

    public void showReadMe()
    {
        cardLayout.show(this, README_CARD);
    }

    public void showReport()
    {
        cardLayout.show(this, REPORT_CARD);
    }
}
