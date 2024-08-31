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

import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

import java.util.List;

/**
 * @author Catch
 * @since 2024-08-31
 */
@Slf4j
public class OpcUaUtils {

	public static void listNode(OpcUaClient client) {
		listNode(client, Identifiers.RootFolder, "");
	}

	public static void listNode(OpcUaClient client, NodeId nodeId, String prefix) {
		try {
			List<? extends UaNode> nodes = client.getAddressSpace().browseNodes(nodeId);
			for (UaNode node : nodes) {
				QualifiedName browseName = node.getBrowseName();
				String name = prefix.isEmpty() ? browseName.getName() : prefix + "." + browseName.getName();
				String namespaceIndex = browseName.getNamespaceIndex().toString();
				log.info("{}[{}]", name, namespaceIndex);
				listNode(client, node.getNodeId(), name);
			}
		} catch (Exception e) {
			log.error("Browsing nodeId={} failed: {}", nodeId, e.getMessage(), e);
		}
	}

	public static void treeNode(OpcUaClient client) {
		treeNode(client, Identifiers.RootFolder, "");
	}

	public static void treeNode(OpcUaClient client, NodeId nodeId, String prefix) {
		try {
			List<? extends UaNode> nodes = client.getAddressSpace().browseNodes(nodeId);
			for (int i = 0; i < nodes.size(); i++) {
				UaNode node = nodes.get(i);
				QualifiedName browseName = node.getBrowseName();
				String name;
				if (prefix.isEmpty()) {
					name = browseName.getName();
				} else if (i < nodes.size() - 1) {
					name = prefix + "├── " + browseName.getName();
				} else {
					name = prefix + "└── " + browseName.getName();
				}
				String namespaceIndex = browseName.getNamespaceIndex().toString();
				log.info("{}[{}]", name, namespaceIndex);
				listNode(client, node.getNodeId(), prefix + "│   " + name);
			}
		} catch (Exception e) {
			log.error("Browsing nodeId={} failed: {}", nodeId, e.getMessage(), e);
		}
	}

}
