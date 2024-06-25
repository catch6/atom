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

package net.wenzuo.atom.mqttv5.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.atom.mqttv5.service.Mqttv5Service;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author Catch
 * @since 2024-06-18
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class Mqttv5ServiceImpl implements Mqttv5Service {

	public static final int DEFAULT_QOS = 1;
	public static final boolean DEFAULT_RETAINED = true;

	private final ApplicationContext applicationContext;

	@Override
	public void send(String id, String topic, String message) {
		send(id, topic, message, DEFAULT_QOS, DEFAULT_RETAINED);
	}

	@Override
	public void send(String id, String topic, String message, int qos) {
		send(id, topic, message, qos, DEFAULT_RETAINED);
	}

	@Override
	public void send(String id, String topic, String message, boolean retained) {
		send(id, topic, message, DEFAULT_QOS, retained);
	}

	@Override
	public void send(String id, String topic, String message, int qos, boolean retained) {
		if (log.isDebugEnabled()) {
			log.debug("Mqtt Send: id={}, topic={}, qos: {}, retained: {}, message={}", id, topic, qos, retained, message);
		}
		try {
			applicationContext.getBean("mqttv5Client-" + id, MqttClient.class)
							  .publish(topic, message.getBytes(), qos, retained);
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

}
