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
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;

import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author Catch
 * @since 2024-07-24
 */
@Slf4j
public abstract class WriteableAccessBase extends AccessBase {

	public WriteableAccessBase(Server server, int period) throws IllegalArgumentException, UnknownHostException, NotConnectedException, JIException, DuplicateGroupException {
		super(server, period);
	}

	public WriteableAccessBase(Server server, int period, String logTag) {
		super(server, period, logTag);
	}

	public void updateItem(String item, Object value) {
		updateItem(item, value, false);
	}

	public void updateItem(String item, Object value, boolean isByRef) {
		Item it = this.itemMap.get(item);
		if (it == null) {
			return;
		}
		try {
			JIVariant val = JIVariant.makeVariant(value, isByRef);
			it.write(val);
			DataCallback dataCallback = this.items.get(it);
			if (dataCallback == null) {
				return;
			}
			ItemState cachedState = this.itemCache.get(it);
			if (cachedState == null) {
				cachedState = new ItemState();
				cachedState.setValue(val);
				this.itemCache.put(it, cachedState);
				dataCallback.changed(it, cachedState);
				return;
			}
			if (!Objects.equals(cachedState.getValue(), val)) {
				cachedState.setValue(val);
				this.itemCache.put(it, cachedState);
				dataCallback.changed(it, cachedState);
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("Failed to update item '%s'", item), e);
		}
	}

}
