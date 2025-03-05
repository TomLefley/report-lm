package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.model.IssuesModel;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.IssuesSelectedEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;

import static java.awt.BorderLayout.CENTER;
import static java.util.Arrays.stream;

public class IssuesPanel extends JPanel
{
    public IssuesPanel(IssuesModel model)
    {
        super(new BorderLayout());

        JTable table = new JTable(model);

        JTableHeader header = table.getTableHeader();
        TableCellRenderer headerRenderer = header.getDefaultRenderer();
        if (headerRenderer instanceof JLabel labelRenderer)
        {
            labelRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        }

        table.setDefaultRenderer(JLabel.class, new LabelCellRenderer());
        table.getSelectionModel().addListSelectionListener(e -> {
            int[] selectedRows = table.getSelectedRows();
            int[] modelSelectedRows = stream(selectedRows).map(table::convertRowIndexToModel).toArray();

            Events.publish(new IssuesSelectedEvent(modelSelectedRows));
        });

        add(new JScrollPane(table), CENTER);
    }

    private static class LabelCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof JLabel label)
            {
                setText(label.getText());
                setIcon(label.getIcon());
            }

            return this;
        }
    }
}