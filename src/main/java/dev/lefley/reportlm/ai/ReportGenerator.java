package dev.lefley.reportlm.ai;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.ai.chat.Message;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import dev.lefley.reportlm.util.Events;
import dev.lefley.reportlm.util.Events.AiToggledEvent;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Threads;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
                                                 You are a vulnerability report writer.
                                                 You will be given a series of wep application vulnerabilities found by Burp Suite's DAST scanner.
                                                 You will also be given a set of requirements or custom instructions from the client.
                                                 
                                                 From these, generate a report that includes:
                                                 
                                                     - A summary of the vulnerabilities found
                                                     - Detailed descriptions of each vulnerability
                                                     - Recommendations for remediation
                                                     - Any additional information requested by the client
                                                 
                                                 Aim to use the original wording of the issues where possible.
                                                 
                                                 Where issue evidence should be embedded, indicate this with the following sequence: [EMBED ${issue_index}]
                                                 """;

    private static final Parser MARKDOWN_PARSER = Parser.builder().build();
    private static final HtmlRenderer HTML_RENDERER = HtmlRenderer.builder().escapeHtml(true).build();

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

    public CompletableFuture<String> generateReport(String customInstructions, List<AuditIssue> issues)
    {
        if (!isAiEnabled())
        {
            return CompletableFuture.failedFuture(new IllegalStateException("AI is not enabled!"));
        }

        if (issues.isEmpty())
        {
            return CompletableFuture.failedFuture(new IllegalStateException("No issues supplied!"));
        }

        List<Message> messages = new ArrayList<>();
        messages.add(systemMessage(SYSTEM_MESSAGE));
        if (customInstructions != null && !customInstructions.isEmpty())
        {
            messages.add(userMessage(customInstructions));
        }
        for (AuditIssue issue : issues)
        {
            messages.add(createIssueMessage(issue));
        }

        Logger.logToOutput("Generating report from %d issues ...".formatted(issues.size()));

        return CompletableFuture.supplyAsync(() -> getReport(messages), Threads::execute)
                .thenApply(ReportGenerator::parseReport);
    }

    private String getReport(List<Message> messages)
    {
        String markdownReport = ai.prompt().execute(messages.toArray(Message[]::new)).content();

        Logger.logToOutput("Report generated!");
        return markdownReport;
    }

    private static Message createIssueMessage(AuditIssue auditIssue)
    {
        return userMessage("""
                           Issue Type: %s
                           URL: %s
                           Detail: %s
                           Remediation: %s
                           """.formatted(
                auditIssue.name(),
                auditIssue.baseUrl(),
                auditIssue.detail(),
                auditIssue.remediation()
        ));
    }

    private static String parseReport(String report)
    {
        Node document = MARKDOWN_PARSER.parse(report);
        return HTML_RENDERER.render(document);
    }

    public boolean isAiEnabled()
    {
        return ai.isEnabled();
    }
}
