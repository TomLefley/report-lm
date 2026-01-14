package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.GenerateReportEvent;

import java.awt.Dimension;

import static dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus.AI_DISABLED;
import static dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus.GENERATION_RUNNING;
import static dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus.READY;
import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.AI;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SPINNER;

public class GenerateReportButton extends IconHoverButton
{
    public GenerateReportButton()
    {
        super(
                "Generate Report",
                icon(AI)
        );

        setGenerationStatus(AI_DISABLED);
        style("ai");

        addActionListener(e -> Events.publish(new GenerateReportEvent()));
    }

    public void setGenerationStatus(GenerationStatus generationStatus)
    {
        String toolTipText = switch (generationStatus)
        {
            case AI_DISABLED -> "Enable AI features to generate a report";
            case ISSUES_NOT_POPULATED -> "Populate issues to generate a report";
            case GENERATION_RUNNING -> "Generation is running";
            case READY -> "Generate a report";
        };
        setToolTipText(toolTipText);

        if (generationStatus == GENERATION_RUNNING)
        {
            Dimension preferredSize = getPreferredSize();
            setText("");
            setIcon(new RotatingIcon(icon(SPINNER).build()));
            setPreferredSize(preferredSize);
        }
        else
        {
            setText("Generate Report");
            setIcon(null);
        }

        setEnabled(generationStatus == READY);
    }
}
