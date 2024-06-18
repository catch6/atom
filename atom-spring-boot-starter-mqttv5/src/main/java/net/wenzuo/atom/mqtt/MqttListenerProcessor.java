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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Slf4j
@Component
public class MqttListenerProcessor implements BeanPostProcessor {

	private final Map<String, List<MqttMessageListener>> mqttListeners = new HashMap<>();

	public List<MqttMessageListener> getMqttListeners(String id) {
		return mqttListeners.get(id);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, @NonNull String beanName) throws BeansException {
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(MqttListener.class)) {
				MqttListener listener = method.getAnnotation(MqttListener.class);
				String id = listener.id();
				String[] topics = listener.topics();
				int[] qos = listener.qos();
				Assert.notNull(id, "MqttListener.id() must not be null");
				Assert.notNull(topics, "MqttListener.topics() must not be null");
				Assert.notNull(listener.qos(), "MqttListener.qos() must not be null");
				if (topics.length != qos.length) {
					if (qos.length == 1) {
						int qos0 = qos[0];
						qos = new int[topics.length];
						Arrays.fill(qos, qos0);
					} else {
						throw new IllegalArgumentException("MqttListener.qos() must be same length as MqttListener.topics()");
					}
				}
				MqttMessageListener mqttMessageListener = new MqttMessageListener(bean, method, topics, qos);
				mqttListeners.computeIfAbsent(listener.id(), k -> new ArrayList<>()).add(mqttMessageListener);
			}
		}
		return bean;
	}

}
