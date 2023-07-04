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

package net.wenzuo.atom.core.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Catch
 * @since 2023-05-30
 */
public class ForbiddenException extends HttpException {

	private static final int STATUS = HttpStatus.FORBIDDEN.value();

	public ForbiddenException(String message) {
		super(STATUS, message);
	}

	public ForbiddenException(Throwable t, String message) {
		super(t, STATUS, message);
	}

}
