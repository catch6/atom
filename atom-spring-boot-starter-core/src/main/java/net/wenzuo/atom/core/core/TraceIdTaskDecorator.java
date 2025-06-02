package net.wenzuo.atom.core.core;

import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.NanoIdUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catch
 * @since 2025-06-02
 */
@Slf4j
@Component
public class TraceIdTaskDecorator implements TaskDecorator {

    @NonNull
    @Override
    public Runnable decorate(@NonNull Runnable runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            setTraceIdIfAbsent(context);
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    private void setTraceIdIfAbsent(Map<String, String> context) {
        Map<String, String> ctx;
        if (context == null) {
            ctx = new HashMap<>();
            ctx.put(CoreConstants.TRACE_ID, NanoIdUtils.nanoId());
            MDC.setContextMap(ctx);
            return;
        }
        if (context.containsKey(CoreConstants.TRACE_ID)) {
            MDC.setContextMap(context);
            return;
        }
        ctx = new HashMap<>(context);
        ctx.put(CoreConstants.TRACE_ID, NanoIdUtils.nanoId());
        MDC.setContextMap(ctx);
    }

}
