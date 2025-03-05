package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.view.OutputView;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class OutputPanel extends JPanel implements OutputView
{
    private final ReportPane reportPane;
    private final CopyToClipboardButton copyToClipboardButton;
    private final ToggleReadMeButton toggleReadMeButton;

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

        toolbar.add(Box.createRigidArea(new Dimension(10, 0)));

        toggleReadMeButton = new ToggleReadMeButton(this::showReadMe, this::hideReadMe);
        toggleReadMeButton.setVisible(false);
        toolbar.add(toggleReadMeButton);

        add(toolbar);

        add(Box.createVerticalStrut(10));

        JScrollPane scrollPane = new JScrollPane(reportPane);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(scrollPane);
    }

    @Override
    public void setReport(String report)
    {
        SwingUtilities.invokeLater(() -> {
            toggleReadMeButton.setVisible(true);
            toggleReadMeButton.toggle(true);

            copyToClipboardButton.setVisible(true);
            reportPane.setReport(report);
        });
    }

    private void showReadMe()
    {
        copyToClipboardButton.setVisible(false);
        reportPane.showReadMe();
    }

    private void hideReadMe()
    {
        copyToClipboardButton.setVisible(true);
        reportPane.hideReadMe();
    }
}
