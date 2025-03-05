package dev.lefley.reportlm.view.components;

import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.ui.contextmenu.AuditIssueContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import dev.lefley.reportlm.util.Events;

import javax.swing.JMenuItem;
import java.awt.Component;
import java.util.List;

import static java.util.Collections.singletonList;

public class ReportMenuItemsProvider implements ContextMenuItemsProvider
{
    @Override
    public List<Component> provideMenuItems(AuditIssueContextMenuEvent event)
    {
        List<AuditIssue> auditIssues = event.selectedIssues();

        JMenuItem menuItem = new JMenuItem();
        menuItem.setText("Add " + (auditIssues.size() > 1 ? "selected issues" : "issue") + " to report");
        menuItem.addActionListener(e -> Events.publish(new Events.AddIssuesEvent(auditIssues)));

        return singletonList(menuItem);
    }
}
