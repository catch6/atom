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

public enum OPCENUMSCOPE {
	OPC_ENUM_PRIVATE_CONNECTIONS(1),
	OPC_ENUM_PUBLIC_CONNECTIONS(2),
	OPC_ENUM_ALL_CONNECTIONS(3),
	OPC_ENUM_PRIVATE(4),
	OPC_ENUM_PUBLIC(5),
	OPC_ENUM_ALL(6),
	OPC_ENUM_UNKNOWN(0);

	private int _id;

	private OPCENUMSCOPE(final int id) {
		this._id = id;
	}

	public int id() {
		return this._id;
	}

	public static OPCENUMSCOPE fromID(final int id) {
		switch (id) {
			case 1:
				return OPC_ENUM_PRIVATE_CONNECTIONS;
			case 2:
				return OPC_ENUM_PUBLIC_CONNECTIONS;
			case 3:
				return OPC_ENUM_ALL_CONNECTIONS;
			case 4:
				return OPC_ENUM_PRIVATE;
			case 5:
				return OPC_ENUM_PUBLIC;
			case 6:
				return OPC_ENUM_ALL;
			default:
				return OPC_ENUM_UNKNOWN;
		}
	}
}
