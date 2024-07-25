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

package org.openscada.opc.dcom.common.impl;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIFrameworkHelper;
import org.openscada.opc.dcom.common.EventHandler;

public class EventHandlerImpl implements EventHandler {

	private String identifier = null;

	private IJIComObject object = null;

	public String getIdentifier() {
		return this.identifier;
	}

	public synchronized IJIComObject getObject() {
		return this.object;
	}

	public synchronized void setInfo(final IJIComObject object, final String identifier) {
		this.object = object;
		this.identifier = identifier;
	}

	public synchronized void detach() throws JIException {
		if (this.object != null && this.identifier != null) {
			try {
				JIFrameworkHelper.detachEventHandler(this.object, this.identifier);
			} finally {
				this.object = null;
				this.identifier = null;
			}
		}
	}

}
