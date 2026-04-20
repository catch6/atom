package org.openscada.opc.dcom.da;

public enum OPCBROWSETYPE {
	OPC_BRANCH(1),
	OPC_LEAF(2),
	OPC_FLAT(3),
	OPC_UNKNOWN(0);

	private int _id;

	private OPCBROWSETYPE(final int id) {
		this._id = id;
	}

	public int id() {
		return this._id;
	}

	public static OPCBROWSETYPE fromID(final int id) {
		switch (id) {
			case 1:
				return OPC_BRANCH;
			case 2:
				return OPC_LEAF;
			case 3:
				return OPC_FLAT;
			default:
				return OPC_UNKNOWN;
		}
	}
}
