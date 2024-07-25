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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ServerStateReader {

	private Server _server = null;

	private ScheduledExecutorService _scheduler = null;

	private final List<ServerStateListener> _listeners = new CopyOnWriteArrayList<ServerStateListener>();

	private ScheduledFuture<?> _job = null;

	public ServerStateReader(final Server server) {
		super();
		this._server = server;
		this._scheduler = this._server.getScheduler();
	}

	/**
	 * Create a new server state reader. Please note that the scheduler might get
	 * blocked for a short period of time in case of a connection failure!
	 *
	 * @param server    the server to check
	 * @param scheduler the scheduler to use
	 */
	public ServerStateReader(final Server server, final ScheduledExecutorService scheduler) {
		super();
		this._server = server;
		this._scheduler = scheduler;
	}

	public synchronized void start() {
		if (this._job != null) {
			return;
		}

		this._job = this._scheduler.scheduleAtFixedRate(new Runnable() {

			public void run() {
				once();
			}
		}, 1000, 1000, TimeUnit.MILLISECONDS);
	}

	public synchronized void stop() {
		this._job.cancel(false);
		this._job = null;
	}

	protected void once() {
		log.debug("Reading server state");

		final OPCSERVERSTATUS state = this._server.getServerState();

		for (final ServerStateListener listener : new ArrayList<ServerStateListener>(this._listeners)) {
			listener.stateUpdate(state);
		}
	}

	public void addListener(final ServerStateListener listener) {
		this._listeners.add(listener);
	}

	public void removeListener(final ServerStateListener listener) {
		this._listeners.remove(listener);
	}

}
