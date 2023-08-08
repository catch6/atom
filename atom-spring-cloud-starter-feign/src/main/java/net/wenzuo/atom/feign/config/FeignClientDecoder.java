/*
 * Copyright (c) 2022-2023 Catch (catchlife6@163.com)
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

import feign.FeignException;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.JsonUtils;
import net.wenzuo.atom.core.util.NonResultWrapper;
import net.wenzuo.atom.core.util.Result;
import net.wenzuo.atom.core.util.ResultProvider;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author Catch
 * @since 2021-06-29
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "atom.feign.logging", matchIfMissing = true)
public class FeignClientDecoder extends SpringDecoder {

	public FeignClientDecoder(ObjectFactory<HttpMessageConverters> messageConverters, ObjectProvider<HttpMessageConverterCustomizer> customizers) {
		super(messageConverters, customizers);
	}

	@Override
	public Object decode(Response response, Type type) throws IOException, FeignException {
		long time = System.currentTimeMillis() - FeignClientEncoder.TIMER.get();
		FeignClientEncoder.TIMER.remove();
		int status = response.status();
		Object result = doDecode(response, type);
		String json = JsonUtils.toJson(result);
		log.info("THIRD-RESPONSE: {}ms {} {}", time, status, json);
		if (status == 200) {
			if (result instanceof Result<?> r) {
				return r.getData();
			}
			return result;
		}
		if (result instanceof ResultProvider provider) {
			throw new ThirdException(status, provider, response.request());
		}
		throw new ThirdException(status, response.request());
	}

	private Object doDecode(Response response, Type type) throws IOException {
		Object result;
		Method method = response.request().requestTemplate().methodMetadata().method();
		if (method.getDeclaringClass().isAnnotationPresent(NonResultWrapper.class)
			|| method.isAnnotationPresent(NonResultWrapper.class)) {
			return super.decode(response, type);
		}
		Response.Body body = response.body();
		if (body == null) {
			return super.decode(response, type);
		}
		String bodyStr = Util.toString(body.asReader(Util.UTF_8));
		Class<?> returnType = method.getReturnType();
		result = JsonUtils.toObject(bodyStr, Result.class, returnType);
		return result;
	}

}
