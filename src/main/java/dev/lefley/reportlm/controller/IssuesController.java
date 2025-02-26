package dev.lefley.reportlm.controller;

import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.ui.contextmenu.AuditIssueContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import dev.lefley.reportlm.model.IssuesModel;

import javax.swing.JMenuItem;
import java.awt.Component;
import java.util.List;

import static java.util.Collections.singletonList;

public class IssuesController implements ContextMenuItemsProvider
{
    private final IssuesModel issuesModel;

    public IssuesController(IssuesModel issuesModel)
    {
        this.issuesModel = issuesModel;
    }

    @Override
    public List<Component> provideMenuItems(AuditIssueContextMenuEvent event)
    {
        List<AuditIssue> auditIssues = event.selectedIssues();

        JMenuItem menuItem = new JMenuItem();
        menuItem.setText("Add " + (auditIssues.size() > 1 ? "selected issues" : "issue") + " to report");
        menuItem.addActionListener(e -> issuesModel.addIssues(auditIssues));

        return singletonList(menuItem);
    }
}
