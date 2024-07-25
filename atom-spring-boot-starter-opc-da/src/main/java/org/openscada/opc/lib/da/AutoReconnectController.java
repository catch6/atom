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

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class AutoReconnectController implements ServerConnectionStateListener {

	private static final int DEFAULT_DELAY = 5 * 1000;

	private int _delay;

	private final Server _server;

	private final Set<AutoReconnectListener> _listeners = new CopyOnWriteArraySet<AutoReconnectListener>();

	private AutoReconnectState _state = AutoReconnectState.DISABLED;

	private Thread _connectTask = null;

	public AutoReconnectController(final Server server) {
		this(server, DEFAULT_DELAY);
	}

	public AutoReconnectController(final Server server, final int delay) {
		super();
		setDelay(delay);

		this._server = server;
		this._server.addStateListener(this);
	}

	public void addListener(final AutoReconnectListener listener) {
		if (listener != null) {
			this._listeners.add(listener);
			listener.stateChanged(this._state);
		}
	}

	public void removeListener(final AutoReconnectListener listener) {
		this._listeners.remove(listener);
	}

	protected void notifyStateChange(final AutoReconnectState state) {
		this._state = state;
		for (AutoReconnectListener listener : this._listeners) {
			listener.stateChanged(state);
		}
	}

	public int getDelay() {
		return this._delay;
	}

	/**
	 * Set the reconnect delay. If the delay less than or equal to zero it will be
	 * the default delay time.
	 *
	 * @param delay The delay to use
	 */
	public void setDelay(int delay) {
		if (delay <= 0) {
			delay = DEFAULT_DELAY;
		}
		this._delay = delay;
	}

	public synchronized void connect() {
		if (isRequested()) {
			return;
		}

		log.debug("Requesting connection");
		notifyStateChange(AutoReconnectState.DISCONNECTED);

		triggerReconnect(false);
	}

	public synchronized void disconnect() {
		if (!isRequested()) {
			return;
		}

		log.debug("Un-Requesting connection");

		notifyStateChange(AutoReconnectState.DISABLED);
		this._server.disconnect();
	}

	public boolean isRequested() {
		return this._state != AutoReconnectState.DISABLED;
	}

	public synchronized void connectionStateChanged(final boolean connected) {
		log.debug("Connection state changed: " + connected);

		if (!connected) {
			if (isRequested()) {
				notifyStateChange(AutoReconnectState.DISCONNECTED);
				triggerReconnect(true);
			}
		} else {
			if (!isRequested()) {
				this._server.disconnect();
			} else {
				notifyStateChange(AutoReconnectState.CONNECTED);
			}
		}
	}

	private synchronized void triggerReconnect(final boolean wait) {
		if (this._connectTask != null) {
			log.info("Connect thread already running");
			return;
		}

		log.debug("Trigger reconnect");

		this._connectTask = new Thread(new Runnable() {

			public void run() {
				boolean result = false;
				try {
					result = performReconnect(wait);
				} finally {
					AutoReconnectController.this._connectTask = null;
					log.debug(String.format("performReconnect completed : %s", result));
					if (!result) {
						triggerReconnect(true);
					}
				}
			}
		}, "OPCReconnectThread");
		this._connectTask.setDaemon(true);
		this._connectTask.start();
	}

	private boolean performReconnect(final boolean wait) {
		try {
			if (wait) {
				notifyStateChange(AutoReconnectState.WAITING);
				log.debug(String.format("Delaying (%s)...", this._delay));
				Thread.sleep(this._delay);
			}
		} catch (InterruptedException e) {
		}

		if (!isRequested()) {
			log.debug("Request canceled during delay");
			return true;
		}

		try {
			log.debug("Connecting to server");
			notifyStateChange(AutoReconnectState.CONNECTING);
			synchronized (this) {
				this._server.connect();
				return true;
			}
			// CONNECTED state will be set by server callback
		} catch (Throwable e) {
			log.info("Re-connect failed", e);
			notifyStateChange(AutoReconnectState.DISCONNECTED);
			return false;
		}
	}

}
