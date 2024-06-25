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

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.common.EventHandler;
import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.ResultSet;
import org.openscada.opc.dcom.da.IOPCDataCallback;
import org.openscada.opc.dcom.da.OPCDATASOURCE;
import org.openscada.opc.dcom.da.ValueData;
import org.openscada.opc.dcom.da.impl.OPCAsyncIO2;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * @author Catch
 * @since 2024-06-22
 */
public class Async20Access extends AccessBase implements IOPCDataCallback {

	private static final Logger logger = LoggerFactory.getLogger(org.openscada.opc.lib.da.Async20Access.class);

	private EventHandler eventHandler = null;

	private boolean initialRefresh = false;

	public Async20Access(final Server server, final int period, final boolean initialRefresh) throws IllegalArgumentException, UnknownHostException, NotConnectedException, JIException, DuplicateGroupException {
		super(server, period);
		this.initialRefresh = initialRefresh;
	}

	public Async20Access(final Server server, final int period, final boolean initialRefresh, final String logTag) throws IllegalArgumentException, UnknownHostException, NotConnectedException, JIException, DuplicateGroupException {
		super(server, period, logTag);
		this.initialRefresh = initialRefresh;
	}

	@Override
	protected synchronized void stop() throws JIException {
		if (!isActive()) {
			return;
		}

		if (this.eventHandler != null) {
			try {
				this.eventHandler.detach();
			} catch (final Throwable e) {
				logger.warn("Failed to detach group", e);
			}

			this.eventHandler = null;
		}

		super.stop();
	}

	@Override
	protected synchronized void start() throws JIException, IllegalArgumentException, UnknownHostException, NotConnectedException, DuplicateGroupException {
		if (isActive()) {
			return;
		}

		super.start();

		this.eventHandler = this.group.attach(this);
		if (!this.items.isEmpty() && this.initialRefresh) {
			final OPCAsyncIO2 async20 = this.group.getAsyncIO20();
			if (async20 == null) {
				throw new NotConnectedException();
			}

			this.group.getAsyncIO20().refresh(OPCDATASOURCE.OPC_DS_CACHE, 0);
		}
	}

	public void dataChange(final int transactionId, final int serverGroupHandle, final int masterQuality, final int masterErrorCode, final KeyedResultSet<Integer, ValueData> result) {
		logger.debug("dataChange - transId {}, items: {}", transactionId, result.size());

		final Group group = this.group;
		if (group == null) {
			return;
		}

		for (final KeyedResult<Integer, ValueData> entry : result) {
			final Item item = group.findItemByClientHandle(entry.getKey());
			logger.debug("Update for '{}'", item.getId());
			updateItem(item, new ItemState(entry.getErrorCode(), entry.getValue().getValue(), entry.getValue().getTimestamp(), entry.getValue().getQuality()));
		}
	}

	public void readComplete(final int transactionId, final int serverGroupHandle, final int masterQuality, final int masterErrorCode, final KeyedResultSet<Integer, ValueData> result) {
		logger.debug("readComplete - transId {}", transactionId);
	}

	public void writeComplete(final int transactionId, final int serverGroupHandle, final int masterErrorCode, final ResultSet<Integer> result) {
		logger.debug("writeComplete - transId {}", transactionId);
	}

	public void cancelComplete(final int transactionId, final int serverGroupHandle) {
	}

}

