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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Catch
 * @since 2024-06-16
 */
@ConfigurationProperties(prefix = "atom.mqttv5")
@Data
public class Mqttv5Properties {

	/**
	 * 是否启用
	 */
	private Boolean enabled = true;

	/**
	 * MQTT 实例配置
	 */
	private List<MqttInstance> instances;

	@Data
	public static class MqttInstance {

		/**
		 * 实例 ID
		 */
		private String id;
		/**
		 * 是否启用
		 */
		private Boolean enabled = true;
		/**
		 * MQTT 服务器地址
		 */
		private String url;
		/**
		 * MQTT 服务器用户名
		 */
		private String username;
		/**
		 * MQTT 服务器密码
		 */
		private String password;
		/**
		 * MQTT 客户端 ID
		 */
		private String clientId;

	}

}
