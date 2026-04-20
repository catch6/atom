package org.openscada.opc.lib.da;

import org.jinterop.dcom.core.JIVariant;

public class WriteRequest {

	private Item _item = null;

	private JIVariant _value = null;

	public WriteRequest(final Item item, final JIVariant value) {
		super();
		this._item = item;
		this._value = value;
	}

	public Item getItem() {
		return this._item;
	}

	public JIVariant getValue() {
		return this._value;
	}

}
