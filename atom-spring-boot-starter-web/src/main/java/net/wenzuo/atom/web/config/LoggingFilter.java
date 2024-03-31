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

package net.wenzuo.atom.web.config;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.NanoIdUtils;
import net.wenzuo.atom.web.properties.LoggingProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Slf4j
@ConditionalOnProperty(value = "atom.web.logging.enabled", matchIfMissing = true)
@Order(value = -50)
@Component
public class LoggingFilter extends OncePerRequestFilter {

	private static final ThreadLocal<Long> TIMER = new ThreadLocal<>();
	private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
	private static final String REQ_ID = "Req-Id";
	private static final String OPTIONS = "OPTIONS";

	@Resource
	private LoggingProperties loggingProperties;

	@Value("${server.servlet.context-path:}")
	private String contextPath;

	@Value("${spring.mvc.servlet.path:}")
	private String servletPath;

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
		if (!log.isInfoEnabled()) {
			return true;
		}

		String method = request.getMethod();
		if (OPTIONS.contains(method)) {
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
		TIMER.set(System.currentTimeMillis());
		String reqId = NanoIdUtils.nanoId();
		response.setHeader(REQ_ID, reqId);
		MDC.put(REQ_ID, reqId);

		HttpServletRequest requestToUse = loggingRequest(request);
		HttpServletResponse responseToUse = response;

		if (!(responseToUse instanceof CachedResponseWrapper)) {
			responseToUse = new CachedResponseWrapper(response);
		}
		try {
			filterChain.doFilter(requestToUse, responseToUse);
		} finally {
			loggingResponse((CachedResponseWrapper) responseToUse);
			TIMER.remove();
			MDC.remove(REQ_ID);
		}
	}

	private HttpServletRequest loggingRequest(HttpServletRequest request) throws IOException {
		HttpServletRequest requestToUse = request;
		StringBuilder msg = new StringBuilder();
		msg.append("REQUEST: ")
		   .append(requestToUse.getMethod()).append(' ')
		   .append(requestToUse.getRequestURI());

		String queryString = requestToUse.getQueryString();
		if (queryString != null) {
			msg.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8));
		}

		StringBuilder payload = new StringBuilder();
		Map<String, String[]> form = requestToUse.getParameterMap();
		for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
			String name = nameIterator.next();
			List<String> values = Arrays.asList(form.get(name));
			for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
				String value = valueIterator.next();
				payload.append(name);
				if (value != null) {
					payload.append('=')
						   .append(value);
					if (valueIterator.hasNext()) {
						payload.append('&');
					}
				}
			}
			if (nameIterator.hasNext()) {
				payload.append('&');
			}
		}

		String contentType = requestToUse.getContentType();
		if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
			if (!(requestToUse instanceof CachedRequestWrapper)) {
				requestToUse = new CachedRequestWrapper(request);
			}
			try (ServletInputStream inputStream = requestToUse.getInputStream()) {
				payload.append(StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8));
			}
		}

		if (!payload.isEmpty()) {
			msg.append(' ').append(payload);
		}
		log.info(msg.toString());
		return requestToUse;
	}

	private void loggingResponse(CachedResponseWrapper wrapper) throws IOException {
		StringBuilder msg = new StringBuilder();
		long time = System.currentTimeMillis() - TIMER.get();
		msg.append("RESPONSE: ").append(time).append("ms");
		msg.append(' ').append(wrapper.getStatus());

		String contentType = wrapper.getContentType();
		if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
			String payload;
			try (InputStream is = wrapper.getContentInputStream()) {
				payload = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
			}
			if (!payload.isEmpty()) {
				msg.append(' ').append(payload);
			}
		}
		wrapper.copyBodyToResponse();
		log.info(msg.toString());
	}

}
