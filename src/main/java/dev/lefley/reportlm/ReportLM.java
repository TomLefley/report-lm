package dev.lefley.reportlm;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.EnhancedCapability;
import burp.api.montoya.MontoyaApi;
import dev.lefley.reportlm.controller.IssuesController;
import dev.lefley.reportlm.controller.ReportController;
import dev.lefley.reportlm.model.IssuesModel;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Threads;
import dev.lefley.reportlm.view.InputPanel;
import dev.lefley.reportlm.view.OutputPanel;
import dev.lefley.reportlm.view.ReportTab;
import dev.lefley.reportlm.view.components.burp.BurpFont;

import java.util.Set;

import static burp.api.montoya.EnhancedCapability.AI_FEATURES;

@SuppressWarnings("unused")
public class ReportLM implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi montoyaApi)
    {
        Logger.initialize(montoyaApi.logging());
        BurpFont.initialize(montoyaApi.userInterface());
        Threads.initialize(montoyaApi.extension());


        IssuesModel issuesModel = new IssuesModel();
        IssuesController issuesController = new IssuesController(issuesModel);
        montoyaApi.userInterface().registerContextMenuItemsProvider(issuesController);

        OutputPanel outputPanel = new OutputPanel();
        ReportController reportController = new ReportController(montoyaApi.ai(), outputPanel);
        InputPanel inputPanel = new InputPanel(reportController, issuesModel);

        ReportTab reportTab = new ReportTab(inputPanel, outputPanel);
        montoyaApi.userInterface().registerSuiteTab("ReportLM", reportTab);
    }

    @Override
    public Set<EnhancedCapability> enhancedCapabilities()
    {
        return Set.of(AI_FEATURES);
    }
}
