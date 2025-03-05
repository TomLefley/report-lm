package dev.lefley.reportlm.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GenerateReportModel
{
    private final Lock readLock;
    private final Lock writeLock;

    private volatile boolean aiEnabled;
    private volatile boolean issuesPopulated;
    private volatile boolean generationRunning;

    public GenerateReportModel()
    {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();

        this.aiEnabled = false;
        this.issuesPopulated = false;
        this.generationRunning = false;
    }

    public boolean canGenerateReport()
    {
        readLock.lock();
        try
        {
            return aiEnabled && issuesPopulated && !generationRunning;
        }
        finally
        {
            readLock.unlock();
        }
    }

    public void setAiEnabled(boolean aiEnabled)
    {
        writeLock.lock();
        try
        {
            this.aiEnabled = aiEnabled;
        }
        finally
        {
            writeLock.unlock();
        }
    }

    public void setIssuesPopulated(boolean issuesPopulated)
    {
        writeLock.lock();
        try
        {
            this.issuesPopulated = issuesPopulated;
        }
        finally
        {
            writeLock.unlock();
        }
    }

    public void setGenerationRunning(boolean generationRunning)
    {
        writeLock.lock();
        try
        {
            this.generationRunning = generationRunning;
        }
        finally
        {
            writeLock.unlock();
        }
    }
}
