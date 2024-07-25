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

package org.openscada.opc.dcom.da.impl;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.*;
import org.openscada.opc.dcom.common.impl.BaseCOMObject;
import org.openscada.opc.dcom.common.impl.EnumString;
import org.openscada.opc.dcom.common.impl.Helper;
import org.openscada.opc.dcom.da.Constants;
import org.openscada.opc.dcom.da.OPCBROWSEDIRECTION;
import org.openscada.opc.dcom.da.OPCBROWSETYPE;
import org.openscada.opc.dcom.da.OPCNAMESPACETYPE;

import java.net.UnknownHostException;

/**
 * Implementation for <code>IOPCBrowseServerAddressSpace</code>
 *
 * @author Jens Reimann <jens.reimann@th4-systems.com>
 */
public class OPCBrowseServerAddressSpace extends BaseCOMObject {

	public OPCBrowseServerAddressSpace(final IJIComObject opcServer) throws IllegalArgumentException, UnknownHostException, JIException {
		super(opcServer.queryInterface(Constants.IOPCBrowseServerAddressSpace_IID));
	}

	/**
	 * Get the information how the namespace is organized
	 *
	 * @return the organization of the namespace
	 * @throws JIException
	 */
	public OPCNAMESPACETYPE queryOrganization() throws JIException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(0);

		callObject.addOutParamAsType(Short.class, JIFlags.FLAG_NULL);

		Object result[] = getCOMObject().call(callObject);

		return OPCNAMESPACETYPE.fromID((Short) result[0]);
	}

	/**
	 * Direct the browser to another position
	 * <p>
	 * Depending on the <em>direction</em> the new position will be set based on the provided
	 * position information. If the direction is {@link OPCBROWSEDIRECTION#OPC_BROWSE_TO} then
	 * the <em>position</em> is the item to go to. If the direction is {@link OPCBROWSEDIRECTION#OPC_BROWSE_DOWN}
	 * the browser will descent into the tree down (not to) the branch item in <em>position</em>.
	 * Passing {@link OPCBROWSEDIRECTION#OPC_BROWSE_UP} won't need a <em>position</em> (pass <code>null</code>)
	 * and will ascent in the tree one level.
	 * <p>
	 * Passing {@link OPCBROWSEDIRECTION#OPC_BROWSE_TO} and <code>null</code> as position will
	 * go to the first root entry of the namespace.
	 *
	 * @param position  The item position reference for the direction
	 * @param direction The direction to go based on the position
	 * @throws JIException
	 */
	public void changePosition(final String position, final OPCBROWSEDIRECTION direction) throws JIException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(1);

		callObject.addInParamAsShort((short) direction.id(), JIFlags.FLAG_NULL);
		callObject.addInParamAsString(position, JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR);

		getCOMObject().call(callObject);

	}

	public EnumString browse(final OPCBROWSETYPE browseType, final String filterCriteria, final int accessRights, final int dataType) throws JIException, IllegalArgumentException, UnknownHostException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(2);

		callObject.addInParamAsShort((short) browseType.id(), JIFlags.FLAG_NULL);
		callObject.addInParamAsString(filterCriteria, JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR);
		callObject.addInParamAsShort((short) dataType, JIFlags.FLAG_NULL);
		callObject.addInParamAsInt(accessRights, JIFlags.FLAG_NULL);
		callObject.addOutParamAsType(IJIComObject.class, JIFlags.FLAG_NULL);

		Object result[] = Helper.callRespectSFALSE(getCOMObject(), callObject);

		return new EnumString((IJIComObject) result[0]);
	}

	/**
	 * Return the possible access paths for an item
	 *
	 * @param itemID the item to query
	 * @return A string enumerator for the possible access paths
	 * @throws JIException
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 */
	public EnumString browseAccessPaths(final String itemID) throws JIException, IllegalArgumentException, UnknownHostException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(4);

		callObject.addInParamAsString(itemID, JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR);
		callObject.addOutParamAsType(IJIComObject.class, JIFlags.FLAG_NULL);

		Object[] result = Helper.callRespectSFALSE(getCOMObject(), callObject);

		return new EnumString((IJIComObject) result[0]);
	}

	/**
	 * Get the complete item id from an item at the local position.
	 * <p>
	 * Browsing a hierarchical namespace the browse method will return items based on the
	 * local level in the namespace. So actually only the last part of the item ID hierarchy
	 * is returned. In order to convert this to the full item ID one can use this method. It
	 * will only work if the browser is still at the position in question.
	 *
	 * @param item the local item
	 * @return the complete item ID
	 * @throws JIException
	 */
	public String getItemID(final String item) throws JIException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(3);

		callObject.addInParamAsString(item, JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR);
		callObject.addOutParamAsObject(new JIPointer(new JIString(JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR)), JIFlags.FLAG_NULL);

		Object[] result = getCOMObject().call(callObject);

		return ((JIString) ((JIPointer) result[0]).getReferent()).getString();
	}

}
