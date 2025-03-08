package dev.lefley.reportlm.util;

import burp.api.montoya.logging.Logging;
import dev.lefley.reportlm.config.LoggingLevel;

import static dev.lefley.reportlm.config.LoggingLevel.DEBUG;
import static dev.lefley.reportlm.config.LoggingLevel.OFF;

public class Logger
{
    private static volatile LoggingLevel loggingLevel;
    private static Logging logging;

    public static void initialize(Logging logging)
    {
        loggingLevel = OFF;
        Logger.logging = logging;
    }

    public static void setLevel(LoggingLevel loggingLevel)
    {
        Logger.loggingLevel = loggingLevel;
    }

    public static LoggingLevel getLevel()
    {
        return loggingLevel;
    }

    public static void logToError(Throwable cause)
    {
        logging.logToError(cause);
    }

    public static void logToError(String message)
    {
        logging.logToError(message);
    }

    public static void logToError(String message, Throwable cause)
    {
        logging.logToError(message, cause);
    }

    public static void logToOutput(String message)
    {
        logToOutput(message, DEBUG);
    }

    public static void logToOutput(String message, LoggingLevel atLevel)
    {
        if (loggingLevel.isAtLeast(atLevel))
        {
            logging.logToOutput(message);
        }
    }
}
