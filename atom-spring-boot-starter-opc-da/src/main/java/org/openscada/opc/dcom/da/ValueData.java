/*
 * Copyright (c) 2022-2024 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
