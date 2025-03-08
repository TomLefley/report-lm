package dev.lefley.reportlm.config;

public enum ReportGenerationMode
{
    COMBINED(
            "Combined",
            """
            All issues will be sent to Burp AI for report generation in a single request.
            This may result in a less comprehensive report - particularly as the number of issue to report increases - but is faster and uses fewer credits."""
    ),
    INDIVIDUAL(
            "Individual",
            """
            Each issue will be sent to Burp AI for report generation in a separate request, and the results will be combined into a single report.
            This may result in a more comprehensive report, but is slower and uses more credits."""
    ),
    BURP(
            "Burp",
            """
            Issue detail, background, and remediation will be extracted and sent to Burp AI individually for each issue. The results will be combined into a single report using Burp Suite's in-built report generation.
            This will result in the most comprehensive report, but is the slowest method and uses the most credits."""
    );

    private final String displayName;
    private final String explanation;

    ReportGenerationMode(String displayName, String explanation)
    {
        this.displayName = displayName;
        this.explanation = explanation;
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
