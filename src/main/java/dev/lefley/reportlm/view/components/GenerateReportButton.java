package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.GenerateReportEvent;

import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.AI;

public class GenerateReportButton extends PrimaryButton
{
    public GenerateReportButton()
    {
        super(
                "Generate Report",
                icon(AI)
        );

        setEnabled(false);

        addActionListener(e -> Events.publish(new GenerateReportEvent()));
    }

    public void setEnabled(boolean enabled)
    {
        setToolTipText(enabled ? "Generate a report" : "Enable AI features to generate a report");
        super.setEnabled(enabled);
    }
}
