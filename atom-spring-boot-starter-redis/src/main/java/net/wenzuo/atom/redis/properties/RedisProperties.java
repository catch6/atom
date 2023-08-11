/*
 * Copyright (c) 2022-2023 Catch(catchlife6@163.com).
 * Atom is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package net.wenzuo.atom.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Catch
 * @since 2023-06-06
 */
@Data
@ConfigurationProperties(prefix = "atom.redis")
public class RedisProperties {

	/**
	 * 是否启用 redis 模块
	 */
	private Boolean enabled = true;

	/**
	 * 是否启用 redisTemplate
	 */
	private Boolean redisTemplate = true;

	/**
	 * 是否启用 cacheManager
	 */
	private Boolean cacheManager = true;

	/**
	 * 是否启用 RedisService
	 */
	private Boolean redisService = true;

}
