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
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JICallBuilder;
import org.jinterop.dcom.core.JIFlags;
import org.jinterop.dcom.core.JIVariant;

public class Helper {

	/**
	 * Make the COM call but do not treat S_FALSE as error condition for the whole call
	 *
	 * @param object     the object to make to call on
	 * @param callObject the call object
	 * @return the result of the call
	 * @throws JIException
	 */
	public static Object[] callRespectSFALSE(final IJIComObject object, final JICallBuilder callObject) throws JIException {
		try {
			return object.call(callObject);
		} catch (JIException e) {
			if (e.getErrorCode() != org.openscada.opc.dcom.common.Constants.S_FALSE) {
				throw e;
			}
			return callObject.getResultsInCaseOfException();
		}
	}

	/**
	 * Perform some fixes on the variant when writing it to OPC items. This method
	 * only changes control information on the variant and not the value itself!
	 *
	 * @param value the value to fix
	 * @return the fixed value
	 * @throws JIException In case something goes wrong
	 */
	public static JIVariant fixVariant(final JIVariant value) throws JIException {
		if (value.isArray()) {
			if (value.getObjectAsArray().getArrayInstance() instanceof Boolean[]) {
				value.setFlag(JIFlags.FLAG_REPRESENTATION_VARIANT_BOOL);
			}
		}
		return value;
	}

}
