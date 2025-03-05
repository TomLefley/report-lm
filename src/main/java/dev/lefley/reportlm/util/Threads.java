package dev.lefley.reportlm.util;

import burp.api.montoya.extension.Extension;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Threads
{
    private static ExecutorService executor;
    private static ScheduledExecutorService scheduledExecutor;

    public static void initialize(Extension extension)
    {
        executor = Executors.newVirtualThreadPerTaskExecutor();
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        extension.registerUnloadingHandler(() -> {
            executor.shutdownNow();
            scheduledExecutor.shutdownNow();
        });
    }

    public static <T> Future<T> submit(Callable<T> task)
    {
        return executor.submit(task);
    }

    public static <T> Future<T> submit(Runnable task, T result)
    {
        return executor.submit(task, result);
    }

    public static Future<?> submit(Runnable task)
    {
        return executor.submit(task);
    }

    public static <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
    {
        return executor.invokeAll(tasks);
    }

    public static <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
    {
        return executor.invokeAll(tasks, timeout, unit);
    }

    public static <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
    {
        return executor.invokeAny(tasks);
    }

    public static <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
    {
        return executor.invokeAny(tasks, timeout, unit);
    }

    public static void execute(Runnable command)
    {
        executor.execute(command);
    }

    public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
    {
        return scheduledExecutor.schedule(command, delay, unit);
    }

    public static <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
    {
        return scheduledExecutor.schedule(callable, delay, unit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
    {
        return scheduledExecutor.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
    {
        return scheduledExecutor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
}
