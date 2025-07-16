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

package cn.mindit.atom.mqtt;

import lombok.extern.slf4j.Slf4j;
import cn.mindit.atom.mqtt.config.MqttProperties;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
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
        BeanExpressionContext expressionContext = new BeanExpressionContext(beanFactory, null);
        BeanExpressionResolver expressionResolver = beanFactory.getBeanExpressionResolver();

        for (MqttConsumer consumer : consumers) {
            String id = consumer.getId();
            if (id == null || id.isEmpty()) {
                consumer.setId(properties.getId());
            }
            String[] topics = processExpression(consumer.getTopics(), beanFactory, expressionContext, expressionResolver);
            consumer.setTopics(topics);
            consumer.initialize();
            List<MqttConsumer> list = consumerMap.computeIfAbsent(consumer.getId(), k -> new ArrayList<>());
            list.add(consumer);
        }
    }

    private static String[] processExpression(String[] topics, ConfigurableListableBeanFactory beanFactory, BeanExpressionContext expressionContext, BeanExpressionResolver expressionResolver) {
        if (topics == null || topics.length == 0) {
            return null;
        }

        if (expressionResolver != null) {
            List<String> newTopics = new ArrayList<>();
            for (String topic : topics) {
                Object object = expressionResolver.evaluate(beanFactory.resolveEmbeddedValue(topic), expressionContext);
                if (object == null) {
                    throw new IllegalArgumentException("MQTT topic must not be null");
                }
                if (object instanceof String str) {
                    newTopics.add(str);
                } else if (object instanceof String[] strs) {
                    for (String str : strs) {
                        if (str == null) {
                            throw new IllegalArgumentException("MQTT topic must not be null");
                        }
                        newTopics.add(str);
                    }
                } else {
                    throw new IllegalArgumentException("MQTT topic must be String or String[]");
                }
            }
            topics = newTopics.toArray(new String[0]);
        }
        return topics;
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
