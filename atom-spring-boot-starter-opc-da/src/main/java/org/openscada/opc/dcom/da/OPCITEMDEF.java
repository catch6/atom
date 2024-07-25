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

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.*;

public class OPCITEMDEF {

	private String accessPath = "";

	private String itemID = "";

	private boolean active = true;

	private int clientHandle;

	private short requestedDataType = JIVariant.VT_EMPTY;

	private short reserved;

	public String getAccessPath() {
		return this.accessPath;
	}

	public void setAccessPath(final String accessPath) {
		this.accessPath = accessPath;
	}

	public int getClientHandle() {
		return this.clientHandle;
	}

	public void setClientHandle(final int clientHandle) {
		this.clientHandle = clientHandle;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public String getItemID() {
		return this.itemID;
	}

	public void setItemID(final String itemID) {
		this.itemID = itemID;
	}

	public short getRequestedDataType() {
		return this.requestedDataType;
	}

	public void setRequestedDataType(final short requestedDataType) {
		this.requestedDataType = requestedDataType;
	}

	public short getReserved() {
		return this.reserved;
	}

	public void setReserved(final short reserved) {
		this.reserved = reserved;
	}

	/**
	 * Convert to structure to a J-Interop structure
	 *
	 * @return the j-interop structe
	 * @throws JIException
	 */
	public JIStruct toStruct() throws JIException {
		final JIStruct struct = new JIStruct();
		struct.addMember(new JIString(getAccessPath(), JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR));
		struct.addMember(new JIString(getItemID(), JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR));
		struct.addMember(new Integer(isActive() ? 1 : 0));
		struct.addMember(Integer.valueOf(getClientHandle()));

		struct.addMember(Integer.valueOf(0)); // blob size
		struct.addMember(new JIPointer(null)); // blob

		struct.addMember(Short.valueOf(getRequestedDataType()));
		struct.addMember(Short.valueOf(getReserved()));
		return struct;
	}

}
