package dev.lefley.reportlm.view.components;

import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class ReportPane extends JTextPane
{
    public ReportPane()
    {
        setBorder(new EmptyBorder(10, 10, 10, 10));

        setEditable(false);
        setContentType("text/html");
    }
}
