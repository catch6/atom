package cn.mindit.atom.web.core;

import cn.mindit.atom.core.core.CoreConstants;
import cn.mindit.atom.core.util.NanoIdUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
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
        MDC.put(CoreConstants.TRACE_ID, NanoIdUtils.nanoId());
        response.setHeader(CoreConstants.TRACE_ID, MDC.get(CoreConstants.TRACE_ID));
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        MDC.clear();
    }

}
