package dev.lefley.reportlm.controller;

import dev.lefley.reportlm.view.ToolbarView;

public class ToolbarController
{
    private final ToolbarView toolbarView;

    public ToolbarController(ToolbarView toolbarView)
    {
        this.toolbarView = toolbarView;
    }

    public void setIssuesSelected(boolean issuesSelected)
    {
        toolbarView.setIssuesSelected(issuesSelected);
    }

    public void setGenerationEnabled(boolean canGenerateReport)
    {
        toolbarView.setGenerationEnabled(canGenerateReport);
    }
}
