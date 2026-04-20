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
