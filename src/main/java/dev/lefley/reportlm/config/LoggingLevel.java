package dev.lefley.reportlm.config;

import java.util.Optional;

public enum LoggingLevel
{
    OFF(
            0,
            "Off",
            """
            No logging. Errors will still be sent to the error stream."""
    ),
    DEBUG(
            1, "Debug",
            """
            Debug events will be logged, such as report generation initiation and completion."""
    ),
    TRACE(
            2,
            "Trace",
            """
            Debug events will be logged, as well as all information sent to Burp AI."""
    );

    private final int id;
    private final String displayName;
    private final String explanation;

    LoggingLevel(int id, String displayName, String explanation)
    {
        this.id = id;
        this.displayName = displayName;
        this.explanation = explanation;
    }

    public int getId()
    {
        return id;
    }

    public static Optional<LoggingLevel> getById(int id)
    {
        for (LoggingLevel level : values())
        {
            if (level.getId() == id)
            {
                return Optional.of(level);
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

    public boolean isAtLeast(LoggingLevel level)
    {
        return this.getId() >= level.getId();
    }
}
