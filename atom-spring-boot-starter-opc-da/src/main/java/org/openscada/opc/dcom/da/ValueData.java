package org.openscada.opc.dcom.da;

import org.jinterop.dcom.core.JIVariant;

import java.util.Calendar;

public class ValueData {

	private JIVariant value;

	private short quality;

	private Calendar timestamp;

	public short getQuality() {
		return this.quality;
	}

	public void setQuality(final short quality) {
		this.quality = quality;
	}

	public Calendar getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public JIVariant getValue() {
		return this.value;
	}

	public void setValue(final JIVariant value) {
		this.value = value;
	}

}
