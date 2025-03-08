package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.util.Clipboard;
import dev.lefley.reportlm.view.components.burp.BurpIcon;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.COPY;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.TICK;

public class CopyToClipboardButton extends JLabel
{
    private final Supplier<String> textSupplier;

    private final BurpIcon copyIcon;
    private final BurpIcon tickIcon;
    private final Timer onCopy;

    public CopyToClipboardButton(Supplier<String> textSupplier)
    {
        this.textSupplier = textSupplier;

        setFocusable(false);
        setHorizontalTextPosition(SwingConstants.LEADING);

        copyIcon = icon(COPY).fontSized().build();
        tickIcon = icon(TICK).fontSized().build();
        onCopy = new Timer(1000, e -> setReadyToCopy());

        setReadyToCopy();

        MouseAdapter mouseAdapter = new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                copyToClipboard();
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                copyIcon.setHover();
                tickIcon.setHover();

                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                copyIcon.setNormal();
                tickIcon.setNormal();

                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void setReadyToCopy()
    {
        setEnabled(true);
        setIcon(copyIcon);
        setToolTipText("Copy to clipboard");
        setText("");
    }

    private void copyToClipboard()
    {
        setEnabled(false);
        setIcon(tickIcon);
        setToolTipText("Copied!");
        setText("Copied!");

        Clipboard.copyToClipboard(textSupplier.get());

        if (!onCopy.isRunning())
        {
            onCopy.start();
        }
    }
}
