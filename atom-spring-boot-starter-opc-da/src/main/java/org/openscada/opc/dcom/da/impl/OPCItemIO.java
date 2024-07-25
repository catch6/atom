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
import org.openscada.opc.dcom.common.FILETIME;
import org.openscada.opc.dcom.common.impl.BaseCOMObject;
import org.openscada.opc.dcom.da.Constants;
import org.openscada.opc.dcom.da.IORequest;

import java.net.UnknownHostException;

public class OPCItemIO extends BaseCOMObject {

	public OPCItemIO(final IJIComObject opcItemIO) throws IllegalArgumentException, UnknownHostException, JIException {
		super(opcItemIO.queryInterface(Constants.IOPCItemIO_IID));
	}

	public void read(final IORequest[] requests) throws JIException {
		if (requests.length == 0) {
			return;
		}

		JICallBuilder callObject = new JICallBuilder(true);
		callObject.setOpnum(0);

		JIString itemIDs[] = new JIString[requests.length];
		Integer maxAges[] = new Integer[requests.length];
		for (int i = 0; i < requests.length; i++) {
			itemIDs[i] = new JIString(requests[i].getItemID(), JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR);
			maxAges[i] = new Integer(requests[i].getMaxAge());
		}

		callObject.addInParamAsInt(requests.length, JIFlags.FLAG_NULL);
		callObject.addInParamAsArray(new JIArray(itemIDs, true), JIFlags.FLAG_NULL);
		callObject.addInParamAsArray(new JIArray(maxAges, true), JIFlags.FLAG_NULL);

		callObject.addOutParamAsObject(new JIPointer(new JIArray(JIVariant.class, null, 1, true)), JIFlags.FLAG_NULL);
		callObject.addOutParamAsObject(new JIPointer(new JIArray(Integer.class, null, 1, true)), JIFlags.FLAG_NULL);
		callObject.addOutParamAsObject(new JIPointer(new JIArray(FILETIME.getStruct(), null, 1, true)), JIFlags.FLAG_NULL);
		callObject.addOutParamAsObject(new JIPointer(new JIArray(Integer.class, null, 1, true)), JIFlags.FLAG_NULL);

		getCOMObject().call(callObject);
	}

}
