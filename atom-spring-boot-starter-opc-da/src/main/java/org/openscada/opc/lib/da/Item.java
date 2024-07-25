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

import lombok.extern.slf4j.Slf4j;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;

@Slf4j
public class Item {

	private Group _group = null;

	private int _serverHandle = 0;

	private int _clientHandle = 0;

	private String _id = null;

	Item(final Group group, final int serverHandle, final int clientHandle, final String id) {
		super();
		log.debug(String.format("Adding new item '%s' (0x%08X) for group %s", id, serverHandle, group.toString()));
		this._group = group;
		this._serverHandle = serverHandle;
		this._clientHandle = clientHandle;
		this._id = id;
	}

	public Group getGroup() {
		return this._group;
	}

	public int getServerHandle() {
		return this._serverHandle;
	}

	public int getClientHandle() {
		return this._clientHandle;
	}

	public String getId() {
		return this._id;
	}

	public void setActive(final boolean state) throws JIException {
		this._group.setActive(state, this);
	}

	public ItemState read(final boolean device) throws JIException {
		return this._group.read(device, this).get(this);
	}

	public Integer write(final JIVariant value) throws JIException {
		return this._group.write(new WriteRequest[]{new WriteRequest(this, value)}).get(this);
	}

}
