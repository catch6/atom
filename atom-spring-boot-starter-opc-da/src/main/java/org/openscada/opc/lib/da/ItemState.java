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

package org.openscada.opc.lib.da;

import org.jinterop.dcom.core.JIVariant;

import java.util.Calendar;

public class ItemState {

	private int _errorCode = 0;

	private JIVariant _value = null;

	private Calendar _timestamp = null;

	private Short _quality = null;

	public ItemState(final int errorCode, final JIVariant value, final Calendar timestamp, final Short quality) {
		super();
		this._errorCode = errorCode;
		this._value = value;
		this._timestamp = timestamp;
		this._quality = quality;
	}

	public ItemState() {
		super();
	}

	@Override
	public String toString() {
		return String.format("Value: %s, Timestamp: %tc, Quality: %s, ErrorCode: %08x", this._value, this._timestamp, this._quality, this._errorCode);
	}

	public Short getQuality() {
		return this._quality;
	}

	public void setQuality(final Short quality) {
		this._quality = quality;
	}

	public Calendar getTimestamp() {
		return this._timestamp;
	}

	public void setTimestamp(final Calendar timestamp) {
		this._timestamp = timestamp;
	}

	public JIVariant getValue() {
		return this._value;
	}

	public void setValue(final JIVariant value) {
		this._value = value;
	}

	public int getErrorCode() {
		return this._errorCode;
	}

	public void setErrorCode(final int errorCode) {
		this._errorCode = errorCode;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + this._errorCode;
		result = PRIME * result + (this._quality == null ? 0 : this._quality.hashCode());
		result = PRIME * result + (this._timestamp == null ? 0 : this._timestamp.hashCode());
		result = PRIME * result + (this._value == null ? 0 : this._value.hashCode());
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
		final ItemState other = (ItemState) obj;
		if (this._errorCode != other._errorCode) {
			return false;
		}
		if (this._quality == null) {
			if (other._quality != null) {
				return false;
			}
		} else if (!this._quality.equals(other._quality)) {
			return false;
		}
		if (this._timestamp == null) {
			if (other._timestamp != null) {
				return false;
			}
		} else if (!this._timestamp.equals(other._timestamp)) {
			return false;
		}
		if (this._value == null) {
			if (other._value != null) {
				return false;
			}
		} else if (!this._value.equals(other._value)) {
			return false;
		}
		return true;
	}

}
