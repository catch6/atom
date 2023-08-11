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

package net.wenzuo.atom.core.exception;

import lombok.Getter;

/**
 * @author Catch
 * @since 2023-07-03
 */
@Getter
public class HttpException extends RuntimeException {

	private final int status;
	private final String message;

	public HttpException(int status, String message) {
		super(message);
		this.status = status;
		this.message = message;
	}

	public HttpException(Throwable t, int status, String message) {
		super(t);
		this.status = status;
		this.message = message;
	}

}
