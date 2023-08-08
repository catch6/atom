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

package net.wenzuo.atom.core.util;

/**
 * @author Catch
 * @since 2023-08-08
 */
public class BusinessException extends RuntimeException implements ResultProvider {

	public static final int DEFAULT_CODE = 400;
	public static final String DEFAULT_MESSAGE = "请求数据错误";

	private final int code;
	private final String message;

	public BusinessException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.message = message;
	}

	public BusinessException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BusinessException() {
		this(DEFAULT_CODE, DEFAULT_MESSAGE);
	}

	public BusinessException(String message) {
		this(DEFAULT_CODE, message);
	}

	public BusinessException(String message, Throwable cause) {
		this(DEFAULT_CODE, message, cause);
	}

	public BusinessException(Throwable cause) {
		this(DEFAULT_CODE, DEFAULT_MESSAGE, cause);
	}

	public BusinessException(ResultProvider provider) {
		this(provider.getCode(), provider.getMessage());
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
