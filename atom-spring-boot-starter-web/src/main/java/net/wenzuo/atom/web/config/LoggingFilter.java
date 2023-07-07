/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.web.config;

import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.NanoIdUtils;
import net.wenzuo.atom.web.properties.LoggingProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@Order(value = Ordered.HIGHEST_PRECEDENCE + 100)
@Component
public class LoggingFilter extends OncePerRequestFilter {

	private static final ThreadLocal<Long> TIMER = new ThreadLocal<>();

	private static final String REQ_ID = "Req-Id";

	private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

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

		CachedRequestWrapper requestToUse;
		if (request instanceof CachedRequestWrapper) {
			requestToUse = (CachedRequestWrapper) request;
		} else {
			requestToUse = new CachedRequestWrapper(request);
		}

		CachedResponseWrapper responseToUse;
		if (response instanceof CachedResponseWrapper) {
			responseToUse = (CachedResponseWrapper) response;
		} else {
			responseToUse = new CachedResponseWrapper(response);
		}

		loggingRequest(requestToUse);

		try {
			filterChain.doFilter(requestToUse, responseToUse);
		} finally {
			loggingResponse(responseToUse);
			TIMER.remove();
			MDC.remove(REQ_ID);
		}

	}

	private void loggingRequest(CachedRequestWrapper wrapper) throws IOException {
		StringBuilder msg = new StringBuilder();
		msg.append("REQUEST: ")
		   .append(wrapper.getMethod()).append(' ')
		   .append(wrapper.getRequestURI());

		String queryString = wrapper.getQueryString();
		if (queryString != null) {
			msg.append('?').append(URLDecoder.decode(queryString, StandardCharsets.UTF_8.name()));
		}

		// String client = request.getRemoteAddr();
		// if (StringUtils.hasLength(client)) {
		//     msg.append(", client=").append(client);
		// }
		// HttpSession session = request.getSession(false);
		// if (session != null) {
		//     msg.append(", session=").append(session.getId());
		// }
		// String user = request.getRemoteUser();
		// if (user != null) {
		//     msg.append(", user=").append(user);
		// }

		// HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
		// msg.append(", headers=").append(headers);

		if (isReadable(wrapper)) {
			String payload;
			try (ServletInputStream inputStream = wrapper.getInputStream()) {
				payload = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			}

			if (payload.length() == 0) {
				Map<String, String[]> form = wrapper.getParameterMap();
				StringBuilder param = new StringBuilder();
				for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
					String name = nameIterator.next();
					List<String> values = Arrays.asList(form.get(name));
					for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
						String value = valueIterator.next();
						param.append(name);
						if (value != null) {
							param.append('=')
								 .append(value);
							if (valueIterator.hasNext()) {
								param.append('&');
							}
						}
					}
					if (nameIterator.hasNext()) {
						param.append('&');
					}
				}
				payload = param.toString();
			}
			if (payload.length() > 0) {
				msg.append(' ').append(payload);
			}
		}

		log.info(msg.toString());
	}

	private void loggingResponse(CachedResponseWrapper wrapper) throws IOException {
		StringBuilder msg = new StringBuilder();
		long time = System.currentTimeMillis() - TIMER.get();
		msg.append("RESPONSE: ").append(time).append("ms");
		msg.append(' ').append(wrapper.getStatus());

		if (isReadable(wrapper)) {
			String payload;
			// byte[] byteArray = wrapper.getContentAsByteArray();
			// if (byteArray.length > 0) {
			//     payload = new String(byteArray, StandardCharsets.UTF_8);
			// }

			try (InputStream is = wrapper.getContentInputStream()) {
				payload = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
			}
			if (payload.length() > 0) {
				msg.append(' ').append(payload);
			}
		}
		wrapper.copyBodyToResponse();

		log.info(msg.toString());
	}

	private boolean isReadable(CachedRequestWrapper wrapper) {
		String contentType = wrapper.getContentType();
		if (contentType == null) {
			return false;
		}
		return !contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE);
	}

	private boolean isReadable(CachedResponseWrapper wrapper) {
		if (wrapper.getStatus() != HttpStatus.OK.value()) {
			return true;
		}
		String contentType = wrapper.getContentType();
		if (contentType == null) {
			return false;
		}
		return contentType.contains(MediaType.APPLICATION_JSON_VALUE)
			|| contentType.contains(MediaType.APPLICATION_XML_VALUE)
			|| contentType.contains(MediaType.TEXT_PLAIN_VALUE)
			|| contentType.contains(MediaType.TEXT_HTML_VALUE)
			|| contentType.contains(MediaType.TEXT_XML_VALUE);
	}

}
