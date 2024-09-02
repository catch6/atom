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

package org.openscada.opc.lib.da.browser;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.da.OPCBROWSETYPE;
import org.openscada.opc.dcom.da.impl.OPCBrowseServerAddressSpace;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.EnumSet;

/**
 * Browse through the flat server namespace
 *
 * @author Jens Reimann jens.reimann@th4-systems.com
 */
public class FlatBrowser extends BaseBrowser {

	public FlatBrowser(final OPCBrowseServerAddressSpace browser) {
		super(browser);
	}

	public FlatBrowser(final OPCBrowseServerAddressSpace browser, final int batchSize) {
		super(browser, batchSize);
	}

	/**
	 * Perform a flat browse operation
	 *
	 * @param filterCriteria The filter criteria. Use an empty string if you don't need one.
	 * @param accessMask     The access mask. An empty set will search for all.
	 * @param variantType    The variant type. Must be one of the <code>VT_</code> constants of {@link JIVariant}. Use {@link JIVariant#VT_EMPTY} if you want to browse for all.
	 * @return The list of entries
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 * @throws JIException
	 */
	public Collection<String> browse(final String filterCriteria, final EnumSet<Access> accessMask, final int variantType) throws IllegalArgumentException, UnknownHostException, JIException {
		return browse(OPCBROWSETYPE.OPC_FLAT, filterCriteria, accessMask, variantType);
	}

	public Collection<String> browse(final String filterCriteria) throws IllegalArgumentException, UnknownHostException, JIException {
		return browse(filterCriteria, EnumSet.noneOf(Access.class), JIVariant.VT_EMPTY);
	}

	public Collection<String> browse() throws IllegalArgumentException, UnknownHostException, JIException {
		return browse("", EnumSet.noneOf(Access.class), JIVariant.VT_EMPTY);
	}

	public Collection<String> browse(final EnumSet<Access> accessMask) throws IllegalArgumentException, UnknownHostException, JIException {
		return browse("", accessMask, JIVariant.VT_EMPTY);
	}

}
