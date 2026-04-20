package org.openscada.opc.dcom.da;

public enum OPCDATASOURCE {
	OPC_DS_CACHE(1),
	OPC_DS_DEVICE(2),
	OPC_DS_UNKNOWN(0);

	private int _id;

	private OPCDATASOURCE(final int id) {
		this._id = id;
	}

	public int id() {
		return this._id;
	}

	public static OPCDATASOURCE fromID(final int id) {
		switch (id) {
			case 1:
				return OPC_DS_CACHE;
			case 2:
				return OPC_DS_DEVICE;
			default:
				return OPC_DS_UNKNOWN;
		}
	}
}
