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

package org.openscada.opc.dcom.list;

/**
 * Details about an OPC server class
 *
 * @author Jens Reimann &lt;jens.reimann@th4-systems.com&gt;
 * @since 0.1.8
 */
public class ClassDetails {

	private String _clsId;

	private String _progId;

	private String _description;

	public String getClsId() {
		return this._clsId;
	}

	public void setClsId(final String clsId) {
		this._clsId = clsId;
	}

	public String getDescription() {
		return this._description;
	}

	public void setDescription(final String description) {
		this._description = description;
	}

	public String getProgId() {
		return this._progId;
	}

	public void setProgId(final String progId) {
		this._progId = progId;
	}

}
