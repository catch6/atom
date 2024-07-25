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

package org.openscada.opc.lib.da;

import java.util.HashMap;
import java.util.Map;

public class AddFailedException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 5299486640366935298L;

	private Map<String, Integer> _errors = new HashMap<String, Integer>();

	private Map<String, Item> _items = new HashMap<String, Item>();

	public AddFailedException(final Map<String, Integer> errors, final Map<String, Item> items) {
		super();
		this._errors = errors;
		this._items = items;
	}

	/**
	 * Get the map of item id to error code
	 *
	 * @return the result map containing the failed items
	 */
	public Map<String, Integer> getErrors() {
		return this._errors;
	}

	/**
	 * Get the map of item it to item object
	 *
	 * @return the result map containing the succeeded items
	 */
	public Map<String, Item> getItems() {
		return this._items;
	}

}
