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