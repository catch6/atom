/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.openscada.opc.dcom.common;

public class Result<T> {

	private T value;

	private int errorCode;

	public Result() {
	}

	public Result(final T value, final int errorCode) {
		this.value = value;
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(final int errorCode) {
		this.errorCode = errorCode;
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(final T value) {
		this.value = value;
	}

	public boolean isFailed() {
		return this.errorCode != 0;
	}

}
