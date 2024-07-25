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

package org.openscada.opc.dcom.common.impl;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.*;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

public class OPCCommon extends BaseCOMObject {

	public OPCCommon(final IJIComObject opcObject) throws IllegalArgumentException, UnknownHostException, JIException {
		super(opcObject.queryInterface(org.openscada.opc.dcom.common.Constants.IOPCCommon_IID));
	}

	public void setLocaleID(final int localeID) throws JIException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(0);

		callObject.addInParamAsInt(localeID, JIFlags.FLAG_NULL);

		getCOMObject().call(callObject);
	}

	public int getLocaleID() throws JIException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(1);

		callObject.addOutParamAsObject(Integer.class, JIFlags.FLAG_NULL);

		Object[] result = getCOMObject().call(callObject);
		return (Integer) result[0];
	}

	public String getErrorString(final int errorCode, final int localeID) throws JIException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(3);

		callObject.addInParamAsInt(errorCode, JIFlags.FLAG_NULL);
		callObject.addInParamAsInt(localeID, JIFlags.FLAG_NULL);
		callObject.addOutParamAsObject(new JIPointer(new JIString(JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR)), JIFlags.FLAG_NULL);

		Object[] result = getCOMObject().call(callObject);
		return ((JIString) ((JIPointer) result[0]).getReferent()).getString();
	}

	public void setClientName(final String clientName) throws JIException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(4);

		callObject.addInParamAsString(clientName, JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR);

		getCOMObject().call(callObject);
	}

	public Collection<Integer> queryAvailableLocaleIDs() throws JIException {
		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(2);

		callObject.addOutParamAsType(Integer.class, JIFlags.FLAG_NULL);
		callObject.addOutParamAsObject(new JIPointer(new JIArray(Integer.class, null, 1, true)), JIFlags.FLAG_NULL);

		Object[] result = getCOMObject().call(callObject);

		JIArray resultArray = (JIArray) ((JIPointer) result[1]).getReferent();
		Integer[] intArray = (Integer[]) resultArray.getArrayInstance();

		return Arrays.asList(intArray);
	}

}
