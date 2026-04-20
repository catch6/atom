package org.openscada.opc.dcom.common.impl;

import org.jinterop.dcom.core.IJIComObject;

public class BaseCOMObject {

	private IJIComObject comObject = null;

	/**
	 * Create a new base COM object
	 *
	 * @param comObject The COM object to wrap but be addRef'ed
	 */
	public BaseCOMObject(final IJIComObject comObject) {
		this.comObject = comObject;
	}

	protected synchronized IJIComObject getCOMObject() {
		return this.comObject;
	}

}
