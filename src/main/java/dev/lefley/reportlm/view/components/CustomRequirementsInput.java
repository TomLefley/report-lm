package dev.lefley.reportlm.view.components;

import dev.lefley.reportlm.model.CustomRequirementsModel;

import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CustomRequirementsInput extends JTextArea implements CustomRequirementsModel
{
    private static final String PLACEHOLDER = """
                                              Enter any custom requirements for report generation here.
                                              This could include:
                                              
                                                  - The target audience for the report
                                                  - Issue details to focus on or exclude
                                                  - Desired formatting or layout""";

    private boolean showingPlaceholder;

    public CustomRequirementsInput()
    {
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setPlaceholder();

        setLineWrap(true);
        setWrapStyleWord(true);

        addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                if (showingPlaceholder)
                {
                    setText("");
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if (getText().isEmpty())
                {
                    setPlaceholder();
                }
            }
        });
    }

    @Override
    public String getRequirements()
    {
        return showingPlaceholder ? "" : getText();
    }

    private void setPlaceholder()
    {
        setText(PLACEHOLDER);
        showingPlaceholder = true;
    }

}