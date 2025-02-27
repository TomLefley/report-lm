package dev.lefley.reportlm.view;

import burp.api.montoya.MontoyaApi;

import dev.lefley.reportlm.controller.IssuesController;
import dev.lefley.reportlm.controller.ReportController;
import dev.lefley.reportlm.model.IssuesModel;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;

import static java.awt.BorderLayout.CENTER;

public class ReportTab extends JPanel
{
    public ReportTab(MontoyaApi montoyaApi)
    {
        super(new BorderLayout());

        IssuesModel issuesModel = new IssuesModel();
        IssuesController issuesController = new IssuesController(issuesModel);
        montoyaApi.userInterface().registerContextMenuItemsProvider(issuesController);

        OutputPanel outputPanel = new OutputPanel();
        ReportController reportController = new ReportController(montoyaApi.ai(), outputPanel);
        InputPanel inputPanel = new InputPanel(montoyaApi.ai(), reportController, issuesModel);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(inputPanel);
        splitPane.setRightComponent(outputPanel);

        add(splitPane, CENTER);

        splitPane.setResizeWeight(0.3f);
    }
}
