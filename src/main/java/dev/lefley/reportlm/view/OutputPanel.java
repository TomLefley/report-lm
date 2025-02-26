package dev.lefley.reportlm.view;

import dev.lefley.reportlm.view.components.CopyToClipboardButton;
import dev.lefley.reportlm.view.components.ReportPane;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class OutputPanel extends JPanel
{
    private final ReportPane reportPane;
    private final CopyToClipboardButton copyToClipboardButton;

    public OutputPanel()
    {
        super(new BorderLayout());

        setLayout(new BoxLayout(this, Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        reportPane = new ReportPane();

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, X_AXIS));

        toolbar.add(Box.createHorizontalGlue());

        copyToClipboardButton = new CopyToClipboardButton(reportPane::getText);
        copyToClipboardButton.setVisible(false);
        toolbar.add(copyToClipboardButton);

        add(toolbar);

        add(Box.createVerticalStrut(10));

        JScrollPane scrollPane = new JScrollPane(reportPane);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(scrollPane);
    }

    public void setReport(String report)
    {
        copyToClipboardButton.setVisible(true);
        reportPane.setText(report);
    }
}
