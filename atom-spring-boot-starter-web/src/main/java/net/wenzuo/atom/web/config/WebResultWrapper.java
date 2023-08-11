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

package net.wenzuo.atom.web.config;

import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.JsonUtils;
import net.wenzuo.atom.core.util.NonResultWrapper;
import net.wenzuo.atom.core.util.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Catch
 * @since 2023-08-08
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(value = "atom.web.result-wrapper", matchIfMissing = true)
public class WebResultWrapper implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
		// 若类或者方法上加了 @NonResultWrapper 注解, 则不再包装 Result
		return !returnType.getDeclaringClass().isAnnotationPresent(NonResultWrapper.class)
			&& !returnType.hasMethodAnnotation(NonResultWrapper.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
		// 如果已经包装了 Result, 则不再包装
		if (body instanceof Result<?>) {
			return body;
		}
		// fix: java.lang.ClassCastException: class net.wenzuo.atom.core.util.Result cannot be cast to class java.lang.String (net.wenzuo.atom.core.util.Result is in unnamed module of loader 'app'; java.lang.String is in module java.base of loader 'bootstrap')
		if (body instanceof String) {
			// 解决返回值为字符串时，不能正常包装
			return JsonUtils.toJson(Result.ok(body));
		}
		return Result.ok(body);
	}

}
