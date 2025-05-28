package net.wenzuo.atom.web.config;

import net.wenzuo.atom.core.util.NanoIdUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author Catch
 * @since 2025-05-28
 */
@Aspect
@Component
public class ScheduledTraceIdAspect {

    private static final String TRACE_ID = "Trace-Id";

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void scheduleTask() {
    }

    @Around("scheduleTask()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            String traceId = NanoIdUtils.nanoId();
            MDC.put(TRACE_ID, traceId);
            return joinPoint.proceed();
        } finally {
            MDC.remove(TRACE_ID);
        }
    }

}
