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
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.NonResultWrapper;
import net.wenzuo.atom.core.util.Result;
import org.apache.commons.lang3.reflect.TypeUtils;
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
@ConditionalOnProperty(value = "atom.feign.result-wrapper", matchIfMissing = true)
public class FeignClientDecoder extends SpringDecoder {

	public FeignClientDecoder(ObjectFactory<HttpMessageConverters> messageConverters, ObjectProvider<HttpMessageConverterCustomizer> customizers) {
		super(messageConverters, customizers);
	}

	@Override
	public Object decode(Response response, Type type) throws IOException, FeignException {
		Method method = response.request().requestTemplate().methodMetadata().method();
		boolean resultWrapper = !method.isAnnotationPresent(NonResultWrapper.class)
			&& !method.getDeclaringClass().isAnnotationPresent(NonResultWrapper.class);
		if (resultWrapper) {
			type = TypeUtils.parameterize(Result.class, type);
		}
		int status = response.status();
		Object object = super.decode(response, type);
		if (resultWrapper) {
			Result<?> result = (Result<?>) object;
			if (status < 400) {
				return result.getData();
			}
			throw new ThirdException(status, result, response.request());
		}
		if (status < 400) {
			return object;
		}
		throw new ThirdException(status, response.request());
	}

}
