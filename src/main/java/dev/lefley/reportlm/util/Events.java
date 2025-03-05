package dev.lefley.reportlm.util;

import burp.api.montoya.scanner.audit.issues.AuditIssue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class Events
{
    public interface Event
    {

    }

    public interface EventListener<E extends Event>
    {
        void onEvent(E event);
    }

    private static final Map<Class<?>, List<EventListener<?>>> subscriptions = new HashMap<>();

    public static <E extends Event> void subscribe(Class<E> eventType, EventListener<E> listener)
    {
        subscriptions.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public static <E extends Event> void publish(E event)
    {
        List<EventListener<?>> listeners = subscriptions.getOrDefault(event.getClass(), emptyList());
        for (EventListener<?> listener : listeners)
        {
            //noinspection unchecked
            ((EventListener<E>) listener).onEvent(event);
        }
    }

    public record AddIssuesEvent(List<AuditIssue> auditIssues) implements Event
    {
    }

    public record RemoveSelectedIssues() implements Event
    {
    }

    public record GenerateReportEvent() implements Event
    {
    }

    public record IssuesSelectedEvent(int[] selectedRows) implements Event
    {
    }

    public record AiToggledEvent(boolean aiEnabled) implements Event
    {
    }
}
