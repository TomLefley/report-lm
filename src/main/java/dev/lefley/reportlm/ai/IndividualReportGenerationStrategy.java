package dev.lefley.reportlm.ai;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.ai.chat.Message;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import dev.lefley.reportlm.model.Report;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Markdown;
import dev.lefley.reportlm.util.Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

import static burp.api.montoya.ai.chat.Message.systemMessage;
import static burp.api.montoya.ai.chat.Message.userMessage;

public class IndividualReportGenerationStrategy implements ReportGenerationStrategy
{
    private static final String SYSTEM_MESSAGE =
            """
            You are a DAST vulnerability report writer.
            You will be given a web application vulnerability found by Burp Suite's DAST scanner.
            You will also be given a set of custom requirements from the client.
            
            Your task is to generate a vulnerability report in simple markdown.
            
            Unless otherwise specified, the report should:
            
                   - Be structured in a clear and readable format
                   - Be detailed and comprehensive
                   - Retain the original wording of the issue where possible
                   - Include all the information requested by the client
                   - Include any additional information you think is relevant
            
            """;

    private static final String EVIDENCE_SYSTEM_MESSAGE =
            """
            Where issue evidence is available, each item can be referenced with a link to "file:./evidence/<issue_id>/request<n>" and "file:./evidence/<issue_id>/response<n>".
            """;

    private final Ai ai;
    private final boolean includeEvidence;

    public IndividualReportGenerationStrategy(Ai ai, boolean includeEvidence)
    {
        this.ai = ai;
        this.includeEvidence = includeEvidence;
    }

    @Override
    public CompletableFuture<Report> generateReport(String customInstructions, List<AuditIssue> issues)
    {
        return CompletableFuture.supplyAsync(() -> initializeReport(issues), Threads::execute)
                .thenApply(report -> {
                    String reportIndex = generateReportIndex(customInstructions, issues);
                    report.saveIndex(reportIndex);

                    return report;
                });
    }

    private Report initializeReport(List<AuditIssue> auditIssues)
    {
        Report report = Report.createReport();

        if (includeEvidence)
        {
            report.saveEvidence(auditIssues);
        }

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

        StringJoiner report = new StringJoiner("\n");

        for (int i = 0; i < issues.size(); i++)
        {
            Logger.logToOutput("Generating report for issue %d".formatted(i));

            AuditIssue issue = issues.get(i);
            List<Message> messages = createMessages(customRequirements, issue);

            String markdownReport = ai.prompt().execute(messages.toArray(Message[]::new)).content();

            report.add(markdownReport);

            Logger.logToOutput("Report generated for issue %d".formatted(i));
        }

        Logger.logToOutput("Report generated!");

        return report.toString();
    }

    private List<Message> createMessages(String customInstructions, AuditIssue issue)
    {
        List<Message> messages = new ArrayList<>();
        messages.add(createSystemMessage(includeEvidence));
        if (customInstructions != null && !customInstructions.isEmpty())
        {
            messages.add(createCustomRequirementsMessage(customInstructions));
        }

        messages.add(createIssueMessage(issue, includeEvidence));

        return messages;
    }

    private static Message createSystemMessage(boolean includeEvidence)
    {
        return systemMessage(SYSTEM_MESSAGE + (includeEvidence ? EVIDENCE_SYSTEM_MESSAGE : ""));
    }

    private static Message createCustomRequirementsMessage(String customInstructions)
    {
        return userMessage("Custom requirements:\n\n" + customInstructions);
    }

    private static Message createIssueMessage(AuditIssue auditIssue, boolean includeEvidence)
    {
        String message = """
                           Issue ID: %s
                           Issue type: %s
                           Issue severity: %s
                           Issue confidence: %s
                           URL: %s
                           Detail: %s
                           Background: %s
                           Remediation: %s
                           """.formatted(
                auditIssue.hashCode(),
                auditIssue.name(),
                auditIssue.severity(),
                auditIssue.confidence(),
                auditIssue.baseUrl(),
                auditIssue.detail(),
                auditIssue.definition().background(),
                auditIssue.remediation()
        );

        if (includeEvidence)
        {
            message += "Evidence items: %d".formatted(auditIssue.requestResponses().size());
        }

        return userMessage(message);
    }
}
