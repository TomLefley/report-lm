package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.model.Report;
import dev.lefley.reportlm.view.OutputView;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class OutputPanel extends JPanel implements OutputView
{
    private final ReportPanel reportPanel;
    private final CopyToClipboardButton copyToClipboardButton;
    private final SaveButton saveButton;
    private final ToggleReadMeButton toggleReadMeButton;

    private Report report;

    public OutputPanel()
    {
        super(new BorderLayout());

        setLayout(new BoxLayout(this, Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        reportPanel = new ReportPanel();

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, X_AXIS));

        toolbar.add(Box.createHorizontalGlue());

        copyToClipboardButton = new CopyToClipboardButton(() -> report.readIndex());
        copyToClipboardButton.setVisible(false);
        toolbar.add(copyToClipboardButton);

        toolbar.add(Box.createRigidArea(new Dimension(10, 0)));

        saveButton = new SaveButton(() -> report.getDirectory());
        saveButton.setVisible(false);
        toolbar.add(saveButton);

        toolbar.add(Box.createRigidArea(new Dimension(10, 0)));

        toggleReadMeButton = new ToggleReadMeButton(this::showReadMe, this::showReport);
        toggleReadMeButton.setVisible(false);
        toolbar.add(toggleReadMeButton);

        add(toolbar);

        add(Box.createVerticalStrut(10));

        add(reportPanel);
    }

    @Override
    public void setReport(Report report)
    {
        SwingUtilities.invokeLater(() -> {
            this.report = report;

            reportPanel.showReport(report);

            toggleReadMeButton.setVisible(true);
            toggleReadMeButton.toggle(true);

            copyToClipboardButton.setVisible(true);
            saveButton.setVisible(true);
        });
    }

    private void showReadMe()
    {
        copyToClipboardButton.setVisible(false);
        saveButton.setVisible(false);
        reportPanel.showReadMe();
    }

    private void showReport()
    {
        copyToClipboardButton.setVisible(true);
        saveButton.setVisible(true);
        reportPanel.showReport();
    }
}
