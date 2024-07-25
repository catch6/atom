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

public enum OPCSERVERSTATE {
	OPC_STATUS_RUNNING(1),
	OPC_STATUS_FAILED(2),
	OPC_STATUS_NOCONFIG(3),
	OPC_STATUS_SUSPENDED(4),
	OPC_STATUS_TEST(5),
	OPC_STATUS_COMM_FAULT(6),
	OPC_STATUS_UNKNOWN(0);

	private int _id;

	private OPCSERVERSTATE(final int id) {
		this._id = id;
	}

	public int id() {
		return this._id;
	}

	public static OPCSERVERSTATE fromID(final int id) {
		switch (id) {
			case 1:
				return OPC_STATUS_RUNNING;
			case 2:
				return OPC_STATUS_FAILED;
			case 3:
				return OPC_STATUS_NOCONFIG;
			case 4:
				return OPC_STATUS_SUSPENDED;
			case 5:
				return OPC_STATUS_TEST;
			case 6:
				return OPC_STATUS_COMM_FAULT;
			default:
				return OPC_STATUS_UNKNOWN;
		}
	}
}
