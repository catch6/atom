package net.wenzuo.atom.web.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Catch
 * @since 2025-06-01
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    // 限制日志长度，避免过大请求体影响性能
    private static final int MAX_BODY_LENGTH = 10000;
    private static final ThreadLocal<Long> TIMER = new ThreadLocal<>();

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        TIMER.set(System.currentTimeMillis());
        StringBuilder loggingText = new StringBuilder();
        loggingText.append("REQUEST:")
                   .append(" ").append(request.getMethod())
                   .append(" ").append(request.getRequestURI());
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && !parameterMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (values == null) {
                    loggingText.append(" ").append(key).append("=null");
                    continue;
                }
                for (String value : values) {
                    loggingText.append(" ").append(key).append("=").append(value);
                }
            }
        }
        if (request instanceof CachedRequestWrapper requestToUse) {
            String body = new String(requestToUse.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (body.length() > MAX_BODY_LENGTH) {
                body = body.substring(0, MAX_BODY_LENGTH) + "...[TRUNCATED]";
            }
            loggingText.append(" ").append(body);
        }
        log.info(loggingText.toString());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        StringBuilder loggingText = new StringBuilder();
        loggingText.append("RESPONSE:")
                   .append(" ").append(System.currentTimeMillis() - TIMER.get()).append("ms")
                   .append(" ").append(response.getStatus());
        TIMER.remove();
        if (response instanceof CachedResponseWrapper responseToUse) {
            String contentType = response.getContentType();
            if (contentType != null
                && (contentType.contains(MediaType.APPLICATION_JSON_VALUE) || contentType.contains(MediaType.APPLICATION_XML_VALUE))) {
                String body = new String(responseToUse.getContentAsByteArray(), StandardCharsets.UTF_8);
                if (body.length() > MAX_BODY_LENGTH) {
                    body = body.substring(0, MAX_BODY_LENGTH) + "...[TRUNCATED]";
                }
                loggingText.append(" ").append(body);
            }
        }
        log.info(loggingText.toString());
    }

}
