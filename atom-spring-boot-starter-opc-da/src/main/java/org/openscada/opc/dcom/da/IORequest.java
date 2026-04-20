package org.openscada.opc.dcom.da;

public class IORequest {

	private String itemID;

	private int maxAge;

	public IORequest(final String itemID, final int maxAge) {
		this.itemID = itemID;
		this.maxAge = maxAge;
	}

	public String getItemID() {
		return this.itemID;
	}

	public void setItemID(final String itemID) {
		this.itemID = itemID;
	}

	public int getMaxAge() {
		return this.maxAge;
	}

	public void setMaxAge(final int maxAge) {
		this.maxAge = maxAge;
	}

}