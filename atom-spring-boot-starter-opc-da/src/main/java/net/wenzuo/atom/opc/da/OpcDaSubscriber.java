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

import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;

/**
 * @author Catch
 * @since 2024-06-25
 */
public interface OpcDaSubscriber {

	/**
	 * 实例 ID
	 */
	default String id() {
		return null;
	}

	/**
	 * 订阅项目
	 */
	String[] items();

	/**
	 * 订阅消息
	 *
	 * @param item      项目
	 * @param itemState 项目状态
	 */
	void message(Item item, ItemState itemState);

}
