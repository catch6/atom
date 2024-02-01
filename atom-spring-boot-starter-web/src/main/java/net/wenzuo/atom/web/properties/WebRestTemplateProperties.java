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

package net.wenzuo.atom.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author Catch
 * @since 2023-11-24
 */
@Data
@ConfigurationProperties(prefix = "atom.web.rest-template")
public class WebRestTemplateProperties {

	/**
	 * 是否启用 RestTemplate
	 */
	private final boolean enabled = true;

	/**
	 * 建立连接的超时时间
	 */
	private Duration connectTimeout = Duration.ofSeconds(5);

	/**
	 * 从连接池获取到连接的超时时间
	 */
	private Duration connectionRequestTimeout = Duration.ofSeconds(5);

}
