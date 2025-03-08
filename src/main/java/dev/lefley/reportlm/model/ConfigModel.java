package dev.lefley.reportlm.model;

import burp.api.montoya.persistence.Preferences;
import dev.lefley.reportlm.config.Config;
import dev.lefley.reportlm.config.LoggingLevel;
import dev.lefley.reportlm.config.ReportGenerationMode;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConfigModel
{
    private static final String REPORTLM_GENERATION_MODE_CONFIG_KEY = "reportlm.generationMode";
    private static final String REPORTLM_INCLUDE_EVIDENCE_CONFIG_KEY = "reportlm.includeEvidence";
    private static final String REPORTLM_LOGGING_LEVEL_CONFIG_KEY = "reportlm.loggingLevel";

    private final Preferences preferences;

    private volatile Config config;

    public ConfigModel(Preferences preferences)
    {
        this.preferences = preferences;

        this.config = loadConfig();
    }

    public synchronized Config getConfig()
    {
        return config;
    }

    public synchronized void setConfig(Config config)
    {
        this.config = config;

        persistConfig(config);
    }

    private void persistConfig(Config config)
    {
        preferences.setInteger(REPORTLM_GENERATION_MODE_CONFIG_KEY, config.reportGenerationMode().getId());
        preferences.setBoolean(REPORTLM_INCLUDE_EVIDENCE_CONFIG_KEY, config.includeEvidence());
        preferences.setInteger(REPORTLM_LOGGING_LEVEL_CONFIG_KEY, config.loggingLevel().getId());
    }

    private Config loadConfig()
    {
        Config defaultConfig = Config.defaultConfig();
        ReportGenerationMode defaultReportGenerationMode = defaultConfig.reportGenerationMode();
        boolean defaultIncludeEvidence = defaultConfig.includeEvidence();
        LoggingLevel defaultLoggingLevel = defaultConfig.loggingLevel();

        ReportGenerationMode generationMode = loadConfigValue(Preferences::getInteger, ReportGenerationMode::getById, REPORTLM_GENERATION_MODE_CONFIG_KEY, defaultReportGenerationMode);
        boolean includeEvidence = loadConfigValue(Preferences::getBoolean, Optional::ofNullable, REPORTLM_INCLUDE_EVIDENCE_CONFIG_KEY, defaultIncludeEvidence);
        LoggingLevel loggingLevel = loadConfigValue(Preferences::getInteger, LoggingLevel::getById, REPORTLM_LOGGING_LEVEL_CONFIG_KEY, defaultLoggingLevel);

        return new Config(
                generationMode,
                includeEvidence,
                loggingLevel
        );
    }

    private <C, T> T loadConfigValue(BiFunction<Preferences, String, C> loadFunction, Function<C, Optional<T>> deserializer, String key, T defaultValue)
    {
        return Optional.ofNullable(loadFunction.apply(preferences, key))
                .flatMap(deserializer)
                .orElse(defaultValue);
    }
}
