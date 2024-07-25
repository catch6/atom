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

package org.openscada.opc.lib.da;

import lombok.extern.slf4j.Slf4j;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.common.impl.OPCCommon;

import java.util.HashMap;
import java.util.Map;

/**
 * An error message resolver that will lookup the error code using the
 * server interface and will cache the result locally.
 *
 * @author Jens Reimann
 */
@Slf4j
public class ErrorMessageResolver {

	private OPCCommon _opcCommon = null;

	private final Map<Integer, String> _messageCache = new HashMap<Integer, String>();

	private int _localeId = 0;

	public ErrorMessageResolver(final OPCCommon opcCommon, final int localeId) {
		super();
		this._opcCommon = opcCommon;
		this._localeId = localeId;
	}

	/**
	 * Get an error message from an error code
	 *
	 * @param errorCode The error code to look up
	 * @return the error message or <code>null</code> if no message could be looked up
	 */
	public synchronized String getMessage(final int errorCode) {
		String message = this._messageCache.get(Integer.valueOf(errorCode));

		if (message == null) {
			try {
				message = this._opcCommon.getErrorString(errorCode, this._localeId);
				log.info(String.format("Resolved %08X to '%s'", errorCode, message));
			} catch (JIException e) {
				log.warn(String.format("Failed to resolve error code for %08X", errorCode), e);
			}
			if (message != null) {
				this._messageCache.put(errorCode, message);
			}
		}
		return message;
	}

}
