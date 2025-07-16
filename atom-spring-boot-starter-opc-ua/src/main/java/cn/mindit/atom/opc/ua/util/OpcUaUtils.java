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

package cn.mindit.atom.opc.ua.util;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Catch
 * @since 2024-08-31
 */
@Slf4j
public class OpcUaUtils {

    private OpcUaUtils() {
    }

    // public static void main(String[] args) {
    // 	// OpcUaUtils.showItemList("opc.tcp://opcuaserver.com:48010", null, null);
    // 	OpcUaUtils.showItemTree("opc.tcp://opcuaserver.com:48010", null, null);
    // }

    public static OpcUaClient getClient(String url, String username, String password) {
        Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "security");
        try {
            Files.createDirectories(securityTempDir);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create security dir: " + securityTempDir, e);
        }
        IdentityProvider identityProvider;
        if (username == null) {
            identityProvider = new AnonymousProvider();
        } else {
            identityProvider = new UsernameProvider(username, password);
        }
        try {
            OpcUaClient opcUaClient = OpcUaClient.create(url,
                endpoints -> endpoints
                    .stream()
                    .findFirst(),
                configBuilder -> configBuilder
                    .setApplicationName(LocalizedText.english("Atom Opc Ua Client"))
                    .setApplicationUri("urn:atom:opc:ua:client")
                    .setIdentityProvider(identityProvider)
                    .setRequestTimeout(UInteger.valueOf(5000))
                    .build()
            );
            opcUaClient.connect().get();
            return opcUaClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void showItemList(String url, String username, String password) {
        showItemList(getClient(url, username, password));
    }

    public static void showItemList(OpcUaClient client) {
        showItemList(client, Identifiers.RootFolder);
    }

    public static void showItemList(OpcUaClient client, NodeId nodeId) {
        showItemList(client, nodeId, "");
    }

    private static void showItemList(OpcUaClient client, NodeId nodeId, String prefix) {
        try {
            List<? extends UaNode> nodes = client.getAddressSpace().browseNodes(nodeId);
            for (UaNode node : nodes) {
                QualifiedName browseName = node.getBrowseName();
                String name = prefix.isEmpty() ? browseName.getName() : prefix + "." + browseName.getName();
                String namespaceIndex = browseName.getNamespaceIndex().toString();
                log.info("{}[{}]", name, namespaceIndex);
                showItemList(client, node.getNodeId(), name);
            }
        } catch (Exception e) {
            log.error("Browsing nodeId={} failed: {}", nodeId, e.getMessage(), e);
        }
    }

    public static void showItemTree(String url, String username, String password) {
        showItemTree(getClient(url, username, password));
    }

    public static void showItemTree(OpcUaClient client) {
        showItemTree(client, Identifiers.RootFolder);
    }

    public static void showItemTree(OpcUaClient client, NodeId nodeId) {
        showItemTree(client, nodeId, "");
    }

    private static void showItemTree(OpcUaClient client, NodeId nodeId, String prefix) {
        try {
            List<? extends UaNode> nodes = client.getAddressSpace().browseNodes(nodeId);
            for (int i = 0; i < nodes.size(); i++) {
                UaNode node = nodes.get(i);
                QualifiedName browseName = node.getBrowseName();
                String name;
                if (i < nodes.size() - 1) {
                    name = prefix + "├── " + browseName.getName();
                } else {
                    name = prefix + "└── " + browseName.getName();
                }
                String namespaceIndex = browseName.getNamespaceIndex().toString();
                log.info("{}[{}]", name, namespaceIndex);
                showItemTree(client, node.getNodeId(), prefix + "│   ");
            }
        } catch (Exception e) {
            log.error("Browsing nodeId={} failed: {}", nodeId, e.getMessage(), e);
        }
    }

}
