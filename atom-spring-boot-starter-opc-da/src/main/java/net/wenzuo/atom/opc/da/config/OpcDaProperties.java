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

import java.util.List;

/**
 * @author Catch
 * @since 2024-06-16
 */
@ConfigurationProperties(prefix = "atom.opc.da")
@Data
public class OpcDaProperties {

	/**
	 * 是否启用
	 */
	private Boolean enabled = true;

	/**
	 * Bean 前缀
	 */
	private String beanPrefix = "opcDaClient-";

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
		private String host = "127.0.0.1";
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

	}

}
