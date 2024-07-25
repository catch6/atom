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

/**
 * A state for the auto-reconnect controller
 *
 * @author Jens Reimann
 */
public enum AutoReconnectState {
	/**
	 * Auto reconnect is disabled.
	 */
	DISABLED,
	/**
	 * Auto reconnect is enabled, but the connection is currently not established.
	 */
	DISCONNECTED,
	/**
	 * Auto reconnect is enabled, the connection is not established and the controller
	 * is currently waiting the delay until it will reconnect.
	 */
	WAITING,
	/**
	 * Auto reconnect is enabled, the connection is not established but the controller
	 * currently tries to establish the connection.
	 */
	CONNECTING,
	/**
	 * Auto reconnect is enabled and the connection is established.
	 */
	CONNECTED
}