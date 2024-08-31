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

package net.wenzuo.atom.opc.ua.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catch
 * @since 2024-06-23
 */
@ConfigurationProperties(prefix = "atom.opc.ua")
@Data
public class OpcUaProperties {

	public static final String CLIENT_BEAN_PREFIX = "opcUaClient-";

	/**
	 * 是否启用
	 */
	private Boolean enabled = true;

	/**
	 * 加载顺序, 默认 Ordered.LOWEST_PRECEDENCE
	 */
	private Integer order = Ordered.LOWEST_PRECEDENCE;

	/**
	 * 证书路径
	 */
	private String certificatePath;

	/**
	 * 实例 ID
	 */
	private String id = "default";

	/**
	 * 服务器地址, 如: opc.tcp://milo.digitalpetri.com:62541/milo
	 */
	private String url;

	/**
	 * 服务器用户名
	 */
	private String username;

	/**
	 * 服务器密码
	 */
	private String password;

	/**
	 * OPC UA 实例配置
	 */
	private List<OpcUaInstance> instances;

	@Data
	public static class OpcUaInstance {

		/**
		 * 实例 ID
		 */
		private String id;
		/**
		 * 是否启用
		 */
		private Boolean enabled = true;

		/**
		 * 服务器地址, 如: opc.tcp://milo.digitalpetri.com:62541/milo
		 */
		private String url;
		/**
		 * 服务器用户名
		 */
		private String username;
		/**
		 * 服务器密码
		 */
		private String password;

	}

	public List<OpcUaInstance> getInstances() {
		List<OpcUaInstance> instances = new ArrayList<>();
		if (id != null && url != null) {
			OpcUaInstance instance = new OpcUaInstance();
			instance.setId(id);
			instance.setEnabled(enabled);
			instance.setUrl(url);
			instance.setUsername(username);
			instance.setPassword(password);
			instances.add(instance);
		}
		if (this.instances != null && !instances.isEmpty()) {
			instances.addAll(this.instances);
		}
		return instances;
	}

}
