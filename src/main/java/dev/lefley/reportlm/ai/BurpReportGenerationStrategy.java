package dev.lefley.reportlm.ai;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.ai.chat.Message;
import burp.api.montoya.scanner.ReportFormat;
import burp.api.montoya.scanner.Scanner;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import dev.lefley.reportlm.model.Report;
import dev.lefley.reportlm.util.Logger;
import dev.lefley.reportlm.util.Markdown;
import dev.lefley.reportlm.util.Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static burp.api.montoya.ai.chat.Message.systemMessage;
import static burp.api.montoya.ai.chat.Message.userMessage;

public class BurpReportGenerationStrategy implements ReportGenerationStrategy
{
    private static final String SYSTEM_MESSAGE =
            """
            You are a DAST vulnerability report writer.
            You will be given a web application vulnerability found by Burp Suite's DAST scanner.
            You will also be given a set of custom requirements from the client.
            
            Your task is to generate a specific section at a time for a vulnerability report, in simple markdown.
            
            Unless otherwise specified, the generated section should:
            
                   - Not include a section header, just the content
                   - Be structured in a clear and readable format
                   - Be detailed and comprehensive
                   - Retain the original wording of the issues where possible
                   - Only include the information requested by the client if it is relevant to the current section
                   - Include any additional information you think is relevant to the current section
            
            """;

    private final Ai ai;
    private final Scanner scanner;

    public BurpReportGenerationStrategy(Ai ai, Scanner scanner)
    {
        this.ai = ai;
        this.scanner = scanner;
    }

    @Override
    public CompletableFuture<Report> generateReport(String customRequirements, List<AuditIssue> issues)
    {
        return CompletableFuture.supplyAsync(Report::createReport, Threads::execute)
                .thenApply(report -> {
                    List<AuditIssue> generatedIssues = executePrompt(customRequirements, issues);

                    scanner.generateReport(
                            generatedIssues,
                            ReportFormat.HTML,
                            report.getIndex()
                    );

                    return report;
                });
    }

    private List<AuditIssue> executePrompt(String customRequirements, List<AuditIssue> issues)
    {
        Logger.logToOutput("Generating report from %d issues ...".formatted(issues.size()));

        List<AuditIssue> generatedIssues = new ArrayList<>();

        for (int i = 0; i < issues.size(); i++)
        {
            Logger.logToOutput("Generating report for issue %d".formatted(i));

            AuditIssue issue = issues.get(i);

            String detail = generateSection(customRequirements, issue, "Detail");
            String remediation = generateSection(customRequirements, issue, "Remediation");
            String background = generateSection(customRequirements, issue, "Background");
            String remediationBackground = generateSection(customRequirements, issue, "Background remediation");

            AuditIssue generatedIssue = AuditIssue.auditIssue(
                    issue.name(),
                    detail,
                    remediation,
                    issue.baseUrl(),
                    issue.severity(),
                    issue.confidence(),
                    background,
                    remediationBackground,
                    issue.definition().typicalSeverity(),
                    issue.requestResponses()
            );

            Logger.logToOutput("Report generated for issue %d".formatted(i));

            generatedIssues.add(generatedIssue);
        }

        Logger.logToOutput("Report generated!");

        return generatedIssues;
    }

    private String generateSection(String customRequirements, AuditIssue issue, String section)
    {
        Logger.logToOutput("Generating section %s".formatted(section));

        List<Message> messages = createMessages(customRequirements, issue, section);
        String generatedSection = ai.prompt().execute(messages.toArray(Message[]::new)).content();

        Logger.logToOutput("Section generated");

        return Markdown.renderMarkdownAsHtml(generatedSection);
    }

    private List<Message> createMessages(String customRequirements, AuditIssue issue, String section)
    {
        List<Message> messages = new ArrayList<>();
        messages.add(createSystemMessage());
        if (customRequirements != null && !customRequirements.isEmpty())
        {
            messages.add(createCustomRequirementsMessage(customRequirements));
        }

        messages.add(createIssueMessage(issue));
        messages.add(userMessage("Generate the %s section for the issue above.".formatted(section)));

        return messages;
    }

    private static Message createSystemMessage()
    {
        return systemMessage(SYSTEM_MESSAGE);
    }

    private static Message createCustomRequirementsMessage(String customInstructions)
    {
        return userMessage("Custom requirements:\n\n" + customInstructions);
    }

    private static Message createIssueMessage(AuditIssue auditIssue)
    {
        String message = """
                           Issue ID: %s
                           Issue type: %s
                           Issue severity: %s
                           Issue confidence: %s
                           URL: %s
                           Detail: %s
                           Remediation: %s
                           Background: %s
                           Background remediation: %s
                           """.formatted(
                auditIssue.hashCode(),
                auditIssue.name(),
                auditIssue.severity(),
                auditIssue.confidence(),
                auditIssue.baseUrl(),
                auditIssue.detail(),
                auditIssue.remediation(),
                auditIssue.definition().background(),
                auditIssue.definition().remediation()
        );

        return userMessage(message);
    }
}
