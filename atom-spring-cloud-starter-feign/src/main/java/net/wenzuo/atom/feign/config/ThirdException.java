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

import feign.FeignException;
import feign.Request;
import net.wenzuo.atom.core.util.ResultProvider;

/**
 * @author Catch
 * @since 2021-12-02
 */
public class ThirdException extends FeignException implements ResultProvider {

	public static final int DEFAULT_CODE = 500;
	public static final String DEFAULT_MESSAGE = "第三方服务繁忙, 请稍后再试";

	private final int code;
	private final String message;

	public ThirdException(int status, int code, String message, Request request) {
		super(status, message, request);
		this.code = code;
		this.message = message;
	}

	public ThirdException(int status, Request request) {
		this(status, DEFAULT_CODE, DEFAULT_MESSAGE, request);
	}

	public ThirdException(int status, String message, Request request) {
		this(status, DEFAULT_CODE, message, request);
	}

	public ThirdException(int status, ResultProvider provider, Request request) {
		this(status, provider.getCode(), provider.getMessage(), request);
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
