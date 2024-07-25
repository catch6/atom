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

import lombok.extern.slf4j.Slf4j;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.common.impl.EnumString;
import org.openscada.opc.dcom.da.OPCBROWSETYPE;
import org.openscada.opc.dcom.da.impl.OPCBrowseServerAddressSpace;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.EnumSet;

/**
 * A class implementing base browsing
 *
 * @author Jens Reimann
 */
@Slf4j
public class BaseBrowser {

	protected OPCBrowseServerAddressSpace _browser;

	/**
	 * The batch size is the number of entries that will be requested with one call
	 * from the server. Sometimes too big batch sizes will cause an exception. And
	 * smaller batch sizes degrade perfomance. The default is set by {@link EnumString#DEFAULT_BATCH_SIZE}
	 * and can be overridden by the java property <q>openscada.dcom.enum-batch-size</q>.
	 */
	protected int _batchSize;

	public BaseBrowser(final OPCBrowseServerAddressSpace browser) {
		this(browser, EnumString.DEFAULT_BATCH_SIZE);
	}

	public BaseBrowser(final OPCBrowseServerAddressSpace browser, final int batchSize) {
		super();
		this._browser = browser;
		this._batchSize = batchSize;
	}

	/**
	 * Set the batch size
	 *
	 * @param batchSize The new batch size
	 */
	public void setBatchSize(final int batchSize) {
		this._batchSize = batchSize;
	}

	/**
	 * Get the batch size
	 *
	 * @return the current batch size
	 */
	public int getBatchSize() {
		return this._batchSize;
	}

	/**
	 * Perform the browse operation.
	 *
	 * @param type
	 * @param filterCriteria
	 * @param accessMask
	 * @param variantType
	 * @return The browse result
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 * @throws JIException
	 */
	protected Collection<String> browse(final OPCBROWSETYPE type, final String filterCriteria, final EnumSet<Access> accessMask, final int variantType) throws IllegalArgumentException, UnknownHostException, JIException {
		int accessMaskValue = 0;

		if (accessMask.contains(Access.READ)) {
			accessMaskValue |= Access.READ.getCode();
		}
		if (accessMask.contains(Access.WRITE)) {
			accessMaskValue |= Access.WRITE.getCode();
		}

		log.debug("Browsing with a batch size of " + this._batchSize);

		return this._browser.browse(type, filterCriteria, accessMaskValue, variantType).asCollection(this._batchSize);
	}

	/**
	 * Browse the access paths for one item.
	 *
	 * @param itemId The item ID to look up the access paths
	 * @return The collection of the access paths
	 * @throws JIException
	 * @throws UnknownHostException
	 * @throws IllegalArgumentException
	 */
	public Collection<String> getAccessPaths(final String itemId) throws IllegalArgumentException, UnknownHostException, JIException {
		return this._browser.browseAccessPaths(itemId).asCollection(this._batchSize);
	}

}