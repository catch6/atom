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

package net.wenzuo.atom.opc.da;

import lombok.extern.slf4j.Slf4j;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author Catch
 * @since 2024-07-24
 */
@Slf4j
public class WriteableSyncAccess extends WriteableAccessBase implements Runnable {

	private Thread runner = null;

	private Throwable lastError = null;

	public WriteableSyncAccess(final Server server, final int period) throws IllegalArgumentException, UnknownHostException, NotConnectedException, JIException, DuplicateGroupException {
		super(server, period);
	}

	public WriteableSyncAccess(final Server server, final int period, final String logTag) throws IllegalArgumentException, UnknownHostException, NotConnectedException, JIException, DuplicateGroupException {
		super(server, period, logTag);
	}

	public void run() {
		while (this.active) {
			try {
				runOnce();
				if (this.lastError != null) {
					this.lastError = null;
					handleError(null);
				}
			} catch (Throwable e) {
				log.error("Sync read failed", e);
				handleError(e);
				this.server.disconnect();
			}

			try {
				Thread.sleep(getPeriod());
			} catch (InterruptedException e) {
			}
		}
	}

	protected void runOnce() throws JIException {
		if (!this.active || this.group == null) {
			return;
		}

		Map<Item, ItemState> result;

		// lock only this section since we could get into a deadlock otherwise
		// calling updateItem
		synchronized (this) {
			Item[] items = this.items.keySet().toArray(new Item[this.items.size()]);
			result = this.group.read(false, items);
		}

		for (Map.Entry<Item, ItemState> entry : result.entrySet()) {
			updateItem(entry.getKey(), entry.getValue());
		}

	}

	@Override
	protected synchronized void start() throws JIException, IllegalArgumentException, UnknownHostException, NotConnectedException, DuplicateGroupException {
		super.start();

		this.runner = new Thread(this, "UtgardSyncReader");
		this.runner.setDaemon(true);
		this.runner.start();
	}

	@Override
	protected synchronized void stop() throws JIException {
		super.stop();

		this.runner = null;
		this.items.clear();
	}

}
