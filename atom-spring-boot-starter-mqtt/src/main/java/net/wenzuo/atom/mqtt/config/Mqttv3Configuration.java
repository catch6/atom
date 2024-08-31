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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.mqtt.MqttConsumer;
import net.wenzuo.atom.mqtt.MqttConsumerProcessor;
import net.wenzuo.atom.mqtt.MqttSubscriber;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
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
@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass(MqttClient.class)
@Configuration
public class Mqttv3Configuration implements ApplicationListener<ApplicationStartedEvent>, Ordered {

	private final MqttProperties mqttProperties;
	private final List<MqttSubscriber> mqttSubscribers;

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
						listeners[i] = (topic, message) -> consumer.getConsumer().accept(topic, new String(message.getPayload(), StandardCharsets.UTF_8));
					}
					mqttClient.subscribe(topics, qos, listeners);
				}
				beanFactory.registerSingleton(MqttProperties.CLIENT_BEAN_PREFIX + instance.getId(), mqttClient);
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
