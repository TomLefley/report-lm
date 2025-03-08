package dev.lefley.reportlm.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus.AI_DISABLED;
import static dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus.GENERATION_RUNNING;
import static dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus.ISSUES_NOT_POPULATED;
import static dev.lefley.reportlm.model.GenerateReportModel.GenerationStatus.READY;

public class GenerateReportModel
{
    public enum GenerationStatus
    {
        AI_DISABLED,
        ISSUES_NOT_POPULATED,
        GENERATION_RUNNING,
        READY
    }

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

    public GenerationStatus getGenerationStatus()
    {
        readLock.lock();
        try
        {
            if (generationRunning)
            {
                return GENERATION_RUNNING;
            }

            if (!aiEnabled)
            {
                return AI_DISABLED;
            }

            if (!issuesPopulated)
            {
                return ISSUES_NOT_POPULATED;
            }

            return READY;
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
