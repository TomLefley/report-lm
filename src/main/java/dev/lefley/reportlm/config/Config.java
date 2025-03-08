package dev.lefley.reportlm.config;

public record Config(
        ReportGenerationMode reportGenerationMode,
        boolean includeEvidence,
        LoggingLevel loggingLevel
)
{
}
