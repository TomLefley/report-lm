package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.util.Clipboard;
import dev.lefley.reportlm.view.components.burp.BurpIcon;

import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.util.function.Supplier;

import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.COPY;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.TICK;

public class CopyToClipboardButton extends SimpleIconButton
{
    private static final BurpIcon COPY_ICON = icon(COPY).fontSized().build();
    private static final BurpIcon TICK_ICON = icon(TICK).fontSized().build();

    private final Supplier<String> textSupplier;

    private final Timer onCopy;

    public CopyToClipboardButton(Supplier<String> textSupplier)
    {
        super(COPY_ICON);

        this.textSupplier = textSupplier;

        setHorizontalTextPosition(SwingConstants.LEADING);

        onCopy = new Timer(1000, e -> setReadyToCopy());

        setReadyToCopy();

        addClickListener(this::copyToClipboard);
    }

    private void setReadyToCopy()
    {
        setEnabled(true);
        setIcon(COPY_ICON);
        setToolTipText("Copy to clipboard");
        setText("");
    }

    private void copyToClipboard()
    {
        setEnabled(false);
        setIcon(TICK_ICON);
        setToolTipText("Copied!");
        setText("Copied!");

        Clipboard.copyToClipboard(textSupplier.get());

        if (!onCopy.isRunning())
        {
            onCopy.start();
        }
    }
}
