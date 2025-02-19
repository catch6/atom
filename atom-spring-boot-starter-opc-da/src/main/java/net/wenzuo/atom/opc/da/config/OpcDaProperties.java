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

package net.wenzuo.atom.opc.da.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catch
 * @since 2024-06-16
 */
@ConfigurationProperties(prefix = "atom.opc.da")
@Data
public class OpcDaProperties {

	public static final String CLIENT_BEAN_PREFIX = "opcDaClient-";
	public static final String CONNECTION_BEAN_PREFIX = "opcDaConnection-";

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
	 * 服务器主机
	 */
	private String host;
	/**
	 * 服务器域
	 */
	private String domain = "";
	/**
	 * 服务器用户
	 */
	private String user;
	/**
	 * 服务器密码
	 */
	private String password;

	/**
	 * 服务器 ProgID
	 */
	private String progId;
	/**
	 * 服务器 ClsID, 留空则使用 ProgID 获取
	 */
	private String clsId;
	/**
	 * 轮询周期,单位毫秒, 默认 1 秒
	 */
	private int period = 1000;

	/**
	 * 是否异步
	 */
	private boolean async = true;

	/**
	 * 初始化获取全量数据, 仅在 async 为 true 时有效
	 */
	private Boolean initialRefresh = false;

	/**
	 * OPC DA 实例配置
	 */
	private List<OpcDaInstance> instances;

	@Data
	public static class OpcDaInstance {

		/**
		 * 实例 ID
		 */
		private String id;
		/**
		 * 是否启用
		 */
		private Boolean enabled = true;
		/**
		 * 服务器主机
		 */
		private String host;
		/**
		 * 服务器域
		 */
		private String domain = "";
		/**
		 * 服务器用户
		 */
		private String user;
		/**
		 * 服务器密码
		 */
		private String password;

		/**
		 * 服务器 ProgID
		 */
		private String progId;
		/**
		 * 服务器 ClsID
		 */
		private String clsId;
		/**
		 * 轮询周期,单位毫秒, 默认 1 秒
		 */
		private int period = 1000;

		/**
		 * 是否异步
		 */
		private boolean async = true;

		/**
		 * 初始化获取全量数据, 仅在 async 为 true 时有效
		 */
		private Boolean initialRefresh = false;

	}

	public List<OpcDaInstance> getInstances() {
		List<OpcDaInstance> instances = new ArrayList<>();
		if (id != null && host != null) {
			OpcDaInstance instance = new OpcDaInstance();
			instance.setId(id);
			instance.setEnabled(enabled);
			instance.setHost(host);
			instance.setDomain(domain);
			instance.setUser(user);
			instance.setPassword(password);
			instance.setProgId(progId);
			instance.setClsId(clsId);
			instance.setPeriod(period);
			instance.setAsync(async);
			instance.setInitialRefresh(initialRefresh);
			instances.add(instance);
		}
		if (this.instances != null && !instances.isEmpty()) {
			instances.addAll(this.instances);
		}
		return instances;
	}

}
