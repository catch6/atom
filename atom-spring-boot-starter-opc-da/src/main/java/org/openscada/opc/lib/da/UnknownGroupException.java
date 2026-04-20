package org.openscada.opc.lib.da;

public class UnknownGroupException extends Exception {

	private String _name = null;

	public UnknownGroupException(final String name) {
		super();
		this._name = name;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1771564928794033075L;

	public String getName() {
		return this._name;
	}

	public void setName(final String name) {
		this._name = name;
	}

}
