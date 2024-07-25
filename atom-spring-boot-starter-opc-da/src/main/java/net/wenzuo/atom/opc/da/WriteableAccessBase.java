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
import net.wenzuo.atom.opc.da.util.OpcDaUtils;
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
		Item cachedItem = this.itemMap.get(item);
		if (cachedItem == null) {
			return;
		}
		try {
			JIVariant jiVariant = OpcDaUtils.getJIVariant(value);
			cachedItem.write(jiVariant);
			DataCallback dataCallback = this.items.get(cachedItem);
			if (dataCallback == null) {
				return;
			}
			ItemState cachedState = this.itemCache.get(cachedItem);
			if (cachedState == null) {
				cachedState = new ItemState();
				cachedState.setValue(jiVariant);
				this.itemCache.put(cachedItem, cachedState);
				dataCallback.changed(cachedItem, cachedState);
				return;
			}
			if (!Objects.equals(cachedState.getValue().getObject(), jiVariant.getObject())) {
				cachedState.setValue(jiVariant);
				this.itemCache.put(cachedItem, cachedState);
				dataCallback.changed(cachedItem, cachedState);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to update item " + item, e);
		}
	}

	public Item getItem(String item) {
		return this.itemMap.get(item);
	}

}
