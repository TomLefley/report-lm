package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.model.IssuesModel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;

public class InputPanel extends JPanel
{
    public InputPanel(Toolbar toolbar, CustomRequirementsInput customInstructionsInput, IssuesModel issuesModel)
    {
        super(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(0, 0, 5,0));
        topPanel.setLayout(new BoxLayout(topPanel, Y_AXIS));

        topPanel.add(toolbar);
        topPanel.add(Box.createVerticalStrut(10));

        IssuesPanel issuesPanel = new IssuesPanel(issuesModel);
        topPanel.add(issuesPanel);

        JScrollPane bottomPanel = new JScrollPane(customInstructionsInput);
        bottomPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JSplitPane splitPane = new JSplitPane(VERTICAL_SPLIT);
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(bottomPanel);

        add(splitPane, CENTER);

        splitPane.setResizeWeight(0.6f);
    }
}
