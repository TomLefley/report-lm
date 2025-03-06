package dev.lefley.reportlm.ai;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.ai.chat.Message;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import dev.lefley.reportlm.model.Report;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.AiToggledEvent;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Markdown;
import dev.lefley.reportlm.util.Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static burp.api.montoya.ai.chat.Message.systemMessage;
import static burp.api.montoya.ai.chat.Message.userMessage;

public class ReportGenerator
{
    private static final String SYSTEM_MESSAGE = """
                                                 You are a DAST vulnerability report writer.
                                                 You will be given a series of wep application vulnerabilities found by Burp Suite's DAST scanner.
                                                 You will also be given a set of custom requirements from the client.
                                                 
                                                 Your task is to generate a vulnerability report in simple markdown.
                                                 
                                                 Unless otherwise specified, the report should:
                                                 
                                                        - Be structured in a clear and readable format
                                                        - Be detailed and comprehensive
                                                        - Retain the original wording of the issues where possible
                                                        - Include all the information requested by the client
                                                        - Include any additional information you think is relevant
                                                 
                                                 Where issue evidence is available, each item can be referenced with a link to "file:./evidence/<issue_hash>/request<n>" and "file:./evidence/<issue_hash>/response<n>".
                                                 """;

    private final Ai ai;

    public ReportGenerator(Ai ai)
    {
        this.ai = ai;

        AtomicBoolean aiEnabled = new AtomicBoolean(false);
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

        return CompletableFuture.supplyAsync(() -> initializeReport(issues), Threads::execute)
                .thenApply(report -> {
                    String reportIndex = generateReportIndex(customInstructions, issues);
                    report.saveIndex(reportIndex);

                    return report;
                });
    }

    private static Report initializeReport(List<AuditIssue> auditIssues)
    {
        Report report = Report.createReport();
        report.saveEvidence(auditIssues);

        return report;
    }

    private String generateReportIndex(String customInstructions, List<AuditIssue> issues)
    {
        String markdownReport = executePrompt(customInstructions, issues);

        return Markdown.renderMarkdownAsHtml(markdownReport);
    }

    private String executePrompt(String customRequirements, List<AuditIssue> issues)
    {
        Logger.logToOutput("Generating report from %d issues ...".formatted(issues.size()));

        List<Message> messages = createMessages(customRequirements, issues);

        String markdownReport = ai.prompt().execute(messages.toArray(Message[]::new)).content();

        Logger.logToOutput("Report generated!");
        return markdownReport;
    }

    private static List<Message> createMessages(String customInstructions, List<AuditIssue> issues)
    {
        List<Message> messages = new ArrayList<>();
        messages.add(systemMessage(SYSTEM_MESSAGE));
        if (customInstructions != null && !customInstructions.isEmpty())
        {
            messages.add(createCustomRequirementsMessage(customInstructions));
        }
        for (AuditIssue issue : issues)
        {
            messages.add(createIssueMessage(issue));
        }
        return messages;
    }

    private static Message createCustomRequirementsMessage(String customInstructions)
    {
        return userMessage("Custom requirements:\n\n" + customInstructions);
    }

    private static Message createIssueMessage(AuditIssue auditIssue)
    {
        return userMessage("""
                           Issue has: %s
                           Issue type: %s
                           Issue severity: %s
                           Issue confidence: %s
                           URL: %s
                           Detail: %s
                           Background: %s
                           Remediation: %s
                           Evidence items: %d
                           """.formatted(
                auditIssue.hashCode(),
                auditIssue.name(),
                auditIssue.severity(),
                auditIssue.confidence(),
                auditIssue.baseUrl(),
                auditIssue.detail(),
                auditIssue.definition().background(),
                auditIssue.remediation(),
                auditIssue.requestResponses().size()
        ));
    }
}
