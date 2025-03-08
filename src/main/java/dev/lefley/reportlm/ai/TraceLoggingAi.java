package dev.lefley.reportlm.ai;

import burp.api.montoya.ai.Ai;
import burp.api.montoya.ai.chat.Message;
import burp.api.montoya.ai.chat.Prompt;
import burp.api.montoya.ai.chat.PromptException;
import burp.api.montoya.ai.chat.PromptOptions;
import burp.api.montoya.ai.chat.PromptResponse;
import dev.lefley.reportlm.util.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static dev.lefley.reportlm.config.LoggingLevel.TRACE;
import static java.util.Arrays.stream;

public class TraceLoggingAi implements Ai
{
    private final Ai ai;

    public TraceLoggingAi(Ai ai)
    {
        this.ai = ai;
    }

    @Override
    public boolean isEnabled()
    {
        return ai.isEnabled();
    }

    @Override
    public Prompt prompt()
    {
        return new TraceLoggingPrompt(ai.prompt());
    }

    private static class TraceLoggingPrompt implements Prompt
    {
        private final Prompt prompt;

        public TraceLoggingPrompt(Prompt prompt)
        {
            this.prompt = prompt;
        }

        @Override
        public PromptResponse execute(String... messages) throws PromptException
        {
            logMessages(messages);

            return prompt.execute(messages);
        }

        @Override
        public PromptResponse execute(PromptOptions options, String... messages) throws PromptException
        {
            logMessages(messages);

            return prompt.execute(options, messages);
        }

        @Override
        public PromptResponse execute(Message... messages) throws PromptException
        {
            logMessages(messages);

            return prompt.execute(messages);
        }

        @Override
        public PromptResponse execute(PromptOptions options, Message... messages) throws PromptException
        {
            logMessages(messages);

            return prompt.execute(options, messages);
        }

        private static void logMessages(Message[] messages)
        {
            logMessages(
                    stream(messages)
                            .map(TraceLoggingPrompt::extractMessageContent)
                            .toArray(String[]::new)
            );
        }

        // WHY DID WE NOT GIVE MESSAGE A SENSIBLE TOSTRING?? JFC
        private static String extractMessageContent(Message message)
        {
            StringBuilder content = new StringBuilder();

            for (Method method : message.getClass().getMethods())
            {
                try
                {
                    if (!Modifier.isStatic(method.getModifiers())
                        && !"toString".equals(method.getName())
                        && method.getParameterCount() == 0
                        && String.class.equals(method.getReturnType())
                    )
                    {
                        Object value = method.invoke(message);

                        content.append(value);
                        content.append("\n");
                    }
                }
                catch (Exception exception)
                {
                    Logger.logToError("Could not extract message content for trace logging", exception);
                }
            }

            return content.toString();
        }


        private static void logMessages(String[] messages)
        {
            Logger.logToOutput(
                    """
                    
                    ========================================================
                    Executing prompt with messages:\s
                    
                    %s
                    ========================================================
                    """
                            .formatted(
                                    String.join(
                                            "\n--------------------------------------------------------\n",
                                            messages
                                    )
                            ),
                    TRACE
            );
        }
    }
}
