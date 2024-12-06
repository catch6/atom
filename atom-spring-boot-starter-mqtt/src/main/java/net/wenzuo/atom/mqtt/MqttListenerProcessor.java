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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Slf4j
@Getter
public class MqttListenerProcessor implements BeanPostProcessor {

	private final List<MqttConsumer> consumers;

	public MqttListenerProcessor() {
		consumers = new ArrayList<>();
	}

	@Override
	public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
		Method[] methods = bean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(MqttListener.class)) {
				MqttListener listener = method.getAnnotation(MqttListener.class);
				MqttConsumer consumer = new MqttConsumer(listener.id(), listener.topics(), listener.qos(), (topic, value) -> {
					try {
						method.invoke(bean, topic, value);
					} catch (Exception e) {
						log.error("MQTT invoke error", e);
					}
				});
				consumers.add(consumer);
			}
		}
		return bean;
	}

}
