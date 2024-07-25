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

public enum OPCBROWSETYPE {
	OPC_BRANCH(1),
	OPC_LEAF(2),
	OPC_FLAT(3),
	OPC_UNKNOWN(0);

	private int _id;

	private OPCBROWSETYPE(final int id) {
		this._id = id;
	}

	public int id() {
		return this._id;
	}

	public static OPCBROWSETYPE fromID(final int id) {
		switch (id) {
			case 1:
				return OPC_BRANCH;
			case 2:
				return OPC_LEAF;
			case 3:
				return OPC_FLAT;
			default:
				return OPC_UNKNOWN;
		}
	}
}
