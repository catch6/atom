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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.wenzuo.atom.mqtt.MqttListenerProcessor;
import net.wenzuo.atom.mqtt.MqttListenerSubscriber;
import net.wenzuo.atom.mqtt.MqttSubscriber;
import org.eclipse.paho.mqttv5.client.IMqttMessageListener;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Catch
 * @since 2024-06-26
 */
@RequiredArgsConstructor
@ConditionalOnClass(MqttClient.class)
@Configuration
public class Mqttv5Configuration implements ApplicationListener<ApplicationStartedEvent> {

	private final MqttProperties mqttProperties;
	private final List<MqttSubscriber> mqttSubscribers;

	@Override
	public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
		ConfigurableApplicationContext applicationContext = event.getApplicationContext();
		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		Map<String, List<MqttSubscriberWrapper>> subscriberMap = new HashMap<>();
		processListener(subscriberMap, applicationContext);
		processSubscriber(subscriberMap, mqttSubscribers);

		List<MqttProperties.MqttInstance> instances = mqttProperties.getInstances();
		for (MqttProperties.MqttInstance instance : instances) {
			if (!instance.getEnabled()) {
				continue;
			}
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

				List<MqttSubscriberWrapper> wrappers = subscriberMap.get(instance.getId());
				if (wrappers != null) {
					MqttSubscription[] subscriptions = new MqttSubscription[wrappers.size()];
					IMqttMessageListener[] listeners = new IMqttMessageListener[wrappers.size()];
					for (int i = 0; i < wrappers.size(); i++) {
						MqttSubscriberWrapper wrapper = wrappers.get(i);
						subscriptions[i] = new MqttSubscription(wrapper.getTopic(), wrapper.getQos());
						listeners[i] = wrapper.getListener();
					}
					mqttClient.subscribe(subscriptions, listeners);
				}
				beanFactory.registerSingleton(mqttProperties.getBeanPrefix() + instance.getId(), mqttClient);
			} catch (MqttException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void processListener(Map<String, List<MqttSubscriberWrapper>> subscriberMap, ConfigurableApplicationContext applicationContext) {
		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		BeanExpressionContext expressionContext = new BeanExpressionContext(beanFactory, null);
		BeanExpressionResolver expressionResolver = beanFactory.getBeanExpressionResolver();

		MqttListenerProcessor processor = applicationContext.getBean(MqttListenerProcessor.class);
		List<MqttListenerSubscriber> subscribers = processor.getSubscribers();

		for (MqttListenerSubscriber subscriber : subscribers) {
			String id = subscriber.getId();
			String[] topics = subscriber.getTopics();
			int[] qos = subscriber.getQos();
			Assert.notNull(id, "mqtt id must not be null");
			Assert.notEmpty(topics, "mqtt topics must not be empty");
			Assert.notNull(qos, "mqtt qos must not be null");

			if (expressionResolver != null) {
				List<String> newTopics = new ArrayList<>();
				for (String topic : topics) {
					Object object = expressionResolver.evaluate(beanFactory.resolveEmbeddedValue(topic), expressionContext);
					if (object == null) {
						throw new IllegalArgumentException("mqtt topic must not be null");
					}
					if (object instanceof String str) {
						newTopics.add(str);
					} else if (object instanceof String[] strs) {
						for (String str : strs) {
							if (str == null) {
								throw new IllegalArgumentException("mqtt topic must not be null");
							}
							newTopics.add(str);
						}
					} else {
						throw new IllegalArgumentException("mqtt topic must be String or String[]");
					}
				}
				topics = newTopics.toArray(new String[0]);
			}
			Assert.isTrue(qos.length == 1 || qos.length == topics.length, "mqtt qos length must be 1 or equal to topics length");
			if (qos.length == 1) {
				int qos0 = qos[0];
				qos = new int[topics.length];
				Arrays.fill(qos, qos0);
			}
			Object bean = subscriber.getBean();
			Method method = subscriber.getMethod();
			IMqttMessageListener listener = (topic, message) -> {
				method.invoke(bean, topic, new String(message.getPayload(), StandardCharsets.UTF_8));
			};
			fillSubscriberMap(subscriberMap, id, topics, qos, listener);
		}
	}

	private void processSubscriber(Map<String, List<MqttSubscriberWrapper>> subscriberMap, List<MqttSubscriber> mqttSubscribers) {
		if (mqttSubscribers == null) {
			return;
		}
		for (MqttSubscriber subscriber : mqttSubscribers) {
			String id = subscriber.id();
			String[] topics = subscriber.topics();
			int[] qos = subscriber.qos();
			Assert.notNull(id, "mqtt id must not be null");
			Assert.notEmpty(topics, "mqtt topics must not be empty");
			Assert.notNull(qos, "mqtt qos must not be null");
			if (topics.length != qos.length) {
				Assert.isTrue(qos.length == 1, "mqtt qos length must be 1 or equal to topics length");
				int qos0 = qos[0];
				qos = new int[topics.length];
				Arrays.fill(qos, qos0);
			}
			IMqttMessageListener listener = (topic, message) -> {
				subscriber.message(topic, new String(message.getPayload(), StandardCharsets.UTF_8));
			};
			fillSubscriberMap(subscriberMap, id, topics, qos, listener);
		}
	}

	private static void fillSubscriberMap(Map<String, List<MqttSubscriberWrapper>> subscriberMap, String id, String[] topics, int[] qos, IMqttMessageListener listener) {
		List<MqttSubscriberWrapper> wrappers = subscriberMap.computeIfAbsent(id, k -> new ArrayList<>());
		for (int i = 0; i < topics.length; i++) {
			String topic = topics[i];
			int qos0 = qos[i];
			MqttSubscriberWrapper wrapper = new MqttSubscriberWrapper(topic, qos0, listener);
			wrappers.add(wrapper);
		}
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class MqttSubscriberWrapper {

		private String topic;
		private int qos;
		private IMqttMessageListener listener;

	}

}
