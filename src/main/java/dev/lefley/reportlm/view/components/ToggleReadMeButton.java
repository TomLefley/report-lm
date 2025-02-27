package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.view.components.burp.BurpIcon;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static dev.lefley.reportlm.view.components.burp.BurpColor.ACTION_HOVER;
import static dev.lefley.reportlm.view.components.burp.BurpColor.ACTION_NORMAL;
import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.README;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.REPORT;

public class ToggleReadMeButton extends JLabel
{
    private final Runnable showReadMe;
    private final Runnable showReport;

    private final BurpIcon readMeIcon;
    private final BurpIcon reportIcon;

    private boolean isReadMeShowing;

    public ToggleReadMeButton(Runnable showReadMe, Runnable showReport)
    {
        this.showReadMe = showReadMe;
        this.showReport = showReport;

        setFocusable(false);
        setHorizontalTextPosition(SwingConstants.LEADING);

        readMeIcon = icon(README).fontSized().withNormalColour(ACTION_NORMAL).withHoverColour(ACTION_HOVER).build();
        reportIcon = icon(REPORT).fontSized().withNormalColour(ACTION_NORMAL).withHoverColour(ACTION_HOVER).build();

        MouseAdapter mouseAdapter = new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                toggle(ToggleReadMeButton.this.isReadMeShowing);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                readMeIcon.setHover();
                reportIcon.setHover();

                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                readMeIcon.setNormal();
                reportIcon.setNormal();

                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        toggle(false);
    }

    public void toggle(boolean transitionToReport)
    {
        if (transitionToReport)
        {
            setIcon(readMeIcon);
            setToolTipText("Show README");

            showReport.run();
        }
        else
        {
            setIcon(reportIcon);
            setToolTipText("Show report");

            showReadMe.run();
        }

        isReadMeShowing = !transitionToReport;
    }
}
