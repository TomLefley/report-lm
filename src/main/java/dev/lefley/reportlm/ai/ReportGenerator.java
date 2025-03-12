package dev.lefley.reportlm.ai;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.scanner.Scanner;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import dev.lefley.reportlm.config.Config;
import dev.lefley.reportlm.model.ConfigModel;
import dev.lefley.reportlm.model.Report;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.AiToggledEvent;
import dev.lefley.reportlm.util.Threads;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReportGenerator
{
    private final Ai ai;
    private final Scanner scanner;
    private final ConfigModel configModel;

    public ReportGenerator(Ai ai, Scanner scanner, ConfigModel configModel)
    {
        this.ai = ai;
        this.scanner = scanner;
        this.configModel = configModel;

        AtomicBoolean aiEnabled = new AtomicBoolean(ai.isEnabled());
        Events.publish(new AiToggledEvent(ai.isEnabled()));

        Threads.scheduleAtFixedRate(
                () -> {
                    boolean enabled = ai.isEnabled();
                    if (aiEnabled.compareAndSet(!enabled, enabled))
                    {
                        Events.publish(new AiToggledEvent(enabled));
                    }
                },
                0,
                1,
                TimeUnit.SECONDS
        );
    }

    public CompletableFuture<Report> generateReport(String customInstructions, List<AuditIssue> issues)
    {
        if (!ai.isEnabled())
        {
            return CompletableFuture.failedFuture(new IllegalStateException("AI is not enabled!"));
        }

        if (issues.isEmpty())
        {
            return CompletableFuture.failedFuture(new IllegalStateException("No issues supplied!"));
        }

        return getReportGenerationStrategy(configModel.getConfig()).generateReport(customInstructions, issues);
    }

    private ReportGenerationStrategy getReportGenerationStrategy(Config config)
    {
        return switch (config.reportGenerationMode())
        {
            case COMBINED -> new CombinedReportGenerationStrategy(ai, config.includeEvidence());
            case INDIVIDUAL -> new IndividualReportGenerationStrategy(ai, config.includeEvidence());
            case BURP -> new BurpReportGenerationStrategy(ai, scanner);
        };
    }
}
