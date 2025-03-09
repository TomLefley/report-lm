package dev.lefley.reportlm.controller;

import dev.lefley.reportlm.config.Config;
import dev.lefley.reportlm.model.ConfigModel;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.ConfigChangedEvent;
import dev.lefley.reportlm.util.Logger;

public class ConfigController
{
    private final ConfigModel configModel;

    public ConfigController(ConfigModel configModel)
    {
        this.configModel = configModel;

        updateConfig(configModel.getConfig());
        Events.subscribe(ConfigChangedEvent.class, event -> updateConfig(event.config()));
    }

    private void updateConfig(Config config)
    {
        configModel.setConfig(config);

        Logger.setLevel(config.loggingLevel());
    }
}
