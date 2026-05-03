package cn.mindit.atom.mqtt.config;

import cn.hutool.core.util.StrUtil;
import cn.mindit.atom.mqtt.MqttConsumer;
import cn.mindit.atom.mqtt.MqttConsumerProcessor;
import cn.mindit.atom.mqtt.MqttSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * MQTT v3/v5 配置类的公共骨架
 *
 * @param <C> MQTT 客户端类型 (mqttv3.MqttClient / mqttv5.client.MqttClient)
 * @author Catch
 * @since 2026-05-03
 */
@Slf4j
public abstract class AbstractMqttConfiguration<C> implements ApplicationListener<ApplicationStartedEvent>, Ordered, DisposableBean {

    protected static final long DISCONNECT_TIMEOUT_MS = 3000L;

    protected final MqttProperties mqttProperties;
    protected final List<MqttSubscriber> mqttSubscribers;
    private final List<C> managedClients = new ArrayList<>();

    private ThreadPoolTaskExecutor asyncExecutor;

    @Value("${spring.application.name:atom}")
    protected String applicationName;
    @Value("${spring.profiles.active:}")
    protected String activeProfile;

    protected AbstractMqttConfiguration(MqttProperties mqttProperties, List<MqttSubscriber> mqttSubscribers) {
        this.mqttProperties = mqttProperties;
        this.mqttSubscribers = mqttSubscribers;
    }

    protected abstract String version();

    protected abstract C createAndConnect(MqttProperties.MqttInstance instance) throws Exception;

    protected abstract void subscribe(C client, MqttConsumer consumer, Executor executor) throws Exception;

    protected abstract String clientId(C client);

    protected abstract boolean isConnected(C client);

    protected abstract void disconnectForcibly(C client) throws Exception;

    protected abstract void close(C client) throws Exception;

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

        List<MqttProperties.MqttInstance> enabledInstances = instances.stream().filter(MqttProperties.MqttInstance::getEnabled).toList();
        for (MqttProperties.MqttInstance instance : enabledInstances) {
            if (StrUtil.isBlank(instance.getClientId())) {
                instance.setClientId(applicationName + "-" + activeProfile);
            }
        }

        List<CompletableFuture<C>> futures = enabledInstances.stream()
                                                             .map(instance -> CompletableFuture.supplyAsync(() -> {
                                                                 try {
                                                                     return createAndConnect(instance);
                                                                 } catch (Exception e) {
                                                                     throw new RuntimeException("MQTT connect error: " + e.getMessage(), e);
                                                                 }
                                                             }))
                                                             .toList();

        for (int i = 0; i < enabledInstances.size(); i++) {
            MqttProperties.MqttInstance instance = enabledInstances.get(i);
            C client = futures.get(i).join();
            managedClients.add(client);
            beanFactory.registerSingleton(MqttProperties.CLIENT_BEAN_PREFIX + instance.getId(), client);

            List<MqttConsumer> consumers = consumerMap.get(instance.getId());
            if (consumers == null || consumers.isEmpty()) {
                continue;
            }
            for (MqttConsumer consumer : consumers) {
                String[] topics = consumer.getTopics();
                if (topics == null || topics.length == 0) {
                    continue;
                }
                try {
                    subscribe(client, consumer, executor);
                } catch (Exception e) {
                    throw new RuntimeException("MQTT subscribe error: " + e.getMessage(), e);
                }
            }
        }
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
        executor.setThreadNamePrefix(config.getThreadNamePrefix() + version() + "-");
        executor.initialize();
        this.asyncExecutor = executor;
        log.info("MQTT {} async executor initialized: core={}, max={}, queue={}",
            version(), config.getCorePoolSize(), config.getMaxPoolSize(), config.getQueueCapacity());
        return executor;
    }

    @Override
    public int getOrder() {
        return mqttProperties.getOrder();
    }

    @Override
    public void destroy() {
        for (C client : managedClients) {
            try {
                if (isConnected(client)) {
                    disconnectForcibly(client);
                }
                close(client);
            } catch (Exception e) {
                log.warn("Failed to close MQTT {} client: {}", version(), clientId(client), e);
            }
        }
        managedClients.clear();
        if (asyncExecutor != null) {
            asyncExecutor.shutdown();
        }
    }

}
