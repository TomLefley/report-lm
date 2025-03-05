package dev.lefley.reportlm.view.components;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;

import static java.awt.BorderLayout.CENTER;

public class ReportTab extends JPanel
{
    public ReportTab(InputPanel inputPanel, OutputPanel outputPanel)
    {
        super(new BorderLayout());

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(inputPanel);
        splitPane.setRightComponent(outputPanel);

        add(splitPane, CENTER);

        splitPane.setResizeWeight(0.3f);
    }
}
