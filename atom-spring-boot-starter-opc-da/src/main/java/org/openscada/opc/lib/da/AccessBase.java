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
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public abstract class AccessBase implements ServerConnectionStateListener {

	protected Server server = null;

	protected Group group = null;

	protected boolean active = false;

	private final List<AccessStateListener> stateListeners = new CopyOnWriteArrayList<AccessStateListener>();

	private boolean bound = false;

	/**
	 * Holds the item to callback assignment
	 */
	protected Map<Item, DataCallback> items = new HashMap<Item, DataCallback>();

	protected Map<String, Item> itemMap = new HashMap<String, Item>();

	protected Map<Item, ItemState> itemCache = new HashMap<Item, ItemState>();

	private int period = 0;

	protected Map<String, DataCallback> itemSet = new HashMap<String, DataCallback>();

	protected String logTag = null;

	protected Logger dataLogger = null;

	public AccessBase(final Server server, final int period) throws IllegalArgumentException, UnknownHostException, NotConnectedException, JIException, DuplicateGroupException {
		super();
		this.server = server;
		this.period = period;
	}

	public AccessBase(final Server server, final int period, final String logTag) {
		super();
		this.server = server;
		this.period = period;
		this.logTag = logTag;
		if (this.logTag != null) {
			this.dataLogger = LoggerFactory.getLogger("opc.data." + logTag);
		}
	}

	public boolean isBound() {
		return this.bound;
	}

	public synchronized void bind() {
		if (isBound()) {
			return;
		}

		this.server.addStateListener(this);
		this.bound = true;
	}

	public synchronized void unbind() throws JIException {
		if (!isBound()) {
			return;
		}

		this.server.removeStateListener(this);
		this.bound = false;

		stop();
	}

	public boolean isActive() {
		return this.active;
	}

	public void addStateListener(final AccessStateListener listener) {
		this.stateListeners.add(listener);
		listener.stateChanged(isActive());
	}

	public void removeStateListener(final AccessStateListener listener) {
		this.stateListeners.remove(listener);
	}

	protected void notifyStateListenersState(final boolean state) {
		final List<AccessStateListener> list = new ArrayList<AccessStateListener>(this.stateListeners);

		for (final AccessStateListener listener : list) {
			listener.stateChanged(state);
		}
	}

	protected void notifyStateListenersError(final Throwable t) {
		final List<AccessStateListener> list = new ArrayList<AccessStateListener>(this.stateListeners);

		for (final AccessStateListener listener : list) {
			listener.errorOccured(t);
		}
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

	public synchronized void removeItem(final String itemId) {
		if (!this.itemSet.containsKey(itemId)) {
			return;
		}

		this.itemSet.remove(itemId);

		if (isActive()) {
			unrealizeItem(itemId);
		}
	}

	public void connectionStateChanged(final boolean connected) {
		try {
			if (connected) {
				start();
			} else {
				stop();
			}
		} catch (final Exception e) {
			log.error(String.format("Failed to change state (%s)", connected), e);
		}
	}

	protected synchronized void start() throws JIException, IllegalArgumentException, UnknownHostException, NotConnectedException, DuplicateGroupException {
		if (isActive()) {
			return;
		}

		log.debug("Create a new group");
		this.group = this.server.addGroup();
		this.group.setActive(true);
		this.active = true;

		notifyStateListenersState(true);

		realizeAll();
	}

	protected void realizeItem(final String itemId) throws JIException, AddFailedException {
		log.debug("Realizing item: {}", itemId);

		final DataCallback dataCallback = this.itemSet.get(itemId);
		if (dataCallback == null) {
			return;
		}

		final Item item = this.group.addItem(itemId);
		this.items.put(item, dataCallback);
		this.itemMap.put(itemId, item);
	}

	protected void unrealizeItem(final String itemId) {
		final Item item = this.itemMap.remove(itemId);
		this.items.remove(item);
		this.itemCache.remove(item);

		try {
			this.group.removeItem(itemId);
		} catch (final Throwable e) {
			log.error(String.format("Failed to unrealize item '%s'", itemId), e);
		}
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
				log.warn(String.format("Failed to add item: %s (%08X)", itemId, rc));

			} catch (final Exception e) {
				log.warn("Failed to realize item: " + itemId, e);
			}
		}
	}

	protected void unrealizeAll() {
		this.items.clear();
		this.itemCache.clear();
		try {
			this.group.clear();
		} catch (final JIException e) {
			log.info("Failed to clear group. No problem if we already lost the connection", e);
		}
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
			log.warn("Failed to disable group. No problem if we already lost connection");
		}
		this.group = null;
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

}