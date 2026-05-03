package cn.mindit.atom.mqtt.config;

import cn.mindit.atom.mqtt.MqttConsumer;
import cn.mindit.atom.mqtt.MqttSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author Catch
 * @since 2024-06-26
 */
@Slf4j
@ConditionalOnClass(MqttClient.class)
@Configuration
public class Mqttv3Configuration extends AbstractMqttConfiguration<MqttClient> {

    public Mqttv3Configuration(MqttProperties mqttProperties, List<MqttSubscriber> mqttSubscribers) {
        super(mqttProperties, mqttSubscribers);
    }

    @Override
    protected String version() {
        return "v3";
    }

    @Override
    protected MqttClient createAndConnect(MqttProperties.MqttInstance instance) throws Exception {
        String[] urls = instance.getUrl().split(",");
        MqttClient client = new MqttClient(urls[0], instance.getClientId(), new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(urls);
        if (instance.getUsername() != null) {
            options.setUserName(instance.getUsername());
        }
        if (instance.getPassword() != null) {
            options.setPassword(instance.getPassword().toCharArray());
        }
        options.setAutomaticReconnect(true);
        client.connect(options);
        return client;
    }

    @Override
    protected void subscribe(MqttClient client, MqttConsumer consumer, Executor executor) throws Exception {
        String[] topics = consumer.getTopics();
        int[] qos = consumer.getQos();
        IMqttMessageListener[] listeners = new IMqttMessageListener[topics.length];
        for (int i = 0; i < topics.length; i++) {
            listeners[i] = createListener(consumer, executor);
        }
        client.subscribe(topics, qos, listeners);
    }

    private IMqttMessageListener createListener(MqttConsumer consumer, Executor executor) {
        if (executor != null) {
            return (topic, message) -> {
                String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                executor.execute(() -> consumer.getConsumer().accept(topic, payload));
            };
        }
        return (topic, message) -> consumer.getConsumer().accept(topic, new String(message.getPayload(), StandardCharsets.UTF_8));
    }

    @Override
    protected String clientId(MqttClient client) {
        return client.getClientId();
    }

    @Override
    protected boolean isConnected(MqttClient client) {
        return client.isConnected();
    }

    @Override
    protected void disconnectForcibly(MqttClient client) throws Exception {
        client.disconnectForcibly(DISCONNECT_TIMEOUT_MS);
    }

    @Override
    protected void close(MqttClient client) throws Exception {
        client.close();
    }

}
