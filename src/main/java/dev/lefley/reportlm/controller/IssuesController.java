package dev.lefley.reportlm.controller;

import burp.api.montoya.scanner.audit.issues.AuditIssue;
import dev.lefley.reportlm.model.IssuesModel;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.AddIssuesEvent;
import dev.lefley.reportlm.util.Events.IssuesSelectedEvent;
import dev.lefley.reportlm.util.Events.RemoveSelectedIssues;

import java.util.List;

public class IssuesController
{
    private final ToolbarController toolbarController;
    private final OutputController outputController;
    private final IssuesModel issuesModel;

    public IssuesController(ToolbarController toolbarController, OutputController outputController, IssuesModel issuesModel)
    {
        this.toolbarController = toolbarController;
        this.outputController = outputController;
        this.issuesModel = issuesModel;

        Events.subscribe(AddIssuesEvent.class, e -> addIssues(e.auditIssues()));
        Events.subscribe(IssuesSelectedEvent.class, e -> setIssuesSelected(e.selectedRows()));
        Events.subscribe(RemoveSelectedIssues.class, e -> removeSelectedIssues());
    }

    private void addIssues(List<AuditIssue> auditIssues)
    {
        issuesModel.addIssues(auditIssues);

        setIssuesPopulated(!auditIssues.isEmpty());
    }

    private void removeSelectedIssues()
    {
        issuesModel.removeSelectedIssues();

        setIssuesPopulated(!issuesModel.getIssues().isEmpty());
    }

    private void setIssuesPopulated(boolean issuesPopulated)
    {
        outputController.setIssuesPopulated(issuesPopulated);
    }

    private void setIssuesSelected(int[] selectedRows)
    {
        issuesModel.setSelectedRows(selectedRows);
        toolbarController.setIssuesSelected(selectedRows.length > 0);
    }
}
