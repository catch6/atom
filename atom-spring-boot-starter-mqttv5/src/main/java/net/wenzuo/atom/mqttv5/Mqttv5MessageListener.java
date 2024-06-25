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

package net.wenzuo.atom.mqttv5;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.paho.mqttv5.client.IMqttMessageListener;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * @author Catch
 * @since 2024-06-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mqttv5MessageListener implements IMqttMessageListener {

	private Object bean;
	private Method method;
	private String[] topics;
	private int[] qos;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		method.invoke(bean, topic, new String(message.getPayload(), StandardCharsets.UTF_8));
	}

}
