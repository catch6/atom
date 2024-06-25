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
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Catch
 * @since 2024-06-22
 */
public abstract class AccessBase implements ServerConnectionStateListener {

	private static final Logger logger = LoggerFactory.getLogger(org.openscada.opc.lib.da.AccessBase.class);
	private final List<AccessStateListener> stateListeners = new CopyOnWriteArrayList<AccessStateListener>();
	private final int period;
	protected Server server;
	protected Group group;
	protected boolean active;
	/**
	 * Holds the item to callback assignment
	 */
	protected Map<Item, DataCallback> items = new HashMap<Item, DataCallback>();

	protected Map<String, Item> itemMap = new HashMap<String, Item>();

	protected Map<Item, ItemState> itemCache = new HashMap<Item, ItemState>();
	protected Map<String, DataCallback> itemSet = new HashMap<String, DataCallback>();
	protected String logTag;
	protected Logger dataLogger;
	private boolean bound;

	public AccessBase(final Server server, final int period) throws IllegalArgumentException, UnknownHostException, NotConnectedException, JIException, DuplicateGroupException {
		this.server = server;
		this.period = period;
	}

	public AccessBase(final Server server, final int period, final String logTag) {
		this.server = server;
		this.period = period;
		this.logTag = logTag;
		if (this.logTag != null) {
			this.dataLogger = LoggerFactory.getLogger("opc.data." + logTag);
		}
	}

	public synchronized void bind() {
		if (isBound()) {
			return;
		}

		this.server.addStateListener(this);
		this.bound = true;
	}

	public boolean isBound() {
		return this.bound;
	}

	public synchronized void unbind() throws JIException {
		if (!isBound()) {
			return;
		}

		this.server.removeStateListener(this);
		this.bound = false;

		stop();
	}

	protected synchronized void stop() throws JIException {
		if (!isActive()) {
			return;
		}

		unrealizeAll();

		this.active = false;
		notifyStateListenersState(false);

		try {
			this.group.remove();
		} catch (final Throwable t) {
			logger.warn("Failed to disable group. No problem if we already lost connection");
		}
		this.group = null;
	}

	public boolean isActive() {
		return this.active;
	}

	protected void unrealizeAll() {
		this.items.clear();
		this.itemCache.clear();
		try {
			this.group.clear();
		} catch (final JIException e) {
			logger.info("Failed to clear group. No problem if we already lost the connection", e);
		}
	}

	protected void notifyStateListenersState(final boolean state) {
		final List<AccessStateListener> list = new ArrayList<AccessStateListener>(this.stateListeners);

		for (final AccessStateListener listener : list) {
			listener.stateChanged(state);
		}
	}

	public void addStateListener(final AccessStateListener listener) {
		this.stateListeners.add(listener);
		listener.stateChanged(isActive());
	}

	public void removeStateListener(final AccessStateListener listener) {
		this.stateListeners.remove(listener);
	}

	public int getPeriod() {
		return this.period;
	}

	public synchronized void addItem(final String itemId, final DataCallback dataCallback) throws JIException, AddFailedException {
		if (this.itemSet.containsKey(itemId)) {
			return;
		}

		this.itemSet.put(itemId, dataCallback);

		if (isActive()) {
			realizeItem(itemId);
		}
	}

	protected void realizeItem(final String itemId) throws JIException, AddFailedException {
		logger.debug("Realizing item: {}", itemId);

		final DataCallback dataCallback = this.itemSet.get(itemId);
		if (dataCallback == null) {
			return;
		}

		final Item item = this.group.addItem(itemId);
		this.items.put(item, dataCallback);
		this.itemMap.put(itemId, item);
	}

	public synchronized void removeItem(final String itemId) {
		if (!this.itemSet.containsKey(itemId)) {
			return;
		}

		this.itemSet.remove(itemId);

		if (isActive()) {
			unrealizeItem(itemId);
		}
	}

	protected void unrealizeItem(final String itemId) {
		final Item item = this.itemMap.remove(itemId);
		this.items.remove(item);
		this.itemCache.remove(item);

		try {
			this.group.removeItem(itemId);
		} catch (final Throwable e) {
			logger.error(String.format("Failed to unrealize item '%s'", itemId), e);
		}
	}

	@Override
	public void connectionStateChanged(final boolean connected) {
		try {
			if (connected) {
				start();
			} else {
				stop();
			}
		} catch (final Exception e) {
			logger.error(String.format("Failed to change state (%s)", connected), e);
		}
	}

	protected synchronized void start() throws JIException, IllegalArgumentException, UnknownHostException, NotConnectedException, DuplicateGroupException {
		if (isActive()) {
			return;
		}

		logger.debug("Create a new group");
		this.group = this.server.addGroup();
		this.group.setActive(true, this.period);
		this.active = true;

		notifyStateListenersState(true);

		realizeAll();
	}

	/*
	 * FIXME: need some perfomance boost: subscribe all in one call
	 */
	protected void realizeAll() {
		for (final String itemId : this.itemSet.keySet()) {
			try {
				realizeItem(itemId);
			} catch (final AddFailedException e) {
				Integer rc = e.getErrors().get(itemId);
				if (rc == null) {
					rc = -1;
				}
				logger.warn(String.format("Failed to add item: %s (%08X)", itemId, rc));

			} catch (final Exception e) {
				logger.warn("Failed to realize item: " + itemId, e);
			}
		}
	}

	public synchronized void clear() {
		this.itemSet.clear();
		this.items.clear();
		this.itemMap.clear();
		this.itemCache.clear();
	}

	protected void updateItem(final Item item, final ItemState itemState) {
		if (this.dataLogger != null) {
			this.dataLogger.debug("Update item: {}, {}", item.getId(), itemState);
		}

		final DataCallback dataCallback = this.items.get(item);
		if (dataCallback == null) {
			return;
		}

		final ItemState cachedState = this.itemCache.get(item);
		if (cachedState == null) {
			this.itemCache.put(item, itemState);
			dataCallback.changed(item, itemState);
		} else {
			if (!cachedState.equals(itemState)) {
				this.itemCache.put(item, itemState);
				dataCallback.changed(item, itemState);
			}
		}
	}

	protected void handleError(final Throwable e) {
		notifyStateListenersError(e);
		this.server.dispose();
	}

	protected void notifyStateListenersError(final Throwable t) {
		final List<AccessStateListener> list = new ArrayList<AccessStateListener>(this.stateListeners);

		for (final AccessStateListener listener : list) {
			listener.errorOccured(t);
		}
	}

	public void writeItem(String itemId, Object value) {
		writeItem(itemId, value, false);
	}

	public void writeItem(String itemId, Object value, boolean isByRef) {
		Item item = this.itemMap.get(itemId);
		if (item == null) {
			return;
		}
		try {
			item.write(JIVariant.makeVariant(value, isByRef));
		} catch (JIException e) {
			logger.error(String.format("Failed to update item '%s'", itemId), e);
		}
	}

}

