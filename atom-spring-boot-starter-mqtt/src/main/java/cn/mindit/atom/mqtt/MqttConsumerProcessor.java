package cn.mindit.atom.mqtt;

import cn.mindit.atom.core.util.ExpressionResolverUtils;
import cn.mindit.atom.mqtt.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Catch
 * @since 2024-08-31
 */
@Slf4j
public class MqttConsumerProcessor {

    public static Map<String, List<MqttConsumer>> processConsumerMap(ConfigurableApplicationContext applicationContext, MqttProperties properties, List<MqttSubscriber> subscribers) {
        Map<String, List<MqttConsumer>> consumerMap = new HashMap<>();
        processListener(consumerMap, applicationContext, properties);
        processSubscriber(consumerMap, subscribers, properties);
        return consumerMap;
    }

    private static void processListener(Map<String, List<MqttConsumer>> consumerMap, ConfigurableApplicationContext applicationContext, MqttProperties properties) {
        MqttListenerProcessor mqttListenerProcessor = applicationContext.getBean(MqttListenerProcessor.class);
        List<MqttConsumer> consumers = mqttListenerProcessor.getConsumers();
        if (consumers == null || consumers.isEmpty()) {
            return;
        }

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        for (MqttConsumer consumer : consumers) {
            String id = consumer.getId();
            if (id == null || id.isEmpty()) {
                consumer.setId(properties.getId());
            }
            String[] topics = ExpressionResolverUtils.resolveStringArray(consumer.getTopics(), beanFactory, "MQTT topic");
            consumer.setTopics(topics);
            consumer.initialize();
            List<MqttConsumer> list = consumerMap.computeIfAbsent(consumer.getId(), k -> new ArrayList<>());
            list.add(consumer);
        }
    }

    private static void processSubscriber(Map<String, List<MqttConsumer>> consumerMap, List<MqttSubscriber> subscribers, MqttProperties properties) {
        if (subscribers == null || subscribers.isEmpty()) {
            return;
        }
        for (MqttSubscriber subscriber : subscribers) {
            String id = subscriber.id();
            if (id == null || id.isEmpty()) {
                id = properties.getId();
            }
            String[] topics = subscriber.topics();
            int[] qos = subscriber.qos();
            MqttConsumer consumer = new MqttConsumer(id, topics, qos, (topic, message) -> {
                try {
                    subscriber.message(topic, message);
                } catch (Exception e) {
                    log.error("MQTT invoke error", e);
                }
            });
            consumer.initialize();
            List<MqttConsumer> list = consumerMap.computeIfAbsent(id, k -> new ArrayList<>());
            list.add(consumer);
        }
    }

}
