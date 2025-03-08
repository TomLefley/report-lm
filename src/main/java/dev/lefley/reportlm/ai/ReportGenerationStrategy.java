package dev.lefley.reportlm.ai;

import burp.api.montoya.scanner.audit.issues.AuditIssue;
import dev.lefley.reportlm.model.Report;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ReportGenerationStrategy
{
    CompletableFuture<Report> generateReport(String customInstructions, List<AuditIssue> issues);
}
