package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.view.components.burp.BurpIcon;

import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.README;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.REPORT;

public class ToggleReadMeButton extends SimpleIconButton
{
    private static final BurpIcon READ_ME_ICON = icon(README).fontSized().build();
    private static final BurpIcon REPORT_ICON = icon(REPORT).fontSized().build();

    private final Runnable showReadMe;
    private final Runnable showReport;

    private boolean isReadMeShowing;

    public ToggleReadMeButton(Runnable showReadMe, Runnable showReport)
    {
        super(READ_ME_ICON);

        this.showReadMe = showReadMe;
        this.showReport = showReport;

        addClickListener(() -> toggle(isReadMeShowing));

        toggle(false);
    }

    public void toggle(boolean transitionToReport)
    {
        if (transitionToReport)
        {
            setIcon(READ_ME_ICON);
            setToolTipText("Show README");

            showReport.run();
        }
        else
        {
            setIcon(REPORT_ICON);
            setToolTipText("Show report");

            showReadMe.run();
        }

        isReadMeShowing = !transitionToReport;
    }
}
