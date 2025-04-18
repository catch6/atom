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

/**
 * @author Catch
 * @since 2024-06-25
 */
public interface MqttSubscriber {

	/**
	 * 实例 ID, 为空则使用 MqttProperties.getId()
	 *
	 * @return 实例 ID
	 */
	default String id() {
		return "";
	}

	/**
	 * 订阅主题
	 *
	 * @return 主题列表
	 */
	String[] topics();

	/**
	 * QoS
	 *
	 * @return QoS列表, 如果length为1, 则所有主题的QoS都为第一个值, 默认所有QoS为0
	 */
	default int[] qos() {
		return new int[]{0};
	}

	/**
	 * 订阅消息
	 *
	 * @param topic   主题
	 * @param message 消息
	 */
	void message(String topic, String message);

}
