/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.mindit.atom.web.core;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@ConditionalOnProperty(value = "atom.web.logging.enabled", matchIfMissing = true)
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CachedWrapperFilter extends OncePerRequestFilter {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final Set<String> CACHED_METHODS = Set.of("POST", "PUT", "PATCH");

    @Resource
    private LoggingProperties loggingProperties;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${spring.mvc.servlet.path:}")
    private String servletPath;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String method = request.getMethod();
        // 排除 OPTIONS 请求
        if ("OPTIONS".contains(method)) {
            return true;
        }
        // 排除 Server-Sent Events (SSE) 的请求
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains(MediaType.TEXT_EVENT_STREAM_VALUE)) {
            return true;
        }

        String uri = request.getRequestURI().substring(contextPath.length() + servletPath.length());
        for (String path : loggingProperties.getInternalExcludePath()) {
            if (PATH_MATCHER.match(path, uri)) {
                return true;
            }
        }
        for (String path : loggingProperties.getExcludePath()) {
            if (PATH_MATCHER.match(path, uri)) {
                return true;
            }
        }
        for (String path : loggingProperties.getIncludePath()) {
            if (PATH_MATCHER.match(path, uri)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest requestToUse = wrapRequest(request);
        HttpServletResponse responseToUse = wrapResponse(response);
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            CachedResponseWrapper responseWrapper = (CachedResponseWrapper) responseToUse;
            responseWrapper.copyBodyToResponse();
        }
    }

    private HttpServletResponse wrapResponse(HttpServletResponse response) {
        if (!(response instanceof CachedResponseWrapper)) {
            return new CachedResponseWrapper(response);
        }
        return response;
    }

    private HttpServletRequest wrapRequest(HttpServletRequest request) throws IOException {
        if (CACHED_METHODS.contains(request.getMethod())
            && request.getContentLength() > 0
            && request.getContentType() != null
            && (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().contains(MediaType.APPLICATION_XML_VALUE))
            && !(request instanceof CachedRequestWrapper)) {
            return new CachedRequestWrapper(request);
        }
        return request;
    }

}
