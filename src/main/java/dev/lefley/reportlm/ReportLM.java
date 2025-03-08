package dev.lefley.reportlm;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.EnhancedCapability;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ai.Ai;
import dev.lefley.reportlm.ai.ReportGenerator;
import dev.lefley.reportlm.ai.TraceLoggingAi;
import dev.lefley.reportlm.controller.ConfigController;
import dev.lefley.reportlm.controller.IssuesController;
import dev.lefley.reportlm.controller.OutputController;
import dev.lefley.reportlm.controller.ToolbarController;
import dev.lefley.reportlm.model.ConfigModel;
import dev.lefley.reportlm.model.GenerateReportModel;
import dev.lefley.reportlm.model.IssuesModel;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Threads;
import dev.lefley.reportlm.view.components.CustomRequirementsInput;
import dev.lefley.reportlm.view.components.InputPanel;
import dev.lefley.reportlm.view.components.OutputPanel;
import dev.lefley.reportlm.view.components.ReportMenuItemsProvider;
import dev.lefley.reportlm.view.components.ReportTab;
import dev.lefley.reportlm.view.components.Toolbar;
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

        Ai ai = new TraceLoggingAi(montoyaApi.ai());

        ConfigModel configModel = new ConfigModel(montoyaApi.persistence().preferences());
        IssuesModel issuesModel = new IssuesModel();
        CustomRequirementsInput customInstructionsModel = new CustomRequirementsInput();
        GenerateReportModel generateReportModel = new GenerateReportModel();

        ReportGenerator reportGenerator = new ReportGenerator(ai, configModel);

        Toolbar toolbar = new Toolbar(configModel);
        InputPanel inputPanel = new InputPanel(toolbar, customInstructionsModel, issuesModel);
        OutputPanel outputPanel = new OutputPanel();
        ReportTab reportTab = new ReportTab(inputPanel, outputPanel);

        montoyaApi.userInterface().registerSuiteTab("ReportLM", reportTab);
        montoyaApi.userInterface().registerContextMenuItemsProvider(new ReportMenuItemsProvider());

        ConfigController configController = new ConfigController(configModel);
        ToolbarController toolbarController = new ToolbarController(toolbar);
        OutputController outputController = new OutputController(toolbarController, issuesModel, customInstructionsModel, generateReportModel, outputPanel, reportGenerator);
        IssuesController issuesController = new IssuesController(toolbarController, outputController, issuesModel);
    }

    @Override
    public Set<EnhancedCapability> enhancedCapabilities()
    {
        return Set.of(AI_FEATURES);
    }
}
