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

package org.openscada.opc.dcom.common;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIStruct;

import java.math.BigDecimal;
import java.util.Calendar;

public class FILETIME {

	private int high = 0;

	private int low = 0;

	public FILETIME() {
	}

	public FILETIME(final FILETIME arg0) {
		this.high = arg0.high;
		this.low = arg0.low;
	}

	public FILETIME(final int high, final int low) {
		this.high = high;
		this.low = low;
	}

	public int getHigh() {
		return this.high;
	}

	public void setHigh(final int high) {
		this.high = high;
	}

	public int getLow() {
		return this.low;
	}

	public void setLow(final int low) {
		this.low = low;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + this.high;
		result = PRIME * result + this.low;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final FILETIME other = (FILETIME) obj;
		if (this.high != other.high) {
			return false;
		}
		if (this.low != other.low) {
			return false;
		}
		return true;
	}

	public static JIStruct getStruct() throws JIException {
		final JIStruct struct = new JIStruct();

		struct.addMember(Integer.class);
		struct.addMember(Integer.class);

		return struct;
	}

	public static FILETIME fromStruct(final JIStruct struct) {
		final FILETIME ft = new FILETIME();

		ft.setLow((Integer) struct.getMember(0));
		ft.setHigh((Integer) struct.getMember(1));

		return ft;
	}

	public Calendar asCalendar() {
		final Calendar c = Calendar.getInstance();

		/*
		 * The following "strange" stuff is needed since we miss a ulong type
		 */
		long i = 0xFFFFFFFFL & this.high;
		i = i << 32;
		long j = 0xFFFFFFFFFFFFFFFFL & i;

		i = 0xFFFFFFFFL & this.low;
		j += i;
		j /= 10000L;
		j -= 11644473600000L;

		c.setTimeInMillis(j);

		return c;
	}

	public Calendar asBigDecimalCalendar() {
		final Calendar c = Calendar.getInstance();

		/*
		 * The following "strange" stuff is needed since we miss a ulong type
		 */
		long i = 0xFFFFFFFFL & this.high;
		i = i << 32;
		BigDecimal d1 = new BigDecimal(0xFFFFFFFFFFFFFFFFL & i);

		i = 0xFFFFFFFFL & this.low;
		d1 = d1.add(new BigDecimal(i));
		d1 = d1.divide(new BigDecimal(10000L));
		d1 = d1.subtract(new BigDecimal(11644473600000L));

		c.setTimeInMillis(d1.longValue());

		return c;
	}

	@Override
	public String toString() {
		return String.format("%s/%s", this.high, this.low);
	}

}
