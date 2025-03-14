package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.util.Events;

import javax.swing.JButton;

import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.DELETE;

public class RemoveIssueButton extends JButton
{
    public RemoveIssueButton()
    {
        super(icon(DELETE).fontSized().build());

        setEnabled(false);

        addActionListener(e -> Events.publish(new Events.RemoveSelectedIssues()));
    }

    public void setEnabled(boolean enabled)
    {
        setToolTipText(enabled ? "Remove selected issues" : "No issues selected");

        super.setEnabled(enabled);
    }
}
