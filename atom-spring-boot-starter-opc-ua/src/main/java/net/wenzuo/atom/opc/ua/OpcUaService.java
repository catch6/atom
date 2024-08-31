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

package net.wenzuo.atom.opc.ua;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.core.util.JsonUtils;
import net.wenzuo.atom.opc.ua.config.OpcUaProperties;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Catch
 * @since 2024-08-05
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OpcUaService {

	private final ApplicationContext applicationContext;
	private final OpcUaProperties opcUaProperties;

	public String readItem(int namespaceIndex, String item) {
		return readItem(opcUaProperties.getId(), namespaceIndex, item);
	}

	public String readItem(String id, int namespaceIndex, String item) {
		OpcUaClient client = applicationContext.getBean(OpcUaProperties.CLIENT_BEAN_PREFIX + id, OpcUaClient.class);
		NodeId nodeId = new NodeId(namespaceIndex, item);
		try {
			DataValue dataValue = client.readValue(0.0, TimestampsToReturn.Neither, nodeId).get();
			return JsonUtils.toJson(dataValue.getValue().getValue());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void writeItem(int namespaceIndex, String item, Object value) {
		writeItem(opcUaProperties.getId(), namespaceIndex, item, value);
	}

	public void writeItem(String id, int namespaceIndex, String item, Object value) {
		OpcUaClient client = applicationContext.getBean(OpcUaProperties.CLIENT_BEAN_PREFIX + id, OpcUaClient.class);
		NodeId nodeId = new NodeId(namespaceIndex, item);
		DataValue newValue = new DataValue(new Variant(value));
		client.writeValue(nodeId, newValue);
	}

}
