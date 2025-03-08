package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.config.Config;
import dev.lefley.reportlm.config.LoggingLevel;
import dev.lefley.reportlm.config.ReportGenerationMode;
import dev.lefley.reportlm.view.components.burp.BurpFont;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Objects;

import static dev.lefley.reportlm.config.LoggingLevel.OFF;
import static dev.lefley.reportlm.config.ReportGenerationMode.BURP;
import static dev.lefley.reportlm.config.ReportGenerationMode.COMBINED;

public class ConfigPanel extends JPanel
{
    private final Description generationModeDescription;
    private final JComboBox<ReportGenerationMode> generationModeSelection;
    private final JCheckBox includeEvidenceSelection;
    private final Description includeEvidenceDescription;
    private final JComboBox<LoggingLevel> loggingLevelSelection;
    private final Description loggingLevelDescription;
    private final JLabel includeEvidenceLabel;

    public ConfigPanel(Config config)
    {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new GridBagLayout());

        GridBagConstraints constraints;

        constraints = buildDefaultConstraints(0, 0);
        constraints.gridwidth = 2;

        JLabel reportSectionHeading = new JLabel("Report generation");
        reportSectionHeading.setFont(BurpFont.title());
        add(reportSectionHeading, constraints);

        constraints = buildDefaultConstraints(0, 1);
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 10, 5);
        add(new Description("These settings control how the report is generated."), constraints);

        JLabel generationModeLabel = new JLabel("Mode:");
        generationModeLabel.setFont(BurpFont.bold());
        add(generationModeLabel, buildDefaultConstraints(0, 2));

        generationModeSelection = buildReportGenerationModeSelection();
        add(generationModeSelection, buildDefaultConstraints(1, 2));

        constraints = buildDefaultConstraints(0, 3);
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        generationModeDescription = new Description("");
        add(generationModeDescription, constraints);

        includeEvidenceLabel = new JLabel("Include evidence:");
        includeEvidenceLabel.setFont(BurpFont.bold());
        add(includeEvidenceLabel, buildDefaultConstraints(0, 4));

        includeEvidenceSelection = new JCheckBox();
        includeEvidenceSelection.setSelected(true);
        includeEvidenceSelection.addItemListener(e -> setIncludeEvidenceSelection(((boolean) e.getItem())));
        add(includeEvidenceSelection, buildDefaultConstraints(1, 4));

        constraints = buildDefaultConstraints(0, 5);
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 10, 5);

        includeEvidenceDescription = new Description("");
        add(includeEvidenceDescription, constraints);

        setSelectedGenerationMode(config.reportGenerationMode());
        setIncludeEvidenceSelection(config.includeEvidence());

        constraints = buildDefaultConstraints(0, 6);
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 10, 5);
        add(new JSeparator(), constraints);

        constraints = buildDefaultConstraints(0, 7);
        constraints.gridwidth = 2;

        JLabel loggingSectionHeading = new JLabel("Logging");
        loggingSectionHeading.setFont(BurpFont.title());
        add(loggingSectionHeading, constraints);

        constraints = buildDefaultConstraints(0, 8);
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 10, 5);
        add(new Description("These settings control how the amount of logging sent to the Extensions output stream."), constraints);

        JLabel loggingLevelLabel = new JLabel("Level:");
        loggingLevelLabel.setFont(BurpFont.bold());
        add(loggingLevelLabel, buildDefaultConstraints(0, 9));

        loggingLevelSelection = buildLoggingLevelSelection();
        add(loggingLevelSelection, buildDefaultConstraints(1, 9));

        constraints = buildDefaultConstraints(0, 10);
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 10, 5);
        loggingLevelDescription = new Description("");
        add(loggingLevelDescription, constraints);

        setSelectedLoggingLevel(config.loggingLevel());

        constraints = buildDefaultConstraints(0, 11);
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(new JSeparator(), constraints);

        constraints = buildDefaultConstraints(0, 12);
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(new JPanel(), constraints);
    }

    private static GridBagConstraints buildDefaultConstraints(int gridx, int gridy)
    {
        GridBagConstraints constraints;
        constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(5, 5, 5, 5);
        return constraints;
    }

    private JComboBox<ReportGenerationMode> buildReportGenerationModeSelection()
    {
        JComboBox<ReportGenerationMode> generationModeSelection = new JComboBox<>(ReportGenerationMode.values());
        generationModeSelection.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                return super.getListCellRendererComponent(list, ((ReportGenerationMode) value).getDisplayName(), index, isSelected, cellHasFocus);
            }
        });
        generationModeSelection.addItemListener(e -> setSelectedGenerationMode((ReportGenerationMode) e.getItem()));
        return generationModeSelection;
    }

    private void setSelectedGenerationMode(ReportGenerationMode generationMode)
    {
        generationModeSelection.setSelectedItem(generationMode);
        generationModeDescription.setText(generationMode.getExplanation());

        if (generationMode == BURP)
        {
            includeEvidenceLabel.setEnabled(false);

            includeEvidenceSelection.setEnabled(false);
            includeEvidenceSelection.setSelected(true);

            includeEvidenceDescription.setEnabled(false);
            includeEvidenceDescription.setText(
                    """
                    Inline evidence is always included when generating a Burp report.
                    Note: as it is not necessary to rewrite evidence it is not passed to Burp AI."""
            );
        }
        else
        {
            includeEvidenceLabel.setEnabled(true);

            includeEvidenceSelection.setEnabled(true);
            includeEvidenceDescription.setEnabled(true);

            setIncludeEvidenceSelection(includeEvidenceSelection.isSelected());
        }
    }

    private void setIncludeEvidenceSelection(boolean includeEvidence)
    {
        includeEvidenceSelection.setSelected(includeEvidence);
        if (includeEvidence)
        {
            includeEvidenceDescription.setText(
                    """
                    Links to evidence will be included in the report.
                    Note: all evidence is attached to the report after generation and will not be passed to Burp AI."""
            );
        }
        else
        {
            includeEvidenceDescription.setText(
                    """
                    Evidence will not be included in the report."""
            );
        }
    }

    private JComboBox<LoggingLevel> buildLoggingLevelSelection()
    {
        JComboBox<LoggingLevel> loggingLevelSelection = new JComboBox<>(LoggingLevel.values());
        loggingLevelSelection.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                return super.getListCellRendererComponent(list, ((LoggingLevel) value).getDisplayName(), index, isSelected, cellHasFocus);
            }
        });
        loggingLevelSelection.addItemListener(e -> setSelectedLoggingLevel((LoggingLevel) e.getItem()));
        return loggingLevelSelection;
    }

    private void setSelectedLoggingLevel(LoggingLevel loggingLevel)
    {
        loggingLevelSelection.setSelectedItem(loggingLevel);
        loggingLevelDescription.setText(loggingLevel.getExplanation());
    }

    public Config getConfig()
    {
        return new Config(
                (ReportGenerationMode) Objects.requireNonNullElse(generationModeSelection.getSelectedItem(), COMBINED),
                includeEvidenceSelection.isSelected(),
                (LoggingLevel) Objects.requireNonNullElse(loggingLevelSelection.getSelectedItem(), OFF)
        );
    }
}
