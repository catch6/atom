package cn.mindit.atom.mqtt.config;

import cn.hutool.core.util.StrUtil;
import cn.mindit.atom.mqtt.MqttConsumer;
import cn.mindit.atom.mqtt.MqttConsumerProcessor;
import cn.mindit.atom.mqtt.MqttSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Catch
 * @since 2024-06-26
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass(MqttClient.class)
@Configuration
public class Mqttv3Configuration implements ApplicationListener<ApplicationStartedEvent>, Ordered, DisposableBean {

    private static final long DISCONNECT_TIMEOUT_MS = 3000L;

    private final MqttProperties mqttProperties;
    private final List<MqttSubscriber> mqttSubscribers;
    private final List<MqttClient> managedClients = new ArrayList<>();

    private ThreadPoolTaskExecutor asyncExecutor;

    @Value("${spring.application.name:-atom}")
    private String applicationName;
    @Value("${spring.profiles.active:-}")
    private String activeProfile;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        List<MqttProperties.MqttInstance> instances = mqttProperties.getInstances();
        if (instances == null || instances.isEmpty()) {
            return;
        }

        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        Map<String, List<MqttConsumer>> consumerMap = MqttConsumerProcessor.processConsumerMap(applicationContext, mqttProperties, mqttSubscribers);

        Executor executor = initExecutor();

        for (MqttProperties.MqttInstance instance : instances) {
            if (!instance.getEnabled()) {
                continue;
            }
            try {
                if (StrUtil.isBlank(instance.getClientId())) {
                    instance.setClientId(applicationName + "-" + activeProfile);
                }

                String[] urls = instance.getUrl().split(",");
                MqttClient mqttClient = new MqttClient(urls[0], instance.getClientId(), new MemoryPersistence());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setServerURIs(urls);
                if (instance.getUsername() != null) {
                    options.setUserName(instance.getUsername());
                }
                if (instance.getPassword() != null) {
                    options.setPassword(instance.getPassword().toCharArray());
                }
                options.setAutomaticReconnect(true);
                mqttClient.connect(options);
                managedClients.add(mqttClient);

                beanFactory.registerSingleton(MqttProperties.CLIENT_BEAN_PREFIX + instance.getId(), mqttClient);

                List<MqttConsumer> consumers = consumerMap.get(instance.getId());
                if (consumers == null || consumers.isEmpty()) {
                    continue;
                }
                for (MqttConsumer consumer : consumers) {
                    String[] topics = consumer.getTopics();
                    if (topics == null || topics.length == 0) {
                        continue;
                    }
                    int[] qos = consumer.getQos();
                    IMqttMessageListener[] listeners = new IMqttMessageListener[topics.length];
                    for (int i = 0; i < topics.length; i++) {
                        listeners[i] = createListener(consumer, executor);
                    }
                    mqttClient.subscribe(topics, qos, listeners);
                }
            } catch (Exception e) {
                throw new RuntimeException("MQTT connect error: " + e.getMessage(), e);
            }
        }
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

    private Executor initExecutor() {
        if (!Boolean.TRUE.equals(mqttProperties.getAsync())) {
            return null;
        }
        MqttProperties.MqttExecutor config = mqttProperties.getExecutor();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getCorePoolSize());
        executor.setMaxPoolSize(config.getMaxPoolSize());
        executor.setQueueCapacity(config.getQueueCapacity());
        executor.setThreadNamePrefix(config.getThreadNamePrefix() + "v3-");
        executor.initialize();
        this.asyncExecutor = executor;
        log.info("MQTT v3 async executor initialized: core={}, max={}, queue={}",
            config.getCorePoolSize(), config.getMaxPoolSize(), config.getQueueCapacity());
        return executor;
    }

    @Override
    public int getOrder() {
        return mqttProperties.getOrder();
    }

    @Override
    public void destroy() {
        for (MqttClient client : managedClients) {
            try {
                if (client.isConnected()) {
                    client.disconnectForcibly(DISCONNECT_TIMEOUT_MS);
                }
                client.close();
            } catch (Exception e) {
                log.warn("Failed to close MQTT v3 client: {}", client.getClientId(), e);
            }
        }
        managedClients.clear();
        if (asyncExecutor != null) {
            asyncExecutor.shutdown();
        }
    }

}
