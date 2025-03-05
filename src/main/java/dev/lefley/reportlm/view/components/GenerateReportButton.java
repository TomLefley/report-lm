package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.GenerateReportEvent;

public class GenerateReportButton extends PrimaryButton
{
    public GenerateReportButton()
    {
        super("Generate Report");

        setEnabled(false);

        addActionListener(e -> Events.publish(new GenerateReportEvent()));
    }

    public void setEnabled(boolean enabled)
    {
        setToolTipText(enabled ? "Generate a report" : "Enable AI features to generate a report");
        super.setEnabled(enabled);
    }
}
