package dev.lefley.reportlm.util;

import burp.api.montoya.logging.Logging;

public class Logger
{
    private static Logging logging;

    public static void initialize(Logging logging)
    {
        Logger.logging = logging;
    }

    public static void logToError(Throwable cause)
    {
        logging.logToError(cause);
    }

    public static void logToError(String message)
    {
        logging.logToError(message);
    }

    public static void raiseInfoEvent(String message)
    {
        logging.raiseInfoEvent(message);
    }

    public static void logToOutput(String message)
    {
        logging.logToOutput(message);
    }

    public static void raiseCriticalEvent(String message)
    {
        logging.raiseCriticalEvent(message);
    }

    public static void logToError(String message, Throwable cause)
    {
        logging.logToError(message, cause);
    }

    public static void raiseDebugEvent(String message)
    {
        logging.raiseDebugEvent(message);
    }

    public static void raiseErrorEvent(String message)
    {
        logging.raiseErrorEvent(message);
    }
}
