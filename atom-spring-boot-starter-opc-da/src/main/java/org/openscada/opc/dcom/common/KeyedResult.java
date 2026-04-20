package org.openscada.opc.dcom.common;

public class KeyedResult<K, V> extends Result<V> {

	private K key;

	public KeyedResult() {
		super();
	}

	public KeyedResult(final K key, final V value, final int errorCode) {
		super(value, errorCode);
		this.key = key;
	}

	public K getKey() {
		return this.key;
	}

	public void setKey(final K key) {
		this.key = key;
	}

}
