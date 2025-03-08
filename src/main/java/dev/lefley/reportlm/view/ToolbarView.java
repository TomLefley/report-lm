package dev.lefley.reportlm.view;

import dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus;

public interface ToolbarView
{
    void setIssuesSelected(boolean issuesSelected);

    void setGenerationStatus(GenerationStatus generationStatus);
}
