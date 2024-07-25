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

package org.openscada.opc.dcom.da;

public class PropertyDescription {

	private int _id = -1;

	private String _description = "";

	private short _varType = 0;

	public String getDescription() {
		return this._description;
	}

	public void setDescription(final String description) {
		this._description = description;
	}

	public int getId() {
		return this._id;
	}

	public void setId(final int id) {
		this._id = id;
	}

	public short getVarType() {
		return this._varType;
	}

	public void setVarType(final short varType) {
		this._varType = varType;
	}

}
