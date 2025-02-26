package dev.lefley.reportlm;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.EnhancedCapability;
import burp.api.montoya.MontoyaApi;

import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Threads;
import dev.lefley.reportlm.view.ReportTab;
import dev.lefley.reportlm.view.components.burp.BurpFont;

import java.util.Set;

import static burp.api.montoya.EnhancedCapability.AI_FEATURES;

@SuppressWarnings("unused")
public class ReportLM implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi montoyaApi)
    {
        Logger.initialize(montoyaApi.logging());
        BurpFont.initialize(montoyaApi.userInterface());
        Threads.initialize(montoyaApi.extension());

        ReportTab reportTab = new ReportTab(montoyaApi);
        montoyaApi.userInterface().registerSuiteTab("ReportLM", reportTab);
    }

    @Override
    public Set<EnhancedCapability> enhancedCapabilities()
    {
        return Set.of(AI_FEATURES);
    }
}
