/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.feign.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

/**
 * @author Catch
 * @since 2023-08-09
 */
@ConditionalOnProperty(value = "atom.feign.logging", matchIfMissing = true)
@Component
public class FeignLogger extends Logger {

	private static final ThreadLocal<Long> TIMER = new ThreadLocal<>();

	private final org.slf4j.Logger logger;

	public FeignLogger() {
		this(Logger.class);
	}

	public FeignLogger(Class<?> clazz) {
		this(LoggerFactory.getLogger(clazz));
	}

	public FeignLogger(String name) {
		this(LoggerFactory.getLogger(name));
	}

	FeignLogger(org.slf4j.Logger logger) {
		this.logger = logger;
	}

	protected void logRequest(String configKey, Logger.Level logLevel, Request request) {
		TIMER.set(System.currentTimeMillis());
		String bodyText = null;
		if (request.body() != null) {
			bodyText =
				request.charset() != null
					? new String(request.body(), request.charset())
					: null;
		}
		if (bodyText == null) {
			this.logger.info("THIRD-REQUEST: {} {}", request.httpMethod().name(), request.url());
			return;
		}
		this.logger.info("THIRD-REQUEST: {} {} {}", request.httpMethod().name(), request.url(), bodyText);
	}

	protected Response logAndRebufferResponse(String configKey, Logger.Level logLevel, Response response, long elapsedTime) throws IOException {
		int status = response.status();
		long time = System.currentTimeMillis() - TIMER.get();
		TIMER.remove();
		if (response.body() != null && !(status == 204 || status == 205)) {
			byte[] bodyData = Util.toByteArray(response.body().asInputStream());
			String bodyText = decodeOrDefault(bodyData, UTF_8, null);
			this.logger.info("THIRD-RESPONSE: {}ms {} {}", time, status, bodyText);
			return response.toBuilder().body(bodyData).build();
		}
		this.logger.info("THIRD-RESPONSE: {}ms {}", time, status);
		return response;
	}

	protected void log(String configKey, String format, Object... args) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug(String.format(methodTag(configKey) + format, args));
		}
	}

}
