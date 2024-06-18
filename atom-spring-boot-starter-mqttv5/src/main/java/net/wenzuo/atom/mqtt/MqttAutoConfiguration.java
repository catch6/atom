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

package net.wenzuo.atom.mqtt;

import lombok.RequiredArgsConstructor;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author Catch
 * @since 2024-06-18
 */
@RequiredArgsConstructor
@ComponentScan("net.wenzuo.atom.mqtt")
@EnableConfigurationProperties(MqttProperties.class)
@ConditionalOnProperty(value = "atom.mqtt.enabled", matchIfMissing = true)
public class MqttAutoConfiguration implements ApplicationListener<ApplicationStartedEvent> {

	private final MqttProperties mqttProperties;

	@Override
	public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
		ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();
		List<MqttProperties.MqttInstance> instances = mqttProperties.getInstances();
		for (MqttProperties.MqttInstance instance : instances) {
			String id = instance.getId();
			String[] urls = instance.getUrl().split(",");
			try {
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
				MqttListenerProcessor processor = beanFactory.getBean(MqttListenerProcessor.class);
				List<MqttMessageListener> listeners = processor.getMqttListeners(id);
				if (listeners != null) {
					for (MqttMessageListener listener : listeners) {
						String[] topics = listener.getTopics();
						int[] qos = listener.getQos();
						MqttSubscription[] subscriptions = new MqttSubscription[topics.length];
						for (int i = 0; i < topics.length; i++) {
							subscriptions[i] = new MqttSubscription(topics[i], qos[i]);
						}
						MqttMessageListener[] mqttMessageListeners = new MqttMessageListener[topics.length];
						Arrays.fill(mqttMessageListeners, listener);
						mqttClient.subscribe(subscriptions, mqttMessageListeners);
					}
				}
				beanFactory.registerSingleton("mqttClient-" + id, mqttClient);
			} catch (MqttException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
