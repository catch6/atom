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

package org.openscada.opc.lib.list;

public interface Categories {

	/**
	 * Category of the OPC DA 1.0 Servers
	 */
	public final static Category OPCDAServer10 = new Category(org.openscada.opc.dcom.common.Categories.OPCDAServer10);

	/**
	 * Category of the OPC DA 2.0 Servers
	 */
	public final static Category OPCDAServer20 = new Category(org.openscada.opc.dcom.common.Categories.OPCDAServer20);

	/**
	 * Category of the OPC DA 3.0 Servers
	 */
	public final static Category OPCDAServer30 = new Category(org.openscada.opc.dcom.common.Categories.OPCDAServer30);

	/**
	 * Category of the XML DA 1.0 Servers
	 */
	public final static Category XMLDAServer10 = new Category(org.openscada.opc.dcom.common.Categories.XMLDAServer10);

}
