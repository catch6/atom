package org.openscada.opc.dcom.common;

import java.util.ArrayList;

public class KeyedResultSet<K, V> extends ArrayList<KeyedResult<K, V>> {

	private static final long serialVersionUID = 1L;

	public KeyedResultSet() {
		super();
	}

	public KeyedResultSet(final int size) {
		super(size); // me
	}

}
