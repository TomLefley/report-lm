package dev.lefley.reportlm.controller;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.ai.chat.Message;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Threads;
import dev.lefley.reportlm.view.OutputView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static burp.api.montoya.ai.chat.Message.systemMessage;
import static burp.api.montoya.ai.chat.Message.userMessage;

public class ReportController
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

    private final Ai ai;
    private final OutputView outputView;

    public ReportController(Ai ai, OutputView outputView)
    {
        this.ai = ai;
        this.outputView = outputView;
    }

    public CompletableFuture<Void> generateReport(String customInstructions, List<AuditIssue> issues)
    {
        if (!isAiEnabled())
        {
            Logger.logToOutput("Cannot generate report. AI is not enabled!");
            return CompletableFuture.completedFuture(null);
        }

        if (issues.isEmpty())
        {
            Logger.logToOutput("Cannot generate report. No issues supplied!");
            return CompletableFuture.completedFuture(null);
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
                .thenApply(ReportController::parseReport)
                .thenAccept(outputView::setReport)
                .exceptionally(throwable -> {
                    Logger.logToError("Failed to generate report!", throwable);
                    return null;
                });
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
        Node document = Parser.builder().build().parse(report);
        return HtmlRenderer.builder().build().render(document);
    }

    public boolean isAiEnabled()
    {
        return ai.isEnabled();
    }
}
