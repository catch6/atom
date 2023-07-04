/*
 * Copyright (c) 2022-2023 Catch
 * [Atom] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.web.cors")
public class CorsProperties {

	/**
	 * 是否启用 CORS
	 */
	private Boolean enabled = true;

	private List<Config> configs = Collections.singletonList(new Config());

	@Data
	public static class Config {

		private static final String ALL = "*";

		private static final List<String> ALL_LIST = Collections.singletonList(ALL);

		/**
		 * 是否配置全局跨域支持
		 */
		private String pattern = "/**";

		/**
		 * 是否允许 Credentials , 默认 false
		 */
		private Boolean allowCredentials = false;

		/**
		 * 允许的 Origins, 默认 ["*"], 生产环境建议设置为具体值，可以为多个
		 */
		private List<String> allowedOrigins = ALL_LIST;

		/**
		 * 允许的 Origins Pattern, 默认 null, 生产环境建议设置为具体值，可以为多个
		 */
		private List<String> allowedOriginPatterns;

		/**
		 * 允许的 Headers, 默认 ["*"], 生产环境建议设置为具体值，可以为多个
		 */
		private List<String> allowedHeaders = ALL_LIST;

		/**
		 * 允许的 Methods, 默认 ["*"], 生产环境建议设置为具体值，可以为多个
		 */
		private List<String> allowedMethods = ALL_LIST;

		/**
		 * 允许客户端读取的 Headers, 默认 ["*"]
		 */
		private List<String> exposedHeaders = ALL_LIST;

	}

}
