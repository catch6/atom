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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catch
 * @since 2024-06-16
 */
@ConfigurationProperties(prefix = "atom.mqtt")
@Data
public class MqttProperties {

	public static final String CLIENT_BEAN_PREFIX = "mqttClient-";

	/**
	 * 是否启用
	 */
	private Boolean enabled = true;

	/**
	 * 加载顺序, 默认 Ordered.LOWEST_PRECEDENCE
	 */
	private Integer order = Ordered.LOWEST_PRECEDENCE;

	/**
	 * 实例 ID
	 */
	private String id = "default";
	/**
	 * 实例服务器地址
	 */
	private String url;
	/**
	 * 实例服务器用户名
	 */
	private String username;
	/**
	 * 实例服务器密码
	 */
	private String password;
	/**
	 * 实例客户端 ID, 默认为 ${spring.application.name}-${spring.profiles.active}-随机6位字符
	 */
	private String clientId;

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
		 * 实例是否启用
		 */
		private Boolean enabled = true;
		/**
		 * 实例服务器地址
		 */
		private String url;
		/**
		 * 实例服务器用户名
		 */
		private String username;
		/**
		 * 实例服务器密码
		 */
		private String password;
		/**
		 * 实例客户端 ID, 默认为 ${spring.application.name}-${spring.profiles.active}-随机6位字符
		 */
		private String clientId;

	}

	public List<MqttInstance> getInstances() {
		List<MqttInstance> instances = new ArrayList<>();
		if (id != null && url != null) {
			MqttInstance instance = new MqttInstance();
			instance.setId(id);
			instance.setEnabled(enabled);
			instance.setUrl(url);
			instance.setUsername(username);
			instance.setPassword(password);
			instance.setClientId(clientId);
			instances.add(instance);
		}
		if (this.instances != null && !instances.isEmpty()) {
			instances.addAll(this.instances);
		}
		return instances;
	}

}
