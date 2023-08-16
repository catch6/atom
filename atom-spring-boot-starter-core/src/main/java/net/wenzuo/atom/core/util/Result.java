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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Catch
 * @since 2023-08-08
 */
@Schema(name = "Result", description = "返回结果")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> implements ResultProvider {

	private static final int OK_CODE = 200;
	private static final String OK_MESSAGE = "成功";
	private int code;
	private String message;
	private T data;

	public static <T> Result<T> ok() {
		return new Result<>(OK_CODE, OK_MESSAGE, null);
	}

	public static <T> Result<T> ok(T data) {
		return new Result<>(OK_CODE, OK_MESSAGE, data);
	}

	public static <T> Result<T> fail(int code, String message) {
		return new Result<>(code, message, null);
	}

	public static <T> Result<T> fail(ResultProvider provider) {
		return new Result<>(provider.getCode(), provider.getMessage(), null);
	}

}
