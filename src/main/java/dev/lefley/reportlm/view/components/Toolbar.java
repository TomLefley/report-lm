package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.view.ToolbarView;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

import static javax.swing.BoxLayout.X_AXIS;

public class Toolbar extends JPanel implements ToolbarView
{
    private final RemoveIssueButton removeIssueButton;
    private final GenerateReportButton generateButton;

    public Toolbar()
    {
        setLayout(new BoxLayout(this, X_AXIS));

        removeIssueButton = new RemoveIssueButton();
        add(removeIssueButton);

        add(Box.createHorizontalGlue());

        add(new OpenConfigButton());

        add(Box.createRigidArea(new Dimension(5, 0)));

        generateButton = new GenerateReportButton();
        add(generateButton);
    }

    @Override
    public void setIssuesSelected(boolean issuesSelected)
    {
        SwingUtilities.invokeLater(() -> removeIssueButton.setEnabled(issuesSelected));
    }

    @Override
    public void setGenerationEnabled(boolean canGenerateReport)
    {
        SwingUtilities.invokeLater(() -> generateButton.setEnabled(canGenerateReport));
    }
}
