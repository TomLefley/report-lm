package dev.lefley.reportlm.ai;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.ai.chat.Message;
import burp.api.montoya.ai.chat.PromptException;
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

public class ReportGenerator
{
    private static final String SYSTEM_MESSAGE =
            """
            You are a DAST vulnerability report writer.
            You will be given a series of web application vulnerabilities found by Burp Suite's DAST scanner.
            You will also be given a set of custom requirements from the client.
            
            Your task is to generate a vulnerability report in simple markdown.
            
            Unless otherwise specified, the report should:
            
                   - Be structured in a clear and readable format
                   - Be detailed and comprehensive
                   - Retain the original wording of the issues where possible
                   - Include all the information requested by the client
                   - Include any additional information you think is relevant
            
            Where issue evidence is available, each item can be referenced with a link to "file:./evidence/<issue_id>/request<n>" and "file:./evidence/<issue_id>/response<n>".
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

        return CompletableFuture.supplyAsync(() -> buildReport(customInstructions, issues), Threads::execute);
    }

    private Report buildReport(String customInstructions, List<AuditIssue> issues)
    {
        Report report = Report.createReport();
        report.saveEvidence(issues);

        String markdownReport = executePrompt(customInstructions, issues);

        String htmlReport = Markdown.renderMarkdownAsHtml(markdownReport);

        report.saveIndex(htmlReport);

        return report;
    }

    private String executePrompt(String customRequirements, List<AuditIssue> issues)
    {
        List<Message> messages = new ArrayList<>();

        Message systemMessage = Message.systemMessage(SYSTEM_MESSAGE);
        messages.add(systemMessage);

        Message userMessage = Message.userMessage("Custom requirements: " + customRequirements);
        messages.add(userMessage);

        issues.forEach(issue -> {
            Message issueMessage = Message.userMessage(issueMessage(issue));
            messages.add(issueMessage);
        });

        try
        {
            return ai.prompt().execute(messages.toArray(Message[]::new)).content();
        }
        catch (PromptException e)
        {
            Logger.logToError("Could not execute prompt", e);
            return "";
        }
    }

    private static String issueMessage(AuditIssue issue)
    {
        return """
               Issue type: %s
               Issue severity: %s
               Issue confidence: %s
               URL: %s
               Detail: %s
               Background: %s
               Remediation: %s
               Evidence item: %d
               """.formatted(
                issue.name(),
                issue.severity(),
                issue.confidence(),
                issue.baseUrl(),
                issue.detail(),
                issue.definition().background(),
                issue.definition().remediation(),
                issue.requestResponses().size()
        );
    }
}
