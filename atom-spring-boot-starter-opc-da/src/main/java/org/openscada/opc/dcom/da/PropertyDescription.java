package org.openscada.opc.dcom.da;

public class PropertyDescription {

	private int _id = -1;

	private String _description = "";

	private short _varType = 0;

	public String getDescription() {
		return this._description;
	}

	public void setDescription(final String description) {
		this._description = description;
	}

	public int getId() {
		return this._id;
	}

	public void setId(final int id) {
		this._id = id;
	}

	public short getVarType() {
		return this._varType;
	}

	public void setVarType(final short varType) {
		this._varType = varType;
	}

}
