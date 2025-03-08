package dev.lefley.reportlm.config;

import dev.lefley.reportlm.util.Logger;

public record Config(
        ReportGenerationMode reportGenerationMode,
        boolean includeEvidence,
        LoggingLevel loggingLevel
)
{
    public static Config defaultConfig()
    {
        return new Config(
                ReportGenerationMode.COMBINED,
                true,
                Logger.getLevel()
        );
    }
}
