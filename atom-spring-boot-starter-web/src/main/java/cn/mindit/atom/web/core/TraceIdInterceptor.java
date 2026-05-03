package cn.mindit.atom.web.core;

import cn.mindit.atom.core.core.CoreConstants;
import cn.mindit.atom.core.util.NanoIdUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Catch
 * @since 2025-06-01
 */
@Component
public class TraceIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String traceId = request.getHeader(CoreConstants.TRACE_ID);
        if (traceId == null || traceId.isEmpty()) {
            traceId = NanoIdUtils.nanoId();
        }
        MDC.put(CoreConstants.TRACE_ID, traceId);
        response.setHeader(CoreConstants.TRACE_ID, traceId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        MDC.clear();
    }

}
