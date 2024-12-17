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

package net.wenzuo.atom.mqtt.config;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.wenzuo.atom.mqtt.MqttConsumer;
import net.wenzuo.atom.mqtt.MqttConsumerProcessor;
import net.wenzuo.atom.mqtt.MqttSubscriber;
import org.eclipse.paho.mqttv5.client.IMqttMessageListener;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author Catch
 * @since 2024-06-26
 */
@RequiredArgsConstructor
@ConditionalOnClass(MqttClient.class)
@Configuration
public class Mqttv5Configuration implements ApplicationListener<ApplicationStartedEvent>, Ordered {

    private final MqttProperties mqttProperties;
    private final List<MqttSubscriber> mqttSubscribers;

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

        for (MqttProperties.MqttInstance instance : instances) {
            if (!instance.getEnabled()) {
                continue;
            }
            try {
                if (StrUtil.isBlank(instance.getClientId())) {
                    String suffix = RandomUtil.randomString(6);
                    instance.setClientId(applicationName + "-" + activeProfile + "-" + suffix);
                }
                String[] urls = instance.getUrl().split(",");
                MqttClient mqttClient = new MqttClient(urls[0], instance.getClientId(), new MemoryPersistence());
                MqttConnectionOptions options = new MqttConnectionOptions();
                options.setServerURIs(urls);
                if (instance.getUsername() != null) {
                    options.setUserName(instance.getUsername());
                }
                if (instance.getPassword() != null) {
                    options.setPassword(instance.getPassword().getBytes(StandardCharsets.UTF_8));
                }
                options.setAutomaticReconnect(true);
                mqttClient.connect(options);

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
                    MqttSubscription[] subscriptions = new MqttSubscription[topics.length];
                    IMqttMessageListener[] listeners = new IMqttMessageListener[topics.length];
                    for (int i = 0; i < topics.length; i++) {
                        subscriptions[i] = new MqttSubscription(topics[i], qos[i]);
                        listeners[i] = (topic, message) -> consumer.getConsumer().accept(topic, new String(message.getPayload(), StandardCharsets.UTF_8));
                    }
                    mqttClient.subscribe(subscriptions, listeners);
                }
            } catch (Exception e) {
                throw new RuntimeException("MQTT connect error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public int getOrder() {
        return mqttProperties.getOrder();
    }

}
