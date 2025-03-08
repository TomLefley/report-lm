package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.config.Config;
import dev.lefley.reportlm.model.ConfigModel;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.ConfigChangedEvent;
import dev.lefley.reportlm.util.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

import static dev.lefley.reportlm.view.components.burp.BurpIcon.Builder.icon;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.CLOSE;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.SETTINGS;
import static dev.lefley.reportlm.view.components.burp.BurpIconFile.TICK;
import static javax.swing.BoxLayout.X_AXIS;

public class OpenConfigButton extends JButton
{
    private ConfigWindow configWindow;

    public OpenConfigButton(ConfigModel configModel)
    {
        super(icon(SETTINGS).fontSized().build());

        setToolTipText("Open settings");

        addActionListener(e -> openSettings(configModel.getConfig()));
    }

    private void openSettings(Config config)
    {
        try
        {
            if (configWindow == null)
            {
                configWindow = new ConfigWindow(config);
            }

            configWindow.setVisible(true);
            configWindow.toFront();
            configWindow.requestFocus();
        }
        catch (Exception e)
        {
            Logger.logToError(e);
        }
    }

    public class ConfigWindow extends JFrame
    {
        public ConfigWindow(Config config)
        {
            setTitle("ReportLM settings");

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            add(panel, BorderLayout.CENTER);

            ConfigPanel configPanel = new ConfigPanel(config);
            panel.add(configPanel, BorderLayout.CENTER);

            JPanel actions = new JPanel();
            actions.setLayout(new BoxLayout(actions, X_AXIS));

            actions.add(Box.createHorizontalGlue());

            JButton cancelButton = new IconHoverButton("Cancel", icon(CLOSE).fontSized().build());
            JButton saveButton = new PrimaryButton("Save", icon(TICK));

            saveButton.addActionListener(e -> saveConfig(configPanel.getConfig()));
            cancelButton.addActionListener(e -> dispose());

            actions.add(cancelButton);
            actions.add(Box.createHorizontalStrut(5));
            actions.add(saveButton);

            panel.add(actions, BorderLayout.SOUTH);

            pack();
            setSize(getWidth() + 500, getHeight());

            setLocationRelativeTo(SwingUtilities.getWindowAncestor(OpenConfigButton.this));
        }

        private void saveConfig(Config config)
        {
            Events.publish(new ConfigChangedEvent(config));
            dispose();
        }

        @Override
        public void dispose()
        {
            configWindow = null;
            super.dispose();
        }
    }
}
