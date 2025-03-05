package dev.lefley.reportlm.view;

import dev.lefley.reportlm.controller.ReportController;
import dev.lefley.reportlm.model.IssuesModel;
import dev.lefley.reportlm.util.Threads;
import dev.lefley.reportlm.view.components.CustomInstructionsInput;
import dev.lefley.reportlm.view.components.IssuesPanel;
import dev.lefley.reportlm.view.components.PrimaryButton;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.util.concurrent.TimeUnit;

import static javax.swing.BoxLayout.Y_AXIS;

public class InputPanel extends JPanel
{
    public InputPanel(ReportController reportController, IssuesModel issuesModel)
    {
        setLayout(new BoxLayout(this, Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        PrimaryButton generateButton = new PrimaryButton("Generate Report");
        Threads.scheduleAtFixedRate(
                () -> {
                    if (reportController.isAiEnabled())
                    {
                        generateButton.setEnabled(true);
                        generateButton.setToolTipText("Generate a report");
                    }
                    else
                    {
                        generateButton.setEnabled(false);
                        generateButton.setToolTipText("Enable AI features to generate a report");
                    }
                },
                0,
                1,
                TimeUnit.SECONDS
        );

        IssuesPanel issuesPanel = new IssuesPanel(issuesModel, generateButton);
        add(issuesPanel);

        add(Box.createVerticalStrut(10));

        CustomInstructionsInput customInstructionsInput = new CustomInstructionsInput();
        add(new JScrollPane(customInstructionsInput));

        generateButton.addActionListener(e -> {
            generateButton.setEnabled(false);
            reportController.generateReport(
                    customInstructionsInput.getValue(),
                    issuesModel.getIssues()
            )
                    .thenRunAsync(() -> generateButton.setEnabled(true), SwingUtilities::invokeLater);
        });
    }
}
