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

public class IORequest {

	private String itemID;

	private int maxAge;

	public IORequest(final String itemID, final int maxAge) {
		this.itemID = itemID;
		this.maxAge = maxAge;
	}

	public String getItemID() {
		return this.itemID;
	}

	public void setItemID(final String itemID) {
		this.itemID = itemID;
	}

	public int getMaxAge() {
		return this.maxAge;
	}

	public void setMaxAge(final int maxAge) {
		this.maxAge = maxAge;
	}

}