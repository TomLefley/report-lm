package dev.lefley.reportlm;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.EnhancedCapability;
import burp.api.montoya.MontoyaApi;

import java.util.Set;

import static burp.api.montoya.EnhancedCapability.AI_FEATURES;

@SuppressWarnings("unused")
public class ReportLM implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi montoyaApi)
    {
        montoyaApi.logging().logToOutput("Hello, world!");
    }

    @Override
    public Set<EnhancedCapability> enhancedCapabilities()
    {
        return Set.of(AI_FEATURES);
    }
}
