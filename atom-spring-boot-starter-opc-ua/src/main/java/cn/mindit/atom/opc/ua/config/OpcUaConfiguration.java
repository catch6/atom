package cn.mindit.atom.opc.ua.config;

import cn.mindit.atom.core.util.JsonUtils;
import cn.mindit.atom.opc.ua.OpcUaConsumer;
import cn.mindit.atom.opc.ua.OpcUaConsumerProcessor;
import cn.mindit.atom.opc.ua.OpcUaSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Catch
 * @since 2024-08-05
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class OpcUaConfiguration implements ApplicationListener<ApplicationStartedEvent>, Ordered, DisposableBean {

    private static final long DISCONNECT_TIMEOUT_SECONDS = 3L;

    private final OpcUaProperties opcUaProperties;
    private final List<OpcUaSubscriber> opcUaSubscribers;
    private final List<OpcUaClient> managedClients = new ArrayList<>();

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        List<OpcUaProperties.OpcUaInstance> instances = opcUaProperties.getInstances();
        if (instances == null || instances.isEmpty()) {
            return;
        }

        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        Map<String, List<OpcUaConsumer>> consumerMap = OpcUaConsumerProcessor.processConsumerMap(applicationContext, opcUaProperties, opcUaSubscribers);

        Path securityTempDir;
        if (opcUaProperties.getCertificatePath() == null) {
            securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "security");
        } else {
            securityTempDir = Paths.get(opcUaProperties.getCertificatePath());
        }
        try {
            Files.createDirectories(securityTempDir);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create security dir: " + securityTempDir, e);
        }

        for (OpcUaProperties.OpcUaInstance instance : instances) {
            if (!instance.getEnabled()) {
                continue;
            }
            try {
                IdentityProvider identityProvider;
                if (instance.getUsername() == null) {
                    identityProvider = new AnonymousProvider();
                } else {
                    identityProvider = new UsernameProvider(instance.getUsername(), instance.getPassword());
                }
                OpcUaClient opcUaClient = OpcUaClient.create(instance.getUrl(),
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
                opcUaClient.connect().get(DISCONNECT_TIMEOUT_SECONDS * 2, TimeUnit.SECONDS);
                managedClients.add(opcUaClient);

                List<OpcUaConsumer> consumers = consumerMap.get(instance.getId());
                if (consumers == null || consumers.isEmpty()) {
                    continue;
                }
                ManagedSubscription subscription = ManagedSubscription.create(opcUaClient);
                for (OpcUaConsumer consumer : consumers) {
                    String[] items = consumer.getItems();
                    if (items == null || items.length == 0) {
                        continue;
                    }
                    int[] namespaceIndices = consumer.getNamespaceIndices();
                    for (int i = 0; i < items.length; i++) {
                        int namespaceIndex = namespaceIndices[i];
                        String item = items[i];
                        NodeId nodeId = new NodeId(namespaceIndex, item);
                        ManagedDataItem dataItem = subscription.createDataItem(nodeId);
                        dataItem.addDataValueListener(dataValue -> {
                            if (log.isDebugEnabled()) {
                                log.debug("OPC UA consumer item:{}, value:{}", item, dataValue);
                            }
                            if (dataValue.getStatusCode() != null && dataValue.getStatusCode().isGood()) {
                                String value = JsonUtils.toJson(dataValue.getValue().getValue());
                                consumer.getConsumer().accept(item, value);
                            }
                        });
                    }
                }
                beanFactory.registerSingleton(OpcUaProperties.CLIENT_BEAN_PREFIX + instance.getId(), opcUaClient);
            } catch (Exception e) {
                throw new RuntimeException("OPC UA connect error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public int getOrder() {
        return opcUaProperties.getOrder();
    }

    @Override
    public void destroy() {
        for (OpcUaClient client : managedClients) {
            try {
                client.disconnect().get(DISCONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.warn("Failed to disconnect OPC UA client", e);
            }
        }
        managedClients.clear();
    }

}
