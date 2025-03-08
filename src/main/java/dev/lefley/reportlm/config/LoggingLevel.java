package dev.lefley.reportlm.config;

public enum LoggingLevel
{
    OFF(
            "Off",
            """
            No logging. Errors will still be sent to the error stream."""
    ),
    DEBUG(
            "Debug",
            """
            Debug events will be logged, such as report generation initiation and completion."""
    ),
    TRACE(
            "Trace",
            """
            Debug events will be logged, as well as all information sent to Burp AI."""
    );

    private final String displayName;
    private final String explanation;

    LoggingLevel(String displayName, String explanation)
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

    public boolean isAtLeast(LoggingLevel level)
    {
        return this.ordinal() >= level.ordinal();
    }
}
