package dev.lefley.reportlm.model;

import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.scanner.audit.issues.AuditIssue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Report
{
    private final Path directory;

    public Report(Path directory)
    {
        this.directory = directory;
    }

    public static Report createReport()
    {
        try
        {
            return new Report(Files.createTempDirectory("report_lm"));
        }
        catch (IOException e)
        {
            throw new UncheckedIOException("Could not create temporary folder for report", e);
        }
    }

    public String readIndex()
    {
        try
        {
            return Files.readString(getIndex());
        }
        catch (IOException e)
        {
            throw new UncheckedIOException("Could not read index file", e);
        }
    }

    public Path getDirectory()
    {
        return directory;
    }

    public Path getIndex()
    {
        return directory.resolve("index.html");
    }

    public void saveIndex(String index)
    {
        try
        {
            Path indexFile = Files.createFile(directory.resolve("index.html"));
            Files.writeString(indexFile, index);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException("Could not write report to index file", e);
        }
    }

    public void saveEvidence(List<AuditIssue> auditIssues)
    {
        try
        {
            Path evidenceDirectory = Files.createDirectory(directory.resolve("evidence"));

            for (AuditIssue auditIssue : auditIssues)
            {
                Path issueDirectory = Files.createDirectory(evidenceDirectory.resolve(String.valueOf(auditIssue.hashCode())));

                List<HttpRequestResponse> requestResponses = auditIssue.requestResponses();
                for (int i = 0; i < requestResponses.size(); i++)
                {
                    HttpRequestResponse requestResponse = requestResponses.get(i);

                    Path requestFile = Files.createFile(issueDirectory.resolve("request" + (i + 1)));
                    Files.write(requestFile, requestResponse.request().toByteArray().getBytes());

                    Path responseFile = Files.createFile(issueDirectory.resolve("response" + (i + 1)));
                    Files.write(responseFile, requestResponse.response().toByteArray().getBytes());
                }
            }
        }
        catch (IOException e)
        {
            throw new UncheckedIOException("Could not save evidence to report folder", e);
        }
    }
}
