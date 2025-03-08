package dev.lefley.reportlm.model;

import dev.lefley.reportlm.config.Config;

public class ConfigModel
{
    private volatile Config config;

    public ConfigModel()
    {
        this.config = Config.defaultConfig();
    }

    public synchronized Config getConfig()
    {
        return config;
    }

    public synchronized void setConfig(Config config)
    {
        this.config = config;
    }
}
