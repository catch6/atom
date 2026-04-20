package org.openscada.opc.dcom.common;

public class Result<T> {

	private T value;

	private int errorCode;

	public Result() {
	}

	public Result(final T value, final int errorCode) {
		this.value = value;
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(final int errorCode) {
		this.errorCode = errorCode;
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(final T value) {
		this.value = value;
	}

	public boolean isFailed() {
		return this.errorCode != 0;
	}

}
