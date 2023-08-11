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

package net.wenzuo.atom.core.util;

/**
 * @author Catch
 * @since 2023-08-08
 */
public class ServiceException extends RuntimeException implements ResultProvider {

	public static final int DEFAULT_CODE = 500;
	public static final String DEFAULT_MESSAGE = "服务繁忙, 请稍后再试";

	private final int code;
	private final String message;

	public ServiceException() {
		this(DEFAULT_CODE, DEFAULT_MESSAGE);
	}

	public ServiceException(String message) {
		this(DEFAULT_CODE, message);
	}

	public ServiceException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public ServiceException(ResultProvider provider) {
		this(provider.getCode(), provider.getMessage());
	}

	public ServiceException(Throwable cause) {
		this(DEFAULT_CODE, DEFAULT_MESSAGE, cause);
	}

	public ServiceException(String message, Throwable cause) {
		this(DEFAULT_CODE, message, cause);
	}

	public ServiceException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.message = message;
	}

	public ServiceException(ResultProvider provider, Throwable cause) {
		this(provider.getCode(), provider.getMessage(), cause);
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
