package dev.lefley.reportlm.controller;

import dev.lefley.reportlm.ai.ReportGenerator;
import dev.lefley.reportlm.model.CustomRequirementsModel;
import dev.lefley.reportlm.model.GenerateReportModel;
import dev.lefley.reportlm.model.IssuesModel;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.AiToggledEvent;
import dev.lefley.reportlm.util.Events.GenerateReportEvent;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.view.OutputView;

public class OutputController
{
    private final ToolbarController toolbarController;

    private final IssuesModel issuesModel;
    private final CustomRequirementsModel customRequirementsModel;
    private final GenerateReportModel generateReportModel;

    private final OutputView outputView;

    private final ReportGenerator reportGenerator;

    public OutputController(
            ToolbarController toolbarController,
            IssuesModel issuesModel,
            CustomRequirementsModel customRequirementsModel,
            GenerateReportModel generateReportModel,
            OutputView outputView,
            ReportGenerator reportGenerator
    )
    {
        this.toolbarController = toolbarController;
        this.issuesModel = issuesModel;
        this.customRequirementsModel = customRequirementsModel;
        this.generateReportModel = generateReportModel;
        this.outputView = outputView;

        this.reportGenerator = reportGenerator;

        Events.subscribe(AiToggledEvent.class, event -> setAiEnabled(event.aiEnabled()));
        Events.subscribe(GenerateReportEvent.class, event -> generateReport());
    }

    private void generateReport()
    {
        setGenerationRunning(true);

        reportGenerator.generateReport(customRequirementsModel.getRequirements(), issuesModel.getIssues())
                .thenAccept(outputView::setReport)
                .exceptionally(throwable -> {
                    if (throwable instanceof IllegalStateException)
                    {
                        Logger.logToOutput("Cannot generate report. " + throwable.getMessage());
                    }
                    else
                    {
                        Logger.logToError("Failed to generate report!", throwable);
                    }

                    return null;
                })
                .thenAccept(ignored -> setGenerationRunning(false));
    }

    private void setAiEnabled(boolean aiEnabled)
    {
        generateReportModel.setAiEnabled(aiEnabled);
        toolbarController.setGenerationStatus(generateReportModel.getGenerationStatus());
    }

    private void setGenerationRunning(boolean generationRunning)
    {
        generateReportModel.setGenerationRunning(generationRunning);
        toolbarController.setGenerationStatus(generateReportModel.getGenerationStatus());
    }

    public void setIssuesPopulated(boolean issuesPopulated)
    {
        generateReportModel.setIssuesPopulated(issuesPopulated);
        toolbarController.setGenerationStatus(generateReportModel.getGenerationStatus());
    }
}
