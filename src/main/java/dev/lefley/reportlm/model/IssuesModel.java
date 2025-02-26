package dev.lefley.reportlm.model;

import burp.api.montoya.scanner.audit.issues.AuditIssue;

import dev.lefley.reportlm.view.components.burp.BurpIcon;
import dev.lefley.reportlm.view.components.burp.BurpIconFile;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_FALSE_POSITIVE;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_HIGH_CERTAIN;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_HIGH_FIRM;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_HIGH_TENTATIVE;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_INFO_CERTAIN;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_INFO_FIRM;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_INFO_TENTATIVE;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_LOW_CERTAIN;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_LOW_FIRM;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_LOW_TENTATIVE;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_MEDIUM_CERTAIN;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_MEDIUM_FIRM;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SCAN_ISSUE_MEDIUM_TENTATIVE;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;

public class IssuesModel extends AbstractTableModel
{
    private enum Column
    {
        ISSUE_TYPE(0, "Issue type", JLabel.class, IssuesModel::issueType),
        PATH(1, "URL", String.class, AuditIssue::baseUrl),
        ;

        private final int index;

        private final String header;
        private final Class<?> columnClass;
        private final Function<AuditIssue, ?> valueExtractor;

        <T> Column(int index, String header, Class<T> columnClass, Function<AuditIssue, T> valueExtractor)
        {
            this.index = index;
            this.header = header;
            this.columnClass = columnClass;
            this.valueExtractor = valueExtractor;
        }

        static Column getColumnAtIndex(int index)
        {
            for (Column column : values())
            {
                if (column.index == index)
                {
                    return column;
                }
            }

            throw new IllegalArgumentException("No column found for index: " + index);
        }

    }

    private final List<AuditIssue> issues;
    private final SortedSet<Integer> selectedRows;

    public IssuesModel()
    {
        this.issues = new ArrayList<>();
        this.selectedRows = new TreeSet<>(Comparator.reverseOrder());
    }

    public List<AuditIssue> getIssues()
    {
        return unmodifiableList(issues);
    }

    public void addIssues(List<AuditIssue> issuesToAdd)
    {
        Set<AuditIssue> existingIssues = new HashSet<>(issues);

        int added = 0;
        for (AuditIssue auditIssue : issuesToAdd)
        {
            if (!existingIssues.contains(auditIssue))
            {
                issues.add(auditIssue);
                added++;
            }
        }

        if (added > 0)
        {
            fireTableRowsInserted(existingIssues.size(), existingIssues.size() + added - 1);
        }
    }

    public void setSelectedRows(int[] selectedRows)
    {
        this.selectedRows.clear();
        stream(selectedRows).forEach(this.selectedRows::add);
    }

    public void removeSelectedIssues()
    {
        for (Integer selectedRow : selectedRows)
        {
            issues.remove((int) selectedRow);
        }

        fireTableDataChanged();
    }

    @Override
    public int getRowCount()
    {
        return issues.size();
    }

    @Override
    public int getColumnCount()
    {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return Column.getColumnAtIndex(columnIndex).valueExtractor.apply(issues.get(rowIndex));
    }

    @Override
    public String getColumnName(int column)
    {
        return Column.getColumnAtIndex(column).header;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return Column.getColumnAtIndex(columnIndex).columnClass;
    }

    private static JLabel issueType(AuditIssue auditIssue)
    {
        JLabel label = new JLabel(auditIssue.name());
        label.setIcon(getIssueIcon(auditIssue));
        return label;
    }

    private static BurpIcon getIssueIcon(AuditIssue auditIssue)
    {
        return icon(getIssueIconFile(auditIssue)).fontSized().build();
    }

    @SuppressWarnings("DuplicatedCode")
    private static BurpIconFile getIssueIconFile(AuditIssue auditIssue)
    {
        return switch (auditIssue.severity())
        {
            case HIGH -> switch (auditIssue.confidence())
            {
                case CERTAIN -> SCAN_ISSUE_HIGH_CERTAIN;
                case FIRM -> SCAN_ISSUE_HIGH_FIRM;
                case TENTATIVE -> SCAN_ISSUE_HIGH_TENTATIVE;
            };
            case MEDIUM -> switch (auditIssue.confidence())
            {
                case CERTAIN -> SCAN_ISSUE_MEDIUM_CERTAIN;
                case FIRM -> SCAN_ISSUE_MEDIUM_FIRM;
                case TENTATIVE -> SCAN_ISSUE_MEDIUM_TENTATIVE;
            };
            case LOW -> switch (auditIssue.confidence())
            {
                case CERTAIN -> SCAN_ISSUE_LOW_CERTAIN;
                case FIRM -> SCAN_ISSUE_LOW_FIRM;
                case TENTATIVE -> SCAN_ISSUE_LOW_TENTATIVE;
            };
            case INFORMATION -> switch (auditIssue.confidence())
            {
                case CERTAIN -> SCAN_ISSUE_INFO_CERTAIN;
                case FIRM -> SCAN_ISSUE_INFO_FIRM;
                case TENTATIVE -> SCAN_ISSUE_INFO_TENTATIVE;
            };
            case FALSE_POSITIVE -> SCAN_ISSUE_FALSE_POSITIVE;
        };
    }
}
