package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.model.IssuesModel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

import static dev.lefley.reportlm.view.components.burp.BurpColor.ACTION_HOVER;
import static dev.lefley.reportlm.view.components.burp.BurpColor.ACTION_NORMAL;
import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.DELETE;
import static java.util.Arrays.stream;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class IssuesPanel extends JPanel
{
    public IssuesPanel(IssuesModel model, JButton... additionalToolbarButtons)
    {
        setLayout(new BoxLayout(this, Y_AXIS));

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, X_AXIS));

        JButton removeIssueButton = new JButton(icon(DELETE).fontSized().withNormalColour(ACTION_NORMAL).withHoverColour(ACTION_HOVER).build());
        removeIssueButton.setToolTipText("Remove selected issues");
        removeIssueButton.addActionListener(e -> model.removeSelectedIssues());
        removeIssueButton.setEnabled(false);

        toolbar.add(removeIssueButton);

        toolbar.add(Box.createHorizontalGlue());

        for (JButton toolbarButton : additionalToolbarButtons)
        {
            toolbarButton.setEnabled(false);

            toolbar.add(toolbarButton);
        }

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
            model.setSelectedRows(stream(selectedRows).map(table::convertRowIndexToModel).toArray());

            removeIssueButton.setEnabled(selectedRows.length > 0);
        });

        model.addTableModelListener(e -> {
            boolean enableToolbar = model.getRowCount() > 0;

            for (JButton toolbarButton : additionalToolbarButtons)
            {
                toolbarButton.setEnabled(enableToolbar);
            }
        });

        add(toolbar);
        add(Box.createVerticalStrut(10));
        add(new JScrollPane(table));
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