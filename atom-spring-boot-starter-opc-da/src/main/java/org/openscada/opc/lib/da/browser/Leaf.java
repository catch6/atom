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

package org.openscada.opc.lib.da.browser;

public class Leaf {

	private Branch _parent = null;

	private String _name = "";

	private String _itemId = null;

	public Leaf(final Branch parent, final String name) {
		this._parent = parent;
		this._name = name;
	}

	public Leaf(final Branch parent, final String name, final String itemId) {
		this._parent = parent;
		this._name = name;
		this._itemId = itemId;
	}

	public String getItemId() {
		return this._itemId;
	}

	public void setItemId(final String itemId) {
		this._itemId = itemId;
	}

	public String getName() {
		return this._name;
	}

	public void setName(final String name) {
		this._name = name;
	}

	public Branch getParent() {
		return this._parent;
	}

}
