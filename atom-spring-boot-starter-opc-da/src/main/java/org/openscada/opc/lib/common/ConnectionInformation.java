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

package org.openscada.opc.lib.common;

/**
 * Holds the connection information
 *
 * @author Jens Reimann jens.reimann@th4-systems.com
 * <p>
 * If both <code>clsId</code> and <code>progId</code> are set then <code>clsId</code>
 * has priority!
 */
public class ConnectionInformation {

	private String _host = "localhost";

	private String _domain = "localhost";

	private String _user = "";

	private String _password = "";

	private String _clsid = null;

	private String _progId = null;

	public ConnectionInformation() {
		super();
	}

	public ConnectionInformation(String host, String clsid, String user, String password) {
		this._host = host;
		this._clsid = clsid;
		this._user = user;
		this._password = password;
	}

	public ConnectionInformation(final String user, final String password) {
		super();
		this._user = user;
		this._password = password;
	}

	public ConnectionInformation(final ConnectionInformation arg0) {
		super();
		this._user = arg0._user;
		this._password = arg0._password;
		this._domain = arg0._domain;
		this._host = arg0._host;
		this._progId = arg0._progId;
		this._clsid = arg0._clsid;
	}

	public String getDomain() {
		return this._domain;
	}

	/**
	 * Set the domain of the user used for logging on
	 *
	 * @param domain
	 */
	public void setDomain(final String domain) {
		this._domain = domain;
	}

	public String getHost() {
		return this._host;
	}

	/**
	 * Set the host on which the server is located
	 *
	 * @param host The host to use, either an IP address oder hostname
	 */
	public void setHost(final String host) {
		this._host = host;
	}

	public String getPassword() {
		return this._password;
	}

	public void setPassword(final String password) {
		this._password = password;
	}

	public String getUser() {
		return this._user;
	}

	public void setUser(final String user) {
		this._user = user;
	}

	public String getClsid() {
		return this._clsid;
	}

	public void setClsid(final String clsid) {
		this._clsid = clsid;
	}

	public String getProgId() {
		return this._progId;
	}

	public void setProgId(final String progId) {
		this._progId = progId;
	}

	public String getClsOrProgId() {
		if (this._clsid != null) {
			return this._clsid;
		} else if (this._progId != null) {
			return this._progId;
		} else {
			return null;
		}
	}

}
