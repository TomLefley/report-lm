package dev.lefley.reportlm.config;

import java.util.Optional;

public enum ReportGenerationMode
{
    COMBINED(
            0,
            "Combined",
            """
            All issues will be sent to Burp AI for report generation in a single request.
            This may result in a less comprehensive report - particularly as the number of issue to report increases - but is faster and uses fewer credits."""
    ),
    INDIVIDUAL(
            1,
            "Individual",
            """
            Each issue will be sent to Burp AI for report generation in a separate request, and the results will be combined into a single report.
            This may result in a more comprehensive report, but is slower and uses more credits."""
    ),
    BURP(
            2,
            "Burp",
            """
            Issue detail, background, and remediation will be extracted and sent to Burp AI individually for each issue. The results will be combined into a single report using Burp Suite's in-built report generation.
            This will result in the most comprehensive report, but is the slowest method and uses the most credits."""
    );

    private final int id;
    private final String displayName;
    private final String explanation;

    ReportGenerationMode(int id, String displayName, String explanation)
    {
        this.id = id;
        this.displayName = displayName;
        this.explanation = explanation;
    }

    public int getId()
    {
        return id;
    }

    public static Optional<ReportGenerationMode> getById(int id)
    {
        for (ReportGenerationMode mode : values())
        {
            if (mode.getId() == id)
            {
                return Optional.of(mode);
            }
        }

        return Optional.empty();
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getExplanation()
    {
        return explanation;
    }
}
