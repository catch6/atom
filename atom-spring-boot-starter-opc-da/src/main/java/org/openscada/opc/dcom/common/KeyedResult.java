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

public class KeyedResult<K, V> extends Result<V> {

	private K key;

	public KeyedResult() {
		super();
	}

	public KeyedResult(final K key, final V value, final int errorCode) {
		super(value, errorCode);
		this.key = key;
	}

	public K getKey() {
		return this.key;
	}

	public void setKey(final K key) {
		this.key = key;
	}

}
