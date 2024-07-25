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

import lombok.extern.slf4j.Slf4j;
import org.openscada.opc.dcom.da.OPCSERVERSTATUS;
import org.openscada.opc.dcom.da.impl.OPCServer;

/**
 * A server state operation which can be interruped
 *
 * @author Jens Reimann
 */
@Slf4j
public class ServerStateOperation implements Runnable {

	public OPCSERVERSTATUS _serverStatus = null;

	public OPCServer _server;

	public Throwable _error;

	public Object _lock = new Object();

	public boolean _running = false;

	public ServerStateOperation(final OPCServer server) {
		super();
		this._server = server;
	}

	/**
	 * Perform the operation.
	 * <p>
	 * This method will block until either the serve state has been aquired or the
	 * timeout triggers cancels the call.
	 * </p>
	 */
	public void run() {
		synchronized (this._lock) {
			this._running = true;
		}
		try {
			this._serverStatus = this._server.getStatus();
			synchronized (this._lock) {
				this._running = false;
				this._lock.notify();
			}
		} catch (Throwable e) {
			log.info("Failed to get server state", e);
			this._error = e;
			this._running = false;
			synchronized (this._lock) {
				this._lock.notify();
			}
		}

	}

	/**
	 * Get the server state with a timeout.
	 *
	 * @param timeout the timeout in ms
	 * @return the server state or <code>null</code> if the server is not set.
	 * @throws Throwable any error that occurred
	 */
	public OPCSERVERSTATUS getServerState(final int timeout) throws Throwable {
		if (this._server == null) {
			log.debug("No connection to server. Skipping...");
			return null;
		}

		Thread t = new Thread(this, "OPCServerStateReader");

		synchronized (this._lock) {
			t.start();
			this._lock.wait(timeout);
			if (this._running) {
				log.warn("State operation still running. Interrupting...");
				t.interrupt();
				throw new InterruptedException("Interrupted getting server state");
			}
		}
		if (this._error != null) {
			log.warn("An error occurred while getting server state", this._error);
			throw this._error;
		}

		return this._serverStatus;
	}

}
