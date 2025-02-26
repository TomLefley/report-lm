package dev.lefley.reportlm.util;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Arrays;

public class Clipboard
{
    public static void copyToClipboard(String str)
    {
        StringSelection stringSelection = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    private static class HtmlSelection implements Transferable
    {
        private static final DataFlavor[] DATA_FLAVOURS = {
                DataFlavor.stringFlavor,
                DataFlavor.allHtmlFlavor
        };

        private final String html;

        public HtmlSelection(String html)
        {
            this.html = html;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors()
        {
            return DATA_FLAVOURS;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return Arrays.asList(DATA_FLAVOURS).contains(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
        {
            if (!isDataFlavorSupported(flavor))
            {
                throw new UnsupportedFlavorException(flavor);
            }

            return html;
        }
    }
}
